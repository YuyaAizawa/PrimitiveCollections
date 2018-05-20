package com.lethe_river.util.primitive;

import java.util.NoSuchElementException;

/**
 * primitiveからprimitiveへの写像
 * @author YuyaAizawa
 *
 * @param <K> 元の型
 * @param <V> 像の型
 */
public interface PrimitiveMap<K, V> {
	/**
	 * この写像で定義された元と像の対応関係の数を返す．
	 * @return 要素数
	 */
	int size();

	/**
	 * 指定したキー値がこの写像の始域に含まれるか判定する
	 * @param key
	 * @return 含まれればtrue
	 */
	boolean containsKey(K key);

	/**
	 * 指定したバリュー値がこの写像の終域に含まれるか判定する
	 * @param value
	 * @return 含まれればtrue
	 */
	boolean containsValue(V value);

	/**
	 * この写像で定義された対応関係を全て削除する(オプションの操作)
	 */
	void clear();

	/**
	 * 指定した元に対する像を返す．
	 * @param key 元
	 * @return 像
	 * @throws NoSuchElementException 指定された元に対する像が定義されていないとき
	 */
	V get(K key);

	/**
	 * 指定した元に対する像を返す．指定された元に対する像が定義されていないときはdefを返す．
	 * @param key 元
	 * @param def 像が存在しないときの値
	 * @return 像またはdefで指定した値
	 */
	V getOrDefault(K key, V def);

	/**
	 * 指定した元と像の対応をこの写像に定義する．既に同一の元に対する像が定義されていた場合置き換える．(オプションの操作)
	 * @param key 元
	 * @param value 像
	 * @throws UnsupportedOperationException この操作に対応していない場合
	 */
	void put(K key, V value);

	/**
	 * 指定した元をもつ対応関係をこの写像から取り除く
	 * @param key 元
	 * @return 指定された元が存在すればtrue
	 */
	boolean remove(K key);

	/**
	 * この写像の始域を返す．戻り値への変更はこの写像へ反映される．
	 * @return 始域
	 */
	PrimitiveSet<K> keys();

	/**
	 * この写像の終域を返す．戻り値への変更はこの写像へ反映される．
	 * @return 終域
	 */
	PrimitiveCollection<V> values();

	/**
	 * この写像の各元の対応関係を列挙するカーソルを返す．
	 * @return 対応関係を列挙するカーソル
	 */
	PrimitiveMapEntryCursor<K, V> entryCursor();

	/**
	 * 指定されたオブジェクトがこの写像と同値か判定する．
	 * 元と像の対応関係が完全に一致すればtrue.
	 * PrimitiveMap以外に対しては常にfalse.
	 * @param obj
	 * @return
	 */
	@Override
	boolean equals(Object obj);

	@Override
	int hashCode();
}
