package com.lethe_river.util.primitive.collection;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

import com.lethe_river.util.primitive.function.ByteConsumer;

public final class EmptyList {
	private EmptyList() {}

	public static IntList ofInt() {
		return EmptyIntList.SINGLETON;
	}

	public static ByteList ofByte() {
		return EmptyByteList.SINGLETON;
	}



	private static final class EmptyIntList extends AbstractIntList {

		public static final EmptyIntList SINGLETON = new EmptyIntList();

		private Object readResolve() {
			return SINGLETON;
		}

		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
		}

		@Override
		public PrimitiveIterator.OfInt iterator() {
			return EmptyIterator.ofInt();
		}

		@Override
		public int size() {
			return 0;
		}

		@Override
		public IntListIterator listIterator(int index) {
			return EmptyListIterator.ofInt();
		}
	}

	private static final class EmptyByteList extends AbstractByteList {

		public static final EmptyByteList SINGLETON = new EmptyByteList();

		private Object readResolve() {
			return SINGLETON;
		}

		@Override
		public void forEach(ByteConsumer action) {
			Objects.requireNonNull(action);
		}
		@Override
		public ByteIterator iterator() {
			return EmptyIterator.ofByte();
		}
		@Override
		public int size() {
			return 0;
		}

		@Override
		public ByteListIterator listIterator(int index) {
			return EmptyListIterator.ofByte();
		}
	}
}
