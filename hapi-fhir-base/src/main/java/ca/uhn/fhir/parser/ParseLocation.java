package ca.uhn.fhir.parser;

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

import ca.uhn.fhir.parser.IParserErrorHandler.IParseLocation;

import static org.apache.commons.lang3.StringUtils.defaultString;

class ParseLocation implements IParseLocation {

	private String myParentElementName;

	/**
	 * Constructor
	 */
	ParseLocation() {
		super();
	}

	/**
	 * Constructor
	 */
	ParseLocation(String theParentElementName) {
		setParentElementName(theParentElementName);
	}

	@Override
	public String getParentElementName() {
		return myParentElementName;
	}

	ParseLocation setParentElementName(String theParentElementName) {
		myParentElementName = theParentElementName;
		return this;
	}

	@Override
	public String toString() {
		return "[element=\"" + defaultString(myParentElementName) + "\"]";
	}

	/**
	 * Factory method
	 */
	static ParseLocation fromElementName(String theChildName) {
		return new ParseLocation(theChildName);
	}
}
