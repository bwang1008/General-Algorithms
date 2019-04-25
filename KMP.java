import java.util.ArrayList;

public class KMP {
	
	public static void main(String[] args) {
		String text = "AAAAAAAAAAAAA";
		String patt = "AAAA";
		
		System.out.println("text: " + text.length());
		System.out.println("patt: " + patt.length());
		ArrayList<Integer> indexes = kmp(patt, text);
		System.out.println(indexes);
		for(int i : indexes)
			System.out.println(text.substring(i, i+patt.length()));
	}
	
	public static ArrayList<Integer> kmp(String patt, String text) {
		ArrayList<Integer> indexes = new ArrayList<>();
		char[] c1 = text.toCharArray();
		char[] c2 = patt.toCharArray();
		
		int[] lps = lps(c2);
		
		int i = 0, j = 0;
		while(j == c2.length || i < text.length()) {
			if(j == c2.length) {
				indexes.add(i-j);
				j = lps[j-1];
			}
			else if(c1[i] == c2[j]) {
				i++; j++;
			}
			else if(j > 0) j = lps[j-1];
			else i++;
		}
		
		return indexes;
	}
	
	public static int[] lps(char[] c) {
		int[] lps = new int[c.length];
		
		int j = 0;
		for(int i = 1; i < c.length; i++) {
			while(j > 0 && c[j] != c[i])
				j = lps[j-1];
			if(c[j] == c[i]) j++;
			lps[i] = j;
		}
		
		return lps;
	}

}
/*
AAAAAAAAAAAAAAAAAB
AAAAB
32


*/