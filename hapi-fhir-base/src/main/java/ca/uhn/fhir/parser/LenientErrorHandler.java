package ca.uhn.fhir.parser;

import static org.apache.commons.lang3.StringUtils.isBlank;

import ca.uhn.fhir.context.FhirContext;
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
 * The default error handler, which logs issues but does not abort parsing, with only one exception:
 * <p>
 * The {@link #invalidValue(ca.uhn.fhir.parser.IParserErrorHandler.IParseLocation, String, String)}
 * method will throw a {@link DataFormatException} by default since ignoring this type of error
 * can lead to data loss (since invalid values are silently ignored). See
 * {@link #setErrorOnInvalidValue(boolean)} for information on this.
 * </p>
 * 
 * @see IParser#setParserErrorHandler(IParserErrorHandler)
 * @see FhirContext#setParserErrorHandler(IParserErrorHandler)
 */
public class LenientErrorHandler extends BaseErrorHandler implements IParserErrorHandler {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(LenientErrorHandler.class);
	private static final StrictErrorHandler STRICT_ERROR_HANDLER = new StrictErrorHandler();
	private boolean myErrorOnInvalidValue = true;
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
	 * @param theLogErrors
	 *           Should errors be logged?
	 * @since 1.2
	 */
	public LenientErrorHandler(boolean theLogErrors) {
		myLogErrors = theLogErrors;
	}

	@Override
	public void containedResourceWithNoId(IParseLocation theLocation) {
		if (myLogErrors) {
			ourLog.warn("Resource has contained child resource with no ID");
		}
	}

	@Override
	public void incorrectJsonType(IParseLocation theLocation, String theElementName, ValueType theExpected, ScalarType theExpectedScalarType, ValueType theFound, ScalarType theFoundScalarType) {
		if (myLogErrors) {
			if (ourLog.isWarnEnabled()) {
				String message = createIncorrectJsonTypeMessage(theElementName, theExpected, theExpectedScalarType, theFound, theFoundScalarType);
				ourLog.warn(message);
			}
		}
	}

	@Override
	public void invalidValue(IParseLocation theLocation, String theValue, String theError) {
		if (isBlank(theValue) || myErrorOnInvalidValue == false) {
			if (myLogErrors) {
				ourLog.warn("{}Invalid attribute value \"{}\": {}", describeLocation(theLocation), theValue, theError);
			}
		} else {
			STRICT_ERROR_HANDLER.invalidValue(theLocation, theValue, theError);
		}
	}

	/**
	 * If set to <code>false</code> (default is <code>true</code>) invalid values will be logged. By
	 * default invalid attribute values cause this error handler to throw a {@link DataFormatException} (unlike
	 * other methods in this class which default to simply logging errors).
	 * <p>
	 * Note that empty values (e.g. <code>""</code>) will not lead to an error when this is set to
	 * <code>true</code>, only invalid values (e.g. a gender code of <code>foo</code>)
	 * </p>
	 * 
	 * @see #setErrorOnInvalidValue(boolean)
	 */
	public boolean isErrorOnInvalidValue() {
		return myErrorOnInvalidValue;
	}

	@Override
	public void missingRequiredElement(IParseLocation theLocation, String theElementName) {
		if (myLogErrors) {
			ourLog.warn("Resource is missing required element: {}", theElementName);
		}
	}

	/**
	 * If set to <code>false</code> (default is <code>true</code>) invalid values will be logged. By
	 * default invalid attribute values cause this error handler to throw a {@link DataFormatException} (unlike
	 * other methods in this class which default to simply logging errors).
	 * <p>
	 * Note that empty values (e.g. <code>""</code>) will not lead to an error when this is set to
	 * <code>true</code>, only invalid values (e.g. a gender code of <code>foo</code>)
	 * </p>
	 * 
	 * @return Returns a reference to <code>this</code> for easy method chaining
	 * @see #isErrorOnInvalidValue()
	 */
	public LenientErrorHandler setErrorOnInvalidValue(boolean theErrorOnInvalidValue) {
		myErrorOnInvalidValue = theErrorOnInvalidValue;
		return this;
	}

	@Override
	public void unexpectedRepeatingElement(IParseLocation theLocation, String theElementName) {
		if (myLogErrors) {
			ourLog.warn("{}Multiple repetitions of non-repeatable element '{}' found while parsing", describeLocation(theLocation), theElementName);
		}
	}

	@Override
	public void unknownAttribute(IParseLocation theLocation, String theElementName) {
		if (myLogErrors) {
			ourLog.warn("{}Unknown attribute '{}' found while parsing",describeLocation(theLocation),  theElementName);
		}
	}

	@Override
	public void unknownElement(IParseLocation theLocation, String theElementName) {
		if (myLogErrors) {
			ourLog.warn("{}Unknown element '{}' found while parsing", describeLocation(theLocation), theElementName);
		}
	}

	@Override
	public void unknownReference(IParseLocation theLocation, String theReference) {
		if (myLogErrors) {
			ourLog.warn("{}Resource has invalid reference: {}", describeLocation(theLocation), theReference);
		}
	}

	@Override
	public void extensionContainsValueAndNestedExtensions(IParseLocation theLocation) {
		if (myLogErrors) {
			ourLog.warn("{}Extension contains both a value and nested extensions", describeLocation(theLocation));
		}
	}

	public static String createIncorrectJsonTypeMessage(String theElementName, ValueType theExpected, ScalarType theExpectedScalarType, ValueType theFound, ScalarType theFoundScalarType) {
		StringBuilder b = new StringBuilder();
		b.append("Found incorrect type for element ");
		b.append(theElementName);
		b.append(" - Expected ");
		b.append(theExpected.name());
		if (theExpectedScalarType != null) {
			b.append(" (");
			b.append(theExpectedScalarType.name());
			b.append(")");
		}
		b.append(" and found ");
		b.append(theFound.name());
		if (theFoundScalarType != null) {
			b.append(" (");
			b.append(theFoundScalarType.name());
			b.append(")");
		}
		String message = b.toString();
		return message;
	}

}
