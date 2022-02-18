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

import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.CoverageIgnore;

@DatatypeDef(name = "unsignedInt", profileOf=IntegerDt.class)
@CoverageIgnore
public class UnsignedIntDt extends IntegerDt {

	/**
	 * Constructor
	 */
	public UnsignedIntDt() {
		// nothing
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public UnsignedIntDt(@SimpleSetter.Parameter(name = "theInteger") int theInteger) {
		setValue(theInteger);
	}

	/**
	 * Constructor
	 * 
	 * @param theIntegerAsString
	 *            A string representation of an integer
	 * @throws DataFormatException
	 *             If the string is not a valid integer representation
	 */
	public UnsignedIntDt(String theIntegerAsString) {
		setValueAsString(theIntegerAsString);
	}

}
