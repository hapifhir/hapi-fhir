package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;

import static org.apache.commons.lang3.StringUtils.left;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

/**
 * Implementation of the _has method parameter
 */
public class SourceParam extends BaseParam implements IQueryParameterType {

	private static final long serialVersionUID = 1L;
	private String myParameterValue;
	private String mySourceUri;
	private String myRequestId;

	public SourceParam() {}

	public SourceParam(String theParameterValue) {
		setValue(theParameterValue);
	}

	private void setValue(String theParameterValue) {
		myParameterValue = theParameterValue;
		String requestId;
		int lastHashValueIndex = theParameterValue.lastIndexOf('#');
		if (lastHashValueIndex == -1) {
			mySourceUri = theParameterValue;
			requestId = null;
		} else {
			mySourceUri = theParameterValue.substring(0, lastHashValueIndex);
			requestId = theParameterValue.substring(lastHashValueIndex + 1);
		}
		myRequestId = left(requestId, Constants.REQUEST_ID_LENGTH);
	}

	@Override
	String doGetQueryParameterQualifier() {
		return myParameterValue;
	}
	
	@Override
	String doGetValueAsQueryToken(FhirContext theContext) {
		return myParameterValue;
	}

	@Override
	void doSetValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theValue) {
		setValue(theValue);
	}

	public String getSourceUri() {
		return mySourceUri;
	}

	public String getRequestId() {
		return myRequestId;
	}
}
