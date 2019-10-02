package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.validation.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.r4.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SnapshotGeneratorR4Test {

	private FhirContext myFhirCtx = FhirContext.forR4();
	private static final Logger ourLog = LoggerFactory.getLogger(SnapshotGeneratorR4Test.class);

	@Test
	public void testGenerateSnapshot() throws IOException {
		StructureDefinition differential = loadResourceFromClasspath(StructureDefinition.class, "/r4/profile-differential-patient-r4.json");

		// Create a validation chain that includes default validation support and a
		// snapshot generator
		DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport();
		SnapshotGeneratingValidationSupport snapshotGenerator = new SnapshotGeneratingValidationSupport(myFhirCtx, defaultSupport);
		ValidationSupportChain chain = new ValidationSupportChain(defaultSupport, snapshotGenerator);

		// Generate the snapshot
		StructureDefinition snapshot = chain.generateSnapshot(differential, "http://foo", null, "THE BEST PROFILE");

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(snapshot));

		assertEquals(51, snapshot.getSnapshot().getElement().size());
	}



	protected <T extends IBaseResource> T loadResourceFromClasspath(Class<T> type, String resourceName) throws IOException {
		InputStream stream = SnapshotGeneratorR4Test.class.getResourceAsStream(resourceName);
		if (stream == null) {
			fail("Unable to load resource: " + resourceName);
		}
		String string = IOUtils.toString(stream, StandardCharsets.UTF_8);
		IParser newJsonParser = EncodingEnum.detectEncodingNoDefault(string).newParser(myFhirCtx);
		return newJsonParser.parseResource(type, string);
	}

}
