package com.lethe_river.util.primitive;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface CharIterator extends PrimitiveIterator<Character, CharConsumer>{
	char nextChar();

	@Override
	default void forEachRemaining(CharConsumer action) {
		Objects.requireNonNull(action);
		while (hasNext())
			action.accept(nextChar());
	}

	@Override
	default Character next() {
		return nextChar();
	}

	@Override
	default void forEachRemaining(Consumer<? super Character> action) {
		if(action instanceof CharConsumer) {
			forEachRemaining((CharConsumer) action);
		} else {
			forEachRemaining((CharConsumer) action::accept);
		}
	}
}
