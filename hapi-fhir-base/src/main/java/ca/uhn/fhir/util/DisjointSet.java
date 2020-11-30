package ca.uhn.fhir.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.istack.NotNull;

public class DisjointSet<T> {

	private final Map<T, Node<T>> theNodeMap = new HashMap<>();

	public DisjointSet(@NotNull List<List<T>> theSrcGrpList) {
		
		//-- init
		for (List<T> theSrcGrp : theSrcGrpList) {
			for (T theItem : theSrcGrp) {
				makeSet(theItem); 
			}
		}
		
		//-- union 
		int size = 0;
		for (List<T> theSrcGrp : theSrcGrpList) {
			
			size = theSrcGrp.size();
			if (size == 1)
				continue;
			for (int i=1; i<size; i++) {
				union(theSrcGrp.get(0), theSrcGrp.get(i));
			}
		}
	}
	
	public List<List<T>> getNewGrp() {
		
		Map<T, List<T>> theNewGrpMap = new HashMap<>();
		
		Collection<T> nodes = theNodeMap.keySet();
		for (T t : nodes) {
			
			T root = findSet(t);
			
			List<T> theGrp = theNewGrpMap.get(root);
			if (theGrp == null) {
				theGrp = new ArrayList<>();
				theGrp.add(t);
				theNewGrpMap.put(root, theGrp);
			} else {
				theGrp.add(t);
			}
		}
		
		return new ArrayList<List<T>>(theNewGrpMap.values());
	}
	
	private T findSet(T t) {
		DisjointSet.Node<T> node = (DisjointSet.Node<T>) theNodeMap.get(t);
		if (node == null)
			return null;
		if (!t.equals(node.parent))
			node.parent = findSet(node.parent);
		return node.parent;
	}

	private void makeSet(T t) {
		theNodeMap.put(t, new Node<T>(t, 0));
	}

	private void union(T a, T b) {
		
		T setA = findSet(a);
		T setB = findSet(b);
		if (setA == null || setB == null || setA.equals(setB))
			return;
		
		DisjointSet.Node<T> nodeA = (DisjointSet.Node<T>) theNodeMap.get(setA);
		DisjointSet.Node<T> nodeB = (DisjointSet.Node<T>) theNodeMap.get(setB);
		if (nodeA.rank > nodeB.rank) {
			nodeB.parent = a;
		} else {
			nodeA.parent = b;
			if (nodeA.rank == nodeB.rank)
				nodeB.rank++;
		}
	}
	
	private static class Node<T> {
		int rank;
		T parent;

		Node(T parent, int rank) {
			this.parent = parent;
			this.rank = rank;
		}
	}
	
}
