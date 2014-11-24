import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * New elements are entered in through standard input, and this
 * prints out the median of all given elements so far in O(log(n))
 * where n is the number of elements at each point.
 * 
 * Implemented using 2 heaps (Java Priority Queues), one for greater half
 * and the other for lesser half of elements seen so far.
 * @author marcelp
 *
 */
public class MaintainMedian {
	
	// Comparator needed to make this a max heap. Will hold lesser half of elements
	private static PriorityQueue<Integer> maxHeap = new PriorityQueue<Integer>(1, new Comparator<Integer>() {
		public int compare(Integer int1, Integer int2) {
			return int2 - int1;
		}
	});
	
	// By default, the comparator makes this a minHeap. Will hold greater half of elements
	private static PriorityQueue<Integer> minHeap = new PriorityQueue<Integer>();
	
	private static final int SENTINEL = -1;
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.print("Enter a number: ");
			int newNum = in.nextInt();
			if (newNum == SENTINEL) break;
			
			if (maxHeap.isEmpty()) {
				maxHeap.add(newNum);
				System.out.println("Median is: " + newNum);
			} else {
				addNumWhileMaintainingHalfInBothHeaps(newNum);
				System.out.println("Median is: " + minHeap.peek());
			}
		}
		System.out.println("Done");
		in.close();
	}
	
	/**
	 * Adds number to appropriate heap by comparing to the head of the bottom heap.
	 * Also makes sure that both sides are balanced by shifting head of either heap
	 * when necessary.
	 * 
	 * Note that this operation takes O(log(n)) time, where n is the total number
	 * of elements across both heaps.
	 * @param newNum
	 */
	private static void addNumWhileMaintainingHalfInBothHeaps(int newNum) {
		if (newNum > maxHeap.peek()) {			
			minHeap.add(newNum);
			
			if (minHeap.size() > maxHeap.size() + 1) {
				// Maintain balance if difference is greater than 2
				maxHeap.add(minHeap.poll());
			}
		} else {
			maxHeap.add(newNum);
			
			if (maxHeap.size() > minHeap.size() + 1) {
				// Maintain balance if difference is greater than 2
				minHeap.add(maxHeap.poll());
			}
		}
		
	}
}
