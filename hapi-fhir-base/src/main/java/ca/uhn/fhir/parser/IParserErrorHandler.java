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
 * Error handler
 */
public interface IParserErrorHandler {

	/**
	 * Invoked when a contained resource is parsed that has no ID specified (and is therefore invalid)
	 *
	 * @param theLocation The location in the document. WILL ALWAYS BE NULL currently, as this is not yet implemented, but this parameter is included so that locations can be added in the future without
	 *                    changing the API.
	 * @since 2.0
	 */
	void containedResourceWithNoId(IParseLocation theLocation);

	/**
	 * Invoked if the wrong type of element is found while parsing JSON. For example if a given element is
	 * expected to be a JSON Object and is a JSON array
	 *
	 * @param theLocation           The location in the document. Note that this may be <code>null</code> as the ParseLocation feature is experimental. Use with caution, as the API may change.
	 * @param theElementName        The name of the element that was found.
	 * @param theExpectedValueType  The datatype that was expected at this location
	 * @param theExpectedScalarType If theExpectedValueType is {@link ValueType#SCALAR}, this is the specific scalar type expected. Otherwise this parameter will be null.
	 * @param theFoundValueType     The datatype that was found at this location
	 * @param theFoundScalarType    If theFoundValueType is {@link ValueType#SCALAR}, this is the specific scalar type found. Otherwise this parameter will be null.
	 * @since 2.2
	 */
	void incorrectJsonType(IParseLocation theLocation, String theElementName, ValueType theExpectedValueType, ScalarType theExpectedScalarType, ValueType theFoundValueType, ScalarType theFoundScalarType);

	/**
	 * The parser detected an attribute value that was invalid (such as: empty "" values are not permitted)
	 *
	 * @param theLocation The location in the document. Note that this may be <code>null</code> as the ParseLocation feature is experimental. Use with caution, as the API may change.
	 * @param theValue    The actual value
	 * @param theError    A description of why the value was invalid
	 * @since 2.2
	 */
	void invalidValue(IParseLocation theLocation, String theValue, String theError);

	/**
	 * Resource was missing a required element
	 *
	 * @param theLocation    The location in the document. Note that this may be <code>null</code> as the ParseLocation feature is experimental. Use with caution, as the API may change.
	 * @param theElementName The missing element name
	 * @since 2.1
	 */
	void missingRequiredElement(IParseLocation theLocation, String theElementName);

	/**
	 * Invoked when an element repetition (e.g. a second repetition of something) is found for a field
	 * which is non-repeating.
	 *
	 * @param theLocation    The location in the document. Note that this may be <code>null</code> as the ParseLocation feature is experimental. Use with caution, as the API may change.
	 * @param theElementName The name of the element that was found.
	 * @since 1.2
	 */
	void unexpectedRepeatingElement(IParseLocation theLocation, String theElementName);

	/**
	 * Invoked when an unknown element is found in the document.
	 *
	 * @param theLocation      The location in the document. Note that this may be <code>null</code> as the ParseLocation feature is experimental. Use with caution, as the API may change.
	 * @param theAttributeName The name of the attribute that was found.
	 */
	void unknownAttribute(IParseLocation theLocation, String theAttributeName);

	/**
	 * Invoked when an unknown element is found in the document.
	 *
	 * @param theLocation    The location in the document. Note that this may be <code>null</code> as the ParseLocation feature is experimental. Use with caution, as the API may change.
	 * @param theElementName The name of the element that was found.
	 */
	void unknownElement(IParseLocation theLocation, String theElementName);

	/**
	 * Resource contained a reference that could not be resolved and needs to be resolvable (e.g. because
	 * it is a local reference to an unknown contained resource)
	 *
	 * @param theLocation  The location in the document. Note that this may be <code>null</code> as the ParseLocation feature is experimental. Use with caution, as the API may change.
	 * @param theReference The actual invalid reference (e.g. "#3")
	 * @since 2.0
	 */
	void unknownReference(IParseLocation theLocation, String theReference);

	/**
	 * An extension contains both a value and at least one nested extension
	 *
	 * @param theLoc The location in the document. Note that this may be <code>null</code> as the ParseLocation feature is experimental. Use with caution, as the API may change.
	 */
	void extensionContainsValueAndNestedExtensions(IParseLocation theLocation);

	/**
	 * For now this is an empty interface. Error handling methods include a parameter of this
	 * type which will currently always be set to null. This interface is included here so that
	 * locations can be added to the API in a future release without changing the API.
	 */
	interface IParseLocation {

		/**
		 * Returns the name of the parent element (the element containing the element currently being parsed)
		 *
		 * @since 2.1
		 */
		String getParentElementName();

	}

}
