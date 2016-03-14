package ca.uhn.fhir.model.dstu2;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;

public class CompartmentDstu2Test {
	
	private static FhirContext ourCtx = FhirContext.forDstu2();
	
	@Test
	public void testMembership() {
		
		Patient p1 = new Patient();
		p1.setId("PID1");
		
		Patient p2 = new Patient();
		p2.setId("PID2");

		Observation o = new Observation();
		o.getSubject().setReference("Patient/PID1");
		
		assertTrue(ourCtx.newTerser().isSourceInCompartmentForTarget("Patient", o, p1));
		assertFalse(ourCtx.newTerser().isSourceInCompartmentForTarget("Patient", o, p2));
	}
	
}
