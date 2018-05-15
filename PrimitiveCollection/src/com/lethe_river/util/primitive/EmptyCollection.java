package com.lethe_river.util.primitive;

import java.util.Objects;
import java.util.function.IntConsumer;

public final class EmptyCollection {
	private EmptyCollection() {}

	public static IntCollection ofInt() {
		return EmptyIntCollection.SINGLETON;
	}

	public static ByteCollection ofByte() {
		return EmptyByteCollection.SINGLETON;
	}

	static CharCollection ofChar() {
		return EmptyCharCollection.SINGLETON;
	}



	private static final class EmptyIntCollection extends AbstractIntCollection {

		static final EmptyIntCollection SINGLETON = new EmptyIntCollection();
		private EmptyIntCollection() {}

		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
		}

		@Override
		public int size() {
			return 0;
		}

		private Object readResolve() {
			return SINGLETON;
		}

		@Override
		public java.util.PrimitiveIterator.OfInt iterator() {
			return EmptyIterator.ofInt();
		}
	}

	private static final class EmptyByteCollection extends AbstractByteCollection {

		static final EmptyByteCollection SINGLETON = new EmptyByteCollection();
		private EmptyByteCollection() {}

		@Override
		public void forEach(ByteConsumer action) {
			Objects.requireNonNull(action);
		}

		@Override
		public int size() {
			return 0;
		}

		private Object readResolve() {
			return SINGLETON;
		}

		@Override
		public ByteIterator iterator() {
			return EmptyIterator.ofByte();
		}
	}

	private static final class EmptyCharCollection extends AbstractCharCollection {

		static final EmptyCharCollection SINGLETON = new EmptyCharCollection();
		private EmptyCharCollection() {}

		@Override
		public void forEach(CharConsumer action) {
			Objects.requireNonNull(action);
		}

		@Override
		public int size() {
			return 0;
		}

		private Object readResolve() {
			return SINGLETON;
		}

		@Override
		public CharIterator iterator() {
			return EmptyIterator.ofChar();
		}
	}
}
