package com.lethe_river.util.primitive;

public final class EmptyCursor {
	private EmptyCursor() {}

	public static IntIntCursor ofIntInt() {
		return EmptyIteratorOfIntInt.SINGLETON;
	}



	private static enum EmptyIteratorOfIntInt implements IntIntCursor {
		SINGLETON;

		@Override
		public boolean next() {
			return false;
		}

		@Override
		public int key() {
			throw new IllegalStateException();
		}

		@Override
		public int value() {
			throw new IllegalStateException();
		}

		@Override
		public void remove() {
			throw new IllegalStateException();
		}

		@Override
		public void setValue(int value) {
			throw new IllegalStateException();
		}
	}
}
