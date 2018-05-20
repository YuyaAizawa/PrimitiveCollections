package com.lethe_river.util.primitive;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;

/**
 * プリミティブのlongを扱う，全体順序付けを提供するSet.
 * @author YuyaAizawa
 *
 */
public interface LongSortedSet extends LongSet {


	/**
	 * この集合の順序付けを行うComparatorを返す.
	 * @return comparator
	 */
	public default Comparator<Long> comparator() {
		return Comparator.naturalOrder();
	}

	/**
	 * この集合の最初の要素を返す.
	 * @return 最初の要素
	 *
	 * @throws NoSuchElementException 空の場合
	 */
	long first();

	/**
	 * この集合の最後の要素を返す.
	 * @return 最後の要素
	 */
	long last();

	/**
	 * この集合のformからtoまで(含まない)の要素からなる部分集合を返す．
	 * ビューは範囲外の値の挿入に対してIllegalArgumentExceptionをスローする．
	 * @param from
	 * @param to
	 * @return 部分集合のビュー
	 */
	LongSortedSet subSet(long from, long to);

	/**
	 * この集合の指定された要素よりも前にある要素からなる部分集合のビューを返す.
	 * ビューは範囲外の値の挿入に対してIllegalArgumentExceptionをスローする．
	 * @param to 境界の値
	 * @return 部分集合のビュー
	 */
	LongSortedSet headSet(long to);

	/**
	 * この集合の指定された要素から後にある要素からなる部分集合のビューを返す.
	 * ビューは範囲外の値の挿入に対してIllegalArgumentExceptionをスローする．
	 * @param from 境界の値
	 * @return 部分集合のビュー
	 */
	LongSortedSet tailSet(long from);

	@Override
	public default SortedSet<Long> boxedView() {
		Set<Long> setWrapper = boxedView();
		return new SortedSet<>() {
			@Override
			public int size() {
				return LongSortedSet.this.size();
			}

			@Override
			public boolean isEmpty() {
				return LongSortedSet.this.isEmpty();
			}

			@Override
			public boolean contains(Object o) {
				if(!(o instanceof Long)) {
					return false;
				}
				return LongSortedSet.this.contains((long)o);
			}

			@Override
			public Iterator<Long> iterator() {
				return LongSortedSet.this.iterator();
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
			public boolean add(Long e) {
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
			public boolean addAll(Collection<? extends Long> c) {
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
			public Comparator<? super Long> comparator() {
				return (x, y) -> Long.compare(x, y);
			}

			@Override
			public SortedSet<Long> subSet(Long fromElement, Long toElement) {
				return LongSortedSet.this.subSet(fromElement, toElement).boxedView();
			}

			@Override
			public SortedSet<Long> headSet(Long toElement) {
				return LongSortedSet.this.headSet(toElement).boxedView();
			}

			@Override
			public SortedSet<Long> tailSet(Long fromElement) {
				return LongSortedSet.this.tailSet(fromElement).boxedView();
			}

			@Override
			public Long first() {
				return LongSortedSet.this.first();
			}

			@Override
			public Long last() {
				return LongSortedSet.this.last();
			}
		};
	}
}
