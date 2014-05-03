package ca.uhn.fhir.parser;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;

public interface IParser {

	String encodeBundleToString(Bundle theBundle) throws DataFormatException;

	void encodeBundleToWriter(Bundle theBundle, Writer theWriter) throws IOException, DataFormatException;

	String encodeResourceToString(IResource theResource) throws DataFormatException;

	void encodeResourceToWriter(IResource theResource, Writer stringWriter) throws IOException, DataFormatException;

	<T extends IResource> Bundle parseBundle(Class<T> theResourceType, Reader theReader);

	Bundle parseBundle(Reader theReader);

	Bundle parseBundle(String theMessageString) throws ConfigurationException, DataFormatException;

	/**
	 * Parses a resource
	 * 
	 * @param theResourceType
	 *            The resource type to use. This can be used to explicitly
	 *            specify a class which extends a built-in type (e.g. a custom
	 *            type extending the default Patient class)
	 * @param theReader
	 *            The reader to parse inpou from
	 * @return A parsed resource
	 * @throws DataFormatException
	 *             If the resource can not be parsed because the data is not
	 *             recognized or invalid for any reason
	 */
	<T extends IResource> T parseResource(Class<T> theResourceType, Reader theReader) throws DataFormatException;

	/**
	 * Parses a resource
	 * 
	 * @param theResourceType
	 *            The resource type to use. This can be used to explicitly
	 *            specify a class which extends a built-in type (e.g. a custom
	 *            type extending the default Patient class)
	 * @param theString
	 *            The string to parse
	 * @return A parsed resource
	 * @throws DataFormatException
	 *             If the resource can not be parsed because the data is not
	 *             recognized or invalid for any reason
	 */
	<T extends IResource> T parseResource(Class<T> theResourceType, String theString) throws DataFormatException;

	IResource parseResource(Reader theReader) throws ConfigurationException, DataFormatException;

	IResource parseResource(String theMessageString) throws ConfigurationException, DataFormatException;

	/**
	 * Sets the "pretty print" flag, meaning that the parser will encode
	 * resources with human-readable spacing and newlines between elements
	 * instead of condensing output as much as possible.
	 * 
	 * @param thePrettyPrint
	 *            The flag
	 * @return Returns an instance of <code>this</code> parser so that method
	 *         calls can be conveniently chained
	 */
	IParser setPrettyPrint(boolean thePrettyPrint);

	/**
	 * If set to <code>true</code> (default is <code>false</code>), narratives
	 * will not be included in the encoded values.
	 */
	IParser setSuppressNarratives(boolean theSuppressNarratives);

}
