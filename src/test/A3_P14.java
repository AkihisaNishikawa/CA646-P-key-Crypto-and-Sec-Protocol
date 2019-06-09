package test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*

*/
public class A3_P14 {

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

	/*
	 * @parameter list should be list of factors of p-1
	 */
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
		y = BigInteger.valueOf((long) r.nextInt(p1 - 2) + 1);

		// Public Key
		x = a.modPow(a, p);

		keys.add(p);
		keys.add(a);
		keys.add(x);
		keys.add(y);
		return keys;
	}

	public static List<BigInteger> egEnc(BigInteger p, BigInteger a, BigInteger y, BigInteger m) {
		List<BigInteger> c = new ArrayList<>();
		BigInteger c1, c2, k;
		// ephemeral key
		Random r = new Random();
		k = BigInteger.valueOf((long) r.nextInt(p.intValue() - 2) + 1);
		// c1
		c1 = a.modPow(k, p);
		// c2
		int k1 = k.intValue();
		c2 = m.multiply(y.pow(k1)).mod(p);

		c.add(c1);
		c.add(c2);

		return c;
	}

	public static BigInteger egDec(BigInteger p, BigInteger x, BigInteger c1, BigInteger c2) {
		BigInteger m, a, c1_dash;

		// generator again
		int p1 = p.intValue();
		a = BigInteger.valueOf(findg(p1, factors(p1 - 1)));

		// c1^(-a) mod p
		c1_dash = c1.modInverse(p).modPow(a, p);

		m = c1_dash.multiply(c2).mod(p);
		return m;
	}

	public static void main(String[] args) {

		// Key Generation
		List<BigInteger> components = egKey(BigInteger.valueOf(5));
		System.out.println("[prime, generator, public, private]");
		System.out.println(components);
		System.out.println();

		// Encryption
		BigInteger p, a, x;
		p = components.get(0);// BigInteger.valueOf(24007);
		a = components.get(1);// BigInteger.valueOf(2);
		x = components.get(2); // BigInteger.valueOf(21339);

		BigInteger m = BigInteger.valueOf(1234);
		List<BigInteger> ciphertexts = egEnc(p, a, x, m);
		System.out.println("[c1, c2]");
		System.out.println(ciphertexts);
		System.out.println();

		// Decryption
		BigInteger y, c1, c2;
		y = components.get(3);
		c1 = ciphertexts.get(0);
		c2 = ciphertexts.get(1);

		System.out.print("m = ");
		System.out.println(egDec(p, y, c1, c2));

	}
}
