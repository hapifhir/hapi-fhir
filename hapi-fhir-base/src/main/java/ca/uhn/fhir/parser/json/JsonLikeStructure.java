/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
package ca.uhn.fhir.parser.json;

import java.io.Reader;
import java.io.Writer;

import ca.uhn.fhir.parser.DataFormatException;

/**
 * This interface is the generic representation of any sort of data 
 * structure that looks and smells like JSON. These data structures
 * can be abstractly viewed as a <code.Map</code> or <code>List</code>
 * whose members are other Maps, Lists, or scalars (Strings, Numbers, Boolean)
 * 
 * @author Bill.Denton
 */
public interface JsonLikeStructure {
	public JsonLikeStructure getInstance();
	
	/**
	 * Parse the JSON document into the Json-like structure
	 * so that it can be navigated.
	 * 
	 * @param theReader a <code>Reader</code> that will
	 * 			process the JSON input stream
	 * @throws DataFormatException when invalid JSON is received
	 */
	public void load (Reader theReader) throws DataFormatException;
	public void load (Reader theReader, boolean allowArray) throws DataFormatException;
	public JsonLikeObject getRootObject () throws DataFormatException;
	public JsonLikeArray getRootArray () throws DataFormatException;
	public JsonLikeWriter getJsonLikeWriter ();
	public JsonLikeWriter getJsonLikeWriter (Writer writer);
}
