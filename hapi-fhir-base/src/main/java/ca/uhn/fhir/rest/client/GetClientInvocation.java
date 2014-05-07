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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;

public class GetClientInvocation extends BaseClientInvocation {

	private final Map<String, List<String>> myParameters;
	private final String myUrlPath;

	public GetClientInvocation(Map<String, List<String>> theParameters, String... theUrlFragments) {
		myParameters = theParameters;
		myUrlPath = StringUtils.join(theUrlFragments, '/');
	}

	public GetClientInvocation(String theUrlPath) {
		myParameters = new HashMap<String, List<String>>();
		myUrlPath = theUrlPath;
	}

	public GetClientInvocation(String... theUrlFragments) {
		myParameters = new HashMap<String, List<String>>();
		myUrlPath = StringUtils.join(theUrlFragments, '/');
	}

	public Map<String, List<String>> getParameters() {
		return myParameters;
	}

	public String getUrlPath() {
		return myUrlPath;
	}

	@Override
	public HttpRequestBase asHttpRequest(String theUrlBase, Map<String, List<String>> theExtraParams, EncodingEnum theEncoding) {
		StringBuilder b = new StringBuilder();
		b.append(theUrlBase);
		if (!theUrlBase.endsWith("/")) {
			b.append('/');
		}
		b.append(myUrlPath);

		boolean first = true;
		for (Entry<String, List<String>> next : myParameters.entrySet()) {
			if (next.getValue() == null || next.getValue().isEmpty()) {
				continue;
			}
			String nextKey = next.getKey();
			for (String nextValue : next.getValue()) {
				first = addQueryParameter(b, first, nextKey, nextValue);
			}
		}

		appendExtraParamsWithQuestionMark(theExtraParams, b, first);

		HttpGet retVal = new HttpGet(b.toString());
		super.addHeadersToRequest(retVal);

		return retVal;
	}

	private boolean addQueryParameter(StringBuilder b, boolean first, String nextKey, String nextValue) {
		if (first) {
			b.append('?');
			first = false;
		} else {
			b.append('&');
		}
		try {
			b.append(URLEncoder.encode(nextKey, "UTF-8"));
			b.append('=');
			b.append(URLEncoder.encode(nextValue, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationException("Could not find UTF-8 encoding. This shouldn't happen.", e);
		}
		return first;
	}

}
