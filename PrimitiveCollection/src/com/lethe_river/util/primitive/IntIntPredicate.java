package com.lethe_river.util.primitive;

import java.util.function.Predicate;

public interface IntIntPredicate extends Predicate<IntIntEntry> {
	boolean test(int i, int j);
	
	@Override
	default boolean test(IntIntEntry t) {
		return test(t.getKeyInt(), t.getValueInt());
	}
}
