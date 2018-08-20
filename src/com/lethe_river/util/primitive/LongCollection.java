package com.lethe_river.util.primitive;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

/**
 * longを格納するPrimitiveCollection
 *
 * @author YuyaAizawa
 *
 */
public interface LongCollection extends PrimitiveCollection<Long> ,LongIterable {
	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean equals(Object obj);

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
	 * このコレクションが指定したlong値を含んでいればtrueを返す.
	 * @param l 判定する値
	 * @return 指定した値を含んでいればtrue
	 */
	boolean contains(long l);

	/**
	 * このコレクションの要素に対する反復操作を提供するPrimitiveIterator.OfLongを返す.
	 * @return PrimitiveIterator.OfLong
	 */
	@Override
	PrimitiveIterator.OfLong iterator();

	/**
	 * このコレクションの格納する要素をすべて含む配列を返す.
	 * Iteratorが返す要素の順番が保証される場合，配列の順番はこれに準ずる．
	 * @return すべての要素を含む配列
	 */
	long[] toArray();

	/**
	 * 指定されたlong値をこのコレクションに追加する(オプションの操作).
	 *
	 * @param l 追加する値
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean add(long i);

	/**
	 * 指定された要素すべてをこのコレクションに追加する(オプションの操作).
	 *
	 * @param ls
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	default boolean addAll(long... ls) {
		return addAll(ls, 0, ls.length);
	}

	/**
	 * 指定された配列の要素の一部区間をこのコレクションに追加する(オプションの操作).
	 *
	 * @param ls 配列
	 * @param offset 追加する区間の開始インデックス
	 * @param length 追加する区間の長さ
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 * @throws IndexOutOfBoundsException offsetまたはlengthが不正だった場合
	 * @throws NullPointerException 配列がnullだった場合
	 */
	boolean addAll(long[] ls, int offset, int length);

	/**
	 * 指定された要素すべてをこのコレクションに追加する(オプションの操作).
	 *
	 * @param ls
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 * @throws NullPointerException コレクションがnullだった場合
	 */
	boolean addAll(LongCollection is);

	/**
	 * 指定された要素をこのコレクションから削除する(オプションの操作).
	 *
	 * @param l
	 * @return 指定された要素を保持していればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean remove(long i);

	/**
	 * 指定された要素すべてをこのコレクションから削除する(オプションの操作).
	 *
	 * @param ls
	 * @return 指定された要素を保持していればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean removeAll(long... ls);

	/**
	 * 指定された要素すべてをこのコレクションから削除する(オプションの操作).
	 *
	 * @param ls
	 * @return 指定された要素を保持していればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean removeAll(LongCollection ls);

	/**
	 * 指定されたlong値がすべてこのコレクションに含まれていればtrueを返す.
	 * @param ls
	 * @return 指定されたlong値がすべて含まれていればtrue
	 */
	boolean containsAll(long... ls);

	/**
	 * 指定されたコレクションの値がすべてこのコレクションに含まれていればtrueを返す.
	 * @param ls
	 * @return 指定されたlong値がすべて含まれていればtrue
	 */
	boolean containsAll(LongCollection ls);

	/**
	 * このコレクションから指定された要素以外の要素を全て削除する(オプションの操作).
	 * @param ls
	 * @return 要素が1つでも削除されればtrue
	 *
	 *  @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean retainAll(long... ls);

	/**
	 * このコレクションから指定された要素以外の要素を全て削除する(オプションの操作).
	 * @param ls
	 * @return 要素が1つでも削除されればtrue
	 *
	 *  @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean retainAll(LongCollection ls);

	/**
	 * このコレクションに対する逐次的なLongStreamを返す．
	 * @return stream
	 */
	default LongStream stream() {
		return StreamSupport.longStream(spliterator(), false);
	}

	/**
	 * このコレクションに対する並列なLongStreamを返す．
	 * @return stream
	 */
	default LongStream parallelStream() {
		return StreamSupport.longStream(spliterator(), true);
	}

	/**
	 * このコレクションの要素に対する反復的な操作を提供するSpliteratorを返す.
	 * @return spliterator
	 */
	@Override
	default Spliterator.OfLong spliterator() {
		return Spliterators.spliterator(iterator(), size(),
				Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED);
	}

	/**
	 * このコレクションのCollection&lt;Integer&gt;のビューを返す.
	 * ビューに対する操作はこのインタンスに反映される．
	 * @return このIntCollectionのCollection&lt;Integer&gt;のビュー
	 */
	@Override
	default Collection<Long> boxedView() {
		return PrimitiveSupport.boxed(this);
	}

	public static LongCollection empty() {
		return EmptyCollection.ofLong();
	}
}
