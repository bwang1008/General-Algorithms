import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;
public class DistinctNumbers {

	/**
	 * Given a list of N integers, answer Q queries of form L R, find number of unique elements 
	 * from range [L, R). O((N+Q) log N)
	 */
	public static void main(String[] args) {
		//Read in with FastIO
		FastScanner in = new FastScanner(System.in);
		PrintWriter out = new PrintWriter(System.out);
		
		int N = in.nextInt();
		int[] arr = new int[N];
		for(int i = 0; i < N; i++)
			arr[i] = in.nextInt(); //O(N)
		
		int Q = in.nextInt();
		Pair[] q = new Pair[Q];
		Pair[] q2 = new Pair[Q];
		for(int i = 0; i < Q; i++) {
			int a = in.nextInt();
			int b = in.nextInt();
			q[i] = new Pair(a, b);
			q2[i] = new Pair(a, b); //O(Q)
		}
		
		//create BinaryIndexTree - out of frequencies (1 if first time met, 0 otherwise)
		BIT bit = new BIT(arr);	//O(N)
		//process queries offline
		HashMap<Pair, Integer> map = bit.processQueries(q2);
		
		for(Pair p : q)
			out.println(map.get(p));
		
		out.close();
	}
	
	/**
	 * Binary Index Tree - based on freq rather than traditional arr
	 * i & -i -> gets lowest bit that was 1
	 */
	static class BIT {
		private int N;
		private int[] arr;
		private int[] tree;
		private int[] freq;
		private TreeSet<Integer> set;
		
		public BIT(int[] a) {	//O(N)
			N = a.length;	//length of array
			arr = new int[N];	//actual contents to test uniqueness of numbers
			tree = new int[N];	//actual BIT
			freq = new int[N];	//BIT made from freq (1 if first time met traversing from left to right, 0 otherwise)
			set = new TreeSet<>();	//just a set to record how many unique numbers total
			
			for(int i = 0; i < N; i++)
				arr[i] = a[i];
			
			for(int i = 0; i < N; i++) {
				if(!set.contains(arr[i])) {
					freq[i] = 1;	//set freq
					set.add(arr[i]);
				}
			}
			
			int[] cul = new int[N+1]; //cumulative array of freq (rather than arr)- only temporary
			for(int i = 1; i < N+1; i++)	//of size N+1; cul[0] = 0
				cul[i] = cul[i-1] + freq[i-1]; 	//normally would be cul[i-1] + arr[i-1] but this we're not summing actual arr values
			
			for(int i = 1; i < N+1; i++)	//standard approach to create BIT (0 indexing)
				tree[i-1] = cul[i] - cul[i - (i & -i)];
		}
		
		public int cumulative(int index) {	//[0, L], 0-indexing, O(log N)
			if(index == 0) return freq[0];	//normally arr[0]
			int sum = 0;
			while(index > 0) {
				sum += tree[index++];
				index -= (index & -index);
				index--;
			}
			return sum;
		}
		
		/**
		 * Does actual preprocessing of pair Queries
		 * @param q - copy of queries
		 * @return HashMap<Pair, Integer> to output actual results in correct order of given pair queries
		 */
		public HashMap<Pair, Integer> processQueries(Pair[] q) {	//O((N+Q) log N)
			Arrays.sort(q);	//sort based on Left priority first
			
			//say arr = {2, 3, 2, 5, 5, 3, 2}
			//   freq = {1, 1, 0, 1, 0, 0, 0}
			
			int index = 0;
			HashMap<Integer, Integer> indexToContainer = new HashMap<>(set.size());
			for(int i : set)
				indexToContainer.put(i, index++);
			
			//indexToContainer: {(2 -> 0), (3 -> 1), (5 -> 2)}
			
			index = 0;
			@SuppressWarnings("unchecked")
			Queue<Integer>[] qList = new Queue[indexToContainer.size()];
			for(int i = 0; i < qList.length; i++)
				qList[i] = new LinkedList<Integer>();
			for(int i : arr)
				qList[indexToContainer.get(i)].add(index++);
			
			/* qList:
			 *  2:  0 |   0, 2, 6
			 *  3:  1 |   1, 5
			 *  5:  2 |   3, 4
			 */
			
			int L = 0;
			HashMap<Pair, Integer> result = new HashMap<>(q.length);	//to return
			
			for(Pair pair : q) {
				int qL = pair.a;
				int qR = pair.b;
				
				//say qL = 1, qR = 2
				
				while(L < qL) {	//do some precomputations to prepare freq and tree
					int toRemove = arr[L];	//don't need this L for now (need qL)  = 2
					Queue<Integer> temp = qList[indexToContainer.get(toRemove)];
					temp.poll(); //polled out the 0 in 0, 2, 6
					if(temp.size() > 0) {
						int nextPos = temp.peek();	// next one is 2
						set(nextPos, 1);	//set(2, 1) -> //freq = {1, 1, 1, 1, 0, 0, 0}
					}
					L++;
				}
				
				int value = sumRange(qL, qR);	//sum how many "uniqueness" there are - aka how many 1's there are in the range 
				result.put(pair, value);
			}
			
			return result;
		}
		
		public void set(int pos, int val) {
			int increase = val - freq[pos];
			freq[pos] = val;	//normally pos
			while(pos < N) {
				tree[pos++] += increase;
				pos += (pos & -pos);
				pos--;
			}
			
			
		}
		
		public int sumRange(int left, int right) {	// [L, R), 0-indexing
			int LHS = (left == 0) ? 0 : cumulative(left-1);
			return cumulative(right-1) - LHS;
		}
		
	}
	
	static class Pair implements Comparable<Pair> {
		int a; int b;
		
		public Pair(int x, int y) {
			a = x; b = y;
		}

		public int compareTo(Pair p2) {
			if(a < p2.a)
				return -1;
			if(a == p2.a) {
				if(b < p2.b)
					return -1;
				if(b == p2.b)
					return 0;
				return 1;
			}
			return 1;
		}
		
		public boolean equals(Object o) {
			if(!(o instanceof Pair))
				return false;
			Pair p2 = (Pair) o;
			return (a == p2.a && b == p2.b);
		}
		
		public int hashCode() {
			return (a + " " + b).hashCode();
		}
		
		public String toString() {
			return "(" + a + ", " +  b + ")";
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
/*
7
2 3 2 5 5 3 2
28
0 1
0 2
0 3
0 4
0 5
0 6
0 7
1 2
1 3
1 4
1 5
1 6
1 7
2 3
2 4
2 5
2 6
2 7
3 4
3 5
3 6
3 7
4 5
4 6
4 7
5 6
5 7
6 7

1
2
2
3
3
3
3

1
2
3
3
3
3

1
2
2
3
3

1
1
2
3

1
2
3

1
2

1
*/