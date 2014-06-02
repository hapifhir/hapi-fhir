package ca.uhn.fhir.rest.method;

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
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServer.NarrativeModeEnum;

public abstract class BaseHttpClientInvocationWithContents extends BaseHttpClientInvocation {

	private final FhirContext myContext;
	private final IResource myResource;
	private final String myUrlExtension;
	private final TagList myTagList;
	private final List<IResource> myResources;

	public BaseHttpClientInvocationWithContents(FhirContext theContext, IResource theResource, String theUrlExtension) {
		super();
		myContext = theContext;
		myResource = theResource;
		myUrlExtension = theUrlExtension;
		myTagList = null;
		myResources=null;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, TagList theTagList, String... theUrlExtension) {
		super();
		if (theTagList == null) {
			throw new NullPointerException("Tag list must not be null");
		}

		myResource = null;
		myContext = theContext;
		myTagList = theTagList;
		myResources=null;

		myUrlExtension = StringUtils.join(theUrlExtension, '/');
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, List<IResource> theResources) {
		myContext=theContext;
		myResource=null;
		myTagList=null;
		myUrlExtension=null;
		myResources = theResources;
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
		String url = b.toString();

		if (myResource != null && Binary.class.isAssignableFrom(myResource.getClass())) {
			Binary binary = (Binary)myResource;
			ByteArrayEntity entity = new ByteArrayEntity(binary.getContent(), ContentType.parse(binary.getContentType()));
			HttpRequestBase retVal = createRequest(url, entity);
			return retVal;
		}
		
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
		} else if (myResources != null) {
			Bundle bundle = BaseResourceReturningMethodBinding.createBundleFromResourceList(myContext, "", myResources, theEncoding, "", "", false, NarrativeModeEnum.NORMAL);
			contents = parser.encodeBundleToString(bundle);
		} else {
			contents = parser.encodeResourceToString(myResource);
		}

		StringEntity entity = new StringEntity(contents, ContentType.create(contentType, "UTF-8"));

		HttpRequestBase retVal = createRequest(url, entity);
		super.addHeadersToRequest(retVal);
		return retVal;
	}

	protected abstract HttpRequestBase createRequest(String url, AbstractHttpEntity theEntity);

}
