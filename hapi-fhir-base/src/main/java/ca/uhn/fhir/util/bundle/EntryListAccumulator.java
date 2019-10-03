package ca.uhn.fhir.util.bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EntryListAccumulator implements Consumer<ModifiableBundleEntry> {
	private final List<BundleEntryParts> myList = new ArrayList<>();

	@Override
	public void accept(ModifiableBundleEntry theModifiableBundleEntry) {
		myList.add(theModifiableBundleEntry.getBundleEntryParts());
	}

	public List<BundleEntryParts> getList() {
		return myList;
	}
}
