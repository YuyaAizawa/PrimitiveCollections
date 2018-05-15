package com.lethe_river.util.primitive;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

public class IntIntScatterMapTest {

	@Test
	public void putGetTest() {
		Map<Integer, Integer> oracle = new HashMap<>();
		ScatterIntIntMap testee = new ScatterIntIntMap(11, 0.75f);


		Random random = new Random(1145141919810L);

		for (int i = 0; i < 100; i++) {
			int k = random.nextInt(30)*3;
			int v = random.nextInt(100);
			oracle.put(k, v);
			testee.put(k, v);
		}

		for (int i = 0; i < 100; i++) {
			int k = random.nextInt(100);

			Integer expected = oracle.get(k);
			Integer actual = testee.getOrDefault(k, -1);
			if(actual == -1) {
				actual = null;
			}
			assertEquals(
					expected,
					actual);
		}

		assertEquals(
				oracle.size(),
				testee.size());
	}


	@Test
	public void removeTest() {
		Map<Integer, Integer> oracle = new HashMap<>();
		ScatterIntIntMap testee = new ScatterIntIntMap(11, 0.75f);


		Random random = new Random(1145141919810L);

		for (int i = 0; i < 100; i++) {
			int k = random.nextInt(30)*3;
			int v = random.nextInt(100);
			oracle.put(k, v);
			testee.put(k, v);
		}

		for (int i = 0; i < 100; i++) {
			int k = random.nextInt(100);
			oracle.remove(k);
			testee.remove(k);
		}

		assertEquals(
				oracle.keySet(),
				testee.keys().boxedView());

		for(int i : oracle.keySet()) {
			assertEquals(
					(int)oracle.get(i),
					testee.get(i));
		}

		for (int i = 0; i < 200; i++) {
			int k = random.nextInt(1000);
			int v = random.nextInt(100);
			oracle.put(k, v);
			testee.put(k, v);
		}

		for (int i = 0; i < 1000; i++) {
			int k = random.nextInt(1000);
			oracle.remove(k);
			testee.remove(k);
		}

		assertEquals(
				oracle.keySet(),
				testee.keys().boxedView());

		for(int i : oracle.keySet()) {
			assertEquals(
					(int)oracle.get(i),
					testee.get(i));
		}
	}

	@Test
	public void StreamTest() {
		Map<Integer, Integer> oracle = new HashMap<>();
		ScatterIntIntMap testee = new ScatterIntIntMap(11, 0.75f);


		Random random = new Random(1145141919810L);

		for (int i = 0; i < 100; i++) {
			int k = random.nextInt(100);
			int v = random.nextInt(100);
			oracle.put(k, v);
			testee.put(k, v);
		}

		Set<Entry<Integer, Integer>> actual = testee.boxedView()
				.entrySet()
				.stream()
				.filter(e -> e.getKey() < e.getValue())
				.collect(Collectors.toSet());

		Set<Entry<Integer, Integer>> expected = oracle
				.entrySet()
				.stream()
				.filter(e -> e.getKey() < e.getValue())
				.collect(Collectors.toSet());

		assertEquals(expected, actual);
	}
}
