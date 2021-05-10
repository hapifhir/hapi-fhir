package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.annotation.Nonnull;

public class CommonCodeSystemsTerminologyServiceTest {

	private CommonCodeSystemsTerminologyService mySvc;
	private FhirContext myCtx;

	@BeforeEach
	public void before() {
		myCtx = FhirContext.forR4();
		mySvc = new CommonCodeSystemsTerminologyService(myCtx);
	}

	@Test
	public void testUcum_LookupCode_Good() {
		IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(newSupport(), "http://unitsofmeasure.org", "Cel");
		assertEquals(true, outcome.isFound());
	}

	@Test
	public void testUcum_LookupCode_Good2() {
		IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(newSupport(), "http://unitsofmeasure.org", "kg/m2");
		assertEquals(true, outcome.isFound());
	}

	@Test
	public void testUcum_LookupCode_Bad() {
		IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(newSupport(), "http://unitsofmeasure.org", "AAAAA");
		assertEquals(false, outcome.isFound());
	}

	@Test
	public void testUcum_LookupCode_UnknownSystem() {
		IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(newSupport(), "http://foo", "AAAAA");
		assertNull(outcome);
	}

	@Test
	public void testUcum_ValidateCode_Good() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://hl7.org/fhir/ValueSet/ucum-units");
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(newSupport(), newOptions(), "http://unitsofmeasure.org", "mg", null, vs);
		assertEquals(true, outcome.isOk());
		assertEquals("(milligram)", outcome.getDisplay());
	}

	@Test
	public void testUcum_ValidateCode_Good_SystemInferred() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://hl7.org/fhir/ValueSet/ucum-units");
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(newSupport(), newOptions().setInferSystem(true), null, "mg", null, vs);
		assertEquals(true, outcome.isOk());
		assertEquals("(milligram)", outcome.getDisplay());
	}

	@Test
	public void testUcum_ValidateCode_Bad() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://hl7.org/fhir/ValueSet/ucum-units");
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(newSupport(), newOptions(), "http://unitsofmeasure.org", "aaaaa", null, vs);
		assertNull(outcome);
	}

	@Test
	public void testLanguages_CommonLanguagesVs_GoodCode() {
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "urn:ietf:bcp:47", "en-US", null, "http://hl7.org/fhir/ValueSet/languages");
		assertTrue(outcome.isOk());
		assertEquals("English (United States)", outcome.getDisplay());
	}

	@Test
	public void testLanguages_CommonLanguagesVs_BadCode() {
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "urn:ietf:bcp:47", "FOO", null, "http://hl7.org/fhir/ValueSet/languages");
		assertFalse(outcome.isOk());
		assertEquals("Code \"FOO\" is not in valueset: http://hl7.org/fhir/ValueSet/languages", outcome.getMessage());
	}

	@Test
	public void testLanguages_CommonLanguagesVs_BadSystem() {
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "FOO", "en-US", null, "http://hl7.org/fhir/ValueSet/languages");
		assertFalse(outcome.isOk());
		assertEquals("Inappropriate CodeSystem URL \"FOO\" for ValueSet: http://hl7.org/fhir/ValueSet/languages", outcome.getMessage());
	}

	@Test
	public void testLanguages_AllLanguagesVs_GoodCode() {
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "urn:ietf:bcp:47", "en-US", null, "http://hl7.org/fhir/ValueSet/all-languages");
		assertTrue(outcome.isOk());
		assertEquals("English United States", outcome.getDisplay());
	}

	@Test
	public void testLanguages_AllLanguagesVs_BadCode() {
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "urn:ietf:bcp:47", "FOO", null, "http://hl7.org/fhir/ValueSet/all-languages");
		assertFalse(outcome.isOk());
		assertEquals("Code \"FOO\" is not in valueset: http://hl7.org/fhir/ValueSet/all-languages", outcome.getMessage());
	}

	@Test
	public void testLanguages_AllLanguagesVs_BadSystem() {
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "FOO", "en-US", null, "http://hl7.org/fhir/ValueSet/all-languages");
		assertFalse(outcome.isOk());
		assertEquals("Inappropriate CodeSystem URL \"FOO\" for ValueSet: http://hl7.org/fhir/ValueSet/all-languages", outcome.getMessage());
	}

	@Test
	public void testFetchCodeSystemBuiltIn_Iso3166_R4() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assertEquals(498, cs.getConcept().size());
	}

	@Test
	public void testFetchCodeSystemBuiltIn_Iso3166_DSTU3() {
		CommonCodeSystemsTerminologyService svc = new CommonCodeSystemsTerminologyService(FhirContext.forCached(FhirVersionEnum.DSTU3));
		org.hl7.fhir.dstu3.model.CodeSystem cs = (org.hl7.fhir.dstu3.model.CodeSystem) svc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assertEquals(498, cs.getConcept().size());
	}

	@Test
	public void testFetchCodeSystemBuiltIn_Iso3166_R5() {
		CommonCodeSystemsTerminologyService svc = new CommonCodeSystemsTerminologyService(FhirContext.forCached(FhirVersionEnum.R5));
		org.hl7.fhir.r5.model.CodeSystem cs = (org.hl7.fhir.r5.model.CodeSystem) svc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assertEquals(498, cs.getConcept().size());
	}

	@Test
	public void testFetchCodeSystemBuiltIn_Iso3166_DSTU2() {
		CommonCodeSystemsTerminologyService svc = new CommonCodeSystemsTerminologyService(FhirContext.forCached(FhirVersionEnum.DSTU2));
		IBaseResource cs = svc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assertEquals(null, cs);
	}

	@Test
	public void testFetchCodeSystemBuiltIn_Iso_R4() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem(CommonCodeSystemsTerminologyService.CURRENCIES_CODESYSTEM_URL);
		assertEquals(182, cs.getConcept().size());
	}

	@Test
	public void testFetchCodeSystemBuiltIn_Unknown() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem("http://foo");
		assertEquals(null, cs);
	}

	@Test
	public void testFetchCodeSystemUrlDstu3() {
		try {
			CommonCodeSystemsTerminologyService.getCodeSystemUrl(new org.hl7.fhir.dstu3.model.CodeSystem());

			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Can not handle version: DSTU3", e.getMessage());
		}
	}

	private ValidationSupportContext newSupport() {
		return new ValidationSupportContext(myCtx.getValidationSupport());
	}

	private ConceptValidationOptions newOptions() {
		return new ConceptValidationOptions();
	}


}
