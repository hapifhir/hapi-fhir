package ca.uhn.fhir.rest.api.server;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collections;
import java.util.List;

public class SimplePreResourceAccessDetails implements IPreResourceAccessDetails {

	private final List<IBaseResource> myResources;
	private final boolean[] myBlocked;

	public SimplePreResourceAccessDetails(IBaseResource theResource) {
		this(Collections.singletonList(theResource));
	}

	public SimplePreResourceAccessDetails(List<IBaseResource> theResources) {
		myResources = theResources;
		myBlocked = new boolean[myResources.size()];
	}

	@Override
	public int size() {
		return myResources.size();
	}

	@Override
	public IBaseResource getResource(int theIndex) {
		return myResources.get(theIndex);
	}

	@Override
	public void setDontReturnResourceAtIndex(int theIndex) {
		myBlocked[theIndex] = true;
	}

	public boolean isDontReturnResourceAtIndex(int theIndex) {
		return myBlocked[theIndex];
	}
}
