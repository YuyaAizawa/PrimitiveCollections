package com.lethe_river.util.primitive;

import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * プリミティブのlongを扱う，要素の重複を許さないコレクション.
 *
 * @author YuyaAizawa
 *
 */
public interface LongSet extends PrimitiveSet<Long>, LongCollection {

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
	boolean contains(long i);

	/**
	 * {@inheritDoc}
	 */
	@Override
	PrimitiveIterator.OfLong iterator();

	/**
	 * {@inheritDoc}
	 */
	@Override
	long[] toArray();

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean add(long l);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean addAll(long... ls);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean addAll(LongCollection ls);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean remove(long i);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean removeAll(long... ls);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean removeAll(LongCollection ls);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean containsAll(long... ls);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean retainAll(long... ls);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean retainAll(LongCollection ls);

	/**
	 * 指定した集合の要素すべてがこの集合に含まれるか判定する.
	 * @param ls
	 * @return 含まれればture
	 */
	boolean containsAll(LongSet ls);

	/**
	 * この集合の要素を列挙するSpliteratorを返す.
	 * @return spliterator
	 */
	@Override
	default Spliterator.OfLong spliterator() {
		return Spliterators.spliterator(iterator(), size(),
				Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.DISTINCT);
	}

	/**
	 * このコレクションのSet&lt;Integer&gt;のビューを返す.
	 * ビューに対する操作はこのインタンスに反映される．
	 * @return このIntSetのSet&lt;Integer&gt;のビュー
	 */
	@Override
	Set<Long> boxedView();

	public static LongSortedSet empty() {
		return EmptySet.ofLong();
	}
}

