import java.math.BigInteger;
import java.util.ArrayList;
public class BigInt {
	
	public static final long BASE = 100_000_000;
	public static final BigInt ZERO = new BigInt();
	
	private ArrayList<Long> con;
	private boolean negative;
	
	private String decString;

	public static void main(String[] args) {
		BigInt t1 = new BigInt();
		System.out.println("t1 = " + t1);
		
		String g2 = "-125672434";
		BigInt t2 = new BigInt(g2);
		t2.decString = null;
		System.out.println(t2.con);
		System.out.println("t2 = " + t2);
		
		String g3 = "-286519837205897";
		BigInt t3 = new BigInt(g3);
		t3.decString = null;
		System.out.println("t3 = " + t3);
		
		BigInteger b2 = new BigInteger(g2);
		BigInteger b3 = new BigInteger(g3);
		
		System.out.println(t2 + " x " + t3 + " = " + t2.multiply(t3));
		System.out.println(t3 + " x " + t2 + " = " + t3.multiply(t2));
		System.out.println("Has: " + t2.multiply(t3).digits() + " digits");
		System.out.println("actual: " + b2.multiply(b3));
	}
	
	public BigInt() {
		con = new ArrayList<>();
		con.add(0L);
		negative = false;
		decString = "0";
	}
	
	public BigInt(String val) {
		con = new ArrayList<>(val.length()/8 + 2);
		if(val.charAt(0) == '-')
			negative = true;
		
		decString = val;
		
		int i = val.charAt(0) == '-' ? 1 : 0;
		while(i < val.length() && val.charAt(i) == '0') i++;
		val = val.substring(i);
		if(val.length() == 0) {
			con.add(0L);
			negative = false;
			decString = "0";
			return;
		}
		
		char[] c = val.toCharArray();
		for(i = c.length-8; i >= 0; i -= 8) {
			long t = 0;
			for(int j = i; j < i+8; j++)
				t = 10*t + (c[j]-'0');
			con.add(t);
		}
		
		i += 8;
		if(i != 0) {
			long t = 0;
			for(int start = 0; start < i; start++)
				t = 10*t + (c[start]-'0');
			
			con.add(t);
		}
	}
	
	public BigInt(BigInt b) {
		con = new ArrayList<>(b.con.size());
		negative = b.negative;
		decString = b.toString();
		
		for(int i = 0; i < b.con.size(); i++)
			con.add(b.con.get(i));
	}
	
	public void absSelf() {
		if(negative = true) {
			negative = false;
			decString = null;
		}
	}
	
	public BigInt add(BigInt b) {
		BigInt result = new BigInt();
		result.con.remove(0);
		result.decString = null;
		
		if(b.equals(ZERO)) return new BigInt(this);
		if(equals(ZERO)) return new BigInt(b);
		
		if(!(negative ^ b.negative)) {
			int index1 = 0;
			int index2 = 0;
			while(index1 < con.size() && index2 < b.con.size()) {
				result.con.add(con.get(index1++) + b.con.get(index2++));
			}
			
			while(index1 < con.size())
				result.con.add(con.get(index1++));
			while(index2 < b.con.size())
				result.con.add(b.con.get(index2++));
			
			for(int i = 0; i < con.size()-1; i++) {
				long t = con.get(i);
				if(t >= 100_000_000) {
					long div = 1;
					long rem = t - 100_000_000;
					con.set(i, rem);
					con.set(i+1, con.get(i+1) + div);
				}
			}
			
			int last = con.size()-1;
			long t = con.get(last);
			if(t >= 100_000_000) {
				long div = t / 100_000_000;
				long rem = t % 100_000_000;
				con.set(last, rem);
				con.add(div);
			}
			
			result.negative = negative;
		}
		else if(negative) {
			negate();
			result = b.subtract(this);
			if(max(b) == this) result.negative = true;
			negate();
		}
		else if(b.negative) {
			b.negate();
			result = subtract(b);
			if(max(b) == b) result.negative = true; 
			b.negate();
		}
		
		return result;
	}
	
	public int compareTo(BigInt b) {
		if(negative && !b.negative) return -1;
		if(!negative && b.negative) return 1;
		if(equals(b)) return 0;
		if(negative && b.negative) {
			
		}
		return 0;
	}
	
	public int digits() {
		int ans = (con.size()-1) << 3;
		String g = String.valueOf(con.get(con.size()-1));
		return ans + g.length();
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof BigInt))
			return false;
		
		BigInt b = (BigInt) o;
		if(negative ^ b.negative)
			return false;
		
		if(con.size() != b.con.size())
			return false;
		
		for(int i = 0; i < con.size(); i++)
			if(con.get(i) != b.con.get(i))
				return false;
		
		return true;
	}
	
	public BigInt max(BigInt b) {
		if(negative && !b.negative)
			return new BigInt(b);
		if(!negative && b.negative)
			return new BigInt(this);
		if(negative && b.negative) {
			if(con.size() > b.con.size()) return new BigInt(b);
			if(con.size() < b.con.size()) return new BigInt(this);
			for(int i = con.size()-1; i >= 0; i--) {
				long v1 = con.get(i);
				long v2 = b.con.get(i);
				if(v1 < v2) return new BigInt(this);
				if(v1 > v2) return new BigInt(b);
			}
		}
		
		if(con.size() > b.con.size()) return new BigInt(this);
		if(con.size() < b.con.size()) return new BigInt(b);
		for(int i = con.size()-1; i >= 0; i--) {
			long v1 = con.get(i);
			long v2 = b.con.get(i);
			if(v1 < v2) return new BigInt(b);
			if(v1 > v2) return new BigInt(this);
		}
		
		return new BigInt(this);
	}
	
	public BigInt min(BigInt b) {
		if(negative && !b.negative)
			return new BigInt(this);
		if(!negative && b.negative)
			return new BigInt(b);
		if(negative && b.negative) {
			if(con.size() > b.con.size()) return new BigInt(this);
			if(con.size() < b.con.size()) return new BigInt(b);
			for(int i = con.size()-1; i >= 0; i--) {
				long v1 = con.get(i);
				long v2 = b.con.get(i);
				if(v1 < v2) return new BigInt(b);
				if(v1 > v2) return new BigInt(this);
			}
		}
		
		if(con.size() > b.con.size()) return new BigInt(b);
		if(con.size() < b.con.size()) return new BigInt(this);
		for(int i = con.size()-1; i >= 0; i--) {
			long v1 = con.get(i);
			long v2 = b.con.get(i);
			if(v1 < v2) return new BigInt(this);
			if(v1 > v2) return new BigInt(b);
		}
		
		return new BigInt(this);
	}
	
	public BigInt multiply(BigInt b) {
		boolean negA = negative;
		boolean negB = b.negative;
		boolean prodNegative = negative ^ b.negative;
		this.absSelf();
		b.absSelf();
		
		int N = Math.max(con.size(), b.con.size());
		int M = Math.min(con.size(), b.con.size());
		
		BigInt result = multiplyNaive(b);
		
		negative = negA;
		b.negative = negB;
		result.negative = prodNegative;
		return result;
	}
	
	public BigInt multiplyKaratsuba(BigInt b) {
		BigInt max = max(b);
		BigInt min = min(b);
		
		int N = max.con.size();
		int M = min.con.size();
		
		
		
		return null;
	}
	
	public BigInt multiplyNaive(BigInt b) {
		BigInt max = max(b);
		BigInt min = min(b);
		
		int N = max.con.size();
		int M = min.con.size();
		long[] product = new long[M+N+1];
		
		for(int i = 0; i < min.con.size(); i++) {
			for(int j = 0; j < max.con.size(); j++) {
				product[i+j] += min.con.get(i) * max.con.get(j);
				if(product[i+j] > 100_000_000) {
					int div = (int) (product[i+j] / 100_000_000);
					int rem = (int) (product[i+j] - div * 100_000_000);
					product[i+j] = rem;
					product[i+j+1] += div;
				}
			}
		}
		
//		System.out.println("Product:" + Arrays.toString(product));
		
		ArrayList<Long> list = new ArrayList<>(product.length);
		for(int i = 0; i < product.length; i++)
			list.add(product[i]);

		System.out.println("con = " + con);
		removeBeginningZeroes(list);
		System.out.println("con = " + con);
		
		BigInt result = new BigInt();
		result.con = list;
		result.decString = null;
		
		return result;
	}
	
	public void negate() {
		negative = !negative;
		decString = null;
	}
	
	private void removeBeginningZeroes(ArrayList<Long> list) {
		long t = 0;
		while((t = list.remove(list.size()-1)) == 0);
		list.add(t);
	}
	
	public BigInt subtract(BigInt b) {
		BigInt result = new BigInt();	//null result
		result.con.remove(0);
		result.decString = null;
		
		if(b.equals(ZERO)) return new BigInt(this);
		if(equals(ZERO)) return new BigInt(b);
		
		System.out.println("GONNA DO : " + this + " - " + b);
		
		if(!(negative ^ b.negative)) {	//If both have the same sign,
			System.out.println("Option1");
			if(equals(b)) return new BigInt();	//If equal magnitudes, return 0
			
			boolean bothNegative = negative;
			if(bothNegative) {
				negative = b.negative = false;
			}
			
			int index1 = 0;
			int index2 = 0;
			BigInt theMax = max(b);
			ArrayList<Long> maxList = theMax.con;
			ArrayList<Long> minList = (maxList == b.con) ? con : b.con;

			while(minList.size() < maxList.size())
				minList.add(0L);
			
			maxList.add(0L);
			minList.add(0L);
			
			for(int i = 0; i < maxList.size(); i++)
				result.con.add(0L);
			
			System.out.println("maxList = " + maxList);
			System.out.println("minList = " + minList);
			
			while(index1 < maxList.size() && index2 < minList.size()) {
				long val = maxList.get(index1) - minList.get(index2) + result.con.get(index1);
				if(val < 0) {
					val += 100_000_000;
					result.con.set(index1 + 1, -1L);
				}
				result.con.set(index1, val);
				index1 = ++index2;
			}
			
			long t = 0;
			while(result.con.size() > 0 && (t = result.con.remove(result.con.size()-1)) == 0);
			result.con.add(t);
			
			t = 0;
			while(minList.size() > 0 && (t = minList.remove(minList.size()-1)) == 0);
			minList.add(t);
			
			t = 0;
			while(maxList.size() > 0 && (t = maxList.remove(maxList.size()-1)) == 0);
			maxList.add(t);
			
			if(bothNegative) {
				negative = b.negative = true;
			}
			
			if(con == maxList) {
				result.negative = theMax.negative;
			}
			else {
				result.negative = !theMax.negative;
			}
		}
		else {
			b.negate();
			result = add(b);
			b.negate();
		}
		
		return result;
	}
	
	public String toString() {
		if(decString != null)
			return decString;
		
		StringBuilder sb = new StringBuilder();
		if(negative)
			sb.append('-');
		
		sb.append(con.get(con.size()-1));
		
		for(int i = con.size()-2; i >= 0; i--) {
			String temp = String.format("%08d", con.get(i));
			sb.append(temp);
		}
		
		return decString = sb.toString();
	}

}
