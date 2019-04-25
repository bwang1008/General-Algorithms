import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;
public class RSA_Helper {

	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(new File("RSA.txt"));
		in.next();
		String line;
		String a = "";
		while(!(line = in.next()).equals("*"))
			a += line;
		
		BigInteger M = new BigInteger(a);
		
		int e = 3;
		
		while(!(line = in.next()).equals("N:"));
		a = "";
		while(!(line = in.next()).equals("*"))
			a += line;
		
		BigInteger N = new BigInteger(a);
		
		System.out.println("8815769761");
		System.out.println("77773");
		System.out.println("6256003596");
		BigInteger result = M.modPow(BigInteger.valueOf(e), N);
		System.out.println(result);
		in.close();
	}

}
