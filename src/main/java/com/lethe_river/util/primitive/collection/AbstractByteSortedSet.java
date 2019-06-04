package com.lethe_river.util.primitive.collection;

import java.util.SortedSet;

public abstract class AbstractByteSortedSet extends AbstractByteSet implements ByteSortedSet {

	@Override
	public SortedSet<Byte> boxedView() {
		return PrimitiveSupport.boxed(this);
	}

	@Override
	public ByteSortedSet headSet(byte to) {
		return subSet(Byte.MIN_VALUE, to);
	}

	@Override
	public ByteSortedSet tailSet(byte from) {
		return subSet(from, Byte.MAX_VALUE);
	}
}
