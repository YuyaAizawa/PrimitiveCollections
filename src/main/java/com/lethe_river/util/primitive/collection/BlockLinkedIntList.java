package com.lethe_river.util.primitive.collection;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * 内部をn^(1/2)程度の大きさの配列に分けることで高速処理を可能にするリスト
 * @author YuyaAizawa
 *
 */
public final class BlockLinkedIntList extends AbstractIntList implements RandomAccess {

	private final ArrayList<IntList> root;
	private int size;

	private int modCount;

	public BlockLinkedIntList() {
		root = new ArrayList<>();
	}

	@Override
	public IntListIterator listIterator(int index) {
		return new BlockLinkedIntListIterator(index);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public int get(int index) {
		if(index < 0 || size() <= index) {
			throw new IndexOutOfBoundsException(index);
		}

		int offset = index;
		for (int i = 0; i < root.size(); i++) {
			IntList list = root.get(i);
			if(offset < list.size()) {
				return list.get(offset);
			}
			offset -= list.size();
		}
		throw new Error();
	}

	@Override
	public int set(int index, int element) {
		if(index < 0 || size() <= index) {
			throw new IndexOutOfBoundsException(index);
		}

		int offset = index;
		for (int i = 0; i < root.size(); i++) {
			IntList list = root.get(i);
			if(offset < list.size()) {
				return list.set(offset, element);
			}
			offset -= list.size();
		}
		throw new Error();
	}

	@Override
	public void insert(int index, int element) {
		if(index < 0 || size() < index) {
			throw new IndexOutOfBoundsException(index);
		}

		int offset = index;
		int i = 0;
		for (; i < root.size(); i++) {
			IntList list = root.get(i);
			if(offset < list.size() || i == root.size()-1) {
				list.insert(offset, element);

				size++;
				splitCheck(i);
				return;
			}
			offset -= list.size();
		}

	}

	@Override
	public int removeAt(int index) {
		if(index < 0 || size() <= index) {
			throw new IndexOutOfBoundsException(index);
		}

		int result = 0;
		int offset = index;
		int i = 0;
		for (; i < root.size(); i++) {
			IntList list = root.get(i);
			if(offset < list.size()) {
				result = list.removeAt(offset);
				size--;
				mergeCheck(i);

				return result;
			}
			offset -= list.size();
		}

		throw new Error();
	}

	/**
	 * i番目のリストの大きさが2*n^(1/2)を超えていれば分割する
	 * @param i
	 */
	private void splitCheck(int listNum) {
		int[] target = root.get(listNum).toArray();
		int targetSize = target.length;
		if(targetSize < 11) {
			return;
		}
		if(((long) targetSize) * targetSize > ((long) size())<<2) {
			int init = (targetSize>>1) + (targetSize>>2);
			IntList firstHalf = new ArrayIntList(init);
			IntList lastHalf = new ArrayIntList(init);

			firstHalf.addAll(target, 0, targetSize>>1);
			lastHalf.addAll(target, targetSize>>1, targetSize - (targetSize>>1));

			root.set(listNum, firstHalf);
			root.add(listNum+1, lastHalf);
			modCount++;
		}
	}

	private void mergeCheck(int listNum) {
		int targetSize = root.get(listNum).size();
		if(((long) targetSize) * targetSize < size()>>2) {
			if(listNum > 0) {
				long fhSize = root.get(listNum-1).size();
				if(fhSize * fhSize < size()>>2) {
					merge(listNum-1);
					return;
				}
			}
			if(listNum < root.size()-1) {
				long lhSize = root.get(listNum+1).size();
				if(lhSize * lhSize < size()>>2) {
					merge(listNum);
					return;
				}
			}
		}
	}

	private void merge(int firstListNum) {
		IntList target = root.get(firstListNum);
		target.addAll(root.get(firstListNum+1));
		root.remove(firstListNum+1);
		modCount++;
	}

	private class BlockLinkedIntListIterator implements IntListIterator {
		// 構造的変更検出用
		int expectedModCount = modCount;

		// removeやsetで変更されるindex 対象がない場合-1
		int pIndex = -1;

		/*
		 *  ここでのインデックスは配列の要素の間を指すものとする
		 *  index=nがさすのはn-1番目の要素とn番目の要素の間である
		 *  操作の前後でindex∈[0, size]を満たすこととする
		 */
		int index;

		public BlockLinkedIntListIterator(int index) {
			if(index < 0 || size() < index) {
				throw new IndexOutOfBoundsException(index);
			}
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
			BlockLinkedIntList.this.insert(index, e);

			expectedModCount = modCount;
			pIndex = -1;
			index++;
		}

		@Override
		public void remove() {
			modificationCheck();
			if(pIndex == -1) {
				throw new IllegalStateException();
			}
			BlockLinkedIntList.this.removeAt(pIndex);
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
			BlockLinkedIntList.this.set(pIndex, e);
		}

		@Override
		public int nextInt() {
			modificationCheck();
			if(!hasNext()) {
				throw new NoSuchElementException();
			}

			pIndex = index;
			index++;
			return BlockLinkedIntList.this.get(index-1);
		}

		@Override
		public int previousInt() {
			modificationCheck();
			if(!hasPrevious()) {
				throw new NoSuchElementException();
			}
			pIndex = index-1;
			index--;
			return BlockLinkedIntList.this.get(index);
		}

		private void modificationCheck() {
			if(expectedModCount != modCount) {
				throw new ConcurrentModificationException();
			}
		}
	}
}
