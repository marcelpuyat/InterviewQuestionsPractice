
/**
 * Prints out all permutations of letters in a word using recursion.
 * @author marcelp
 *
 */
public class AllPermutationsRecursive {
	public static void main(String[] args) { 
		generatePermsWrapper("abcde");
	}
	
	public static void generatePermsWrapper(String str) {
		generatePermsRecur("", str);
	}
	
	public static void generatePermsRecur(String prefix, String remaining) {
		if (remaining.length() == 0) {
			System.out.println(prefix);
			return;
		}
		
		for (int i = 0; i < remaining.length(); i++) {
			String prefixPlusNewLetter = prefix + String.valueOf(remaining.charAt(i));
			String remainingMinusLetter = remaining.substring(0, i) + remaining.substring(i + 1);
			generatePermsRecur(prefixPlusNewLetter, remainingMinusLetter);
		}
	}
}
