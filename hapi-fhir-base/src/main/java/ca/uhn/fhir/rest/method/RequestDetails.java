package ca.uhn.fhir.rest.method;

import java.util.Map;

import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;

public class RequestDetails {

	private IdDt myId;
	private Map<String, String[]> myParameters;
	private RestfulOperationTypeEnum myResourceOperationType;
	private RestfulOperationSystemEnum mySystemOperationType;

	public IdDt getId() {
		return myId;
	}

	public Map<String, String[]> getParameters() {
		return myParameters;
	}


	public RestfulOperationTypeEnum getResourceOperationType() {
		return myResourceOperationType;
	}

	public RestfulOperationSystemEnum getSystemOperationType() {
		return mySystemOperationType;
	}

	public void setId(IdDt theId) {
		myId = theId;
	}

	public void setParameters(Map<String, String[]> theParams) {
		myParameters = theParams;
	}


	public void setResourceOperationType(RestfulOperationTypeEnum theResourceOperationType) {
		myResourceOperationType = theResourceOperationType;
	}

	public void setSystemOperationType(RestfulOperationSystemEnum theSystemOperationType) {
		mySystemOperationType = theSystemOperationType;
	}

}
