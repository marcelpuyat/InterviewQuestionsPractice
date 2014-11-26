
import java.util.Stack;

/**
 * Stack that supports pop, push, and getMin all in O(1) time.
 * This implementation uses an extra stack to keep track of
 * every element that becomes a minimum value. 
 * @author marcelp
 * 
 */
public class MinStack<T extends Comparable<T>> {
	private Stack<T> stack;
	private Stack<T> stackForMins;
	
	public MinStack() {
		stack = new Stack<T>();
		stackForMins = new Stack<T>();
	}
	
	public void push(T element) { 
		stack.push(element);
		if (stackForMins.isEmpty() || element.compareTo(stackForMins.peek()) <= 0) {
			/* Push onto min stack as well if it is empty or this is less (or equal to)
			 * the top element on it. The "or equal to" is important here; this makes it
			 * so that when popping elements off, we can simply remove elements from both
			 * the minstack and the regular stack when they are equal. */
			stackForMins.push(element);
		}
	}
	
	public T pop() {
		T element = stack.pop();
		if (element.equals(stackForMins.peek())) {
			/* Whenever what we are popping off is also the min value (or equal to other min
			 * values), we pop it off the min stack as well */
			stackForMins.pop();
		}
		return element;
	}
	
	public T getMin() {
		return stackForMins.peek();
	}
	
	/* Example using Characters */
	public static void main(String[] args) { 
		MinStack<Character> stack = new MinStack<Character>();
		Character first = 'H', second = 'B', third = 'Z', fourth = 'A';
		stack.push(first);
		stack.push(second);
		stack.push(third);
		stack.push(fourth);
		
		System.out.println("Stack (starting from bottom) now looks like: " + first + " "
																		   + second + " "
																		   + third + " "
																		   + fourth);
		System.out.println("Minimum: " + stack.getMin());
		Character removed = stack.pop();
		System.out.println("Popping top of stack off. Removed: " + removed);
		System.out.println("Minimum: " + stack.getMin());
		removed = stack.pop();
		System.out.println("Popping top of stack off. Removed: " + removed);
		System.out.println("Minimum: " + stack.getMin());
		removed = stack.pop();
		System.out.println("Popping top of stack off. Removed: " + removed);
		System.out.println("Minimum: " + stack.getMin());
	}
}
