import helpers.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class ConvertListOfParentChildPairsToTree {
	
	public static void printTreeFromPairs(ArrayList<Pair<String, String>> ancestryPairs) {
		HashMap<String, ArrayList<String>> treeMap = new HashMap<String, ArrayList<String>>();
		HashMap<String, Integer> parentCount = new HashMap<String, Integer>();
		for (Pair<String, String> p : ancestryPairs) {
			if (treeMap.get(p.first) == null) {
				ArrayList<String> newChildList = new ArrayList<String>();
				newChildList.add(p.second);
				treeMap.put(p.first, newChildList);
			} else {
				treeMap.get(p.first).add(p.second);
			}
			parentCount.put(p.second, 1);
		}
		String rootParent = null;
		for (Pair<String, String> p : ancestryPairs) {
			if (parentCount.get(p.first) == null) {
				rootParent = p.first;
				break;
			}
		}
		Queue<Pair<String, Integer>> parentQueue = new LinkedList<Pair<String, Integer>>();
		parentQueue.add(new Pair<String, Integer>(rootParent, 0));
		int currLevel = 0;
		while (!parentQueue.isEmpty()) {
			Pair<String, Integer> pair = parentQueue.poll();
			String parent = pair.first;
			Integer level = pair.second;
			if (level > currLevel) { 
				System.out.print("\n");
				currLevel = level;
			}
			ArrayList<String> childList = treeMap.get(parent);
			if (childList != null) {
				for (String child : childList) {
					System.out.print(child + " ");
					parentQueue.add(new Pair<String, Integer>(child, currLevel + 1));
				}
			}
		}
	}
}
