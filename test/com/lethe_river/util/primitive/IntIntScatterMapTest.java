package com.lethe_river.util.primitive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
	public void entriesTest() {
		ScatterIntIntMap map = new ScatterIntIntMap();
		map.put(0, 1);
		map.put(1, 2);
		map.put(-2, -3);

		IntIntCursor cursor = map.entryCursor();
		int count = 0;
		while(cursor.next()) {
			count++;
			switch(cursor.key()) {
			case 0:
				assertEquals(1, cursor.value());
				break;
			case 1:
				assertEquals(2, cursor.value());
				cursor.remove();
				break;
			case -2:
				assertEquals(-3, cursor.value());
				cursor.setValue(3);
				break;
			default:
				fail();
			}
		}
		assertEquals(3, count);
		assertEquals(114514, map.getOrDefault(2, 114514));
		assertEquals(3, map.get(-2));
	}

//	@Test
//	public void StreamTest() {
//		Map<Integer, Integer> oracle = new HashMap<>();
//		ScatterIntIntMap testee = new ScatterIntIntMap(11, 0.75f);
//
//
//		Random random = new Random(1145141919810L);
//
//		for (int i = 0; i < 100; i++) {
//			int k = random.nextInt(100);
//			int v = random.nextInt(100);
//			oracle.put(k, v);
//			testee.put(k, v);
//		}
//
//		Set<Entry<Integer, Integer>> actual = testee.boxedView()
//				.entrySet()
//				.stream()
//				.filter(e -> e.getKey() < e.getValue())
//				.collect(Collectors.toSet());
//
//		Set<Entry<Integer, Integer>> expected = oracle
//				.entrySet()
//				.stream()
//				.filter(e -> e.getKey() < e.getValue())
//				.collect(Collectors.toSet());
//
//		assertEquals(expected, actual);
//	}
}
