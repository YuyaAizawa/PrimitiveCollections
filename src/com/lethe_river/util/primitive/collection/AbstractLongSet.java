package com.lethe_river.util.primitive.collection;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.StringJoiner;

/**
 * LongSetのスケルトン
 * @author YuyaAizawa
 *
 */
public abstract class AbstractLongSet extends AbstractLongCollection implements LongSet {

	/**
	 * 基底のコンストラクタ
	 */
	protected AbstractLongSet() {}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof LongSet)) {
			return false;
		}
		LongSet set = (LongSet) obj;
		if(set.size() != this.size()) {
			return false;
		}
		return containsAll(set);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for(PrimitiveIterator.OfLong i = iterator();i.hasNext();) {
			hash += i.nextLong();
		}
		return hash;
	}

	@Override
	public boolean removeAll(LongCollection ls) {
		Objects.requireNonNull(ls);

		boolean modified = false;
		if (size() > ls.size()) {
			for (PrimitiveIterator.OfLong i = ls.iterator(); i.hasNext(); )
				modified |= remove(i.nextLong());
		} else {
			for (PrimitiveIterator.OfLong i = iterator(); i.hasNext(); ) {
				if (ls.contains(i.nextLong())) {
					i.remove();
					modified = true;
				}
			}
		}
		return modified;
	}

	@Override
	public boolean removeAll(long... ls) {
		Objects.requireNonNull(ls);

		boolean modified = false;
		if (size() > ls.length) {
			for (int i = 0;i < ls.length;i++)
				modified |= remove(ls[i]);
		} else {
			for (PrimitiveIterator.OfLong i = iterator(); i.hasNext(); ) {
				if (PrimitiveSupport.linearSearchFirst(ls, i.nextLong()) != -1) {
					i.remove();
					modified = true;
				}
			}
		}
		return modified;
	}

	@Override
	public boolean containsAll(LongSet ls) {
		Objects.requireNonNull(ls);
		if(ls == this) { return true; }
		if(ls.size() > this.size()) { return false; }
		return super.containsAll(ls);
	}

	@Override
	public Set<Long> boxedView() {
		return PrimitiveSupport.boxed(this);
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "{", "}");
		forEach(l -> sj.add(Long.toString(l)));
		return super.toString();
	}
}
