package ca.uhn.fhir.model.dstu2;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.parser.IParser;

public class ModelSerializationTest {

	private static final FhirContext ourCtx = FhirContext.forDstu2();
	
	@Test
	public void testSerialization() throws Exception {
		String input = IOUtils.toString(ModelSerializationTest.class.getResourceAsStream("/diagnosticreport-examples-lab-text(72ac8493-52ac-41bd-8d5d-7258c289b5ea).xml"));
		
		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		Bundle roundTripped = SerializationUtils.roundtrip(parsed);
		
		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		assertEquals(p.encodeResourceToString(parsed), p.encodeResourceToString(roundTripped));
		
	}
	
}
