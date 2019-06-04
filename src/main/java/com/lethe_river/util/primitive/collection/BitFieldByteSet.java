package com.lethe_river.util.primitive.collection;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * ビットフィールドで実装されたByteSet
 *
 * @author YuyaAizawa
 *
 */
public final class BitFieldByteSet extends AbstractByteSortedSet implements RandomAccess {
	private static final long[] EMPTY_BIT_FIELD = {};
	private static final int FIELD_LENGTH = 256 / Long.SIZE;

	/**
	 * ビットフィールド field[0]が1のとき-128を含む
	 */
	private long[] field;

	private transient int modCount;

	public BitFieldByteSet() {
		field = EMPTY_BIT_FIELD;
		modCount = 0;
	}

	BitFieldByteSet(long[] field) {
		if(field.length != FIELD_LENGTH) {
			throw new Error();
		}
		this.field = field;
		modCount = 0;
	}

	@Override
	public boolean contains(byte b) {
		if(field == EMPTY_BIT_FIELD) {
			return false;
		}
		int unsigned = b - Byte.MIN_VALUE;
		int index = unsigned / Long.SIZE;
		long mask = 1L << (unsigned % (Long.SIZE));

		return (field[index] & mask) != 0;
	}

	@Override
	public boolean add(byte b) {
		if(field == EMPTY_BIT_FIELD) {
			field = new long[FIELD_LENGTH];
		}
		int unsigned = b - Byte.MIN_VALUE;
		int index = unsigned / Long.SIZE;
		long mask = 1L << (unsigned % (Long.SIZE));

		return field[index] != (field[index] |= mask);
	}

	public boolean addAll(BitFieldByteSet bs) {
		if(bs.isEmpty()) {
			return false;
		}
		if(field == EMPTY_BIT_FIELD) {
			field = new long[FIELD_LENGTH];
		}
		boolean changed = false;
		for (int i = 0; i < FIELD_LENGTH; i++) {
			changed |= field[i] != (field[i] |= bs.field[i]);
		}
		return changed;
	}

	@Override
	public boolean remove(byte b) {
		if(field == EMPTY_BIT_FIELD) {
			return false;
		}
		int unsigned = b - Byte.MIN_VALUE;
		int index = unsigned / Long.SIZE;
		long mask = ~(1 << (unsigned % (Long.SIZE)));

		long prev = field[index];
		field[index] &= mask;

		return prev != field[index];
	}

	@Override
	public byte first() {
		if(field == EMPTY_BIT_FIELD) {
			throw new NoSuchElementException();
		}
		for (int i = 0; i < FIELD_LENGTH; i++) {
			int lowest = Long.numberOfTrailingZeros(field[i]);
			if(lowest != Long.SIZE) {
				return (byte)(Long.SIZE * i + lowest + Byte.MIN_VALUE);
			}
		}
		throw new NoSuchElementException();
	}
	@Override
	public byte last() {
		if(field == EMPTY_BIT_FIELD) {
			throw new NoSuchElementException();
		}
		for (int i = FIELD_LENGTH -1; i >= 0; i--) {
			int highest = Long.numberOfLeadingZeros(field[i]);
			if(highest != Long.SIZE) {
				return (byte)(Long.SIZE * i + highest + Byte.MIN_VALUE);
			}
		}
		throw new NoSuchElementException();
	}

	// TODO test
	@Override
	public ByteSortedSet subSet(byte from, byte to) {
		return new BitFieldSubSet(from -Byte.MIN_VALUE, to - Byte.MIN_VALUE);
	}
	@Override
	public int size() {
		if(field == EMPTY_BIT_FIELD) {
			return 0;
		}
		int size = 0;
		for (int i = 0; i < FIELD_LENGTH; i++) {
			size += Long.bitCount(field[i]);
		}
		return size;
	}

	@Override
	public ByteIterator iterator() {
		if(field == EMPTY_BIT_FIELD) {
			return EmptyIterator.ofByte();
		}
		return new BitFieldIterator(0, 256);
	}

	class BitFieldIterator implements ByteIterator {

		private static final int UNFETCHED = -1;
		private static final int RUN_OUT = -2;

		int nextUnsigned = UNFETCHED;
		int previousUnsigned = UNFETCHED;

		int fieldIndex;
		long bitStatus;

		final int from, to;

		int modCount = BitFieldByteSet.this.modCount;

		public BitFieldIterator(int from, int to) {
			this.from = from;
			this.to   = to;

			fieldIndex = from/Long.SIZE;

			// TODO 高速化
			bitStatus = 0;
			do {
				fetch();
			} while(nextUnsigned != RUN_OUT && nextUnsigned < from);
		}

		/*
		 * 次のbitをnextUnsignedに格納する(存在しない場合はRUN_OUT)．
		 */
		private void fetch() {
			while(fieldIndex < FIELD_LENGTH) {
				if(bitStatus == 0) {
					bitStatus = field[fieldIndex] & -field[fieldIndex];
				}
				if(bitStatus == 0) {
					fieldIndex++;
					continue;
				}
				nextUnsigned = Long.numberOfTrailingZeros(bitStatus) + fieldIndex * Long.SIZE;
				if(nextUnsigned >= to) {
					break;
				}
				bitStatus = field[fieldIndex] & (~field[fieldIndex] + (bitStatus << 1));
				if(bitStatus == 0) {
					fieldIndex++;
				}
				return;
			}
			nextUnsigned = RUN_OUT;
		}

		@Override
		public boolean hasNext() {
			if(nextUnsigned == UNFETCHED) {
				fetch();
			}
			return nextUnsigned != RUN_OUT;
		}

		@Override
		public byte nextByte() {
			if(modCount != BitFieldByteSet.this.modCount) {
				throw new ConcurrentModificationException();
			}
			return (byte)(nextUnsigned() - Byte.MIN_VALUE);
		}

		@Override
		public int nextUnsigned() {
			if(modCount != BitFieldByteSet.this.modCount) {
				throw new ConcurrentModificationException();
			}

			if(nextUnsigned == UNFETCHED) {
				fetch();
			}
			if(nextUnsigned == RUN_OUT) {
				throw new NoSuchElementException();
			}

			int ret = nextUnsigned;
			nextUnsigned = UNFETCHED;
			previousUnsigned = ret;
			return ret;
		}

		@Override
		public void remove() {
			if(previousUnsigned == UNFETCHED) {
				throw new IllegalStateException();
			}
			BitFieldByteSet.this.remove((byte)(previousUnsigned - Byte.MIN_VALUE));
			modCount = BitFieldByteSet.this.modCount;
			previousUnsigned = UNFETCHED;
		}
	}

	class BitFieldSubSet extends AbstractByteSortedSet {

		// 符号付き
		final int from;
		final int to;

		BitFieldSubSet(int from, int to) {
			this.from = from;
			this.to   = to;
		}

		@Override
		public int size() {
			int size = 0;
			for(ByteIterator i = iterator();i.hasNext();i.next()) {
				size++;
			}
			return size;
		}

		@Override
		public ByteIterator iterator() {
			return new BitFieldIterator(from, to);
		}

		@Override
		public byte first() {
			if(field == EMPTY_BIT_FIELD) {
				throw new NoSuchElementException();
			}

			long mask = PrimitiveSupport.zeroFillLong(from|63);
			int i = from / Long.SIZE;
			for (; i < FIELD_LENGTH; i++) {
				int lowest = Long.numberOfTrailingZeros(mask & field[i]);
				if(lowest != Long.SIZE) {
					return (byte)(Long.SIZE * i + lowest + Byte.MIN_VALUE);
				}
				mask = -1L;
			}
			throw new NoSuchElementException();
		}

		@Override
		public byte last() {
			if(field == EMPTY_BIT_FIELD) {
				throw new NoSuchElementException();
			}

			long mask = PrimitiveSupport.oneFillLong(to|63);
			int i = to / Long.SIZE;
			for (; i >= 0; i--) {
				int highest = Long.numberOfLeadingZeros(mask & field[i]);
				if(highest != Long.SIZE) {
					return (byte)(Long.SIZE * i + highest + Byte.MIN_VALUE);
				}
				mask = -1;
			}
			throw new NoSuchElementException();
		}

		@Override
		public ByteSortedSet subSet(byte from, byte to) {
			return new BitFieldSubSet(from - Byte.MIN_VALUE, to - Byte.MIN_VALUE);
		}
	}
}
