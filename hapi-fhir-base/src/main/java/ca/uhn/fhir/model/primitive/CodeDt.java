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

import static org.apache.commons.lang3.StringUtils.*;
import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "code")
public class CodeDt extends BasePrimitive<String> implements ICodedDatatype, Comparable<CodeDt> {

	private String myValue;

	/**
	 * Constructor
	 */
	public CodeDt() {
		super();
	}

	/**
	 * Constructor which accepts a string code
	 */
	@SimpleSetter()
	public CodeDt(@SimpleSetter.Parameter(name = "theCode") String theCode) {
		setValue(theCode);
	}

	@Override
	public String getValue() {
		return myValue;
	}

	@Override
	public void setValue(String theValue) throws DataFormatException {
		if (theValue == null) {
			myValue = null;
		} else {
			String newValue = theValue.trim();
			myValue = newValue;
		}
	}

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		setValue(theValue);
	}

	@Override
	public String getValueAsString() {
		return getValue();
	}

	@Override
	public int compareTo(CodeDt theCode) {
		if (theCode == null) {
			return 1;
		}
		return defaultString(getValue()).compareTo(defaultString(theCode.getValue()));
	}

}
