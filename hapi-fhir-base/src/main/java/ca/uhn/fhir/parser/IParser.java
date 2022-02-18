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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.api.EncodingEnum;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A parser, which can be used to convert between HAPI FHIR model/structure objects, and their respective String wire
 * formats, in either XML or JSON.
 * <p>
 * Thread safety: <b>Parsers are not guaranteed to be thread safe</b>. Create a new parser instance for every thread or
 * every message being parsed/encoded.
 * </p>
 */
public interface IParser {

	String encodeResourceToString(IBaseResource theResource) throws DataFormatException;

	void encodeResourceToWriter(IBaseResource theResource, Writer theWriter) throws IOException, DataFormatException;

	/**
	 * If not set to null (as is the default) this ID will be used as the ID in any
	 * resources encoded by this parser
	 */
	IIdType getEncodeForceResourceId();

	/**
	 * When encoding, force this resource ID to be encoded as the resource ID
	 */
	IParser setEncodeForceResourceId(IIdType theForceResourceId);

	/**
	 * Which encoding does this parser instance produce?
	 */
	EncodingEnum getEncoding();

	/**
	 * Gets the preferred types, as set using {@link #setPreferTypes(List)}
	 *
	 * @return Returns the preferred types, or <code>null</code>
	 * @see #setPreferTypes(List)
	 */
	List<Class<? extends IBaseResource>> getPreferTypes();

	/**
	 * If set, when parsing resources the parser will try to use the given types when possible, in
	 * the order that they are provided (from highest to lowest priority). For example, if a custom
	 * type which declares to implement the Patient resource is passed in here, and the
	 * parser is parsing a Bundle containing a Patient resource, the parser will use the given
	 * custom type.
	 * <p>
	 * This feature is related to, but not the same as the
	 * {@link FhirContext#setDefaultTypeForProfile(String, Class)} feature.
	 * <code>setDefaultTypeForProfile</code> is used to specify a type to be used
	 * when a resource explicitly declares support for a given profile. This
	 * feature specifies a type to be used irrespective of the profile declaration
	 * in the metadata statement.
	 * </p>
	 *
	 * @param thePreferTypes The preferred types, or <code>null</code>
	 */
	void setPreferTypes(List<Class<? extends IBaseResource>> thePreferTypes);

	/**
	 * Returns true if resource IDs should be omitted
	 *
	 * @see #setOmitResourceId(boolean)
	 * @since 1.1
	 */
	boolean isOmitResourceId();

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the ID of any resources being encoded will not be
	 * included in the output. Note that this does not apply to contained resources, only to root resources. In other
	 * words, if this is set to <code>true</code>, contained resources will still have local IDs but the outer/containing
	 * ID will not have an ID.
	 * <p>
	 * If the resource being encoded is a Bundle or Parameters resource, this setting only applies to the
	 * outer resource being encoded, not any resources contained wihthin.
	 * </p>
	 *
	 * @param theOmitResourceId Should resource IDs be omitted
	 * @return Returns a reference to <code>this</code> parser so that method calls can be chained together
	 * @since 1.1
	 */
	IParser setOmitResourceId(boolean theOmitResourceId);

	/**
	 * If set to <code>true<code> (which is the default), resource references containing a version
	 * will have the version removed when the resource is encoded. This is generally good behaviour because
	 * in most situations, references from one resource to another should be to the resource by ID, not
	 * by ID and version. In some cases though, it may be desirable to preserve the version in resource
	 * links. In that case, this value should be set to <code>false</code>.
	 *
	 * @return Returns the parser instance's configuration setting for stripping versions from resource references when
	 * encoding. This method will retun <code>null</code> if no value is set, in which case
	 * the value from the {@link ParserOptions} will be used (default is <code>true</code>)
	 * @see ParserOptions
	 */
	Boolean getStripVersionsFromReferences();

	/**
	 * If set to <code>true<code> (which is the default), resource references containing a version
	 * will have the version removed when the resource is encoded. This is generally good behaviour because
	 * in most situations, references from one resource to another should be to the resource by ID, not
	 * by ID and version. In some cases though, it may be desirable to preserve the version in resource
	 * links. In that case, this value should be set to <code>false</code>.
	 * <p>
	 * This method provides the ability to globally disable reference encoding. If finer-grained
	 * control is needed, use {@link #setDontStripVersionsFromReferencesAtPaths(String...)}
	 * </p>
	 *
	 * @param theStripVersionsFromReferences Set this to <code>false<code> to prevent the parser from removing resource versions from references (or <code>null</code> to apply the default setting from the {@link ParserOptions}
	 * @return Returns a reference to <code>this</code> parser so that method calls can be chained together
	 * @see #setDontStripVersionsFromReferencesAtPaths(String...)
	 * @see ParserOptions
	 */
	IParser setStripVersionsFromReferences(Boolean theStripVersionsFromReferences);

	/**
	 * Is the parser in "summary mode"? See {@link #setSummaryMode(boolean)} for information
	 *
	 * @see {@link #setSummaryMode(boolean)} for information
	 */
	boolean isSummaryMode();

	/**
	 * If set to <code>true</code> (default is <code>false</code>) only elements marked by the FHIR specification as
	 * being "summary elements" will be included.
	 *
	 * @return Returns a reference to <code>this</code> parser so that method calls can be chained together
	 */
	IParser setSummaryMode(boolean theSummaryMode);

	/**
	 * Parses a resource
	 *
	 * @param theResourceType The resource type to use. This can be used to explicitly specify a class which extends a built-in type
	 *                        (e.g. a custom type extending the default Patient class)
	 * @param theReader       The reader to parse input from. Note that the Reader will not be closed by the parser upon completion.
	 * @return A parsed resource
	 * @throws DataFormatException If the resource can not be parsed because the data is not recognized or invalid for any reason
	 */
	<T extends IBaseResource> T parseResource(Class<T> theResourceType, Reader theReader) throws DataFormatException;

	/**
	 * Parses a resource
	 *
	 * @param theResourceType The resource type to use. This can be used to explicitly specify a class which extends a built-in type
	 *                        (e.g. a custom type extending the default Patient class)
	 * @param theInputStream  The InputStream to parse input from, <b>with an implied charset of UTF-8</b>. Note that the InputStream will not be closed by the parser upon completion.
	 * @return A parsed resource
	 * @throws DataFormatException If the resource can not be parsed because the data is not recognized or invalid for any reason
	 */
	<T extends IBaseResource> T parseResource(Class<T> theResourceType, InputStream theInputStream) throws DataFormatException;

	/**
	 * Parses a resource
	 *
	 * @param theResourceType The resource type to use. This can be used to explicitly specify a class which extends a built-in type
	 *                        (e.g. a custom type extending the default Patient class)
	 * @param theString       The string to parse
	 * @return A parsed resource
	 * @throws DataFormatException If the resource can not be parsed because the data is not recognized or invalid for any reason
	 */
	<T extends IBaseResource> T parseResource(Class<T> theResourceType, String theString) throws DataFormatException;

	/**
	 * Parses a resource
	 *
	 * @param theReader The reader to parse input from. Note that the Reader will not be closed by the parser upon completion.
	 * @return A parsed resource. Note that the returned object will be an instance of {@link IResource} or
	 * {@link IAnyResource} depending on the specific FhirContext which created this parser.
	 * @throws DataFormatException If the resource can not be parsed because the data is not recognized or invalid for any reason
	 */
	IBaseResource parseResource(Reader theReader) throws ConfigurationException, DataFormatException;

	/**
	 * Parses a resource
	 *
	 * @param theInputStream The InputStream to parse input from (charset is assumed to be UTF-8).
	 *                       Note that the stream will not be closed by the parser upon completion.
	 * @return A parsed resource. Note that the returned object will be an instance of {@link IResource} or
	 * {@link IAnyResource} depending on the specific FhirContext which created this parser.
	 * @throws DataFormatException If the resource can not be parsed because the data is not recognized or invalid for any reason
	 */
	IBaseResource parseResource(InputStream theInputStream) throws ConfigurationException, DataFormatException;

	/**
	 * Parses a resource
	 *
	 * @param theMessageString The string to parse
	 * @return A parsed resource. Note that the returned object will be an instance of {@link IResource} or
	 * {@link IAnyResource} depending on the specific FhirContext which created this parser.
	 * @throws DataFormatException If the resource can not be parsed because the data is not recognized or invalid for any reason
	 */
	IBaseResource parseResource(String theMessageString) throws ConfigurationException, DataFormatException;

	/**
	 * If provided, specifies the elements which should NOT be encoded. Valid values for this
	 * field would include:
	 * <ul>
	 * <li><b>Patient</b> - Don't encode patient and all its children</li>
	 * <li><b>Patient.name</b> - Don't encode the patient's name</li>
	 * <li><b>Patient.name.family</b> - Don't encode the patient's family name</li>
	 * <li><b>*.text</b> - Don't encode the text element on any resource (only the very first position may contain a
	 * wildcard)</li>
	 * </ul>
	 * <p>
	 * DSTU2 note: Note that values including meta, such as <code>Patient.meta</code>
	 * will work for DSTU2 parsers, but values with subelements on meta such
	 * as <code>Patient.meta.lastUpdated</code> will only work in
	 * DSTU3+ mode.
	 * </p>
	 *
	 * @param theDontEncodeElements The elements to encode
	 * @see #setEncodeElements(Set)
	 */
	IParser setDontEncodeElements(Collection<String> theDontEncodeElements);

	/**
	 * If provided, specifies the elements which should be encoded, to the exclusion of all others. Valid values for this
	 * field would include:
	 * <ul>
	 * <li><b>Patient</b> - Encode patient and all its children</li>
	 * <li><b>Patient.name</b> - Encode only the patient's name</li>
	 * <li><b>Patient.name.family</b> - Encode only the patient's family name</li>
	 * <li><b>*.text</b> - Encode the text element on any resource (only the very first position may contain a
	 * wildcard)</li>
	 * <li><b>*.(mandatory)</b> - This is a special case which causes any mandatory fields (min > 0) to be encoded</li>
	 * </ul>
	 *
	 * @param theEncodeElements The elements to encode
	 * @see #setDontEncodeElements(Collection)
	 */
	IParser setEncodeElements(Set<String> theEncodeElements);

	/**
	 * If set to <code>true</code> (default is false), the values supplied
	 * to {@link #setEncodeElements(Set)} will not be applied to the root
	 * resource (typically a Bundle), but will be applied to any sub-resources
	 * contained within it (i.e. search result resources in that bundle)
	 */
	boolean isEncodeElementsAppliesToChildResourcesOnly();

	/**
	 * If set to <code>true</code> (default is false), the values supplied
	 * to {@link #setEncodeElements(Set)} will not be applied to the root
	 * resource (typically a Bundle), but will be applied to any sub-resources
	 * contained within it (i.e. search result resources in that bundle)
	 */
	void setEncodeElementsAppliesToChildResourcesOnly(boolean theEncodeElementsAppliesToChildResourcesOnly);

	/**
	 * Registers an error handler which will be invoked when any parse errors are found
	 *
	 * @param theErrorHandler The error handler to set. Must not be null.
	 */
	IParser setParserErrorHandler(IParserErrorHandler theErrorHandler);

	/**
	 * Sets the "pretty print" flag, meaning that the parser will encode resources with human-readable spacing and
	 * newlines between elements instead of condensing output as much as possible.
	 *
	 * @param thePrettyPrint The flag
	 * @return Returns an instance of <code>this</code> parser so that method calls can be chained together
	 */
	IParser setPrettyPrint(boolean thePrettyPrint);

	/**
	 * Sets the server's base URL used by this parser. If a value is set, resource references will be turned into
	 * relative references if they are provided as absolute URLs but have a base matching the given base.
	 *
	 * @param theUrl The base URL, e.g. "http://example.com/base"
	 * @return Returns an instance of <code>this</code> parser so that method calls can be chained together
	 */
	IParser setServerBaseUrl(String theUrl);

	/**
	 * If set to <code>true</code> (which is the default), the Bundle.entry.fullUrl will override the Bundle.entry.resource's
	 * resource id if the fullUrl is defined. This behavior happens when parsing the source data into a Bundle object. Set this
	 * to <code>false</code> if this is not the desired behavior (e.g. the client code wishes to perform additional
	 * validation checks between the fullUrl and the resource id).
	 *
	 * @param theOverrideResourceIdWithBundleEntryFullUrl Set this to <code>false</code> to prevent the parser from overriding resource ids with the
	 *                                                    Bundle.entry.fullUrl (or <code>null</code> to apply the default setting from the {@link ParserOptions})
	 * @return Returns a reference to <code>this</code> parser so that method calls can be chained together
	 * @see ParserOptions
	 */
	IParser setOverrideResourceIdWithBundleEntryFullUrl(Boolean theOverrideResourceIdWithBundleEntryFullUrl);

	/**
	 * If set to <code>true</code> (default is <code>false</code>), narratives will not be included in the encoded
	 * values.
	 */
	IParser setSuppressNarratives(boolean theSuppressNarratives);

	/**
	 * Returns the value supplied to {@link IParser#setDontStripVersionsFromReferencesAtPaths(String...)}
	 * or <code>null</code> if no value has been set for this parser (in which case the default from
	 * the {@link ParserOptions} will be used}
	 *
	 * @see #setDontStripVersionsFromReferencesAtPaths(String...)
	 * @see #setStripVersionsFromReferences(Boolean)
	 * @see ParserOptions
	 */
	Set<String> getDontStripVersionsFromReferencesAtPaths();

	/**
	 * If supplied value(s), any resource references at the specified paths will have their
	 * resource versions encoded instead of being automatically stripped during the encoding
	 * process. This setting has no effect on the parsing process.
	 * <p>
	 * This method provides a finer-grained level of control than {@link #setStripVersionsFromReferences(Boolean)}
	 * and any paths specified by this method will be encoded even if {@link #setStripVersionsFromReferences(Boolean)}
	 * has been set to <code>true</code> (which is the default)
	 * </p>
	 *
	 * @param thePaths A collection of paths for which the resource versions will not be removed automatically
	 *                 when serializing, e.g. "Patient.managingOrganization" or "AuditEvent.object.reference". Note that
	 *                 only resource name and field names with dots separating is allowed here (no repetition
	 *                 indicators, FluentPath expressions, etc.). Set to <code>null</code> to use the value
	 *                 set in the {@link ParserOptions}
	 * @return Returns a reference to <code>this</code> parser so that method calls can be chained together
	 * @see #setStripVersionsFromReferences(Boolean)
	 * @see ParserOptions
	 */
	IParser setDontStripVersionsFromReferencesAtPaths(String... thePaths);

	/**
	 * If supplied value(s), any resource references at the specified paths will have their
	 * resource versions encoded instead of being automatically stripped during the encoding
	 * process. This setting has no effect on the parsing process.
	 * <p>
	 * This method provides a finer-grained level of control than {@link #setStripVersionsFromReferences(Boolean)}
	 * and any paths specified by this method will be encoded even if {@link #setStripVersionsFromReferences(Boolean)}
	 * has been set to <code>true</code> (which is the default)
	 * </p>
	 *
	 * @param thePaths A collection of paths for which the resource versions will not be removed automatically
	 *                 when serializing, e.g. "Patient.managingOrganization" or "AuditEvent.object.reference". Note that
	 *                 only resource name and field names with dots separating is allowed here (no repetition
	 *                 indicators, FluentPath expressions, etc.). Set to <code>null</code> to use the value
	 *                 set in the {@link ParserOptions}
	 * @return Returns a reference to <code>this</code> parser so that method calls can be chained together
	 * @see #setStripVersionsFromReferences(Boolean)
	 * @see ParserOptions
	 */
	IParser setDontStripVersionsFromReferencesAtPaths(Collection<String> thePaths);

}
