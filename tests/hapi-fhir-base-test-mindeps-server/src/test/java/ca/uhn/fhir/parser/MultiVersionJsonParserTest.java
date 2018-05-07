package ca.uhn.fhir.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;

public class MultiVersionJsonParserTest {
	
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MultiVersionJsonParserTest.class);
	
	@Test
	public void testEncodeExtensionFromDifferentVersion() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:sys").setValue("001");
		p.addUndeclaredExtension(false, "http://foo#ext", new QuantityDt(2.2));
		
		try {
			FhirContext.forDstu3().newJsonParser().encodeResourceToString(p);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("This parser is for FHIR version DSTU3 - Can not encode a structure for version DSTU2", e.getMessage());
		}
	}

}
