
/**
 * Converts a decimal to any base, and converts from a number in base n to decimal.
 * @author marcelp
 *
 */
public class ConvertToBaseN {
	public static void main(String[] args) { 
		System.out.println(convertToBaseN(10, 2)); // Prints out 1010
		System.out.println(convertToDecimal(1010, 2)); // Prints out 10
	}
	
	public static int convertToBaseN(int num, int baseN) {
		assert(baseN > 1 && num >= 0);
		
		int counter = 0;
		int result = 0;
		while (num > 0) {
			/* Intuition: On first pass, num % n gives number of n^0's.
			 * Then after dividing number by n, num % n gives number of n^x's, where x is number of iterations so far. */
			result += (num % baseN) * (int)Math.pow(10, counter);
			counter++;
			num /= baseN;
		}
		return result;
	}
	
	public static int convertToDecimal(int num, int baseN) {
		assert(baseN > 1 && num >= 0);
		
		// Iterate digit by digit, starting from right most.
		String asString = String.valueOf(num);
		int currIndex = asString.length() - 1;
		
		int counter = 0;
		int result = 0;
		while (counter < asString.length()) {
			result += Math.pow(baseN, counter) * (asString.charAt(currIndex) - '0'); // Note that subt. by '0' gives int value
			counter++;
			currIndex--;
		}
		return result;
	}
}
