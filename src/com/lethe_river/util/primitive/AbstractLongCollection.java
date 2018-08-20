package com.lethe_river.util.primitive;

import java.util.PrimitiveIterator;
import java.util.StringJoiner;

/**
 * LongCollectionのスケルトン実装
 * @author YuyaAizawa
 *
 */
public abstract class AbstractLongCollection implements LongCollection {

	/**
	 * 基底の(そして規定の)コンストラクタ
	 */
	protected AbstractLongCollection() {
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public abstract int size();

	@Override
	public void clear() {
		for (PrimitiveIterator.OfLong i = iterator();i.hasNext();) {
			i.nextLong();
			i.remove();
		}
	}

	@Override
	public long[] toArray() {
		long[] ls = new long[size()];
		int i = 0;
		for (PrimitiveIterator.OfLong it = iterator();it.hasNext();) {
			ls[i++] = it.nextLong();
		}
		return ls;
	}

	@Override
	public abstract PrimitiveIterator.OfLong iterator();


	@Override
	public boolean contains(long l) {
		for (PrimitiveIterator.OfLong it = iterator();it.hasNext();) {
			if (l == it.nextLong()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(long... ls) {
		boolean f = false;
		for (int i = 0; i < ls.length; i++) {
			f &= contains(ls[i]);
		}
		return f;
	}

	@Override
	public boolean containsAll(LongCollection ls) {
		return ls.stream().allMatch(l -> contains(l));
	}

	@Override
	public boolean add(long i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(long... ls) {
		return addAll(ls, 0, ls.length);
	}

	@Override
	public boolean addAll(long[] ls, int offset, int length) {
		PrimitiveSupport.checkBounds(ls, offset, length);

		boolean changed = false;
		for (int i = 0; i < length; i++) {
			changed |= add(ls[offset + i]);
		}
		return changed;
	}

	@Override
	public boolean addAll(LongCollection ls) {
		boolean changed = false;
		PrimitiveIterator.OfLong i = ls.iterator();
		while(i.hasNext()) {
			changed |= add(i.nextLong());
		}
		return changed;
	}

	@Override
	public boolean remove(long l) {
		for (PrimitiveIterator.OfLong it = iterator();it.hasNext();) {
			if (l == it.nextLong()) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeAll(long... ls) {
		boolean modified = false;
		for (int i = 0; i < ls.length; i++) {
			while(remove(ls[i])) {
				modified |= true;
			}
		}
		return modified;
	}

	@Override
	public boolean removeAll(LongCollection ls) {
		boolean modified = false;
		PrimitiveIterator.OfLong i = ls.iterator();
		while(i.hasNext()) {
			long l = i.nextLong();
			while(remove(l)) {
				modified |= true;
			}
		}
		return modified;
	}

	@Override
	public boolean retainAll(long... ls) {
		boolean modified = false;
		for(int i = 0;i < ls.length;i++) {
			if(!contains(ls[i])) {
				remove(ls[i]);
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public boolean retainAll(LongCollection ls) {
		boolean modified = false;
		for(PrimitiveIterator.OfLong i = iterator();i.hasNext();) {
			if(!ls.contains(i.nextLong())) {
				i.remove();
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "[", "]");
		forEach(l -> sj.add(Long.toString(l)));
		return sj.toString();
	}
}
