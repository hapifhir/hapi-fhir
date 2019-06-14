package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.jpa.util.DeleteConflict;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class DeleteConflictList {
	private final List<DeleteConflict> myList = new ArrayList<>();

	public void add(DeleteConflict theDeleteConflict) {
		myList.add(theDeleteConflict);
	}

	public boolean isEmpty() {
		return myList.isEmpty();
	}

	public Iterator<DeleteConflict> iterator() {
		return myList.iterator();
	}

	public boolean removeIf(Predicate<DeleteConflict> theFilter) {
		return myList.removeIf(theFilter);
	}

	public void addAll(DeleteConflictList theNewConflicts) {
		myList.addAll(theNewConflicts.myList);
	}

	public int size() {
		return myList.size();
	}
}
