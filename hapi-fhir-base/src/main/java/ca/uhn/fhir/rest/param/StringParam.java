package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import ca.uhn.fhir.rest.server.Constants;

public class StringParam extends BaseParam implements IQueryParameterType {

	private boolean myExact;
	private String myValue;
	private boolean myContains;

	public StringParam() {
	}

	public StringParam(String theValue) {
		setValue(theValue);
	}

	public StringParam(String theValue, boolean theExact) {
		setValue(theValue);
		setExact(theExact);
	}

	@Override
	String doGetQueryParameterQualifier() {
		if (isExact()) {
			return Constants.PARAMQUALIFIER_STRING_EXACT;
		} else if (isContains()) {
			return Constants.PARAMQUALIFIER_STRING_CONTAINS;
		} else {
			return null;
		}
	}

	@Override
	String doGetValueAsQueryToken(FhirContext theContext) {
		return ParameterUtil.escape(myValue);
	}

	@Override
	void doSetValueAsQueryToken(String theQualifier, String theValue) {
		if (Constants.PARAMQUALIFIER_STRING_EXACT.equals(theQualifier)) {
			setExact(true);
		} else {
			setExact(false);
		}
		if (Constants.PARAMQUALIFIER_STRING_CONTAINS.equals(theQualifier)) {
			setContains(true);
		} else {
			setContains(false);
		}
		myValue = ParameterUtil.unescape(theValue);
	}

	public String getValue() {
		return myValue;
	}

	public StringDt getValueAsStringDt() {
		return new StringDt(myValue);
	}

	public String getValueNotNull() {
		return defaultString(myValue);
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(myValue);
	}

	public boolean isExact() {
		return myExact;
	}

	public StringParam setExact(boolean theExact) {
		myExact = theExact;
		if (myExact) {
			setContains(false);
			setMissing(null);
		}
		return this;
	}

	/**
	 * String parameter modifier <code>:contains</code>
	 */
	public boolean isContains() {
		return myContains;
	}

	public StringParam setValue(String theValue) {
		myValue = theValue;
		return this;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("value", getValue());
		if (myExact) {
			builder.append("exact", myExact);
		}
		if (myContains) {
			builder.append("contains", myContains);
		}
		if (getMissing() != null) {
			builder.append("missing", getMissing().booleanValue());
		}
		return builder.toString();
	}

	/**
	 * String parameter modifier <code>:contains</code>
	 */
	public StringParam setContains(boolean theContains) {
		myContains = theContains;
		if (myContains) {
			setExact(false);
			setMissing(null);
		}
		return this;
	}

}
