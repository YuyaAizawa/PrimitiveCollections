package com.lethe_river.util.primitive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//import java.io.File;
import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.OutputStream;
import java.io.PrintStream;
//import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import org.junit.Test;

import com.lethe_river.util.primitive.collection.ArrayIntList;
import com.lethe_river.util.primitive.collection.IntSet;
import com.lethe_river.util.primitive.collection.ScatterIntSet;

//import net.sourceforge.sizeof.SizeOf;

public class ScatterIntSetTest {

	@Test
	public void test() {
		Set<Integer> oracle = new HashSet<>();
		ScatterIntSet testee = new ScatterIntSet();

		addSome(oracle, testee);

		assertEquals(oracle.size(), testee.size());
		assertTrue(oracle.containsAll(testee.boxedView()));
		assertTrue(testee.stream().allMatch(i -> oracle.contains(i)));
	}

	@Test
	public void boxedTest() {
		Set<Integer> oracle = new HashSet<>();
		ScatterIntSet testee = new ScatterIntSet();

		addSome(oracle, testee);

		Set<Integer> boxed = testee.boxedView();

		assertEquals(oracle, boxed);
	}

	@Test
	public void containsTest() {
		Set<Integer> oracle = new HashSet<>();
		ScatterIntSet testee = new ScatterIntSet();

		addSome(oracle, testee);

		Random random = new Random(1145141919810L);
		for (int i = 0; i < 100; i++) {
			int e = random.nextInt(150);
			assertEquals(
					oracle.contains(e),
					testee.contains(e));
		}
	}

	@Test
	public void removeTest() {
		Set<Integer> oracle = new HashSet<>();
		ScatterIntSet testee = new ScatterIntSet();
		Random random = new Random(1145141919810L);
		for (int i = 0; i < 100; i++) {
			int e = random.nextInt(100) * 3;
			oracle.add(e);
			testee.add(e);
		}
		for (int i = 0; i < 100; i++) {
			int e = random.nextInt(300);
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

//	@Test
//	public void sizeTest() throws FileNotFoundException {
//		PrintWriter pw = new PrintWriter(new File("C:\\home\\log.txt"));
//		Set<Integer> oracle = new HashSet<>();
//		ScatterIntSet testee = new ScatterIntSet();
//		SizeOf.skipStaticField(true);
//		SizeOf.setMinSizeToLog(1);
//		SizeOf.setLogOutputStream(new OutputStream(){
//			@Override
//			public void write(int b) throws IOException {}
//		});
//		Random random = new Random(1145141919810L);
//		for (int i = 0; i < 11000; i++) {
//			if(oracle.size()%1000==0 || oracle.size() == 100 || oracle.size() == 10) {
//				pw.println("entry size : "+testee.size());
//				pw.println("HashSet<Integer>: "+SizeOf.deepSizeOf(oracle));
//				pw.println("InsScatterTable : "+SizeOf.deepSizeOf(testee));
//				pw.println();
//			}
//			int e = random.nextInt(1000000);
//			oracle.add(e);
//			testee.add(e);
//		}
//		pw.flush();
//		pw.close();
//	}

	@Test
	public void speedTest() throws FileNotFoundException {
//		PrintWriter pw = new PrintWriter(new File("C:\\home\\log2.txt"));
		PrintStream pw = System.out;
		int[] rnd = new Random(1145141919810L).ints(2000000, -500000, 500000).toArray();
		long start, end;

		for(int j = 0;j < 10;j++) {
			Set<Integer> is = new HashSet<>();
			for (int i = 0; i < 1000000; i++) {
				is.add(rnd[i]);
			}
		}
		start = System.currentTimeMillis();
		for(int j = 0;j < 10;j++) {
			Set<Integer> is = new HashSet<>();
			for (int i = 0; i < 1000000; i++) {
				is.add(rnd[i]);
			}
		}
		end   = System.currentTimeMillis();
		pw.println("HashSet<Integer>: "+(end-start)/10+" msec/(1 new + 1M add)");

		for(int j = 0;j < 10;j++) {
			Set<Integer> is = new HashSet<>();
			for (int i = 0; i < 1000000; i++) {
				is.add(rnd[i]);
			}
			for (int i = 0; i < 1000000; i++) {
				is.remove(rnd[i+1000000]);
			}
		}
		start = System.currentTimeMillis();
		for(int j = 0;j < 10;j++) {
			Set<Integer> is = new HashSet<>();
			for (int i = 0; i < 1000000; i++) {
				is.add(rnd[i]);
			}
			for (int i = 0; i < 1000000; i++) {
				is.remove(rnd[i+1000000]);
			}
		}
		end   = System.currentTimeMillis();
		pw.println("HashSet<Integer>: "+(end-start)/10+" msec/(1 new + 1M add + 1M remove)");

		Set<Integer> is = new HashSet<>();
		for (int i = 0; i < 1000000; i++) {
			is.add(rnd[i]);
		}
		for(int j = 0;j < 10;j++) {
			for (int i = 0; i < 1000000; i++) {
				is.contains(rnd[i+1000000]);
			}
		}
		start = System.currentTimeMillis();
		for(int j = 0;j < 10;j++) {
			for (int i = 0; i < 1000000; i++) {
				is.contains(rnd[i+1000000]);
			}
		}
		end   = System.currentTimeMillis();
		pw.println("HashSet<Integer>: "+(end-start)/10+" msec/1M contains");

		for(int j = 0;j < 10;j++) {
			ScatterIntSet ist = new ScatterIntSet();
			for (int i = 0; i < 1000000; i++) {
				ist.add(rnd[i]);
			}
		}
		start = System.currentTimeMillis();
		for(int j = 0;j < 10;j++) {
			ScatterIntSet ist = new ScatterIntSet();
			for (int i = 0; i < 1000000; i++) {
				ist.add(rnd[i]);
			}
		}
		end   = System.currentTimeMillis();
		pw.println("IntScatterSet : "+(end-start)/10+" msec/(1 new + 1M add)");

		for(int j = 0;j < 10;j++) {
			ScatterIntSet ist = new ScatterIntSet();
			for (int i = 0; i < 1000000; i++) {
				ist.add(rnd[i]);
			}
			for (int i = 0; i < 1000000; i++) {
				ist.remove(rnd[i+1000000]);
			}
		}
		start = System.currentTimeMillis();
		for(int j = 0;j < 10;j++) {
			ScatterIntSet ist = new ScatterIntSet();
			for (int i = 0; i < 1000000; i++) {
				ist.add(rnd[i]);
			}
			for (int i = 0; i < 1000000; i++) {
				ist.remove(rnd[i+1000000]);
			}
		}
		end   = System.currentTimeMillis();
		pw.println("IntScatterSet: "+(end-start)/10+" msec/(1 new + 1M add + 1M remove)");

		ScatterIntSet ist = new ScatterIntSet();
		for (int i = 0; i < 1000000; i++) {
			ist.add(rnd[i]);
		}
		for(int j = 0;j < 10;j++) {
			for (int i = 0; i < 1000000; i++) {
				ist.contains(rnd[i+1000000]);
			}
		}
		start = System.currentTimeMillis();
		for(int j = 0;j < 10;j++) {
			for (int i = 0; i < 1000000; i++) {
				ist.contains(rnd[i+1000000]);
			}
		}
		end   = System.currentTimeMillis();
		pw.println("IntScatterSet : "+(end-start)/10+" msec/1M contains");

		pw.flush();
		pw.close();
	}

	private void addSome(Set<Integer> oracle, IntSet testee) {
		Random random = new Random(1145141919810L);
		for (int i = 0; i < 100; i++) {
			int e = random.nextInt(100);
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
