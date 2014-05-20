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

import static org.apache.commons.lang3.StringUtils.*;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;

public class ReferenceParam implements IQueryParameterType {

	private String myChain;
	private String myResourceType;
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

	public ReferenceParam(String theResourceType, String theChain, String theValue) {
		setResourceType(theResourceType);
		setValueAsQueryToken(null, theValue);
		setChain(theChain);
	}

	public String getChain() {
		return myChain;
	}

	@Override
	public String getQueryParameterQualifier() {
		StringBuilder b = new StringBuilder();
		if (isNotBlank(myResourceType)) {
			b.append(':');
			b.append(myResourceType);
		}
		if (isNotBlank(myChain)) {
			b.append('.');
			b.append(myChain);
		}
		if (b.length() != 0) {
			return b.toString();
		}
		return null;
	}

	public String getResourceType() {
		return myResourceType;
	}

	@Override
	public String getValueAsQueryToken() {
		return myValue;
	}

	public void setChain(String theChain) {
		myChain = theChain;
	}

	public Class<? extends IResource> getResourceType(FhirContext theCtx) {
		if (isBlank(myResourceType)) {
			return null;
		}
		return theCtx.getResourceDefinition(myResourceType).getImplementingClass();
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	@Override
	public void setValueAsQueryToken(String theQualifier, String theValue) {
		String q = theQualifier;
		if (isNotBlank(q)) {
			if (q.startsWith(":")) {
				int nextIdx = q.indexOf('.');
				if (nextIdx != -1) {
					myResourceType = q.substring(1, nextIdx);
					myChain = q.substring(nextIdx + 1);
				} else {
					myResourceType = q.substring(1);
				}
			} else if (q.startsWith(".")) {
				myChain = q.substring(1);
			}
		}

		myValue = theValue;
	}

	public String getValue() {
		return myValue;
	}

}
