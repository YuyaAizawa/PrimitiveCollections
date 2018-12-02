package com.lethe_river.util.primitive;

import java.util.List;
import java.util.ListIterator;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public interface IntList extends PrimitiveList<Integer>, IntCollection {

	/**
	 * このリストの最後に指定した要素を追加する(オプションの操作)
	 * @param i 追加する要素
	 * @return true
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	@Override
	default boolean add(int i) {
		insert(size(), i);
		return true;
	}

	/**
	 * このリストの最後に指定した要素をすべて追加する(オプションの操作)
	 * @param is 追加する要素
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	@Override
	boolean addAll(int... is);

	/**
	 * このリストの最後に指定された配列の要素の一部区間を追加する(オプションの操作).
	 *
	 * @param is 配列
	 * @param offset 追加する区間の開始インデックス
	 * @param length 追加する区間の長さ
	 *
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされていない場合
	 * @throws IndexOutOfBoundsException offsetまたはlengthが不正だった場合
	 * @throws NullPointerException 配列がnullだった場合
	 */
	@Override
	default boolean addAll(int[] is, int offset, int length) {
		insertAll(size(), is, offset, length);
		return length != 0;
	}

	/**
	 * このリストの最後に指定した要素をすべて追加する(オプションの操作)
	 * @param is 追加する要素
	 * @return
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	@Override
	default boolean addAll(IntCollection c) {
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
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	void insert(int index, int element);

	/**
	 * このリストの指定した位置に配列の要素を挿入的に要素を追加する(オプションの操作)
	 * @param index 挿入の開始インデックス
	 * @param is 配列
	 *
	 * @throws IndexOutOfBoundsException インデックスが範囲外の場合(index < 0 || index >= size())
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	default void insertAll(int index, int[] is) {
		insertAll(index, is, 0, is.length);
	}

	/**
	 * このリストの指定した位置に，配列の一部区間の要素を挿入的に追加する(オプションの操作)
	 * @param index 挿入の開始インデックス
	 * @param is 配列
	 * @param offset 区間の開始インデックス
	 * @param length 区間の長さ
	 * @return リストに変更があればtrue
	 *
	 * @throws IndexOutOfBoundsException index，offsetまたはlengthが不正な場合
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	void insertAll(int index, int[] is, int offset, int length);

	/**
	 * このリストの指定した位置に挿入的に要素を追加する(オプションの操作)
	 * @param index 置換される要素のインデックス
	 * @param c 指定された位置に格納される要素
	 * @return 指定された位置に以前あった要素
	 * @throws IndexOutOfBoundsException インデックスが範囲外の場合(index < 0 || index >= size())
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	void insertAll(int index, IntCollection c);

	/**
	 * このリストが指定した要素を含むとき，最初に出現するものを削除する(オプションの操作)
	 * @param i 削除する要素
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	@Override
	boolean remove(int i);

	/**
	 * このリストに出現する指定した要素をすべてを削除する(オプションの操作).
	 * @param is 削除する要素
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	@Override
	boolean removeAll(int... is);

	/**
	 * 指定したindexにある要素を削除する(オプションの操作).
	 *
	 * @param index
	 * @return 削除した要素
	 * @throws IndexOutOfBoundsException インデックスが範囲外の場合(index < 0 || index >= size())
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	int removeAt(int index);

	/**
	 * このリストの指定した位置にある要素を返す.
	 * @param index 返される要素のインデックス
	 * @return 指定された位置にある要素
	 * @throws IndexOutOfBoundsException インデックスが範囲外の場合(index < 0 || index >= size())
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	int get(int index);

	/**
	 * リスト内の指定された位置にある要素を，指定された要素に置き換える(オプションの操作)
	 * @param index 置換される要素のインデックス
	 * @param element 指定された位置に格納される要素
	 * @return 指定された位置に以前あった要素
	 * @throws IndexOutOfBoundsException インデックスが範囲外の場合(index < 0 || index >= size())
	 * @throws UnsupportedOperationException 操作がこのリストでサポートされない場合
	 */
	int set(int index, int element);

	/**
	 * このリスト中に指定した要素が最初に出現するindexを返す．このリストが指定した要素を含まなければ-1を返す．
	 * @param element
	 * @return 指定した要素の出現位置でもっとも小さいもの(出現しなければ-1)
	 */
	int indexOf(int element);

	/**
	 * このリスト中に指定した要素が最後に出現するindexを返す．このリストが指定した要素を含まなければ-1を返す．
	 * @param element
	 * @return 指定した要素の出現位置でもっとも大きいもの(出現しなければ-1)
	 */
	int lastIndexOf(int element);

	/**
	 * このIntListが引数の部分系列かどうか判定する
	 * @param target
	 * @return 部分系列ならtrue
	 */
	default boolean isSubsequence(IntList list) {
		if(this.size() > list.size()) {
			return false;
		}
		PrimitiveIterator.OfInt i1 = this.iterator();
		PrimitiveIterator.OfInt i2 = list.iterator();

		loop: while(i1.hasNext()) {
			int t1 = i1.nextInt();
			while(i2.hasNext()) {
				int t2 = i2.nextInt();
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
	default boolean isSupersequenceOf(IntList list) {
		return list.isSubsequence(this);
	}

	/**
	 * このリスト内の要素を指定した位置から適切な順序で繰り返し処理する反復子を返す．
	 * @param index 繰り返しの開始位置
	 * @return このリスト要素の繰り返し処理に関する{@link ListIterator}
	 * @throws IndexOutOfBoundsException index &lt; sizeまたはsize+1 &lt; indexのとき
	 */
	@Override
	IntListIterator listIterator(int index);

	@Override
	default IntListIterator listIterator() {
		return listIterator(0);
	}

	/**
	 * このリスト内の要素を逆順で繰り返し処理する反復子を返す．
	 * @return このリスト要素の逆順繰り返し処理に関する{@link PrimitiveIterator}
	 */
	@Override
	PrimitiveIterator.OfInt reversedIterator();

	/**
	 * このリストを逆順に処理する逐次的なIntStreamを返す．
	 * @return stream
	 */
	default IntStream reversedStream() {
		return StreamSupport.intStream(
				Spliterators.spliterator(
						reversedIterator(),
						size(),
						Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED),
				false);
	}

	/**
	 * このリストの要素を列挙するSpliteratorを返す.
	 * @return spliterator
	 */
	@Override
	default Spliterator.OfInt spliterator() {
		return Spliterators.spliterator(iterator(), size(),
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
	IntList subList(int fromIndex, int toIndex);

	@Override
	List<Integer> boxedView();

	/**
	 * 空のIntListを表すSingletonのインスタンスを返す
	 * @return 空のIntList
	 */
	public static IntList empty() {
		return EmptyList.ofInt();
	}

	/**
	 * 不変IntListを生成する
	 * @return 不変IntList
	 */
	public static IntList of(int... is) {
		if(is.length == 0) {
			return empty();
		} else {
			return new FrozenArrayIntList(is);
		}
	}
}

