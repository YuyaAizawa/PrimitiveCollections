package com.lethe_river.util.primitive.collection;

import java.util.NoSuchElementException;
import java.util.function.IntUnaryOperator;

/**
 * intからintへの写像
 * @author YuyaAizawa
 *
 */
public interface IntIntMap extends PrimitiveMap<Integer, Integer> {

	/**
	 * 指定したint値がこのIntIntMapの始域に含まれるか判定する
	 * @param key
	 * @return 含まれればtrue
	 */
	boolean containsKey(int key);

	/**
	 * 指定したint値がこのIntIntMapの終域に含まれるか判定する
	 * @param value
	 * @return 含まれればtrue
	 */
	boolean containsValue(int value);

	/**
	 * 指定した元に対する像を返す．
	 * @param key 元
	 * @return 像
	 * @throws NoSuchElementException 指定された元に対する像が定義されていないとき
	 */
	int get(int key);

	/**
	 * 指定した元に対する像を返す．指定された元に対する像が定義されていないときはdefを返す．
	 * @param key 元
	 * @param def 像が存在しないときの値
	 * @return 像またはdefで指定した値
	 */
	int getOrDefault(int key, int def);

	/**
	 * 指定した元と像の対応をこの写像に定義する．既に同一の元に対する像が定義されていた場合置き換える．(オプションの操作)
	 * @param key 元
	 * @param value 像
	 * @throws UnsupportedOperationException この操作に対応していない場合
	 */
	default void put(int key, int value) {
		merge(key, value, old -> value);
	}

	/**
	 * 指定した元と像の対応をこの写像に定義するか，既に定義されている場合は更新する．
	 * 更新は古い値をとり新しい値を返す関数を指定する．
	 * @param key 元
	 * @param value 像
	 * @param updater 更新法
	 * @throws UnsupportedOperationException この操作に対応していない場合
	 */
	void merge(int key, int value, IntUnaryOperator updater);

	/**
	 * 指定した元をもつ対応関係をこの写像から取り除く
	 * @param key 元
	 * @return 指定された元が存在すればtrue
	 */
	boolean remove(int key);

	/**
	 * この写像の始域を返す．戻り値への変更はこの写像へ反映される．
	 * @return 始域
	 */
	@Override
	IntSet keys();

	/**
	 * この写像の終域を返す．戻り値への変更はこの写像へ反映される．
	 * @return 終域
	 */
	@Override
	IntCollection values();

	/**
	 * この写像の各元の対応関係を列挙するカーソルを返す．
	 * @return 対応関係を列挙するカーソル
	 */
	@Override
	IntIntCursor entryCursor();

	/**
	 * 指定されたオブジェクトがこの写像と同値か判定する．
	 * 元と像の対応関係が完全に一致すればtrue.
	 * IntIntMap以外に対しては常にfalse.
	 * @param obj
	 * @return
	 */
	@Override
	boolean equals(Object obj);
}
