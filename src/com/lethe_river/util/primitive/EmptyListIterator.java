package com.lethe_river.util.primitive;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.IntConsumer;

public final class EmptyListIterator {
	private EmptyListIterator() {}

	public static IntListIterator ofInt() {
		return EmptyIntListIterator.SINGLETON;
	}

	public static ByteListIterator ofByte() {
		return EmptyByteListIterator.SINGLETON;
	}



	private static final class EmptyIntListIterator implements IntListIterator {
		static final EmptyIntListIterator SINGLETON = new EmptyIntListIterator();

		@Override
		public void forEachRemaining(IntConsumer action) {
			Objects.requireNonNull(action);
		}
		@Override
		public void remove() {
			throw new IllegalStateException();
		}
		@Override
		public int previousIndex() {
			return -1;
		}
		@Override
		public int nextIndex() {
			return 0;
		}
		@Override
		public boolean hasPrevious() {
			return false;
		}
		@Override
		public boolean hasNext() {
			return false;
		}
		@Override
		public void set(int e) {
			throw new IllegalStateException();
		}
		@Override
		public int previousInt() {
			throw new NoSuchElementException();
		}
		@Override
		public int nextInt() {
			throw new NoSuchElementException();
		}
		@Override
		public void add(int e) {
			throw new UnsupportedOperationException();
		}
	}

	private static final class EmptyByteListIterator implements ByteListIterator {
		static final EmptyByteListIterator SINGLETON = new EmptyByteListIterator();

		@Override
		public void forEachRemaining(ByteConsumer action) {
			Objects.requireNonNull(action);
		}
		@Override
		public void remove() {
			throw new IllegalStateException();
		}
		@Override
		public int previousIndex() {
			return -1;
		}
		@Override
		public int nextIndex() {
			return 0;
		}
		@Override
		public boolean hasPrevious() {
			return false;
		}
		@Override
		public boolean hasNext() {
			return false;
		}
		@Override
		public void set(byte e) {
			throw new IllegalStateException();
		}
		@Override
		public byte previousByte() {
			throw new NoSuchElementException();
		}
		@Override
		public byte nextByte() {
			throw new NoSuchElementException();
		}
		@Override
		public void add(byte e) {
			throw new UnsupportedOperationException();
		}
	}
}
