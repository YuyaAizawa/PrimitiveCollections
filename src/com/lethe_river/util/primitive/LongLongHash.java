package com.lethe_river.util.primitive;

/**
 * long値からlong値のハッシュを生成する方法.
 * @author YuyaAizawa
 *
 */
@FunctionalInterface
public interface LongLongHash {
	public long hash(long i);
}
