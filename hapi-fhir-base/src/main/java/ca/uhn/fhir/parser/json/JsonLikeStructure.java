package ca.uhn.fhir.parser.json;

import java.io.Reader;
import java.io.Writer;

import ca.uhn.fhir.parser.DataFormatException;

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
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public interface JsonLikeStructure {
	public JsonLikeStructure getInstance();
	public void load (Reader theReader) throws DataFormatException;
	public JsonLikeObject getRootObject ();
	public JsonLikeWriter getJsonLikeWriter ();
	public JsonLikeWriter getJsonLikeWriter (Writer writer);
}
