package com.lethe_river.util.primitive.collection;

import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * 配列をベースにした変更不可能なIntList
 *
 * @author YuyaAizawa
 *
 */
public class FrozenArrayIntList extends AbstractIntList implements IntList, RandomAccess {

	private static final long serialVersionUID = 7207403370949769575L;

	private int[] field;

	public FrozenArrayIntList(int... field) {
		this.field = field;
	}

	@Override
	public IntListIterator listIterator(int index) {
		return new FrozenArrayListIterator(index);
	}

	@Override
	public int size() {
		return field.length;
	}

	private class FrozenArrayListIterator implements IntListIterator {

		/*
		 *  ここでのインデックスは配列の要素の間を指すものとする
		 *  index=nがさすのはfield[n-1]とfield[n]の間である
		 *  操作の前後でindex∈[0, size]を満たすこととする
		 */
		int index;

		public FrozenArrayListIterator(int index) {
			this.index = index;
		}

		@Override
		public boolean hasNext() {
			return index < size();
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
		public int nextInt() {
			try {
				index++;
				return field[index-1];
			} catch (ArrayIndexOutOfBoundsException e) {
				index--;
				throw new NoSuchElementException();
			}
		}

		@Override
		public int previousInt() {
			try {
				index--;
				return field[index];
			} catch (ArrayIndexOutOfBoundsException e) {
				index++;
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(int e) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(int e) {
			throw new UnsupportedOperationException();
		}
	}
}
