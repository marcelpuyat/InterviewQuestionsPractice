import java.util.HashMap;

/**
 * Prints out nth fibonacci number, done recursively and iteratively.
 * Recursive version done with memoization.
 * @author marcelp
 *
 */
public class Fibonacci {
	
	// Used for memoization. Default value of null signifies the value has not been computed yet.
	private static HashMap<Integer, Integer> fibonacciIndexToValue = new HashMap<Integer, Integer>();
	
	public static void main(String[] args){ 
		fibRecurWrapper(7);
		fibIter(7);
	}
	
	public static void fibRecurWrapper(int n) {
		assert(n > 0);
		System.out.println(fibRecur(n));
	}
	
	public static int fibRecur(int n) {
		if (n == 1 || n == 2)
			return 1;
		
		Integer fibOfNMinusOne = fibonacciIndexToValue.get(n - 1);
		if (fibOfNMinusOne == null) {
			fibOfNMinusOne = fibRecur(n - 1);
			fibonacciIndexToValue.put(n - 1, fibOfNMinusOne);
		}
		Integer fibOfNMinusTwo = fibonacciIndexToValue.get(n - 2);
		if (fibOfNMinusTwo == null) {
			fibOfNMinusTwo = fibRecur(n - 2);
			fibonacciIndexToValue.put(n - 2, fibOfNMinusTwo);
		}
		
		return fibOfNMinusOne + fibOfNMinusTwo;
	}
	
	public static void fibIter(int n) {
		assert(n > 0);
		int fibMinus1 = 0;
		int curr = 1;
		while (n > 1) {
			int old = curr;
			curr += fibMinus1;
			fibMinus1 = old;
			n--;
		}
		System.out.println(curr);
	}
}
