package com.lethe_river.util.primitive;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.RandomAccess;

/**
 * 配列をベースにしたint値を格納するPrimitiveList
 *
 * @author YuyaAizawa
 *
 */
public final class ArrayIntList extends AbstractIntList implements IntList, RandomAccess {

	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE >> 1;

	private static final int[] EMPTY_ELEMENTS = {};
	private static final int[] DEFAULT_CAPACITY_EMPTY_ELEMENTS = {};

	private static final int DEFAULT_CAPACITY = 10;

	// リストの内容
	private int[] field;

	private int size;

	public static ArrayIntList of(int... is) {
		ArrayIntList result = new ArrayIntList(is.length);
		for (int i = 0; i < is.length; i++) {
			result.add(is[i]);
		}
		return result;
	}

	public static IntList of(IntList il) {
		ArrayIntList result = new ArrayIntList(il.size());
		result.addAll(il);
		return result;
	}

	public ArrayIntList(int initialCapacity) {
		if(initialCapacity > 0) {
			field = new int[initialCapacity];
			return;
		}
		if(initialCapacity == 0) {
			field = EMPTY_ELEMENTS;
		}
		throw new IllegalArgumentException("initalCapacity must be positive : "+initialCapacity);
	}

	public ArrayIntList() {
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
	public boolean add(int i) {
		ensureCapacity(size+1);
		field[size] = i;
		size++;
		return true;
	}

	@Override
	public void insert(int index, int element) {
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
	public void insertAll(int index, int[] is, int offset, int length) {
		if(index < 0 || size < index) {
			throw new IndexOutOfBoundsException(index);
		}
		PrimitiveSupport.checkBounds(is, offset, length);

		if(length == 0) {
			return;
		}
		ensureCapacity(size+length);

		// 退避
		for(int i = size;i > index;i--) {
			field[length+i] = field[i];
		}

		// 挿入
		for (int i = 0; i < length; i++) {
			field[size+i] = is[offset+i];
		}
		size += length;
	}

	@Override
	public void insertAll(int index, IntCollection c) {
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
		PrimitiveIterator.OfInt itr = c.iterator();
		for(int i = index;itr.hasNext();i++) {
			field[i] = itr.nextInt();
		}
		size += csize;
	}

	@Override
	public int removeAt(int index) {
		if(index < 0 || size <= index) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}

		int removed = field[index];

		for(int i = index;i < size-1;i++) {
			field[i] = field[i+1];
		}
		size--;
		modCount++;
		return removed;
	}

	@Override
	public int get(int index) {
		if(index < 0 || size <= index) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
		return field[index];
	}

	@Override
	public int set(int index, int element) {
		if(index < 0 || size <= index) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
		int previous = field[index];
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
	public IntListIterator listIterator(int i) {
		return new ArrayListLterator(i);
	}

	private class ArrayListLterator implements IntListIterator {

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
		public void add(int e) {
			modificationCheck();
			ArrayIntList.this.insert(index, e);

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
		public void set(int e) {
			modificationCheck();
			if(pIndex == -1) {
				throw new IllegalStateException();
			}
			ArrayIntList.this.set(pIndex, e);
		}

		@Override
		public int nextInt() {
			modificationCheck();
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			pIndex = index;
			index++;
			return field[index-1];
		}

		@Override
		public int previousInt() {
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
}
