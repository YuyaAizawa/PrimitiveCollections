package com.lethe_river.util.primitive;

import java.io.Serializable;

/**
 * primitiveからprimitiveへの写像.
 *
 * @author YuyaAizawa
 *
 * @param <K> 元の型
 * @param <V> 像の型
 */
public interface PrimitiveMap<K, V> extends Serializable {
	/**
	 * この写像で定義された元と像の対応関係の数を返す．
	 * @return 要素数
	 */
	int size();

	/**
	 * この写像で定義された対応関係を全て削除する(オプションの操作)
	 */
	void clear();

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

	/**
	 * 各対応関係(k, v)に対してk*31 + vの総和を返す
	 */
	@Override
	int hashCode();
}
