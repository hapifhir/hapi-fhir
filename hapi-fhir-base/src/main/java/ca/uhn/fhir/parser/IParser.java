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

	String encodeBundleToString(Bundle theBundle) throws DataFormatException, IOException;

	void encodeBundleToWriter(Bundle theBundle, Writer theWriter) throws IOException, DataFormatException;

	String encodeResourceToString(IResource theResource) throws DataFormatException, IOException;

	void encodeResourceToWriter(IResource theResource, Writer stringWriter) throws IOException, DataFormatException;

	Bundle parseBundle(Reader theReader);

	Bundle parseBundle(String theMessageString) throws ConfigurationException, DataFormatException;

	IResource parseResource(String theMessageString) throws ConfigurationException, DataFormatException;

	IResource parseResource(Reader theReader) throws ConfigurationException, DataFormatException;

	<T extends IResource> T parseResource(Class<T> theResourceType, String theMessageString);

	<T extends IResource> T parseResource(Class<T> theResourceType, Reader theReader);

	IParser setPrettyPrint(boolean thePrettyPrint);

	/**
	 * If set to <code>true</code> (default is <code>false</code>), narratives will not be included in the
	 * encoded values.
	 */
	IParser setSuppressNarratives(boolean theSuppressNarratives);

}
