package ca.uhn.fhir.parser;

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
	 * @param theLocation
	 *           The location in the document. WILL ALWAYS BE NULL currently, as this is not yet implemented, but this parameter is included so that locations can be added in the future without
	 *           changing the API.
	 * @since 2.0
	 */
	void containedResourceWithNoId(IParseLocation theLocation);

	/**
	 * Invoked when an element repetition (e.g. a second repetition of something) is found for a field
	 * which is non-repeating.
	 * 
	 * @param theLocation
	 *           The location in the document. WILL ALWAYS BE NULL currently, as this is not yet implemented, but this parameter is included so that locations can be added in the future without
	 *           changing the API.
	 * @param theElementName
	 *           The name of the element that was found.
	 * @since 1.2
	 */
	void unexpectedRepeatingElement(IParseLocation theLocation, String theElementName);

	/**
	 * Invoked when an unknown element is found in the document.
	 * 
	 * @param theLocation
	 *           The location in the document. WILL ALWAYS BE NULL currently, as this is not yet implemented, but this parameter is included so that locations can be added in the future without
	 *           changing the API.
	 * @param theAttributeName
	 *           The name of the attribute that was found.
	 */
	void unknownAttribute(IParseLocation theLocation, String theAttributeName);

	/**
	 * Invoked when an unknown element is found in the document.
	 * 
	 * @param theLocation
	 *           The location in the document. WILL ALWAYS BE NULL currently, as this is not yet implemented, but this parameter is included so that locations can be added in the future without
	 *           changing the API.
	 * @param theElementName
	 *           The name of the element that was found.
	 */
	void unknownElement(IParseLocation theLocation, String theElementName);

	/**
	 * Resource contained a reference that could not be resolved and needs to be resolvable (e.g. because
	 * it is a local reference to an unknown contained resource)
	 * 
	 * @param theLocation
	 *           The location in the document. WILL ALWAYS BE NULL currently, as this is not yet implemented, but this parameter is included so that locations can be added in the future without
	 *           changing the API.
	 * @param theReference The actual invalid reference (e.g. "#3")
	 * @since 2.0
	 */
	void unknownReference(IParseLocation theLocation, String theReference);

	/**
	 * For now this is an empty interface. Error handling methods include a parameter of this
	 * type which will currently always be set to null. This interface is included here so that
	 * locations can be added to the API in a future release without changing the API.
	 */
	public interface IParseLocation {
		// nothing for now
	}

}
