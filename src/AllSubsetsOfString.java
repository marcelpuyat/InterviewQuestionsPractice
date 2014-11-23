import helpers.Pair;

import java.util.Stack;


public class AllSubsetsOfString {
	public static void main(String[] args) { 
		generateSubsetsIter("marcel");
		generateSubsetsWrapper("marcel");
	}
		
	public static void generateSubsetsWrapper(String str) {
		generateSubsetsRecur(str, 0, "");
	}
	
	public static void generateSubsetsRecur(String str, int startingIndex, String currStr) {
		if (startingIndex == str.length()) {
			System.out.println(currStr);
			return;
		}
		String currStrPlusCurrChar = currStr + str.charAt(startingIndex);
		generateSubsetsRecur(str, startingIndex + 1, currStrPlusCurrChar);
		generateSubsetsRecur(str, startingIndex + 1, currStr);
	}
	
	public static void generateSubsetsIter(String str) {
		Stack<Pair<String, String>> stack = new Stack<Pair<String, String>>();
		stack.push(new Pair<String, String>("", str));
		while (!stack.isEmpty()) {
			Pair<String, String> p = stack.pop();
			if (p.second.length() == 0)
				System.out.println(p.first);
			else {
				stack.push(new Pair<String, String>(p.first, p.second.substring(1)));
				stack.push(new Pair<String, String>(p.first + p.second.charAt(0), p.second.substring(1)));
			}
		}
	}
}
