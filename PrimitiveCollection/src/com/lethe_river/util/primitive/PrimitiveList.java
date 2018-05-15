package com.lethe_river.util.primitive;

import java.util.List;
import java.util.ListIterator;
import java.util.PrimitiveIterator;

/**
 * プリミティブを格納するList
 *
 * @param <T>
 *
 * @author YuyaAizawa
 */
public interface PrimitiveList<T> extends PrimitiveCollection<T> {
	/**
	 * 指定されたオブジェクトとこのコレクションが集合として等しいかどうかを比較する.
	 * 対称性に関する規約に基づき，java.util.Listとの比較結果は必ずfalseとなる．
	 * 比較が必要な場合は{@link PrimitiveList#boxedView()}で取得したインスタンスを利用せよ．
	 * @return 等しければtrue
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * このリストのハッシュコードを返す.
	 * java.util.Listと互換性がある．
	 * @return ハッシュ
	 */
	@Override
	int hashCode();

	/**
	 * ラッパー型のListのビューを返す.
	 * ビューに対する操作はこのインスタンスに反映される．
	 * @return このPrimitiveCollectionのラッパー型のビュー
	 */
	@Override
	List<T> boxedView();

	/**
	 * このリスト内の要素を逆順で繰り返し処理する反復子を返す．
	 * @return このリスト要素の逆順繰り返し処理に関する{@link PrimitiveIterator}
	 */
	PrimitiveIterator<T, ?> reversedIterator();

	/**
	 * このリスト内の要素を適切な順序で繰り返し処理する反復子を返す．
	 * @return このリスト要素の繰り返し処理に関する{@link ListIterator}
	 */
	ListIterator<T> listIterator();

	/**
	 * このリスト内の要素を指定した位置から適切な順序で繰り返し処理する反復子を返す．
	 * @param index 繰り返しの開始位置
	 * @return このリスト要素の繰り返し処理に関する{@link ListIterator}
	 * @throws IndexOutOfBoundsException index &lt; sizeまたはsize+1 &lt; indexのとき
	 */

	ListIterator<T> listIterator(int index);

	/**
	 * このリストの指定したインデックス範囲のビューを返す
	 * @param fromIndex 範囲の開始位置(このインデックスの要素を含む)
	 * @param toIndex 範囲の終了位置(このインデックスの要素を含まない)
	 * @return 指定したインデックス範囲のビュー
	 */

	PrimitiveList<T> subList(int fromIndex, int toIndex);
}
