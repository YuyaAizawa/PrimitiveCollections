package com.lethe_river.util.primitive;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.RandomAccess;
import java.util.StringJoiner;

/**
 * IntListのスケルトン
 * @author YuyaAizawa
 *
 */
public abstract class AbstractIntList extends AbstractIntCollection implements IntList {

	/**
	 * 構造的変更検知用
	 *  add, removeなどの際に増加させる
	 */
	protected transient int modCount = 0;

	/**
	 * 基底のコンストラクタ
	 */
	protected AbstractIntList() {}

	@Override
	public IntListIterator listIterator() {
		return listIterator(0);
	}

	@Override
	public abstract IntListIterator listIterator(int index);

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
		if(!(obj instanceof IntList)) {
			return false;
		}
		IntList list = (IntList) obj;
		if(list.size() != this.size()) {
			return false;
		}
		PrimitiveIterator.OfInt i = this.iterator();
		PrimitiveIterator.OfInt j = list.iterator();
		while(i.hasNext()) {
			if(i.nextInt() != j.nextInt()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public PrimitiveIterator.OfInt iterator() {
		return listIterator();
	}

	@Override
	public PrimitiveIterator.OfInt reversedIterator() {
		return new PrimitiveIterator.OfInt() {
			IntListIterator i = listIterator(size());

			@Override
			public boolean hasNext() {
				return i.hasPrevious();
			}

			@Override
			public int nextInt() {
				return i.previousInt();
			}

			@Override
			public void remove() {
				i.remove();
			}
		};
	}

	@Override
	public void insert(int index, int element) {
		IntListIterator i = listIterator(index);
		i.add(element);
	}

	@Override
	public void insertAll(int index, int[] is, int offset, int length) {
		PrimitiveSupport.checkBounds(is, offset, length);

		IntListIterator i = listIterator(index);
		for (int j = 0; j < length; j++) {
			i.add(is[offset+j]);
		}
	}

	@Override
	public void insertAll(int index, IntCollection c) {
		IntListIterator itr = listIterator(index);
		c.forEach((int i) -> itr.add(i));
	}

	@Override
	public int removeAt(int index) {
		if(index < 0 || size() <= index) {
			throw new IndexOutOfBoundsException(index);
		}
		IntListIterator i = listIterator(index);
		int removed = i.nextInt();
		i.remove();
		return removed;
	}

	@Override
	public int get(int index) {
		if(index < 0 || size() <= index) {
			throw new IndexOutOfBoundsException(index);
		}
		IntListIterator i = listIterator(index);
		return i.nextInt();
	}

	@Override
	public int set(int index, int element) {
		IntListIterator i = listIterator(index);
		try {
			int removed = i.nextInt();
			i.set(element);
			return removed;
		} catch (NoSuchElementException e) {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public boolean add(int i) {
		insert(size(), i);
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		PrimitiveIterator.OfInt i = iterator();
		while(i.hasNext()) {
			hash = hash*31 + i.nextInt();
		}
		return hash;
	}

	@Override
	public int indexOf(int element) {
		IntListIterator i = listIterator();
		while(i.hasNext()) {
			if(i.nextInt() == element) {
				return i.previousIndex();
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(int element) {
		IntListIterator i = listIterator(size());
		while(i.hasPrevious()) {
			if(i.previousInt() == element) {
				return i.nextIndex();
			}
		}
		return -1;
	}

	@Override
	public IntList subList(int fromIndex, int toIndex) {
		if(this instanceof RandomAccess) {
			return new RandomAccessSubList(this, fromIndex, toIndex);
		} else {
			return new SubList(this, fromIndex, toIndex);
		}
	}

	@Override
	public List<Integer> boxedView() {
		return PrimitiveSupport.boxed(this);
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "<", ">");
		forEach(i -> sj.add(Integer.toString(i)));
		return sj.toString();
	}
}
class SubList extends AbstractIntList {
	private final AbstractIntList original;
	private final int offset;
	private int size;

	public SubList(AbstractIntList original, int fromIndex, int toIndex) {
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
	public int get(int index) {
		checkRange(index);
		checkModCount();
		return super.get(index+offset);
	}

	@Override
	public int set(int index, int element) {
		checkRange(index);
		checkModCount();
		return original.set(index+offset, element);
	}

	@Override
	public void insert(int index, int element) {
		checkRange(index);
		checkModCount();
		original.insert(index+offset, element);
		modCount = original.modCount;
		size++;
	}

	@Override
	public int removeAt(int index) {
		checkRange(index);
		checkModCount();
		int removed = original.removeAt(index+offset);
		modCount = original.modCount;
		size--;
		return removed;
	}

	@Override
	public IntListIterator listIterator(int index) {
		if(index < 0 || size < index) {
			throw new IndexOutOfBoundsException("index: "+index+", size: "+size);
		}
		checkModCount();

		return new IntListIterator() {
			private final IntListIterator i = original.listIterator(index+offset);

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
			public void set(int e) {
				i.set(e);
			}

			@Override
			public int previousInt() {
				if(hasPrevious()) {
					return i.previousInt();
				} else {
					throw new NoSuchElementException();
				}
			}

			@Override
			public int nextInt() {
				if(hasNext()) {
					return i.nextInt();
				} else {
					throw new NoSuchElementException();
				}
			}

			@Override
			public void add(int e) {
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

class RandomAccessSubList extends SubList implements RandomAccess {

	public RandomAccessSubList(AbstractIntList original, int fromIndex, int toIndex) {
		super(original, fromIndex, toIndex);
	}
	@Override
	public IntList subList(int fromIndex, int toIndex) {
		return new RandomAccessSubList(this, fromIndex, toIndex);
	}
}

