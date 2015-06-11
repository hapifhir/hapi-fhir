package ca.uhn.fhir.context;

import static org.junit.Assert.*;

import org.junit.Test;

public class MultiVersionModelScannerTest {

	@Test
	public void testScan() {
		FhirContext ctx = FhirContext.forDstu1();
		
		RuntimeResourceDefinition def;
		def = ctx.getResourceDefinition(ca.uhn.fhir.model.dstu2.resource.Patient.class);
		assertEquals(FhirVersionEnum.DSTU2, def.getStructureVersion());
		
		def = ctx.getResourceDefinition(new ca.uhn.fhir.model.dstu2.resource.Patient());
		assertEquals(FhirVersionEnum.DSTU2, def.getStructureVersion());
		
		def = ctx.getResourceDefinition("Patient");
		assertEquals(FhirVersionEnum.DSTU1, def.getStructureVersion());
		assertEquals(ca.uhn.fhir.model.dstu.resource.Patient.class, def.getImplementingClass());

		def = ctx.getResourceDefinition(FhirVersionEnum.DSTU2, "Patient");
		assertEquals(FhirVersionEnum.DSTU2, def.getStructureVersion());
		assertEquals(ca.uhn.fhir.model.dstu2.resource.Patient.class, def.getImplementingClass());
	}
	
}
