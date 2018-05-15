package com.lethe_river.util.primitive;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntConsumer;

/**
 * PrimitiveCollectionのそれぞれの要素に対する操作を提供するインターフェース
 * @author YuyaAizawa
 *
 * @param <T> ラッパー型
 * @param <T_CONS> プリミティブ用Consumerの型
 */
interface PrimitiveIterable<T, T_CONS> {

	PrimitiveIterator<T, T_CONS> iterator();

	/**
	 * 指定した操作をこのPrimitiveCollectionのそれぞれの要素に適用する．要素の順番はiteratorで返される
	 *
	 * @param action 適用する操作
	 * @throws NullPointerException actionがnullの場合
	 */
	void forEach(T_CONS action);
}

interface IntIterable extends PrimitiveIterable<Integer, IntConsumer> {
	@Override
	PrimitiveIterator.OfInt iterator();

	Spliterator.OfInt spliterator();

	@Override
	default void forEach(IntConsumer action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
}

interface ByteIterable extends PrimitiveIterable<Byte, ByteConsumer> {
	@Override
	ByteIterator iterator();

	default PrimitiveIterator.OfInt iteratorSigned() {
		return new PrimitiveIterator.OfInt() {
			final ByteIterator original = iterator();
			@Override
			public boolean hasNext() {
				return original.hasNext();
			}
			@Override
			public int nextInt() {
				return original.nextByte();
			}
			@Override
			public void remove() {
				original.remove();
			}
		};
	}
	default PrimitiveIterator.OfInt iteratorUnsigned() {
		return new PrimitiveIterator.OfInt() {
			final ByteIterator original = iterator();
			@Override
			public boolean hasNext() {
				return original.hasNext();
			}
			@Override
			public int nextInt() {
				return Byte.toUnsignedInt(original.nextByte());
			}
			@Override
			public void remove() {
				original.remove();
			}
		};
	}

	Spliterator.OfInt spliteratorSigned();
	Spliterator.OfInt spliteratorUnsigned();

	@Override
	default void forEach(ByteConsumer action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
}

interface CharIterable extends PrimitiveIterable<Character, CharConsumer> {
	@Override
	CharIterator iterator();

	default PrimitiveIterator.OfInt iteratorOfInt() {
		return new PrimitiveIterator.OfInt() {
			final CharIterator original = iterator();
			@Override
			public boolean hasNext() {
				return original.hasNext();
			}
			@Override
			public int nextInt() {
				return original.nextChar();
			}
			@Override
			public void remove() {
				original.remove();
			}
		};
	}

	Spliterator.OfInt spliterator();

	@Override
	default void forEach(CharConsumer action) {
		Objects.requireNonNull(action);
		iterator().forEachRemaining(action);
	}
}