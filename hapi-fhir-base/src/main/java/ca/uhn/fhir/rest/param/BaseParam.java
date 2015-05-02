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

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.server.Constants;

/**
 * Base class for RESTful operation parameter types
 */
abstract class BaseParam implements IQueryParameterType {

	private Boolean myMissing;

	/**
	 * If set to non-null value, indicates that this parameter has been populated with a "[name]:missing=true" or "[name]:missing=false" vale instead of a normal value
	 */
	@Override
	public Boolean getMissing() {
		return myMissing;
	}

	@Override
	public final String getQueryParameterQualifier() {
		if (myMissing != null) {
			return Constants.PARAMQUALIFIER_MISSING;
		}
		return doGetQueryParameterQualifier();
	}

	abstract String doGetQueryParameterQualifier();

	abstract String doGetValueAsQueryToken();
	
	@Override
	public final String getValueAsQueryToken() {
		if (myMissing != null) {
			return myMissing ? Constants.PARAMQUALIFIER_MISSING_TRUE : Constants.PARAMQUALIFIER_MISSING_FALSE;
		}
		return doGetValueAsQueryToken();
	}

	/**
	 * If set to non-null value, indicates that this parameter has been populated with a "[name]:missing=true" or "[name]:missing=false" vale instead of a normal value
	 */
	@Override
	public void setMissing(Boolean theMissing) {
		myMissing = theMissing;
	}

	@Override
	public final void setValueAsQueryToken(String theQualifier, String theValue) {
		if (Constants.PARAMQUALIFIER_MISSING.equals(theQualifier)) {
			myMissing = "true".equals(theValue);
			doSetValueAsQueryToken(null, null);
		} else {
			myMissing = null;
			doSetValueAsQueryToken(theQualifier, theValue);
		}
	}

	abstract void doSetValueAsQueryToken(String theQualifier, String theValue);

	static class ComposableBaseParam extends BaseParam{

		@Override
		String doGetQueryParameterQualifier() {
			return null;
		}

		@Override
		String doGetValueAsQueryToken() {
			return null;
		}

		@Override
		void doSetValueAsQueryToken(String theQualifier, String theValue) {
			// nothing
		}
		
	}
	
}
