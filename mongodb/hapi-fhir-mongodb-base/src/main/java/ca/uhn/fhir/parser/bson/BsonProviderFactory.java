/*
 * #%L
 * HAPI FHIR - MongoDB Framework Bundle
 * %%
 * Copyright (C) 2016 - 2020 University Health Network
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
package ca.uhn.fhir.parser.bson;

import java.io.Reader;
import java.io.Writer;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.parser.json.JsonLikeProviderFactory;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import ca.uhn.fhir.parser.json.JsonLikeWriter;

/**
 * This is a dummy FHIR Json Provider factory used with
 * BSON documents. The methods on this instance will all
 * throw exceptions since the BSON serialization does not
 * use a standard {@code Reader} or {@code Writer}. 
 * 
 * This implementation of the Json-Like structures with BSON
 * can only be used within the context of a BSON {@code Codec} 
 *
 * Copyright (C) 2014 - 2020 University Health Network
 * @author williamEdenton@gmail.com
 */
public class BsonProviderFactory implements JsonLikeProviderFactory {

	@Override
	public JsonLikeStructure createJsonLikeStructure (Reader input) {
		throw new ConfigurationException("Trying to use BSON outside the context of a Codec");
	}

	@Override
	public JsonLikeWriter createJsonLikeWriter(Writer output) {
		throw new ConfigurationException("Trying to use BSON outside the context of a Codec");
	}

}
