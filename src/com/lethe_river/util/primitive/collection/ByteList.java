package com.lethe_river.util.primitive.collection;

import java.util.List;
import java.util.ListIterator;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public interface ByteList extends PrimitiveList<Byte>, ByteCollection {

	/**
	 * このリストの最後に指定した要素を追加する(オプションの操作)
	 * @param b 追加する要素
	 * @return true
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	@Override
	default boolean add(byte b) {
		insert(size(), b);
		return true;
	}


	/**
	 * このリストの最後に指定した要素をすべて追加する(オプションの操作)
	 * @param bs 追加する要素
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	@Override
	boolean addAll(byte... bs);

	/**
	 * このリストの最後に指定された配列の要素の一部区間を追加する(オプションの操作)
	 *
	 * @param bs 配列
	 * @param offset 追加する区間の開始インデックス
	 * @param length 追加する区間の長さ
	 *
	 * @throws UnsupportedOperationException 操作が実装されていない場合
	 * @throws IndexOutOfBoundsException offsetまたはlengthが不正だった場合
	 * @throws NullPointerException 配列がnullだった場合
	 */
	@Override
	default boolean addAll(byte[] bs, int offset, int length) {
		insertAll(size(), bs, offset, length);
		return length != 0;
	}

	/**
	 * このリストの最後に指定した要素をすべて追加する(オプションの操作)
	 * @param bs 追加する要素
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	@Override
	default boolean addAll(ByteCollection c) {
		if(c.isEmpty()) {
			return false;
		}
		insertAll(size(), c);
		return true;
	}

	/**
	 * このリストの指定した位置に挿入的に要素を追加する(オプションの操作)
	 * @param index 挿入する位置
	 * @param element 追加する要素
	 *
	 * @throws IndexOutOfBoundsException インデックスが範囲外の場合(index < 0 || index >= size())
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	void insert(int index, byte element);

	/**
	 * このリストの指定した位置に配列の要素を挿入的に要素を追加する(オプションの操作)
	 * @param index 挿入の開始インデックス
	 * @param bs 配列
	 *
	 * @throws IndexOutOfBoundsException インデックスが範囲外の場合(index < 0 || index >= size())
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	default void insertAll(int index, byte[] bs) {
		insertAll(index, bs, 0, bs.length);
	}

	/**
	 * このリストの指定した位置に，配列の一部区間の要素を挿入的に追加する(オプションの操作)
	 * @param index 挿入の開始インデックス
	 * @param bs 配列
	 * @param offset 区間の開始インデックス
	 * @param length 区間の長さ
	 * @return リストに変更があればtrue
	 *
	 * @throws IndexOutOfBoundsException index，offsetまたはlengthが不正な場合
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	void insertAll(int index, byte[] bs, int offset, int length);

	/**
	 * このリストの指定した位置に挿入的に要素を追加する(オプションの操作)
	 * @param index 挿入の開始インデックス
	 * @param c 指定された位置に格納される要素
	 *
	 * @throws IndexOutOfBoundsException インデックスが範囲外の場合(index < 0 || index >= size())
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	void insertAll(int index, ByteCollection c);

	/**
	 * このリストが指定した要素を含むとき，最初に出現するものを削除する(オプションの操作)
	 * @param b 削除する要素
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	@Override
	boolean remove(byte b);

	/**
	 * このリストに出現する指定した要素をすべてを削除する(オプションの操作).
	 * @param bs 削除する要素
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	@Override
	boolean removeAll(byte... bs);

	/**
	 * 指定したindexにある要素を削除する(オプションの操作).
	 *
	 * @param index
	 * @return 削除した要素
	 * @throws IndexOutOfBoundsException インデックスが範囲外の場合(index < 0 || index >= size())
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	byte removeAt(int index);

	/**
	 * このリストの指定した位置にある要素を返す.
	 * @param index 返される要素のインデックス
	 * @return 指定された位置にある要素
	 * @throws IndexOutOfBoundsException インデックスが範囲外の場合(index < 0 || index >= size())
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	byte get(int index);

	/**
	 * リスト内の指定された位置にある要素を，指定された要素に置き換える(オプションの操作)
	 * @param index 置換される要素のインデックス
	 * @param element 指定された位置に格納される要素
	 * @return 指定された位置に以前あった要素
	 * @throws IndexOutOfBoundsException インデックスが範囲外の場合(index < 0 || index >= size())
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	byte set(int index, byte element);

	/**
	 * このリスト中に指定した要素が最初に出現するindexを返す．このリストが指定した要素を含まなければ-1を返す．
	 * @param element
	 * @return 指定した要素の出現位置でもっとも小さいもの(出現しなければ-1)
	 */
	int indexOf(byte element);

	/**
	 * このリスト中に指定した要素が最後に出現するindexを返す．このリストが指定した要素を含まなければ-1を返す．
	 * @param element
	 * @return 指定した要素の出現位置でもっとも大きいもの(出現しなければ-1)
	 */
	int lastIndexOf(byte element);

	/**
	 * このIntListが引数の部分系列かどうか判定する
	 * @param target
	 * @return 部分系列ならtrue
	 */
	default boolean isSubsequence(ByteList list) {
		if(this.size() > list.size()) {
			return false;
		}
		ByteIterator i1 = this.iterator();
		ByteIterator i2 = list.iterator();

		loop: while(i1.hasNext()) {
			int t1 = i1.nextByte();
			while(i2.hasNext()) {
				int t2 = i2.nextByte();
				if(t1 == t2) {
					continue loop;
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * このIntListが引数の上位系列かどうか判定する
	 * @param target
	 * @return 上位系列ならtrue
	 */
	default boolean isSupersequenceOf(ByteList list) {
		return list.isSubsequence(this);
	}

	/**
	 * このリスト内の要素を指定した位置から適切な順序で繰り返し処理する反復子を返す．
	 * @param index 繰り返しの開始位置
	 * @return このリスト要素の繰り返し処理に関する{@link ListIterator}
	 *
	 * @throws IndexOutOfBoundsException index &lt; sizeまたはsize+1 &lt; indexのとき
	 */
	@Override
	ByteListIterator listIterator(int index);

	@Override
	default ByteListIterator listIterator() {
		return listIterator(0);
	}

	/**
	 * このリスト内の要素を逆順で繰り返し処理する反復子を返す．
	 * @return このリスト要素の逆順繰り返し処理に関する{@link PrimitiveIterator}
	 */
	@Override
	ByteIterator reversedIterator();

	/**
	 * このリスト内の要素を符号付き整数として逆順で繰り返し処理する反復子を返す．
	 * @return このリスト要素の逆順繰り返し処理に関する{@link PrimitiveIterator}
	 */
	PrimitiveIterator.OfInt reversedIteratorSigned();

	/**
	 * このリスト内の要素を符号無し整数として逆順で繰り返し処理する反復子を返す．
	 * @return このリスト要素の逆順繰り返し処理に関する{@link PrimitiveIterator}
	 */
	PrimitiveIterator.OfInt reversedIteratorUnsigned();

	/**
	 * このリストを逆順に処理する逐次的なIntStreamを返す．
	 * @return stream
	 */
	default IntStream reversedStreamSigned() {
		return StreamSupport.intStream(
				Spliterators.spliterator(
						reversedIteratorSigned(),
						size(),
						Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED),
				false);
	}

	/**
	 * このリストを逆順に処理する逐次的なIntStreamを返す．
	 * @return stream
	 */
	default IntStream reversedStreamUnsigned() {
		return StreamSupport.intStream(
				Spliterators.spliterator(
						reversedIteratorUnsigned(),
						size(),
						Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED),
				false);
	}

	/**
	 * このリストの要素を列挙するSpliteratorを返す.
	 * @return spliterator
	 */
	@Override
	default Spliterator.OfInt spliteratorSigned() {
		return Spliterators.spliterator(iteratorSigned(), size(),
				Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED);
	}

	/**
	 * このリストの要素を列挙するSpliteratorを返す.
	 * @return spliterator
	 */
	@Override
	default Spliterator.OfInt spliteratorUnsigned() {
		return Spliterators.spliterator(iteratorUnsigned(), size(),
				Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED);
	}

	/**
	 * このリストの指定された範囲のインデックスに対するビューを返す.
	 * ビューに対する操作は元のリストに反映される.
	 * 元のリストに対する構造的変更後のビューの動作は未定義．
	 * @param fromIndex ビューの開始インデックス(この値を含む)
	 * @param toIndex ビューの終了インデックス(この値を含まず)
	 * @return 部分リストのビュー
	 */
	@Override
	ByteList subList(int fromIndex, int toIndex);

	@Override
	List<Byte> boxedView();
}
