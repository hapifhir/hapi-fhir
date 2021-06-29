package ca.uhn.fhir.interceptor.model;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import org.hl7.fhir.instance.model.api.IIdType;

public class ReadPartitionIdRequestDetails {

	private final String myResourceType;
	private final RestOperationTypeEnum myRestOperationType;
	private final IIdType myReadResourceId;
	private final Object mySearchParams;

	public ReadPartitionIdRequestDetails(String theResourceType, RestOperationTypeEnum theRestOperationType, IIdType theReadResourceId, Object theSearchParams) {
		myResourceType = theResourceType;
		myRestOperationType = theRestOperationType;
		myReadResourceId = theReadResourceId;
		mySearchParams = theSearchParams;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public RestOperationTypeEnum getRestOperationType() {
		return myRestOperationType;
	}

	public IIdType getReadResourceId() {
		return myReadResourceId;
	}

	public Object getSearchParams() {
		return mySearchParams;
	}

	public static ReadPartitionIdRequestDetails forRead(String theResourceType, IIdType theId) {
		return new ReadPartitionIdRequestDetails(theResourceType, RestOperationTypeEnum.READ, theId.withResourceType(theResourceType), null);
	}

	public static ReadPartitionIdRequestDetails forSearchType(String theResourceType, Object theParams) {
		return new ReadPartitionIdRequestDetails(theResourceType, RestOperationTypeEnum.SEARCH_TYPE, null, theParams);
	}

	public static ReadPartitionIdRequestDetails forHistory(String theResourceType, IIdType theIdType) {
		RestOperationTypeEnum restOperationTypeEnum;
		if (theIdType != null) {
			restOperationTypeEnum = RestOperationTypeEnum.HISTORY_INSTANCE;
		} else if (theResourceType != null) {
			restOperationTypeEnum = RestOperationTypeEnum.HISTORY_TYPE;
		} else {
			restOperationTypeEnum = RestOperationTypeEnum.HISTORY_SYSTEM;
		}
		return new ReadPartitionIdRequestDetails(theResourceType, restOperationTypeEnum, theIdType, null);
	}
}
