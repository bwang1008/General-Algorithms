public class ModularInverse {

	public static void main(String[] args) {
		int p = 101;
		int[] result = allModInverses(p);
		for(int i = 0; i < result.length; i++) {
			System.out.println(i + " * " + result[i] + " = " + (i * result[i] % p));
		}
	}
	
/*	public static int chooseMod(int n, int r, int p) {
		if(r == 0 || r == n) return 1;
		int[] factorial = new int[n+1];
		factorial[0] = 1;
		for(int i = 1; i < n+1; i++) factorial[i] = (factorial[i-1] * i) % p;
		int[] temp = allModInverses(p);
		int[] inv = new int[n+1];
		for(int i = 1; i < n+1; i++) {
			inv[i] = temp[i % p];
		}
		
		
	} */
	
	// Calculuates all modular inverses, p must be prime!
	public static int[] allModInverses(int p) {
		int[] result = new int[p];
		result[1] = 1;
		for(int i = 2; i < p; i++) {
			int q = p / i;
			int r = p % i;
			result[i] = -q * result[r] % p;
			if(result[i] < 0) result[i] += p;
		}
		return result;
	}
	
	//Calculates an integer a such that a*k (mod p) == 1
	// Pre: p must be prime!
	public static int modInverse(int x, int p) {
		return extendedEuclidean(x, p)[1];
	}
	
	//[gcd, a, b] such that a*x + b*y = gcd
	public static int[] extendedEuclidean(int x, int y) {
		int[] a = {x, 1, 0};
		int[] b = {y, 0, 1};
		while(b[0] != 0) {
			int[] c = new int[3];
			int q = a[0] / b[0];
			c[0] = a[0] - q * b[0];
			c[1] = a[1] - q * b[1];
			c[2] = a[2] - q * b[2];
			a = b; b = c;
		}
		return a;
	}

}
