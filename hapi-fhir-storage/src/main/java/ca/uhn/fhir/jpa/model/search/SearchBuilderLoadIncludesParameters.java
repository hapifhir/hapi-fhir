package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchBuilderLoadIncludesParameters<T extends IResourcePersistentId> {

	private FhirContext myFhirContext;

	private EntityManager myEntityManager;

	/**
	 * A collection of already obtained PIDs
	 */
	private Collection<T> myMatches;

	/**
	 * A collection of fhirpaths to include in the search.
	 * Used to search for resources recursively.
	 */
	private Collection<Include> myIncludeFilters;

	private boolean myReverseMode;

	private DateRangeParam myLastUpdated;

	private String mySearchIdOrDescription;

	private RequestDetails myRequestDetails;

	private Integer myMaxCount;

	/**
	 * List of resource types of interest.
	 * If specified, only these resource types are returned.
	 *
	 * This may have performance issues as TARGET_RESOURCE_TYPE is not
	 * an indexed field.
	 *
	 * Use sparingly
	 */
	private List<String> myDesiredResourceTypes;

	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	public void setFhirContext(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public EntityManager getEntityManager() {
		return myEntityManager;
	}

	public void setEntityManager(EntityManager theEntityManager) {
		myEntityManager = theEntityManager;
	}

	public Collection<T> getMatches() {
		if (myMatches == null) {
			myMatches = new ArrayList<>();
		}
		return myMatches;
	}

	public void setMatches(Collection<T> theMatches) {
		myMatches = theMatches;
	}

	public Collection<Include> getIncludeFilters() {
		return myIncludeFilters;
	}

	public void setIncludeFilters(Collection<Include> theIncludeFilters) {
		myIncludeFilters = theIncludeFilters;
	}

	public boolean isReverseMode() {
		return myReverseMode;
	}

	public void setReverseMode(boolean theReverseMode) {
		myReverseMode = theReverseMode;
	}

	public DateRangeParam getLastUpdated() {
		return myLastUpdated;
	}

	public void setLastUpdated(DateRangeParam theLastUpdated) {
		myLastUpdated = theLastUpdated;
	}

	public String getSearchIdOrDescription() {
		return mySearchIdOrDescription;
	}

	public void setSearchIdOrDescription(String theSearchIdOrDescription) {
		mySearchIdOrDescription = theSearchIdOrDescription;
	}

	public RequestDetails getRequestDetails() {
		return myRequestDetails;
	}

	public void setRequestDetails(RequestDetails theRequestDetails) {
		myRequestDetails = theRequestDetails;
	}

	public Integer getMaxCount() {
		return myMaxCount;
	}

	public void setMaxCount(Integer theMaxCount) {
		myMaxCount = theMaxCount;
	}

	public List<String> getDesiredResourceTypes() {
		return myDesiredResourceTypes;
	}

	public void setDesiredResourceTypes(List<String> theDesiredResourceTypes) {
		myDesiredResourceTypes = theDesiredResourceTypes;
	}
}
