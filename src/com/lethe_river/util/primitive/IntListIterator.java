package com.lethe_river.util.primitive;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

/**
 * ListIteratorのプリミティブ版
 * @author YuyaAizawa
 *
 */
public interface IntListIterator extends ListIterator<Integer>, PrimitiveIterator.OfInt {

	@Override
	default void add(Integer e) {
		add(e.intValue());
	}

	@Override
	default Integer next() {
		return Integer.valueOf(nextInt());
	}

	@Override
	default Integer previous() {
		return Integer.valueOf(previousInt());
	}

	@Override
	default void set(Integer e) {
		set(e.intValue());
	}

	/**
	 * 指定された要素をリストに挿入する．挿入する位置はnextInt()が返す要素の直前，previousInt()が返す要素の直後.
	 * @param element
	 */
	void add(int e);

	/**
	 * リスト内の次の要素を返す．
	 * @return 次の要素
	 * @throws NoSuchElementException 次の要素がない場合
	 */
	@Override
	int nextInt();

	/**
	 * リスト内の前の要素を返す
	 * @return 前の要素
	 * @throws NoSuchElementException 前の要素がない場合
	 */
	int previousInt();

	/**
	 * next()またはprevious()から最後に返された要素を指定された要素で置き換える(オプションの操作).
	 * 最後のnext(), previous()の呼び出しからremove(),add(int)のどちらも呼ばれていないときのみ行える
	 * @param e
	 */
	void set(int e);

	/**
	 * 空のIntListIteratorのシングルトンインスタンスを返す．
	 * @return 空のIntListIterator
	 */
	static IntListIterator empty() {
		return EmptyListIterator.ofInt();
	}
}
