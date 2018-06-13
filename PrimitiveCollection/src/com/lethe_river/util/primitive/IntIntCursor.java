package com.lethe_river.util.primitive;

public interface IntIntCursor extends PrimitiveMapEntryCursor<Integer, Integer> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean next();

	/**
	 * 現在のカーソルが指すキーを返す.
	 * @return キー
	 * @throws IllegalStateException カーソルの位置が不正な場合
	 */
	int key();

	@Override
	default Integer keyAsObject() {
		return key();
	}

	/**
	 * 現在のカーソルが指すバリューを返す.
	 * @return バリュー
	 * @throws IllegalStateException カーソルの位置が不正な場合
	 */
	int value();

	@Override
	default Integer valueAsObject() {
		return value();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void remove();

	/**
	 * 現在のカーソルが指すバリューを更新する(オプションの操作)
	 * @throws UnsupportedOperationException この機能がサポートされていない場合
	 * @throws IllegalStateException カーソルの位置が不正な場合
	 */
	void setValue(int value);

	/**
	 * 空のカーソルを返す
	 * @return 空のカーソル
	 */
	public static IntIntCursor empty() {
		return EmptyCursor.ofIntInt();
	}
}
