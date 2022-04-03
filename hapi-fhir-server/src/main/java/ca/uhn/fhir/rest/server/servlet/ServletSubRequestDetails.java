package ca.uhn.fhir.rest.server.servlet;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServletSubRequestDetails extends ServletRequestDetails {

	private final ServletRequestDetails myWrap;
	private Map<String, List<String>> myHeaders = new HashMap<>();

	/**
	 * Constructor
	 *
	 * @param theRequestDetails The parent request details
	 */
	public ServletSubRequestDetails(ServletRequestDetails theRequestDetails) {
		super(theRequestDetails.getInterceptorBroadcaster());

		myWrap = theRequestDetails;

		if (theRequestDetails != null) {
			Map<String, List<String>> headers = theRequestDetails.getHeaders();
			for (Map.Entry<String, List<String>> next : headers.entrySet()) {
				myHeaders.put(next.getKey().toLowerCase(), next.getValue());
			}
		}
	}

	@Override
	public HttpServletRequest getServletRequest() {
		return myWrap.getServletRequest();
	}

	@Override
	public HttpServletResponse getServletResponse() {
		return myWrap.getServletResponse();
	}

	public void addHeader(String theName, String theValue) {
		String lowerCase = theName.toLowerCase();
		List<String> list = myHeaders.computeIfAbsent(lowerCase, k -> new ArrayList<>());
		list.add(theValue);
	}

	@Override
	public String getHeader(String theName) {
		List<String> list = myHeaders.get(theName.toLowerCase());
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<String> getHeaders(String theName) {
		List<String> list = myHeaders.get(theName.toLowerCase());
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list;
	}

	@Override
	public Map<Object, Object> getUserData() {
		return myWrap.getUserData();
	}

	@Override
	public boolean isSubRequest() {
		return true;
	}

}
