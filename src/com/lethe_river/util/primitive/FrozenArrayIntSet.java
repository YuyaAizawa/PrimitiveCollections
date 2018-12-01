package com.lethe_river.util.primitive;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.IntConsumer;


/**
 * 内部で自然順序でソートされた不変IntSet
 * @author YuyaAizawa
 *
 */
public class FrozenArrayIntSet extends AbstractIntSortedSet {
	private final int[] field;
	private final int from;
	private final int to;

	private FrozenArrayIntSet(int[] field) {
		this(field, 0, field.length);
	}

	private FrozenArrayIntSet(int[] field, int from, int to) {
		this.field = field;
		this.from = from;
		this.to = to;
	}

	/**
	 * 指定した要素を持つIntImmutableArraySetを生成する.
	 * @param ints
	 * @return このクラスのインスタンス
	 */
	public static FrozenArrayIntSet of(int... ints) {
		return new FrozenArrayIntSet(Arrays.stream(ints)
				.sorted()
				.distinct()
				.toArray());
	}

	/**
	 * 指定した要素を持つIntImmutableArraySetを生成する.
	 * @param set
	 * @return このクラスのインスタンス
	 */
	public static FrozenArrayIntSet of(Set<Integer> set) {
		return new FrozenArrayIntSet(set.stream()
				.mapToInt(Integer::intValue)
				.sorted()
				.toArray());
	}

	/**
	 * n番目の要素を返す．インデックスは0からsize()-1までの間であり，インデックスの大小関係は値のそれと等しい.
	 * @param index
	 * @return n番目の要素
	 * @throws ArrayIndexOutOfBoundsException 指定されたインデックスが0からsize()-1出なかった場合
	 */
	public int nth(int index) {
		if(index+from >= to) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return field[index+from];
	}

	@Override
	public int size() {
		return to - from;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean contains(int i) {
		int index = Arrays.binarySearch(field, i);
		return index >= from && index < to;
	}

	@Override
	public boolean containsAll(int... is) {
		Objects.requireNonNull(is);
		for (int i = 0; i < is.length; i++) {
			if(!contains(is[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 指定した集合の要素すべてがこの集合に含まれるか判定する.
	 * @param is
	 * @return 含まれればtrue
	 */
	public boolean containsAll(FrozenArrayIntSet is) {
		if(!contains(is.first()) || !contains(is.last())) {
			return false;
		}
		return PrimitiveSupport.isSubsequence(is.iterator(), iterator());
	}

	@Override
	public boolean containsAll(IntSet is) {
		return PrimitiveSupport.isSubsequence(is.stream().sorted().iterator(), iterator());
	}

	@Override
	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			int index = from;

			@Override
			public boolean hasNext() {
				return index < to;
			}

			@Override
			public int nextInt() {
				if(index < to) {
					try {
						return field[index++];
					} catch(ArrayIndexOutOfBoundsException e) {
						throw new NoSuchElementException();
					}
				}
				throw new NoSuchElementException();
			}
		};
	}

	@Override
	public Spliterator.OfInt spliterator() {
		return new ArraySpliterator(from, to);
	}

	class ArraySpliterator implements Spliterator.OfInt {

		public ArraySpliterator(int index, int limit) {
			this.index = index;
			this.limit = limit;
		}

		int index;
		final int limit;

		@Override
		public long estimateSize() {
			return limit - index;
		}

		@Override
		public int characteristics() {
			return Spliterator.IMMUTABLE
				  | Spliterator.DISTINCT
				  | Spliterator.NONNULL
				  | Spliterator.ORDERED
				  | Spliterator.SIZED
				  | Spliterator.SORTED
				  | Spliterator.SUBSIZED;
		}

		@Override
		public Spliterator.OfInt trySplit() {
			if(limit - index < 2) {
				return null;
			}
			int oldIndex = index;
			index += (limit - index) / 2;
			return new ArraySpliterator(oldIndex, index);
		}

		@Override
		public boolean tryAdvance(IntConsumer action) {
			Objects.requireNonNull(action);
			if(index >= limit) {
				return false;
			}
			action.accept(field[index++]);
			return true;
		}

		@Override
		public void forEachRemaining(IntConsumer action) {
			Objects.requireNonNull(action);
			for(;index < limit;index++) {
				action.accept(field[index]);
			}
		}
	}

	@Override
	public int first() {
		try {
			return field[from];
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new NoSuchElementException();
		}
	}

	@Override
	public int last() {
		try {
			return field[to - 1];
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new NoSuchElementException();
		}
	}

	@Override
	public IntSortedSet subSet(int from, int to) {
		int fromIndex = Arrays.binarySearch(field, from);
		if(fromIndex < 0) {
			fromIndex = -fromIndex-1;
		}
		int toIndex = Arrays.binarySearch(field, to);
		if(toIndex < 0) {
			toIndex = -toIndex-1;
		}
		if(fromIndex == toIndex) {
			return EmptySet.ofInt();
		}
		return new FrozenArrayIntSet(field, Math.max(fromIndex, this.from), Math.min(toIndex, this.to));
	}
}
