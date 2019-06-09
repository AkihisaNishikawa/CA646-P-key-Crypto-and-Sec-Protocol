package test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*

*/
public class A3_P11 {

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
	 * From Assignment 1
	 */
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

	public static List<Integer> pair(int d) {
		List<Integer> elements = new ArrayList<Integer>();
		int p;
		for (;;) {
			Random rand = new Random();
			int min = (int) Math.pow(10, d - 1);
			int max = (int) Math.pow(10, d) - 1;
			p = rand.nextInt(max - min + 1) + min;

			BigInteger prime = BigInteger.valueOf(p);

			if (prime.isProbablePrime(20)) {
				break;
			}
		}
		elements.add(p);
		elements.add(findg(p, factors(p - 1)));
		return elements;
	}

	public static int log(int p, int a, int x) {
		// initialize the variable
		int ans = 0;
		/*
		 * for(int i = 0;;i++){ if(Math.pow(a, i) % p == x){ ans = i; break; }
		 * 
		 * }
		 */
		ans = (int) (Math.pow(a, x) % p);
		 
		return ans;
	}

	public static void main(String[] args) {
		int p = 7;
		int a = findg(7, factors(6));
		int x = 5;
		System.out.println(log(p, a, x));
	}
}
