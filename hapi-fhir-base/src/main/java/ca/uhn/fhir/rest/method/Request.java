package ca.uhn.fhir.rest.method;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;

public class Request {

	private IdDt myId;
	private String myOperation;
	private Map<String, String[]> myParameterNames;
	private RequestType myRequestType;
	private String myResourceName;
	private IdDt myVersion;

	public IdDt getId() {
		return myId;
	}

	public String getOperation() {
		return myOperation;
	}

	public Map<String, String[]> getParameterNames() {
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

	public void setParameterNames(Map<String, String[]> theParams) {
		myParameterNames = theParams;
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

	public static Request withResourceAndParams(String theResourceName, RequestType theRequestType, Set<String> theParamNames) {
		Request retVal = new Request();
		retVal.setResourceName(theResourceName);
		retVal.setRequestType(theRequestType);
		Map<String, String[]> paramNames = new HashMap<String, String[]>();
		for (String next : theParamNames) {
			paramNames.put(next, new String[0]);
		}
		retVal.setParameterNames(paramNames);
		return retVal;
	}

}
