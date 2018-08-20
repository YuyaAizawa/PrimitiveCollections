package com.lethe_river.util.primitive;

import java.util.Objects;

@FunctionalInterface
public interface CharPredicate {
	boolean test(char value);

	default CharPredicate and(CharPredicate other) {
		Objects.requireNonNull(other);
		return (value) -> test(value) && other.test(value);
	}

	default CharPredicate or(CharPredicate other) {
		Objects.requireNonNull(other);
		return (value) -> test(value) || other.test(value);
	}

	default CharPredicate negate() {
		return (value) -> !test(value);
	}
}
