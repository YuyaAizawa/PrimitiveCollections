package com.lethe_river.util.primitive.function;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.lethe_river.util.primitive.collection.IntIntCursor;

public interface IntIntBiFunction<R> extends BiFunction<Integer, Integer, R>, Function<IntIntCursor, R>{

	R apply(int i, int j);

	@Override
	default R apply(Integer t, Integer u) {
		return apply(t.intValue(), u.intValue());
	}

	@Override
	default R apply(IntIntCursor cursor) {
		return apply(cursor.key(), cursor.value());
	}

	@Override
	default <V> IntIntBiFunction<V> andThen(Function<? super R, ? extends V> after) {
		return (int i, int j) -> after.apply(apply(i, j));
	}
}
