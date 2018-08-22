package com.lethe_river.util.primitive;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface IntIntBiConsumer extends BiConsumer<Integer, Integer> {
	void accept(int i, int j);

	@Override
	default void accept(Integer t, Integer u) {
		accept(t.intValue(), u.intValue());
	}

	@Override
	default IntIntBiConsumer andThen(BiConsumer<? super Integer, ? super Integer> after) {
		if(after instanceof IntIntBiConsumer) {
			return (int i, int j) -> {
				this.accept(i, j);
				((IntIntBiConsumer) after).accept(i, j);
			};
		}
		return (int i, int j) -> {
			this.accept(i, j);
			after.accept(Integer.valueOf(i), Integer.valueOf(j));
		};
	}
}
