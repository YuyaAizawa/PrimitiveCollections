package com.lethe_river.util.primitive.collection;

import java.util.PrimitiveIterator;

final class ArrayIterator {
	private ArrayIterator() {}

	public static PrimitiveIterator.OfInt of(int[] array) {
		return new PrimitiveIterator.OfInt() {
			int index = 0;

			@Override
			public boolean hasNext() {
				return index < array.length;
			}

			@Override
			public int nextInt() {
				return array[index++];
			}
		};
	}
}
