import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.LinkedList;
public class SumAllSubsets {

	//https://www.hackerrank.com/challenges/xoring-ninja/problem?utm_campaign=challenge-recommendation&utm_medium=email&utm_source=24-hour-campaign
	
	public static int p = 1_000_000_007;
	
	public static void main(String[] args) {
		FastScanner in = new FastScanner(System.in);
		int T = in.nextInt();
		while(T --> 0) {
			int N = in.nextInt();
			long OR = 0;
			for(int i = 0; i < N; i++)
				OR |= in.nextInt();
			
			System.out.println((OR * modPow(2, N-1)) % p);
		}
	}
	
	public static int modPow(long base, int exp) {
		if(exp < 0) return 0;
		if(exp == 0) return 1;
		
		LinkedList<Boolean> list = new LinkedList<>();
		int log = 0;
		int E = exp;
		while(E > 0) {
			list.addFirst(((E & 1) == 1) ? true : false);
			E >>= 1;
			log++;
		}
		
		long[] powers = new long[log];
		powers[log-1] = base;
		for(int i = log-2; i >= 0; i--)
			powers[i] = powers[i+1] * powers[i+1] % p;
		
		long ans = 1;
		for(int i = 0; i < powers.length; i++)
			if(list.get(i))
				ans = ans * powers[i] % p;
		
		return (int) ans;
	}
	
	/**
	 * Source: Matt Fontaine
	 */
	static class FastScanner {
		private InputStream stream;
		private byte[] buf = new byte[1024];
		private int curChar;
		private int chars;

		public FastScanner(InputStream stream) {
			this.stream = stream;
		}

		int read() {
			if (chars == -1)
				throw new InputMismatchException();
			if (curChar >= chars) {
				curChar = 0;
				try {
					chars = stream.read(buf);
				} catch (IOException e) {
					throw new InputMismatchException();
				}
				if (chars <= 0)
					return -1;
			}
			return buf[curChar++];
		}

		boolean isSpaceChar(int c) {
			return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
		}

		boolean isEndline(int c) {
			return c == '\n' || c == '\r' || c == -1;
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public String next() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			} while (!isSpaceChar(c));
			return res.toString();
		}

		public String nextLine() {
			int c = read();
			while (isEndline(c))
				c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			} while (!isEndline(c));
			return res.toString();
		}
	}

}
/*
1
3
1 2 3
Sample Output 0

12

2
4
1 2 4 8
5
1 2 3 5 100

120
1648
*/