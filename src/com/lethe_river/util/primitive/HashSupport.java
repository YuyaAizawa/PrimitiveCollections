package com.lethe_river.util.primitive;

/**
 * ハッシュを利用するときに使うユーティリティ
 * ハッシュ関数は以下のサイトを参照した．
 * https://gist.github.com/badboy/6267743
 * @author YuyaAizawa
 *
 */
public class HashSupport {

	/**
	 * 素数長配列用に入力より大きい素数を返す.
	 * 2^nの次の素数のうち入力より大きいものを返す．
	 * より大きいものがなければIllegalArgumentExceptionを返す．
	 * @param i
	 * @return 入力より大きい素数
	 */
	public static int nextArrayPrime(int i) {
		for (int j = 0; j < primes1.length; j++) {
			if(primes1[j] > i) {
				return primes1[j];
			}
		}
		throw new IllegalArgumentException();
	}

	/**
	 * 素数長配列用に入力より大きい素数を返す.
	 * sqrt(2^n)の次の素数のうち入力より大きいものを返す．
	 * より大きいものがなければIllegalArgumentExceptionを返す．
	 * @param i
	 * @return 入力より大きい素数
	 */
	public static int nextArrayPrime2(int i) {
		for (int j = 0; j < primes1.length; j++) {
			if(primes2[j] > i) {
				return primes2[j];
			}
			if(primes1[j] > i) {
				return primes1[j];
			}
		}
		throw new IllegalArgumentException();
	}

	private static final int pc2357[] = {6, 2, 4, 2, 4, 6, 4, 2};
	/**
	 * 入力より大きい擬似素数を返す.
	 * 返される値は入力より大きい最小の素数以下の数であり，2, 3, 5, 7で割り切れない．
	 * @param i 正数
	 * @return 入力より大きい擬似素数
	 */
	public static int avoid2357(int i) {
		if(i <= 7) {
			if(i < 3) {
				return 2;
			}
			return i + (i % 2 == 0?1:0);
		}
		if(i % 2 == 0) {
			i+=1;
		}
		int r3 = i % 3;
		if(r3 == 0) {
			i+=2;
			r3 = 2;
		}
		int r5 = i % 5;
		if(r5 == 0) {
			if(r3 == 1) {
				i+=4;
				r3 = 2;
				r5 = 4;
			} else {
				i+=2;
				r3 = 1;
				r5 = 2;
			}
		}
		if(i % 7 == 0) {
			i += pc2357[((r5-1)<<1)+r3-1];
		}
		return i;
	}

	public static IntIntHash hash32shift() {
		return new IntIntHash() {
			@Override
			public int hash(int i) {
				i = ~i + (i << 15); // key = (key << 15) - key - 1;
				i = i ^ (i >>> 12);
				i = i + (i << 2);
				i = i ^ (i >>> 4);
				i = i * 2057; // key = (key + (key << 3)) + (key << 11);
				i = i ^ (i >>> 16);
				return i;
			}
		};
	}

	/**
	 *
	 * @param salt 素数若しくは奇数
	 * @return
	 */

	public static IntIntHash hash32shiftmult(int salt) {
		return new IntIntHash() {
			@Override
			public int hash(int i) {
				i = (i ^ 61) ^ (i >>> 16);
				i += i << 3;
				i ^= i >>> 4;
				i *= salt;
				i ^= i >>> 15;
				return i;
			}
		};
	}
	public static IntIntHash hash32shiftmult() {
		return hash32shiftmult(0x27d4eb2d);
	}

	public static IntIntHash robertJenkins() {
		return new IntIntHash() {
			@Override
			public int hash(int i) {
				i = (i+0x7ed55d16) + (i<<12);
				i = (i^0xc761c23c) ^ (i>>>19);
				i = (i+0x165667b1) + (i<<5);
				i = (i+0xd3a2646c) ^ (i<<9);
				i = (i+0xfd7046c5) + (i<<3);
				i = (i^0xb55a4f09) ^ (i>>>16);
				return i;
			}
		};
	}

	/*
	 *  Thomas Muellerの成果
	 *  入力1bitの差が出力1bitを変化させる可能性が等しいらしい
	 */
	public static IntIntHash thomasMueller() {
		return new IntIntHash() {
			@Override
			public int hash(int x) {
				x = ((x >>> 16) ^ x) * 0x45d9f3b;
				x = ((x >>> 16) ^ x) * 0x45d9f3b;
				x = (x >>> 16) ^ x;
				return x;
			}
		};
	}

	private static final int[] primes1 = {
			8 + 3,
			16 + 1,
			32 + 5,
			64 + 3,
			128 + 3,
			256 + 1,
			512 + 9,
			1024 + 7,
			2048 + 5,
			4096 + 3,
			8192 + 17,
			16384 + 27,
			32768 + 3,
			65536 + 1,
			131072 + 29,
			262144 + 3,
			524288 + 21,
			1048576 + 7,
			2097152 + 17,
			4194304 + 15,
			8388608 + 9,
			16777216 + 43,
			33554432 + 35,
			67108864 + 15,
			134217728 + 29,
			268435456 + 3,
			536870912 + 11,
			1073741824 + 3
	};

//	public static void main(String[] args) {
//		int limit = Integer.MAX_VALUE >> 1;
//		for(int i = 1;i < limit;i<<=1) {
//			int j = (int)(i*Math.sqrt(2));
//			int q = BigInteger.valueOf(j).nextProbablePrime().intValue();
//			System.out.printf("%d + %d,%n",j,q-(j));
//
//			int p = BigInteger.valueOf(i<<1).nextProbablePrime().intValue();
//			System.out.printf("%d + %d,%n",i<<1,p-(i<<1));
//		}
//	}

	private static final int[] primes2 = {
			5 + 2,
			11 + 2,
			22 + 1,
			45 + 2,
			90 + 7,
			181 + 10,
			362 + 5,
			724 + 3,
			1448 + 3,
			2896 + 1,
			5792 + 9,
			11585 + 2,
			23170 + 3,
			46340 + 9,
			92681 + 2,
			185363 + 6,
			370727 + 32,
			741455 + 2,
			1482910 + 9,
			2965820 + 27,
			5931641 + 8,
			11863283 + 6,
			23726566 + 3,
			47453132 + 17,
			94906265 + 32,
			189812531 + 2,
			379625062 + 21,
			759250124 + 9
	};
}
