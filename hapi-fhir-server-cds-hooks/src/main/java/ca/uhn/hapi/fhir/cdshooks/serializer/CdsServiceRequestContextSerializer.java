/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2023 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package ca.uhn.hapi.fhir.cdshooks.serializer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestContextJson;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;

public class CdsServiceRequestContextSerializer extends StdSerializer<CdsServiceRequestContextJson> {
	private final IParser myParser;
	private final ObjectMapper myObjectMapper;

	public CdsServiceRequestContextSerializer(FhirContext theFhirContext, ObjectMapper theObjectMapper) {
		super(CdsServiceRequestContextJson.class);
		myParser = theFhirContext.newJsonParser().setPrettyPrint(true);
		myObjectMapper = theObjectMapper;
	}

	@Override
	public void serialize(CdsServiceRequestContextJson theContext, JsonGenerator theJsonGenerator, SerializerProvider theProvider) throws IOException {
		theJsonGenerator.writeStartObject();
		for (String key : theContext.getKeys()) {
			theJsonGenerator.writeFieldName(key);
			Object value = theContext.get(key);
			String json;
			if (value instanceof IBaseResource) {
				IBaseResource resource = (IBaseResource) value;
				json = myParser.encodeResourceToString(resource);
			} else {
				json = myObjectMapper.writeValueAsString(value);
			}
			theJsonGenerator.writeRawValue(json);
		}
		theJsonGenerator.writeEndObject();
	}
}
