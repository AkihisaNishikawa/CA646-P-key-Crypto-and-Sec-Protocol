package test;

/*

*/
public class A4_P1 {

	public static int addSimple(int m, int k, int x) {
		x = (int) ((Math.pow(2, k) - 1) * Math.pow(2,m)  + x);
		return x;
	}

	public static String removeSimple(int m, int k, int x) {
		x = (int) (x - (Math.pow(2, k) - 1) * Math.pow(2,m));
		String padded = String.format("%"+m+"s", Integer.toBinaryString(x)).replace(' ', '0');
		return padded;
	}

	public static Boolean okSimple(int m, int k, int x) {
		if(Integer.toBinaryString((int) Math.pow(2, m) - 1).length() == removeSimple(m, k, x).length()){
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		int m = 4;
		int k = 4;
		int x = 2;
		int x1 = addSimple(m, k, x);
		
		System.out.println(Integer.toBinaryString(x1));
		System.out.println(removeSimple(m,k,x1));
		System.out.println(okSimple(m,k,x1));
	}
}
