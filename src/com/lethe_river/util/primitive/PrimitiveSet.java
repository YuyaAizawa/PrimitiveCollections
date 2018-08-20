package com.lethe_river.util.primitive;

import java.util.Set;

/**
 * プリミティブを格納するSet
 * @author YuyaAizawa
 *
 * @param <T> 格納する要素のラッパー型
 */
public interface PrimitiveSet<	T> extends PrimitiveCollection<T> {

	/**
	 * 指定されたオブジェクトとこのコレクションが集合として等しいかどうかを比較する.
	 * 対称性に関する規約に基づき，java.util.Setとの比較結果は必ずfalseとなる．
	 * 比較が必要な場合は{@link PrimitiveSet#boxedView()}で取得したインスタンスを利用せよ．
	 * @return 等しければtrue
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * この集合の要素のハッシュ値の合計を返す.
	 * java.util.Setと互換性がある．
	 * @return ハッシュ
	 */
	@Override
	int hashCode();

	/**
	 * ラッパー型のSetのビューを返す.
	 * ビューに対する操作はこのインスタンスに反映される．
	 * @return このPrimitiveCollectionのラッパー型のビュー
	 */
	@Override
	Set<T> boxedView();
}
