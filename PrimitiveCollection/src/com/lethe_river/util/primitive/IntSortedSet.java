package com.lethe_river.util.primitive;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;

/**
 * プリミティブのintを扱う，全体順序付けを提供するSet.
 * @author YuyaAizawa
 *
 */
public interface IntSortedSet extends IntSet {


	/**
	 * この集合の順序付けを行うComparatorを返す.
	 * @return comparator
	 */
	public default Comparator<Integer> comparator() {
		return Comparator.naturalOrder();
	}

	/**
	 * この集合の最初の要素を返す.
	 * @return 最初の要素
	 *
	 * @throws NoSuchElementException 空の場合
	 */
	int first();

	/**
	 * この集合の最後の要素を返す.
	 * @return 最後の要素
	 */
	int last();

	/**
	 * この集合のformからtoまで(含まない)の要素からなる部分集合を返す．
	 * ビューは範囲外の値の挿入に対してIllegalArgumentExceptionをスローする．
	 * @param from
	 * @param to
	 * @return 部分集合のビュー
	 */
	IntSortedSet subSet(int from, int to);

	/**
	 * この集合の指定された要素よりも前にある要素からなる部分集合のビューを返す.
	 * ビューは範囲外の値の挿入に対してIllegalArgumentExceptionをスローする．
	 * @param to 境界の値
	 * @return 部分集合のビュー
	 */
	IntSortedSet headSet(int to);

	/**
	 * この集合の指定された要素から後にある要素からなる部分集合のビューを返す.
	 * ビューは範囲外の値の挿入に対してIllegalArgumentExceptionをスローする．
	 * @param from 境界の値
	 * @return 部分集合のビュー
	 */
	IntSortedSet tailSet(int from);

	@Override
	public default SortedSet<Integer> boxedView() {
		Set<Integer> setWrapper = boxedView();
		return new SortedSet<>() {
			@Override
			public int size() {
				return IntSortedSet.this.size();
			}

			@Override
			public boolean isEmpty() {
				return IntSortedSet.this.isEmpty();
			}

			@Override
			public boolean contains(Object o) {
				if(!(o instanceof Integer)) {
					return false;
				}
				return IntSortedSet.this.contains((int)o);
			}

			@Override
			public Iterator<Integer> iterator() {
				return IntSortedSet.this.iterator();
			}

			@Override
			public Object[] toArray() {
				return setWrapper.toArray();
			}

			@Override
			public <T> T[] toArray(T[] a) {
				return setWrapper.toArray(a);
			}

			@Override
			public boolean add(Integer e) {
				return setWrapper.add(e);
			}

			@Override
			public boolean remove(Object o) {
				return setWrapper.remove(o);
			}

			@Override
			public boolean containsAll(Collection<?> c) {
				return setWrapper.containsAll(c);
			}

			@Override
			public boolean addAll(Collection<? extends Integer> c) {
				return setWrapper.addAll(c);
			}

			@Override
			public boolean retainAll(Collection<?> c) {
				return setWrapper.retainAll(c);
			}

			@Override
			public boolean removeAll(Collection<?> c) {
				return setWrapper.removeAll(c);
			}

			@Override
			public void clear() {
				setWrapper.clear();
			}

			@Override
			public Comparator<? super Integer> comparator() {
				return (x, y) -> Integer.compare(x, y);
			}

			@Override
			public SortedSet<Integer> subSet(Integer fromElement, Integer toElement) {
				return IntSortedSet.this.subSet(fromElement, toElement).boxedView();
			}

			@Override
			public SortedSet<Integer> headSet(Integer toElement) {
				return IntSortedSet.this.headSet(toElement).boxedView();
			}

			@Override
			public SortedSet<Integer> tailSet(Integer fromElement) {
				return IntSortedSet.this.tailSet(fromElement).boxedView();
			}

			@Override
			public Integer first() {
				return IntSortedSet.this.first();
			}

			@Override
			public Integer last() {
				return IntSortedSet.this.last();
			}
		};
	}
}
