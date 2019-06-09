package test;

import java.util.ArrayList;
import java.util.List;

/*

*/
public class A3_P9 {

    public static int order(int p, int[] f, int a) {
        int t = p - 1;
        for (int i = 0; i < f.length; i++) {
            int t2 = t / f[i];

            double expm = Math.pow((double) a, (double) t2) %p;
            if (expm == 1) {
                t = t2;
            }
        }
        return t;
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

	public static int findg(int p, List<Integer> list) {
		int a;
		int phi = p-1;
		int counter = 1;
		int[] f = new int[list.size()];
		
		 for (int i = 0 ; i < list.size() ; i++){
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

	public static void main(String[] args) {
		int p = 17;
		// BigInteger a = new BigInteger("9");
		System.out.println(findg(p, factors(p-1)));
	}
}
