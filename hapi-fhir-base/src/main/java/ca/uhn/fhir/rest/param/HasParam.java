package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import static org.apache.commons.lang3.StringUtils.defaultString;

/*
 * #%L
 * HAPI FHIR - Core Library
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

/**
 * Implementation of the _has method parameter
 */
public class HasParam extends BaseParam implements IQueryParameterType {

	private static final long serialVersionUID = 1L;

	private String myReferenceFieldName;
	private String myParameterName;
	private String myParameterValue;
	private String myTargetResourceType;

	public HasParam() {
		super();
	}


	public HasParam(String theTargetResourceType, String theReferenceFieldName, String theParameterName, String theParameterValue) {
		this();
		myTargetResourceType = theTargetResourceType;
		myReferenceFieldName = theReferenceFieldName;
		myParameterName = theParameterName;
		myParameterValue = theParameterValue;
	}


	@Override
	String doGetQueryParameterQualifier() {
		return ':' + myTargetResourceType + ':' + myReferenceFieldName + ':' + myParameterName;
	}
	
	@Override
	String doGetValueAsQueryToken(FhirContext theContext) {
		return myParameterValue;
	}

	@Override
	void doSetValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theValue) {
		String qualifier = defaultString(theQualifier);
		if (!qualifier.startsWith(":")) {
			throwInvalidSyntaxException(Constants.PARAM_HAS + qualifier);
		}
		int colonIndex0 = qualifier.indexOf(':', 1);
		validateColon(qualifier, colonIndex0);
		int colonIndex1 = qualifier.indexOf(':', colonIndex0 + 1);
		validateColon(qualifier, colonIndex1);
		
		myTargetResourceType = qualifier.substring(1, colonIndex0);
		myReferenceFieldName = qualifier.substring(colonIndex0 + 1, colonIndex1);
		myParameterName = qualifier.substring(colonIndex1 + 1);
		myParameterValue = theValue;
	}

	public String getReferenceFieldName() {
		return myReferenceFieldName;
	}

	public String getParameterName() {
		return myParameterName;
	}

	public String getParameterValue() {
		return myParameterValue;
	}

	public String getTargetResourceType() {
		return myTargetResourceType;
	}

	private static void validateColon(String theParameterName, int colonIndex) {
		if (colonIndex == -1) {
			throwInvalidSyntaxException(theParameterName);
		}
	}


	private static void throwInvalidSyntaxException(String theParameterName) {
		throw new InvalidRequestException(Msg.code(1942) + "Invalid _has parameter syntax: " + theParameterName);
	}

}
