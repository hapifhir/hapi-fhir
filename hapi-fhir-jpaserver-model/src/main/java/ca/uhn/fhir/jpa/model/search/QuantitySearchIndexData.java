package ca.uhn.fhir.jpa.model.search;

/*-
 * #%L
 * HAPI FHIR JPA Model
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

import java.math.BigDecimal;

class QuantitySearchIndexData {

	// unit is also referred as code
	private final String myCode;
	private final String mySystem;
	private final double myValue;


	QuantitySearchIndexData(String theCode, String theSystem, double theValue) {
		myCode = theCode;
		mySystem = theSystem;
		myValue = theValue;
	}


	public String getCode() { return myCode; }

	public String getSystem() { return mySystem; }

	public double getValue() { return myValue; }
}
