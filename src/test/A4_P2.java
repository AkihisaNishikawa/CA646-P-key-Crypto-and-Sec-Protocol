package test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class A4_P2 {

	private final static SecureRandom SECURE_RANDOM = new SecureRandom();

	public static List<BigInteger> rsaKeys(int s) {
		List<BigInteger> keys = new ArrayList<>();
		BigInteger p, q;
		BigInteger phi, N, e, d;
		// Generate two large prime
		p = BigInteger.probablePrime(s / 2, SECURE_RANDOM);
		q = BigInteger.probablePrime(s / 2, SECURE_RANDOM);

		// Generate phi
		phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

		// Creating key-pair
		N = p.multiply(q);

		// Generate e
		e = new BigInteger(s - 1, SECURE_RANDOM);

		while (gcd(e, phi).equals(BigInteger.ONE) == false) {
			e = new BigInteger(s - 1, SECURE_RANDOM);
		}

		// Generate d
		d = null;
		try {
			d = e.modInverse(phi);
		} catch (Exception e1) {
			System.out.println("RSA function: Random e does not have invertible, try it again");
			System.exit(0);
		}
		keys.add(N);
		keys.add(phi);
		keys.add(e);
		keys.add(d);
		System.out.println("[N, phi, e, d] = " + keys);

		return keys;

	}

	public static BigInteger gcd(BigInteger a, BigInteger b) {
		if (a.compareTo(b) < 0) {
			return gcd(b, a);
		} else if (a.mod(b).equals(BigInteger.ZERO)) {
			return b;
		} else {
			return gcd(b, a.mod(b));
		}
	}

	public static BigInteger addSimple(int m, int k, BigInteger x) {
		BigInteger two = new BigInteger("2");
		x = two.pow(k).subtract(BigInteger.ONE).multiply(two.pow(m)).add(x);
		// Math.pow(2, k) - 1) * Math.pow(2,m) + x;
		return x;
	}

	public static String removeSimple(int m, int k, BigInteger x) {
		BigInteger two = new BigInteger("2");
		x = x.subtract(two.pow(k).subtract(BigInteger.ONE).multiply(two.pow(m)));
		// x = (int) (x - (Math.pow(2, k) - 1) * Math.pow(2, m));
		String padded = String.format("%" + m + "s", x.toString(2)).replace(' ', '0');
		return padded;
	}

	public static Boolean okSimple(int m, int k, BigInteger x) {
		if (Integer.toBinaryString((int) Math.pow(2, m) - 1).length() == removeSimple(m, k, x).length()) {
			return true;
		}
		return false;
	}

	public static BigInteger rsaSign(List<BigInteger> keys, int addbits, BigInteger message) {
		int m = message.bitLength();
		BigInteger fixed = addSimple(m, addbits, message);

		System.out.println("(fixed Binary, message length in bit) =  (" + fixed.toString(2) + ", " + m + ")");
		BigInteger signature = fixed.modPow(keys.get(3), keys.get(0));

		return signature;
	}

	public static String rsaRecovery(int mbit, int k, BigInteger signature, List<BigInteger> keys) {
		String message;
		message = removeSimple(mbit, k, signature.modPow(keys.get(2), keys.get(0)));
		return message;
	}

	public static Boolean rsaValid(BigInteger signature, List<BigInteger> keys, int kbit){
		// compare first k-bits are as what function does
		// this is uder the condition that both knows the redundancy fucntion add k one-bit in front
		Character ch = new Character('1');
		for(int i = 0;i < kbit ;i++){
		if(!ch.equals(signature.modPow(keys.get(2), keys.get(0)).toString(2).charAt(i))){
			return false;
		}
		}
		return true; 
	}

	public static void main(String[] args) {
		int primeBit = 20;
		// if its too small, errors come out
		// make prime bit big enough to satisfy 1 =< s < n

		List<BigInteger> keys = new ArrayList<>();
		keys = rsaKeys(primeBit);

		int addbits = 4;
		BigInteger message = new BigInteger("10"); // Smaller than p
		System.out.println("Message: " + message);

		BigInteger signature = rsaSign(keys, addbits, message);
		System.out.println("Signature: " + signature);

		System.out.println("Recovered Message: " + rsaRecovery(message.bitLength(), addbits, signature, keys));

		System.out.println(rsaValid(signature, keys, addbits));
	}
}
