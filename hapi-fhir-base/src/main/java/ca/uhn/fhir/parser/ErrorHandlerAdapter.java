package ca.uhn.fhir.parser;

import ca.uhn.fhir.parser.json.JsonLikeValue.ScalarType;
import ca.uhn.fhir.parser.json.JsonLikeValue.ValueType;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * Adapter implementation with NOP implementations of all {@link IParserErrorHandler} methods.
 */
public class ErrorHandlerAdapter implements IParserErrorHandler {

	@Override
	public void containedResourceWithNoId(IParseLocation theLocation) {
		// NOP
	}

	@Override
	public void incorrectJsonType(IParseLocation theLocation, String theElementName, ValueType theExpected, ScalarType theExpectedScalarType, ValueType theFound, ScalarType theFoundScalarType) {
		// NOP
	}

	@Override
	public void missingRequiredElement(IParseLocation theLocation, String theElementName) {
		// NOP
	}

	@Override
	public void unexpectedRepeatingElement(IParseLocation theLocation, String theElementName) {
		// NOP
	}

	@Override
	public void unknownAttribute(IParseLocation theLocation, String theElementName) {
		// NOP
	}

	@Override
	public void unknownElement(IParseLocation theLocation, String theElementName) {
		// NOP
	}

	@Override
	public void unknownReference(IParseLocation theLocation, String theReference) {
		// NOP
	}

	@Override
	public void invalidValue(IParseLocation theLocation, String theValue, String theError) {
		// NOP
	}

	@Override
	public void extensionContainsValueAndNestedExtensions(IParseLocation theLoc) {
		// NOP
	}
}
