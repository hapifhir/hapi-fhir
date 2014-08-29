package ca.uhn.fhir.rest.param;

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

import static org.apache.commons.lang3.StringUtils.*;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;

public class ReferenceParam extends IdDt implements IQueryParameterType {

	private String myChain;
	private BaseParam myBase=new BaseParam();

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
		if (isNotBlank(theResourceType)) {
			setValue(theResourceType + "/" + theValue);
		} else {
			setValue(theValue);
		}
		setChain(theChain);
	}

	public String getChain() {
		return myChain;
	}

	@Override
	public String getQueryParameterQualifier() {
		if (myBase.getMissing()!=null) {
			return myBase.getQueryParameterQualifier();
		}
		
		StringBuilder b = new StringBuilder();
		if (isNotBlank(getResourceType())) {
			b.append(':');
			b.append(getResourceType());
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

	@Override
	public String getValueAsQueryToken() {
		if (myBase.getMissing()!=null) {
			return myBase.getValueAsQueryToken();
		}
		return getIdPart();
	}

	public void setChain(String theChain) {
		myChain = theChain;
	}

	public Class<? extends IResource> getResourceType(FhirContext theCtx) {
		if (isBlank(getResourceType())) {
			return null;
		}
		return theCtx.getResourceDefinition(getResourceType()).getImplementingClass();
	}

	@Override
	public void setValueAsQueryToken(String theQualifier, String theValue) {
		myBase.setValueAsQueryToken(theQualifier, theValue);
		if (myBase.getMissing()!=null) {
			myChain=null;
			setValue(null);
			return;
		}
		
		String q = theQualifier;
		String resourceType = null;
		if (isNotBlank(q)) {
			if (q.startsWith(":")) {
				int nextIdx = q.indexOf('.');
				if (nextIdx != -1) {
					resourceType = q.substring(1, nextIdx);
					myChain = q.substring(nextIdx + 1);
				} else {
					resourceType = q.substring(1);
				}
			} else if (q.startsWith(".")) {
				myChain = q.substring(1);
			}
		}

		setValue(theValue);

		if (isNotBlank(resourceType) && isBlank(getResourceType())) {
			setValue(resourceType + '/' + theValue);
		}
	}

}
