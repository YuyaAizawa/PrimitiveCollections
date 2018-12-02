package com.lethe_river.util.primitive.function;

import java.util.function.Predicate;

import com.lethe_river.util.primitive.collection.IntIntCursor;

public interface IntIntPredicate extends Predicate<IntIntCursor> {
	boolean test(int i, int j);

	@Override
	default boolean test(IntIntCursor t) {
		return test(t.key(), t.value());
	}
}
