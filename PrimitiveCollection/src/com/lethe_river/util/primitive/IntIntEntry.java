package com.lethe_river.util.primitive;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

public interface IntIntEntry extends Map.Entry<Integer, Integer> {

	/**
	 * 現在の位置のキーを返す
	 * @return
	 */
	int getKeyInt();

	/**
	 * 現在の位置のバリューを返す
	 * @return
	 */
	int getValueInt();

	/**
	 * 現在の位置のキーを返す
	 * @return
	 */
	@Override
	default Integer getKey() {
		return getKeyInt();
	}

	/**
	 * 現在の位置のバリューを返す
	 * @return
	 */
	@Override
	default Integer getValue() {
		return getValueInt();
	}

	/**
	 * 現在の位置のバリューを指定した値で上書きする(オプションの操作)
	 * @param value
	 * @return
	 */
	default int setValue(int value) {
		throw new UnsupportedOperationException();
	}

	@Override
	default Integer setValue(Integer value) {
		return setValue(value);
	}

	public static Spliterator<IntIntEntry> spliterator(Iterator<IntIntEntry> intIntCursorIterator, int size, int characteristics) {
		return new IntIntIteratorSpliterator(Objects.requireNonNull(intIntCursorIterator), size, characteristics);
	}

	static final class IntIntIteratorSpliterator implements Spliterator<IntIntEntry> {
		static final int BATCH_UNIT = 1 << 10;
		static final int MAX_BATCH  = 1 << 25;

		private Iterator<IntIntEntry> itr;
		private int estimateSize;
		private final int characteristics;
		private int batchSize;

		public IntIntIteratorSpliterator(Iterator<IntIntEntry> iterator, int size, int characteristics) {
			this.itr = iterator;
			this.estimateSize = size;
			this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
					? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
					: characteristics;
		}

		@Override
		public int characteristics() {
			return characteristics;
		}

		@Override
		public long estimateSize() {
			return estimateSize;
		}

		@Override
		public boolean tryAdvance(Consumer<? super IntIntEntry> action) {
			if(itr.hasNext()) {
				Objects.requireNonNull(action).accept(itr.next());
				return true;
			}
			return false;
		}

		@Override
		public void forEachRemaining(Consumer<? super IntIntEntry> action) {
			Objects.requireNonNull(action);
			for(int i = 0;i < estimateSize;i++) {
				action.accept(itr.next());
			}
		}

		@Override
		public Comparator<? super Entry<Integer, Integer>> getComparator() {
			if (hasCharacteristics(Spliterator.SORTED))
				return null;
			throw new IllegalStateException();
		}

		@Override
		public Spliterator<IntIntEntry> trySplit() {
			if(estimateSize > 1 && itr.hasNext()) {
				int nextBatchSize = batchSize + BATCH_UNIT;
				if(nextBatchSize > estimateSize) {
					nextBatchSize = estimateSize;
				}
				if(nextBatchSize > MAX_BATCH) {
					nextBatchSize = MAX_BATCH;
				}
				int[] k = new int[nextBatchSize];
				int[] v = new int[nextBatchSize];
				for(int i = 0;i < nextBatchSize;i++) {
					IntIntEntry cursor = itr.next();
					k[i] = cursor.getKeyInt();
					v[i] = cursor.getValueInt();
				}
				estimateSize -= nextBatchSize;
				return new IntIntArraySpliterator(k, v, 0, nextBatchSize, characteristics);
			}
			return null;
		}
	}

	static final  class IntIntArraySpliterator implements Spliterator<IntIntEntry> {
		private final int[] keys;
		private final int[] values;
		private int index;
		private final int end;
		private final int characteristics;

		private int key, value;

		IntIntEntry cursor = new IntIntEntry() {
			@Override
			public int getKeyInt() {
				return key;
			}

			@Override
			public int getValueInt() {
				return value;
			}
		};
		public IntIntArraySpliterator(int[] k, int[] v, int from, int to, int characteristics) {
			keys = k;
			values = v;
			index = from;
			end = to;
			this.characteristics = characteristics;
		}

		@Override
		public Spliterator<IntIntEntry> trySplit() {
			int from = index, to = (from + end) >>> 1;
			if(from >= to) {
				return null;
			}
			index = to;
			return new IntIntArraySpliterator(keys, values, from, to, characteristics);
		}

		@Override
		public void forEachRemaining(Consumer<? super IntIntEntry> action) {
			Objects.requireNonNull(action);
			int[] k = keys, v = values;
			int i = index, end = this.end;

			IntIntEntry cursor = this.cursor;
			if(k.length >= end && i >= 0 && i < (index = end)) {
				for(;i < end;i++) {
					key = k[i];
					value = v[i];
					action.accept(cursor);
				}
			}
		}

		@Override
		public boolean tryAdvance(Consumer<? super IntIntEntry> action) {
			Objects.requireNonNull(action);
			if(index < end) {
				key = keys[index];
				value = values[index];
				action.accept(cursor);
				index++;
				return true;
			}
			return false;
		}

		@Override
		public long estimateSize() {
			return end - index;
		}

		@Override
		public int characteristics() {
			return characteristics;
		}

		@Override
		public Comparator<? super IntIntEntry> getComparator() {
			if(hasCharacteristics(Spliterator.SORTED)) {
				return null;
			}
			throw new IllegalStateException();
		}
	}
}
