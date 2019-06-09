package test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;


public class A3_P8 {

	private final static BigInteger BIG_TWO = new BigInteger("2");
	private final static SecureRandom SECURE_RANDOM = new SecureRandom();

	private static BigInteger pollardRho(BigInteger pq) {

		BigInteger c = new BigInteger(pq.bitLength(), SECURE_RANDOM);
		BigInteger x = new BigInteger(pq.bitLength(), SECURE_RANDOM);
		BigInteger xx = x;

		BigInteger divisor;

		if (pq.mod(BIG_TWO).compareTo(BigInteger.ZERO) == 0) {
			return BIG_TWO;
		}

		do {
			x = x.multiply(x).mod(pq).add(c).mod(pq);
			xx = xx.multiply(xx).mod(pq).add(c).mod(pq);
			xx = xx.multiply(xx).mod(pq).add(c).mod(pq);
			divisor = x.subtract(xx).gcd(pq);
		} while ((divisor.compareTo(BigInteger.ONE)) == 0);

		return divisor;
	}

	private static List<BigInteger> factors = new ArrayList<>();

	public static List<BigInteger> efactors(BigInteger pq) {
		if (pq.compareTo(BigInteger.ONE) == 0) {
			System.exit(1);
		} else if (pq.isProbablePrime(20)) {
			factors.add(pq);
		} else {
			BigInteger big = pollardRho(pq);
			efactors(big);
			efactors(pq.divide(big));
		}
		return factors;
	}

	public static BigInteger expm(BigInteger p, BigInteger a, BigInteger k) {
		BigInteger ans;
		ans = a.modPow(k, p);
		return ans;
	}

	public static BigInteger order(BigInteger p, List<BigInteger> f, BigInteger a) {
		BigInteger t = p.subtract(BigInteger.ONE);
	//	List<BigInteger> ord = new ArrayList<>();
		for (int i = 0; i < f.size(); i++) {
			BigInteger t2 = t.divide(f.get(i));

			if (expm(p, a, t2).equals(BigInteger.ONE)) {
				t = t2;
			}
		}

		return t;
	}

	public static void main(String[] args) {
		BigInteger p = new BigInteger("17");
		BigInteger a = new BigInteger("9");
		System.out.println("Order is " + order(p, efactors(p.subtract(BigInteger.ONE)), a));
	}
}
