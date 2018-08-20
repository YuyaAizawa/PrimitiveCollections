package com.lethe_river.util.primitive;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.IntConsumer;

/**
 * 内部で自然順序でソートされた不変IntSet
 * @author YuyaAizawa
 *
 */
public class ImmutableArrayByteSet extends AbstractByteSortedSet {
	private final byte[] field;
	private final int from;
	private final int to;

	/**
	 * ソート済み配列を利用して不変IntSetを作成する．配列は変更されてはならない．
	 * @param field
	 */
	private ImmutableArrayByteSet(byte[] field) {
		this(field, 0, field.length);
	}

	private ImmutableArrayByteSet(byte[] field, int from, int to) {
		this.field = field;
		this.from = from;
		this.to = to;
	}

	/**
	 * 指定した要素を持つByteImmutableArraySetを生成する.
	 * @param bytes
	 * @return このクラスのインスタンス
	 */
	public static ImmutableArrayByteSet of(byte... bytes) {
		byte[] tmp = Arrays.copyOf(bytes, bytes.length);
		Arrays.sort(tmp);
		int prev = Byte.MIN_VALUE -1;
		int contentLength = 0;
		for (int i = 0; i < tmp.length; i++) {
			if(prev!=tmp[i]) {
				contentLength++;
			}
			prev = tmp[i];
		}
		byte[] contents = new byte[contentLength];
		prev = Byte.MIN_VALUE -1;
		contentLength = 0;
		for (int i = 0; i < tmp.length; i++) {
			if(prev!=tmp[i]) {
				contents[contentLength++] = tmp[i];
			}
			prev = tmp[i];
		}
		return new ImmutableArrayByteSet(contents);
	}

	/**
	 * 指定した要素を持つByteImmutableArraySetを生成する.
	 * @param set
	 * @return このクラスのインスタンス
	 */
	public static ImmutableArrayByteSet of(Set<Byte> set) {
		byte[] contents = new byte[set.size()];
		int index = 0;
		for(byte b: set) {
			contents[index++] = b;
		}
		Arrays.sort(contents);
		return new ImmutableArrayByteSet(contents);
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
	public boolean contains(byte i) {
		int index = Arrays.binarySearch(field, i);
		return index >= from && index < to;
	}

	@Override
	public boolean containsAll(byte... is) {
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
	public boolean containsAll(ByteSortedSet bs) {
		if(!contains(bs.first()) || !contains(bs.last())) {
			return false;
		}
		return PrimitiveSupport.isSubsequence(bs.iteratorSigned(), iteratorSigned());
	}

	@Override
	public boolean containsAll(ByteSet bs) {
		if(bs instanceof ByteSortedSet)  {
			containsAll((ByteSortedSet)bs);
		}
		for(ByteIterator i = bs.iterator(); i.hasNext();) {
			if(!contains(i.nextByte())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ByteIterator iterator() {
		return new ByteIterator() {
			int index = from;

			@Override
			public boolean hasNext() {
				return index < to;
			}

			@Override
			public byte nextByte() {
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
	public Spliterator.OfInt spliteratorSigned() {
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
	public Comparator<? super Byte> comparator() {
		return Comparator.naturalOrder();
	}

	@Override
	public byte first() {
		try {
			return field[from];
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new NoSuchElementException();
		}
	}

	@Override
	public byte last() {
		try {
			return field[to - 1];
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new NoSuchElementException();
		}
	}

	@Override
	public ByteSortedSet subSet(byte from, byte to) {
		int fromIndex = Arrays.binarySearch(field, from);
		if(fromIndex < 0) {
			fromIndex = -fromIndex-1;
		}
		int toIndex = Arrays.binarySearch(field, to);
		if(toIndex < 0) {
			toIndex = -toIndex-1;
		}
		if(fromIndex == toIndex) {
			return EmptySet.ofByte();
		}
		return new ImmutableArrayByteSet(field, Math.max(fromIndex, this.from), Math.min(toIndex, this.to));
	}
}

