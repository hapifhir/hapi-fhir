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

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;

public class HttpPostClientInvocation extends BaseHttpClientInvocationWithContents {


	public HttpPostClientInvocation(FhirContext theContext, IBaseResource theResource, String theUrlExtension) {
		super(theContext, theResource, theUrlExtension);
	}

	public HttpPostClientInvocation(FhirContext theContext, IBaseResource theResource) {
		super(theContext, theResource, null);
	}

	public HttpPostClientInvocation(FhirContext theContext, List<? extends IBaseResource> theResources, BundleTypeEnum theBundleType) {
		super(theContext, theResources, theBundleType);
	}

	public HttpPostClientInvocation(FhirContext theContext, String theContents, boolean theIsBundle, String theUrlExtension) {
		super(theContext, theContents, theIsBundle, theUrlExtension);
	}

	public HttpPostClientInvocation(FhirContext theContext, Map<String, List<String>> theParams, String... theUrlExtension) {
		super(theContext, theParams, theUrlExtension);
	}

	@Override
	protected RequestTypeEnum getRequestType() {
		return RequestTypeEnum.POST;
	}

}
