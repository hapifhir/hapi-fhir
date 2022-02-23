package ca.uhn.fhir.rest.param;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.UriDt;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class SpecialParam extends BaseParam /*implements IQueryParameterType*/ {

	private String myValue;

	/**
	 * Constructor
	 */
	public SpecialParam() {
		super();
	}

	@Override
	String doGetQueryParameterQualifier() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	String doGetValueAsQueryToken(FhirContext theContext) {
		return ParameterUtil.escape(getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void doSetValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theParameter) {
		setValue(ParameterUtil.unescape(theParameter));
	}

	/**
	 * Returns the value for the token (generally the value to the right of the
	 * vertical bar on the URL) 
	 */
	public String getValue() {
		return myValue;
	}

	public String getValueNotNull() {
		return defaultString(myValue);
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(myValue);
	}


	public SpecialParam setValue(String theValue) {
		myValue = theValue;
		return this;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("value", getValue());
		if (getMissing() != null) {
			builder.append(":missing", getMissing());
		}
		return builder.toString();
	}

	private static String toSystemValue(UriDt theSystem) {
		return theSystem.getValueAsString();
	}

}
