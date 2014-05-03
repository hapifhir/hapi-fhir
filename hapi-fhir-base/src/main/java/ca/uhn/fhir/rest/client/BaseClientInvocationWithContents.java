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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.Constants;

public abstract class BaseClientInvocationWithContents extends BaseClientInvocation {

	private final Bundle myBundle;
	private final FhirContext myContext;
	private List<Header> myHeaders;
	private final IResource myResource;
	private String myUrlExtension;

	public BaseClientInvocationWithContents(FhirContext theContext, Bundle theBundle) {
		super();
		myContext = theContext;
		myResource = null;
		myBundle = theBundle;
	}

	public BaseClientInvocationWithContents(FhirContext theContext, IResource theResource, String theUrlExtension) {
		super();
		myContext = theContext;
		myResource = theResource;
		myBundle = null;
		myUrlExtension = theUrlExtension;
	}

	public void addHeader(String theName, String theValue) {
		if (myHeaders == null) {
			myHeaders = new ArrayList<Header>();
		}
		myHeaders.add(new BasicHeader(theName, theValue));
	}

	@Override
	public HttpRequestBase asHttpRequest(String theUrlBase, Map<String, List<String>> theExtraParams) throws DataFormatException {
		StringBuilder b = new StringBuilder();
		b.append(theUrlBase);
		if (!theUrlBase.endsWith("/")) {
			b.append('/');
		}
		b.append(StringUtils.defaultString(myUrlExtension));

		appendExtraParamsWithQuestionMark(theExtraParams, b, true);

		String url = b.toString();
		String contents = myContext.newXmlParser().encodeResourceToString(myResource);
		StringEntity entity = new StringEntity(contents, ContentType.create(Constants.CT_FHIR_XML, "UTF-8"));

		HttpRequestBase http = createRequest(url, entity);
		if (myHeaders != null) {
			for (Header next : myHeaders) {
				http.addHeader(next);
			}
		}
		return http;
	}

	protected abstract HttpRequestBase createRequest(String url, StringEntity theEntity);

}
