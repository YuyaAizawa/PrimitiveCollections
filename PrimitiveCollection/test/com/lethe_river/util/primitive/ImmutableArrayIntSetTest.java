package com.lethe_river.util.primitive;

import static org.junit.Assert.assertEquals;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.lethe_river.util.primitive.ImmutableArrayIntSet;



public class ImmutableArrayIntSetTest {

	public static Set<Integer> oracle;

	@Before
	public void prepere() {
		Random random = new Random(1145141919810L);
		oracle = IntStream.range(-100, 100)
				.map(i -> random.nextInt(2000)-1000)
				.boxed()
				.collect(Collectors.toSet());
		System.out.println(oracle);
	}

	@Test
	public void containsTest() {
		ImmutableArrayIntSet target = ImmutableArrayIntSet.of(oracle.stream().mapToInt(i -> i).toArray());
		IntStream.range(-1000, 1000)
				.forEach(i -> {
					assertEquals(oracle.contains(i), target.contains(i));
				});
	}
}
