package com.lethe_river.util.primitive;

import static org.junit.Assert.assertEquals;

import java.util.stream.IntStream;

import org.junit.Test;

import com.lethe_river.util.primitive.collection.ArrayIntList;
import com.lethe_river.util.primitive.collection.IntList;

public class ArrayIntListTest {
	@Test
	public void collectTest() {

		IntList expected = ArrayIntList.of(1, 2, 3, 4, 5);
		IntList actual   = IntStream.rangeClosed(1, 5)
				.collect(
						ArrayIntList::new,
						ArrayIntList::add,
						ArrayIntList::addAll);
		assertEquals(expected, actual);
	}
}
