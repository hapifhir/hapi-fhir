package ca.uhn.fhir.jpa.empi.provider;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmpiProviderMatchR4Test extends BaseProviderR4Test {


	@Override
	@BeforeEach
	public void before() {
		super.before();
		super.loadEmpiSearchParameters();
	}

	@Test
	public void testMatch() {
		Patient jane = buildJanePatient();
		jane.setActive(true);
		Patient createdJane = createPatient(jane);
		Patient newJane = buildJanePatient();

		Bundle result = myEmpiProviderR4.match(newJane);
		assertEquals(1, result.getEntry().size());
		assertEquals(createdJane.getId(), result.getEntryFirstRep().getResource().getId());
	}
}
