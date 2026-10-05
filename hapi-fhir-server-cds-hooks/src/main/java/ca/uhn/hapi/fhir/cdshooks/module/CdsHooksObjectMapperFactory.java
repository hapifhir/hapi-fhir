/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestContextJson;
import ca.uhn.fhir.serializer.FhirResourceDeserializer;
import ca.uhn.fhir.serializer.FhirResourceSerializer;
import ca.uhn.hapi.fhir.cdshooks.serializer.CdsServiceRequestContextDeserializer;
import ca.uhn.hapi.fhir.cdshooks.serializer.CdsServiceRequestContextSerializer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

// Jackson 3 migration notes for this class:
//
// 1. "extends ObjectMapper" REMOVED.
//    In Jackson 2 this was already a code smell — the class never used any inherited
//    ObjectMapper methods; it was just a factory. In Jackson 3, ObjectMapper is immutable
//    and not designed to be subclassed as a mutable factory, so the extends is dropped
//    entirely. Callers that typed this as an ObjectMapper should type it as ObjectMapper
//    via the newMapper() return value instead.
//
// 2. Jackson2ObjectMapperBuilder REMOVED.
//    Spring Framework 7.0 deprecated it for removal with no Jackson 3 equivalent.
//    The single feature used here (indentOutput(true)) maps directly to
//    JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).
//
// 3. Chicken-and-egg: CdsServiceRequestContextSerializer and
//    CdsServiceRequestContextDeserializer are constructed with the mapper instance
//    before the module is registered. In Jackson 3 the preferred path is to add
//    modules via the builder before calling build(). We resolve this by building a
//    temporary mapper to pass into the serializer/deserializer constructors, then
//    building the final mapper with the module attached via the builder.
//    The serializer/deserializer will hold a reference to the temp mapper (which has
//    no custom modules). If those classes need the full mapper at runtime, consider
//    refactoring them to accept a Supplier<ObjectMapper> or use a lazy holder.
//
// 4. ObjectMapper.registerModule() is removed from the mutable API in Jackson 3.
//    Module registration moves to MapperBuilder.addModule() at construction time.
//
// 5. FAIL_ON_TRAILING_TOKENS disabled on the temp mapper only.
//    Off in Jackson 2, on in Jackson 3. CdsServiceRequestContextDeserializer reads the "context"
//    property through the temp mapper, which treats it as a whole-document read and then checks
//    that nothing follows. It runs partway through the request, where the next property
//    legitimately does follow, so with the Jackson 3 default any request whose "context" is not
//    the last property fails to parse. The final mapper keeps the check, since there it applies
//    at the top level and correctly rejects anything after the closing brace.
//
// 6. SORT_PROPERTIES_ALPHABETICALLY disabled.
//    Off in Jackson 2, on in Jackson 3. Property order carries no meaning in JSON, so leaving it
//    on should not matter to any client. It is disabled because some tests compare serialized
//    output as text and so depend on the order, and because keeping the output identical to what
//    this endpoint produced before avoids surprising anyone who does the same.

public class CdsHooksObjectMapperFactory {

	private final FhirContext myFhirContext;

	public CdsHooksObjectMapperFactory(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public ObjectMapper newMapper() {
		// Step 1: build a temporary mapper to satisfy the serializer/deserializer
		// constructors that require a mapper reference. These two classes hold onto
		// this instance, so be aware they will NOT see the custom module registered
		// in the final mapper below. Refactor those constructors if that matters.
		JsonMapper tempMapper = JsonMapper.builder()
				.enable(SerializationFeature.INDENT_OUTPUT)
				.disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
				.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
				.build();

		// Step 2: build the module using the temp mapper where required.
		SimpleModule module = new SimpleModule();
		module.addSerializer(new FhirResourceSerializer(myFhirContext));
		module.addSerializer(new CdsServiceRequestContextSerializer(myFhirContext, tempMapper));
		module.addDeserializer(IBaseResource.class, new FhirResourceDeserializer(myFhirContext));
		module.addDeserializer(
				CdsServiceRequestContextJson.class,
				new CdsServiceRequestContextDeserializer(myFhirContext, tempMapper));

		// Step 3: build the real, immutable mapper with the module registered via the builder.
		// Jackson 3: module registration belongs in the builder, not on the built mapper.
		return JsonMapper.builder()
				.enable(SerializationFeature.INDENT_OUTPUT)
				.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
				.addModule(module)
				.build();
	}
}
