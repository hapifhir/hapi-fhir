/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.serializer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;

public class FhirResourceDeserializer extends StdDeserializer<IBaseResource> {
	private final IParser myParser;

	public FhirResourceDeserializer(FhirContext theFhirContext) {
		super(IBaseResource.class);
		myParser = theFhirContext.newJsonParser().setPrettyPrint(true);
	}

	@Override
	public IBaseResource deserialize(JsonParser theJsonParser, DeserializationContext theContext) throws IOException {
		String json = theJsonParser.getCodec().readTree(theJsonParser).toString();
		return myParser.parseResource(json);
	}
}
