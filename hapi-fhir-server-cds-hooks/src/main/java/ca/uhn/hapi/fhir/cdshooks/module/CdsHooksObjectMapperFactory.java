/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2023 Smile CDR, Inc.
 * %%
 * All rights reserved.
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
		module.addDeserializer(CdsServiceRequestContextJson.class, new CdsServiceRequestContextDeserializer(myFhirContext, retval));
		retval.registerModule(module);
		return retval;

	}
}
