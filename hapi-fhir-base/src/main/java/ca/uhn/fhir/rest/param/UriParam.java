package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

public class UriParam extends BaseParam implements IQueryParameterType {

	private String myValue;

	public UriParam() {
	}

	public UriParam(String theValue) {
		setValue(theValue);
	}

	@Override
	String doGetQueryParameterQualifier() {
		return null;
	}

	@Override
	String doGetValueAsQueryToken() {
		return ParameterUtil.escape(myValue);
	}

	@Override
	void doSetValueAsQueryToken(String theQualifier, String theValue) {
		myValue = ParameterUtil.unescape(theValue);
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

	public void setValue(String theValue) {
		myValue = theValue;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("value", getValue());
		return builder.toString();
	}

}
