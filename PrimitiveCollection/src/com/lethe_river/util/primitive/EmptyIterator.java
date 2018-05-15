package com.lethe_river.util.primitive;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class EmptyIterator {
	private EmptyIterator() {}

	public static PrimitiveIterator.OfInt ofInt() {
		return EmptyIteratorOfInt.SINGLETON;
	}

	public static ByteIterator ofByte() {
		return EmptyIteratorOfByte.SINGLETON;
	}

	public static PrimitiveIterator.OfDouble ofDouble() {
		return EmptyIteratorOfDouble.SINGLETON;
	}

	public static CharIterator ofChar() {
		return EmptyIteratorOfChar.SINGLETON;
	}

	private enum EmptyIteratorOfInt implements PrimitiveIterator.OfInt {
		SINGLETON;

		@Override
		public boolean hasNext() {
			return false;
		}
		@Override
		public int nextInt() {
			throw new NoSuchElementException();
		}
	}

	private enum EmptyIteratorOfByte implements ByteIterator {
		SINGLETON;

		@Override
		public boolean hasNext() {
			return false;
		}
		@Override
		public byte nextByte() {
			throw new NoSuchElementException();
		}
	}

	private enum EmptyIteratorOfDouble implements PrimitiveIterator.OfDouble {
		SINGLETON;

		@Override
		public boolean hasNext() {
			return false;
		}
		@Override
		public double nextDouble() {
			throw new NoSuchElementException();
		}
	}

	private enum EmptyIteratorOfChar implements CharIterator {
		SINGLETON;

		@Override
		public boolean hasNext() {
			return false;
		}
		@Override
		public char nextChar() {
			throw new NoSuchElementException();
		}
	}
}
