package org.hl7.fhir.instance.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.utils.IWorkerContext;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HapiWorkerContextTest {

	@Test
	public void testIdTypes(){

		HapiWorkerContext hwc = new HapiWorkerContext(FhirContext.forDstu2(), new DefaultProfileValidationSupport());
		ValueSet vs = new ValueSet();
		vs.setId("http://hl7.org/fhir/ValueSet/defined-types");
		IWorkerContext.ValidationResult outcome;

		outcome = hwc.validateCode(null, "Patient", null, vs);
		assertTrue(outcome.isOk());

		outcome = hwc.validateCode(null, "id", null, vs);
		assertTrue(outcome.isOk());

	}


}
