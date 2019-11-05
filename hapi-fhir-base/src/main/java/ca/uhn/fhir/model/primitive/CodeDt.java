package ca.uhn.fhir.model.primitive;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;

@DatatypeDef(name = "code", profileOf=StringDt.class)
public class CodeDt extends BasePrimitive<String> implements Comparable<CodeDt> {

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
	public boolean isEmpty() {
		return super.isBaseEmpty() && isBlank(getValueAsString());
	}

	@Override
	public int compareTo(CodeDt theCode) {
		if (theCode == null) {
			return 1;
		}
		return defaultString(getValue()).compareTo(defaultString(theCode.getValue()));
	}

	@Override
	protected String parse(String theValue) {
		return theValue.trim();
	}

	@Override
	protected String encode(String theValue) {
		return theValue;
	}

}
