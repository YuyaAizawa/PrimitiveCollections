package com.lethe_river.util.primitive;

import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.SortedSet;
import java.util.function.IntConsumer;

public final class EmptySet {
	private EmptySet() {}

	public static IntSortedSet ofInt() {
		return EmptyIntSet.SINGLETON;
	}

	public static ByteSortedSet ofByte() {
		return EmptyByteSet.SINGLETON;
	}

	static CharSet ofChar() {
		return EmptyCharSet.SINGLETON;
	}



	private static final class EmptyIntSet extends AbstractIntSet implements IntSortedSet {

		public static final IntSortedSet SINGLETON = new EmptyIntSet();

		private EmptyIntSet() {}

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
		public Comparator<Integer> comparator() {
			return null;
		}

		@Override
		public int first() {
			throw new NoSuchElementException();
		}

		@Override
		public int last() {
			throw new NoSuchElementException();
		}

		@Override
		public IntSortedSet subSet(int from, int to) {
			return this;
		}

		@Override
		public IntSortedSet headSet(int to) {
			return this;
		}

		@Override
		public IntSortedSet tailSet(int from) {
			return this;
		}

		@Override
		public SortedSet<Integer> boxedView() {
			return Collections.emptySortedSet();
		}
	}

	private static final class EmptyByteSet extends AbstractByteSet implements ByteSortedSet {

		public static final EmptyByteSet SINGLETON = new EmptyByteSet();

		private EmptyByteSet() {}

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
		public byte first() {
			throw new NoSuchElementException();
		}

		@Override
		public byte last() {
			throw new NoSuchElementException();
		}

		@Override
		public ByteSortedSet subSet(byte from, byte to) {
			return this;
		}

		@Override
		public ByteSortedSet headSet(byte to) {
			return this;
		}

		@Override
		public ByteSortedSet tailSet(byte from) {
			return this;
		}

		@Override
		public SortedSet<Byte> boxedView() {
			return Collections.emptySortedSet();
		}
	}

	private static final class EmptyCharSet extends AbstractCharSet implements CharSortedSet {

		public static final CharSortedSet SINGLETON = new EmptyCharSet();

		private EmptyCharSet() {}

		private Object readResolve() {
			return SINGLETON;
		}

		@Override
		public void forEach(CharConsumer action) {
			Objects.requireNonNull(action);
		}
		@Override
		public CharIterator iterator() {
			return EmptyIterator.ofChar();
		}
		@Override
		public int size() {
			return 0;
		}

		@Override
		public Comparator<Character> comparator() {
			return null;
		}

		@Override
		public char first() {
			throw new NoSuchElementException();
		}

		@Override
		public char last() {
			throw new NoSuchElementException();
		}

		@Override
		public CharSortedSet subSet(char from, char to) {
			return this;
		}

		@Override
		public CharSortedSet headSet(char to) {
			return this;
		}

		@Override
		public CharSortedSet tailSet(char from) {
			return this;
		}

		@Override
		public SortedSet<Character> boxedView() {
			return Collections.emptySortedSet();
		}
	}
}
