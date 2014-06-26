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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.server.Constants;

public class StringParam extends StringDt {

	private boolean myExact;

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
	public void setValueAsQueryToken(String theQualifier, String theValue) {
		if (Constants.PARAMQUALIFIER_STRING_EXACT.equals(theQualifier)) {
			setExact(true);
		} else {
			setExact(false);
		}
		super.setValueAsQueryToken(theQualifier, theValue);
	}

	@Override
	public String getQueryParameterQualifier() {
		if (isExact()) {
			return Constants.PARAMQUALIFIER_STRING_EXACT;
		} else {
			return null;
		}
	}

	public boolean isExact() {
		return myExact;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("value", getValue());
		if (myExact) {
			builder.append("exact", myExact);
		}
		return builder.toString();
	}

	public void setExact(boolean theExact) {
		myExact = theExact;
	}

}
