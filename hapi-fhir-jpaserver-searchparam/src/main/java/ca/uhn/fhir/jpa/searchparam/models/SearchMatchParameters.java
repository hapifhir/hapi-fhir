package ca.uhn.fhir.jpa.searchparam.models;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class SearchMatchParameters {
	/**
	 * Query parameters
	 */
	private String myCriteria;
	/**
	 * The base resource that this search should be returning.
	 */
	private IBaseResource myBaseResource;

	private SearchParameterMap mySearchParameterMap;
	private ResourceIndexedSearchParams myIndexedSearchParams;
	private boolean myRequiresResourceIndexedSearchParams = true; // default
	private RequestDetails myRequestDetails;

	private RuntimeResourceDefinition myRuntimeResourceDefinition;

	private boolean myAllowChainSearches = false; // default

	public void setCriteria(String theCriteria) {
		myCriteria = theCriteria;
	}

	public void setBaseResource(IBaseResource theBaseResource) {
		myBaseResource = theBaseResource;
	}

	public String getCriteria() {
		return myCriteria;
	}

	public IBaseResource getBaseResource() {
		return myBaseResource;
	}

	public ResourceIndexedSearchParams getIndexedSearchParams() {
		return myIndexedSearchParams;
	}

	public void setIndexedSearchParams(ResourceIndexedSearchParams theIndexedSearchParams) {
		myIndexedSearchParams = theIndexedSearchParams;
	}

	public RequestDetails getRequestDetails() {
		return myRequestDetails;
	}

	public void setRequestDetails(RequestDetails theRequestDetails) {
		myRequestDetails = theRequestDetails;
	}

	public SearchParameterMap getSearchParameterMap() {
		return mySearchParameterMap;
	}

	public void setSearchParameterMap(SearchParameterMap theSearchParameterMap) {
		mySearchParameterMap = theSearchParameterMap;
	}

	public RuntimeResourceDefinition getRuntimeResourceDefinition() {
		return myRuntimeResourceDefinition;
	}

	public void setRuntimeResourceDefinition(RuntimeResourceDefinition theRuntimeResourceDefinition) {
		myRuntimeResourceDefinition = theRuntimeResourceDefinition;
	}

	public boolean isRequiresResourceIndexedSearchParams() {
		return myRequiresResourceIndexedSearchParams;
	}

	public void setRequiresResourceIndexedSearchParams(boolean theRequiresResourceIndexedSearchParams) {
		myRequiresResourceIndexedSearchParams = theRequiresResourceIndexedSearchParams;
	}

	public boolean isAllowChainSearches() {
		return myAllowChainSearches;
	}

	public void setAllowChainSearches(boolean theAllowChainSearches) {
		myAllowChainSearches = theAllowChainSearches;
	}

	public boolean hasResourceIndexedSearchParameters() {
		return myIndexedSearchParams != null;
	}

	public boolean hasBaseResource() {
		return myBaseResource != null;
	}

	public boolean hasSearchParameterMap() {
		return mySearchParameterMap != null;
	}

	public boolean hasCriteria() {
		return myCriteria != null && !myCriteria.trim().equals("");
	}

	public boolean hasRuntimeResourceDefinition() {
		return myRuntimeResourceDefinition != null;
	}
}
