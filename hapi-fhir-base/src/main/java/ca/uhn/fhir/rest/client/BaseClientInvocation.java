package ca.uhn.fhir.rest.client;

/*
 * #%L
 * HAPI FHIR Library
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.methods.HttpRequestBase;

public abstract class BaseClientInvocation {

	/**
	 * Create an HTTP request out of this client request
	 * 
	 * @param theUrlBase
	 *            The FHIR server base url (with a trailing "/")
	 * @param theExtraParams
	 *            Any extra request parameters the server wishes to add
	 */
	public abstract HttpRequestBase asHttpRequest(String theUrlBase, Map<String, List<String>> theExtraParams);

	protected static void appendExtraParamsWithQuestionMark(Map<String, List<String>> theExtraParams, StringBuilder theUrlBuilder, boolean theWithQuestionMark)  {
		boolean first = theWithQuestionMark;

		if (theExtraParams != null && theExtraParams.isEmpty() == false) {
			for (Entry<String, List<String>> next : theExtraParams.entrySet()) {
				for (String nextValue : next.getValue()) {
					if (first) {
						theUrlBuilder.append('?');
						first = false;
					} else {
						theUrlBuilder.append('&');
					}
					try {
						theUrlBuilder.append(URLEncoder.encode(next.getKey(), "UTF-8"));
						theUrlBuilder.append('=');
						theUrlBuilder.append(URLEncoder.encode(nextValue, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						throw new Error("UTF-8 not supported - This should not happen");
					}
				}
			}
		}
	}

}
