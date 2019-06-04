package com.lethe_river.util.primitive.collection;

import java.util.StringJoiner;

/**
 * ByteCollectionのスケルトン実装
 * @author YuyaAizawa
 *
 */
public abstract class AbstractByteCollection implements ByteCollection {
	protected AbstractByteCollection() {

	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public abstract int size();

	@Override
	public void clear() {
		for(ByteIterator i = iterator();i.hasNext();) {
			i.nextByte();
			i.remove();
		}
	}

	@Override
	public byte[] toArray() {
		byte[] bs = new byte[size()];
		int i = 0;
		for (ByteIterator it = iterator();it.hasNext();) {
			bs[i++] = it.nextByte();
		}
		return bs;
	}

	@Override
	public abstract ByteIterator iterator();

	@Override
	public boolean contains(byte i) {
		for (ByteIterator it = iterator();it.hasNext();) {
			if (i == it.nextByte()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(byte... bs) {
		boolean f = false;
		for (int i = 0; i < bs.length; i++) {
			f &= contains(bs[i]);
		}
		return f;
	}

	@Override
	public boolean containsAll(ByteCollection bs) {
		boolean f = false;
		for (ByteIterator it = bs.iterator();it.hasNext();) {
			f &= contains(it.nextByte());
		}
		return f;
	}

	@Override
	public boolean add(byte i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(byte... bs) {
		return addAll(bs, 0, bs.length);
	}

	@Override
	public boolean addAll(byte bs[], int offset, int length) {
		PrimitiveSupport.checkBounds(bs, offset, length);

		boolean changed = false;
		for (int i = 0; i < length; i++) {
			changed |= add(bs[offset + i]);
		}
		return changed;
	}

	@Override
	public boolean addAll(ByteCollection is) {
		if(is.isEmpty()) {
			return false;
		}

		boolean changed = false;

		ByteIterator i = is.iterator();
		while(i.hasNext()) {
			changed |= add(i.nextByte());

		}
		return changed;
	}

	@Override
	public boolean remove(byte i) {
		for (ByteIterator it = iterator();it.hasNext();) {
			if (i == it.nextByte()) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeAll(byte... bs) {
		boolean modified = false;
		for (int i = 0; i < bs.length; i++) {
			while(remove(bs[i])) {
				modified |= true;
			}
		}
		return modified;
	}

	@Override
	public boolean removeAll(ByteCollection bs) {
		boolean modified = false;
		ByteIterator i = bs.iterator();
		while(i.hasNext()) {
			byte j = i.nextByte();
			while(remove(j)) {
				modified |= true;
			}
		}
		return modified;
	}

	@Override
	public boolean retainAll(byte... bs) {
		boolean modified = false;
		for(int j = 0;j < bs.length;j++) {
			if(!contains(bs[j])) {
				remove(bs[j]);
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public boolean retainAll(ByteCollection is) {
		boolean modified = false;
		for(ByteIterator i = iterator();i.hasNext();) {
			if(!is.contains(i.nextByte())) {
				i.remove();
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "[", "]");
		forEach(i -> sj.add(Byte.toString(i)));
		return sj.toString();
	}
}
