package com.lethe_river.util.primitive.collection;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.StringJoiner;

/**
 * IntSetのスケルトン
 * @author YuyaAizawa
 *
 */
public abstract class AbstractIntSet extends AbstractIntCollection implements IntSet {

	/**
	 * 基底のコンストラクタ
	 */
	protected AbstractIntSet() {}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof IntSet)) {
			return false;
		}
		IntSet set = (IntSet) obj;
		if(set.size() != this.size()) {
			return false;
		}
		return containsAll(set);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for(PrimitiveIterator.OfInt i = iterator();i.hasNext();) {
			hash += i.nextInt();
		}
		return hash;
	}

	@Override
	public boolean removeAll(IntCollection is) {
		Objects.requireNonNull(is);

		boolean modified = false;
		if (size() > is.size()) {
			for (PrimitiveIterator.OfInt i = is.iterator(); i.hasNext(); )
				modified |= remove(i.nextInt());
		} else {
			for (PrimitiveIterator.OfInt i = iterator(); i.hasNext(); ) {
				if (is.contains(i.nextInt())) {
					i.remove();
					modified = true;
				}
			}
		}
		return modified;
	}

	@Override
	public boolean removeAll(int... is) {
		Objects.requireNonNull(is);

		boolean modified = false;
		if (size() > is.length) {
			for (int i = 0;i < is.length;i++)
				modified |= remove(is[i]);
		} else {
			for (PrimitiveIterator.OfInt i = iterator(); i.hasNext(); ) {
				if (PrimitiveSupport.linearSearchFirst(is, i.nextInt()) != -1) {
					i.remove();
					modified = true;
				}
			}
		}
		return modified;
	}

	@Override
	public boolean containsAll(IntSet is) {
		Objects.requireNonNull(is);
		if(is == this) { return true; }
		if(is.size() > this.size()) { return false; }
		return super.containsAll(is);
	}

	@Override
	public Set<Integer> boxedView() {
		return PrimitiveSupport.boxed(this);
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "{", "}");
		forEach(i -> sj.add(Integer.toString(i)));
		return super.toString();
	}
}
