import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;
public class XOR_LeftRightRange {

	public static void main(String[] args) {
		FastScanner in = new FastScanner(System.in);
		PrintWriter out = new PrintWriter(System.out);
		
		int Q = in.nextInt();
		while(Q --> 0)
			out.println(ans(in.nextLong(), in.nextLong()));
		out.close();
	}
	
	public static long ans(long left, long right) {
		return get(right) ^ get(Math.max(left-1, 0));
	}
	
	public static long get(long q) {
		int b = (int) q & 7;
		if(b == 0 || b == 1) return q;
		if(b == 2 || b == 3) return 2;
		if(b == 4 || b == 5) return q + 2;
		return 0;
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
3
2 4
2 8
5 9
Sample Output 0

7
9
15
*/