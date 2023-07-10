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
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;
import java.util.LinkedHashMap;

public class CdsServiceRequestContextDeserializer extends StdDeserializer<CdsServiceRequestContextJson> {
	private final IParser myParser;
	private final ObjectMapper myObjectMapper;

	public CdsServiceRequestContextDeserializer(FhirContext theFhirContext, ObjectMapper theObjectMapper) {
		super(CdsServiceRequestContextJson.class);
		myParser = theFhirContext.newJsonParser().setPrettyPrint(true);
		myObjectMapper = theObjectMapper;
	}

	@Override
	public CdsServiceRequestContextJson deserialize(JsonParser theJsonParser, DeserializationContext theContext) throws IOException {
		// First deserialize the context as a LinkedHashMap
		LinkedHashMap<String, Object> map = myObjectMapper.readValue(theJsonParser.getCodec().readTree(theJsonParser).toString(), LinkedHashMap.class);
		CdsServiceRequestContextJson retval = new CdsServiceRequestContextJson();

		for (String key : map.keySet()) {
			Object value = map.get(key);
			// Convert LinkedHashMap entries to Resources
			if (value instanceof LinkedHashMap) {
				String json = myObjectMapper.writeValueAsString(value);
				IBaseResource resource = myParser.parseResource(json);
				retval.put(key, resource);
			} else {
				retval.put(key, value);
			}
		}
		return retval;
	}
}
