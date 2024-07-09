/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.api.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class SpecialParam extends BaseParam /*implements IQueryParameterType*/ {
	private static final Logger ourLog = LoggerFactory.getLogger(StringParam.class);

	private String myValue;
	private boolean myContains;

	/**
	 * Constructor
	 */
	public SpecialParam() {
		super();
	}

	@Override
	String doGetQueryParameterQualifier() {
		if (myContains) {
			return Constants.PARAMQUALIFIER_STRING_CONTAINS;
		} else {
			return null;
		}
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
		if (Constants.PARAMQUALIFIER_STRING_CONTAINS.equals(theQualifier)) {
			if (theParamName.equalsIgnoreCase(Constants.PARAM_TEXT)
					|| theParamName.equalsIgnoreCase(Constants.PARAM_CONTENT)) {
				setContains(true);
			} else {
				ourLog.debug(
						"Attempted to set the :contains modifier on a special search parameter that was not `_text` or `_content`. This is not supported.");
			}
		}
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
	/**
	 * Special parameter modifier <code>:contains</code> for _text and _content
	 */
	public boolean isContains() {
		return myContains;
	}

	/**
	 * Special parameter modifier <code>:contains</code> for _text and _content
	 */
	public SpecialParam setContains(boolean theContains) {
		myContains = theContains;
		if (myContains) {
			setMissing(null);
		}
		return this;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(isContains())
				.append(getValue())
				.append(getMissing())
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SpecialParam)) {
			return false;
		}

		SpecialParam other = (SpecialParam) obj;

		EqualsBuilder eb = new EqualsBuilder();
		eb.append(myContains, other.myContains);
		eb.append(myValue, other.myValue);
		eb.append(getMissing(), other.getMissing());

		return eb.isEquals();
	}
}
