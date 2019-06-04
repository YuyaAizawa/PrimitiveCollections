package com.lethe_river.util.primitive.collection;

import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;

public interface ByteSet extends PrimitiveSet<Byte>, ByteCollection {
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
	boolean contains(byte b);

	/**
	 * {@inheritDoc}
	 */
	@Override
	ByteIterator iterator();

	/**
	 * {@inheritDoc}
	 */
	@Override
	byte[] toArray();

	/**
	 * 指定されたbyte値をこのByteSetに追加する(オプションの操作).
	 *
	 * @param b 追加する値
	 * @return 追加されればtrue
	 *
	 * @throws UnsupportedOperationException 操作が実装されていない場合
	 */
	@Override
	boolean add(byte b);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean addAll(byte... bs);

	/**
	 * 指定された配列の要素の一部区間をこのByteSetに追加する(オプションの操作)
	 *
	 * @param bs 配列
	 * @param offset 追加する区間の開始インデックス
	 * @param length 追加する区間の長さ
	 *
	 * @throws UnsupportedOperationException 操作が実装されていない場合
	 */
	@Override
	boolean addAll(byte[] bs, int offset, int length);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean addAll(ByteCollection bs);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean remove(byte b);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean removeAll(byte... bs);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean removeAll(ByteCollection bs);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean containsAll(byte... bs);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean retainAll(byte... bs);

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean retainAll(ByteCollection bs);

	/**
	 * 指定した集合の要素すべてがこの集合に含まれるか判定する.
	 * @param bs
	 * @return 含まれればture
	 */
	boolean containsAll(ByteSet bs);

	/**
	 * この集合の要素を列挙するSpliteratorを返す.
	 * @return spliterator
	 */
	@Override
	default Spliterator.OfInt spliteratorSigned() {
		return Spliterators.spliterator(iteratorSigned(), size(),
				Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.DISTINCT);
	}

	/**
	 * このコレクションのSet&lt;Byte&gt;のビューを返す.
	 * ビューに対する操作はこのインタンスに反映される．
	 * @return このByteSetのSet&lt;Byte&gt;のビュー
	 */
	@Override
	Set<Byte> boxedView();

	public static ByteSet empty() {
		return EmptySet.ofByte();
	}
}
