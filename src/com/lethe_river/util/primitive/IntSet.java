package com.lethe_river.util.primitive;

import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * プリミティブのintを扱う，要素の重複を許さないコレクション.
 *
 * @author YuyaAizawa
 *
 */
public interface IntSet extends PrimitiveSet<Integer>, IntCollection {

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
	boolean contains(int i);

	/**
	 * {@inheritDoc}
	 */
	@Override
	PrimitiveIterator.OfInt iterator();

	/**
	 * {@inheritDoc}
	 */
	@Override
	int[] toArray();

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean add(int i);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean addAll(int... is);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean addAll(IntCollection is);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean remove(int i);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean removeAll(int... is);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean removeAll(IntCollection is);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean containsAll(int... is);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean retainAll(int... is);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean retainAll(IntCollection is);

	/**
	 * 指定した集合の要素すべてがこの集合に含まれるか判定する.
	 * @param is
	 * @return 含まれればture
	 */
	boolean containsAll(IntSet is);

	/**
	 * この集合の要素を列挙するSpliteratorを返す.
	 * @return spliterator
	 */
	@Override
	default Spliterator.OfInt spliterator() {
		return Spliterators.spliterator(iterator(), size(),
				Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.DISTINCT);
	}

	/**
	 * このコレクションのSet&lt;Integer&gt;のビューを返す.
	 * ビューに対する操作はこのインタンスに反映される．
	 * @return このIntSetのSet&lt;Integer&gt;のビュー
	 */
	@Override
	Set<Integer> boxedView();

	public static IntSortedSet empty() {
		return EmptySet.ofInt();
	}

	/**
	 * 不変InSetを生成する
	 * @return 不変IntSet
	 */
	public static IntSortedSet of(int... is) {
		if(is.length == 0) {
			return empty();
		} else {
			return FrozenArrayIntSet.of(is);
		}
	}
}

