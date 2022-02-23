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
import static org.apache.commons.lang3.StringUtils.defaultString;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

public class UriParam extends BaseParam implements IQueryParameterType {

	private UriParamQualifierEnum myQualifier;
	private String myValue;

	/**
	 * Constructor
	 */
	public UriParam() {
		super();
	}

	public UriParam(String theValue) {
		setValue(theValue);
	}

	@Override
	String doGetQueryParameterQualifier() {
		return myQualifier != null ? myQualifier.getValue() : null;
	}

	@Override
	String doGetValueAsQueryToken(FhirContext theContext) {
		return ParameterUtil.escape(myValue);
	}

	@Override
	void doSetValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theValue) {
		myQualifier = UriParamQualifierEnum.forValue(theQualifier);
		myValue = ParameterUtil.unescape(theValue);
	}

	/**
	 * Gets the qualifier for this param (may be <code>null</code> and generally will be)
	 */
	public UriParamQualifierEnum getQualifier() {
		return myQualifier;
	}

	public String getValue() {
		return myValue;
	}

	public StringDt getValueAsStringDt() {
		return new StringDt(myValue);
	}

	public UriDt getValueAsUriDt() {
		return new UriDt(myValue);
	}

	public String getValueNotNull() {
		return defaultString(myValue);
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(myValue);
	}

	/**
	 * Sets the qualifier for this param (may be <code>null</code> and generally will be)
	 * 
	 * @return Returns a reference to <code>this</code> for easy method chanining
	 */
	public UriParam setQualifier(UriParamQualifierEnum theQualifier) {
		myQualifier = theQualifier;
		return this;
	}

	/**
	 * Sets the value for this param
	 * 
	 * @return Returns a reference to <code>this</code> for easy method chanining
	 */
	public UriParam setValue(String theValue) {
		myValue = theValue;
		return this;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("value", getValue());
		return builder.toString();
	}

}
