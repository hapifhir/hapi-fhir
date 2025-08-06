/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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

/**
 * This class wraps a {@link ServletRequestDetails} object for
 * processing sub-requests, such as processing individual
 * entries in a transaction or batch bundle. An instance of this class is used for modifying some of the data
 * in the request details, such as the request headers, for an individual entry,
 * without affecting the original ServletRequestDetails.
 */
public class ServletSubRequestDetails extends ServletRequestDetails {

	private final ServletRequestDetails myWrap;

	/**
	 * Map with case-insensitive keys
	 * This map contains only the headers modified by the user after this object is created.
	 * If a header is not modified, the original value from the wrapped RequestDetails is returned by the
	 * getters in this class.
	 * <p>
	 * The reason for implementing the map this way, which is just keeping track of the overrides, as opposed
	 * to creating a copy of the header map of the wrapped RequestDetails at the time this object is created,
	 * is that there some test code where the header values are stubbed for the wrapped details using Mockito
	 * like `when(requestDetails.getHeader("headerName")).thenReturn("headerValue")`.
	 * Creating a copy of the headers by iterating the map of the wrapped instance wouldn't satisfy such stubbing,
	 * the stubbed values are not actually in the map.
	 * For stubbing to work we have to make a call the getHeader method of the wrapped RequestDetails.
	 * This is what the getters in this class do.
	 */
	private final ListMultimap<String, String> myHeaderOverrides = MultimapBuilder.treeKeys(
					String.CASE_INSENSITIVE_ORDER)
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
		myHeaderOverrides.put(theName, theValue);
	}

	@Override
	public String getHeader(String theName) {
		List<String> list = myHeaderOverrides.get(theName);
		if (list.isEmpty()) {
			return myWrap.getHeader(theName);
		}
		return list.get(0);
	}

	@Override
	public List<String> getHeaders(String theName) {
		List<String> list = myHeaderOverrides.get(theName.toLowerCase());
		if (list.isEmpty()) {
			return myWrap.getHeaders(theName);
		}
		return list;
	}

	@Override
	public void setHeaders(String theName, List<String> theValues) {
		myHeaderOverrides.removeAll(theName);
		myHeaderOverrides.putAll(theName, theValues);
	}

	@Override
	public @Nonnull Map<Object, Object> getUserData() {
		return myWrap.getUserData();
	}

	@Override
	public boolean isSubRequest() {
		return true;
	}
}
