package com.lethe_river.util.primitive;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * scatter tableを用いたCharSetの実装
 *
 * - open address
 * - prime number bucket
 * - Robin Hood Hashing
 * - liner probing
 *
 * @author YuyaAizawa
 *
 */

public final class ScatterCharSet extends AbstractCharSet {


	// NULLをあらわす数字
	private static final char NULL = 0;

	private static final int DEFAULT_INIT_CAPACITY = 11;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	private static final int MAX_CAPACITY = 1<<30;

	private static final IntIntHash hashFunction = HashSupport.thomasMueller();

	private static final char[] EMPTY_DATA = {};

	// NULL以外の要素を入れる配列
	private char [] field;

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
	 * 初期容量と負荷係数を指定してCharScatterTableを生成する.
	 *
	 * @param initCapacity 初期容量
	 * @param loadFactor 負荷係数
	 */
	public ScatterCharSet(int initCapacity, float loadFactor) {
		if(initCapacity < 0) {
			throw new IllegalArgumentException("initCapacity must be positive :"+initCapacity);
		}

		field = initCapacity == 0 ?
				EMPTY_DATA :
				new char[initCapacity];
		hasNull = false;
		size = 0;
		this.loadFactor = loadFactor;
		threshold = (int) (initCapacity * loadFactor);
		modCount = 0;
	}

	/**
	 * 初期容量を指定してCharScatterTableを生成する.
	 *
	 * 負荷係数はデフォルトの値(0.75)が用いられる
	 * @param initCapacity 初期容量
	 */
	public ScatterCharSet(int initCapacity) {
		this(initCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * デフォルトの初期容量(11)と負荷係数を利用してCharScatterTableを生成する.
	 */
	public ScatterCharSet() {
		field = EMPTY_DATA;
		hasNull = false;
		size = 0;
		loadFactor = DEFAULT_LOAD_FACTOR;
		threshold = 0;
		modCount = 0;
	}

	/**
	 * 指定されたCharSetの内容をコピーする.
	 * 付加係数はデフォルトの値(0.75)，
	 * 初期容量はオリジナルのIntSetが拡張なしに入る値となる．
	 * @param original
	 */
	public ScatterCharSet(CharSet original) {
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
	public boolean contains(char ch) {
		if(ch == NULL) {
			return hasNull;
		}

		if(field==EMPTY_DATA) {
			return false;
		}
		int index = hash(ch) % field.length;
		int limit = index-1;
		if(limit == -1) {
			limit = field.length;
		}
		for(;index!=limit;index++) {
			if(index == field.length) {
				index = 0;
			}
			char j = field[index];
			if(j == ch) {
				return true;
			}
			if(j == NULL) {
				return false;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(char ch) {

		if(ch == NULL) {
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

		for(int index = hash(ch) % field.length;;index++) {
			if(index == field.length) {
				index = 0;
			}
			char j = field[index];
			if(j == ch) {
				return false;
			}
			if(j == NULL) {
				field[index] = ch;
				size++;
				return true;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(char ch) {
		if(ch == NULL) {
			if(hasNull) {
				hasNull = false;
				size--;
				return true;
			}
			return false;
		}

		if(field==EMPTY_DATA) {
			return false;
		}
		for(int index = hash(ch) % field.length;;index++) {
			if(index == field.length) {
				index = 0;
			}
			char j = field[index];
			if(j == ch) {
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
			char ch = field[src];
			if(ch == NULL || src == index) {
				field[dst] = NULL;
				modCount++;
				return;
			}
			int pos = hash(ch) % field.length;
			if((dst < src && pos <= dst)
			|| (src < dst && (src < pos && pos <= dst))) {
				field[dst] = ch;
				dst = src;
			}
		}

	}

	/**
	 * 指定されたCharSetに含まれる値をこのコレクションに追加する
	 * @param chs 追加するchar値の入ったCharSet
	 */
	public void add(CharSet chs) {
		try {
			ensureCapacity(chs.size());
		} catch(IllegalArgumentException e) {
			throw new  RuntimeException("Too many entries!");
		}
		chs.forEach(i -> add(i));
	}

	/**
	 * 指定した容量を格納できるようこのCharScatterTableの内部配列を拡張する.
	 * @param minCapasity 保持するchar値の数
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
		char[] tmp = new char[nextCapacity];
		for (int i = 0; i < field.length; i++) {
			char j = field[i];
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
	 * {@link ScatterCharSet#hashCode()}とは無関係
	 * @param j 要素
	 * @return 要素のハッシュ(正数のint)
	 */
	private static int hash(char j) {
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
	 * 列挙に必要な計算量はCharSetの容量に比例する．
	 */
	@Override
	public CharIterator iterator() {
		// 1つ目は0を返す
		return new CharIterator() {

			int index = 0;

			// 構造的変更検出用
			int expectedModCount = modCount;

			// nextで返した数
			int replied = 0;

			// 削除した要素の数
			int removed = 0;

			// removeが行える状態
			boolean removable = false;
			char removeTarget = NULL;

			@Override
			public boolean hasNext() {
				return replied < size + removed;
			}

			@Override
			public char nextChar() {
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
						char retVal = field[index++];
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
				ScatterCharSet.this.remove(removeTarget);
				expectedModCount = modCount;
				removed++;
				removable = false;
			}
		};
	}
}

