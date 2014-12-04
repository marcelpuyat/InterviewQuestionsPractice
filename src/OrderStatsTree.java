
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import helpers.Pair;

/**
 * Supports all of the following methods in O(height) time:
 * - get (search)
 * - insert
 * - delete
 * - max
 * - min
 * - getKthOrder (select)
 * - getRank
 * - predecessor/successor
 * 
 * Can also return ordered list in O(n) time.
 * @author marcelp
 *
 * @param <T1>
 * @param <T2>
 */
public class OrderStatsTree <T1 extends Comparable<T1>, T2> {

	/**
	 * Augmented BST node which contains size of subtree.
	 * Because all values are modified often, all instance variables left public.
	 * @author marcelp
	 *
	 * @param <T1>
	 * @param <T2>
	 */
	protected static class OSTNode<T1 extends Comparable<T1>, T2> {
		// Contains # of nodes in subtree beneath this node, as well as itself
		public int sizeOfSubtree;
		
		public T1 key;
		public T2 value;
		
		public OSTNode<T1, T2> rightChild;
		public OSTNode<T1, T2> leftChild;
		public OSTNode<T1, T2> parent;
		
		public OSTNode(T1 key, T2 value) {
			this.key = key;
			this.value = value;
			sizeOfSubtree = 1;
		}
		
		// For returning node as key-value pair
		public Pair<T1, T2> toKeyValuePair() {
			return new Pair<T1, T2>(this.key, this.value);
		}
		
		@Override
		public String toString() {
			String toReturn = "";
			toReturn += this.key;
			if (this.leftChild != null && this.rightChild == null) {
				toReturn += new String(": {L " + this.leftChild.key + "}");
			} else if (this.leftChild == null && this.rightChild != null) {
				toReturn += new String(": {R " + this.rightChild.key + "}");
			} else if (this.leftChild != null && this.rightChild != null) {
				toReturn += new String(": {L " + this.leftChild.key + ", R " + this.rightChild.key + "}");
			} else {
				toReturn += new String(":{}");
			}
			
			toReturn += " Size: " + sizeOfSubtree;
			return toReturn;
		}
	}
	
	protected OSTNode<T1, T2> root;
	
	/**
	 * Returns true on successful insertion, false when this key had already been entered and did an update.
	 * 
	 * O(height) running time
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean insert(T1 key, T2 value) {
		return insertAndReturnNode(key, value).first;
	}
	
	protected Pair<Boolean, OSTNode<T1, T2>> insertAndReturnNode(T1 key, T2 value) {
		OSTNode<T1, T2> newNode = new OSTNode<T1, T2>(key, value);
		
		OSTNode<T1, T2> newParent = null;
		OSTNode<T1, T2> spotForNewNode = root;
		
		// Loop as if we are searching for this node, but save the parent before we hit null.
		while (spotForNewNode != null) {
			newParent = spotForNewNode;
			
			// Update size of every node in the path to this new node
			newParent.sizeOfSubtree++;
			
			if (spotForNewNode.key.compareTo(key) == 0) {
				// Case where node with this key already existed.
				spotForNewNode.value = value;
				return new Pair<Boolean, OSTNode<T1, T2>>(false, null);
			}
			
			if (spotForNewNode.key.compareTo(key) < 0) {
				spotForNewNode = spotForNewNode.rightChild;
			} else {
				spotForNewNode = spotForNewNode.leftChild;
			}
		}
		
		newNode.parent = newParent;
		if (newParent == null) {
			// Case where tree was empty, so newNode is now root
			root = newNode;
		}
		else {
			if (newParent.key.compareTo(key) < 0) {
				newParent.rightChild = newNode;
			} else {
				newParent.leftChild = newNode;
			}
		}
		return new Pair<Boolean, OSTNode<T1, T2>>(false, newNode);
	}
	
	/**
	 * Returns true on successful deletion, false if could not find element with given key.
	 * 
	 * O(height) running time
	 * @param key
	 * @return
	 */
	public boolean delete(T1 key) {
		OSTNode<T1, T2> nodeToDelete = search(root, key);
		if (nodeToDelete == null) {
			return false;
		}
				
		// First case: Node has no children. Can simply update it's parent to point to null
		if (nodeToDelete.rightChild == null && nodeToDelete.leftChild == null) {
			decrementParentsSubtreeCount(nodeToDelete.parent);
			updateParentOfReplacedNode(nodeToDelete, null);
		}
		
		// Second case: Node has a single child. Make parent point to its child.
		else if (nodeToDelete.rightChild == null || nodeToDelete.leftChild == null) {
			decrementParentsSubtreeCount(nodeToDelete.parent);
			OSTNode<T1, T2> child = nodeToDelete.rightChild == null ? nodeToDelete.leftChild : nodeToDelete.rightChild;
			updateParentOfReplacedNode(nodeToDelete, child);
		}
		
		// Third case: Node has two children. Must replace with successor. Note that there are two subcases
		else {
			OSTNode<T1, T2> successorNode = successorNode(nodeToDelete);
			
			if (successorNode == nodeToDelete.rightChild) {
				/* If successor is the right child, simply transplant and 
				 * make old left subtree be the new nodes left subtree */
				
				decrementParentsSubtreeCount(nodeToDelete.parent);
				successorNode.leftChild = nodeToDelete.leftChild;
				nodeToDelete.leftChild.parent = successorNode;
			} else {
				/* Successor was not the right child. Successor takes place of nodeToDelete, inheriting its two children.
				 * And successor's old right child becomes its old parent's left child. */
				
				decrementParentsSubtreeCount(successorNode.parent);
				
				// Update old parent of successor to take on the successor's old right child
				successorNode.parent.leftChild = successorNode.rightChild;
				
				// Update children of node to be deleted
				successorNode.leftChild = nodeToDelete.leftChild;
				successorNode.leftChild.parent = successorNode;
				successorNode.rightChild = nodeToDelete.rightChild;
				successorNode.rightChild.parent = successorNode;
			}
			// Set count of successor to be the sum of its two children's sizes + 1
			updateSubtreeCount(successorNode);
			updateParentOfReplacedNode(nodeToDelete, successorNode);
		}
		return true;
	}
	
	/**
	 * Propogate upward until hitting root, decrementing subtree counts at each node.
	 * 
	 * O(height) running time.
	 * @param node
	 */
	private void decrementParentsSubtreeCount(OSTNode<T1, T2> node) {
		while (node != null) {
			node.sizeOfSubtree--;
			node = node.parent;
		}
	}
	
	/**
	 * This is used on a node that takes on 2 new children through rewiring. Will
	 * update its subtree size count to be size of leftChild + size of rightChild + 1
	 * 
	 * Operates in constant time.
	 * @param node
	 */
	private void updateSubtreeCount(OSTNode<T1, T2> node) {
		node.sizeOfSubtree = 1; // For this node itself
		if (node.rightChild != null) {
			node.sizeOfSubtree += node.rightChild.sizeOfSubtree;
		}
		if (node.leftChild != null) {
			node.sizeOfSubtree += node.leftChild.sizeOfSubtree;
		}
	}
	
	/**
	 * Rewires parent of nodeToBeReplaced to have nodeToPlaceIn as its child (in the proper position).
	 * Also handles case where node to be placed in must be the root.
	 * 
	 * Operates in constant time.
	 * @param nodeToBeReplaced
	 * @param nodeToPlaceIn
	 */
	private void updateParentOfReplacedNode(OSTNode<T1, T2> nodeToBeReplaced, OSTNode<T1, T2> nodeToPlaceIn) { 
		if (nodeToBeReplaced == root) {
			root = nodeToPlaceIn;
		} else {			
			if (nodeToBeReplaced.parent.rightChild == nodeToBeReplaced) { 
				nodeToBeReplaced.parent.rightChild = nodeToPlaceIn;
			} else {
				nodeToBeReplaced.parent.leftChild = nodeToPlaceIn;
			}
		}
		nodeToBeReplaced = null; // For garbage collector
	}
	
	/**
	 * Returns value for element with the given key, or null if it does not exist in tree.
	 * 
	 * Operates in log(n) time.
	 * @param key
	 * @return
	 */
	public T2 get(T1 key) {
		OSTNode<T1, T2> searchResult = search(root, key);
		
		if (searchResult == null) {
			return null;
		}
		return searchResult.value;
	}
	
	/**
	 * Returns node with given key, starting search from node passed in as the currNode
	 * 
	 * Operates in O(height) time.
	 * @param root
	 * @param key
	 * @return
	 */
	protected OSTNode<T1, T2> search(OSTNode<T1, T2> currNode, T1 key) {
		while (currNode != null && currNode.key.compareTo(key) != 0) {
			if (currNode.key.compareTo(key) < 0) {
				/* Because of Binary-search-tree property, any node with a key greater than the current node must be
				 * in its right subtree */
				currNode = currNode.rightChild;
			} else {
				currNode = currNode.leftChild;
			}
		}
		return currNode;
	}
	
	/**
	 * Returns key-value pair of kth order element, or null if outside of range of existing elements.
	 * 
	 * Operates in O(height) time.
	 * @param kthOrder
	 * @return
	 */
	public Pair<T1, T2> getKthOrder(int kthOrder) {
		if (kthOrder < 0 || kthOrder >= root.sizeOfSubtree) {
			return null;
		}
		return getKthOrderRecur(root, kthOrder).toKeyValuePair();
	}
	
	/**
	 * Recursive way of finding out what the kth order element is.
	 * 
	 * Operates in O(height) time.
	 * @param root
	 * @param kthOrder
	 * @return
	 */
	private OSTNode<T1, T2> getKthOrderRecur(OSTNode<T1, T2> root, int kthOrder) {
		int numLessThanCurr = root.leftChild == null ? 0 : root.leftChild.sizeOfSubtree;
		int myRank = numLessThanCurr; // zero indexed
		if (myRank == kthOrder) {
			return root;
		} else if (myRank < kthOrder) {
			/* Here, if we know we must search in the right subtree, recurse on right and effectively search for the
			 * k - currRank - 1 element */
			return getKthOrderRecur(root.rightChild, kthOrder - myRank - 1);
		} else {
			return getKthOrderRecur(root.leftChild, kthOrder);
		}
	}
	
	/**
	 * Returns key-value pair for element with max key in the tree, or null if empty.
	 * 
	 * Operates in O(height)
	 * @return
	 */
	public Pair<T1, T2> getMax() {
		return getMaxStartingAtNode(root).toKeyValuePair();
	}
	
	/**
	 * Broken out into helper function so that finding successor can reuse this.
	 * @param currNode
	 * @return
	 */
	private OSTNode<T1, T2> getMaxStartingAtNode(OSTNode<T1, T2> currNode) { 
		if (currNode == null) return null;
		
		while (currNode.rightChild != null) {
			// Go right until we see null is next
			currNode = currNode.rightChild;
		}
		return currNode;
	}
	
	/**
	 * Returns key-value pair for element with min key in the tree, or null if empty.
	 * 
	 * Operates in O(height)
	 * @return
	 */
	public Pair<T1, T2> getMin() {
		return getMinStartingAtNode(root).toKeyValuePair();
	}
	
	/**
	 * Broken out into helper function so that finding successor can reuse this.
	 * @param currNode
	 * @return
	 */
	private OSTNode<T1, T2> getMinStartingAtNode(OSTNode<T1, T2> currNode) { 
		if (currNode == null) return null;
		
		while (currNode.leftChild != null) {
			// Go left until we see null is next
			currNode = currNode.leftChild;
		}
		return currNode;
	}
	
	/**
	 * Returns rank of node with given key (starting at 0), or -1 if not in tree.
	 * 
	 * Operates in O(height) time.
	 * @param key
	 * @return
	 */
	public int getRank(T1 key) {
		OSTNode<T1, T2> node = search(root, key);
		if (node == null) {
			return -1;
		}
		
		// First, we know we are greater than all elements in left subtree
		int rank = node.leftChild == null ? 0 : node.leftChild.sizeOfSubtree;
		
		// Now we must get all nodes with key < our key in the tree, plus their left subtree sizes
		OSTNode<T1, T2> potentialLesserAncestor = node;
		while (potentialLesserAncestor != root) {
			
			if (potentialLesserAncestor == potentialLesserAncestor.parent.rightChild) {
				/* Whenever we find that we have a rightChild, the parent must be less, hence we add it and its left
				 * subtree */
				rank += potentialLesserAncestor.leftChild == null ? 1 : potentialLesserAncestor.leftChild.sizeOfSubtree + 1;
			}
			potentialLesserAncestor = potentialLesserAncestor.parent;
		}
		return rank;
	}
	
	/**
	 * Returns key-value pair for element immediately preceding element with given key, or null if tree is empty or
	 * no predecessor exists.
	 * 
	 * Operates in O(height) time.
	 * @param key
	 * @return
	 */
	public Pair<T1, T2> predecessor(T1 key) {
		OSTNode<T1, T2> node = search(root, key);
		if (node == null) {
			return null;
		}
		
		// If a left child exists, get max of left subtree.
		if (node.leftChild != null) {
			return getMaxStartingAtNode(node.leftChild).toKeyValuePair();
		}
		
		// If a left child does not exist, iterate upwards until finding a lesser parent. If none, then no predecessor.
		else {
			OSTNode<T1, T2> potentialPredecessor = node;
			while (potentialPredecessor.parent != null) {
				potentialPredecessor = node.parent;
				if (potentialPredecessor.key.compareTo(node.key) < 0) {
					return potentialPredecessor.toKeyValuePair();
				}
			}
			return null;
		}
	}
	
	/**
	 * Returns key-value pair for element immediately succeeding element with given key, or null if tree is empty or
	 * no successor exists.
	 * 
	 * Operates in O(height) time.
	 * @param key
	 * @return
	 */
	public Pair<T1, T2> successor(T1 key) {
		OSTNode<T1, T2> node = search(root, key);
		if (node == null) {
			return null;
		}
		return successorNode(node).toKeyValuePair();
	}
	
	/**
	 * This is broken out into a helper function so that deletion can use it.
	 * @param node
	 * @return
	 */
	private OSTNode<T1, T2> successorNode(OSTNode<T1, T2> node) {
		// If a right child exists, get min of right subtree.
		if (node.rightChild != null) {
			return getMinStartingAtNode(node.rightChild);
		}
		// If a right child does not exist, iterate upwards until finding a greater parent. If none, then no predecessor.
		else {
			OSTNode<T1, T2> potentialSuccessor = node;
			while (potentialSuccessor.parent != null) {
				potentialSuccessor = node.parent;
				if (potentialSuccessor.key.compareTo(node.key) > 0) {
					return potentialSuccessor;
				}
			}
			return null;
		}
	}
	
	/**
	 * Returns array list of key-value pairs sorted in ascending order by keys
	 * 
	 * Operates in O(n) time.
	 * @return
	 */
	public ArrayList<Pair<T1, T2>> getSortedList() {
		ArrayList<Pair<T1, T2>> sortedList = new ArrayList<Pair<T1, T2>>();
		populateSortedList(root, sortedList);
		return sortedList;
	}
	
	/**
	 * Recursively populates a list by first adding everything left, than itself, than everything right.
	 * @param currNode
	 * @param listToPopulate
	 */
	private void populateSortedList(OSTNode<T1, T2> currNode, ArrayList<Pair<T1, T2>> listToPopulate) {
		// Add those in left subtree if exists
		if (currNode.leftChild != null) {
			populateSortedList(currNode.leftChild, listToPopulate);
		}
		// Add itself
		listToPopulate.add(currNode.toKeyValuePair());
		
		// Add those in right subtree if exists
		if (currNode.rightChild != null) {
			populateSortedList(currNode.rightChild, listToPopulate);
		}
	}
	
	/**
	 * Prints parent-child relationships level by level using breadth first search.
	 */
	public void printTree() {
		
		// Stores pairs of {currLevelInTree, currNode}
		Queue<Pair<Integer, OSTNode<T1, T2>>> bfsQueue = new LinkedList<Pair<Integer, OSTNode<T1, T2>>>();
		bfsQueue.add(new Pair<Integer, OSTNode<T1,T2>>(0, this.root));
		int currLevel = -1;
		System.out.print("Tree printout");
		while (!bfsQueue.isEmpty()) {
			Pair<Integer, OSTNode<T1,T2>> levelAndNode = bfsQueue.poll();
			int thisNodesLevel = levelAndNode.first;
			if (thisNodesLevel > currLevel) { 
				System.out.print("\n");
				System.out.print("Level " + thisNodesLevel + ":\t");
				currLevel = thisNodesLevel;
			} else if (thisNodesLevel != 0) {
				System.out.print(" | ");
			}
			
			OSTNode<T1,T2> currNode = levelAndNode.second;
			System.out.print(currNode);
			if (currNode.leftChild != null) {
				bfsQueue.add(new Pair<Integer, OSTNode<T1,T2>>(thisNodesLevel + 1, currNode.leftChild));
			}
			if (currNode.rightChild != null) {
				bfsQueue.add(new Pair<Integer, OSTNode<T1,T2>>(thisNodesLevel + 1, currNode.rightChild));
			}
		}
	}
}
