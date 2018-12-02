package com.lethe_river.util.primitive;

import java.io.Serializable;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.RandomAccess;


/**
 * 配列をベースにしたbyte値を格納するPrimitiveList
 *
 * @author YuyaAizawa
 *
 */
public final class ArrayByteList extends AbstractByteList implements RandomAccess {

	private static final long serialVersionUID = -1268344803729165353L;

	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE >> 1;

	private static final byte[] EMPTY_ELEMENTS = {};
	private static final byte[] DEFAULT_CAPACITY_EMPTY_ELEMENTS = {};

	private static final int DEFAULT_CAPACITY = 10;

	// リストの内容
	private byte[] field;

	private int size;

	public static ArrayByteList of(byte... bs) {
		ArrayByteList result = new ArrayByteList(bs.length);
		for (int i = 0; i < bs.length; i++) {
			result.add(bs[i]);
		}
		return result;
	}

	public static ArrayByteList of(ByteList bl) {
		ArrayByteList result = new ArrayByteList(bl.size());
		result.addAll(bl);
		return result;
	}

	public ArrayByteList(int initialCapacity) {
		if(initialCapacity > 0) {
			field = new byte[initialCapacity];
			return;
		}
		if(initialCapacity == 0) {
			field = EMPTY_ELEMENTS;
		}
		throw new IllegalArgumentException("initalCapacity must be positive : "+initialCapacity);
	}

	public ArrayByteList() {
		field = DEFAULT_CAPACITY_EMPTY_ELEMENTS;
	}

	@Override
	public void clear() {
		size = 0;
		modCount++;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean add(byte b) {
		ensureCapacity(size+1);
		field[size] = b;
		size++;
		return true;
	}

	@Override
	public void insert(int index, byte element) {
		if(index < 0 || size < index) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
		ensureCapacity(size+1);

		for(int i = size;i > index;i--) {
			field[i+1] = field[i];
		}
		field[index] = element;
		size++;
	}

	@Override
	public void insertAll(int index, byte[] bs, int offset, int length) {
		if(index < 0 || size < index) {
			throw new IndexOutOfBoundsException(index);
		}
		PrimitiveSupport.checkBounds(bs, offset, length);

		int bsize = length;
		if(bsize == 0) {
			return;
		}
		ensureCapacity(size+bsize);

		// 退避
		for(int i = size;i > index;i--) {
			field[bsize+i] = field[i];
		}

		// 挿入
		for (int i = 0; i < bsize; i++) {
			field[size+i] = bs[offset+i];
		}
		size += bsize;
	}

	@Override
	public void insertAll(int index, ByteCollection c) {
		if(index < 0 || size < index) {
			throw new IndexOutOfBoundsException(index);
		}
		int csize = c.size();
		if(csize == 0) {
			return;
		}
		ensureCapacity(size+csize);

		// 退避
		for(int i = size;i > index;i--) {
			field[csize + i] = field[i];
		}

		// 挿入
		ByteIterator itr = c.iterator();
		for(int i = index;itr.hasNext();i++) {
			field[i] = itr.nextByte();
		}
		size += csize;
	}

	@Override
	public byte removeAt(int index) {
		if(index < 0 || size <= index) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}

		byte removed = field[index];

		for(int i = index;i < size-1;i++) {
			field[i] = field[i+1];
		}
		size--;
		modCount++;
		return removed;
	}

	@Override
	public byte get(int index) {
		if(index < 0 || size <= index) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
		return field[index];
	}

	@Override
	public byte set(int index, byte element) {
		if(index < 0 || size <= index) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
		byte previous = field[index];
		field[index] = element;
		return previous;
	}

	/**
	 * 指定した数の要素が内部配列の拡張なしに格納できるように内部配列を拡張する
	 * @param minCapacity 格納する要素数
	 */
	public void ensureCapacity(int minCapacity) {
		if(minCapacity > MAX_ARRAY_SIZE) {
			throw new RuntimeException("too big array!");
		}
		if(field == DEFAULT_CAPACITY_EMPTY_ELEMENTS) {
			minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
		}

		modCount++;
		if(minCapacity > field.length) {
			int oldCap = field.length;
			int recommend = oldCap + (oldCap >>> 1);
			int newCap = Math.min(Math.max(minCapacity, recommend), MAX_ARRAY_SIZE);

			field = Arrays.copyOf(field, newCap);
		}
	}

	@Override
	public ByteListIterator listIterator(int i) {
		return new ArrayListLterator(i);
	}

	private class ArrayListLterator implements ByteListIterator {

		// 構造的変更検出用
		int expectedModCount = modCount;

		// removeやsetで変更されるindex 対象がない場合-1
		int pIndex = -1;

		/*
		 *  ここでのインデックスは配列の要素の間を指すものとする
		 *  index=nがさすのはfield[n-1]とfield[n]の間である
		 *  操作の前後でindex∈[0, size]を満たすこととする
		 */
		int index;

		public ArrayListLterator(int index) {
			this.index = index;
		}


		@Override
		public boolean hasNext() {
			return index < size;
		}

		@Override
		public boolean hasPrevious() {
			return index > 0;
		}

		@Override
		public int nextIndex() {
			return index;
		}

		@Override
		public int previousIndex() {
			return index - 1;
		}

		@Override
		public void add(byte e) {
			modificationCheck();
			ArrayByteList.this.insert(index, e);

			pIndex = -1;
			index++;
		}


		@Override
		public void remove() {
			modificationCheck();
			if(pIndex == -1) {
				throw new IllegalStateException();
			}
			removeAt(pIndex);
			if(pIndex<index) {
				// 前向き探索時は配列をつめた分indexを戻す
				index--;
			}
			expectedModCount = modCount;
			pIndex = -1;
		}

		@Override
		public void set(byte e) {
			modificationCheck();
			if(pIndex == -1) {
				throw new IllegalStateException();
			}
			ArrayByteList.this.set(pIndex, e);
		}

		@Override
		public byte nextByte() {
			modificationCheck();
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			pIndex = index;
			index++;
			return field[index-1];
		}

		@Override
		public byte previousByte() {
			modificationCheck();
			if(!hasPrevious()) {
				throw new NoSuchElementException();
			}
			pIndex = index-1;
			index--;
			return field[index];
		}

		private void modificationCheck() {
			if(expectedModCount != modCount) {
				throw new ConcurrentModificationException();
			}
		}
	}

	private static class SerializationProxy implements Serializable {
		private static final long serialVersionUID = 9164685108597624187L;

		/**
		 * @serial
		 */
		private final byte[] elements;

		public SerializationProxy(ByteList list) {
			this.elements = list.toArray();
		}

		private Object readResolve() {
			ArrayByteList result = new ArrayByteList(elements.length);
			result.addAll(elements);
			return result;
		}
	}

	private Object writeReplace() {
		return new SerializationProxy(this);
	}

	private void readObject(java.io.ObjectInputStream s) throws java.io.InvalidObjectException {
		throw new java.io.InvalidObjectException("Proxy required");
	}
}
