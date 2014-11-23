
/**
 * Finds longest palindrome in string. Algorithm works as such:
 * There are 2n - 1 points that can be the center of a palindrome
 * (this includes all letters as well as spaces in between letters).
 * Try each of these, and expand outward to form longest palindrome possible.
 * Expansion takes O(n) time, so in total this takes O(n^2).
 * @author marcelp
 *
 */
public class LongestPalindromeInString {
	
	// Examples
	public static void main(String[] args) {
		printLongestPalindromeInString("racecar");
		printLongestPalindromeInString("momAndPop");
	}
	
	
	public static void printLongestPalindromeInString(String word) {
		if (word.length() == 0) {
			System.out.println("Empty string!");
		} else {
			String longest = word.substring(0, 1); // Longest is simply first letter when starting
			
			for (int i = 0; i < word.length(); i++) {
				
				// Try the center as the letter at index i
				String palindrome1 = expandAroundCenter(word, i, i);
				if (palindrome1.length() > longest.length()) {
					longest = palindrome1;
				}
				
				// Note that we can't use the space after the last index as a center
				if (i < word.length() - 1) {
					
					// Try the center as the space between i and i + 1
					String palindrome2 = expandAroundCenter(word, i, i + 1);
					if (palindrome2.length() > longest.length()) {
						longest = palindrome2;
					}
				}
			}
			System.out.println(longest);
		}
	}
	
	/* Center is considered either: A.) The letter at index1 or index2 when they are equal,
	 * or, the space between the letters and index1 and index2 when they are not equal.
	 */
	public static String expandAroundCenter(String word, int index1, int index2) {
		/* Longest palindrome is either the letter at index when they are equal, or empty string when the center
		 * is a space */
		String longestPalindrome = index1 == index2 ? word.substring(index1, index1 + 1) : "";
		while (index1 >= 0 && index2 <= word.length() - 1 && word.charAt(index1) == word.charAt(index2)) {
			// Spread index1 and index2 apart until no longer palindrome or we reach either end of word.
			index1--;
			index2++;
		}
		longestPalindrome = word.substring(index1 + 1, index2);
		return longestPalindrome;
	}
}
