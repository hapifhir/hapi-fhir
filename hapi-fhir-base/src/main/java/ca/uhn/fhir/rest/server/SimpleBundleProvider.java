package ca.uhn.fhir.rest.server;

import java.util.Collections;
import java.util.List;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.InstantDt;

public class SimpleBundleProvider implements IBundleProvider {

	private List<IResource> myList;
	
	public SimpleBundleProvider(List<IResource> theList) {
		myList = theList;
	}

	public SimpleBundleProvider(IResource theResource) {
		myList = Collections.singletonList(theResource);
	}

	/**
	 * Create an empty bundle
	 */
	public SimpleBundleProvider() {
		myList = Collections.emptyList();
	}

	@Override
	public List<IResource> getResources(int theFromIndex, int theToIndex) {
		return myList.subList(theFromIndex, Math.min(theToIndex, myList.size()));
	}

	@Override
	public int size() {
		return myList.size();
	}

	@Override
	public InstantDt getPublished() {
		return InstantDt.withCurrentTime();
	}
	
}
