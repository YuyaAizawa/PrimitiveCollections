package com.lethe_river.util.primitive.collection;

import java.util.PrimitiveIterator;
import java.util.StringJoiner;

/**
 * IntCollectionのスケルトン実装
 * @author YuyaAizawa
 *
 */
public abstract class AbstractIntCollection implements IntCollection {

	/**
	 * 基底の(そして規定の)コンストラクタ
	 */
	protected AbstractIntCollection() {
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public abstract int size();

	@Override
	public void clear() {
		for (PrimitiveIterator.OfInt i = iterator();i.hasNext();) {
			i.nextInt();
			i.remove();
		}
	}

	@Override
	public int[] toArray() {
		int[] is = new int[size()];
		int i = 0;
		for (PrimitiveIterator.OfInt it = iterator();it.hasNext();) {
			is[i++] = it.nextInt();
		}
		return is;
	}

	@Override
	public abstract PrimitiveIterator.OfInt iterator();


	@Override
	public boolean contains(int i) {
		for (PrimitiveIterator.OfInt it = iterator();it.hasNext();) {
			if (i == it.nextInt()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(int... is) {
		boolean f = false;
		for (int i = 0; i < is.length; i++) {
			f &= contains(is[i]);
		}
		return f;
	}

	@Override
	public boolean containsAll(IntCollection is) {
		return is.stream().allMatch(i -> contains(i));
	}

	@Override
	public boolean add(int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int... is) {
		return addAll(is, 0, is.length);
	}

	@Override
	public boolean addAll(int[] is, int offset, int length) {
		PrimitiveSupport.checkBounds(is, offset, length);

		boolean changed = false;
		for (int i = 0; i < length; i++) {
			changed |= add(is[offset + i]);
		}
		return changed;
	}

	@Override
	public boolean addAll(IntCollection is) {
		boolean changed = false;
		PrimitiveIterator.OfInt i = is.iterator();
		while(i.hasNext()) {
			changed |= add(i.nextInt());
		}
		return changed;
	}

	@Override
	public boolean remove(int i) {
		for (PrimitiveIterator.OfInt it = iterator();it.hasNext();) {
			if (i == it.nextInt()) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeAll(int... is) {
		boolean modified = false;
		for (int i = 0; i < is.length; i++) {
			while(remove(is[i])) {
				modified |= true;
			}
		}
		return modified;
	}

	@Override
	public boolean removeAll(IntCollection is) {
		boolean modified = false;
		PrimitiveIterator.OfInt i = is.iterator();
		while(i.hasNext()) {
			int j = i.nextInt();
			while(remove(j)) {
				modified |= true;
			}
		}
		return modified;
	}

	@Override
	public boolean retainAll(int... is) {
		boolean modified = false;
		for(int j = 0;j < is.length;j++) {
			if(!contains(is[j])) {
				remove(is[j]);
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public boolean retainAll(IntCollection is) {
		boolean modified = false;
		for(PrimitiveIterator.OfInt i = iterator();i.hasNext();) {
			if(!is.contains(i.nextInt())) {
				i.remove();
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "[", "]");
		forEach(i -> sj.add(Integer.toString(i)));
		return sj.toString();
	}
}
