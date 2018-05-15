package com.lethe_river.util.primitive;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * ビットフィールドで実装されたByteSet
 *
 * @author YuyaAizawa
 *
 */
public final class BitFieldByteSet extends AbstractByteSortedSet implements ByteSortedSet {
	private static final int[] EMPTY_BIT_FIELD = {};

	/**
	 * ビットフィールド field[0]が1のとき-128を含む
	 */
	private int[] field;

	private transient int modCount;

	public BitFieldByteSet() {
		field = EMPTY_BIT_FIELD;
		modCount = 0;
	}

	BitFieldByteSet(int[] field) {
		if(field.length != Integer.SIZE / Byte.SIZE) {
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
		int unsigned = b-Byte.MIN_VALUE;
		int index = unsigned / Integer.SIZE;
		int mask = 1 << (unsigned % (Integer.SIZE));

		return (field[index] & mask) != 0;
	}

	@Override
	public boolean add(byte b) {
		if(field == EMPTY_BIT_FIELD) {
			field = new int[Integer.SIZE/Integer.BYTES];
		}
		int unsigned = b-Byte.MIN_VALUE;
		int index = unsigned / Integer.SIZE;
		int mask = 1 << (unsigned % (Integer.SIZE));

		return field[index] != (field[index] |= mask);
	}

	public boolean addAll(BitFieldByteSet bs) {
		if(bs.isEmpty()) {
			return false;
		}
		if(field == EMPTY_BIT_FIELD) {
			field = new int[Integer.SIZE/Integer.BYTES];
		}
		boolean changed = false;
		for (int i = 0; i < (Integer.SIZE/Integer.BYTES); i++) {
			changed |= field[i] != (field[i] |= bs.field[i]);
		}
		return changed;
	}

	@Override
	public boolean addAll(ByteCollection bs) {
		if(bs.isEmpty()) {
			return true;
		}
		if(field == EMPTY_BIT_FIELD) {
			field = new int[Integer.SIZE/Integer.BYTES];
		}
		if(bs instanceof BitFieldByteSet) {
			return addAll((BitFieldByteSet)bs);
		} else {
			return super.addAll(bs);
		}
	}

	@Override
	public boolean remove(byte b) {
		if(field == EMPTY_BIT_FIELD) {
			return false;
		}
		int unsigned = b-Byte.MIN_VALUE;
		int index = unsigned / Integer.SIZE;
		int mask = ~(1 << (unsigned % (Integer.SIZE)));

		int prev = field[index];
		field[index] &= mask;

		return prev != field[index];
	}

	@Override
	public byte first() {
		if(field == EMPTY_BIT_FIELD) {
			throw new NoSuchElementException();
		}
		for (int i = 0; i < (Integer.SIZE/Integer.BYTES); i++) {
			int lowest = Integer.numberOfTrailingZeros(field[i]);
			if(lowest != 32) {
				return (byte)(Integer.SIZE * i + lowest + Byte.MIN_VALUE);
			}
		}
		throw new NoSuchElementException();
	}
	@Override
	public byte last() {
		if(field == EMPTY_BIT_FIELD) {
			throw new NoSuchElementException();
		}
		for (int i = Integer.SIZE/Integer.BYTES -1; i >= 0; i--) {
			int highest = Integer.numberOfLeadingZeros(field[i]);
			if(highest != 32) {
				return (byte)(Integer.SIZE * i + highest + Byte.MIN_VALUE);
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
		for (int i = 0; i < Integer.SIZE/Integer.BYTES; i++) {
			size += Integer.bitCount(field[i]);
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
		int bitStatus;

		final int from, to;

		int modCount = BitFieldByteSet.this.modCount;

		public BitFieldIterator(int from, int to) {
			this.from = from;
			this.to   = to;

			fieldIndex = from/Integer.SIZE;

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
			while(fieldIndex < Integer.SIZE/Integer.BYTES) {
				if(bitStatus == 0) {
					bitStatus = field[fieldIndex] & -field[fieldIndex];
				}
				if(bitStatus == 0) {
					fieldIndex++;
					continue;
				}
				nextUnsigned = Integer.numberOfTrailingZeros(bitStatus) + fieldIndex * Integer.SIZE;
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

			int mask = PrimitiveSupport.zeroFill(from|31);
			int i = from / Integer.SIZE;
			for (; i < (Integer.SIZE/Integer.BYTES); i++) {
				int lowest = Integer.numberOfTrailingZeros(mask & field[i]);
				if(lowest != 32) {
					return (byte)(Integer.SIZE * i + lowest + Byte.MIN_VALUE);
				}
				mask = -1;
			}
			throw new NoSuchElementException();
		}

		@Override
		public byte last() {
			if(field == EMPTY_BIT_FIELD) {
				throw new NoSuchElementException();
			}

			int mask = PrimitiveSupport.oneFill(to|31);
			int i = to / Integer.SIZE;
			for (; i >= 0; i--) {
				int highest = Integer.numberOfLeadingZeros(mask & field[i]);
				if(highest != 32) {
					return (byte)(Integer.SIZE * i + highest + Byte.MIN_VALUE);
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
