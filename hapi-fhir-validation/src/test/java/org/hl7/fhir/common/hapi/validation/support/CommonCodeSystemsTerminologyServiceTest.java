package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CommonCodeSystemsTerminologyServiceTest {

	private CommonCodeSystemsTerminologyService mySvc;
	private FhirContext myCtx;

	@Before
	public void before() {
		myCtx = FhirContext.forR4();
		mySvc = new CommonCodeSystemsTerminologyService(myCtx);
	}

	@Test
	public void testUcum_LookupCode_Good() {
		IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(myCtx.getValidationSupport(), "http://unitsofmeasure.org", "Cel");
		assertEquals(true, outcome.isFound());
	}

	@Test
	public void testUcum_LookupCode_Bad() {
		IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(myCtx.getValidationSupport(), "http://unitsofmeasure.org", "AAAAA");
		assertNull( outcome);
	}

	@Test
	public void testUcum_LookupCode_UnknownSystem() {
		IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(myCtx.getValidationSupport(), "http://foo", "AAAAA");
		assertNull( outcome);
	}

	@Test
	public void testUcum_ValidateCode_Good() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://hl7.org/fhir/ValueSet/ucum-units");
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(myCtx.getValidationSupport(), new ConceptValidationOptions(), "http://unitsofmeasure.org", "mg", null, vs);
		assertEquals(true, outcome.isOk());
		assertEquals("(milligram)", outcome.getDisplay());
	}

	@Test
	public void testUcum_ValidateCode_Good_SystemInferred() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://hl7.org/fhir/ValueSet/ucum-units");
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(myCtx.getValidationSupport(), new ConceptValidationOptions().setInferSystem(true), null, "mg", null, vs);
		assertEquals(true, outcome.isOk());
		assertEquals("(milligram)", outcome.getDisplay());
	}

	@Test
	public void testUcum_ValidateCode_Bad() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://hl7.org/fhir/ValueSet/ucum-units");
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(myCtx.getValidationSupport(), new ConceptValidationOptions(), "http://unitsofmeasure.org", "aaaaa", null, vs);
		assertNull(outcome);
	}

}
