package com.lethe_river.util.primitive;

import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * ByteSetのスケルトン
 * @author YuyaAizawa
 *
 */
public abstract class AbstractByteSet extends AbstractByteCollection implements ByteSet {

	/**
	 * 基底のコンストラクタ
	 */
	protected AbstractByteSet() {}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof ByteSet)) {
			return false;
		}
		ByteSet set = (ByteSet) obj;
		if(set.size() != this.size()) {
			return false;
		}
		return containsAll(set);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for(ByteIterator i = iterator();i.hasNext();) {
			hash += i.nextByte();
		}
		return hash;
	}

	@Override
	public boolean removeAll(ByteCollection bs) {
		Objects.requireNonNull(bs);

		boolean modified = false;
		if (size() > bs.size()) {
			for (ByteIterator i = bs.iterator(); i.hasNext(); )
				modified |= remove(i.nextByte());
		} else {
			for (ByteIterator i = iterator(); i.hasNext(); ) {
				if (bs.contains(i.nextByte())) {
					i.remove();
					modified = true;
				}
			}
		}
		return modified;
	}

	@Override
	public boolean removeAll(byte... bs) {
		Objects.requireNonNull(bs);

		boolean modified = false;
		if (size() > bs.length) {
			for (int i = 0;i < bs.length;i++)
				modified |= remove(bs[i]);
		} else {
			for (ByteIterator i = iterator(); i.hasNext(); ) {
				if (PrimitiveSupport.linearSearchFirst(bs, i.nextByte()) != -1) {
					i.remove();
					modified = true;
				}
			}
		}
		return modified;
	}

	@Override
	public boolean containsAll(ByteSet bs) {
		Objects.requireNonNull(bs);
		if(bs == this) { return true; }
		if(bs.size() > this.size()) { return false; }
		return super.containsAll(bs);
	}

	@Override
	public Set<Byte> boxedView() {
		return PrimitiveSupport.boxed(this);
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "{", "}");
		forEach(i -> sj.add(Byte.toString(i)));
		return super.toString();
	}
}
