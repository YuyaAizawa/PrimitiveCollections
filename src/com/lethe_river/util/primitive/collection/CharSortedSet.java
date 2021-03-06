package com.lethe_river.util.primitive.collection;

import java.util.Comparator;
import java.util.NoSuchElementException;
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

	/**
	 * この順序付集合のSortedSet&lt;Character&gt;のビューを返す.
	 * ビューに対する操作はこのインタンスに反映される．
	 * @return このCharSortedSetのSortedSet&lt;Character&gt;のビュー
	 */
	@Override
	SortedSet<Character> boxedView();
}
