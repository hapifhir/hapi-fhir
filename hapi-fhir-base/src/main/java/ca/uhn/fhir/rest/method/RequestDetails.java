package ca.uhn.fhir.rest.method;

import java.util.Map;

import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;

public class RequestDetails {

	private IdDt myId;
	private OtherOperationTypeEnum myOtherOperationType;
	private Map<String, String[]> myParameters;
	private String myResourceName;
	private RestfulOperationTypeEnum myResourceOperationType;
	private RestfulOperationSystemEnum mySystemOperationType;

	public IdDt getId() {
		return myId;
	}

	public OtherOperationTypeEnum getOtherOperationType() {
		return myOtherOperationType;
	}

	public Map<String, String[]> getParameters() {
		return myParameters;
	}

	public String getResourceName() {
		return myResourceName;
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

	public void setOtherOperationType(OtherOperationTypeEnum theOtherOperationType) {
		myOtherOperationType = theOtherOperationType;
	}

	public void setParameters(Map<String, String[]> theParams) {
		myParameters = theParams;
	}

	public void setResourceName(String theResourceName) {
		myResourceName = theResourceName;
	}

	public void setResourceOperationType(RestfulOperationTypeEnum theResourceOperationType) {
		myResourceOperationType = theResourceOperationType;
	}

	public void setSystemOperationType(RestfulOperationSystemEnum theSystemOperationType) {
		mySystemOperationType = theSystemOperationType;
	}

}
