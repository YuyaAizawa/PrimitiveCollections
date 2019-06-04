package com.lethe_river.util.primitive.collection;

public interface PrimitiveMapEntryCursor<K, V> {

	/**
	 * カーソル位置を1つ進める．進んだ先に要素があればtrueを返す.
	 * @return 要素があればtrue
	 */
	boolean next();

	/**
	 * 現在のカーソルが指すキーをオブジェクトとして返す.
	 * @return キー
	 * @throws IllegalStateException カーソルの位置が不正な場合
	 */
	K keyAsObject();

	/**
	 * 現在のカーソルが指すバリューをオブジェクトとして返す.
	 * @return バリュー
	 * @throws IllegalStateException カーソルの位置が不正な場合
	 */
	V valueAsObject();

	/**
	 * 現在のカーソルが指す要素を削除する(オプションの操作)
	 * @throws UnsupportedOperationException この機能がサポートされていない場合
	 * @throws IllegalStateException カーソルの位置が不正な場合
	 */
	void remove();
}
