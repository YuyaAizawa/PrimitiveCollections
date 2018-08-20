package com.lethe_river.util.primitive;

import java.util.function.Predicate;

public interface IntIntPredicate extends Predicate<IntIntCursor> {
	boolean test(int i, int j);

	@Override
	default boolean test(IntIntCursor t) {
		return test(t.key(), t.value());
	}
}
