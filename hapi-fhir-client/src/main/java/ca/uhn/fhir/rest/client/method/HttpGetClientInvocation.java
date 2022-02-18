package ca.uhn.fhir.rest.client.method;

/*
 * #%L
 * HAPI FHIR - Client Framework
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.UrlSourceEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author James Agnew
 * @author Doug Martin (Regenstrief Center for Biomedical Informatics)
 */
public class HttpGetClientInvocation extends BaseHttpClientInvocation {

	private final Map<String, List<String>> myParameters;
	private final String myUrlPath;
	private final UrlSourceEnum myUrlSource;

	public HttpGetClientInvocation(FhirContext theContext, Map<String, List<String>> theParameters, String... theUrlFragments) {
		this(theContext, theParameters, UrlSourceEnum.GENERATED, theUrlFragments);
	}

	public HttpGetClientInvocation(FhirContext theContext, Map<String, List<String>> theParameters, UrlSourceEnum theUrlSource, String... theUrlFragments) {
		super(theContext);
		myParameters = theParameters;
		myUrlPath = StringUtils.join(theUrlFragments, '/');
		myUrlSource = theUrlSource;
	}

	public HttpGetClientInvocation(FhirContext theContext, String theUrlPath) {
		super(theContext);
		myParameters = new HashMap<>();
		myUrlPath = theUrlPath;
		myUrlSource = UrlSourceEnum.GENERATED;
	}


	private boolean addQueryParameter(StringBuilder b, boolean first, String nextKey, String nextValue) {
		boolean retVal = first;
		if (retVal) {
			b.append('?');
			retVal = false;
		} else {
			b.append('&');
		}
		b.append(UrlUtil.escapeUrlParam(nextKey));
		b.append('=');
		b.append(UrlUtil.escapeUrlParam(nextValue));

		return retVal;
	}

	@Override
	public IHttpRequest asHttpRequest(String theUrlBase, Map<String, List<String>> theExtraParams, EncodingEnum theEncoding, Boolean thePrettyPrint) {
		StringBuilder b = new StringBuilder();

		if (!myUrlPath.contains("://")) {
			b.append(theUrlBase);
			if (!theUrlBase.endsWith("/") && !myUrlPath.startsWith("/")) {
				b.append('/');
			}
		}
		b.append(myUrlPath);

		boolean first = b.indexOf("?") == -1;
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

		IHttpRequest retVal = super.createHttpRequest(b.toString(), theEncoding, RequestTypeEnum.GET);
		retVal.setUrlSource(myUrlSource);

		return retVal;
	}

	public Map<String, List<String>> getParameters() {
		return myParameters;
	}

	public String getUrlPath() {
		return myUrlPath;
	}

}
