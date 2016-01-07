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

import java.util.Date;

import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.util.CoverageIgnore;

/**
 * @deprecated Use {@link DateParam} instead (this class is identical, but was renamed to be less confusing)
 */
@Deprecated
@CoverageIgnore
public class QualifiedDateParam extends DateParam {
	/**
	 * Constructor
	 */
	public QualifiedDateParam() {
	}
	
	/**
	 * Constructor
	 */
	public QualifiedDateParam(QuantityCompararatorEnum theComparator, Date theDate) {
		setComparator(theComparator);
		setValue(theDate);
	}

	/**
	 * Constructor
	 */
	public QualifiedDateParam(QuantityCompararatorEnum theComparator, String theDate) {
		setComparator(theComparator);
		setValueAsString(theDate);
	}

	/**
	 * Constructor which takes a complete [qualifier]{date} string.
	 * 
	 * @param theString The string
	 */
	public QualifiedDateParam(String theString) {
		setValueAsQueryToken(null, theString);
	}

}
