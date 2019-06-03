# PrimitiveCollections
プリミティブ型に特化したコレクション

## コンセプト
- 省メモリ，高速
    - boxingしない
    - プリミティブのStream
- 一部標準ライブラリへのラッパー
- シリアライズ

## 具体的な長所
- Mapのキーにしたときもパフォーマンスが出るSet
    - Set<Integer>はhashCodeが内部の整数の和になるため，小さな整数だと衝突しやすい