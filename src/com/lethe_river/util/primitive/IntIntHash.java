package com.lethe_river.util.primitive;

/**
 * int値からint値のハッシュを生成する方法.
 * @author YuyaAizawa
 *
 */
@FunctionalInterface
public interface IntIntHash {
	public int hash(int i);
}
