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

import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;

public class HttpDeleteClientInvocation extends BaseHttpClientInvocation {

	private String myUrlPath;
	private Map<String, List<String>> myParams;

	public HttpDeleteClientInvocation(FhirContext theContext, IIdType theId, Map<String, List<String>> theAdditionalParams) {
		super(theContext);
		myUrlPath = theId.toUnqualifiedVersionless().getValue();
		myParams = theAdditionalParams;
	}

	public HttpDeleteClientInvocation(FhirContext theContext, String theSearchUrl, Map<String, List<String>> theParams) {
		super(theContext);
		myUrlPath = theSearchUrl;
		myParams = theParams;
	}

	@Override
	public IHttpRequest asHttpRequest(String theUrlBase, Map<String, List<String>> theExtraParams, EncodingEnum theEncoding, Boolean thePrettyPrint) {
		StringBuilder b = new StringBuilder();
		b.append(theUrlBase);
		if (!theUrlBase.endsWith("/")) {
			b.append('/');
		}
		b.append(myUrlPath);

		appendExtraParamsWithQuestionMark(myParams, b, b.indexOf("?") == -1);
		appendExtraParamsWithQuestionMark(theExtraParams, b, b.indexOf("?") == -1);

		return createHttpRequest(b.toString(), theEncoding, RequestTypeEnum.DELETE);
	}

}
