package ca.uhn.fhir.rest.method;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.IResourceProvider;

public class Request {

	private String myCompleteUrl;
	private String myFhirServerBase;
	private IdDt myId;
	private Reader myInputReader;
	private String myOperation;
	private Map<String, String[]> myParameters;
	private RequestType myRequestType;
	private String myResourceName;
	private IResourceProvider myResourceProvider;
	private IdDt myVersion;
	private HttpServletRequest myServletRequest;

	public String getCompleteUrl() {
		return myCompleteUrl;
	}

	public String getFhirServerBase() {
		return myFhirServerBase;
	}

	public IdDt getId() {
		return myId;
	}

	public Reader getInputReader() {
		return myInputReader;
	}

	public String getOperation() {
		return myOperation;
	}

	public Map<String, String[]> getParameters() {
		return myParameters;
	}

	public RequestType getRequestType() {
		return myRequestType;
	}

	public String getResourceName() {
		return myResourceName;
	}

	public IResourceProvider getResourceProvider() {
		return myResourceProvider;
	}

	public IdDt getVersion() {
		return myVersion;
	}


	public void setCompleteUrl(String theCompleteUrl) {
		myCompleteUrl=theCompleteUrl;
	}

	public void setFhirServerBase(String theFhirServerBase) {
		myFhirServerBase=theFhirServerBase;
	}

	public void setId(IdDt theId) {
		myId = theId;
	}

	public void setInputReader(Reader theReader) {
		myInputReader=theReader;
	}

	public void setOperation(String theOperation) {
		myOperation = theOperation;
	}

	public void setParameters(Map<String, String[]> theParams) {
		myParameters = theParams;
	}


	public void setRequestType(RequestType theRequestType) {
		myRequestType = theRequestType;
	}

	public void setResourceName(String theResourceName) {
		myResourceName = theResourceName;
	}

	public void setResourceProvider(IResourceProvider theResourceProvider) {
		myResourceProvider=theResourceProvider;
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
		retVal.setParameters(paramNames);
		return retVal;
	}

	public void setServletRequest(HttpServletRequest theRequest) {
		myServletRequest=theRequest;
	}

	public HttpServletRequest getServletRequest() {
		return myServletRequest;
	}

}
