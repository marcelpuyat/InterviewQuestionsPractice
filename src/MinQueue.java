
/**
 * Implements enqueue in O(1), and dequeue and peek in amortized O(1) time. Uses two MinStacks
 * to implement getMin in O(1) time.
 * 
 * Enqueues values into the enterStack. Whenever a dequeue or peek is performed, if the exitStack is empty,
 * all elements from enterStack are pushed into exitStack (and hence are in reverse order, FIFO) and so the
 * top of the exitStack holds the head of the queue. Notice that when this is performed, all further dequeues
 * take O(1) time until the exitStack is emptied again. This means when dequeueing 10 elements, 19 operations are
 * performed (n for the first dequeue, and then O(1) for the rest), which is O(1) time amortized for each.
 * @author marcelp
 *
 * @param <T>
 */
public class MinQueue<T extends Comparable<T>> {

	/**
	 * Enqueued elements are pushed into this.
	 */
	private MinStack<T> enterStack;
	
	/**
	 * Dequeued elements are taken from this. When this is empty we shift all elems from
	 * enterStack into this.
	 */
	private MinStack<T> exitStack;

	public MinQueue() {
		enterStack = new MinStack<T>();
		exitStack = new MinStack<T>();
	}
	
	/**
	 * O(1) time
	 * @param elem
	 */
	public void enqueue(T elem) {
		enterStack.push(elem);
	}

	/**
	 * Amortized O(1) time
	 * @return
	 */
	public T dequeue() {
		shiftIfNecessary();
		if (exitStack.isEmpty()) return null;
		return exitStack.pop();
	}

	/**
	 * Amortized O(1) time.
	 * @return
	 */
	public T peek() {
		shiftIfNecessary();
		if (exitStack.isEmpty()) return null;
		return exitStack.peek();
	}
	
	/**
	 * Shifts all elements from enterStack into exitStack, this making exitStack perform in a FIFO order.
	 */
	private void shiftIfNecessary() {
		if (exitStack.isEmpty()) {
			while (!enterStack.isEmpty()) {
				exitStack.push(enterStack.pop());
			}
		}
	}

	/**
	 * O(1) time.
	 * @return
	 */
	public boolean isEmpty() {
		return enterStack.isEmpty() && exitStack.isEmpty();
	}

	/**
	 * Returns min in O(1) time.
	 * @return
	 */
	public T getMin() {
		// Because we are using 2 min stacks, we can simply return min between these two, checking for
		// edge cases when either returns null.
		
		T minInEnterStack = enterStack.getMin();
		T minInExitStack = exitStack.getMin();
		if (minInEnterStack == null) {
			return minInExitStack;
		} else if (minInExitStack == null) {
			return minInEnterStack;
		}
		return minInExitStack.compareTo(minInEnterStack) < 0 ? minInExitStack : minInEnterStack;
	}
}