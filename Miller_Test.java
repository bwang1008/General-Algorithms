import java.math.BigInteger;
import java.util.Scanner;
public class Miller_Test {
	
	public static int[] bases = {2, 3, 5, 7};

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int count = 0;
		for(int i = 2; i <= 100000000; i++) {
			if(i % 10000000 == 0) System.out.println("i = " + i);
			boolean b = isPrime(i);
			if(b) count++;
			BigInteger big = BigInteger.valueOf(i);
			if(b && !big.isProbablePrime(20))
				System.out.println("HA " + i);
//			if(b) System.out.println(count + " : " + i);
		}
		
		System.out.println("there are " + count + " primes");
		in.close();
	}
	
	public static boolean isPrime(int N) {
		if(N == 2 || N == 3 || N == 5 || N == 7) return true;
		if((N & 1) == 0) return false;
		
		int d = N-1;
		int s = 0;
		while((d & 1) == 0) {
			s++;
			d >>= 1;
		}
		
		for(int a : bases) {
			boolean isComposite = true;
			int temp = modPow(a, d, N);
			if(temp == 1) {
				isComposite = false;
				continue;
			}
			
			long temp2 = temp;
			for(int r = 0; r < s; r++) {
				if(temp2 == N-1) {
					isComposite = false;
					break;
				}
				temp2 = temp2*temp2 % N;
			}
			
			if(isComposite)
				return false;
		}
		
		return true;
	}
	
	public static int modPow(int base, int exp, int mod) {
		String g = Integer.toBinaryString(exp);
		int N = g.length();
		
		long a = base;
		long[] pow = new long[N];
		pow[pow.length-1] = a;
		for(int i = N-2; i >= 0; i--)
			pow[i] = pow[i+1]*pow[i+1] % mod;
		
		long ans = 1;
		for(int i = 0; i < N; i++)
			if(g.charAt(i) == '1')
				ans = ans*pow[i] % mod;
		
	//	System.out.println(base + " ^ " + exp + " mod " + mod + " = " + ans);
		
		return (int) ans;
	}

}
