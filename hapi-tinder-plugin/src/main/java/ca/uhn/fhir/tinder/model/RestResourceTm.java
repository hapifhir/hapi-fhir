package ca.uhn.fhir.tinder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;

import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResourceOperation;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;

public class RestResourceTm {

	private String myResourceType;
	private Map<RestfulOperationTypeEnum, RestResourceOperationTm> mySupportedOperations = new HashMap<RestfulOperationTypeEnum, RestResourceOperationTm>();
	private ArrayList<SearchParameter> mySearchParams;

	public RestResourceTm(RestResource theResource) {
		myResourceType = theResource.getType().getValue();
		Validate.notNull(myResourceType, "Found a resource definition in Conformance resource with no type (or an invalid type) specified: " + theResource.getType().getValue());

		for (RestResourceOperation next : theResource.getOperation()) {
			mySupportedOperations.put(next.getCode().getValueAsEnum(), new RestResourceOperationTm(next));
		}
	}

	public RestResourceOperationTm getReadOperation() {
		return mySupportedOperations.get(RestfulOperationTypeEnum.READ);
	}

	public RestResourceOperationTm getVReadOperation() {
		return mySupportedOperations.get(RestfulOperationTypeEnum.VREAD);
	}

	public RestResourceOperationTm getSearchOperation() {
		return mySupportedOperations.get(RestfulOperationTypeEnum.SEARCH_TYPE);
	}

	public boolean isHasReadOperation() {
		return mySupportedOperations.containsKey(RestfulOperationTypeEnum.READ);
	}

	public boolean isHasVReadOperation() {
		return mySupportedOperations.containsKey(RestfulOperationTypeEnum.VREAD);
	}

	public boolean isHasSearchOperation() {
		return mySupportedOperations.containsKey(RestfulOperationTypeEnum.SEARCH_TYPE);
	}

	public String getResourceType() {
		return myResourceType;
	}

	public List<SearchParameter> getSearchParams() {
		if (mySearchParams == null) {
			mySearchParams = new ArrayList<SearchParameter>();
		}
		return mySearchParams;
	}

}
