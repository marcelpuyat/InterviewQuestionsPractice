import java.util.ArrayList;
import java.util.Arrays;

/**
 * Solves the knapsack problem, which can be described as:
 * 
 * Given a knapsack with the capacity of holding a total weight W, and a list of items wherein each value has a
 * certain value v and a certain weight w, find the max possible value that can fit into the knapsack as well
 * as the list of items are selected to make up this max value.
 * 
 * This class solves the problem of finding the max possible value using dynamic programming in O(Wn) time and
 * O(Wn) space, where W is the total weight capacity and n is the number of items. The list of items that go into
 * this optimal solution can then (after the O(Wn) time computation for the max value) be computed, backtracking through
 * the dynamic programming grid used, in O(n) time.
 * @author marcelp
 *
 */
public class KnapsackProblem {
	
	private ArrayList<Item> itemList = new ArrayList<Item>();
	
	// Total weight capacit
	private int weightCapacity;
	
	// This is computed and set to the actual value in the constructor
	private int maxValuePossible = 0;
	
	/* These store the coordinates for the best value in the dynamic programming grid, allowing us to reconstruct
	 * the list of items actually chosen. */
	private int maxValueRow = 0;
	private int maxValueCol = 0;
	
	// Lazily generated optimal list of items.
	private ArrayList<Item> optimalListOfItems = null; 
	
	// Grid that is filled out via dynamic programming
	private int[][] optimalValueGrid;
	
	/**
	 * Item in knapsack problem wherein weight and value are integers with positive values.
	 * @author marcelp
	 *
	 */
	private static class Item {
		private int value;
		private int weight;
		
		/**
		 * Both values must be positive
		 * @param value
		 * @param weight
		 */
		public Item(int value, int weight) {
			assert(value > 0 && weight > 0);
			this.value = value;
			this.weight = weight;
		}
		
		public Integer getValue() {
			return this.value;
		}
		
		public Integer getWeight() {
			return this.weight;
		}
		
		@Override
		public String toString() {
			return "Value: " + value + "  |  Weight: " + weight;
		}
	}
	
	/**
	 * Will automatically compute max possible value in O(Wn) time where W is the weight capacity and n is the number
	 * of items.
	 * @param items
	 * @param capacity
	 */
	public KnapsackProblem(ArrayList<Item> items, int capacity) {
		this.weightCapacity = capacity;
		this.itemList = items;
		initializeOptimalValueGrid(capacity, items.size());
	}
	
	/**
	 * Will automatically compute max possible value in O(Wn) time where W is the weight capacity and n is the number
	 * of items.
	 * @param items
	 * @param capacity
	 */
	public KnapsackProblem(Item[] items, int capacity) {
		this.weightCapacity = capacity;
		this.itemList = new ArrayList<Item>(Arrays.asList(items));
		initializeOptimalValueGrid(capacity, items.length);
	}
	
	/**
	 * Returns max possible value possible from selecting items without going over capacity weight.
	 * @return
	 */
	public int getMaxValuePossible() {
		return maxValuePossible;
	}
	
	/**
	 * Returns list of items that make up the max possible value without going over capacity weight.
	 * Will run in O(n) where n is the number of items the first time this is called.
	 * Every call thereafter takes O(1) time.
	 * @return
	 */
	public ArrayList<Item> getOptimalListOfItems() {
		// Lazily generated
		return optimalListOfItems == null ? computeOptimalItemList() : optimalListOfItems;
	}
	
	/**
	 * Reconstructs list of items selected by backpedalling through the dynamic programming grid.
	 * Takes O(n) time.
	 * @return
	 */
	private ArrayList<Item> computeOptimalItemList() { 
		ArrayList<Item> optimalItemList = new ArrayList<Item>();
		
		// We start at the point in the grid that the max value was found.
		int currWeight = maxValueRow;
		
		// Iterate over each item to see if we selected it or not as part of our optimal list
		for (int itemNumber = maxValueCol; itemNumber >= 0; itemNumber--) {
			
			/* In the case of the first item, we know we did not select it if there is a zero its column, and
			 * vice versa if it is non-zero. */
			if (itemNumber == 0) {
				if (optimalValueGrid[currWeight][0] != 0)  {
					optimalItemList.add(itemList.get(0));
				}
			} else {
				/* In the case of anything besides the first item, we know we did not select it if our max value
				 * is maintained when looking at the max value in the grid for the preceding item and same weight.
				 * And we know we must have selected it if this is not the case, so before we iterate onto the next
				 * item, we update our current capacity to be subtracted by the weight of this item. */
				int currValue = optimalValueGrid[currWeight][itemNumber];
				if (optimalValueGrid[currWeight][itemNumber - 1] == currValue) {
					// This means we didn't take this item, so don't add it and continue.
					continue;
				} else {
					Item currItem = itemList.get(itemNumber);
					
					// We must have taken this item, so add its weight to current weight and move on.
					optimalItemList.add(currItem);
					currWeight -= currItem.getWeight();
				}
			}
		}
		return optimalItemList;
	}
	
	/**
	 * Declares and initializes grid and also fills it out in O(Wn) time.
	 * @param capacity
	 * @param numItems
	 */
	private void initializeOptimalValueGrid(int capacity, int numItems) {
		optimalValueGrid = new int[capacity][numItems];
		fillOutOptimalValueGrid();
	}
	
	/**
	 * Examine our decision at each item_i.
	 * In one case, we take item_i, and in another, we dont take item_i. For each decision, we take the max
	 * between both possibilities.
	 * 
	 * The max value if we do select item_i = value_i + max value for items without item_i with capacity w - w_i
	 * The max value if we don't select item_i = max value for items without item_i with same capacity w.
	 * 
	 * Recurrence: 
	 * Best value for weight_i + set_with_item_i = 
	 * Max(value of (total_weight & set_without_item_i),
	 * 	   value of (total_weight - weight_i & set_without_item_i) + value_i)
	 */
	private void fillOutOptimalValueGrid() {
		
		// Handle case where weightCapacity cannot be filled and/or no items given
		if (weightCapacity == 0 || itemList.size() == 0) {
			maxValuePossible = 0;
			return;
		}
		
		for (int currWeightCapacity = 0; currWeightCapacity < weightCapacity; currWeightCapacity++) {
			for (int itemNumber = 0; itemNumber < itemList.size(); itemNumber++) {
				Item currItem = itemList.get(itemNumber);
				int currItemWeight = currItem.getWeight();
				
				// If we are on first item, then this is automatically zero. And if not, we refer to our recurrence.
				int bestValueWithoutTakingCurrItem = itemNumber == 0 ? 
											0 : optimalValueGrid[currWeightCapacity][itemNumber - 1];
				
				/* 3 cases: weight cannot be handled (so zero), we are on first item (best case is its own weight), 
				 * and the case we described in our recurrence. */
				int bestValueWithCurrItem = 0;
				if (currItemWeight > currWeightCapacity) {
					bestValueWithCurrItem = 0;
				} else if (itemNumber == 0) {
					bestValueWithCurrItem = currItem.getValue();
				} else {
					bestValueWithCurrItem = optimalValueGrid[currWeightCapacity - currItem.getWeight()][itemNumber - 1]
							                + currItem.getValue();
				}
				
				int bestValue = Math.max(bestValueWithoutTakingCurrItem, bestValueWithCurrItem);
				optimalValueGrid[currWeightCapacity][itemNumber] = bestValue;
				
				// Update the values we are keeping track of if we actually find a new max
				if (bestValue > maxValuePossible) {
					maxValuePossible = bestValue;
					maxValueRow = currWeightCapacity;
					maxValueCol = itemNumber;
				}
			}
		}
	}
	
	/**
	 * Example of getting optimal value and list given a list of items.
	 * @param args
	 */
	public static void main(String[] args) { 
		// Each item is constructed with the first value being its value and the second value being its weight
		Item[] items = new Item[]{new Item(3, 3), new Item(1, 5), new Item(4, 5), new Item(2, 3),
								  new Item(4, 1), new Item(5, 2), new Item(3, 4)};
		
		KnapsackProblem kp = new KnapsackProblem(items, 10);
		
		System.out.println("Best value: " + kp.getMaxValuePossible());
		System.out.println("Items taken:");
		ArrayList<Item> optimalList = kp.getOptimalListOfItems();
		for (Item item : optimalList) { 
			System.out.println("\t" + item);
		}
	}
	
}
