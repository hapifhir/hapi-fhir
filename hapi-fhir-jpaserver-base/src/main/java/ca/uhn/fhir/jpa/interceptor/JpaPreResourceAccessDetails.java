package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.util.ICallable;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * THIS CLASS IS NOT THREAD SAFE
 */
@NotThreadSafe
public class JpaPreResourceAccessDetails implements IPreResourceAccessDetails {

	private final List<Long> myResourcePids;
	private final boolean[] myBlocked;
	private final ICallable<ISearchBuilder> mySearchBuilderSupplier;
	private List<IBaseResource> myResources;

	public JpaPreResourceAccessDetails(List<Long> theResourcePids, ICallable<ISearchBuilder> theSearchBuilderSupplier) {
		myResourcePids = theResourcePids;
		myBlocked = new boolean[myResourcePids.size()];
		mySearchBuilderSupplier = theSearchBuilderSupplier;
	}

	@Override
	public int size() {
		return myResourcePids.size();
	}

	@Override
	public IBaseResource getResource(int theIndex) {
		if (myResources == null) {
			myResources = new ArrayList<>(myResourcePids.size());
			// FIXME: JA don't call interceptors for this query
			mySearchBuilderSupplier.call().loadResourcesByPid(myResourcePids, Collections.emptySet(), myResources, false, null);
		}
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
