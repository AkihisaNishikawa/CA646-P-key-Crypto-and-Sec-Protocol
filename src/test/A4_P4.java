package test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class A4_P4 {

	public static BigInteger gcd(BigInteger a, BigInteger b) {
		if (a.compareTo(b) < 0) {
			return gcd(b, a);
		} else if (a.mod(b).equals(BigInteger.ZERO)) {
			return b;
		} else {
			return gcd(b, a.mod(b));
		}
	}

	public static List<Integer> factors(int x) {
		List<Integer> factors = new ArrayList<Integer>();
		for (int i = 2; i <= x; i++) {
			while (x % i == 0) {
				factors.add(i);
				x /= i;
			}
		}
		return factors;

	}

	public static int order(int p, int[] f, int a) {
		int t = p - 1;
		for (int i = 0; i < f.length; i++) {
			int t2 = t / f[i];

			double expm = Math.pow((double) a, (double) t2) % p;
			if (expm == 1) {
				t = t2;
			}
		}
		return t;
	}

	public static int findg(int p, List<Integer> list) {
		int a;
		int phi = p - 1;
		int counter = 1;
		int[] f = new int[list.size()];

		for (int i = 0; i < list.size(); i++) {
			f[i] = list.get(i);
		}

		for (;;) {
			if (phi == order(p, f, counter)) {
				a = counter;
				break;
			}
			counter++;
		}

		return a;
	}

	public static BigInteger primeGenerator(int d) {
		int p;
		BigInteger prime = BigInteger.ZERO;
		for (;;) {
			Random rand = new Random();
			int min = (int) Math.pow(10, d - 1);
			int max = (int) Math.pow(10, d) - 1;
			p = rand.nextInt(max - min + 1) + min;

			prime = BigInteger.valueOf(p);

			if (prime.isProbablePrime(20)) {
				break;
			}
		}
		System.out.println("Random Prime is " + prime);
		return prime;
	}

	public static List<BigInteger> egKey(BigInteger s) {
		List<BigInteger> keys = new ArrayList<>();
		BigInteger p, a, x, y;

		// s bits chosen it can be changed
		p = primeGenerator(s.intValue());

		// Generator
		int p1 = p.intValue();
		a = BigInteger.valueOf(findg(p1, factors(p1 - 1)));

		// Private Key
		Random r = new Random();
		x = BigInteger.valueOf((long) r.nextInt(p1 - 2) + 1);

		// Public Key
		y = a.modPow(x, p);

		keys.add(p);
		keys.add(a);
		keys.add(x);
		keys.add(y);
		return keys;
	}

	public static List<BigInteger> egSign(BigInteger p, BigInteger a, BigInteger x, BigInteger m) {
		BigInteger k, inv, r, s;
		List<BigInteger> pair = new ArrayList<>();

		int p1 = p.intValue();
		Random rand = new Random();
		k = BigInteger.valueOf((long) rand.nextInt(p1 - 2) + 1);

		while (!gcd(k, p.subtract(BigInteger.ONE)).equals(BigInteger.ONE)) {
			k = BigInteger.valueOf((long) rand.nextInt(p1 - 2) + 1);
		}
		inv = k.modInverse(p.subtract(BigInteger.ONE));
		r = a.modPow(k, p);

		s = inv.multiply(m.subtract(x.multiply(r))).mod(p.subtract(BigInteger.ONE));

		pair.add(r);
		pair.add(s);
		return pair;
	}

	public static Boolean egValid(List<BigInteger> key, List<BigInteger> pair, BigInteger m) {
		BigInteger p = key.get(0), r = pair.get(0);
		if (0 >= r.compareTo(BigInteger.ONE) || 0 <= r.compareTo(key.get(0).subtract(BigInteger.ONE))) {
			return false;

		}
		BigInteger v1, v2;
		
		v1 = key.get(3).modPow(r, p).multiply(r.modPow(pair.get(1), p)).mod(p);
		v2 = key.get(1).modPow(m, p);

		if (v1.equals(v2)) {
			return true;
		}
		
		return false;
	}

	public static void main(String[] args) {
		List<BigInteger> pair, key = egKey(BigInteger.valueOf(3));
		BigInteger m = new BigInteger("12");
		pair = egSign(key.get(0), key.get(1), key.get(2), m);
		System.out.println("[p,a,x,y] = "+key);
		System.out.println("[r,s] = "+pair);
		System.out.println(egValid(key, pair, m));
	}

}
