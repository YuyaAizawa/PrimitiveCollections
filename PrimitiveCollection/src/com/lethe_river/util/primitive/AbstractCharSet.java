package com.lethe_river.util.primitive;

import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * CharSetのスケルトン
 * @author YuyaAizawa
 *
 */
public abstract class AbstractCharSet extends AbstractCharCollection implements CharSet {

	/**
	 * 基底のコンストラクタ
	 */
	protected AbstractCharSet() {}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof CharSet)) {
			return false;
		}
		CharSet set = (CharSet) obj;
		if(set.size() != this.size()) {
			return false;
		}
		return containsAll(set);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for(CharIterator i = iterator();i.hasNext();) {
			hash += i.nextChar();
		}
		return hash;
	}

	@Override
	public boolean removeAll(CharCollection chs) {
		Objects.requireNonNull(chs);

		boolean modified = false;
		if (size() > chs.size()) {
			for (CharIterator i = chs.iterator(); i.hasNext(); )
				modified |= remove(i.nextChar());
		} else {
			for (CharIterator i = iterator(); i.hasNext(); ) {
				if (chs.contains(i.next())) {
					i.remove();
					modified = true;
				}
			}
		}
		return modified;
	}

	@Override
	public boolean removeAll(char... chs) {
		Objects.requireNonNull(chs);

		boolean modified = false;
		if (size() > chs.length) {
			for (int i = 0;i < chs.length;i++)
				modified |= remove(chs[i]);
		} else {
			for (CharIterator i = iterator(); i.hasNext(); ) {
				if (PrimitiveSupport.linearSearchFirst(chs, i.nextChar()) != -1) {
					i.remove();
					modified = true;
				}
			}
		}
		return modified;
	}

	@Override
	public boolean containsAll(CharSet chs) {
		Objects.requireNonNull(chs);
		if(chs == this) { return true; }
		if(chs.size() > this.size()) { return false; }
		return super.containsAll(chs);
	}

	@Override
	public Set<Character> boxedView() {
		return PrimitiveSupport.boxed(this);
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "{", "}");
		forEach(i -> sj.add(Character.toString(i)));
		return super.toString();
	}
}

