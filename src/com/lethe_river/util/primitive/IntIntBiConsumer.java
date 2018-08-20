package com.lethe_river.util.primitive;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface IntIntBiConsumer extends BiConsumer<Integer, Integer> {
	void accept(int i, int j);

	@Override
	default void accept(Integer t, Integer u) {
		accept((int)t, (int)u);
	}

	@Override
	default IntIntBiConsumer andThen(BiConsumer<? super Integer, ? super Integer> after) {
		return (int i, int j) -> {
			this.accept(i, j);
			after.accept(i, j);
		};
	}
}
