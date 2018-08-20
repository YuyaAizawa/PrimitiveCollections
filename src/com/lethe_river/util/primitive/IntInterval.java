package com.lethe_river.util.primitive;

import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * int値の区間を表す．Immutable
 * @author YuyaAizawa
 *
 */
public interface IntInterval {

	/**
	 * 指定されたint値がこの区間に含まれるか判定する．
	 * @param i 判定する値
	 * @return 含まれればtrue
	 */
	boolean contains(int i);

	/**
	 * この区間の大きさを返す．
	 * @return 区間の大きさ
	 */
	int size();

	/**
	 * この区間の下限(この値を含む)を返す．この区間が空の場合emptyを返す．
	 * @return 下限(この値を含む)値またはempty
	 */
	OptionalInt getLowwerBoundInclusive();

	/**
	 * この区間の上限(この値を含む)を返す．この区間が空の場合emptyを返す．
	 * @return 上限(この値を含む)値またはempty
	 */
	OptionalInt getUpperBoundInclusive();

	/**
	 * この区間が空区間かどうか判定する
	 * @return 空区間ならtrue
	 */
	default boolean isEmpty() {
		return !getLowwerBoundInclusive().isPresent() && !getUpperBoundInclusive().isPresent();
	}

	/**
	 * この区間に含まれるint値に関するIntStreamを返す
	 * @return IntStream
	 */
	default IntStream stream() {
		return isEmpty() ? IntStream.empty() : IntStream.rangeClosed(
				getLowwerBoundInclusive().getAsInt(), getUpperBoundInclusive().getAsInt());
	}

	/**
	 * 指定した閉区間を表すインスタンスを返す
	 * @param lowwer 下限
	 * @param upper 上限
	 * @throws IllegalArgumentException lowwer&gt;upperの場合
	 * @return 閉区間
	 */
	static IntInterval closed(int lowwer, int upper) {
		if(lowwer > upper) {
			throw new IllegalArgumentException("lowwer: "+lowwer+", upper: "+upper);
		}
		return new IntIntervalClosed(lowwer, upper);
	}

	/**
	 * 空区間を表すインスタンスを返す．
	 * @return 空区間
	 */
	static IntInterval empty() {
		return EmptyInterval.SINGLETON;
	}
}

class IntIntervalClosed implements IntInterval {
	// 境界値(この値を含む)
	private final int lowwer;
	private final int upper;

	IntIntervalClosed(int lowwer, int upper) {
		this.lowwer = lowwer;
		this.upper = upper;
	}

	@Override
	public boolean contains(int i) {
		return lowwer <= i && i <= upper;
	}

	@Override
	public int size() {
		return upper - lowwer -1;
	}

	@Override
	public OptionalInt getLowwerBoundInclusive() {
		return OptionalInt.of(lowwer);
	}

	@Override
	public OptionalInt getUpperBoundInclusive() {
		return OptionalInt.of(upper);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof IntInterval)) {
			return false;
		}
		IntInterval target = (IntInterval) obj;
		return this.getLowwerBoundInclusive().equals(target.getLowwerBoundInclusive())
				&& this.getUpperBoundInclusive().equals(target.getUpperBoundInclusive());
	}

	@Override
	public int hashCode() {
		return lowwer + upper * 31;
	}
}

enum EmptyInterval implements IntInterval {
	SINGLETON;

	@Override
	public boolean contains(int i) {
		return false;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public OptionalInt getLowwerBoundInclusive() {
		return OptionalInt.empty();
	}

	@Override
	public OptionalInt getUpperBoundInclusive() {
		return OptionalInt.empty();
	}

	@Override
	public boolean isEmpty() {
		return true;
	}
}