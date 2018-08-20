package com.lethe_river.util.primitive;

import java.io.Serializable;
import java.util.Collection;

/**
 * プリミティブを格納するCollection
 *
 * 実装する場合は対応するプリミティブTに対して以下のメソッドを実装すること
 * boolean contains(T t)
 *
 * @author YuyaAizawa
 *
 * @param <T> 格納する要素のラッパー型
 */
public interface PrimitiveCollection<T> extends Serializable {

	/**
	 * 指定されたオブジェクトとこのコレクションが等しいかどうかを比較する.
	 * 対称性に関する規約に基づき，java.util.Collectionとの比較結果は必ずfalseとなる．
	 * 比較が必要な場合は{@link PrimitiveCollection#boxedView()}で取得したインスタンスを利用せよ．
	 * @return 等しければtrue
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * このコレクションが空ならtrueを返す.
	 * @return 空ならtrue
	 */
	boolean isEmpty();

	/**
	 * このコレクションの要素数を返す.
	 * 要素数がInteger.MAX_VALUEより大きい場合はInteger.MAX_VALUEを返す.
	 * @return 要素数
	 */
	int size();

	/**
	 * このコレクションの要素すべてを削除する(オプションの操作)．
	 */
	void clear();

	/**
	 * ラッパー型のCollectionのビューを返す.
	 * ビューに対する操作はこのインスタンスに反映される．
	 * @return このPrimitiveCollectionのラッパー型のビュー
	 */
	Collection<T> boxedView();
}

