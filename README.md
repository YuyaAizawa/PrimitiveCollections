# PrimitiveCollections
プリミティブ型に特化したコレクション

## コンセプト
- 省メモリ，高速
    - boxingしない
    - プリミティブのStream
- 一部標準ライブラリへのラッパー
- シリアライズ

## 主な実装
- List
    - ArrayByteList
    - ArrayIntList
    - BlockLinkedIntList
    - FrozenArrayIntList
- Set
    - BitFieldByteSet
    - CuckooHashIntSet
    - FrozenArrayByteSet
    - FrozenArrayIntSet
    - ScatterCharSet
    - ScatterIntSet
    - ScatterLongSet
- Map
    - ScatterIntIntMap
    - ScatterLongIntMap
- Utility
    - PrimitiveCollections

## その他
- Mapのキーにしたときもパフォーマンスが出るSet
    - Set<Integer>はhashCodeが内部の整数の和になるため，小さな整数だと衝突しやすい
