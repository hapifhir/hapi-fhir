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
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.client.model.AsHttpRequestParams;
import ca.uhn.fhir.rest.client.model.CreateRequestParameters;
import ca.uhn.fhir.rest.param.HttpClientRequestParameters;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;
import java.util.Map;

public class HttpPatchClientInvocation extends BaseHttpClientInvocation {

	private final String myUrlPath;
	private Map<String, List<String>> myParams;
	private final String myContents;
	private final String myContentType;

	public HttpPatchClientInvocation(FhirContext theContext, IIdType theId, String theContentType, String theContents) {
		super(theContext);
		myUrlPath = theId.toUnqualifiedVersionless().getValue();
		myContentType = theContentType;
		myContents = theContents;
	}

	public HttpPatchClientInvocation(
			FhirContext theContext, String theUrlPath, String theContentType, String theContents) {
		super(theContext);
		myUrlPath = theUrlPath;
		myContentType = theContentType;
		myContents = theContents;
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
		requestParameters.setUrl(b.toString());
		requestParameters.setEncodingEnum(theEncoding);
		requestParameters.setRequestTypeEnum(RequestTypeEnum.PATCH);
		return createHttpRequest(requestParameters);
	}

	@Override
	protected IHttpRequest createHttpRequest(String theUrl, EncodingEnum theEncoding, RequestTypeEnum theRequestType) {
		IHttpClient httpClient = getRestfulClientFactory()
				.getHttpClient(new StringBuilder(theUrl), null, null, theRequestType, getHeaders());
		return httpClient.createByteRequest(getContext(), myContents, myContentType, null);
	}

	@Override
	protected IHttpRequest createHttpRequest(CreateRequestParameters theParameters) {
		IHttpClient client;
		if (theParameters.getClient() == null) {
			client = getRestfulClientFactory()
					.getHttpClient(
							new StringBuilder(theParameters.getUrl()),
							null,
							null,
							theParameters.getRequestTypeEnum(),
							getHeaders());
		} else {
			client = theParameters.getClient();
			client.setNewUrl(new StringBuilder(theParameters.getUrl()), null, null);
		}

		HttpClientRequestParameters params =
				new HttpClientRequestParameters(theParameters.getUrl(), RequestTypeEnum.PATCH);
		params.setContents(myContents);
		params.setContentType(myContentType);
		params.setFhirContext(getContext());
		IHttpRequest req = client.createRequest(params);
		for (Header h : getHeaders()) {
			req.addHeader(h.getName(), h.getValue());
		}
		client.addHeadersToRequest(req, theParameters.getEncodingEnum(), getContext());
		req.addHeader(Constants.HEADER_CONTENT_TYPE, params.getContentType() + Constants.HEADER_SUFFIX_CT_UTF_8);
		return req;
	}
}
