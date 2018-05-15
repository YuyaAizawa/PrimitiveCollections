package com.lethe_river.util.primitive;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface ByteIterator extends PrimitiveIterator<Byte, ByteConsumer> {

	byte nextByte();

	default int nextSigned() {
		return nextByte();
	}

	default int nextUnsigned() {
		return Byte.toUnsignedInt(nextByte());
	}

	@Override
	default void forEachRemaining(ByteConsumer action) {
		Objects.requireNonNull(action);
		while (hasNext())
			action.accept(nextByte());
	}

	@Override
	default Byte next() {
		return nextByte();
	}

	@Override
	default void forEachRemaining(Consumer<? super Byte> action) {
		if(action instanceof ByteConsumer) {
			forEachRemaining((ByteConsumer) action);
		} else {
			forEachRemaining((ByteConsumer) action::accept);
		}
	}
}
