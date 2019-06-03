package com.lethe_river.util.primitive.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class PrimitiveCollections {
	private PrimitiveCollections(){}

	/**
	 * 指定したIntCollectionの変更不可能なビューを返す
	 * @return 変更不可能なビュー
	 */
	public static IntCollection unmodifiableCollection(IntCollection original) {
		return PrimitiveSupport.unmodifiableCollection(original);
	}

	/**
	 * 指定したIntListの変更不可能なビューを返す
	 * @return 変更不可能なビュー
	 */
	public static IntList unmodifiableList(IntList original) {
		return PrimitiveSupport.unmodifiableList(original);
	}

	/**
	 * Set内の要素の組合せをのSetを列挙するためのIterableを返す.
	 * 元のListに変更を加えた後のIteratorへのアクセスは保障されない．
	 * @param set
	 * @param size 組み合わせる要素数
	 * @return 組合せを列挙するIterable
	 */
	public static Iterable<IntSet> combination (IntSet elements, int size) {
		Objects.requireNonNull(elements);

		if(size > elements.size()) {
			throw new IllegalArgumentException();
		}

		return new Iterable<>() {
			@Override
			public Iterator<IntSet> iterator() {
				Iterator<IntList> listIterator = new IntCombinationIterator(
						elements.stream().sorted().collect(
								() -> new ArrayIntList(elements.size()),
								ArrayIntList::add,
								(l, r) -> l.addAll(r))
						, size);

				return new Iterator<>() {

					@Override
					public boolean hasNext() {
						return listIterator.hasNext();
					}

					@Override
					public IntSet next() {
						IntSet result = new ScatterIntSet(size);
						result.addAll(listIterator.next());
						return result;
					}
				};
			}
		};
	}

	/**
	 * List内の要素の組合せを列挙するIteratorを取得するためのIterableを返す.
	 * Iteratorのnext呼出しで返されるリストは，Iterator毎に作られる参照であり，次のnext呼び出しで変更される．
	 * 元のListに変更を加えた後のIteratorへのアクセスは保障されない．
	 * @param list
	 * @param size 組み合わせる要素数
	 * @return 組合せを列挙するIterable
	 */
	public static Iterable<IntList> combinationInPlace (IntList list, int size) {
		Objects.requireNonNull(list);

		if(size > list.size()) {
			throw new IllegalArgumentException();
		}

		return new Iterable<>() {
			@Override
			public Iterator<IntList> iterator() {
				return new IntCombinationIterator(list, size);
			}
		};
	}

	private static class IntCombinationIterator implements Iterator<IntList> {
		private final IntList l;
		private int[] indexes;
		private final IntList view;
		private final IntList view2;

		public IntCombinationIterator(IntList list, int size) {
			l = list;
			indexes = new int[size];
			view = new ArrayIntList(size);
			view2 = PrimitiveCollections.unmodifiableList(view);
			for(int i = 0;i < indexes.length;i++) {
				indexes[i] = i;
			}
		}

		private void prepareIndexes() {
			indexes[indexes.length-1]++;
			int maxIndex = l.size()-1;

			int reallocate = indexes.length-1;
			for(int i = indexes.length-1; i > 0;i--) {
				if(indexes[i] > maxIndex) {
					indexes[i-1]++;
					reallocate = i-1;
				}
				maxIndex--;
			}

			if(reallocate == 0 && indexes[0] > maxIndex) {
				indexes = null;
				return;
			}

			int val = indexes[reallocate];
			for(int i = reallocate;i < indexes.length;i++) {
				indexes[i] = val;
				val++;
			}
		}

		@Override
		public boolean hasNext() {
			return indexes != null;
		}

		@Override
		public IntList next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			view.clear();
			for(int i = 0;i < indexes.length;i++) {
				view.add(l.get(indexes[i]));
			}
			prepareIndexes();
			return view2;
		}
	}
}
