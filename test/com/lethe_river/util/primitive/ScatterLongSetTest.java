package com.lethe_river.util.primitive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import org.junit.Test;

public class ScatterLongSetTest {
	@Test
	public void test() {
		Set<Long> oracle = new HashSet<>();
		ScatterLongSet testee = new ScatterLongSet();

		addSome(oracle, testee);

		assertEquals(oracle.size(), testee.size());
		assertTrue(oracle.containsAll(testee.boxedView()));
		assertTrue(testee.stream().allMatch(i -> oracle.contains(i)));
	}

	@Test
	public void boxedTest() {
		Set<Long> oracle = new HashSet<>();
		ScatterLongSet testee = new ScatterLongSet();

		addSome(oracle, testee);

		Set<Long> boxed = testee.boxedView();

		assertEquals(oracle, boxed);
	}

	@Test
	public void containsTest() {
		Set<Long> oracle = new HashSet<>();
		ScatterLongSet testee = new ScatterLongSet();

		addSome(oracle, testee);

		Random random = new Random(1145141919810L);
		for (int i = 0; i < 100; i++) {
			long e = random.nextInt(150);
			assertEquals(
					oracle.contains(e),
					testee.contains(e));
		}
	}

	@Test
	public void removeTest() {
		Set<Long> oracle = new HashSet<>();
		ScatterLongSet testee = new ScatterLongSet();
		Random random = new Random(1145141919810L);
		for (int i = 0; i < 100; i++) {
			long e = random.nextInt(100) * 3;
			oracle.add(e);
			testee.add(e);
		}
		for (int i = 0; i < 100; i++) {
			long e = random.nextInt(300);
//			if(oracle.contains(e)) {
//				System.out.println(e);
//				System.out.println(Arrays.toString((int[])Util.forceGetField(testee, IntScatterTable.class, "field")));
//			}
			assertEquals(
					oracle.remove(e),
					testee.remove(e));
		}
		assertEquals(oracle.size(), testee.size());
		assertEquals(oracle, testee.boxedView());
	}

	private void addSome(Set<Long> oracle, LongSet testee) {
		Random random = new Random(1145141919810L);
		for (int i = 0; i < 100; i++) {
			long e = random.nextInt(100);
			oracle.add(e);
			testee.add(e);
		}
	}

	@Test
	public void complexTest01() {
		ScatterIntSet target = new ScatterIntSet();
		Set<Integer> oracle = new HashSet<>();

		ArrayIntList ints = ArrayIntList.of(
				23, 16, 14, 15, 23, 23, 23, 22, 20, 13,
				24, 19,  1, 18, 17,  0, 13, 23, 16,  3,
				21, 24,  2);

		let((IntConsumer)i -> {
			assertEquals(oracle.contains(i), target.contains(i));
			oracle.add(i);
			target.add(i);
		}, fn -> ints.forEach(i -> fn.accept(i)));
	}

	static <T> void let(T t, Consumer<T> consumer) {
		consumer.accept(t);
	}
}
