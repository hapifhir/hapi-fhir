package ca.uhn.fhir.model.primitive;

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

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "integer")
public class IntegerDt extends BasePrimitive<Integer> {

	private Integer myValue;

	/**
	 * Constructor
	 */
	public IntegerDt() {
		// nothing
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public IntegerDt(@SimpleSetter.Parameter(name = "theInteger") int theInteger) {
		setValue(theInteger);
	}

	/**
	 * Constructor
	 * 
	 * @param theIntegerAsString A string representation of an integer
	 * @throws DataFormatException If the string is not a valid integer representation
	 */
	public IntegerDt(String theIntegerAsString) {
		setValueAsString(theIntegerAsString);
	}

	public IntegerDt(Long theCount) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Integer getValue() {
		return myValue;
	}

	@Override
	public void setValue(Integer theValue) {
		myValue = theValue;
	}

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		if (theValue == null) {
			myValue = null;
		} else {
			try {
				myValue = Integer.parseInt(theValue);
			} catch (NumberFormatException e) {
				throw new DataFormatException(e);
			}
		}
	}

	@Override
	public String getValueAsString() {
		if (myValue == null) {
			return null;
		}
		return Integer.toString(myValue);
	}

}
