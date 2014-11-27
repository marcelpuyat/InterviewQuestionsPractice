import java.util.ArrayList;

/**
 * Computes max contiguous sum in a given array of integers in O(n) time.
 * @author marcelp
 *
 */
public class MaxContiguousSum {
	
	private int maxSum = 0;
	private ArrayList<Integer> maxContiguousSubSequence = new ArrayList<Integer>();
	
	public MaxContiguousSum(int[] nums) {
		if (nums.length != 0) {
			maxSum = computeMaxContiguousSum(nums);
		}
	}
	
	public int getMaxContiguousSum() {
		return maxSum;
	}
	
	public ArrayList<Integer> getMaxContiguousSubsequence() {
		return maxContiguousSubSequence;
	}
	
	/**
	 * Maxsum starts off with first number. In each iteration, update the maxSum to be the max between (the current
	 * subsequence being built up PLUS the current value, and the current value alone.)
	 * 
	 * O(n) running time.
	 * @param nums
	 * @return
	 */
	private int computeMaxContiguousSum(int[] nums) {
		assert(nums.length > 0);
		
		int maxSoFar = nums[0];
		int currSum =  nums[0], startOfMaxSequence = 0, endOfMaxSequence = 0, lastStartPosition = 0;
		for (int i = 1; i < nums.length; i++) {
			int currValue = nums[i];
			// Choose between max of current value with the sum, and the current value on its own
			if (currSum + currValue > currValue) {
				currSum += currValue;
			} else {
				currSum = currValue;
				
				// This helps us find out the last time we switched starting points for a subseq when computing the
				// start of our max sequence
				lastStartPosition = i;
			}
			if (currSum > maxSoFar) {
				maxSoFar = currSum;
				endOfMaxSequence = i;
				startOfMaxSequence = lastStartPosition;
			}
		}
		
		generateMaxContiguousSubsequence(nums, startOfMaxSequence, endOfMaxSequence);
		return maxSoFar;
	}
	
	/**
	 * Constructs max subsequence list given start and endnpoint of max sequence into nums
	 * @param nums
	 * @param startOfMaxSequence
	 * @param endOfMaxSequence
	 */
	private void generateMaxContiguousSubsequence(int[] nums, int startOfMaxSequence, int endOfMaxSequence) {
		for (int i = startOfMaxSequence; i <= endOfMaxSequence; i++) {
			maxContiguousSubSequence.add(nums[i]);
		}
	}
	
	/**
	 * Example
	 * @param args
	 */
	public static void main(String[] args) { 
		MaxContiguousSum mcs = new MaxContiguousSum(new int[]{2, 3, -4, 9, -6, 3, -4, 1, 3, -2, 3, -1});
		System.out.println("Max sum of contiguous subseq: " + mcs.getMaxContiguousSum());
		System.out.println("Subseq: " + mcs.getMaxContiguousSubsequence());
		
		MaxContiguousSum mcs2 = new MaxContiguousSum(new int[]{-4, -1, -3});
		System.out.println("Max sum of contiguous subseq: " + mcs2.getMaxContiguousSum());
		System.out.println("Subseq: " + mcs2.getMaxContiguousSubsequence());
		
		MaxContiguousSum mcs3 = new MaxContiguousSum(new int[]{});
		System.out.println("Max sum of contiguous subseq: " + mcs3.getMaxContiguousSum());
		System.out.println("Subseq: " + mcs3.getMaxContiguousSubsequence());
	}
	
}
