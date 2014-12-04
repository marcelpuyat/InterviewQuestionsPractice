import helpers.Pair;

/**
 * My implementation of a splay tree that still allows for selection (getRank and getKthOrder), while
 * pushing inserted nodes and searched nodes to the root through a series of rotations in log(height) time.
 * 
 * Note that this implementation does not splay on deletions and on searches that don't find a node.
 * @author marcelp
 *
 * @param <T1>
 * @param <T2>
 */
public class OrderedSplayTree<T1 extends Comparable<T1>, T2> extends OrderStatsTree<T1, T2> {
	
	/**
	 * Inserts in O(height) time and pushes inserted note to root.
	 */
	@Override
	public boolean insert(T1 key, T2 value) {
		Pair<Boolean, OSTNode<T1, T2>> insertionResult = super.insertAndReturnNode(key, value);
		splay(insertionResult.second);
		return insertionResult.first;
	}
	
	/**
	 * Searches in O(height) time and pushes found node to root, or returns null and does not push
	 * any node to root.
	 */
	@Override
	public T2 get(T1 key) { 
		OSTNode<T1, T2> node = search(root, key);
		if (node != null) {
			splay(node);
			return node.value;
		}
		return null;
	}
	
	/**
	 * Performs a series of rotations in O(log(height)) time until node is the root.
	 * @param node
	 */
	private void splay(OSTNode<T1, T2> node) {
		while (node != root) {
			// 5 cases. Note that the rotate function takes care of updating sizes of subtrees.

			// 1st case: Node's parent is root. 
			if (node.parent == this.root) {
				
				// Perform correct rotation depending on which child this node is of the root
				if (root.leftChild == node) {
					rotateAsLeftChild(node);
				} else {
					rotateAsRightChild(node);
				}
				this.root = node;
			}
			
			// 2nd case: Node is a leftChild of its parent, and its parent is a left child of its parent.
			else if (node.parent.leftChild == node && node.parent.parent.leftChild == node.parent) {
				if (node.parent.parent == root) {
					root = node;
				}
				// We first rotate the parent upward, and then rotate the node above its parent
				rotateAsLeftChild(node.parent);
				rotateAsLeftChild(node);
			}
			
			// 3rd case: Node is a rightChild of its parent, and its parent is a right child of its parent.
			else if (node.parent.rightChild == node && node.parent.parent.rightChild == node.parent) {
				if (node.parent.parent == root) {
					root = node;
				}
				// We first rotate the parent upward, and then rotate the node above its parent
				rotateAsRightChild(node.parent);
				rotateAsRightChild(node);
			}
			
			// 4th case: Node is a leftChild of its parent, and its parent is a rightChild of its parent.
			else if (node.parent.leftChild == node && node.parent.parent.rightChild == node.parent) {
				if (node.parent.parent == root) {
					root = node;
				}
				// We rotate node upward twice.
				rotateAsLeftChild(node);
				rotateAsRightChild(node);
			}
			
			// 5th case: Node is a rightChild of its parent, and its parent is a leftChild of its parent.
			else if (node.parent.rightChild == node && node.parent.parent.leftChild == node.parent) {
				if (node.parent.parent == root) {
					root = node;
				}
				// We rotate node upward twice.
				rotateAsRightChild(node);
				rotateAsLeftChild(node);
			}
		}
	}
	
	/**
	 * Rotation code is duplicated for rotation as left child and right
	 * child for clarity and to avoid confusion with too many conditions.
	 * @param node
	 */
	private void rotateAsLeftChild(OSTNode<T1, T2> node) { 
		// This node's right child becomes its parents left child
		node.parent.leftChild = node.rightChild;
		if (node.rightChild != null)
			node.rightChild.parent = node.parent;
		
		// The node's parent becomes its right child
		node.rightChild = node.parent;
		
		// Node becomes parent of its former parent, and inherits parent of its former parent
		OSTNode<T1, T2> formerGrandparent = node.parent.parent;
		if (formerGrandparent != null) {
			if (node.parent == formerGrandparent.rightChild) {
				formerGrandparent.rightChild = node;
			} else {
				formerGrandparent.leftChild = node;
			}
		}
		node.parent.parent = node;
		node.parent = formerGrandparent;
		
		// Lastly, the subtree size of old parent will no longer include this node, as well as the size of the
		// node's left child. And the subtree size of this node will include its old parent, as well as the 
		// old parent's rightChild size.
		node.rightChild.sizeOfSubtree -= 1;
		if (node.leftChild != null) {
			node.rightChild.sizeOfSubtree -= node.leftChild.sizeOfSubtree;
		}
		node.sizeOfSubtree += 1;
		if (node.rightChild.rightChild != null) {
			node.sizeOfSubtree += node.rightChild.rightChild.sizeOfSubtree;
		}
	}
	
	/**
	 * Rotation code is duplicated for rotation as left child and right
	 * child for clarity and to avoid confusion with too many conditions.
	 * @param node
	 */
	private void rotateAsRightChild(OSTNode<T1, T2> node) { 
		// This node's left child becomes its parents right child
		node.parent.rightChild = node.leftChild;
		if (node.leftChild != null)
			node.leftChild.parent = node.parent;
		
		// The node's parent becomes its left child
		node.leftChild = node.parent;
		
		// Node becomes parent of its former parent, and inherits parent of its former parent
		OSTNode<T1, T2> formerGrandparent = node.parent.parent;
		if (formerGrandparent != null) {
			if (node.parent == formerGrandparent.rightChild) {
				formerGrandparent.rightChild = node;
			} else {
				formerGrandparent.leftChild = node;
			}
		}
		node.parent.parent = node;
		node.parent = formerGrandparent;
		
		// Lastly, the subtree size of old parent will no longer include this node, as well as the size of the
		// node's right child. And the subtree size of this node will include its old parent, as well as the 
		// old parent's leftChild size.
		node.leftChild.sizeOfSubtree -= 1;
		if (node.rightChild != null) {
			node.leftChild.sizeOfSubtree -= node.rightChild.sizeOfSubtree;
		}
		node.sizeOfSubtree += 1;
		if (node.leftChild.leftChild != null) {
			node.sizeOfSubtree += node.leftChild.leftChild.sizeOfSubtree;
		}
	}
}
