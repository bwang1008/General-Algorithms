import java.util.Scanner;

public class Logarithm { //Thanks to @user188811 on StackExchange

	public static void main(String[] args) {
		System.out.println(log(10, 2));
		System.out.println("enter a number: ");
		Scanner in = new Scanner(System.in);
		int N = in.nextInt();
		System.out.println("You inputted: N = " + N);
		System.out.println(log(10, N));
		in.close();
		System.out.println("Finish");
	}
	
	public static double log(double base, double num) { //Requires BigDecimal to be able to hold arbitrary precision logs :\
		if(base <= 1 || num <= 0)
			throw new IllegalArgumentException();
		
		StringBuilder sb = new StringBuilder();
		
		if(num < 1)
		{
			num = 1.0 / num;
			sb.append("-");
		}
		
		for(int i = -1; i < 20; i++)
		{
			int count = 0;
			while(num >= base)
			{
				num /= base;
				count++;
			}
			
			sb.append(count);
			
			if(i == -1)
				sb.append(".");
			
			num = Math.pow(num, 10);
		}
		
		return Double.parseDouble(sb.toString());
	}

}
