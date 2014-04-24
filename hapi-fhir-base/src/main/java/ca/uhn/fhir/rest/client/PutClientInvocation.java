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

import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;

public class PutClientInvocation extends BaseClientInvocationWithContents {

	public PutClientInvocation(FhirContext theContext, Bundle theBundle) {
		super(theContext, theBundle);
	}

	public PutClientInvocation(FhirContext theContext, IResource theResource, String theUrlExtension) {
		super(theContext, theResource, theUrlExtension);
	}

	@Override
	protected HttpRequestBase createRequest(String url, StringEntity theEntity) {
		HttpPut retVal = new HttpPut(url);
		retVal.setEntity(theEntity);
		return retVal;
	}


}
