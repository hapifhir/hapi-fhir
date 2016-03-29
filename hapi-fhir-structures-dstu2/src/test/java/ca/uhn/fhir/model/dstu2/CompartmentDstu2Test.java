package ca.uhn.fhir.model.dstu2;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

public class CompartmentDstu2Test {
	
	private static FhirContext ourCtx = FhirContext.forDstu2();
	
	@Test
	public void testMembership() {
		
		Observation o = new Observation();
		o.getSubject().setReference("Patient/PID1");
		
		assertTrue(ourCtx.newTerser().isSourceInCompartmentForTarget("Patient", o, new IdDt("Patient/PID1")));
		assertFalse(ourCtx.newTerser().isSourceInCompartmentForTarget("Patient", o, new IdDt("Patient/PID2")));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBadArguments() {
		Observation o = new Observation();
		o.getSubject().setReference("Patient/PID1");
		ourCtx.newTerser().isSourceInCompartmentForTarget("Patient", o, new IdDt("123"));
	}

}
