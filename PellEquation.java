/* 6/12/2018, 5:08 PM
 * Maybe should use BigInteger instead of longs...
*/
import java.util.*;
public class PellEquation { //Find fundamental (smallest) solution positive integers such that x^2 - D*y^2 = 1, for a given D
							//Based on 100 Great Problems of Elementary Mathematics by Heinrich Dorrie, Problem 20 (Pell Equation)
	public static long start = 0;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("D = ");
		int D = in.nextInt();
		in.close();
		
		start = System.currentTimeMillis();
		
		long[] xy = solve(D);
		
		System.out.println(xy[0] + ", " + xy[1]);
		System.out.println((System.currentTimeMillis() - start) + " milliseconds");
		System.out.println(xy[0] * xy[0] + " - " + D + "* " + xy[1]*xy[1] + " = " + (xy[0] * xy[0] - D * xy[1] * xy[1]));
	}

	public static long[] solve(int D) {
		long[] result = new long[2];
		
		LinkedList<Integer> digits = new LinkedList<>();
		
		//Solve Equivalent: X^2 - D'Y^2 = 4, where X = 2x, D' = 4D, Y = y 
		
		D *= 4;
		double sqrt = Math.sqrt(D);
		
		if(sqrt == (int) sqrt) return new long[]{1, 0}; //X^2-Y'^2 cannot = 1, unless it is trivial solution X = 1, Y = 0;
		
		//Based on fractional expansion of root of reduced quadratic equation is purely periodic and results in solution of Pell Equation
		
		long a = 1; //Guarantees results in reduced quadratic equation; aka coprime a,b,c; first root of ax^2+bx+c is positive improper, second negative proper fraction
					//Results in 2a+b < sqrt < 2a - b (b < 0); even if x is even and D = x^2 + 4x (largest # divisble by 4 below next even square), if b was -x, D < (2+x)^2 is true.
		long b = (((long) sqrt) / 2) * (-2); //b must be even, since b^2 = (4D)^2 + 4ac, and negative 
		long c = (b*b - D) / 4; //From quadratic formula; c must be negative too
		
		long[] abc = {a, b, c};
		boolean flip = false;
		
		//Keeps generating more reduced equations with same discriminant D until back to original a,b,c
		while(abc[0] != a || abc[1] != b || abc[2] != c || !flip) 
		{
			flip = true;
			int g = (int) ((sqrt - abc[1]) / (2*abc[0]));
			digits.add(g); //Partial denominators of purely periodic fraction
			long ga = g * abc[0];
			long gab = ga + abc[1];
			long ggab = g * gab;
			
			long tempa = abc[0];
			abc[0] = -(abc[2] + ggab); //a' = negative of ag^2 + bg + c;
			abc[1] = -(ga + gab);      //b' = negative of 2ag + b
			abc[2] = -tempa;		   //c' = negative of a
			//Results in b'^2 - 4a'c' = b^2 - 4ac = D
		}
		
		Integer[] compact = digits.toArray(new Integer[digits.size()]);
		
		long[][] frac; //Finds numerator and denominator of fractional convergents to (positive) solution of original quadratic abc
		if(compact.length % 2 == 0)
			frac = new long[compact.length+2][2];
		else frac = new long[2*compact.length + 2][2]; //Must have an even amount for a period
		
		frac[0][0] = frac[1][1] = 0;
		frac[0][1] = frac[1][0] = 1;
		
		for(int i = 2; i < frac.length; i++)
			for(int j = 0; j < 2; j++)
				frac[i][j] = frac[i-2][j] + compact[(i-2) % compact.length] * frac[i-1][j];
		
		// p/q = (N-1) convergent; P/Q = Nth convergent; X = P + q; y = Q;
		result[0] = (frac[frac.length-1][0] + frac[frac.length-2][1]) / 2; //x = X/2;
		result[1] = frac[frac.length-1][1];
		
		return result;
	}

}