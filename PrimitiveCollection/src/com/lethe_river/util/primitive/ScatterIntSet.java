package com.lethe_river.util.primitive;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

/**
 * scatter tableを用いたIntSetの実装
 *
 * - open address
 * - prime number bucket
 * - Robin Hood Hashing
 * - liner probing
 *
 * @author YuyaAizawa
 *
 */
/*
 * 参考までにHashSet&lt;Integerとの&gt;性能比較
 *
 * 時間
 *
 * HashSet<Integer> : 139760 usec/(1 new + 1M add)
 * IntScatterTable  :  36260 usec/(1 new + 1M add)
 *
 * HashSet<Integer> :  51320 usec/1M contains
 * IntScatterTable  :  23070 usec/1M contains
 *
 *
 * 空間
 *
 * entry size           :  0,  10,  100,  1000,   5000,  10000
 * HashSet        (byte): 64, 640, 5920, 56288, 272864, 545632
 * InsScatterTable(byte): 56, 152,  848,  6448,  51248, 102448
 */
public final class ScatterIntSet extends AbstractIntSet implements IntSet, Serializable {
	private static final long serialVersionUID = 5569547128406006928L;

	// NULLをあらわす数字
	private static final int NULL = 0;

	private static final int DEFAULT_INIT_CAPACITY = 11;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	private static final int MAX_CAPACITY = 1<<30;

	private static final IntIntHash hashFunction = HashSupport.thomasMueller();

	private static final int[] EMPTY_DATA = {};

	// NULL以外の要素を入れる配列
	private int [] field;

	// NULLで使われている数字を含むか
	private boolean hasNull;

	// 要素数の合計
	private int size;

	// 負荷係数
	private final float loadFactor;

	// 次にリハッシュする容量
	private int threshold;

	// 構造的変更検出用
	private int modCount;

	/**
	 * 初期容量と負荷係数を指定してIntScatterTableを生成する.
	 *
	 * @param initCapacity 初期容量
	 * @param loadFactor 負荷係数
	 */
	public ScatterIntSet(int initCapacity, float loadFactor) {
		if(initCapacity < 0) {
			throw new IllegalArgumentException("initCapacity must be positive :"+initCapacity);
		}

		field = initCapacity == 0 ?
				EMPTY_DATA :
				new int[initCapacity];
		hasNull = false;
		size = 0;
		this.loadFactor = loadFactor;
		threshold = (int) (initCapacity * loadFactor);
		modCount = 0;
	}

	/**
	 * 初期容量を指定してIntScatterTableを生成する.
	 *
	 * 負荷係数はデフォルトの値(0.75)が用いられる
	 * @param initCapacity 初期容量
	 */
	public ScatterIntSet(int initCapacity) {
		this(initCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * デフォルトの初期容量(11)と負荷係数を利用してIntScatterTableを生成する.
	 */
	public ScatterIntSet() {
		field = EMPTY_DATA;
		hasNull = false;
		size = 0;
		loadFactor = DEFAULT_LOAD_FACTOR;
		threshold = 0;
		modCount = 0;
	}

	/**
	 * 指定されたIntSetの内容をコピーする.
	 * 付加係数はデフォルトの値(0.75)，
	 * 初期容量はオリジナルのIntSetが拡張なしに入る値となる．
	 * @param original
	 */
	public ScatterIntSet(IntSet original) {
		this(0);
		try {
			ensureCapacity(original.size());
		} catch(IllegalArgumentException e) {
			throw new  RuntimeException("Too many entries!");
		}
		addAll(original);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(int i) {
		if(i == NULL) {
			return hasNull;
		}

		if(field==EMPTY_DATA) {
			return false;
		}
		int index = hash(i) % field.length;
		int limit = index-1;
		if(limit == -1) {
			limit = field.length;
		}
		for(;index!=limit;index++) {
			if(index == field.length) {
				index = 0;
			}
			int j = field[index];
			if(j == i) {
				return true;
			}
			if(j == NULL) {
				return false;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public boolean add(int i) {

		if(i == NULL) {
			if(!hasNull) {
				hasNull = true;
				size++;
				return true;
			}
			return false;
		}

		try {
			ensureCapacity(size+1);
		} catch(IllegalArgumentException e) {
			throw new  RuntimeException("Too many entries!");
		}

		for(int index = hash(i) % field.length;;index++) {
			if(index == field.length) {
				index = 0;
			}
			int j = field[index];
			if(j == i) {
				return false;
			}
			if(j == NULL) {
				field[index] = i;
				size++;
				return true;
			}
		}
	}

	/**
	 * 指定されたint値をこのコレクションから削除する
	 * @return 指定された要素を保持していればtrue
	 */
	@Override
	public boolean remove(int i) {
		if(i == NULL) {
			if(hasNull) {
				hasNull = false;
				size--;
				return true;
			} else {
				return false;
			}
		}

		if(field==EMPTY_DATA) {
			return false;
		}
		for(int index = hash(i) % field.length;;index++) {
			if(index == field.length) {
				index = 0;
			}
			int j = field[index];
			if(j == i) {
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
			if(src == field.length) {
				src = 0;
			}
			int i = field[src];
			if(i == NULL || src == index) {
				field[dst] = NULL;
				modCount++;
				return;
			} else {
				int pos = hash(i) % field.length;
				if((dst < src && pos <= dst)
				|| (src < dst && (src < pos && pos <= dst))) {
					field[dst] = i;
					dst = src;
				}
			}
		}

	}

	/**
	 * 指定されたIntSetに含まれる値をこのコレクションに追加する
	 * @param is 追加するint値の入ったIntSet
	 */
	public void add(IntSet is) {
		try {
			ensureCapacity(is.size());
		} catch(IllegalArgumentException e) {
			throw new  RuntimeException("Too many entries!");
		}
		is.forEach(i -> add(i));
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
		if(threshold >= minCapasity || field.length == MAX_CAPACITY) {
			return;
		}

		int reccommended = (field==EMPTY_DATA) ?
				DEFAULT_INIT_CAPACITY :
				HashSupport.avoid2357(field.length<<1);
		int nextCapacity = Math.max((int)(minCapasity / loadFactor)+1 , reccommended);
		nextCapacity = Math.min(nextCapacity, MAX_CAPACITY);

		rehash(nextCapacity);
	}

	private void rehash(int nextCapacity) {
		int[] tmp = new int[nextCapacity];
		for (int i = 0; i < field.length; i++) {
			int j = field[i];
			if(j == NULL) {
				continue;
			}
			for(int index = hash(j) % tmp.length;;index++) {
				if(index == tmp.length) {
					index = 0;
				}
				if(tmp[index] == NULL) {
					tmp[index] = j;
					break;
				}
			}
		}
		field = tmp;
		modCount++;
		threshold = (int) (field.length * loadFactor);
	}

	/**
	 * 要素のScatterTable上での位置を決めるためのhashを返す．
	 * {@link ScatterIntSet#hashCode()}とは無関係
	 * @param j 要素
	 * @return 要素のハッシュ(正数のint)
	 */
	private int hash(int j) {
		return Integer.MAX_VALUE & hashFunction.hash(j);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * {@inheritDoc}
	 *
	 * 列挙に必要な計算量はIntSetの容量に比例する．
	 */
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
			int removeTarget = NULL;

			@Override
			public boolean hasNext() {
				return replied < size + removed;
			}

			@Override
			public int nextInt() {
				if(expectedModCount != modCount) {
					throw new ConcurrentModificationException();
				}
				if(replied==0 && hasNull) {
					replied++;
					removable = true;
					removeTarget = NULL;
					return NULL;
				}
				try {
					for(;;) {
						int retVal = field[index++];
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
				ScatterIntSet.this.remove(removeTarget);
				expectedModCount = modCount;
				removed++;
				removable = false;
			}
		};
	}
}
