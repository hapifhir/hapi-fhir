package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;

/**
 * This class is internal to HAPI - Use with caution as methods may change in future versions of the library
 */
public class Request extends RequestDetails {

	private String myFhirServerBase;
	private String myOperation;
	private RequestType myRequestType;
	private boolean myRespondGzip;
	private String mySecondaryOperation;
	private HttpServletRequest myServletRequest;
	private HttpServletResponse myServletResponse;
	private Map<String, List<String>> myUnqualifiedToQualifiedNames;

	public String getFhirServerBase() {
		return myFhirServerBase;
	}

	public String getOperation() {
		return myOperation;
	}

	public RequestType getRequestType() {
		return myRequestType;
	}


	public String getSecondaryOperation() {
		return mySecondaryOperation;
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

	public void setFhirServerBase(String theFhirServerBase) {
		myFhirServerBase = theFhirServerBase;
	}

	public void setOperation(String theOperation) {
		myOperation = theOperation;
	}

	@Override
	public void setParameters(Map<String, String[]> theParams) {
		super.setParameters(theParams);

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

	public void setRequestType(RequestType theRequestType) {
		myRequestType = theRequestType;
	}


	public void setRespondGzip(boolean theRespondGzip) {
		myRespondGzip = theRespondGzip;
	}

	public void setSecondaryOperation(String theSecondaryOperation) {
		mySecondaryOperation = theSecondaryOperation;
	}

	public void setServletRequest(HttpServletRequest theRequest) {
		myServletRequest = theRequest;
	}

	public void setServletResponse(HttpServletResponse theServletResponse) {
		myServletResponse = theServletResponse;
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

}
