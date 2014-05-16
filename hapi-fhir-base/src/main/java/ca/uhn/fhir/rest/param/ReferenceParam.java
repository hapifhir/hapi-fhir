package ca.uhn.fhir.rest.param;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;

public class ReferenceParam implements IQueryParameterType {

	private String myChain;
	private Class<? extends IResource> myType;
	private String myValue;

	public ReferenceParam() {
	}

	public ReferenceParam(String theValue) {
		setValueAsQueryToken(null, theValue);
	}

	public ReferenceParam(String theChain, String theValue) {
		setValueAsQueryToken(null, theValue);
		setChain(theChain);
	}

	public ReferenceParam(Class<? extends IResource> theType, String theChain, String theValue) {
		setType(theType);
		setValueAsQueryToken(null, theValue);
		setChain(theChain);
	}

	public String getChain() {
		return myChain;
	}

	public Class<? extends IResource> getType() {
		return myType;
	}

	@Override
	public String getValueAsQueryToken() {
		return myValue;
	}

	public void setChain(String theChain) {
		myChain = theChain;
	}

	public void setType(Class<? extends IResource> theType) {
		myType = theType;
	}

	@Override
	public void setValueAsQueryToken(String theQualifier, String theValue) {
		myValue = theValue;
	}

	@Override
	public String getQueryParameterQualifier(FhirContext theContext) {
		if (myType != null) {
			return ":" + theContext.getResourceDefinition(myType).getName();
		}
		return null;
	}

}
