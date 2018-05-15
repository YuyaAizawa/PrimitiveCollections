package com.lethe_river.util.primitive;

import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * プリミティブのcharを扱う，要素の重複を許さないコレクション.
 *
 * @author YuyaAizawa
 *
 */
public interface CharSet extends PrimitiveSet<Character>, CharCollection {

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * {@inheritDoc}
	 */
	@Override
	int hashCode();

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean isEmpty();

	/**
	 * {@inheritDoc}
	 */
	@Override
	int size();

	/**
	 * {@inheritDoc}
	 */
	@Override
	void clear();

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean contains(char ch);

	/**
	 * {@inheritDoc}
	 */
	@Override
	CharIterator iterator();

	/**
	 * {@inheritDoc}
	 */
	@Override
	char[] toArray();

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean add(char ch);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean addAll(char... chs);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean addAll(CharCollection chs);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean remove(char ch);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean removeAll(char... chs);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean removeAll(CharCollection chs);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean containsAll(char... chs);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean retainAll(char... chs);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean retainAll(CharCollection chs);

	/**
	 * 指定した集合の要素すべてがこの集合に含まれるか判定する.
	 * @param chs
	 * @return 含まれればture
	 */
	boolean containsAll(CharSet chs);

	/**
	 * この集合の要素を列挙するSpliteratorを返す.
	 * @return spliterator
	 */
	@Override
	default Spliterator.OfInt spliterator() {
		return Spliterators.spliterator(iteratorOfInt(), size(),
				Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.DISTINCT);
	}

	/**
	 * このコレクションのSet&lt;Integer&gt;のビューを返す.
	 * ビューに対する操作はこのインタンスに反映される．
	 * @return このIntSetのSet&lt;Integer&gt;のビュー
	 */
	@Override
	Set<Character> boxedView();

	public static CharSet empty() {
		return EmptySet.ofChar();
	}
}