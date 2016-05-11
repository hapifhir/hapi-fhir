package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;

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

/**
 * The default error handler, which logs issues but does not abort parsing
 * 
 * @see IParser#setParserErrorHandler(IParserErrorHandler)
 * @see FhirContext#setParserErrorHandler(IParserErrorHandler)
 */
public class LenientErrorHandler implements IParserErrorHandler {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(LenientErrorHandler.class);
	private boolean myLogErrors;

	/**
	 * Constructor which configures this handler to log all errors
	 */
	public LenientErrorHandler() {
		myLogErrors = true;
	}

	/**
	 * Constructor
	 * 
	 * @param theLogErrors Should errors be logged?
	 * @since 1.2
	 */
	public LenientErrorHandler(boolean theLogErrors) {
		myLogErrors = theLogErrors;
	}

	@Override
	public void unknownElement(IParseLocation theLocation, String theElementName) {
		if (myLogErrors) {
			ourLog.warn("Unknown element '{}' found while parsing", theElementName);
		}
	}

	@Override
	public void unknownAttribute(IParseLocation theLocation, String theElementName) {
		if (myLogErrors) {
			ourLog.warn("Unknown attribute '{}' found while parsing", theElementName);
		}
	}

	@Override
	public void unexpectedRepeatingElement(IParseLocation theLocation, String theElementName) {
		if (myLogErrors) {
			ourLog.warn("Multiple repetitions of non-repeatable element '{}' found while parsing", theElementName);
		}
	}

}
