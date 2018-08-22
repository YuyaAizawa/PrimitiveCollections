package com.lethe_river.util.primitive;

import java.util.ListIterator;
import java.util.NoSuchElementException;

public interface ByteListIterator extends ListIterator<Byte>, ByteIterator {
	@Override
	default void add(Byte e) {
		add(e.byteValue());
	}

	@Override
	default Byte next() {
		return Byte.valueOf(nextByte());
	}

	@Override
	default Byte previous() {
		return Byte.valueOf(previousByte());
	}

	@Override
	default void set(Byte e) {
		set(e.byteValue());
	}

	/**
	 * 指定された要素をリストに挿入する(オプションの操作)．挿入する位置はnextByte()が返す要素の直前，previousByte()が返す要素の直後.
	 * @param element
	 */
	void add(byte e);

	/**
	 * リスト内の次の要素を返す．
	 * @return 次の要素
	 * @throws NoSuchElementException 次の要素がない場合
	 */
	@Override
	byte nextByte();

	/**
	 * リスト内の前の要素を返す
	 * @return 前の要素
	 * @throws NoSuchElementException 前の要素がない場合
	 */
	byte previousByte();

	/**
	 * next()またはprevious()から最後に返された要素を指定された要素で置き換える(オプションの操作).
	 * 最後のnext(), previous()の呼び出しからremove(),add(byte)のどちらも呼ばれていないときのみ行える
	 * @param e
	 */
	void set(byte e);

}
