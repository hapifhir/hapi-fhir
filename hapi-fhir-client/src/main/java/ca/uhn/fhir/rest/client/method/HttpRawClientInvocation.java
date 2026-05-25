/*
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.gclient.RawRequestEntity;
import org.hl7.fhir.instance.model.api.IBaseBinary;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.ObjectUtils.getIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class HttpRawClientInvocation extends BaseHttpClientInvocation {

	private final String myUrl;
	private final RawRequestEntity myRequestEntity;

	public HttpRawClientInvocation(FhirContext theContext, String theUrlPath, RawRequestEntity theRequestEntity) {
		super(theContext);
		myUrl = theUrlPath;
		myRequestEntity = theRequestEntity;
	}

	@Override
	public IHttpRequest asHttpRequest(
			String theUrlBase,
			Map<String, List<String>> theExtraParams,
			EncodingEnum theEncoding,
			Boolean thePrettyPrint) {

		StringBuilder urlBuilder = new StringBuilder();

		String url = getIfNull(myUrl, "");

		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			urlBuilder.append(theUrlBase);
			if (!theUrlBase.endsWith("/") && !url.startsWith("/")) {
				urlBuilder.append('/');
			}
		}

		if (isNotBlank(url)) {
			urlBuilder.append(url);
		}

		IHttpClient httpClient =
				getRestfulClientFactory().getHttpClient(urlBuilder, null, null, RequestTypeEnum.POST, getHeaders());

		IBaseBinary binary =
				(IBaseBinary) getContext().getResourceDefinition("Binary").newInstance();
		binary.setContentType(myRequestEntity.getContentType());
		binary.setContent(myRequestEntity.getBytes());

		return httpClient.createBinaryRequest(getContext(), binary);
	}
}
