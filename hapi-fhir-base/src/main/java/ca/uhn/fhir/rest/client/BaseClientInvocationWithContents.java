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

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;

public abstract class BaseClientInvocationWithContents extends BaseClientInvocation {

	private final FhirContext myContext;
	private final IResource myResource;
	private final String myUrlExtension;
	private final TagList myTagList;

	public BaseClientInvocationWithContents(FhirContext theContext, IResource theResource, String theUrlExtension) {
		super();
		myContext = theContext;
		myResource = theResource;
		myUrlExtension = theUrlExtension;
		myTagList = null;
	}

	public BaseClientInvocationWithContents(FhirContext theContext, TagList theTagList, String... theUrlExtension) {
		super();
		if (theTagList == null) {
			throw new NullPointerException("Tag list must not be null");
		}

		myResource = null;
		myContext = theContext;
		myTagList = theTagList;

		myUrlExtension = StringUtils.join(theUrlExtension, '/');
	}

	@Override
	public HttpRequestBase asHttpRequest(String theUrlBase, Map<String, List<String>> theExtraParams, EncodingEnum theEncoding) throws DataFormatException {
		StringBuilder b = new StringBuilder();
		b.append(theUrlBase);
		if (!theUrlBase.endsWith("/")) {
			b.append('/');
		}
		b.append(StringUtils.defaultString(myUrlExtension));

		appendExtraParamsWithQuestionMark(theExtraParams, b, true);

		IParser parser;
		String contentType;
		if (theEncoding == EncodingEnum.JSON) {
			parser = myContext.newJsonParser();
			contentType = Constants.CT_FHIR_JSON;
		} else {
			parser = myContext.newXmlParser();
			contentType = Constants.CT_FHIR_XML;
		}

		String contents;
		if (myTagList != null) {
			contents = parser.encodeTagListToString(myTagList);
		} else {
			contents = parser.encodeResourceToString(myResource);
		}

		StringEntity entity = new StringEntity(contents, ContentType.create(contentType, "UTF-8"));

		HttpRequestBase retVal = createRequest(b.toString(), entity);
		super.addHeadersToRequest(retVal);
		return retVal;
	}

	protected abstract HttpRequestBase createRequest(String url, StringEntity theEntity);

}
