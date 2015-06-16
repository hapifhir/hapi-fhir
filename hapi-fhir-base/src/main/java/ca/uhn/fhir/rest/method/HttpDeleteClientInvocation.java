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

import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.EncodingEnum;

public class HttpDeleteClientInvocation extends BaseHttpClientInvocation {

	private String myUrlPath;
	private Map<String, List<String>> myParams;

	public HttpDeleteClientInvocation(IIdType theId) {
		super();
		myUrlPath = theId.toUnqualifiedVersionless().getValue();
	}

	public HttpDeleteClientInvocation(String theSearchUrl) {
		myUrlPath = theSearchUrl;
	}

	public HttpDeleteClientInvocation(String theResourceType, Map<String, List<String>> theParams) {
		myUrlPath = theResourceType;
		myParams = theParams;
	}

	@Override
	public HttpRequestBase asHttpRequest(String theUrlBase, Map<String, List<String>> theExtraParams, EncodingEnum theEncoding, Boolean thePrettyPrint) {
		StringBuilder b = new StringBuilder();
		b.append(theUrlBase);
		if (!theUrlBase.endsWith("/")) {
			b.append('/');
		}
		b.append(myUrlPath);

		appendExtraParamsWithQuestionMark(myParams, b, b.indexOf("?") == -1);
		appendExtraParamsWithQuestionMark(theExtraParams, b, b.indexOf("?") == -1);

		HttpDelete retVal = new HttpDelete(b.toString());
		super.addHeadersToRequest(retVal);
		return retVal;
	}

}
