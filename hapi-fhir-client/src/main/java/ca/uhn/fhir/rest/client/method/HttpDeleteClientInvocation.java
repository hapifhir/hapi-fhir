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
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.client.model.AsHttpRequestParams;
import ca.uhn.fhir.rest.client.model.CreateRequestParameters;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;
import java.util.Map;

public class HttpDeleteClientInvocation extends BaseHttpClientInvocation {

	private final String myUrlPath;
	private final Map<String, List<String>> myParams;

	public HttpDeleteClientInvocation(
			FhirContext theContext, IIdType theId, Map<String, List<String>> theAdditionalParams) {
		super(theContext);
		myUrlPath = theId.toUnqualifiedVersionless().getValue();
		myParams = theAdditionalParams;
	}

	public HttpDeleteClientInvocation(
			FhirContext theContext, String theSearchUrl, Map<String, List<String>> theParams) {
		super(theContext);
		myUrlPath = theSearchUrl;
		myParams = theParams;
	}

	@Override
	public IHttpRequest asHttpRequest(
			String theUrlBase,
			Map<String, List<String>> theExtraParams,
			EncodingEnum theEncoding,
			Boolean thePrettyPrint) {
		return asHttpRequest(new AsHttpRequestParams()
				.setUrlBase(theUrlBase)
				.setExtraParams(theExtraParams)
				.setEncodingEnum(theEncoding)
				.setPrettyPrint(thePrettyPrint));
	}

	@Override
	public IHttpRequest asHttpRequest(AsHttpRequestParams theParams) {
		String theUrlBase = theParams.getUrlBase();
		Map<String, List<String>> theExtraParams = theParams.getExtraParams();
		EncodingEnum theEncoding = theParams.getEncodingEnum();
		Boolean thePrettyPrint = theParams.getPrettyPrint();

		StringBuilder b = new StringBuilder();
		b.append(theUrlBase);
		if (!theUrlBase.endsWith("/")) {
			b.append('/');
		}
		b.append(myUrlPath);

		appendExtraParamsWithQuestionMark(myParams, b, b.indexOf("?") == -1);
		appendExtraParamsWithQuestionMark(theExtraParams, b, b.indexOf("?") == -1);

		CreateRequestParameters requestParameters = new CreateRequestParameters();
		requestParameters.setClient(theParams.getClient());
		requestParameters.setRequestTypeEnum(RequestTypeEnum.DELETE);
		requestParameters.setEncodingEnum(theEncoding);
		requestParameters.setUrl(b.toString());
		return createHttpRequest(requestParameters);
	}
}
