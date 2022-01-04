package ca.uhn.fhir.model.primitive;

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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.rest.param.StringParam;
import org.apache.commons.lang3.StringUtils;

@DatatypeDef(name = "string")
public class StringDt extends BasePrimitive<String> implements IQueryParameterType {

	/**
	 * Create a new String
	 */
	public StringDt() {
		super();
	}

	/**
	 * Create a new String
	 */
	@SimpleSetter
	public StringDt(@SimpleSetter.Parameter(name = "theString") String theValue) {
		setValue(theValue);
	}

	public String getValueNotNull() {
		return StringUtils.defaultString(getValue());
	}

	/**
	 * Returns the value of this string, or <code>null</code>
	 */
	@Override
	public String toString() {
		return getValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringDt other = (StringDt) obj;
		if (getValue() == null) {
			if (other.getValue() != null)
				return false;
		} else if (!getValue().equals(other.getValue()))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theValue) {
		setValue(theValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValueAsQueryToken(FhirContext theContext) {
		return getValue();
	}

	/**
	 * Returns <code>true</code> if this datatype has no extensions, and has either a <code>null</code> value or an empty ("") value.
	 */
	@Override
	public boolean isEmpty() {
		boolean retVal = super.isBaseEmpty() && StringUtils.isBlank(getValue());
		return retVal;
	}

	@Override
	public String getQueryParameterQualifier() {
		return null;
	}

	@Override
	protected String parse(String theValue) {
		return theValue;
	}

	@Override
	protected String encode(String theValue) {
		return theValue;
	}

	/**
	 * <b>Not supported!</b>
	 * 
	 * @deprecated get/setMissing is not supported in StringDt. Use {@link StringParam} instead if you
	 * need this functionality
	 */
	@Deprecated
	@Override
	public Boolean getMissing() {
		return null;
	}

	/**
	 * <b>Not supported!</b>
	 * 
	 * @deprecated get/setMissing is not supported in StringDt. Use {@link StringParam} instead if you
	 * need this functionality
	 */
	@Deprecated
	@Override
	public IQueryParameterType setMissing(Boolean theMissing) {
		throw new UnsupportedOperationException(Msg.code(1874) + "get/setMissing is not supported in StringDt. Use {@link StringParam} instead if you need this functionality");
	}

}
