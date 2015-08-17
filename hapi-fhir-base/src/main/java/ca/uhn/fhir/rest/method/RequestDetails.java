package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.RestfulServer;

public class RequestDetails {

	private String myCompartmentName;
	private String myCompleteUrl;
	private String myFhirServerBase;
	private IdDt myId;
	private String myOperation;
	private Map<String, String[]> myParameters;
	private byte[] myRawRequest;
	private String myRequestPath;
	private RequestTypeEnum myRequestType;
	private String myResourceName;
	private boolean myRespondGzip;
	private RestOperationTypeEnum myRestOperationType;
	private String mySecondaryOperation;
	private RestfulServer myServer;
	private HttpServletRequest myServletRequest;
	private HttpServletResponse myServletResponse;
	private Map<String, List<String>> myUnqualifiedToQualifiedNames;

	public String getCompartmentName() {
		return myCompartmentName;
	}

	public String getCompleteUrl() {
		return myCompleteUrl;
	}

	public String getFhirServerBase() {
		return myFhirServerBase;
	}

	public IdDt getId() {
		return myId;
	}

	public String getOperation() {
		return myOperation;
	}

	public Map<String, String[]> getParameters() {
		return myParameters;
	}

	public byte[] getRawRequest() {
		return myRawRequest;
	}

	/**
	 * The part of the request URL that comes after the server base.
	 * <p>
	 * Will not contain a leading '/'
	 * </p>
	 */
	public String getRequestPath() {
		return myRequestPath;
	}

	public RequestTypeEnum getRequestType() {
		return myRequestType;
	}

	public String getResourceName() {
		return myResourceName;
	}

	public RestOperationTypeEnum getRestOperationType() {
		return myRestOperationType;
	}

	public String getSecondaryOperation() {
		return mySecondaryOperation;
	}

	public RestfulServer getServer() {
		return myServer;
	}

	public HttpServletRequest getServletRequest() {
		return myServletRequest;
	}

	public HttpServletResponse getServletResponse() {
		return myServletResponse;
	}

	public Map<String, List<String>> getUnqualifiedToQualifiedNames() {
		return myUnqualifiedToQualifiedNames;
	}

	public boolean isRespondGzip() {
		return myRespondGzip;
	}

	public void setCompartmentName(String theCompartmentName) {
		myCompartmentName = theCompartmentName;
	}

	public void setCompleteUrl(String theCompleteUrl) {
		myCompleteUrl = theCompleteUrl;
	}

	public void setFhirServerBase(String theFhirServerBase) {
		myFhirServerBase = theFhirServerBase;
	}

	public void setId(IdDt theId) {
		myId = theId;
	}

	public void setOperation(String theOperation) {
		myOperation = theOperation;
	}

	public void setParameters(Map<String, String[]> theParams) {
		myParameters = theParams;

		for (String next : theParams.keySet()) {
			for (int i = 0; i < next.length(); i++) {
				char nextChar = next.charAt(i);
				if (nextChar == ':' || nextChar == '.') {
					if (myUnqualifiedToQualifiedNames == null) {
						myUnqualifiedToQualifiedNames = new HashMap<String, List<String>>();
					}
					String unqualified = next.substring(0, i);
					List<String> list = myUnqualifiedToQualifiedNames.get(unqualified);
					if (list == null) {
						list = new ArrayList<String>(4);
						myUnqualifiedToQualifiedNames.put(unqualified, list);
					}
					list.add(next);
					break;
				}
			}
		}

		if (myUnqualifiedToQualifiedNames == null) {
			myUnqualifiedToQualifiedNames = Collections.emptyMap();
		}

	}

	public void setRawRequest(byte[] theRawRequest) {
		myRawRequest = theRawRequest;
	}

	public void setRequestPath(String theRequestPath) {
		assert theRequestPath.length() == 0 || theRequestPath.charAt(0) != '/';
		myRequestPath = theRequestPath;
	}

	public void setRequestType(RequestTypeEnum theRequestType) {
		myRequestType = theRequestType;
	}

	public void setResourceName(String theResourceName) {
		myResourceName = theResourceName;
	}

	public void setRespondGzip(boolean theRespondGzip) {
		myRespondGzip = theRespondGzip;
	}

	public void setRestOperationType(RestOperationTypeEnum theRestOperationType) {
		myRestOperationType = theRestOperationType;
	}

	public void setSecondaryOperation(String theSecondaryOperation) {
		mySecondaryOperation = theSecondaryOperation;
	}

	public void setServer(RestfulServer theServer) {
		myServer = theServer;
	}

	public void setServletRequest(HttpServletRequest theRequest) {
		myServletRequest = theRequest;
	}

	public void setServletResponse(HttpServletResponse theServletResponse) {
		myServletResponse = theServletResponse;
	}

	public static RequestDetails withResourceAndParams(String theResourceName, RequestTypeEnum theRequestType, Set<String> theParamNames) {
		RequestDetails retVal = new RequestDetails();
		retVal.setResourceName(theResourceName);
		retVal.setRequestType(theRequestType);
		Map<String, String[]> paramNames = new HashMap<String, String[]>();
		for (String next : theParamNames) {
			paramNames.put(next, new String[0]);
		}
		retVal.setParameters(paramNames);
		return retVal;
	}

}
