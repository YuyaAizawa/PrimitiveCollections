package com.lethe_river.util.primitive;

import java.util.Spliterator;
import java.util.function.Consumer;

public interface ByteSpliterator extends Spliterator.OfPrimitive<Byte, ByteConsumer, ByteSpliterator> {
	@Override
	ByteSpliterator trySplit();

	@Override
	boolean tryAdvance(ByteConsumer action);

	@Override
	default void forEachRemaining(ByteConsumer action) {
		for (;;) {
			tryAdvance(action);
		}
	}

	@Override
	default boolean tryAdvance(Consumer<? super Byte> action) {
		if (action instanceof ByteConsumer) {
			return tryAdvance((ByteConsumer) action);
		}
		return tryAdvance((ByteConsumer) action::accept);
	}

	@Override
	default void forEachRemaining(Consumer<? super Byte> action) {
		if (action instanceof ByteConsumer) {
			forEachRemaining((ByteConsumer) action);
		}
		forEachRemaining((ByteConsumer) action::accept);
	}
}
