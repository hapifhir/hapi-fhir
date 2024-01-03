/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.module;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.serializer.FhirResourceDeserializer;
import ca.uhn.fhir.serializer.FhirResourceSerializer;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestContextJson;
import ca.uhn.hapi.fhir.cdshooks.serializer.CdsServiceRequestContextDeserializer;
import ca.uhn.hapi.fhir.cdshooks.serializer.CdsServiceRequestContextSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class CdsHooksObjectMapperFactory extends ObjectMapper {
	private final FhirContext myFhirContext;

	public CdsHooksObjectMapperFactory(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public ObjectMapper newMapper() {
		Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
		b.indentOutput(true);
		ObjectMapper retval = b.build();
		SimpleModule module = new SimpleModule();
		module.addSerializer(new FhirResourceSerializer(myFhirContext));
		module.addSerializer(new CdsServiceRequestContextSerializer(myFhirContext, retval));
		module.addDeserializer(IBaseResource.class, new FhirResourceDeserializer(myFhirContext));
		module.addDeserializer(
				CdsServiceRequestContextJson.class, new CdsServiceRequestContextDeserializer(myFhirContext, retval));
		retval.registerModule(module);
		return retval;
	}
}
