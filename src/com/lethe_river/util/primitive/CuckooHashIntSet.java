package com.lethe_river.util.primitive;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

/**
 * カッコーハッシュを利用したIntSet
 * @author YuyaAizawa
 *
 */
public class CuckooHashIntSet extends AbstractIntSet {

	private static final int DEFAULT_CAPACITY = 5;
	private static final float DEFAULT_LOAD_FACTOR = 0.5f;
	private static final int RETRY = 20;

	private static final Iterator<IntIntHash> HASH_LIST =
			IntStream.iterate(1, HashSupport::avoid2357)
					.mapToObj(p -> HashSupport.hash32shiftmult(p))
					.iterator();

	// 0以外の要素を入れる配列
	private int[] table1, table2;

	// ハッシュとして使う関数
	private IntIntHash hash1, hash2;

	// ハッシュ関数の予備
	private final Iterator<IntIntHash> hashes;

	// 0を含むか
	private boolean hasZero;

	// 要素数の合計
	private int size;

	// 構造的変更検出用
	private int modifyId;

	public CuckooHashIntSet() {
		this(DEFAULT_CAPACITY);
	}

	public CuckooHashIntSet(int tableSize) {
		this(tableSize, () -> HASH_LIST);
	}

	public CuckooHashIntSet(int tableSize, Iterable<IntIntHash> hashFunctions) {
		table1 = new int[tableSize];
		table2 = new int[tableSize];
		hashes = hashFunctions.iterator();
		try {
			hash1 = hashes.next();
			hash2 = hashes.next();
		} catch(NoSuchElementException e) {
			throw new IllegalArgumentException("No enough hash functions.");
		}
		hasZero = false;
		size  = 0;
		modifyId = 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {

			// 構造的変更検出用
			int buildId = modifyId;

			// テーブル走査用
			PrimitiveIterator.OfInt iterator1 = ArrayIterator.of(table1);
			PrimitiveIterator.OfInt iterator2 = ArrayIterator.of(table2);

			// nextで返した数
			int replied = 0;
			boolean repliedZero = false;

			@Override
			public boolean hasNext() {
				if(buildId != modifyId) {
					throw new ConcurrentModificationException();
				}
				return (hasZero && !repliedZero) ||
						replied < size;
			}

			@Override
			public int nextInt() {
				if(buildId != modifyId) {
					throw new ConcurrentModificationException();
				}
				if(hasZero && !repliedZero) {
					repliedZero = true;
					return 0;
				}

				int value = 0;
				while(value == 0 && iterator1.hasNext()){
					value = iterator1.nextInt();
				}
				while(value == 0 && iterator2.hasNext()){
					value = iterator2.nextInt();
				}
				if(value == 0) {
					throw new NoSuchElementException();
				}

				replied++;
				return value;
			}
		};
	}

	@Override
	public void clear() {
		table1 = new int[DEFAULT_CAPACITY];
		table2 = new int[DEFAULT_CAPACITY];
		hasZero = false;
		size = 0;
		modifyId++;
	}

	@Override
	public boolean add(int i) {
		if(contains(i)) {
			return true;
		}
		if(i == 0) {
			hasZero = true;
			size++;
			modifyId++;
			return false;
		}

		if(size > table1.length * DEFAULT_LOAD_FACTOR * 2) {
			expand();
		}

		for (int j = 0; j < RETRY; j++) {
			int h1 = hash1.hash(i) % table1.length;
			int tmp = table1[h1];
			table1[h1] = i;
			i = tmp;

			if(i == 0) {
				size++;
				modifyId++;
				return false;
			}

			int h2 = hash2.hash(i) % table2.length;
			tmp = table2[h2];
			table2[h2] = i;
			i = tmp;

			if(i == 0) {
				size++;
				modifyId++;
				return false;
			}
		}
		rehash();

		return add(i);
	}

	@Override
	public boolean remove(int i) {
		if(i == 0) {
			if(hasZero) {
				hasZero = false;
				size--;
				modifyId++;
				return true;
			}
			return false;
		}
		if(table1[hash1.hash(i)%table1.length] == i) {
			table1[hash1.hash(i)%table1.length] = 0;
			size--;
			modifyId++;
			return true;
		}
		if(table2[hash2.hash(i)%table1.length] == i) {
			table2[hash2.hash(i)%table1.length] = 0;
			size--;
			modifyId++;
			return true;
		}
		return false;
	}

	private void expand() {
		CuckooHashIntSet nset = new CuckooHashIntSet(table1.length * 2);
		nset.addAll(this);
		table1 = nset.table1;
		table2 = nset.table2;
	}

	/**
	 * 再ハッシュする
	 */
	private void rehash() {
		IntIntHash nextHash1;
		IntIntHash nextHash2;
		try {
			nextHash1 = hashes.next();
			nextHash2 = hashes.next();
		} catch (NoSuchElementException e) {
			throw new RuntimeException("Hash source is exhausted.");
		}

		int[] nextTable1 = new int[table1.length];
		int[] nextTable2 = new int[table2.length];

		forEach(i -> {
			if(i == 0) {
				return;
			}

			for(int j = 0; j < RETRY; j++) {
				int h1 = nextHash1.hash(i) % table1.length;
				int tmp = nextTable1[h1];
				nextTable1[h1] = i;
				i = tmp;
				if(i == 0) {
					return;
				}

				int h2 = nextHash2.hash(i) % table2.length;
				tmp = nextTable2[h2];
				nextTable2[h2] = i;
				i = tmp;
				if(i== 0) {
					return;
				}
			}
			throw new RuntimeException("Rehash failed.");
		});

		table1 = nextTable1;
		table2 = nextTable2;
		hash1 = nextHash1;
		hash2 = nextHash2;
	}

	@Override
	public boolean contains(int i) {
		if(i == 0) {
			return hasZero;
		}
		return table1[hash1.hash(i)%table1.length] == i || table2[hash2.hash(i)%table1.length] == i;
	}
}
