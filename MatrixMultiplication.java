import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Random;
public class MatrixMultiplication {
	
	public static int countCall;

	public static void main(String[] args) {
//		FastScanner in = new FastScanner(System.in);
		
		int N = 256;
		countCall = 0;
		
//		int[][] a = {{1,2,3,4}, {5,6,7,8}, {9,10,11,12}, {13,14,15,16}};
//		int[][] b = {{16,15,14,13}, {12,11,10,9}, {8,7,6,5}, {4,3,2,1}};
		
		int[][] a = new int[N][N];
		int[][] b = new int[N][N];
		
		Random rand = new Random();
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				a[i][j] = rand.nextInt(256);
		
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				b[i][j] = rand.nextInt(256);
		
		long[][] c1 = new long[a.length][b[0].length];
		
		doNaive(a, b, c1);
		doStrassen1(a, b, c1);
		doStrassen2(a, b, c1);
//		System.out.println(checkEqual(a, b, c1, false) ? "YES EQUAL" : "Are not equal :(");
		System.out.println("countCall = " + countCall);
	}
	
	public static boolean checkEqual(int[][] a, int[][] b, long[][] c1, boolean show) {
		long[][] c2 = new long[c1.length][c1[0].length];
		doNaive(a, b, c1);
		doStrassen2(a, b, c2);
		
		if(show) {
			print(c1);
			System.out.println();
			print(c2);
		}
		
		return equals(c1, c2);
	}
	
	public static void doStrassen2(int[][] a, int[][] b, long[][] c) {
		int COUNT = 1;
		Stopwatch sw = new Stopwatch();
		sw.start();
		
		for(int i = 0; i < COUNT; i++)
			strassen2(a, b, c);
		
		sw.stop();
		System.out.println(sw);
	}
	
	public static void doStrassen1(int[][] a, int[][] b, long[][] c) {
		int COUNT = 1;
		Stopwatch sw = new Stopwatch();
		sw.start();
		
		for(int i = 0; i < COUNT; i++)
			strassen1(a, b, c);
		
		sw.stop();
		System.out.println(sw);
	}
	
	public static void doNaive(int[][] a, int[][] b, long[][] c) {
		int COUNT = 1;
		Stopwatch sw = new Stopwatch();
		sw.start();
		
		for(int i = 0; i < COUNT; i++)
			naiveMultiply(a, b, c);
		sw.stop();
//		print(c);
		System.out.println(sw);
	}
	
	public static void strassen2(int[][] a1, int[][] b1, long[][] c) {
		countCall++;
		int N = a1.length + (((a1.length & 1) == 1) ? 1 : 0);
		
		if(N <= 10) {
			naiveMultiply(a1, b1, c);
			return;
		}
		
		int[][] a = new int[N][N];
		int[][] b = new int[N][N];
		for(int i = 0; i < a1.length; i++) {
			for(int j = 0; j < a1[i].length; j++) {
				a[i][j] = a1[i][j];
				b[i][j] = b1[i][j];
			}
		}
		
		int half = N >> 1;
		int[][][] sums = new int[14][half][half];
		for(int i = 0; i < half; i++) {
			for(int j = 0; j < half; j++) {
				sums[0][i][j] = a[i][j] + a[i+half][j+half];		//a11 + a22
				sums[1][i][j] = b[i][j] + b[i+half][j+half];		//b11 + b22
				sums[2][i][j] = a[i+half][j] + a[i+half][j+half];	//a21 + a22
				sums[3][i][j] = b[i][j+half] - b[i+half][j+half];	//b12 - b22
				sums[4][i][j] = b[i+half][j] - b[i][j];				//b21 - b11
				sums[5][i][j] = a[i][j] + a[i][j+half];				//a11 + a12
				sums[6][i][j] = a[i+half][j] - a[i][j];				//a21 - a11
				sums[7][i][j] = b[i][j] + b[i][j+half];				//b11 + b12
				sums[8][i][j] = a[i][j+half] - a[i+half][j+half];	//a12 - a22
				sums[9][i][j] = b[i+half][j] + b[i+half][j+half];	//b21 + b22
				sums[10][i][j] = a[i][j];							//a11
				sums[11][i][j] = a[i+half][j+half];					//a22
				sums[12][i][j] = b[i][j];							//b11
				sums[13][i][j] = b[i+half][j+half];					//b22
			}
		}
		
		long[][][] Ms = new long[7][half][half];
		
		strassen2(sums[0], sums[1], Ms[0]);
		strassen2(sums[2], sums[12], Ms[1]);
		strassen2(sums[10], sums[3], Ms[2]);
		strassen2(sums[11], sums[4], Ms[3]);
		strassen2(sums[5], sums[13], Ms[4]);
		strassen2(sums[6], sums[7], Ms[5]);
		strassen2(sums[8], sums[9], Ms[6]);
		
		for(int i = 0; i < half; i++)
			for(int j = 0; j < half; j++)
				c[i][j] = Ms[0][i][j] + Ms[3][i][j] - Ms[4][i][j] + Ms[6][i][j];
		
		for(int i = 0; i < half; i++)
			for(int j = 0; j+half < c[i].length; j++)
				c[i][j+half] = Ms[2][i][j] + Ms[4][i][j];
		
		for(int i = 0; i+half < c.length; i++)
			for(int j = 0; j < half; j++)
				c[i+half][j] = Ms[1][i][j] + Ms[3][i][j];
		
		for(int i = 0; i+half < c.length; i++)
			for(int j = 0; j+half < c[i].length; j++)
				c[i+half][j+half] = Ms[0][i][j] - Ms[1][i][j] + Ms[2][i][j] + Ms[5][i][j];
		
	}
	
	public static void strassen1(int[][] a, int[][] b, long[][] c) {
		countCall++;
		int N = a.length;
		
//		System.out.println("N = " + N);
//		if(countCall % 100 == 0) System.out.println("countCall = " + countCall);
		
		if(N <= 10) {
			naiveMultiply(a, b, c);
			return;
		}
		
		int half = (N + 1) / 2;
		int[][] a11 = new int[half][half];
		int[][] a12 = new int[half][half];
		int[][] a21 = new int[half][half];
		int[][] a22 = new int[half][half];
		int[][] b11 = new int[half][half];
		int[][] b12 = new int[half][half];
		int[][] b21 = new int[half][half];
		int[][] b22 = new int[half][half];
		
		for(int i = 0; i < half; i++)
			for(int j = 0; j < half; j++)
				a11[i][j] = a[i][j];
		
		for(int i = 0; i < half; i++)
			for(int j = 0; j+half < a[i].length; j++)
				a12[i][j] = a[i][j+half];
		
		for(int i = 0; i+half < a.length; i++)
			for(int j = 0; j < half; j++)
				a21[i][j] = a[i+half][j];
		
		for(int i = 0; i+half < a.length; i++)
			for(int j = 0; j+half < a[i].length; j++)
				a22[i][j] = a[i+half][j+half];
		
		for(int i = 0; i < half; i++)
			for(int j = 0; j < half; j++)
				b11[i][j] = b[i][j];
		
		for(int i = 0; i < half; i++)
			for(int j = 0; j+half < a[i].length; j++)
				b12[i][j] = b[i][j+half];
		
		for(int i = 0; i+half < a.length; i++)
			for(int j = 0; j < half; j++)
				b21[i][j] = b[i+half][j];
		
		for(int i = 0; i+half < a.length; i++)
			for(int j = 0; j+half < a[i].length; j++)
				b22[i][j] = b[i+half][j+half];
		
		int[][] a11_p_a22 = new int[half][half];
		int[][] b11_p_b22 = new int[half][half];
		int[][] a21_p_a22 = new int[half][half];
		int[][] b12_m_b22 = new int[half][half];
		int[][] b21_m_b11 = new int[half][half];
		int[][] a11_p_a12 = new int[half][half];
		int[][] a21_m_a11 = new int[half][half];
		int[][] b11_p_b12 = new int[half][half];
		int[][] a12_m_a22 = new int[half][half];
		int[][] b21_p_b22 = new int[half][half];
		
		add(a11, a22, a11_p_a22);
		add(b11, b22, b11_p_b22);
		add(a21, a22, a21_p_a22);
		subtract(b12, b22, b12_m_b22);
		subtract(b21, b11, b21_m_b11);
		add(a11, a12, a11_p_a12);
		subtract(a21, a11, a21_m_a11);
		add(b11, b12, b11_p_b12);
		subtract(a12, a22, a12_m_a22);
		add(b21, b22, b21_p_b22);
		
		long[][] M1 = new long[half][half];
		long[][] M2 = new long[half][half];
		long[][] M3 = new long[half][half];
		long[][] M4 = new long[half][half];
		long[][] M5 = new long[half][half];
		long[][] M6 = new long[half][half];
		long[][] M7 = new long[half][half];
		
		strassen1(a11_p_a22, b11_p_b22, M1);
		strassen1(a21_p_a22, b11, M2);
		strassen1(a11, b12_m_b22, M3);
		strassen1(a22, b21_m_b11, M4);
		strassen1(a11_p_a12, b22, M5);
		strassen1(a21_m_a11, b11_p_b12, M6);
		strassen1(a12_m_a22, b21_p_b22, M7);
		
		long[][] c11 = new long[half][half];
		long[][] c12 = new long[half][half];
		long[][] c21 = new long[half][half];
		long[][] c22 = new long[half][half];
		
		add(M1, M4, c11);
		subtract(c11, M5, c11);
		add(c11, M7, c11);
		
		add(M3, M5, c12);
		
		add(M2, M4, c21);
		
		subtract(M1, M2, c22);
		add(c22, M3, c22);
		add(c22, M6, c22);
		
		for(int i = 0; i < half; i++)
			for(int j = 0; j < half; j++)
				c[i][j] = c11[i][j];
		
		for(int i = 0; i < half; i++)
			for(int j = 0; j+half < c[0].length; j++)
				c[i][j+half] = c12[i][j];
		
		for(int i = 0; i+half < c.length; i++)
			for(int j = 0; j < half; j++)
				c[i+half][j] = c21[i][j];
		
		for(int i = 0; i+half < c.length; i++)
			for(int j = 0; j+half < c[i].length; j++)
				c[i+half][j+half] = c22[i][j];
	}
	
	public static void add(int[][] a, int[][] b, int[][] c) {
		for(int i = 0; i < c.length; i++)
			for(int j = 0; j < c[i].length; j++)
				c[i][j] = a[i][j] + b[i][j];
	}
	
	public static void add(long[][] a, long[][] b, long[][] c) {
		for(int i = 0; i < c.length; i++)
			for(int j = 0; j < c[i].length; j++)
				c[i][j] = a[i][j] + b[i][j];
	}
	
	public static void subtract(int[][] a, int[][] b, int[][] c) {
		for(int i = 0; i < c.length; i++)
			for(int j = 0; j < c[i].length; j++)
				c[i][j] = a[i][j] - b[i][j];
	}
	
	public static void subtract(long[][] a, long[][] b, long[][] c) {
		for(int i = 0; i < c.length; i++)
			for(int j = 0; j < c[i].length; j++)
				c[i][j] = a[i][j] - b[i][j];
	}
	
	public static void naiveMultiply(int[][] a, int[][] b, long[][] c) {
		int K = b.length;
		for(int i = 0; i < a.length; i++)
			for(int j = 0; j < b[0].length; j++)
				for(int k = 0; k < K; k++)
					c[i][j] += a[i][k] * b[k][j];
	}
	
	public static boolean equals(long[][] a, long[][] b) {
		if(a.length != b.length || a[0].length != b[0].length)
			return false;
		for(int i = 0; i < a.length; i++)
			for(int j = 0; j < a[i].length; j++)
				if(a[i][j] != b[i][j])
					return false;
		return true;
	}
	
	public static void print(int[][] a) {
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a[i].length; j++)
				System.out.printf("%-4d ", a[i][j]);
			System.out.println();
		}
	}
	
	public static void print(long[][] a) {
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a[i].length; j++)
				System.out.printf("%-4d ", a[i][j]);
			System.out.println();
		}
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
