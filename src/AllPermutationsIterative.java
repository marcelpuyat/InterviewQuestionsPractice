import helpers.Pair;

import java.util.Stack;

/**
 * Prints out all permutations of letters in a word without recursion.
 * @author marcelp
 *
 */
public class AllPermutationsIterative {
	public static void main(String[] args) { 
		allPermsIter("abcde");
	}
	
	public static void allPermsIter(String word) {
		
		/* Each pair will contain a first value which is the current string we are building up,
		 * and a second value which is the remaining characters. */
		Stack<Pair<String, String>> q = new Stack<Pair<String, String>>();
		
		q.push(new Pair<String, String>("", word));
		
		while (!q.isEmpty()) {
			Pair<String, String> pair = q.pop();
			
			if (pair.second.length() == 0) {
				System.out.println(pair.first);
			} else {
				String remaining = pair.second;
				
				/* Try each of the characters remaining appended onto the current string we are building
				 * up. */
				for (int i = 0; i < remaining.length(); i++) {
					String soFarPlusLetter = pair.first + pair.second.charAt(i);
					String newRemaining = pair.second.substring(0, i) + pair.second.substring(i + 1);
					q.push(new Pair<String, String>(soFarPlusLetter, newRemaining));
				}
			}
		}
	}
}
