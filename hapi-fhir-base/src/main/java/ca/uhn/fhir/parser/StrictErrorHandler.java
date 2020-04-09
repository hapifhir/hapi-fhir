package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.json.JsonLikeValue.ScalarType;
import ca.uhn.fhir.parser.json.JsonLikeValue.ValueType;
import ca.uhn.fhir.util.UrlUtil;

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
 * Parser error handler which throws a {@link DataFormatException} any time an
 * issue is found while parsing.
 * 
 * @see IParser#setParserErrorHandler(IParserErrorHandler)
 * @see FhirContext#setParserErrorHandler(IParserErrorHandler)
 */
public class StrictErrorHandler extends BaseErrorHandler implements IParserErrorHandler {

	@Override
	public void containedResourceWithNoId(IParseLocation theLocation) {
		throw new DataFormatException("Resource has contained child resource with no ID");
	}

	@Override
	public void incorrectJsonType(IParseLocation theLocation, String theElementName, ValueType theExpected, ScalarType theExpectedScalarType, ValueType theFound, ScalarType theFoundScalarType) {
		String message = LenientErrorHandler.createIncorrectJsonTypeMessage(theElementName, theExpected, theExpectedScalarType, theFound, theFoundScalarType);
		throw new DataFormatException(message);
	}

	@Override
	public void invalidValue(IParseLocation theLocation, String theValue, String theError) {
		throw new DataFormatException(describeLocation(theLocation) + "Invalid attribute value \"" + UrlUtil.sanitizeUrlPart(theValue) + "\": " + theError);
	}

	@Override
	public void missingRequiredElement(IParseLocation theLocation, String theElementName) {
		StringBuilder b = new StringBuilder();
		b.append("Resource is missing required element '");
		b.append(theElementName);
		b.append("'");
		if (theLocation != null) {
			b.append(" in parent element '");
			b.append(theLocation.getParentElementName());
			b.append("'");
		}
		throw new DataFormatException(b.toString());
	}

	@Override
	public void unexpectedRepeatingElement(IParseLocation theLocation, String theElementName) {
		throw new DataFormatException(describeLocation(theLocation) + "Multiple repetitions of non-repeatable element '" + theElementName + "' found during parse");
	}

	@Override
	public void unknownAttribute(IParseLocation theLocation, String theAttributeName) {
		throw new DataFormatException(describeLocation(theLocation) + "Unknown attribute '" + theAttributeName + "' found during parse");
	}

	@Override
	public void unknownElement(IParseLocation theLocation, String theElementName) {
		throw new DataFormatException(describeLocation(theLocation) + "Unknown element '" + theElementName + "' found during parse");
	}

	@Override
	public void unknownReference(IParseLocation theLocation, String theReference) {
		throw new DataFormatException(describeLocation(theLocation) + "Resource has invalid reference: " + theReference);
	}

	@Override
	public void extensionContainsValueAndNestedExtensions(IParseLocation theLocation) {
		throw new DataFormatException(describeLocation(theLocation) + "Extension contains both a value and nested extensions");
	}

}
