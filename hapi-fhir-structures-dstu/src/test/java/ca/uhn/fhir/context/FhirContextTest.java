package ca.uhn.fhir.context;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.ValueSet;

public class FhirContextTest {

	@Test
	public void testIncrementalScan() {
		
		FhirContext ctx = new FhirContext();
		ctx.getResourceDefinition(ValueSet.class);
		ctx.getResourceDefinition(Patient.class);
	}
	
	@Test
	public void testFindBinary() {
		RuntimeResourceDefinition def = new FhirContext().getResourceDefinition("Binary");
		assertEquals("Binary", def.getName());
	}
	
	@Test
	public void testUnknownVersion() {
		try {
		new FhirContext(FhirVersionEnum.DEV);
		fail();
		} catch (IllegalStateException e) {
			assertThat(e.getMessage(), containsString("Could not find the HAPI-FHIR structure JAR on the classpath for version {0}"));
		}
	}
	
}
