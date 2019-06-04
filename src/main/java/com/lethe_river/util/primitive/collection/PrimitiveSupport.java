package com.lethe_river.util.primitive.collection;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.PrimitiveIterator;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

final class PrimitiveSupport {

	/**
	 * 指定した桁数だけ下位bitを連続する1で埋めたint値を返す
	 * @param i
	 * @return int値
	 */
	static int oneFillInt(int i) {
		return zeroFillInt(i) ^ -1;
	}

	/**
	 * 指定した桁数だけ下位bitを連続する1で埋めたlong値を返す
	 * @param i
	 * @return long値
	 */
	static long oneFillLong(int i) {
		return zeroFillLong(i) ^ -1L;
	}

	/**
	 * 指定した桁数だけ下位bitを連続する0で埋めたint値を返す
	 * @param i
	 * @return int値
	 */
	static int zeroFillInt(int i) {
		if(i < 0) {
			return -1;
		}
		if(i >= Integer.SIZE) {
			return  0;
		}
		return Integer.MIN_VALUE >> i-1;
	}

	/**
	 * 指定した桁数だけ下位bitを連続する0で埋めたlong値を返す
	 * @param i
	 * @return long値
	 */
	static long zeroFillLong(int i) {
		if(i < 0) {
			return -1L;
		}
		if (i >= Long.SIZE) {
			return 0L;
		}
		return Long.MIN_VALUE >> i-1;
	}

	/**
	 * 指定した配列に対してoffset及びlengthが妥当か検証する
	 * @param array 検証する配列
	 * @param offset 開始インデックス
	 * @param length 開始からの長さ
	 *
	 * @throw NullPointerException 配列がnullだったとき
	 * @throw ArrayIndexOutOfBoundsException offsetまたはlengthが不正だったとき
	 */
	static void checkBounds(int[] array, int offset, int length) {
		if ((offset | length | (array.length - (length + offset)) | (offset + length)) < 0) {
			throw new ArrayIndexOutOfBoundsException(String.format(
					"array length: %d, offset: %d, length: %d",array.length, offset, length));
		}
	}

	/**
	 * 指定した配列に対してoffset及びlengthが妥当か検証する
	 * @param array 検証する配列
	 * @param offset 開始インデックス
	 * @param length 開始からの長さ
	 *
	 * @throw NullPointerException 配列がnullだったとき
	 * @throw ArrayIndexOutOfBoundsException offsetまたはlengthが不正だったとき
	 */
	static void checkBounds(long[] array, int offset, int length) {
		if ((offset | length | (array.length - (length + offset)) | (offset + length)) < 0) {
			throw new ArrayIndexOutOfBoundsException(String.format(
					"array length: %d, offset: %d, length: %d",array.length, offset, length));
		}
	}

	/**
	 * 指定した配列に対してoffset及びlengthが妥当か検証する
	 * @param array 検証する配列
	 * @param offset 開始インデックス
	 * @param length 開始からの長さ
	 *
	 * @throw NullPointerException 配列がnullだったとき
	 * @throw ArrayIndexOutOfBoundsException offsetまたはlengthが不正だったとき
	 */

	static void checkBounds(byte[] array, int offset, int length) {
		if ((offset | length | (array.length - (length + offset)) | (offset + length)) < 0) {
			throw new ArrayIndexOutOfBoundsException(String.format(
					"array length: %d, offset: %d, length: %d",array.length, offset, length));
		}
	}

	/**
	 * 指定した配列に対してoffset及びlengthが妥当か検証する
	 * @param array 検証する配列
	 * @param offset 開始インデックス
	 * @param length 開始からの長さ
	 *
	 * @throw NullPointerException 配列がnullだったとき
	 * @throw ArrayIndexOutOfBoundsException offsetまたはlengthが不正だったとき
	 */
	static void checkBounds(char[] array, int offset, int length) {
		if ((offset | length | (array.length - (length + offset)) | (offset + length)) < 0) {
			throw new ArrayIndexOutOfBoundsException(String.format(
					"array length: %d, offset: %d, length: %d",array.length, offset, length));
		}
	}

	static IntCollection unmodifiableCollection(IntCollection original) {
		return new UnmodifiableIntCollection(original);
	}

	static IntList unmodifiableList(IntList original) {
		return new UnmodifiableIntList(original);
	}

	static IntListIterator unmodifiableListIterator(IntListIterator original) {
		return new UnmodifiableListIterator(original);
	}

	static PrimitiveIterator.OfInt unmodifiableIterator(PrimitiveIterator.OfInt original) {
		return new UnmodifiableIteratorOfInt(original);
	}

	static Integer[] boxed(int[] array) {
		Integer[] boxed = new Integer[array.length];
		for (int i = 0; i < array.length; i++) {
			boxed[i] = Integer.valueOf(array[i]);
		}
		return boxed;
	}

	static Long[] boxed(long[] array) {
		Long[] boxed = new Long[array.length];
		for (int i = 0; i < array.length; i++) {
			boxed[i] = Long.valueOf(array[i]);
		}
		return boxed;
	}

	static Byte[] boxed(byte[] array) {
		Byte[] boxed = new Byte[array.length];
		for (int i = 0; i < array.length; i++) {
			boxed[i] = Byte.valueOf(array[i]);
		}
		return boxed;
	}

	static Double[] boxed(double[] array) {
		Double[] boxed = new Double[array.length];
		for (int i = 0; i < array.length; i++) {
			boxed[i] = Double.valueOf(array[i]);
		}
		return boxed;
	}

	static Character[] boxed(char[] array) {
		Character[] boxed = new Character[array.length];
		for (int i = 0; i < array.length; i++) {
			boxed[i] = Character.valueOf(array[i]);
		}
		return boxed;
	}

	static Collection<Integer> boxed(IntCollection original) {
		return new BoxedIntCollection(original);
	}

	static List<Integer> boxed(IntList original) {
		if(original instanceof RandomAccess) {
			return new BoxedIntListRandomAccess(original);
		}
		return new BoxedIntList(original);
	}

	static Set<Integer> boxed(IntSet original) {
		return new BoxedIntSet(original);
	}

	static SortedSet<Integer> boxed(IntSortedSet original) {
		return new BoxedIntSortedSet(original);
	}

	static Collection<Long> boxed(LongCollection original) {
		return new BoxedLongCollection(original);
	}

	static Set<Long> boxed(LongSet original) {
		return new BoxedLongSet(original);
	}

	static SortedSet<Long> boxed(LongSortedSet original) {
		return new BoxedLongSortedSet(original);
	}

	static Collection<Byte> boxed(ByteCollection original) {
		return new BoxedByteCollection(original);
	}

	static List<Byte> boxed(ByteList original) {
		return new BoxedByteList(original);
	}

	static Set<Byte> boxed(ByteSet original) {
		return new BoxedByteSet(original);
	}

	static SortedSet<Byte> boxed(ByteSortedSet original) {
		return new BoxedByteSortedSet(original);
	}

	static Collection<Character> boxed(CharCollection original) {
		return new BoxedCharCollection(original);
	}

	static Set<Character> boxed(CharSet original) {
		return new BoxedCharSet(original);
	}

	static SortedSet<Character> boxed(CharSortedSet original) {
		return new BoxedCharSortedSet(original);
	}

	static Spliterator<Byte> toByte(Spliterator<? extends Integer> signed) {
		return new IntegerByteSpliterator(signed);
	}

	static Spliterator<Character> toChar(Spliterator<? extends Integer> spliterator) {
		return new IntegerCharSpliterator(spliterator);
	}

	/**
	 * 指定された配列中に指定した値が初めて出現するインデックスを返す．出現しなければ-1.
	 * @param array
	 * @param target
	 * @return 指定した値が初めて出現するインデックス
	 */
	static int linearSearchFirst(int [] array, int target) {
		for (int i = 0; i < array.length; i++) {
			if(array[i] == target) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 指定された配列中に指定した値が初めて出現するインデックスを返す．出現しなければ-1.
	 * @param array
	 * @param target
	 * @return 指定した値が初めて出現するインデックス
	 */
	static int linearSearchFirst(long[] array, long target) {
		for (int i = 0; i < array.length; i++) {
			if(array[i] == target) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 指定された配列中に指定した値が初めて出現するインデックスを返す．出現しなければ-1.
	 * @param array
	 * @param target
	 * @return 指定した値が初めて出現するインデックス
	 */
	static int linearSearchFirst(byte [] array, byte target) {
		for (int i = 0; i < array.length; i++) {
			if(array[i] == target) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 指定された配列中に指定した値が初めて出現するインデックスを返す．出現しなければ-1.
	 * @param array
	 * @param target
	 * @return 指定した値が初めて出現するインデックス
	 */
	static int linearSearchFirst(char[] array, char target) {
		for (int i = 0; i < array.length; i++) {
			if(array[i] == target) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 第一引数の表す系列が第二引数の表す系列の部分系列かどうか判定する
	 * @param i1
	 * @param i2
	 * @return 部分系列ならtrue
	 */
	static <T> boolean isSubsequence(PrimitiveIterator.OfInt i1, PrimitiveIterator.OfInt i2) {
		loop: while(i1.hasNext()) {
			int t1 = i1.nextInt();
			while(i2.hasNext()) {
				int t2 = i2.nextInt();
				if(t1 == t2) {
					continue loop;
				}
			}
			return false;
		}
		return true;
	}



	private static class UnmodifiableIntCollection implements IntCollection {
		private static final long serialVersionUID = 4417160728544445842L;
		private final IntCollection original;

		public UnmodifiableIntCollection(IntCollection original) {
			this.original = original;
		}

		@Override
		public boolean equals(Object obj) {
			return original.equals(obj);
		}

		@Override
		public boolean isEmpty() {
			return original.isEmpty();
		}

		@Override
		public int size() {
			return original.size();
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean contains(int i) {
			return original.contains(i);
		}

		@Override
		public java.util.PrimitiveIterator.OfInt iterator() {
			return unmodifiableIterator(original.iterator());
		}

		@Override
		public int[] toArray() {
			return original.toArray();
		}

		@Override
		public boolean add(int i) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean addAll(int... is) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean addAll(int[] is, int offset, int length) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean addAll(IntCollection is) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean remove(int i) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeAll(int... is) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeAll(IntCollection is) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean containsAll(int... is) {
			return original.containsAll(is);
		}

		@Override
		public boolean retainAll(int... is) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean retainAll(IntCollection is) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean containsAll(IntCollection is) {
			return original.containsAll(is);
		}

		@Override
		public IntStream stream() {
			return original.stream();
		}

		@Override
		public IntStream parallelStream() {
			return original.parallelStream();
		}

		@Override
		public java.util.Spliterator.OfInt spliterator() {
			return original.spliterator();
		}

		@Override
		public void forEach(IntConsumer action) {
			original.forEach(action);
		}

		@Override
		public Collection<Integer> boxedView() {
			return Collections.unmodifiableCollection(original.boxedView());
		}
	}

	private static class UnmodifiableIntList extends UnmodifiableIntCollection implements IntList {
		private static final long serialVersionUID = 3198211791245188355L;
		private final IntList original;

		public UnmodifiableIntList(IntList original) {
			super(original);
			this.original = original;
		}

		@Override
		public void insert(int index, int element) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void insertAll(int index, int[] is, int offset, int length) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void insertAll(int index, IntCollection c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int removeAt(int index) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int get(int index) {
			return original.get(index);
		}

		@Override
		public int set(int index, int element) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int indexOf(int element) {
			return original.indexOf(element);
		}

		@Override
		public int lastIndexOf(int element) {
			return original.lastIndexOf(element);
		}

		@Override
		public IntListIterator listIterator(int index) {
			return unmodifiableListIterator(original.listIterator());
		}

		@Override
		public java.util.PrimitiveIterator.OfInt reversedIterator() {
			return unmodifiableIterator(original.reversedIterator());
		}

		@Override
		public IntList subList(int fromIndex, int toIndex) {
			return unmodifiableList(original.subList(fromIndex, toIndex));
		}

		@Override
		public List<Integer> boxedView() {
			return Collections.unmodifiableList(original.boxedView());
		}

		@Override
		public boolean isSubsequence(IntList list) {
			return original.isSubsequence(list);
		}

		@Override
		public IntStream stream() {
			return original.stream();
		}

		@Override
		public IntStream parallelStream() {
			return original.parallelStream();
		}

		@Override
		public boolean isSupersequenceOf(IntList list) {
			return original.isSupersequenceOf(list);
		}

		@Override
		public void forEach(IntConsumer action) {
			original.forEach(action);
		}

		@Override
		public IntListIterator listIterator() {
			return unmodifiableListIterator(original.listIterator());
		}

		@Override
		public IntStream reversedStream() {
			return original.reversedStream();
		}

		@Override
		public java.util.Spliterator.OfInt spliterator() {
			return original.spliterator();
		}
	}

	private static class UnmodifiableListIterator implements IntListIterator {

		private final IntListIterator original;

		public UnmodifiableListIterator(IntListIterator original) {
			this.original = original;
		}
		@Override
		public void add(Integer e) {
			throw new UnsupportedOperationException();
		}
		@Override
		public Integer next() {
			return original.next();
		}
		@Override
		public Integer previous() {
			return original.previous();
		}
		@Override
		public void set(Integer e) {
			throw new UnsupportedOperationException();
		}
		@Override
		public void add(int e) {
			throw new UnsupportedOperationException();
		}
		@Override
		public int nextInt() {
			return original.nextInt();
		}
		@Override
		public int previousInt() {
			return original.previousInt();
		}
		@Override
		public void set(int e) {
			throw new UnsupportedOperationException();
		}
		@Override
		public boolean hasNext() {
			return original.hasNext();
		}
		@Override
		public boolean hasPrevious() {
			return original.hasPrevious();
		}
		@Override
		public void forEachRemaining(Consumer<? super Integer> action) {
			original.forEachRemaining(action);
		}
		@Override
		public void forEachRemaining(IntConsumer action) {
			original.forEachRemaining(action);
		}
		@Override
		public int nextIndex() {
			return original.nextIndex();
		}
		@Override
		public int previousIndex() {
			return original.previousIndex();
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private static final class UnmodifiableIteratorOfInt implements PrimitiveIterator.OfInt {

		private final PrimitiveIterator.OfInt original;
		public UnmodifiableIteratorOfInt(OfInt original) {
			this.original = original;
		}

		@Override
		public boolean hasNext() {
			return original.hasNext();
		}

		@Override
		public int nextInt() {
			return original.nextInt();
		}

	}

	private static class BoxedIntCollection extends AbstractCollection<Integer> {
		final IntCollection original;

		BoxedIntCollection(IntCollection original) {
			this.original = original;
		}

		@Override
		public int size() {
			return original.size();
		}

		@Override
		public boolean isEmpty() {
			return original.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			if (o != null && o instanceof Integer) {
				return original.contains((int) o);
			}
			return false;
		}

		@Override
		public Iterator<Integer> iterator() {
			return original.iterator();
		}

		@Override
		public Object[] toArray() {
			return boxed(original.toArray());
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			Object[] array = (Object[]) Array.newInstance(a.getClass().getComponentType(), size());

			Iterator<Integer> j = iterator();
			for (int i = 0; i < array.length; i++) {
				array[i] = Integer.valueOf(j.next());
			}
			return (T[]) array;
		}

		@Override
		public boolean add(Integer e) {
			return original.add(e);
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Integer) {
				return original.remove((int) o);
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return c.stream().allMatch(e -> contains(e));
		}

		@Override
		public boolean addAll(Collection<? extends Integer> c) {
			return c.stream().map(i -> add(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return c.stream().map(i -> remove(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			boolean modified = false;
			Iterator<Integer> i = iterator();
			for (; i.hasNext();) {
				if (!c.contains(i.next())) {
					i.remove();
					modified = true;
				}
			}
			return modified;
		}

		@Override
		public void clear() {
			original.clear();
		}

		@Override
		public Spliterator<Integer> spliterator() {
			return original.spliterator();
		}
	}

	private static class BoxedIntList extends AbstractList<Integer> {
		private final IntList original;

		BoxedIntList(IntList original) {
			this.original = original;
		}

		@Override
		public int size() {
			return original.size();
		}

		@Override
		public boolean isEmpty() {
			return original.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			if (o != null && o instanceof Integer) {
				return original.contains((int) o);
			}
			return false;
		}

		@Override
		public Iterator<Integer> iterator() {
			return original.iterator();
		}

		@Override
		public Object[] toArray() {
			return boxed(original.toArray());
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			Object[] array = (Object[]) Array.newInstance(a.getClass().getComponentType(), size());

			Iterator<Integer> j = iterator();
			for (int i = 0; i < array.length; i++) {
				array[i] = Integer.valueOf(j.next());
			}
			return (T[]) array;
		}

		@Override
		public boolean add(Integer e) {
			return add(e);
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Integer) {
				return original.remove((Integer) o);
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return c.stream().allMatch(e -> contains(e));
		}

		@Override
		public boolean addAll(Collection<? extends Integer> c) {
			return c.stream().map(i -> add(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean addAll(int index, Collection<? extends Integer> c) {
			int size = original.size();
			original.addAll(new AbstractIntCollection() {
				@Override
				public int size() {
					return c.size();
				}

				@Override
				public PrimitiveIterator.OfInt iterator() {
					Iterator<? extends Integer> pi = c.iterator();
					return new PrimitiveIterator.OfInt() {
						@Override
						public boolean hasNext() {
							return pi.hasNext();
						}

						@Override
						public int nextInt() {
							return pi.next();
						}
					};
				}
			});
			return size != original.size();
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return c.stream().map(i -> remove(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			boolean modified = false;
			Iterator<Integer> i = iterator();
			for (; i.hasNext();) {
				if (!c.contains(i.next())) {
					i.remove();
					modified = true;
				}
			}
			return modified;
		}

		@Override
		public void clear() {
			original.clear();
		}

		@Override
		public Integer get(int index) {
			return original.get(index);
		}

		@Override
		public Integer set(int index, Integer element) {
			return original.set(index, element);
		}

		@Override
		public void add(int index, Integer element) {
			original.insert(index, element);
		}

		@Override
		public Integer remove(int index) {
			return original.removeAt(index);
		}

		@Override
		public int indexOf(Object o) {
			if (o instanceof Integer && o != null) {
				return original.indexOf((Integer) o);
			}
			return -1;
		}

		@Override
		public int lastIndexOf(Object o) {
			if (o instanceof Integer && o != null) {
				return original.lastIndexOf((Integer) o);
			}
			return -1;
		}

		@Override
		public ListIterator<Integer> listIterator() {
			return original.listIterator();
		}

		@Override
		public ListIterator<Integer> listIterator(int index) {
			return original.listIterator(index);
		}

		@Override
		public List<Integer> subList(int fromIndex, int toIndex) {
			return original.subList(fromIndex, toIndex).boxedView();
		}
	}

	private static class BoxedIntListRandomAccess extends BoxedIntList implements RandomAccess {
		BoxedIntListRandomAccess(IntList original) {
			super(original);
		}
	}

	private static class BoxedIntSet extends AbstractSet<Integer> {
		private final IntSet original;

		BoxedIntSet(IntSet original) {
			this.original = original;
		}

		@Override
		public int size() {
			return original.size();
		}

		@Override
		public boolean isEmpty() {
			return original.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			if (o != null && o instanceof Integer) {
				return original.contains((int) o);
			}
			return false;
		}

		@Override
		public Iterator<Integer> iterator() {
			return original.iterator();
		}

		@Override
		public Object[] toArray() {
			return boxed(original.toArray());
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			Object[] array = (Object[]) Array.newInstance(a.getClass().getComponentType(), size());

			Iterator<Integer> j = iterator();
			for (int i = 0; i < array.length; i++) {
				array[i] = Integer.valueOf(j.next());
			}
			return (T[]) array;
		}

		@Override
		public boolean add(Integer e) {
			return original.add(e);
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Integer) {
				return original.remove((Integer) o);
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return c.stream().allMatch(e -> contains(e));
		}

		@Override
		public boolean addAll(Collection<? extends Integer> c) {
			return c.stream().map(i -> add(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return c.stream().map(i -> remove(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			boolean modified = false;
			Iterator<Integer> i = iterator();
			for (; i.hasNext();) {
				if (!c.contains(i.next())) {
					i.remove();
					modified = true;
				}
			}
			return modified;
		}

		@Override
		public void clear() {
			original.clear();
		}

		@Override
		public Spliterator<Integer> spliterator() {
			return original.spliterator();
		}
	}

	private static class BoxedIntSortedSet extends BoxedIntSet implements SortedSet<Integer> {

		BoxedIntSortedSet(IntSortedSet original) {
			super(original);
			this.original = original;
		}

		private final IntSortedSet original;

		@Override
		public Comparator<? super Integer> comparator() {
			return Comparator.naturalOrder();
		}

		@Override
		public SortedSet<Integer> subSet(Integer from, Integer to) {
			return boxed(original.subSet(from, to));
		}

		@Override
		public SortedSet<Integer> headSet(Integer to) {
			return boxed(original.headSet(to));
		}

		@Override
		public SortedSet<Integer> tailSet(Integer from) {
			return boxed(original.tailSet(from));
		}

		@Override
		public Integer first() {
			return original.first();
		}

		@Override
		public Integer last() {
			return original.last();
		}
	}

	private static class BoxedLongCollection extends AbstractCollection<Long> {
		final LongCollection original;

		BoxedLongCollection(LongCollection original) {
			this.original = original;
		}

		@Override
		public int size() {
			return original.size();
		}

		@Override
		public boolean isEmpty() {
			return original.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			if (o != null && o instanceof Long) {
				return original.contains((long) o);
			}
			return false;
		}

		@Override
		public Iterator<Long> iterator() {
			return original.iterator();
		}

		@Override
		public Object[] toArray() {
			return boxed(original.toArray());
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			Object[] array = (Object[]) Array.newInstance(a.getClass().getComponentType(), size());

			Iterator<Long> j = iterator();
			for (int i = 0; i < array.length; i++) {
				array[i] = Long.valueOf(j.next());
			}
			return (T[]) array;
		}

		@Override
		public boolean add(Long e) {
			return original.add(e);
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Long) {
				return original.remove((long) o);
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return c.stream().allMatch(e -> contains(e));
		}

		@Override
		public boolean addAll(Collection<? extends Long> c) {
			return c.stream().map(i -> add(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return c.stream().map(i -> remove(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			boolean modified = false;
			Iterator<Long> i = iterator();
			for (; i.hasNext();) {
				if (!c.contains(i.next())) {
					i.remove();
					modified = true;
				}
			}
			return modified;
		}

		@Override
		public void clear() {
			original.clear();
		}

		@Override
		public Spliterator<Long> spliterator() {
			return original.spliterator();
		}
	}

	private static class BoxedLongSet extends AbstractSet<Long> {
		private final LongSet original;

		BoxedLongSet(LongSet original) {
			this.original = original;
		}

		@Override
		public int size() {
			return original.size();
		}

		@Override
		public boolean isEmpty() {
			return original.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			if (o != null && o instanceof Long) {
				return original.contains((long) o);
			}
			return false;
		}

		@Override
		public Iterator<Long> iterator() {
			return original.iterator();
		}

		@Override
		public Object[] toArray() {
			return boxed(original.toArray());
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			Object[] array = (Object[]) Array.newInstance(a.getClass().getComponentType(), size());

			Iterator<Long> j = iterator();
			for (int i = 0; i < array.length; i++) {
				array[i] = Long.valueOf(j.next());
			}
			return (T[]) array;
		}

		@Override
		public boolean add(Long e) {
			return original.add(e);
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Integer) {
				return original.remove((Integer) o);
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return c.stream().allMatch(e -> contains(e));
		}

		@Override
		public boolean addAll(Collection<? extends Long> c) {
			return c.stream().map(l -> add(l)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return c.stream().map(l -> remove(l)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			boolean modified = false;
			Iterator<Long> i = iterator();
			for (; i.hasNext();) {
				if (!c.contains(i.next())) {
					i.remove();
					modified = true;
				}
			}
			return modified;
		}

		@Override
		public void clear() {
			original.clear();
		}

		@Override
		public Spliterator<Long> spliterator() {
			return original.spliterator();
		}
	}

	private static class BoxedLongSortedSet extends BoxedLongSet implements SortedSet<Long> {

		BoxedLongSortedSet(LongSortedSet original) {
			super(original);
			this.original = original;
		}

		private final LongSortedSet original;

		@Override
		public Comparator<? super Long> comparator() {
			return Comparator.naturalOrder();
		}

		@Override
		public SortedSet<Long> subSet(Long from, Long to) {
			return boxed(original.subSet(from, to));
		}

		@Override
		public SortedSet<Long> headSet(Long to) {
			return boxed(original.headSet(to));
		}

		@Override
		public SortedSet<Long> tailSet(Long from) {
			return boxed(original.tailSet(from));
		}

		@Override
		public Long first() {
			return original.first();
		}

		@Override
		public Long last() {
			return original.last();
		}
	}

	private static class BoxedByteCollection extends AbstractCollection<Byte> {
		final ByteCollection original;

		BoxedByteCollection(ByteCollection original) {
			this.original = original;
		}

		@Override
		public int size() {
			return original.size();
		}

		@Override
		public boolean isEmpty() {
			return original.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			if (o != null && o instanceof Byte) {
				return original.contains((byte) o);
			}
			return false;
		}

		@Override
		public Iterator<Byte> iterator() {
			return original.iterator();
		}

		@Override
		public Object[] toArray() {
			return boxed(original.toArray());
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			Object[] array = (Object[]) Array.newInstance(a.getClass().getComponentType(), size());

			Iterator<Byte> j = iterator();
			for (int i = 0; i < array.length; i++) {
				array[i] = Integer.valueOf(j.next());
			}
			return (T[]) array;
		}

		@Override
		public boolean add(Byte e) {
			return original.add(e);
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Byte) {
				return original.remove((byte) o);
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return c.stream().allMatch(e -> contains(e));
		}

		@Override
		public boolean addAll(Collection<? extends Byte> c) {
			return c.stream().map(i -> add(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return c.stream().map(i -> remove(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			boolean modified = false;
			Iterator<Byte> i = iterator();
			for (; i.hasNext();) {
				if (!c.contains(i.next())) {
					i.remove();
					modified = true;
				}
			}
			return modified;
		}

		@Override
		public void clear() {
			original.clear();
		}

		@Override
		public Spliterator<Byte> spliterator() {
			return PrimitiveSupport.toByte(original.spliteratorSigned());
		}
	}

	private static class BoxedByteList extends AbstractList<Byte> {
		private final ByteList original;

		BoxedByteList(ByteList original) {
			this.original = original;
		}

		@Override
		public int size() {
			return original.size();
		}

		@Override
		public boolean isEmpty() {
			return original.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			if (o != null && o instanceof Byte) {
				return original.contains((byte) o);
			}
			return false;
		}

		@Override
		public Iterator<Byte> iterator() {
			return original.iterator();
		}

		@Override
		public Object[] toArray() {
			return boxed(original.toArray());
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			Object[] array = (Object[]) Array.newInstance(a.getClass().getComponentType(), size());

			Iterator<Byte> j = iterator();
			for (int i = 0; i < array.length; i++) {
				array[i] = Integer.valueOf(j.next());
			}
			return (T[]) array;
		}

		@Override
		public boolean add(Byte e) {
			return add(e);
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Byte) {
				return original.remove((Byte) o);
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return c.stream().allMatch(e -> contains(e));
		}

		@Override
		public boolean addAll(Collection<? extends Byte> c) {
			return c.stream().map(i -> add(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean addAll(int index, Collection<? extends Byte> c) {
			if(c.isEmpty()) {
				return false;
			}
			original.insertAll(index, new AbstractByteCollection() {
				@Override
				public int size() {
					return c.size();
				}

				@Override
				public ByteIterator iterator() {
					Iterator<? extends Byte> pi = c.iterator();
					return new ByteIterator() {
						@Override
						public boolean hasNext() {
							return pi.hasNext();
						}

						@Override
						public byte nextByte() {
							return pi.next();
						}
					};
				}
			});
			return true;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return c.stream().map(i -> remove(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			boolean modified = false;
			Iterator<Byte> i = iterator();
			for (; i.hasNext();) {
				if (!c.contains(i.next())) {
					i.remove();
					modified = true;
				}
			}
			return modified;
		}

		@Override
		public void clear() {
			original.clear();
		}

		@Override
		public Byte get(int index) {
			return original.get(index);
		}

		@Override
		public Byte set(int index, Byte element) {
			return original.set(index, element);
		}

		@Override
		public void add(int index, Byte element) {
			original.insert(index, element);
		}

		@Override
		public Byte remove(int index) {
			return original.removeAt(index);
		}

		@Override
		public int indexOf(Object o) {
			if (o != null && o instanceof Byte) {
				return original.indexOf((Byte) o);
			}
			return -1;
		}

		@Override
		public int lastIndexOf(Object o) {
			if (o != null && o instanceof Byte) {
				return original.lastIndexOf((Byte) o);
			}
			return -1;
		}

		@Override
		public ListIterator<Byte> listIterator() {
			return original.listIterator();
		}

		@Override
		public ListIterator<Byte> listIterator(int index) {
			return original.listIterator(index);
		}

		@Override
		public List<Byte> subList(int fromIndex, int toIndex) {
			return original.subList(fromIndex, toIndex).boxedView();
		}
	}

	private static class BoxedByteSet extends AbstractSet<Byte> {
		private final ByteSet original;

		BoxedByteSet(ByteSet original) {
			this.original = original;
		}

		@Override
		public int size() {
			return original.size();
		}

		@Override
		public boolean isEmpty() {
			return original.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			if (o != null && o instanceof Byte) {
				return original.contains((byte) o);
			}
			return false;
		}

		@Override
		public Iterator<Byte> iterator() {
			return original.iterator();
		}

		@Override
		public Object[] toArray() {
			return boxed(original.toArray());
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			Object[] array = (Object[]) Array.newInstance(a.getClass().getComponentType(), size());

			Iterator<Byte> j = iterator();
			for (int i = 0; i < array.length; i++) {
				array[i] = Byte.valueOf(j.next());
			}
			return (T[]) array;
		}

		@Override
		public boolean add(Byte e) {
			return original.add(e);
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Integer) {
				return original.remove((Byte) o);
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return c.stream().allMatch(e -> contains(e));
		}

		@Override
		public boolean addAll(Collection<? extends Byte> c) {
			return c.stream().map(i -> add(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return c.stream().map(i -> remove(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			boolean modified = false;
			Iterator<Byte> i = iterator();
			for (; i.hasNext();) {
				if (!c.contains(i.next())) {
					i.remove();
					modified = true;
				}
			}
			return modified;
		}

		@Override
		public void clear() {
			original.clear();
		}

		@Override
		public Spliterator<Byte> spliterator() {
			return toByte(original.spliteratorSigned());
		}
	}

	private static class BoxedByteSortedSet extends BoxedByteSet implements SortedSet<Byte> {
		BoxedByteSortedSet(ByteSortedSet original) {
			super(original);
			this.original = original;
		}

		private final ByteSortedSet original;

		@Override
		public Comparator<? super Byte> comparator() {
			return Comparator.naturalOrder();
		}

		@Override
		public Byte first() {
			return original.first();
		}

		@Override
		public SortedSet<Byte> headSet(Byte to) {
			return boxed(original.headSet(to));
		}

		@Override
		public Byte last() {
			return original.last();
		}

		@Override
		public SortedSet<Byte> subSet(Byte from, Byte to) {
			return boxed(original.subSet(from, to));
		}

		@Override
		public SortedSet<Byte> tailSet(Byte from) {
			return boxed(original.tailSet(from));
		}

	}

	private static class BoxedCharCollection extends AbstractCollection<Character> {
		final CharCollection original;

		BoxedCharCollection(CharCollection original) {
			this.original = original;
		}

		@Override
		public int size() {
			return original.size();
		}

		@Override
		public boolean isEmpty() {
			return original.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			if (o != null && o instanceof Character) {
				return original.contains((char) o);
			}
			return false;
		}

		@Override
		public Iterator<Character> iterator() {
			return original.iterator();
		}

		@Override
		public Object[] toArray() {
			return boxed(original.toArray());
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			Object[] array = (Object[]) Array.newInstance(a.getClass().getComponentType(), size());

			Iterator<Character> j = iterator();
			for (int i = 0; i < array.length; i++) {
				array[i] = Character.valueOf(j.next());
			}
			return (T[]) array;
		}

		@Override
		public boolean add(Character e) {
			return original.add(e);
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Character) {
				return original.remove((char) o);
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return c.stream().allMatch(e -> contains(e));
		}

		@Override
		public boolean addAll(Collection<? extends Character> c) {
			return c.stream().map(i -> add(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return c.stream().map(i -> remove(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			boolean modified = false;
			Iterator<Character> i = iterator();
			for (; i.hasNext();) {
				if (!c.contains(i.next())) {
					i.remove();
					modified = true;
				}
			}
			return modified;
		}

		@Override
		public void clear() {
			original.clear();
		}

		@Override
		public Spliterator<Character> spliterator() {
			return PrimitiveSupport.toChar(original.spliterator());
		}
	}

	private static class BoxedCharSet extends AbstractSet<Character> {
		private final CharSet original;

		BoxedCharSet(CharSet original) {
			this.original = original;
		}

		@Override
		public int size() {
			return original.size();
		}

		@Override
		public boolean isEmpty() {
			return original.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			if (o != null && o instanceof Character) {
				return original.contains((char) o);
			}
			return false;
		}

		@Override
		public Iterator<Character> iterator() {
			return original.iterator();
		}

		@Override
		public Object[] toArray() {
			return boxed(original.toArray());
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			Object[] array = (Object[]) Array.newInstance(a.getClass().getComponentType(), size());

			Iterator<Character> j = iterator();
			for (int i = 0; i < array.length; i++) {
				array[i] = Character.valueOf(j.next());
			}
			return (T[]) array;
		}

		@Override
		public boolean add(Character e) {
			return original.add(e);
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Character) {
				return original.remove((Character) o);
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return c.stream().allMatch(e -> contains(e));
		}

		@Override
		public boolean addAll(Collection<? extends Character> c) {
			return c.stream().map(i -> add(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return c.stream().map(i -> remove(i)).reduce(false, (l, r) -> l | r);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			boolean modified = false;
			Iterator<Character> i = iterator();
			for (; i.hasNext();) {
				if (!c.contains(i.next())) {
					i.remove();
					modified = true;
				}
			}
			return modified;
		}

		@Override
		public void clear() {
			original.clear();
		}

		@Override
		public Spliterator<Character> spliterator() {
			return toChar(original.spliterator());
		}
	}

	private static class BoxedCharSortedSet extends BoxedCharSet implements SortedSet<Character> {

		BoxedCharSortedSet(CharSortedSet original) {
			super(original);
			this.original = original;
		}

		private final CharSortedSet original;

		@Override
		public Comparator<? super Character> comparator() {
			return Comparator.naturalOrder();
		}

		@Override
		public SortedSet<Character> subSet(Character from, Character to) {
			return boxed(original.subSet(from, to));
		}

		@Override
		public SortedSet<Character> headSet(Character to) {
			return boxed(original.headSet(to));
		}

		@Override
		public SortedSet<Character> tailSet(Character from) {
			return boxed(original.tailSet(from));
		}

		@Override
		public Character first() {
			return original.first();
		}

		@Override
		public Character last() {
			return original.last();
		}
	}

	private static class IntegerByteSpliterator implements Spliterator<Byte> {
		final Spliterator<? extends Integer> original;

		IntegerByteSpliterator(Spliterator<? extends Integer> signed) {
			original = signed;
		}

		@Override
		public int characteristics() {
			return original.characteristics();
		}

		@Override
		public long estimateSize() {
			return original.estimateSize();
		}

		@Override
		public boolean tryAdvance(Consumer<? super Byte> arg0) {
			return original.tryAdvance((Integer b) -> arg0.accept((byte)(int) b));
		}

		@Override
		public Spliterator<Byte> trySplit() {
			Spliterator<? extends Integer> splited = original.trySplit();
			if(splited == null) {
				return null;
			}
			return new IntegerByteSpliterator(original.trySplit());
		}
	}

	private static class IntegerCharSpliterator implements Spliterator<Character> {
		final Spliterator<? extends Integer> original;

		IntegerCharSpliterator(Spliterator<? extends Integer> spliterator) {
			original = spliterator;
		}

		@Override
		public int characteristics() {
			return original.characteristics();
		}

		@Override
		public long estimateSize() {
			return original.estimateSize();
		}

		@Override
		public boolean tryAdvance(Consumer<? super Character> arg0) {
			return original.tryAdvance((Integer b) -> arg0.accept((char)(int) b));
		}

		@Override
		public Spliterator<Character> trySplit() {
			Spliterator<? extends Integer> splited = original.trySplit();
			if(splited == null) {
				return null;
			}
			return new IntegerCharSpliterator(original.trySplit());
		}
	}
}
