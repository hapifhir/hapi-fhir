/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.servlet;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

public class ServletSubRequestDetails extends ServletRequestDetails {

	private final ServletRequestDetails myWrap;
	/**
	 * Map with case-insensitive keys
	 */
	private final ListMultimap<String, String> myHeaders = MultimapBuilder.treeKeys(String.CASE_INSENSITIVE_ORDER)
			.arrayListValues()
			.build();

	/**
	 * Constructor
	 *
	 * @param theRequestDetails The parent request details
	 */
	public ServletSubRequestDetails(@Nonnull ServletRequestDetails theRequestDetails) {
		super(theRequestDetails.getInterceptorBroadcaster());

		myWrap = theRequestDetails;

		Map<String, List<String>> headers = theRequestDetails.getHeaders();
		for (Map.Entry<String, List<String>> next : headers.entrySet()) {
			myHeaders.putAll(next.getKey(), next.getValue());
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

	@Override
	public void addHeader(String theName, String theValue) {
		myHeaders.put(theName, theValue);
	}

	@Override
	public String getHeader(String theName) {
		List<String> list = myHeaders.get(theName);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<String> getHeaders(String theName) {
		List<String> list = myHeaders.get(theName.toLowerCase());
		if (list.isEmpty()) {
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
