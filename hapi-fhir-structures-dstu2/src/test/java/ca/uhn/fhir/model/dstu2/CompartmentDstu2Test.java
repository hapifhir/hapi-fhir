package ca.uhn.fhir.model.dstu2;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.util.TestUtil;

public class CompartmentDstu2Test {
	
	private static FhirContext ourCtx = FhirContext.forDstu2();

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


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
