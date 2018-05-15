package com.lethe_river.util.primitive;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;

public final class ScatterIntIntMap implements IntIntMap {

	private static final int NULL = 0;

	private static final int DEFAULT_INIT_CAPACITY = 11;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	private static final int MAX_CAPACITY = 1<<29;

	private static final int[] EMPTY_KEYS = {};

	// NULL以外のkey, value
	private int[] keys;
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
	private int modCount;

	/**
	 * 初期容量と負荷係数を指定してIntIntScatterMapを生成する.
	 *
	 * @param initCapacity 初期容量
	 * @param loadFactor 負荷係数
	 */
	public ScatterIntIntMap(int initCapacity, float loadFactor) {
		if(initCapacity < 0) {
			throw new IllegalArgumentException("initCapacity must be positive :"+initCapacity);
		}

		keys = initCapacity == 0 ?
				EMPTY_KEYS :
				new int[initCapacity];
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
	public ScatterIntIntMap(int initCapacity) {
		this(initCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * デフォルトの初期容量(11)と負荷係数を利用してIntIntScatterMapを生成する.
	 */
	public ScatterIntIntMap() {
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
	public boolean containsKey(int key) {
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
			int j = keys[index];
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
	public int get(int key) {
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
			int j = keys[index];
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
	public int getOrDefault(int key, int def) {
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
			int j = keys[index];
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
	public void put(int key, int value) {
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
			int j = keys[index];
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
	public boolean remove(int key) {
		if(key == NULL) {
			if(nullKey) {
				nullKey = false;
				size--;
				return true;
			} else {
				return false;
			}
		}

		if(keys==EMPTY_KEYS) {
			return false;
		}
		for(int index = hash(key) % keys.length;;index++) {
			if(index == keys.length) {
				index = 0;
			}
			int j = keys[index];
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
			int i = keys[src];
			if(i == NULL || src == index) {
				keys[dst] = NULL;
				return;
			} else {
				int pos = hash(i) % keys.length;
				if((dst < src && pos <= dst)
				|| (src < dst && (src < pos && pos <= dst))) {
					keys[dst] = i;
					values[dst] = values[src];
					dst = src;
				}
			}
		}
	}

	@Override
	public IntSet keys() {
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
	public Set<IntIntEntry> entries() {
		if(keys==EMPTY_KEYS) {
			return Collections.emptySet();
		}
		return new EntrySet();
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
		int[] tmpKeys = new int[nextCapacity];
		int[] tmpValues = new int[nextCapacity];
		for (int i = 0; i < keys.length; i++) {
			if(keys[i] == NULL) {
				continue;
			}

			int j = keys[i];
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
	private int hash(int j) {
		// 正の数にするだけ
		return Integer.MAX_VALUE & j;
	}

	@Override
	public boolean equals(Object object) {
		if(object == this) {
			return true;
		}
		if(object == null || !(object instanceof IntIntMap)) {
			return false;
		}
		IntIntMap target = (IntIntMap) object;
		if(target.size() != size()) {
			return false;
		}
		return entries().containsAll(target.entries());
	}


	@Override
	public int hashCode() {
		return entries().stream()
				.mapToInt(c -> c.getKeyInt() ^ c.getValueInt())
				.sum();
	}


	@Override
	public Map<Integer, Integer> boxedView() {
		return new Map<>() {

			@Override
			public int size() {
				return size;
			}

			@Override
			public boolean isEmpty() {
				return size == 0;
			}

			@Override
			public boolean containsKey(Object key) {
				if(key instanceof Integer) {
					ScatterIntIntMap.this.containsKey((int)key);
				}
				return false;
			}

			@Override
			public boolean containsValue(Object value) {
				if(value instanceof Integer) {
					ScatterIntIntMap.this.containsValue((int)value);
				}
				return false;
			}

			@Override
			public Integer get(Object key) {
				if(key instanceof Integer) {
					try {
						return ScatterIntIntMap.this.get((int)key);
					} catch(NoSuchElementException e) {
						return null;
					}
				}
				return null;
			}

			@Override
			public Integer put(Integer key, Integer value) {
				int k = key;
				int v = value;
				if(ScatterIntIntMap.this.containsKey(k)) {
					int tmp = ScatterIntIntMap.this.get(k);
					ScatterIntIntMap.this.put(k, v);
					return tmp;
				}
				ScatterIntIntMap.this.put(k, v);
				return null;
			}

			@Override
			public Integer remove(Object key) {
				if(key instanceof Integer) {
					int k = (int) key;
					int tmp = ScatterIntIntMap.this.getOrDefault(k, NULL);
					if(ScatterIntIntMap.this.remove(k)) {
						return tmp;
					}
				}
				return null;
			}

			@Override
			public void putAll(Map<? extends Integer, ? extends Integer> m) {
				m.forEach((k, v) -> ScatterIntIntMap.this.put(k, v));
			}

			@Override
			public void clear() {
				ScatterIntIntMap.this.clear();
			}

			@Override
			public Set<Integer> keySet() {
				return ScatterIntIntMap.this.keys().boxedView();
			}

			@Override
			public Collection<Integer> values() {
				return ScatterIntIntMap.this.values().boxedView();
			}

			@Override
			public Set<java.util.Map.Entry<Integer, Integer>> entrySet() {
				throw new Error();
			}
		};
	}

	private class KeySet extends AbstractIntSet {

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
				int removeTarget = 0;

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
						return NULL;
					}
					try {
						for(;;) {
							int retVal = keys[index++];
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
					ScatterIntIntMap.this.remove(removeTarget);
					expectedModCount = modCount;
					removed++;
					removable = false;
				}
			};
		}

		@Override
		public boolean contains(int i) {
			return ScatterIntIntMap.this.containsKey(i);
		}

		@Override
		public boolean remove(int i) {
			return ScatterIntIntMap.this.remove(i);
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
				int removeTarget = 0;

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
							int k = keys[index++];
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
					ScatterIntIntMap.this.remove(removeTarget);
					expectedModCount = modCount;
					removed++;
					removable = false;
				}
			};
		}
	}

	private class EntrySet extends AbstractSet<IntIntEntry> {

		@Override
		public Iterator<IntIntEntry> iterator() {
			return new Iterator<>() {

				int index = 0;

				// 構造的変更検出用
				int expectedModCount = modCount;

				// nextで返した数
				int replied = 0;

				// 削除した要素の数
				int removed = 0;

				// removeが行える状態
				boolean removable = false;
				int cursorKey;
				int cursorValue;

				IntIntEntry cursor = new IntIntEntry() {
					@Override
					public int getKeyInt() {
						return cursorKey;
					}

					@Override
					public int getValueInt() {
						return cursorValue;
					}

					@Override
					public int setValue(int value) {
						if(!removable) {
							throw new IllegalStateException();
						}
						int retVal = cursorValue;
						ScatterIntIntMap.this.put(cursorKey, value);
						cursorValue = value;
						return retVal;
					}
				};

				@Override
				public boolean hasNext() {
					return replied < size + removed;
				}

				@Override
				public IntIntEntry next() {
					if(expectedModCount != modCount) {
						throw new ConcurrentModificationException();
					}
					if(replied==0 && nullKey) {
						replied++;
						removable = true;
						cursorKey = NULL;
						cursorValue = nullValue;
						return cursor;
					}
					try {
						for(;;) {
							int k = keys[index];
							int v = values[index];
							index++;
							if(k != NULL) {
								replied++;
								removable = true;
								cursorKey = k;
								cursorValue = v;
								return cursor;
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
					ScatterIntIntMap.this.remove(cursorKey);
					expectedModCount = modCount;
					removed++;
					removable = false;
				}
			};
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public Spliterator<IntIntEntry> spliterator() {
			return IntIntEntry.spliterator(iterator(), size, Spliterator.DISTINCT | Spliterator.NONNULL | Spliterator.SUBSIZED);
		}
	}
}
