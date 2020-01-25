/*
 * #%L
 * HAPI FHIR - OSGi Server Framework Bundle
 * %%
 * Copyright (C) 2016 - 2019 University Health Network
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

/**
 * Implementations of this interface create instances
 * of the mechanisms used by the JSON Parser to consume
 * and produce the external JSON-like objects using a
 * specific JSON Provider (e.g. Gson, Jackson, etc.)
 *
 * Copyright (C) 2014 - 2020 University Health Network
 * @author williamEdenton@gmail.com
 */
public interface JsonLikeProviderFactory {
	
	/**
	 * Create an instance of the {@code JsonLikeStructure} that
	 * will be used by the {@code JsonParser} to deserialize the
	 * JSON resource using an external JSON Provider
	 * 
	 * @param input the {@code Reader} that accesses the stream
	 *              containing the JSON resource
	 *              
	 * @return a {@code JsonLikeStructure} implementation for the
	 *         specific JSON provider implementation being used. 
	 */
	public JsonLikeStructure createJsonLikeStructure (Reader input);

	/**
	 * Create an instance of the {@code JsonLikeWriter} that will
	 * be used by the JsonParser to externalize Resources using
	 * an external JSON provider.
	 * 
	 * @param output the {@code Writer} already established to
	 *               write the resulting Json-Like byte stream
	 *               to the desired media
	 *               
	 * @return a {@code JsonLikeWriter> implementation for the
	 *         specific JSON provider implementation being used.
	 */
	public JsonLikeWriter createJsonLikeWriter (Writer output);
}
