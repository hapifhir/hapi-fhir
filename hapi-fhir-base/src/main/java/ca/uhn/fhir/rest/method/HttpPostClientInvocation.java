package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;

public class HttpPostClientInvocation extends BaseHttpClientInvocationWithContents {

	public HttpPostClientInvocation(FhirContext theContext, IResource theResource, String theUrlExtension) {
		super(theContext, theResource, theUrlExtension);
	}

	
	public HttpPostClientInvocation(FhirContext theContext, TagList theTagList, String... theUrlExtension) {
		super(theContext, theTagList, theUrlExtension);
	}


	public HttpPostClientInvocation(FhirContext theContext, List<IResource> theResources) {
		super(theContext, theResources);
	}


	public HttpPostClientInvocation(FhirContext theContext, Bundle theBundle) {
		super(theContext,theBundle);
	}

	public HttpPostClientInvocation(FhirContext theContext, String theContents, boolean theIsBundle, String theUrlExtension) {
		super(theContext,theContents, theIsBundle, theUrlExtension);
	}


	public HttpPostClientInvocation(FhirContext theContext, Map<String, List<String>> theParams, String... theUrlExtension) {
		super(theContext, theParams, theUrlExtension);
	}


	@Override
	protected HttpPost createRequest(String url, AbstractHttpEntity theEntity) {
		HttpPost retVal = new HttpPost(url);
		retVal.setEntity(theEntity);
		return retVal;
	}

}
