package com.lethe_river.util.primitive;

import java.util.NoSuchElementException;

/**
 * longからintへの写像
 * @author YuyaAizawa
 *
 */
public interface LongIntMap extends PrimitiveMap<Long, Integer> {

	@Override
	boolean equals(Object obj);

	@Override
	int hashCode();

	/**
	 * この写像で定義された元と像の対応関係の数を返す．
	 * @return 要素数
	 */
	@Override
	int size();

	/**
	 * 指定したlong値がこのLongIntMapの始域に含まれるか判定する
	 * @param key
	 * @return 含まれればtrue
	 */
	boolean containsKey(long key);

	/**
	 * {@inheritDoc}
	 */
	@Override
	default boolean containsKey(Long key) {
		return containsKey((long)key);
	}

	/**
	 * 指定したlong値がこのLongIntMapの終域に含まれるか判定する
	 * @param value
	 * @return 含まれればtrue
	 */
	boolean containsValue(int value);

	/**
	 * {@inheritDoc}
	 */
	@Override
	default boolean containsValue(Integer value) {
		return containsValue((int)value);
	}

	/**
	 * このLongIntMapで定義された対応関係を全て削除する(オプションの操作)
	 */
	@Override
	void clear();

	/**
	 * 指定した元に対する像を返す．
	 * @param key 元
	 * @return 像
	 * @throws NoSuchElementException 指定された元に対する像が定義されていないとき
	 */
	int get(long key);

	/**
	 * {@inheritDoc}
	 */
	@Override
	default Integer get(Long key) {
		return get((long)key);
	}

	/**
	 * 指定した元に対する像を返す．指定された元に対する像が定義されていないときはdefを返す．
	 * @param key 元
	 * @param def 像が存在しないときの値
	 * @return 像またはdefで指定した値
	 */
	int getOrDefault(long key, int def);

	/**
	 * {@inheritDoc}
	 */
	@Override
	default Integer getOrDefault(Long key, Integer def) {
		return getOrDefault((long)key, (int)def);
	}

	/**
	 * 指定した元と像の対応をこの写像に定義する．既に同一の元に対する像が定義されていた場合置き換える．(オプションの操作)
	 * @param key 元
	 * @param value 像
	 * @throws UnsupportedOperationException この操作に対応していない場合
	 */
	void put(long key, int value);

	/**
	 * {@inheritDoc}
	 */
	@Override
	default void put(Long key, Integer value) {
		put((long)key, (int)value);
	}

	/**
	 * 指定した元をもつ対応関係をこの写像から取り除く
	 * @param key 元
	 * @return 指定された元が存在すればtrue
	 */
	boolean remove(long key);

	/**
	 * {@inheritDoc}
	 */
	@Override
	default boolean remove(Long key) {
		return remove((long)key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	LongSet keys();

	/**
	 * {@inheritDoc}
	 */
	@Override
	IntCollection values();

	/**
	 * {@inheritDoc}
	 */
	@Override
	LongIntCursor entryCursor();
}