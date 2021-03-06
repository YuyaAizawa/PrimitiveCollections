package com.lethe_river.util.primitive.collection;

import java.util.SortedSet;

public abstract class AbstractIntSortedSet extends AbstractIntSet implements IntSortedSet {

	@Override
	public SortedSet<Integer> boxedView() {
		return PrimitiveSupport.boxed(this);
	}

	@Override
	public IntSortedSet headSet(int to) {
		return subSet(Integer.MIN_VALUE, to);
	}

	@Override
	public IntSortedSet tailSet(int from) {
		return subSet(from, Integer.MAX_VALUE);
	}
}
