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
 * Json Provider Factory implementation for Google Gson
 *
 * Copyright (C) 2014 - 2019 University Health Network
 * @author williamEdenton@gmail.com
 */
public class GsonProviderFactory implements JsonLikeProviderFactory {

	@Override
	public JsonLikeStructure createJsonLikeStructure (Reader theReader) {
		GsonStructure jsonStructure = new GsonStructure();
		jsonStructure.load(theReader);
		return jsonStructure;
	}

	@Override
	public JsonLikeWriter createJsonLikeWriter (Writer theWriter) {
		GsonWriter jsonWriter = new GsonWriter(theWriter);
		return jsonWriter;
	}

}
