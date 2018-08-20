package com.lethe_river.util.primitive;

import java.util.StringJoiner;

/**
 * CharCollectionのスケルトン実装
 * @author YuyaAizawa
 *
 */
public abstract class AbstractCharCollection implements CharCollection {
	protected AbstractCharCollection() {

	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public abstract int size();

	@Override
	public void clear() {
		for(CharIterator i = iterator();i.hasNext();) {
			i.nextChar();
			i.remove();
		}
	}

	@Override
	public char[] toArray() {
		char[] chs = new char[size()];
		int i = 0;
		for (CharIterator it = iterator();it.hasNext();) {
			chs[i++] = it.nextChar();
		}
		return chs;
	}

	@Override
	public abstract CharIterator iterator();

	@Override
	public boolean contains(char ch) {
		for (CharIterator it = iterator();it.hasNext();) {
			if (ch == it.nextChar()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(char... chs) {
		boolean f = false;
		for (int i = 0; i < chs.length; i++) {
			f &= contains(chs[i]);
		}
		return f;
	}

	@Override
	public boolean containsAll(CharCollection chs) {
		boolean f = false;
		for (CharIterator it = chs.iterator();it.hasNext();) {
			f &= contains(it.nextChar());
		}
		return f;
	}

	@Override
	public boolean add(char ch) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(char... chs) {
		return addAll(chs, 0, chs.length);
	}

	@Override
	public boolean addAll(char chs[], int offset, int length) {
		PrimitiveSupport.checkBounds(chs, offset, length);

		boolean changed = false;
		for (int i = 0; i < length; i++) {
			changed |= add(chs[offset + i]);
		}
		return changed;
	}

	@Override
	public boolean addAll(CharCollection chs) {
		boolean changed = false;

		CharIterator i = chs.iterator();
		while(i.hasNext()) {
			changed |= add(i.nextChar());

		}
		return changed;
	}

	@Override
	public boolean remove(char ch) {
		for (CharIterator it = iterator();it.hasNext();) {
			if (ch == it.nextChar()) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeAll(char... chs) {
		boolean modified = false;
		for (int i = 0; i < chs.length; i++) {
			while(remove(chs[i])) {
				modified |= true;
			}
		}
		return modified;
	}

	@Override
	public boolean removeAll(CharCollection chs) {
		boolean modified = false;
		CharIterator i = chs.iterator();
		while(i.hasNext()) {
			char j = i.nextChar();
			while(remove(j)) {
				modified |= true;
			}
		}
		return modified;
	}

	@Override
	public boolean retainAll(char... chs) {
		boolean modified = false;
		for(int j = 0;j < chs.length;j++) {
			if(!contains(chs[j])) {
				remove(chs[j]);
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public boolean retainAll(CharCollection chs) {
		boolean modified = false;
		for(CharIterator i = iterator();i.hasNext();) {
			if(!chs.contains(i.nextChar())) {
				i.remove();
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "[", "]");
		forEach(i -> sj.add(Character.toString(i)));
		return sj.toString();
	}
}

