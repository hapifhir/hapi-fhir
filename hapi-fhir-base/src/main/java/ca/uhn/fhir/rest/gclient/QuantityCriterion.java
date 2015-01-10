package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.rest.param.ParameterUtil;

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

class QuantityCriterion implements ICriterion<QuantityClientParam>, ICriterionInternal {

	private String myValue;
	private String myName;
	private String mySystem;
	private String myUnits;

	public QuantityCriterion(String theParamName, String theValue, String theSystem, String theUnits) {
		myValue = theValue;
		myName = theParamName;
		mySystem = theSystem;
		myUnits = theUnits;
	}

	@Override
	public String getParameterName() {
		return myName;
	}

	@Override
	public String getParameterValue() {
		return ParameterUtil.escape(myValue) + '|' + ParameterUtil.escape(mySystem) + '|' + ParameterUtil.escape(myUnits);
	}

}
