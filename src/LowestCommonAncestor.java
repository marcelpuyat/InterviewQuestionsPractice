import helpers.Pair;

/**
 * Finds common ancestor between two nodes (with given keys as parameters) that is deepest in the tree.
 * O(log(n)) running time.
 * @author marcelp
 *
 * @param <T1>
 * @param <T2>
 */
public class LowestCommonAncestor<T1 extends Comparable<T1>, T2> extends OrderStatsTree<T1, T2> {
	
	/**
	 * Returns lowest common ancestor in tree.
	 * If either key1 or key2 is not in the tree, will return null.
	 * 
	 * O(log(n)) running time.
	 * @param key1
	 * @param key2
	 * @return
	 */
	public Pair<T1, T2> getLowestCommonAncestor(T1 key1, T1 key2) {
		
		return findLCARecur(root, key1, key2).toKeyValuePair();
	}
	
	public OSTNode<T1, T2> findLCARecur(OSTNode<T1, T2> node, T1 key1, T1 key2) {
		if (node == null) {
			return null;
		}
		
		/* If both keys are greater than current, then LCA must be in right subtree. */
		if (node.key.compareTo(key1) < 0 && node.key.compareTo(key2) < 0) {
			return findLCARecur(node.rightChild, key1, key2);
		}
		
		/* If both keys are less than current, then LCA must be in left subtree. */
		if (node.key.compareTo(key1) > 0 && node.key.compareTo(key2) > 0) {
			return findLCARecur(node.leftChild, key1, key2);
		}
		
		/* If either is equal to current, or this key is between the two keys, then this is LCA.
		 * But first, check to make sure that both keys are indeed present in tree. */
		OSTNode<T1, T2> nodeWithKey1 = search(node, key1);
		OSTNode<T1, T2> nodeWithKey2 = search(node, key2);
		
		// If either is not in tree, return null
		if (nodeWithKey1 == null || nodeWithKey2 == null) {
			return null;
		}
		
		return node;
	}
	
	public static void main(String[] args) {
		LowestCommonAncestor<Integer, Character> lcaTree = new LowestCommonAncestor<Integer, Character>();
		lcaTree.insert(1, 'A');
		lcaTree.insert(0, 'B');
		lcaTree.insert(2, 'C');
		lcaTree.insert(3, 'D');
		lcaTree.insert(-1, 'E');
		
		/* At this point, the tree should look like:
		 *         1
		 *        (A)
		 *       /   \
		 *      /     \
		 *     0       2
		 *    (B)     (C) 
		 *   /           \
		 *  /             \
		 * -1              3
		 * (E)             (D)  */
		
		System.out.println(lcaTree.getLowestCommonAncestor(0, 2)); // Should print kvp 1->A
		System.out.println(lcaTree.getLowestCommonAncestor(2, 3)); // Should print kvp 2->C
		System.out.println(lcaTree.getLowestCommonAncestor(-1, 0)); // Should print kvp 0->B
		System.out.println(lcaTree.getLowestCommonAncestor(-1, 3)); // Should print kvp 1->A
	}
}
