/*
 * #%L
 * HAPI FHIR - Client Framework
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
package ca.uhn.fhir.rest.client.method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.PagingHttpMethodEnum;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.UrlSourceEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;

import java.util.List;
import java.util.Map;

public class HttpSimpleClientInvocation extends BaseHttpClientInvocation {

	private final String myUrl;
	private UrlSourceEnum myUrlSource = UrlSourceEnum.EXPLICIT;

	private PagingHttpMethodEnum myPagingHttpMethod;

	public HttpSimpleClientInvocation(
			FhirContext theContext, String theUrlPath, PagingHttpMethodEnum thePagingHttpMethod) {
		super(theContext);
		myUrl = theUrlPath;
		myPagingHttpMethod = thePagingHttpMethod;
	}

	@Override
	public IHttpRequest asHttpRequest(
			String theUrlBase,
			Map<String, List<String>> theExtraParams,
			EncodingEnum theEncoding,
			Boolean thePrettyPrint) {
		IHttpRequest retVal = createHttpRequest(myUrl, theEncoding, myPagingHttpMethod.getRequestType());
		retVal.setUrlSource(myUrlSource);
		return retVal;
	}

	public void setUrlSource(UrlSourceEnum theUrlSource) {
		myUrlSource = theUrlSource;
	}
}
