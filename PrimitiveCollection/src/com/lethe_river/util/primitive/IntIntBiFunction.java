package com.lethe_river.util.primitive;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface IntIntBiFunction<R> extends BiFunction<Integer, Integer, R>, Function<IntIntEntry, R> {
	
	R apply(int i, int j);
	
	@Override
	default R apply(Integer t, Integer u) {
		return apply((int)t, (int)u);
	}
	
	@Override
	default R apply(IntIntEntry t) {
		return apply(t.getKeyInt(), t.getValueInt());
	}
	
	@Override
	default <V> IntIntBiFunction<V> andThen(Function<? super R, ? extends V> after) {
		return (int i, int j) -> after.apply(apply(i, j));
	}
}
