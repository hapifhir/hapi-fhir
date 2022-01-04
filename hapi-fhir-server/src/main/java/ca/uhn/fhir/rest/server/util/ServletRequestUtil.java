package ca.uhn.fhir.rest.server.util;

/*-
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

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletSubRequestDetails;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.ArrayListMultimap;
import org.apache.http.NameValuePair;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServletRequestUtil {
	public static ServletSubRequestDetails getServletSubRequestDetails(ServletRequestDetails theRequestDetails, String url, ArrayListMultimap<String, String> theParamValues) {
		ServletSubRequestDetails requestDetails = new ServletSubRequestDetails(theRequestDetails);
		requestDetails.setServletRequest(theRequestDetails.getServletRequest());
		requestDetails.setRequestType(RequestTypeEnum.GET);
		requestDetails.setServer(theRequestDetails.getServer());

		int qIndex = url.indexOf('?');
		requestDetails.setParameters(new HashMap<>());
		if (qIndex != -1) {
			String params = url.substring(qIndex);
			List<NameValuePair> parameters = UrlUtil.translateMatchUrl(params);
			for (NameValuePair next : parameters) {
				theParamValues.put(next.getName(), next.getValue());
			}
			for (Map.Entry<String, Collection<String>> nextParamEntry : theParamValues.asMap().entrySet()) {
				String[] nextValue = nextParamEntry.getValue().toArray(new String[nextParamEntry.getValue().size()]);
				requestDetails.addParameter(nextParamEntry.getKey(), nextValue);
			}
			url = url.substring(0, qIndex);
		}

		if (url.length() > 0 && url.charAt(0) == '/') {
			url = url.substring(1);
		}

		requestDetails.setRequestPath(url);
		requestDetails.setFhirServerBase(theRequestDetails.getFhirServerBase());

		theRequestDetails.getServer().populateRequestDetailsFromRequestPath(requestDetails, url);
		return requestDetails;
	}

	public static String extractUrl(ServletRequestDetails theRequestDetails) {
		StringBuilder b = new StringBuilder();
		for (Map.Entry<String, String[]> next : theRequestDetails.getParameters().entrySet()) {
			for (String nextValue : next.getValue()) {
				if (b.length() == 0) {
					b.append('?');
				} else {
					b.append('&');
				}
				b.append(UrlUtil.escapeUrlParam(next.getKey()));
				b.append('=');
				b.append(UrlUtil.escapeUrlParam(nextValue));
			}
		}
		return theRequestDetails.getRequestPath() + b.toString();
	}
}
