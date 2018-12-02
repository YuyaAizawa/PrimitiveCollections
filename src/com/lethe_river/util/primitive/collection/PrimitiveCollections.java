package com.lethe_river.util.primitive.collection;

public final class PrimitiveCollections {
	private PrimitiveCollections(){}

	/**
	 * 指定したIntCollectionの変更不可能なビューを返す
	 * @return 変更不可能なビュー
	 */
	public static IntCollection unmodifiableCollection(IntCollection original) {
		return PrimitiveSupport.unmodifiableCollection(original);
	}

	/**
	 * 指定したIntListの変更不可能なビューを返す
	 * @return 変更不可能なビュー
	 */
	public static IntList unmodifiableList(IntList original) {
		return PrimitiveSupport.unmodifiableList(original);
	}

}
