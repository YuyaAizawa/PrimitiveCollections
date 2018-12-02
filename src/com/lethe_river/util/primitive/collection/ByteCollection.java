package com.lethe_river.util.primitive.collection;

import java.util.Collection;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * byteを格納するPrimitiveCollection
 *
 * @author YuyaAizawa
 *
 */
public interface ByteCollection extends PrimitiveCollection<Byte>, ByteIterable {

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
	 * このコレクションが指定したbyte値を含んでいればtrueを返す.
	 * @param b 判定する値
	 * @return 指定した値を含んでいればtrue
	 */
	boolean contains(byte b);

	/**
	 * このコレクションの要素に対する反復操作を提供するByteIteratorを返す.
	 * @return PrimitiveIterator.OfInt
	 */
	@Override
	ByteIterator iterator();

	/**
	 * このコレクションの格納する要素をすべて含む配列を返す.
	 * Iteratorが返す要素の順番が保証される場合，配列の順番はこれに準ずる．
	 * @return すべての要素を含む配列
	 */
	byte[] toArray();

	/**
	 * 指定されたbyte値をこのコレクションに追加する(オプションの操作).
	 *
	 * @param b 追加する値
	 * @return 追加されればtrue
	 *
	 * @throws UnsupportedOperationException 操作が実装されていない場合
	 */
	boolean add(byte b);

	/**
	 * 指定された要素すべてをこのコレクションに追加する(オプションの操作).
	 *
	 * @param bs
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作が実装されていない場合
	 */
	default boolean addAll(byte... bs) {
		return addAll(bs, 0, bs.length);
	}

	/**
	 * 指定された配列の要素の一部区間をこのコレクションに追加する(オプションの操作).
	 *
	 * @param bs 配列
	 * @param offset 追加する区間の開始インデックス
	 * @param length 追加する区間の長さ
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作がサポートされていない場合
	 * @throws IndexOutOfBoundsException offsetまたはlengthが不正だった場合
	 * @throws NullPointerException 配列がnullだった場合
	 */
	boolean addAll(byte[] bs, int offset, int length);

	/**
	 * 指定された要素すべてをこのコレクションに追加する(オプションの操作).
	 *
	 * @param bs
	 * @return このコレクションが変更されればtrue
	 *
	 * @throws UnsupportedOperationException 操作が実装されていない場合
	 * @throws NullPointerException コレクションがnullだった場合
	 */
	boolean addAll(ByteCollection bs);

	/**
	 * 指定された要素をこのコレクションから削除する(オプションの操作).
	 *
	 * @param b
	 * @return 指定された要素を保持していればtrue
	 *
	 * @throws UnsupportedOperationException 操作が実装されていない場合
	 */
	boolean remove(byte b);

	/**
	 * 指定された要素すべてをこのコレクションから削除する(オプションの操作).
	 *
	 * @param bs
	 * @return 指定された要素を保持していればtrue
	 *
	 * @throws UnsupportedOperationException 操作が実装されていない場合
	 */
	boolean removeAll(byte... bs);

	/**
	 * 指定された要素すべてをこのコレクションから削除する(オプションの操作).
	 *
	 * @param bs
	 * @return 指定された要素を保持していればtrue
	 *
	 * @throws UnsupportedOperationException 操作が実装されていない場合
	 */
	boolean removeAll(ByteCollection bs);

	/**
	 * 指定されたbyte値がすべてこのコレクションに含まれていればtrueを返す.
	 * @param bs
	 * @return 指定されたbyte値がすべて含まれていればtrue
	 */
	boolean containsAll(byte... bs);

	/**
	 * 指定されたコレクション値がすべてこのコレクションに含まれていればtrueを返す.
	 * @param bs
	 * @return 指定されたbyte値がすべて含まれていればtrue
	 */
	boolean containsAll(ByteCollection bs);

	/**
	 * このByteCollectionから指定された要素以外の要素を全て削除する(オプションの操作).
	 * @param bs
	 * @return 要素が1つでも削除されればtrue
	 *
	 *  @throws UnsupportedOperationException 操作が実装されていない場合
	 */
	boolean retainAll(byte... bs);

	/**
	 * このByteCollectionから指定された要素以外の要素を全て削除する(オプションの操作).
	 * @param bs
	 * @return 要素が1つでも削除されればtrue
	 *
	 *  @throws UnsupportedOperationException 操作が実装されていない場合
	 */
	boolean retainAll(ByteCollection is);

	/**
	 * このコレクションに対する逐次的なIntStreamを返す．byte値は符号付きとみなされる．
	 * @return stream
	 */
	default IntStream streamSigned() {
		return StreamSupport.intStream(spliteratorSigned(), false);
	}

	/**
	 * このコレクションに対する逐次的なIntStreamを返す．byte値は符号無しとみなされる．
	 * @return stream
	 */
	default IntStream streamUnsigned() {
		return StreamSupport.intStream(spliteratorUnsigned(), false);
	}

	/**
	 * このコレクションに対する並列なIntStreamを返す．byte値は符号付きとみなされる．
	 * @return stream
	 */
	default IntStream parallelStreamSigned() {
		return StreamSupport.intStream(spliteratorSigned(), true);
	}

	/**
	 * このコレクションに対する並列なIntStreamを返す．byte値は符号無しとみなされる．
	 * @return stream
	 */
	default IntStream parallelStreamUnsigned() {
		return StreamSupport.intStream(spliteratorUnsigned(), true);
	}

	/**
	 * このコレクションの要素に対する反復的な操作を提供するSpliteratorを返す.
	 * @return spliterator
	 */
	@Override
	default Spliterator.OfInt spliteratorSigned() {
		return Spliterators.spliterator(iteratorSigned(), size(),
				Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED);
	}

	@Override
	default Spliterator.OfInt spliteratorUnsigned() {
		return Spliterators.spliterator(iteratorUnsigned(), size(),
				Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED);
	}

	/**
	 * このコレクションのCollection&lt;Integer&gt;のビューを返す.
	 * ビューに対する操作はこのインタンスに反映される．
	 * @return このIntCollectionのCollection&lt;Integer&gt;のビュー
	 */
	@Override
	default Collection<Byte> boxedView() {
		return PrimitiveSupport.boxed(this);
	}

	public static ByteCollection empty() {
		return EmptyCollection.ofByte();
	}
}
