package com.lethe_river.util.primitive;

import java.util.PrimitiveIterator;

class ArrayIntIterator implements PrimitiveIterator.OfInt {

	int[] array;
	int index = 0;

	public ArrayIntIterator(int [] array) {
		this.array = array;
	}

	@Override
	public boolean hasNext() {
		return index < array.length;
	}

	@Override
	public int nextInt() {
		index++;
		return array[index];
	}
}
