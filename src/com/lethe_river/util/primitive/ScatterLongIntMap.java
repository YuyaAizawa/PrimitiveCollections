package com.lethe_river.util.primitive;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class ScatterLongIntMap implements LongIntMap {

	private static final long NULL = 0;

	private static final int DEFAULT_INIT_CAPACITY = 11;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	private static final int MAX_CAPACITY = 1<<29;

	private static final long[] EMPTY_KEYS = {};

	// NULL以外のkey, value
	private long[] keys;
	private int[] values;

	// NULLのkey, value
	private boolean nullKey;
	private int nullValue;

	// 要素数の合計
	private int size;

	// 負荷係数
	private final float loadFactor;

	// 次にリハッシュする容量
	private int threshold;

	// 構造的変更検出用
	private transient int modCount;

	/**
	 * 初期容量と負荷係数を指定してIntIntScatterMapを生成する.
	 *
	 * @param initCapacity 初期容量
	 * @param loadFactor 負荷係数
	 */
	public ScatterLongIntMap(int initCapacity, float loadFactor) {
		if(initCapacity < 0) {
			throw new IllegalArgumentException("initCapacity must be positive :"+initCapacity);
		}

		keys = initCapacity == 0 ?
				EMPTY_KEYS :
				new long[initCapacity];
		values = initCapacity == 0 ?
				null :
				new int[initCapacity];
		nullKey = false;
		size = 0;
		this.loadFactor = loadFactor;
		threshold = (int) (initCapacity * loadFactor);
		modCount = 0;
	}

	/**
	 * 初期容量を指定してIntIntScatterMapを生成する.
	 *
	 * 負荷係数はデフォルトの値(0.75)が用いられる
	 * @param initCapacity 初期容量
	 */
	public ScatterLongIntMap(int initCapacity) {
		this(initCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * デフォルトの初期容量(11)と負荷係数を利用してIntIntScatterMapを生成する.
	 */
	public ScatterLongIntMap() {
		keys = EMPTY_KEYS;
		nullKey = false;
		size = 0;
		loadFactor = DEFAULT_LOAD_FACTOR;
		threshold = 0;
		modCount = 0;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	}


	@Override
	public boolean containsKey(long key) {
		if(key == NULL) {
			return nullKey;
		}

		if(keys==EMPTY_KEYS) {
			return false;
		}
		int index = hash(key) % keys.length;
		int limit = index-1;
		if(limit == -1) {
			limit = keys.length;
		}
		for(;index!=limit;index++) {
			if(index == keys.length) {
				index = 0;
			}
			long j = keys[index];
			if(j == key) {
				return true;
			}
			if(j == NULL) {
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(int value) {
		if(nullKey && nullValue == value) {
			return true;
		}
		for (int i = 0; i < keys.length; i++) {
			if(keys[i] != NULL && values[i] == value) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void clear() {
		Arrays.fill(keys, NULL);
		nullKey = false;
		size = 0;
		modCount++;
	}

	@Override
	public int get(long key) {
		if(key == NULL) {
			return nullValue;
		}

		if(keys==EMPTY_KEYS) {
			throw new NoSuchElementException("key: "+key);
		}
		int index = hash(key) % keys.length;
		int limit = index-1;
		if(limit == -1) {
			limit = keys.length;
		}
		for(;index!=limit;index++) {
			if(index == keys.length) {
				index = 0;
			}
			long j = keys[index];
			if(j == key) {
				return values[index];
			}
			if(j == NULL) {
				throw new NoSuchElementException("key: "+key);
			}
		}
		throw new AssertionError();
	}

	@Override
	public int getOrDefault(long key, int def) {
		if(key == NULL) {
			return nullValue;
		}

		if(keys==EMPTY_KEYS) {
			return def;
		}
		int index = hash(key) % keys.length;
		int limit = index-1;
		if(limit == -1) {
			limit = keys.length;
		}
		for(;index!=limit;index++) {
			if(index == keys.length) {
				index = 0;
			}
			long j = keys[index];
			if(j == key) {
				return values[index];
			}
			if(j == NULL) {
				return def;
			}
		}
		throw new AssertionError();
	}


	@Override
	public void put(long key, int value) {
		if(key == NULL) {
			if(!nullKey) {
				nullKey = true;
				size++;
			}
			nullValue = value;
			return;
		}

		try {
			ensureCapacity(size+1);
		} catch(IllegalArgumentException e) {
			throw new  RuntimeException("Too many entries!");
		}

		for(int index = hash(key) % keys.length;;index++) {
			if(index == keys.length) {
				index = 0;
			}
			long j = keys[index];
			if(j == key) {
				values[index] = value;
				return;
			}
			if(j == NULL) {
				keys[index] = key;
				values[index] = value;
				size++;
				return;
			}
		}
	}

	@Override
	public boolean remove(long key) {
		if(key == NULL) {
			if(nullKey) {
				nullKey = false;
				size--;
				return true;
			}
			return false;
		}

		if(keys==EMPTY_KEYS) {
			return false;
		}
		for(int index = hash(key) % keys.length;;index++) {
			if(index == keys.length) {
				index = 0;
			}
			long j = keys[index];
			if(j == key) {
				pull(index);
				size--;
				return true;
			}
			if(j == NULL) {
				return false;
			}
		}
	}

	/**
	 * 指定したindexのエントリを削除する
	 * @param index
	 */
	private void pull(int index) {

		int dst = index;
		for(int src = dst + 1;;src++) {
			if(src == keys.length) {
				src = 0;
			}
			long i = keys[src];
			if(i == NULL || src == index) {
				keys[dst] = NULL;
				return;
			}
			int pos = hash(i) % keys.length;
			if((dst < src && pos <= dst)
			|| (src < dst && (src < pos && pos <= dst))) {
				keys[dst] = i;
				values[dst] = values[src];
				dst = src;
			}
		}
	}

	@Override
	public LongSet keys() {
		return new KeySet();
	}

	@Override
	public IntCollection values() {
		if(keys==EMPTY_KEYS) {
			return IntCollection.empty();
		}
		return new ValueCollection();
	}

	@Override
	public LongIntCursor entryCursor() {
		if(keys==EMPTY_KEYS) {
			return LongIntCursor.empty();
		}
		return new EntryCursor();
	}

	/**
	 * 指定した容量を格納できるようこのIntScatterTableの内部配列を拡張する.
	 * @param minCapasity 保持するint値の数
	 * @throws IllegalArgumentException minCapacityが[1, MAX_CAPACITY]の範囲外だったとき
	 */
	public void ensureCapacity(int minCapasity) {
		if(minCapasity <= 0 || MAX_CAPACITY < minCapasity) {
			throw new IllegalArgumentException("minCapacity : "+minCapasity);
		}
		if(threshold >= minCapasity || keys.length == MAX_CAPACITY) {
			return;
		}

		int reccommended = (keys==EMPTY_KEYS) ?
				DEFAULT_INIT_CAPACITY :
				HashSupport.avoid2357(keys.length<<1);
		int nextCapacity = Math.max((int)(minCapasity / loadFactor)+1 , reccommended);
		nextCapacity = Math.min(nextCapacity, MAX_CAPACITY);

		rehash(nextCapacity);
	}

	private void rehash(int nextCapacity) {
		long[] tmpKeys = new long[nextCapacity];
		int[] tmpValues = new int[nextCapacity];
		for (int i = 0; i < keys.length; i++) {
			if(keys[i] == NULL) {
				continue;
			}

			long j = keys[i];
			for(int index = hash(j) % tmpKeys.length;;index++) {
				if(index == tmpKeys.length) {
					index = 0;
				}
				if(tmpKeys[index] == NULL) {
					tmpKeys[index] = j;
					tmpValues[index] = values[i];
					break;
				}
			}
		}
		keys = tmpKeys;
		values = tmpValues;
		modCount++;
		threshold = (int) (keys.length * loadFactor);
	}

	/**
	 * 要素のScatterTable上での位置を決めるためのhashを返す．
	 * {@link ScatterIntSet#hashCode()}とは無関係
	 * @param j 要素
	 * @return 要素のハッシュ(正数のint)
	 */
	private static int hash(long l) {
		return Integer.MAX_VALUE & ((int)(l^(l>>>32)));
	}

	@Override
	public boolean equals(Object object) {
		if(object == this) {
			return true;
		}
		if(object == null || !(object instanceof LongIntMap)) {
			return false;
		}
		LongIntMap target = (LongIntMap) object;
		if(target.size() != size()) {
			return false;
		}

		LongIntCursor entries = target.entryCursor();
		while(entries.next()) {
			if(getOrDefault(entries.key(), entries.value()+1) != entries.value()) {
				return false;
			}
		}
		return true;
	}


	@Override
	public int hashCode() {
		int sum = 0;
		LongIntCursor cursor = entryCursor();
		while(cursor.next()) {
			sum += cursor.key() ^ cursor.value();
		}
		return sum;
	}


	private class KeySet extends AbstractLongSet {

		@Override
		public int size() {
			return size;
		}

		@Override
		public PrimitiveIterator.OfLong iterator() {
			// 1つ目は0を返す
			return new PrimitiveIterator.OfLong() {

				int index = 0;

				// 構造的変更検出用
				int expectedModCount = modCount;

				// nextで返した数
				int replied = 0;

				// 削除した要素の数
				int removed = 0;

				// removeが行える状態
				boolean removable = false;
				long removeTarget = 0;

				@Override
				public boolean hasNext() {
					return replied < size + removed;
				}

				@Override
				public long nextLong() {
					if(expectedModCount != modCount) {
						throw new ConcurrentModificationException();
					}
					if(replied==0 && nullKey) {
						replied++;
						removable = true;
						removeTarget = NULL;
						return NULL;
					}
					try {
						for(;;) {
							long retVal = keys[index++];
							if(retVal != NULL) {
								replied++;
								removable = true;
								removeTarget = retVal;
								return retVal;
							}
						}
					} catch (ArrayIndexOutOfBoundsException e){
						throw new NoSuchElementException();
					}
				}

				@Override
				public void remove() {
					if(expectedModCount != modCount) {
						throw new ConcurrentModificationException();
					}
					if(!removable) {
						throw new IllegalStateException();
					}
					ScatterLongIntMap.this.remove(removeTarget);
					expectedModCount = modCount;
					removed++;
					removable = false;
				}
			};
		}

		@Override
		public boolean contains(long l) {
			return ScatterLongIntMap.this.containsKey(l);
		}

		@Override
		public boolean remove(long l) {
			return ScatterLongIntMap.this.remove(l);
		}
	}

	private class ValueCollection extends AbstractIntCollection {

		@Override
		public int size() {
			return size;
		}

		@Override
		public PrimitiveIterator.OfInt iterator() {
			// 1つ目は0を返す
			return new PrimitiveIterator.OfInt() {

				int index = 0;

				// 構造的変更検出用
				int expectedModCount = modCount;

				// nextで返した数
				int replied = 0;

				// 削除した要素の数
				int removed = 0;

				// removeが行える状態
				boolean removable = false;
				long removeTarget = 0;

				@Override
				public boolean hasNext() {
					return replied < size + removed;
				}

				@Override
				public int nextInt() {
					if(expectedModCount != modCount) {
						throw new ConcurrentModificationException();
					}
					if(replied==0 && nullKey) {
						replied++;
						removable = true;
						removeTarget = NULL;
						return nullValue;
					}
					try {
						for(;;) {
							int v = values[index];
							long k = keys[index++];
							if(k != NULL) {
								replied++;
								removable = true;
								removeTarget = k;
								return v;
							}
						}
					} catch (ArrayIndexOutOfBoundsException e){
						throw new NoSuchElementException();
					}
				}

				@Override
				public void remove() {
					if(expectedModCount != modCount) {
						throw new ConcurrentModificationException();
					}
					if(!removable) {
						throw new IllegalStateException();
					}
					ScatterLongIntMap.this.remove(removeTarget);
					expectedModCount = modCount;
					removed++;
					removable = false;
				}
			};
		}
	}

	private class EntryCursor implements LongIntCursor {

		// -2:開始, -1:NULL, others:配列の添字
		int index = -2;

		// 構造的変更検出用
		int expectedModCount = modCount;

		// removeしたことを示す
		boolean removed = false;

		@Override
		public boolean next() {
			if(expectedModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if(index == -2) {
				index = -1;
				if(nullKey) {
					return true;
				}
			}
			if(removed) {
				removed = false;
				if(index != -1 && keys[index] != NULL) {
					return true;
				}
			}
			if(index >= keys.length) {
				return false;
			}
			try {
				do {
					index++;
				} while (keys[index] == NULL);
				removed = false;
				return true;
			} catch (ArrayIndexOutOfBoundsException e){
				return false;
			}
		}

		@Override
		public long key() {
			if(expectedModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if(removed == true) {
				throw new IllegalStateException();
			}
			try {
				return index == -1 ? NULL : keys[index];
			} catch (ArrayIndexOutOfBoundsException e){
				throw new IllegalStateException();
			}
		}

		@Override
		public int value() {
			if(expectedModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if(removed == true) {
				throw new IllegalStateException();
			}
			try {
				return index == -1 ? nullValue : values[index];
			} catch (ArrayIndexOutOfBoundsException e){
				throw new IllegalStateException();
			}
		}

		@Override
		public void remove() {
			if(expectedModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if(removed || index == -2) {
				throw new IllegalStateException();
			}

			long key = index == -1 ? NULL : keys[index];
			ScatterLongIntMap.this.remove(key);
			removed = true;

			// pullでズレた分を補正
			if(key != NULL && keys[hash(key) % keys.length] != NULL) {
				index--;
			}
		}

		@Override
		public void setValue(int value) {
			if(removed || index == -2) {
				throw new IllegalStateException();
			}
			try {
				ScatterLongIntMap.this.put(index == -1 ? NULL : keys[index], value);
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new IllegalStateException();
			}
		}
	}
}
