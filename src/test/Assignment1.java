package test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/*
 * Java is used for the entire assignmetn
 * Practical 1 -12 is stored in one file
 * I tried my best to make inputs clear
 * 
 */

public class Assignment1 {
	public static int gcd(int a, int b) {
		int gcd = 0;
		for (int i = 1; i <= a && i <= b; ++i) {
			if (a % i == 0 && b % i == 0) {
				gcd = i;
			}
		}
		return gcd;
	}

	public static int[] gcde(int a, int b) {
		int g = gcd(a, b);
		int num[] = { g, 0, 0 };
		int[] x;
		int[] y;
		int i = 0;
		x = new int[10];
		y = new int[10];
		x[i] = 1;
		x[i + 1] = 0;
		y[i] = 0;
		y[i + 1] = 1;

		while (b != 0) {
			int p = a / b;
			int q = a % b;
			x[i + 2] = x[i] - p * x[i + 1];
			y[i + 2] = y[i] - p * y[i + 1];

			a = b;
			b = q;
			x[i] = x[i + 1];
			x[i + 1] = x[i + 2];
			y[i] = y[i + 1];
			y[i + 1] = y[i + 2];

		}
		num[1] = x[i];
		num[2] = y[i];
		return num;

	}

	public static int invm(int a, int m) throws IOException {
		int inverse;
		a = a % m;
		for (inverse = 0; inverse < m; inverse++) {

			if (a * inverse % m == 1) {
				break;
			}
		}
		return inverse;
	}

	public static ArrayList<Integer> divm(int a, int b, int m) {
		ArrayList<Integer> field = new ArrayList<Integer>();
		int inverse;
		b = b % m;
		for (inverse = 0; inverse < m; inverse++) {
			if (b * inverse % m == 1) {
				try {
					field.add(a * inverse % m);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
		return field;
	}

	public static long expm(long m, long a, long k) {
		long res = 1;
		long y = a;
		while (k > 0) {
			if (k % 2 == 1) {
				res = (res * y) % m;
			}
			y = (y * y) % m;
			k /= 2;
		}
		return res % m;
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

	/*
	 * this is different from the way it calculates in the notes
	 * 
	 */
	public static int phi(int n) {
		int phi = 1;
		for (int i = 2; i < n; i++)
			if (gcd(i, n) == 1)
				phi++;
		return phi;
	}

	public static boolean fermat(long x, int t) {

		if (x == 0 || x == 1)
			return false;
		if (x == 2)
			return true;
		if (x % 2 == 0)
			return false;

		Random arbitrary = new Random();

		for (int i = 0; i < t; i++) {
			long a = 2 + Math.abs(arbitrary.nextInt((int) x - 2 + 1));
			/*
			 * only using Java function to generate random large integer
			 * 
			 * Range is 2 =< r =< x-2
			 */
			long r = a % (x - 1) + 1;
			if (expm(x, r, x - 1) != 1)
				return false;
		}
		return true;
	}

	public static int prime(int d) {
		String num1 = "1";
		String num2 = "9";
		int x;
		for (int i = 0; i < d - 1; i++) {
			num1 = num1 + "0";
			num2 = num2 + "9";
		}
		int min = Integer.parseInt(num1);
		int max = Integer.parseInt(num2);

		for (;;) {
			x = (int) ((Math.random() * ((max - min) + 1)) + min);
			if (fermat(x, 10) == true) {
				break;
			}
		}

		return x;
	}

	/*
	 * I tried many ways of solving this question but all of them take huge
	 * amount of time except for this one Reference to the website
	 * https://introcs.cs.princeton.edu/java/99crypto/PollardRho.java.html
	 */

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

	public static void efactors(BigInteger pq) {
		if (pq.compareTo(BigInteger.ONE) == 0) {
			return;
		} else if (pq.isProbablePrime(20)) {
			System.out.print(" " + pq + " ");
			return;
		} else {
			BigInteger big = pollardRho(pq);
			efactors(big);
			efactors(pq.divide(big));
		}
	}

	/*
	 * Using normal int instead of using BigInteger to utilize methods from
	 * earlier
	 */
	public static List<BigInteger> keypair = new ArrayList<BigInteger>();

	public static void rsaKeys(int s) {

		// Generate two large prime
		BigInteger p = BigInteger.probablePrime(s / 2, SECURE_RANDOM);
		BigInteger q = BigInteger.probablePrime(s / 2, SECURE_RANDOM);

		// Generate phi
		BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

		// Creating key-par
		BigInteger N = p.multiply(q);

		// Generate e
		BigInteger e = new BigInteger(s + 1, SECURE_RANDOM);

		// Generate d
		BigInteger d = null;
		try {
			d = e.modInverse(phi);
		} catch (Exception e1) {
			System.out.println("RSA function: Random e does not have invertible, try it again");
			System.exit(0);
		}
		keypair.add(N);
		keypair.add(e);
		keypair.add(d);
		System.out.println("[N, e, d] = " + keypair);

	}

	// Since integer is too big I used readly made method but
	// essentiallyexpm(n,e,m)
	public static BigInteger rsaEnc(BigInteger n, BigInteger e, BigInteger m) {
		return m.modPow(e, n);
	}

	public static BigInteger rsaDec(BigInteger n, BigInteger d, BigInteger c) {
		return c.modPow(d, n);
	}

	public static String eCrack(BigInteger m, BigInteger e, BigInteger c) {
		BigInteger f1 = BigInteger.valueOf(-1L);
		BigInteger f2 = BigInteger.valueOf(-1L);
		efactors(m);
		f2 = m.divide(f1);
        BigInteger phi = (f1.subtract(BigInteger.ONE)).multiply(f2.subtract(BigInteger.ONE));

		BigInteger key = e.modInverse(phi);
		BigInteger plaintext = rsaDec(m,key,c);
		return plaintext.toString();
	}

	public static void main(String[] args) {
		int a = 12;
		int b = 25;
		int modulo = 19;
		int k = 5; // exponential

		// gcd Practical 1
		System.out.println("gcd =" + gcd(a, b));

		// Extended Euclidean Algorithm
		int gcde[] = gcde(a, b);
		System.out.println("ECDE (gcd solution1 solution2) = (" + gcde[0] + "," + gcde[1] + "," + gcde[2] + ")");

		// Multiplicative inverse
		try {
			System.out.println("Inverse of " + a + " modulo " + modulo + " = " + invm(a, modulo));
		} catch (IOException e) {
			System.out.println("Error occured in Inverse function");
			e.printStackTrace();
		}

		// Dimv
		ArrayList<Integer> field = divm(a, b, modulo);// main part
		System.out.print("field of ab^-1 mod m = ");
		for (int i = 0; i < field.size(); i++) {
			System.out.print(" " + field.get(i) + " ");
		}

		// modular exponentiation
		System.out.println();
		System.out.println("Power Mod " + expm(modulo, a, k));

		// integer factorization
		System.out.println("factors are " + factors(190));

		// Euler Function
		System.out.println("phi(n) is = " + phi(12));
		// Fermat Primality Test
		System.out.println(fermat(173, 9));

		// Generate d-bit prime with Fermat
		System.out.println("d-bit prime is " + prime(5));

		// Given large prime p,q factorization of pq
		BigInteger pq = new BigInteger("709138557871512933443");
		System.out.println("p and q for " + pq + " are");
		efactors(pq);
		System.out.println();
		
		
		// RSA
		BigInteger m = new BigInteger("1052232");
		BigInteger c = new BigInteger("622967");
		BigInteger N = new BigInteger("1530497");
		BigInteger e = new BigInteger("65537");
		BigInteger d = new BigInteger("260153");

		int s = 5; // input
		rsaKeys(s);
		System.out.println("Encrypted message is "+rsaEnc(N, e, m));
		System.out.println("Decrypted meesage is "+rsaDec(N, d, c));

		// RSA crack
		eCrack(N, e, c);
	}

}
