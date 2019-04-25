
public class FastFourierTransform {

	public static void main(String[] args) {
		Complex[] coef = new Complex[5];
		coef[0] = new Complex(1, 0);
		coef[1] = new Complex(2, -1);
		coef[2] = new Complex(0, -1);
		coef[3] = new Complex(-1, 2);
		coef[4] = new Complex(0, 0);
//		coef[5] = new Complex();
//		coef[6] = new Complex();
//		coef[7] = new Complex();
		
/*		coef[0] = new Complex(2, 0);
		coef[1] = new Complex(-2, -2);
		coef[2] = new Complex(0, -2);
		coef[3] = new Complex(4, 4); */
		
		Complex[] result = FFT(coef);
		for(int i = 0; i < result.length; i++)
			System.out.println(i + ": " + result[i]);
		
		System.out.println("\nNAIVE:");
		Complex[] naive = naiveDFT(coef);
		for(int i = 0; i < naive.length; i++)
			System.out.println(i + ": " + naive[i]);
		
	}
	
	public static double[] multiplyPolynomials(double[] a, double[] b) {
		return null;
	}
	
	public static Complex[] FFT(Complex[] coef) {
		int N = coef.length;
		if(N == 1) return new Complex[] {coef[0]};
		
		if((N & 1) == 1) {
			Complex[] t = new Complex[N+1];
			for(int i = 0; i < N; i++)
				t[i] = coef[i];
			
			t[t.length-1] = new Complex();
			
//			N++;
			coef = t;
		}
		
		
		Complex[] even = new Complex[N+1 >> 1];
		Complex[] odd = new Complex[N - even.length];
		System.out.println("even, odd: length: " + even.length + ", " + odd.length);
		
		for(int i = 0; i < even.length; i++) {
			even[i] = coef[i << 1];
		}
		
		for(int i = 0; i < odd.length; i++) {
			odd[i] = coef[1 + (i << 1)];
		}
		
		for(int i = 0; i < even.length; i++)
			System.out.println("even[" + i + "] = " + even[i]);
		for(int i = 0; i < odd.length; i++)
			System.out.println("odd[" + i + "] = " + odd[i]);
		System.out.println();
		
		Complex[] half1 = FFT(even);
		Complex[] half2 = FFT(odd);
		
		Complex[] result = new Complex[N];
		for(int k = 0; k < N/2; k++) {
			double exp = -2 * Math.PI * k/ N;
			Complex wk = new Complex(Math.cos(exp), Math.sin(exp));
			
			Complex product = wk.multiply(half2[k]);
			result[k] = half1[k].add(product);
			result[k+N/2] = half1[k].subtract(product);
		}
		
		return result;
	}
	
	public static Complex[] naiveDFT(Complex[] coef) {
		int N = coef.length;
		Complex[] result = new Complex[N];
		for(int i = 0; i < N; i++)
			result[i] = new Complex();
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				
				double exp = -2 * Math.PI * i * j / N;
				Complex wk = new Complex(Math.cos(exp), Math.sin(exp));
				result[i] = result[i].add(wk.multiply(coef[j]));
				
			}
		}
		
		return result;
	}
	
	public static class Complex {
		private double re;
		private double im;
		
		public Complex() {
			re = im = 0;
		}
		
		public Complex(double a, double b) {
			re = a;
			im = b;
		}
		
		public Complex add(Complex b) {
			return new Complex(re + b.re, im + b.im);
		}
		
		public Complex conjugate() {
			return new Complex(re, -im);
		}
		
		public boolean equals(Object o) {
			if(!(o instanceof Complex)) return false;
			
			Complex b = (Complex) o;
			
			return (re == b.re && im == b.im);
		}
		
		public int hashCode() {
			return (re + " " + im).hashCode();
		}
		
		public Complex multiply(Complex b) {
			if(b == null) System.out.println("WHATTTTT");
			return new Complex(re * b.re - im * b.im, im * b.re + re * b.im);
		}
		
		public Complex subtract(Complex b) {
			return new Complex(re - b.re, im - b.im);
		}
		
		public String toString() {
			if(im == 0) return re + "";
			if(re == 0) return im + "i";
			if(im < 0) return re + " - " + -im + "i";
			return re + " + " + im + "i";
		}
		
	}

}
