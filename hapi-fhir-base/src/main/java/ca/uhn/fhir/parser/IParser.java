package ca.uhn.fhir.parser;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.server.EncodingEnum;

/**
 * A parser, which can be used to convert between HAPI FHIR model/structure objects, and their respective String wire
 * formats, in either XML or JSON.
 * <p>
 * Thread safety: <b>Parsers are not guaranteed to be thread safe</b>. Create a new parser instance for every thread or
 * every message being parsed/encoded.
 * </p>
 */
public interface IParser {

	String encodeBundleToString(Bundle theBundle) throws DataFormatException;

	void encodeBundleToWriter(Bundle theBundle, Writer theWriter) throws IOException, DataFormatException;

	String encodeResourceToString(IBaseResource theResource) throws DataFormatException;

	void encodeResourceToWriter(IBaseResource theResource, Writer theWriter) throws IOException, DataFormatException;

	/**
	 * Encodes a tag list, as defined in the <a href="http://hl7.org/implement/standards/fhir/http.html#tags">FHIR
	 * Specification</a>.
	 * 
	 * @param theTagList
	 *           The tag list to encode. Must not be null.
	 * @return An encoded tag list
	 */
	String encodeTagListToString(TagList theTagList);

	/**
	 * Encodes a tag list, as defined in the <a href="http://hl7.org/implement/standards/fhir/http.html#tags">FHIR
	 * Specification</a>.
	 * 
	 * @param theTagList
	 *           The tag list to encode. Must not be null.
	 * @param theWriter
	 *           The writer to encode to
	 */
	void encodeTagListToWriter(TagList theTagList, Writer theWriter) throws IOException;

	/**
	 * See {@link #setEncodeElements(Set)}
	 */
	Set<String> getEncodeElements();

	/**
	 * See {@link #setEncodeElementsAppliesToResourceTypes(Set)}
	 */
	Set<String> getEncodeElementsAppliesToResourceTypes();

	/**
	 * Returns true if resource IDs should be omitted
	 * 
	 * @see #setOmitResourceId(boolean)
	 * @since 1.1
	 */
	boolean isOmitResourceId();

	/**
	 * If set to <code>true<code> (which is the default), resource references containing a version 
	 * will have the version removed when the resource is encoded. This is generally good behaviour because
	 * in most situations, references from one resource to another should be to the resource by ID, not
	 * by ID and version. In some cases though, it may be desirable to preserve the version in resource
	 * links. In that case, this value should be set to <code>false</code>.
	 * 
	 * @return Returns the parser instance's configuration setting for stripping versions from resource references when
	 *         encoding. Default is <code>true</code>.
	 */
	boolean isStripVersionsFromReferences();

	/**
	 * Is the parser in "summary mode"? See {@link #setSummaryMode(boolean)} for information
	 * 
	 * @see {@link #setSummaryMode(boolean)} for information
	 */
	boolean isSummaryMode();

	/**
	 * Parse a DSTU1 style Atom Bundle. Note that as of DSTU2, Bundle is a resource so you should use
	 * {@link #parseResource(Class, Reader)} with the Bundle class found in the
	 * <code>ca.uhn.hapi.fhir.model.[version].resource</code> package instead.
	 */
	<T extends IBaseResource> Bundle parseBundle(Class<T> theResourceType, Reader theReader);

	/**
	 * Parse a DSTU1 style Atom Bundle. Note that as of DSTU2, Bundle is a resource so you should use
	 * {@link #parseResource(Class, Reader)} with the Bundle class found in the
	 * <code>ca.uhn.hapi.fhir.model.[version].resource</code> package instead.
	 */
	Bundle parseBundle(Reader theReader);

	/**
	 * Parse a DSTU1 style Atom Bundle. Note that as of DSTU2, Bundle is a resource so you should use
	 * {@link #parseResource(Class, String)} with the Bundle class found in the
	 * <code>ca.uhn.hapi.fhir.model.[version].resource</code> package instead.
	 */
	Bundle parseBundle(String theMessageString) throws ConfigurationException, DataFormatException;

	/**
	 * Parses a resource
	 * 
	 * @param theResourceType
	 *           The resource type to use. This can be used to explicitly specify a class which extends a built-in type
	 *           (e.g. a custom type extending the default Patient class)
	 * @param theReader
	 *           The reader to parse input from. Note that the Reader will not be closed by the parser upon completion.
	 * @return A parsed resource
	 * @throws DataFormatException
	 *            If the resource can not be parsed because the data is not recognized or invalid for any reason
	 */
	<T extends IBaseResource> T parseResource(Class<T> theResourceType, Reader theReader) throws DataFormatException;

	/**
	 * Parses a resource
	 * 
	 * @param theResourceType
	 *           The resource type to use. This can be used to explicitly specify a class which extends a built-in type
	 *           (e.g. a custom type extending the default Patient class)
	 * @param theString
	 *           The string to parse
	 * @return A parsed resource
	 * @throws DataFormatException
	 *            If the resource can not be parsed because the data is not recognized or invalid for any reason
	 */
	<T extends IBaseResource> T parseResource(Class<T> theResourceType, String theString) throws DataFormatException;

	/**
	 * Parses a resource
	 * 
	 * @param theReader
	 *           The reader to parse input from. Note that the Reader will not be closed by the parser upon completion.
	 * @return A parsed resource. Note that the returned object will be an instance of {@link IResource} or
	 *         {@link IAnyResource} depending on the specific FhirContext which created this parser.
	 * @throws DataFormatException
	 *            If the resource can not be parsed because the data is not recognized or invalid for any reason
	 */
	IBaseResource parseResource(Reader theReader) throws ConfigurationException, DataFormatException;

	/**
	 * Parses a resource
	 * 
	 * @param theMessageString
	 *           The string to parse
	 * @return A parsed resource. Note that the returned object will be an instance of {@link IResource} or
	 *         {@link IAnyResource} depending on the specific FhirContext which created this parser.
	 * @throws DataFormatException
	 *            If the resource can not be parsed because the data is not recognized or invalid for any reason
	 */
	IBaseResource parseResource(String theMessageString) throws ConfigurationException, DataFormatException;

	/**
	 * Parses a tag list, as defined in the <a href="http://hl7.org/implement/standards/fhir/http.html#tags">FHIR
	 * Specification</a>.
	 * 
	 * @param theReader
	 *           A reader which will supply a tag list
	 * @return A parsed tag list
	 */
	TagList parseTagList(Reader theReader);

	/**
	 * Parses a tag list, as defined in the <a href="http://hl7.org/implement/standards/fhir/http.html#tags">FHIR
	 * Specification</a>.
	 * 
	 * @param theString
	 *           A string containing a tag list
	 * @return A parsed tag list
	 */
	TagList parseTagList(String theString);

	/**
	 * If provided, specifies the elements which should be encoded, to the exclusion of all others. Valid values for this
	 * field would include:
	 * <ul>
	 * <li><b>Patient</b> - Encode patient and all its children</li>
	 * <li><b>Patient.name</b> - Encoding only the patient's name</li>
	 * <li><b>Patient.name.family</b> - Encode only the patient's family name</li>
	 * <li><b>*.text</b> - Encode the text element on any resource (only the very first position may contain a wildcard)</li>
	 * </ul>
	 * 
	 * @param theEncodeElements
	 *           The elements to encode
	 */
	void setEncodeElements(Set<String> theEncodeElements);

	/**
	 * If provided, tells the parse which resource types to apply {@link #setEncodeElements(Set) encode elements} to. Any
	 * resource types not specified here will be encoded completely, with no elements excluded.
	 * 
	 * @param theEncodeElementsAppliesToResourceTypes
	 */
	void setEncodeElementsAppliesToResourceTypes(Set<String> theEncodeElementsAppliesToResourceTypes);

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the ID of any resources being encoded will not be
	 * included in the output. Note that this does not apply to contained resources, only to root resources. In other
	 * words, if this is set to <code>true</code>, contained resources will still have local IDs but the outer/containing
	 * ID will not have an ID.
	 * 
	 * @param theOmitResourceId
	 *           Should resource IDs be omitted
	 * @return Returns a reference to <code>this</code> parser so that method calls can be chained together
	 * @since 1.1
	 */
	IParser setOmitResourceId(boolean theOmitResourceId);

	/**
	 * Registers an error handler which will be invoked when any parse errors are found
	 * 
	 * @param theErrorHandler
	 *           The error handler to set. Must not be null.
	 */
	IParser setParserErrorHandler(IParserErrorHandler theErrorHandler);

	/**
	 * Sets the "pretty print" flag, meaning that the parser will encode resources with human-readable spacing and
	 * newlines between elements instead of condensing output as much as possible.
	 * 
	 * @param thePrettyPrint
	 *           The flag
	 * @return Returns an instance of <code>this</code> parser so that method calls can be chained together
	 */
	IParser setPrettyPrint(boolean thePrettyPrint);

	/**
	 * Sets the server's base URL used by this parser. If a value is set, resource references will be turned into
	 * relative references if they are provided as absolute URLs but have a base matching the given base.
	 * 
	 * @param theUrl
	 *           The base URL, e.g. "http://example.com/base"
	 * @return Returns an instance of <code>this</code> parser so that method calls can be chained together
	 */
	IParser setServerBaseUrl(String theUrl);

	/**
	 * If set to <code>true<code> (which is the default), resource references containing a version 
	 * will have the version removed when the resource is encoded. This is generally good behaviour because
	 * in most situations, references from one resource to another should be to the resource by ID, not
	 * by ID and version. In some cases though, it may be desirable to preserve the version in resource
	 * links. In that case, this value should be set to <code>false</code>.
	 * 
	 * @param theStripVersionsFromReferences
	 *           Set this to <code>false<code> to prevent the parser from removing
	 * resource versions from references.
	 * @return Returns a reference to <code>this</code> parser so that method calls can be chained together
	 */
	IParser setStripVersionsFromReferences(boolean theStripVersionsFromReferences);

	/**
	 * If set to <code>true</code> (default is <code>false</code>) only elements marked by the FHIR specification as
	 * being "summary elements" will be included.
	 * 
	 * @return Returns a reference to <code>this</code> parser so that method calls can be chained together
	 */
	IParser setSummaryMode(boolean theSummaryMode);

	/**
	 * If set to <code>true</code> (default is <code>false</code>), narratives will not be included in the encoded
	 * values.
	 */
	IParser setSuppressNarratives(boolean theSuppressNarratives);

	/**
	 * Which encoding does this parser instance produce?
	 */
	EncodingEnum getEncoding();

}
