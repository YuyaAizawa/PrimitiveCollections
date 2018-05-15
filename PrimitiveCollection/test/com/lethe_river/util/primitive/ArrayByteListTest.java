package com.lethe_river.util.primitive;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArrayByteListTest {
	@Test
	public void boxedTest() {

		List<Byte> expected = Arrays.asList((byte)1, (byte)2, (byte)3, (byte)4, (byte)5);
		List<Byte> actual   = ArrayByteList.of((byte)1, (byte)2, (byte)3, (byte)4, (byte)5).boxedView();

		assertEquals(expected, actual);
	}
}
