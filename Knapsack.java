import java.util.Scanner;
public class Knapsack {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		while(in.hasNextInt()) {
			int T = in.nextInt();
			in.next();
			int totalWeight = in.nextInt();
			int N = in.nextInt();
			String[] items = new String[N];
			int[] weights = new int[N];
			int[] values = new int[N];
			for(int i = 0; i < N; i++) {
				items[i] = in.next();
				weights[i] = in.nextInt();
				values[i] = in.nextInt();
			}
		}
	}
	
	public static void calc(int totalWeight, int N, String[] items, int[] weights, int[] values) {
		int[][] dp = new int[N+1][N];
		for(int i = 0; i < N; i++) {
			
		}
	}

}
/*
1: 8 3
bread 3 4
rice 8 6
peas 5 5
2: 10 5
asparagus 1 5
thistle 2 3
peanuts 4 5
chickpeas 2 3
seeds 5 2

1: 9
bread
peas
2: 16
asparagus
thistle
peanus
chickpeas
*/