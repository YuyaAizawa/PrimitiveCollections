package com.lethe_river.util.primitive;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;

/**
 * プリミティブのcharを扱う，全体順序付けを提供するSet.
 * @author YuyaAizawa
 *
 */
public interface CharSortedSet extends CharSet {

	/**
	 * この集合の順序付けを行うComparatorを返す.
	 * @return comparator
	 */
	public default Comparator<Character> comparator() {
		return Comparator.naturalOrder();
	}

	/**
	 * この集合の最初の要素を返す.
	 * @return 最初の要素
	 *
	 * @throws NoSuchElementException 空の場合
	 */
	char first();

	/**
	 * この集合の最後の要素を返す.
	 * @return 最後の要素
	 */
	char last();

	/**
	 * この集合のformからtoまで(含まない)の要素からなる部分集合を返す．
	 * ビューは範囲外の値の挿入に対してIllegalArgumentExceptionをスローする．
	 * @param from
	 * @param to
	 * @return 部分集合のビュー
	 */
	CharSortedSet subSet(char from, char to);

	/**
	 * この集合の指定された要素よりも前にある要素からなる部分集合のビューを返す.
	 * ビューは範囲外の値の挿入に対してIllegalArgumentExceptionをスローする．
	 * @param to 境界の値
	 * @return 部分集合のビュー
	 */
	CharSortedSet headSet(char to);

	/**
	 * この集合の指定された要素から後にある要素からなる部分集合のビューを返す.
	 * ビューは範囲外の値の挿入に対してIllegalArgumentExceptionをスローする．
	 * @param from 境界の値
	 * @return 部分集合のビュー
	 */
	CharSortedSet tailSet(char from);

	@Override
	public default SortedSet<Character> boxedView() {
		Set<Character> setWrapper = boxedView();
		return new SortedSet<>() {
			@Override
			public int size() {
				return CharSortedSet.this.size();
			}

			@Override
			public boolean isEmpty() {
				return CharSortedSet.this.isEmpty();
			}

			@Override
			public boolean contains(Object o) {
				if(!(o instanceof Character)) {
					return false;
				}
				return CharSortedSet.this.contains((char)o);
			}

			@Override
			public Iterator<Character> iterator() {
				return CharSortedSet.this.iterator();
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
			public boolean add(Character e) {
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
			public boolean addAll(Collection<? extends Character> c) {
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
			public Comparator<? super Character> comparator() {
				return (x, y) -> Character.compare(x, y);
			}

			@Override
			public SortedSet<Character> subSet(Character fromElement, Character toElement) {
				return CharSortedSet.this.subSet(fromElement, toElement).boxedView();
			}

			@Override
			public SortedSet<Character> headSet(Character toElement) {
				return CharSortedSet.this.headSet(toElement).boxedView();
			}

			@Override
			public SortedSet<Character> tailSet(Character fromElement) {
				return CharSortedSet.this.tailSet(fromElement).boxedView();
			}

			@Override
			public Character first() {
				return CharSortedSet.this.first();
			}

			@Override
			public Character last() {
				return CharSortedSet.this.last();
			}
		};
	}
}
