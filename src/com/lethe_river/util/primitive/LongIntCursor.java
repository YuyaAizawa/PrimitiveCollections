package com.lethe_river.util.primitive;

public interface LongIntCursor extends PrimitiveMapEntryCursor<Long, Integer> {

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
	long key();

	@Override
	default Long keyAsObject() {
		return Long.valueOf(key());
	}

	/**
	 * 現在のカーソルが指すバリューを返す.
	 * @return バリュー
	 * @throws IllegalStateException カーソルの位置が不正な場合
	 */
	int value();

	@Override
	default Integer valueAsObject() {
		return Integer.valueOf(value());
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
	public static LongIntCursor empty() {
		return EmptyCursor.ofLongInt();
	}
}
