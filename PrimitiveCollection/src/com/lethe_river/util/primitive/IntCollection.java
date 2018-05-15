package com.lethe_river.util.primitive;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * intを格納するPrimitiveCollection
 *
 * @author YuyaAizawa
 *
 */
public interface IntCollection extends PrimitiveCollection<Integer>, IntIterable {
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
	 * このコレクションが指定したint値を含んでいればtrueを返す.
	 * @param i 判定する値
	 * @return 指定した値を含んでいればtrue
	 */
	boolean contains(int i);

	/**
	 * このコレクションの要素に対する反復操作を提供するPrimitiveIterator.OfIntを返す.
	 * @return PrimitiveIterator.OfInt
	 */
	@Override
	PrimitiveIterator.OfInt iterator();

	/**
	 * このコレクションの格納する要素をすべて含む配列を返す.
	 * Iteratorが返す要素の順番が保証される場合，配列の順番はこれに準ずる．
	 * @return すべての要素を含む配列
	 */
	int[] toArray();

	/**
	 * 指定されたint値をこのコレクションに追加する(オプションの操作).
	 *
	 * @param i 追加する値
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean add(int i);

	/**
	 * 指定された要素すべてをこのコレクションに追加する(オプションの操作).
	 *
	 * @param is
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	default boolean addAll(int... is) {
		return addAll(is, 0, is.length);
	}

	/**
	 * 指定された配列の要素の一部区間をこのコレクションに追加する(オプションの操作).
	 *
	 * @param is 配列
	 * @param offset 追加する区間の開始インデックス
	 * @param length 追加する区間の長さ
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 * @throws IndexOutOfBoundsException offsetまたはlengthが不正だった場合
	 * @throws NullPointerException 配列がnullだった場合
	 */
	boolean addAll(int[] is, int offset, int length);

	/**
	 * 指定された要素すべてをこのコレクションに追加する(オプションの操作).
	 *
	 * @param is
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 * @throws NullPointerException コレクションがnullだった場合
	 */
	boolean addAll(IntCollection is);

	/**
	 * 指定された要素をこのコレクションから削除する(オプションの操作).
	 *
	 * @param i
	 * @return 指定された要素を保持していればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean remove(int i);

	/**
	 * 指定された要素すべてをこのコレクションから削除する(オプションの操作).
	 *
	 * @param is
	 * @return 指定された要素を保持していればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean removeAll(int... is);

	/**
	 * 指定された要素すべてをこのコレクションから削除する(オプションの操作).
	 *
	 * @param is
	 * @return 指定された要素を保持していればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean removeAll(IntCollection is);

	/**
	 * 指定されたint値がすべてこのコレクションに含まれていればtrueを返す.
	 * @param is
	 * @return 指定されたint値がすべて含まれていればtrue
	 */
	boolean containsAll(int... is);

	/**
	 * 指定されたコレクションの値がすべてこのコレクションに含まれていればtrueを返す.
	 * @param is
	 * @return 指定されたint値がすべて含まれていればtrue
	 */
	boolean containsAll(IntCollection is);

	/**
	 * このコレクションから指定された要素以外の要素を全て削除する(オプションの操作).
	 * @param is
	 * @return 要素が1つでも削除されればtrue
	 *
	 *  @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean retainAll(int... is);

	/**
	 * このコレクションから指定された要素以外の要素を全て削除する(オプションの操作).
	 * @param is
	 * @return 要素が1つでも削除されればtrue
	 *
	 *  @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean retainAll(IntCollection is);

	/**
	 * このコレクションに対する逐次的なIntStreamを返す．
	 * @return stream
	 */
	default IntStream stream() {
		return StreamSupport.intStream(spliterator(), false);
	}

	/**
	 * このコレクションに対する並列なIntStreamを返す．
	 * @return stream
	 */
	default IntStream parallelStream() {
		return StreamSupport.intStream(spliterator(), true);
	}

	/**
	 * このコレクションの要素に対する反復的な操作を提供するSpliteratorを返す.
	 * @return spliterator
	 */
	@Override
	default Spliterator.OfInt spliterator() {
		return Spliterators.spliterator(iterator(), size(),
				Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED);
	}

	/**
	 * このコレクションのCollection&lt;Integer&gt;のビューを返す.
	 * ビューに対する操作はこのインタンスに反映される．
	 * @return このIntCollectionのCollection&lt;Integer&gt;のビュー
	 */
	@Override
	default Collection<Integer> boxedView() {
		return PrimitiveSupport.boxed(this);
	}

	public static IntCollection empty() {
		return EmptyCollection.ofInt();
	}
}
