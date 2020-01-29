package ca.uhn.fhir.model.primitive;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseBooleanDatatype;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "boolean")
public class BooleanDt extends BasePrimitive<Boolean> implements IBaseBooleanDatatype {

	/**
	 * Constructor
	 */
	public BooleanDt() {
		super();
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public BooleanDt(@SimpleSetter.Parameter(name = "theBoolean") boolean theBoolean) {
		setValue(theBoolean);
	}

	@Override
	protected Boolean parse(String theValue) {
		if ("true".equals(theValue)) {
			return Boolean.TRUE;
		} else if ("false".equals(theValue)) {
			return Boolean.FALSE;
		} else {
			throw new DataFormatException("Invalid boolean string: '" + theValue + "'");
		}
	}

	@Override
	protected String encode(Boolean theValue) {
		if (Boolean.TRUE.equals(theValue)) {
			return "true";
		}
		return "false";
	}

}
