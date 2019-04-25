import java.util.HashMap;

public class FactoringLongs {

	public static HashMap<Long, Integer> factor(long N) {
		HashMap<Long, Integer> map = new HashMap<>();
		
		int count = 0;
		long M = N;
		while(M > 1) {
			M >>= 1;
			count++;
		}
		System.out.println("count = " + count);
		for(int i = 1; i <= 10; i++) {
			int a = 1 << i;
			long b = choose(a, a >> 1, N);
			long gcd = gcd(N, b);
			System.out.println("a = " + a + " b = " + b + " gcd = " + gcd);
		}
		return map;
	}
	
	public static long gcd(long a, long b) { return (b == 0) ? a : gcd(b, a % b);}
	
	public static long choose(int n, int k, long p) {
		long[][] bin = new long[n+1][n+1];
		for(int i = 0; i < bin.length; i++)
			bin[i][0] = bin[i][i] = 1;
		for(int i = 2; i < bin.length; i++)
			for(int j = 1; j < i; j++)
				bin[i][j] = (bin[i-1][j-1] + bin[i-1][j]) % p;
		
		return bin[n][k];
	}
	
	public static void main(String[] args) {
		long N = 65 * 139;
		System.out.println("N = " + N);
		factor(N);
		
	}

}
