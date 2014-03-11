package ca.uhn.fhir.rest.common;

import java.util.Set;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.common.SearchMethodBinding.RequestType;

public class Request {

	private IdDt myId;
	private String myOperation;
	private Set<String> myParameterNames;
	private RequestType myRequestType;
	private String myResourceName;

	private IdDt myVersion;

	public IdDt getId() {
		return myId;
	}

	public String getOperation() {
		return myOperation;
	}

	public Set<String> getParameterNames() {
		return myParameterNames;
	}

	public RequestType getRequestType() {
		return myRequestType;
	}

	public String getResourceName() {
		return myResourceName;
	}

	public IdDt getVersion() {
		return myVersion;
	}

	public void setId(IdDt theId) {
		myId = theId;
	}

	public void setOperation(String theOperation) {
		myOperation = theOperation;
	}

	public void setParameterNames(Set<String> theParameterNames) {
		myParameterNames = theParameterNames;
	}

	public void setRequestType(RequestType theRequestType) {
		myRequestType = theRequestType;
	}

	public void setResourceName(String theResourceName) {
		myResourceName = theResourceName;
	}

	public void setVersion(IdDt theVersion) {
		myVersion = theVersion;
	}

	public static Request withResourceAndParams(String theResourceName, RequestType theRequestType, Set<String> theParamNamess) {
		Request retVal = new Request();
		retVal.setResourceName(theResourceName);
		retVal.setParameterNames(theParamNamess);
		retVal.setRequestType(theRequestType);
		return retVal;
	}

}
