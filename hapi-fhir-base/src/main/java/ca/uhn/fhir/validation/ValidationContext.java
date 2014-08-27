package ca.uhn.fhir.validation;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;

class ValidationContext {

	private FhirContext myFhirContext;
	private OperationOutcome myOperationOutcome;
	private IResource myResource;
	private String myXmlEncodedResource;

	public ValidationContext(FhirContext theContext, IResource theResource) {
		myFhirContext = theContext;
		myResource = theResource;
	}

	public String getXmlEncodedResource() {
		if (myXmlEncodedResource == null) {
			myXmlEncodedResource = myFhirContext.newXmlParser().encodeResourceToString(myResource);
		}
		return myXmlEncodedResource;
	}

	public OperationOutcome getOperationOutcome() {
		if (myOperationOutcome == null) {
			myOperationOutcome = new OperationOutcome();
		}
		return myOperationOutcome;
	}

	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	public IResource getResource() {
		return myResource;
	}

}
