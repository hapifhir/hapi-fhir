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

		DefaultProfileValidationSupport validationSupport = new DefaultProfileValidationSupport();
		FhirContext ctx = FhirContext.forDstu2();
		HapiWorkerContext hwc = new HapiWorkerContext(ctx, validationSupport);

		ValueSet vs = validationSupport.fetchResource(ctx, ValueSet.class, "http://hl7.org/fhir/ValueSet/defined-types");
		IWorkerContext.ValidationResult outcome;

		outcome = hwc.validateCode("http://hl7.org/fhir/resource-types", "Patient", null);
		assertTrue(outcome.isOk());

		outcome = hwc.validateCode("http://hl7.org/fhir/resource-types", "Patient", null, vs);
		assertTrue(outcome.isOk());

		outcome = hwc.validateCode(null, "Patient", null, vs);
		assertTrue(outcome.isOk());

		outcome = hwc.validateCode(null, "id", null, vs);
		assertTrue(outcome.isOk());

		outcome = hwc.validateCode(null, "foo", null, vs);
		assertFalse(outcome.isOk());

	}


}
