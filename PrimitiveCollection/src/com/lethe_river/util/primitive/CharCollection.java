package com.lethe_river.util.primitive;

import java.util.Collection;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * charを格納するPrimitiveCollection
 *
 * @author YuyaAizawa
 *
 */
public interface CharCollection extends PrimitiveCollection<Character>, CharIterable {
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
	 * このコレクションが指定したchar値を含んでいればtrueを返す.
	 * @param ch 判定する値
	 * @return 指定した値を含んでいればtrue
	 */
	boolean contains(char ch);

	/**
	 * このコレクションの要素に対する反復操作を提供するCharIteratorを返す.
	 * @return CharIterator
	 */
	@Override
	CharIterator iterator();

	/**
	 * このコレクションの格納する要素をすべて含む配列を返す.
	 * Iteratorが返す要素の順番が保証される場合，配列の順番はこれに準ずる．
	 * @return すべての要素を含む配列
	 */
	char[] toArray();

	/**
	 * 指定されたchar値をこのコレクションに追加する(オプションの操作).
	 *
	 * @param ch 追加する値
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean add(char ch);

	/**
	 * 指定された要素すべてをこのコレクションに追加する(オプションの操作).
	 *
	 * @param chs
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	default boolean addAll(char... chs) {
		return addAll(chs, 0, chs.length);
	}

	/**
	 * 指定された配列の要素の一部区間をこのコレクションに追加する(オプションの操作).
	 *
	 * @param chs 配列
	 * @param offset 追加する区間の開始インデックス
	 * @param length 追加する区間の長さ
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 * @throws IndexOutOfBoundsException offsetまたはlengthが不正だった場合
	 * @throws NullPointerException 配列がnullだった場合
	 */
	boolean addAll(char[] chs, int offset, int length);

	/**
	 * 指定された要素すべてをこのコレクションに追加する(オプションの操作).
	 *
	 * @param chs
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 * @throws NullPointerException コレクションがnullだった場合
	 */
	boolean addAll(CharCollection chs);

	/**
	 * 指定された要素をこのコレクションから削除する(オプションの操作).
	 *
	 * @param ch
	 * @return 指定された要素を保持していればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean remove(char ch);

	/**
	 * 指定された要素すべてをこのコレクションから削除する(オプションの操作).
	 *
	 * @param chs
	 * @return 指定された要素を保持していればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean removeAll(char... chs);

	/**
	 * 指定された要素すべてをこのコレクションから削除する(オプションの操作).
	 *
	 * @param chs
	 * @return 指定された要素を保持していればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean removeAll(CharCollection chs);

	/**
	 * 指定されたchar値がすべてこのコレクションに含まれていればtrueを返す.
	 * @param chs
	 * @return 指定されたchar値がすべて含まれていればtrue
	 */
	boolean containsAll(char... chs);

	/**
	 * 指定されたコレクションの値がすべてこのコレクションに含まれていればtrueを返す.
	 * @param chs
	 * @return 指定されたchar値がすべて含まれていればtrue
	 */
	boolean containsAll(CharCollection chs);

	/**
	 * このコレクションから指定された要素以外の要素を全て削除する(オプションの操作).
	 * @param chs
	 * @return 要素が1つでも削除されればtrue
	 *
	 *  @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean retainAll(char... chs);

	/**
	 * このコレクションから指定された要素以外の要素を全て削除する(オプションの操作).
	 * @param chs
	 * @return 要素が1つでも削除されればtrue
	 *
	 *  @throws UnsupportedOperationException 操作がサポートされていない場合
	 */
	boolean retainAll(CharCollection is);

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
		return Spliterators.spliterator(iteratorOfInt(), size(),
				Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED);
	}

	/**
	 * このコレクションのCollection&lt;Integer&gt;のビューを返す.
	 * ビューに対する操作はこのインタンスに反映される．
	 * @return このIntCollectionのCollection&lt;Integer&gt;のビュー
	 */
	@Override
	default Collection<Character> boxedView() {
		return PrimitiveSupport.boxed(this);
	}

	public static CharCollection empty() {
		return EmptyCollection.ofChar();
	}
}
