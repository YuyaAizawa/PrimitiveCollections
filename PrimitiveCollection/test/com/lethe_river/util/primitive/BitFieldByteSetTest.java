package com.lethe_river.util.primitive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class BitFieldByteSetTest {
	/*
	 * -100, 0, 1が格納されたテスト対象のインスタンスを返す
	 */
	private ByteSet getInstance() {
		ByteSet set = new BitFieldByteSet();
		set.add((byte)-100);
		set.add((byte)0);
		set.add((byte)1);
		return set;
	}

	/*
	 * -100, 0, 1が格納されたオラクルのインスタンスを返す
	 */
	private Set<Byte> getOracle() {
		Set<Byte> set = new TreeSet<>();
		set.add((byte)-100);
		set.add((byte)0);
		set.add((byte)1);
		return set;
	}

	/**
	 * iteratorがjava.util.Setのものと同じに反復するか確認する
	 */
	@Test
	public void iteratorTest() {
		ByteIterator i = getInstance().iterator();
		Iterator<Byte> j = getOracle().iterator();

		for(;;) {
			boolean b = i.hasNext();
			boolean c = j.hasNext();
			assertEquals(c, b);
			if(!c) break;
			assertEquals(j.next(), i.next());
		}
	}

	/**
	 * iteratorで0を削除する
	 */
	@Test
	public void iteratorRemoveTest() {
		ByteSet set = getInstance();

		assertTrue(set.contains((byte)0));

		for(ByteIterator i = set.iterator();i.hasNext();) {
			if(i.next() == 0) {
				i.remove();
			}
		}

		assertTrue(set.contains((byte)-100));
		assertFalse(set.contains((byte)0));
		assertTrue(set.contains((byte)1));
	}

	@Test
	public void addTest() {
		ByteSet target = getInstance();
		int size;

		size = target.size();
		assertFalse(target.contains((byte)10));
		assertTrue(target.add((byte)10));
		assertTrue(target.contains((byte)10));
		assertEquals(size+1, target.size());

		size = target.size();
		assertTrue(target.contains((byte)-100));
		assertFalse(target.add((byte)-100));
		assertTrue(target.contains((byte)-100));
		assertEquals(size, target.size());
	}

	@Test
	public void addAllTest() {
		ByteSet target = getInstance();
		Set<Byte> oracle = getOracle();

		assertEquals(oracle, target.boxedView());

		target.addAll((byte)-20,(byte)32,(byte)0,(byte)108);
		oracle.addAll(Set.of((byte)-20,(byte)32,(byte)0,(byte)108));

		assertEquals(oracle, target.boxedView());
	}

	@Test
	public void removeTest() {
		ByteSet target = getInstance();
		int size;

		size = target.size();
		assertTrue(target.contains((byte)1));
		assertTrue(target.remove((byte)1));
		assertFalse(target.contains((byte)1));
		assertEquals(size-1, target.size());

		size = target.size();
		assertFalse(target.contains((byte)100));
		assertFalse(target.remove((byte)100));
		assertFalse(target.contains((byte)1));
		assertEquals(size, target.size());
	}

	@Test
	public void toArrayTest() {
		assertArrayEquals(new byte[] {-100,  0, 1}, getInstance().toArray());
	}

	/**
	 * boxedViewがjava.util.Setと等価か確認する
	 */
	@Test
	public void boxedEqualTest() {
		assertEquals(getOracle(), getInstance().boxedView());
	}
}

