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

import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;

public class HttpPutClientInvocation extends BaseHttpClientInvocationWithContents {

	public HttpPutClientInvocation(FhirContext theContext, IBaseResource theResource, String theUrlExtension) {
		super(theContext, theResource, theUrlExtension);
	}

	public HttpPutClientInvocation(FhirContext theContext, String theContents, boolean theIsBundle, String theUrlExtension) {
		super(theContext, theContents, theIsBundle, theUrlExtension);
	}

	@Override
	protected HttpRequestBase createRequest(StringBuilder theUrl, AbstractHttpEntity theEntity) {
		HttpPut retVal = new HttpPut(theUrl.toString());
		retVal.setEntity(theEntity);
		return retVal;
	}

}
