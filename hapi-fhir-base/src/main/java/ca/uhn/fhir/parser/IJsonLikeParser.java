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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;

import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import ca.uhn.fhir.parser.json.JsonLikeWriter;

/**
 * An extension to the parser interface that is implemented by parsers that understand a generalized form of
 * JSON data. This generalized form uses Map-like, List-like, and scalar elements to construct resources.
 * <p>
 * Thread safety: <b>Parsers are not guaranteed to be thread safe</b>. Create a new parser instance for every thread or
 * every message being parsed/encoded.
 * </p>
 */
public interface IJsonLikeParser extends IParser {

	void encodeResourceToJsonLikeWriter(IBaseResource theResource, JsonLikeWriter theJsonLikeWriter) throws IOException, DataFormatException;

	/**
	 * Parses a resource from a JSON-like data structure
	 * 
	 * @param theResourceType
	 *           The resource type to use. This can be used to explicitly specify a class which extends a built-in type
	 *           (e.g. a custom type extending the default Patient class)
	 * @param theJsonLikeStructure
	 *           The JSON-like structure to parse
	 * @return A parsed resource
	 * @throws DataFormatException
	 *            If the resource can not be parsed because the data is not recognized or invalid for any reason
	 */
	<T extends IBaseResource> T parseResource(Class<T> theResourceType, JsonLikeStructure theJsonLikeStructure) throws DataFormatException;

	/**
	 * Parses a resource from a JSON-like data structure
	 * 
	 * @param theJsonLikeStructure
	 *           The JSON-like structure to parse
	 * @return A parsed resource. Note that the returned object will be an instance of {@link IResource} or
	 *         {@link IAnyResource} depending on the specific FhirContext which created this parser.
	 * @throws DataFormatException
	 *            If the resource can not be parsed because the data is not recognized or invalid for any reason
	 */
	IBaseResource parseResource(JsonLikeStructure theJsonLikeStructure) throws DataFormatException;

}
