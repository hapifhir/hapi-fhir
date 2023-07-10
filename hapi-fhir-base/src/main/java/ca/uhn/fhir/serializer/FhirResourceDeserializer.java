/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2023 Smile CDR, Inc.
 * %%
 * All rights reserved.
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
