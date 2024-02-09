package ca.uhn.fhir.model.dstu2;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class CompartmentDstu2Test {
	
	private static FhirContext ourCtx = FhirContext.forDstu2();

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


	@Test
	public void testMembership() {
		
		Observation o = new Observation();
		o.getSubject().setReference("Patient/PID1");

		assertThat(ourCtx.newTerser().isSourceInCompartmentForTarget("Patient", o, new IdDt("Patient/PID1"))).isTrue();
		assertThat(ourCtx.newTerser().isSourceInCompartmentForTarget("Patient", o, new IdDt("Patient/PID2"))).isFalse();
	}
	
	@Test
	public void testBadArguments() {
		try {
			Observation o = new Observation();
			o.getSubject().setReference("Patient/PID1");
			ourCtx.newTerser().isSourceInCompartmentForTarget("Patient", o, new IdDt("123"));
			fail("");		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("theTarget must have a populated resource type (theTarget.getResourceType() does not return a value)");
		}
	}

}
