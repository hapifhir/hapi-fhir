package ca.uhn.fhir.parser;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;

public class JsonParserTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParserTest.class);

	@Test
	public void testSimpleEncode() throws IOException {

		FhirContext ctx = new FhirContext(Observation.class);
//		String name = "/observation-example-eeg.xml";
		String name = "/example-patient-general.xml";
		Patient obs = ctx.newXmlParser().parseResource(Patient.class, IOUtils.toString(JsonParser.class.getResourceAsStream(name)));
		String encoded = ctx.newJsonParser().encodeResourceToString(obs);

		ourLog.info(encoded);
	}

}
