package ca.uhn.fhir.parser;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.StringContains;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.resource.Patient;

public class MultiVersionJsonParserTest {
	
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MultiVersionJsonParserTest.class);
	
	@Test
	public void testEncodeExtensionFromDifferentVersion() {
		Patient p = new Patient();
		p.addIdentifier("urn:sys", "001");
		p.addUndeclaredExtension(false, "http://foo#ext", new QuantityDt(2.2));
		
		String str = FhirContext.forDstu2().newJsonParser().encodeResourceToString(p);
		ourLog.info(str);

		assertThat(str, StringContains.containsString("{\"resourceType\":\"Patient\",\"extension\":[{\"url\":\"http://foo#ext\",\"valueQuantity\":{\"value\":2.2}}],\"identifier\":[{\"system\":\"urn:sys\",\"value\":\"001\"}]}"));
	}

}
