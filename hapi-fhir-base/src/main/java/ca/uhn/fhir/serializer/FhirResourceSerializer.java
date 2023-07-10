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
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;

public class FhirResourceSerializer extends StdSerializer<IBaseResource> {
	private final IParser myParser;

	public FhirResourceSerializer(FhirContext theFhirContext) {
		super(IBaseResource.class);
		myParser = theFhirContext.newJsonParser().setPrettyPrint(true);
	}

	@Override
	public void serialize(IBaseResource theResource, JsonGenerator theJsonGenerator, SerializerProvider theProvider) throws IOException {
		String resourceJson = myParser.encodeResourceToString(theResource);
		theJsonGenerator.writeRawValue(resourceJson);
	}
}
