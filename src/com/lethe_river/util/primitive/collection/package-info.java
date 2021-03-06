/**
 * プリミティブを扱うコレクションが含まれる.
 *
 * 今のところint, long, byte, char用のみ．必要ができたら作る
 *
 * 主なコンセプト
 *
 * - 省メモリ，高速
 *   - プリミティブのStream
 * - シリアライズ
 * - 一部標準ライブラリへのラッパー
 *
 *
 * 実装クラスは以下のとおり
 * - PrimitiveList
 *   - ByteList
 *     - ArrayByteList
 *   - IntList
 *     - ArrayIntList
 *     - FrozenArrayIntList
 * - PrimitiveSet
 *   - ByteSet
 *     - BitFieldByteSet
 *     - FrozenArrayByteSet
 *     - ScatterByteSet
 *   - CharSet
 *     - ScatterCharSet
 *   - IntSet
 *     - CuckooHashIntSet
 *     - ScatterIntSet
 *     - FrozenArrayIntSet
 *   - LongSet
 *     - ScatterLongSet
 * - PrimitiveMap
 *   - IntIntMap
 *     - ScatterIntIntMap
 *   - LongIntMap
 *     - ScatterLongIntMap
 */
package com.lethe_river.util.primitive.collection;

/*
 * int, long, double以外のプリミティブのStreamをどうするか
 *   Iteratorは実装が簡単なのでIterableは同じデザインでよさそう
 *   Streamの実装がつらそう
 *     Spliterator.OfIntとかIntPipelineとか全部コピペするのはオリジナリティにかける
 *     実行速度を考えると他のプリミティブは使う意味が薄い(計測してないのでJITで何か変わるかもしれない)
 *     型は残しておきたい(特にbyte)
 *       Collectorでbyteに戻すのもありか
 *     SpliteratorとStreamの間でわけることを試みる
 *       Iterableがiterator()とspliterator()の型に一貫性を求めるので丁度良いような気がする
 *       問題はbyteをintに写すときやその逆だ
 *         byteは符号付だから-128~127を表すが，操作は符号なしの方が使いそうという問題
 *         Streamに変えるときに選べるようにするか？デフォルトはどっち？
 *       PrimitiveIterableでStreamの提供を切り分けることにした
 *         byte系はstreamを直接持たせず，streamSignedとstreamUnsignedにする
 *         次の問題としてByteListのreversedStream問題がある．コレも同じように切り分けるか...
 *
 * estimateSizeどうやって決めてるんだろう
 *
 * emptyのシングルトンをどこにおくか．
 *   EmptyCollectionsとかEmptyIteratorsとかに全種まとめるか
 *   そんでもってIntSetとかのstaticメソッドにemptyとかつけて呼べるようにするか
 *
 * Map.entrySetの互換性
 *   Entryごとにオブジェクトを生成するのはコストが大きい
 *     Entryをmutableにして再生成を抑制
 *       標準ライブラリはStream処理でEntryを一時的に保存するので不正な結果
 *     Entryを取り出す部分(boxedやらspliteratorやら)でオブジェクトを遅延生成
 *       標準ライブラリでは取り出したエントリは元のマップの変更に追従する
 *       互換性が保てないため，マップ系はboxedなしにするか
 *
 * 生成方法はクラスメソッドかnewか
 *   of(クラスメソッド)だとArrayIntList.ofとなって英語に近い
 *   newだとヒープの確保が明示的
 *
 * プリミティブのListIteratorはjava.utilを継承すべきか
 *   無いほうがシンプル
 *   あったほうが標準ライブラリへのラッパーコードが減る
 *   積極的になくす理由はない
 *
 * パッケージをまとめるか
 */