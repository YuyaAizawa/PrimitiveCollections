package com.lethe_river.util.primitive;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.PrimitiveIterator.OfInt;
import java.util.RandomAccess;
import java.util.StringJoiner;

/**
 * ByteListのスケルトン
 * @author YuyaAizawa
 *
 */
public abstract class AbstractByteList extends AbstractByteCollection implements ByteList {
	/**
	 * 構造的変更検知用
	 *  add, removeなどの際に増加させる
	 */
	protected transient int modCount = 0;

	/**
	 * 基底のコンストラクタ
	 */
	protected AbstractByteList() {}

	@Override
	public ByteListIterator listIterator() {
		return listIterator(0);
	}

	@Override
	public abstract ByteListIterator listIterator(int index);

	@Override
	public abstract int size();

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof ByteList)) {
			return false;
		}
		ByteList list = (ByteList) obj;
		if(list.size() != this.size()) {
			return false;
		}
		ByteIterator i = this.iterator();
		ByteIterator j = list.iterator();
		while(i.hasNext()) {
			if(i.nextByte() != j.nextByte()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ByteIterator iterator() {
		return listIterator();
	}

	@Override
	public ByteIterator reversedIterator() {
		return new ByteIterator() {
			ByteListIterator i = listIterator(size());

			@Override
			public boolean hasNext() {
				return i.hasPrevious();
			}

			@Override
			public byte nextByte() {
				return i.previousByte();
			}

			@Override
			public void remove() {
				i.remove();
			}
		};
	}

	@Override
	public PrimitiveIterator.OfInt reversedIteratorSigned() {
		return new PrimitiveIterator.OfInt() {
			private final ByteIterator i = reversedIterator();

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public int nextInt() {
				return i.nextSigned();
			}

			@Override
			public void remove() {
				i.remove();
			}
		};
	}

	@Override
	public OfInt reversedIteratorUnsigned() {
		return new PrimitiveIterator.OfInt() {
			private final ByteIterator i = reversedIterator();

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public int nextInt() {
				return i.nextUnsigned();
			}

			@Override
			public void remove() {
				i.remove();
			}
		};
	}

	@Override
	public void insert(int index, byte element) {
		ByteListIterator i = listIterator(index);
		i.add(element);
	}

	@Override
	public void insertAll(int index, byte[] bs, int offset, int length) {
		PrimitiveSupport.checkBounds(bs, offset, length);

		ByteListIterator i = listIterator(index);
		for (int j = 0; j < length; j++) {
			i.add(bs[offset+j]);
		}
	}

	@Override
	public void insertAll(int index, ByteCollection c) {
		ByteListIterator itr = listIterator(index);
		c.forEach((byte b) -> itr.add(b));
	}

	@Override
	public byte removeAt(int index) {
		if(index < 0 || size() <= index) {
			throw new IndexOutOfBoundsException(index);
		}
		ByteListIterator i = listIterator(index);
		byte removed = i.nextByte();
		i.remove();
		return removed;
	}

	@Override
	public byte get(int index) {
		if(index < 0 || size() <= index) {
			throw new IndexOutOfBoundsException(index);
		}
		ByteListIterator i = listIterator(index);
		return i.nextByte();
	}

	@Override
	public byte set(int index, byte element) {
		ByteListIterator i = listIterator(index);
		try {
			byte removed = i.nextByte();
			i.set(element);
			return removed;
		} catch (NoSuchElementException e) {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public boolean add(byte i) {
		insert(size(), i);
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		ByteIterator i = iterator();
		while(i.hasNext()) {
			hash = hash*31 + i.nextByte();
		}
		return hash;
	}

	@Override
	public int indexOf(byte element) {
		ByteListIterator i = listIterator();
		while(i.hasNext()) {
			if(i.nextByte() == element) {
				return i.previousIndex();
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(byte element) {
		ByteListIterator i = listIterator(size());
		while(i.hasPrevious()) {
			if(i.previousByte() == element) {
				return i.nextIndex();
			}
		}
		return -1;
	}

	@Override
	public ByteList subList(int fromIndex, int toIndex) {
		if(this instanceof RandomAccess) {
			return new RandomAccessSubList(this, fromIndex, toIndex);
		}
		return new SubList(this, fromIndex, toIndex);
	}

	@Override
	public List<Byte> boxedView() {
		return PrimitiveSupport.boxed(this);
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "<", ">");
		forEach(b -> sj.add(Byte.toString(b)));
		return super.toString();
	}

	private static class SubList extends AbstractByteList {
		private final AbstractByteList original;
		private final int offset;
		private int size;

		SubList(AbstractByteList original, int fromIndex, int toIndex) {
			if(fromIndex < 0) {
				throw new IndexOutOfBoundsException("fromIndex: "+fromIndex);
			}
			if(toIndex > original.size()) {
				throw new IndexOutOfBoundsException("toIndex: "+fromIndex);
			}
			if(fromIndex > toIndex) {
				throw new IllegalArgumentException("fromIndex: "+fromIndex+", toIndex:"+toIndex);
			}
			this.original = original;
			offset = fromIndex;
			size = toIndex - fromIndex;
			modCount = original.modCount;
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public byte get(int index) {
			checkRange(index);
			checkModCount();
			return super.get(index+offset);
		}

		@Override
		public byte set(int index, byte element) {
			checkRange(index);
			checkModCount();
			return original.set(index+offset, element);
		}

		@Override
		public void insert(int index, byte element) {
			checkRange(index);
			checkModCount();
			original.insert(index+offset, element);
			modCount = original.modCount;
			size++;
		}

		@Override
		public byte removeAt(int index) {
			checkRange(index);
			checkModCount();
			byte removed = original.removeAt(index+offset);
			modCount = original.modCount;
			size--;
			return removed;
		}

		@Override
		public ByteListIterator listIterator(int index) {
			if(index < 0 || size < index) {
				throw new IndexOutOfBoundsException("index: "+index+", size: "+size);
			}
			checkModCount();

			return new ByteListIterator() {
				private final ByteListIterator i = original.listIterator(index+offset);

				@Override
				public void remove() {
					i.remove();
					SubList.this.modCount = original.modCount;
					size--;
				}

				@Override
				public int previousIndex() {
					return i.previousIndex() - offset;
				}

				@Override
				public int nextIndex() {
					return i.nextIndex() - offset;
				}

				@Override
				public boolean hasPrevious() {
					return previousIndex() >= 0;
				}

				@Override
				public boolean hasNext() {
					return nextIndex() < size;
				}

				@Override
				public void set(byte e) {
					i.set(e);
				}

				@Override
				public byte previousByte() {
					if(hasPrevious()) {
						return i.previousByte();
					}
					throw new NoSuchElementException();
				}

				@Override
				public byte nextByte() {
					if(hasNext()) {
						return i.nextByte();
					}
					throw new NoSuchElementException();
				}

				@Override
				public void add(byte e) {
					i.add(e);
					SubList.this.modCount = original.modCount;
					size++;
				}
			};
		}

		private void checkModCount() {
			if(modCount != original.modCount) {
				throw new ConcurrentModificationException();
			}
		}

		private void checkRange(int index) {
			if(index < 0 || size <= index) {
				throw new IndexOutOfBoundsException("index: "+index+", size: "+size);
			}
		}
	}

	private static class RandomAccessSubList extends SubList implements RandomAccess {
		RandomAccessSubList(AbstractByteList original, int fromIndex, int toIndex) {
			super(original, fromIndex, toIndex);
		}
	}
}
