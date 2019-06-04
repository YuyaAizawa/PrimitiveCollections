package com.lethe_river.util.primitive;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.lethe_river.util.primitive.collection.IntSet;
import com.lethe_river.util.primitive.collection.PrimitiveCollections;

public class PrimitiveCollectionsTest {
	@Test
	public void combinationTest() {
		int[] base = {1,2,3,4,5};
		Iterable<IntSet> c = PrimitiveCollections.combination(IntSet.of(base), 3);

		Set<Set<Integer>> actual = new HashSet<>();
		c.forEach(e -> actual.add(e.boxedView()));

		Set<Set<Integer>> expected = new HashSet<>();
		expected.add(Set.of(1, 2, 3));
		expected.add(Set.of(1, 2, 4));
		expected.add(Set.of(1, 2, 5));
		expected.add(Set.of(1, 3, 4));
		expected.add(Set.of(1, 3, 5));
		expected.add(Set.of(1, 4, 5));
		expected.add(Set.of(2, 3, 4));
		expected.add(Set.of(2, 3, 5));
		expected.add(Set.of(2, 4, 5));
		expected.add(Set.of(3, 4, 5));

		assertEquals(expected, actual);
	}
}
