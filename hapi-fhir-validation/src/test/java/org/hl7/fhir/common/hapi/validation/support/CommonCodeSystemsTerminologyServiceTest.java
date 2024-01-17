package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.IValidationSupport.LookupCodeResult;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.MIMETYPES_CODESYSTEM_URL;
import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.MIMETYPES_VALUESET_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest("http://unitsofmeasure.org", "Cel"));
		assert outcome != null;
		assertTrue(outcome.isFound());
	}

	@Test
	public void testUcum_LookupCode_Good2() {
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest("http://unitsofmeasure.org", "kg/m2"));
		assert outcome != null;
		assertTrue(outcome.isFound());
	}

	@Test
	public void testUcum_LookupCode_Bad() {
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest("http://unitsofmeasure.org", "AAAAA"));
		assert outcome != null;
		assertFalse(outcome.isFound());
	}

	@Test
	public void testUcum_LookupCode_UnknownSystem() {
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest("http://foo", "AAAAA"));
		assertNull(outcome);
	}

	@Test
	public void lookupCode_languageOnlyLookup_isCaseInsensitive() {
		LookupCodeResult outcomeUpper = mySvc.lookupCode(newSupport(), new LookupCodeRequest("urn:ietf:bcp:47", "SGN", "Sign Languages", null));
		LookupCodeResult outcomeLower = mySvc.lookupCode(newSupport(), new LookupCodeRequest("urn:ietf:bcp:47", "sgn", "Sign Languages", null));
		assertNotNull(outcomeUpper);
		assertNotNull(outcomeLower);
		assertTrue(outcomeLower.isFound());
		assertTrue(outcomeUpper.isFound());
	}

	@Test
	public void lookupCode_languageAndRegionLookup_isCaseInsensitive() {
		LookupCodeResult outcomeUpper = mySvc.lookupCode(newSupport(), new LookupCodeRequest("urn:ietf:bcp:47", "EN-US", "English", null));
		LookupCodeResult outcomeLower = mySvc.lookupCode(newSupport(), new LookupCodeRequest("urn:ietf:bcp:47", "en-us", "English", null));
		assertNotNull(outcomeUpper);
		assertNotNull(outcomeLower);
		assertTrue(outcomeLower.isFound());
		assertTrue(outcomeUpper.isFound());
	}

	@Test
	public void testUcum_ValidateCode_Good() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://hl7.org/fhir/ValueSet/ucum-units");
		CodeValidationResult outcome = mySvc.validateCodeInValueSet(newSupport(), newOptions(), "http://unitsofmeasure.org", "mg", null, vs);
		assert outcome != null;
		assertTrue(outcome.isOk());
		assertEquals("(milligram)", outcome.getDisplay());
	}

	@Test
	public void testUcum_ValidateCode_Good_SystemInferred() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://hl7.org/fhir/ValueSet/ucum-units");
		CodeValidationResult outcome = mySvc.validateCodeInValueSet(newSupport(), newOptions().setInferSystem(true), null, "mg", null, vs);
		assert outcome != null;
		assertTrue(outcome.isOk());
		assertEquals("(milligram)", outcome.getDisplay());
	}

	@Test
	public void testUcum_ValidateCode_Bad() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://hl7.org/fhir/ValueSet/ucum-units");
		CodeValidationResult outcome = mySvc.validateCodeInValueSet(newSupport(), newOptions(), "http://unitsofmeasure.org", "aaaaa", null, vs);
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertEquals("Error processing unit 'aaaaa': The unit 'aaaaa' is unknown' at position 0", outcome.getMessage());
		assertEquals("error", outcome.getSeverityCode());
	}

	@Test
	public void testLanguagesLanguagesCs_GoodCode() {
		CodeValidationResult outcome = mySvc.validateLookupCode(newSupport(), "en-CA", "urn:ietf:bcp:47");
		assert outcome != null;
		assertTrue(outcome.isOk());
		assertEquals("English Canada", outcome.getDisplay());
	}

	@Test
	public void testLanguagesLanguagesCs_BadCode() {
		CodeValidationResult outcome = mySvc.validateLookupCode(newSupport(), "en-FOO", "urn:ietf:bcp:47");
		assertNull(outcome);
	}

	@Test
	public void testLanguages_CommonLanguagesVs_GoodCode() {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "urn:ietf:bcp:47", "en-US", null, "http://hl7.org/fhir/ValueSet/languages");
		assert outcome != null;
		assertTrue(outcome.isOk());
		assertEquals("English (United States)", outcome.getDisplay());
	}

	@Test
	public void testLanguages_CommonLanguagesVs_OnlyLanguage_NoRegion() {
		LookupCodeResult nl = mySvc.lookupCode(newSupport(), new LookupCodeRequest("urn:ietf:bcp:47", "nl"));
		assertTrue(nl != null && nl.isFound());
		assertEquals("Dutch", nl.getCodeDisplay());
	}

	@Test
	public void testLanguages_CommonLanguagesVs_LanguageAndRegion() {
		LookupCodeResult nl = mySvc.lookupCode(newSupport(), new LookupCodeRequest("urn:ietf:bcp:47", "nl-NL"));
		assertTrue(nl != null && nl.isFound());
		assertEquals("Dutch Netherlands", nl.getCodeDisplay());
	}

	@Test
	public void testLanguages_CommonLanguagesVs_BadCode() {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "urn:ietf:bcp:47", "FOO", null, "http://hl7.org/fhir/ValueSet/languages");
		assert outcome != null;
		assertFalse(outcome.isOk());
		assertEquals("Code \"FOO\" is not in valueset: http://hl7.org/fhir/ValueSet/languages", outcome.getMessage());
	}

	@Test
	public void testLanguages_CommonLanguagesVs_BadSystem() {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "FOO", "en-US", null, "http://hl7.org/fhir/ValueSet/languages");
		assert outcome != null;
		assertFalse(outcome.isOk());
		assertEquals("Inappropriate CodeSystem URL \"FOO\" for ValueSet: http://hl7.org/fhir/ValueSet/languages", outcome.getMessage());
	}

	@Test
	public void testLanguages_AllLanguagesVs_GoodCode() {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "urn:ietf:bcp:47", "en-US", null, "http://hl7.org/fhir/ValueSet/all-languages");
		assert outcome != null;
		assertTrue(outcome.isOk());
		assertEquals("English United States", outcome.getDisplay());
	}

	@Test
	public void testLanguages_AllLanguagesVs_BadCode() {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "urn:ietf:bcp:47", "FOO", null, "http://hl7.org/fhir/ValueSet/all-languages");
		assert outcome != null;
		assertFalse(outcome.isOk());
		assertEquals("Code \"FOO\" is not in valueset: http://hl7.org/fhir/ValueSet/all-languages", outcome.getMessage());
	}

	@Test
	public void testLanguages_AllLanguagesVs_BadSystem() {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "FOO", "en-US", null, "http://hl7.org/fhir/ValueSet/all-languages");
		assert outcome != null;
		assertFalse(outcome.isOk());
		assertEquals("Inappropriate CodeSystem URL \"FOO\" for ValueSet: http://hl7.org/fhir/ValueSet/all-languages", outcome.getMessage());
	}

	@Test
	public void testFetchCodeSystemBuiltIn_Iso3166_R4() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assert cs != null;
		assertEquals(498, cs.getConcept().size());
	}

	@Test
	public void testFetchCodeSystemBuiltIn_Iso3166_DSTU3() {
		CommonCodeSystemsTerminologyService svc = new CommonCodeSystemsTerminologyService(FhirContext.forDstu3Cached());
		org.hl7.fhir.dstu3.model.CodeSystem cs = (org.hl7.fhir.dstu3.model.CodeSystem) svc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assert cs != null;
		assertEquals(498, cs.getConcept().size());
	}

	@Test
	public void testFetchCodeSystemBuiltIn_Iso3166_R5() {
		CommonCodeSystemsTerminologyService svc = new CommonCodeSystemsTerminologyService(FhirContext.forR5Cached());
		org.hl7.fhir.r5.model.CodeSystem cs = (org.hl7.fhir.r5.model.CodeSystem) svc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assert cs != null;
		assertEquals(498, cs.getConcept().size());
	}

	@Test
	public void testFetchCodeSystemBuiltIn_Iso3166_DSTU2() {
		CommonCodeSystemsTerminologyService svc = new CommonCodeSystemsTerminologyService(FhirContext.forDstu2Cached());
		IBaseResource cs = svc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assertNull(cs);
	}

	@Test
	public void testFetchCodeSystemBuiltIn_Iso_R4() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem(CommonCodeSystemsTerminologyService.CURRENCIES_CODESYSTEM_URL);
		assert cs != null;
		assertEquals(182, cs.getConcept().size());
	}

	@Test
	public void testFetchCodeSystemBuiltIn_Unknown() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem("http://foo");
		assertNull(cs);
	}

	@Test
	public void testFetchCodeSystemUrlDstu3() {
		try {
			CommonCodeSystemsTerminologyService.getCodeSystemUrl(myCtx, new org.hl7.fhir.dstu3.model.CodeSystem());

			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(696) + "Can not handle version: DSTU3", e.getMessage());
		}
	}

	@Test
	public void testFetchCodeSystem_withMimeType_returnsResult() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem(MIMETYPES_CODESYSTEM_URL);
		assertNotNull(cs);
		assertEquals(2085, cs.getConcept().size());
	}

	@ParameterizedTest
	@ValueSource(strings = { EncodingEnum.JSON_PLAIN_STRING, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_JSON, Constants.CT_TEXT })
	public void testValidateCode_withMimetypeKnown_returnValidResult(String code) {
		// test
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), MIMETYPES_CODESYSTEM_URL, code, null, MIMETYPES_VALUESET_URL);

		// verify
		assertNotNull(result);
		assertEquals(code, result.getCode());
		assertTrue(result.isOk());
		assertNull(result.getSeverity());
		assertNull(result.getMessage());
		assertNotNull(result.getDisplay());
	}

	@Test
	public void testValidateCode_withMimetypeUnknown_returnInvalidResult() {
		// setup
		final String system = MIMETYPES_CODESYSTEM_URL;
		final String code = "someCode";

		// test
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), system, code, null, null);

		// verify
		assertNotNull(result);
		assertFalse(result.isOk());
		assertNull(result.getDisplay());
		assertEquals(IValidationSupport.IssueSeverity.ERROR, result.getSeverity());
		assertEquals(mySvc.getErrorMessage("invalidCodeInSystem", system, code), result.getMessage());
	}


	@Test
	public void testValidateCode_withMimetypeUnknown_returnUnknownResult() {
		// setup
		final String system = MIMETYPES_CODESYSTEM_URL;
		final String code = "someCode";

		// test
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), system, code, null, MIMETYPES_VALUESET_URL);

		// verify
		assertNotNull(result);
		assertFalse(result.isOk());
		assertNull(result.getDisplay());
		assertEquals(IValidationSupport.IssueSeverity.ERROR, result.getSeverity());
		assertEquals(mySvc.getErrorMessage("unknownCodeSystem", system, code), result.getMessage());
	}

	@ParameterizedTest
	@ValueSource(strings = { EncodingEnum.JSON_PLAIN_STRING, Constants.FORMAT_TURTLE, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_JSON, Constants.CT_TEXT })
	public void testLookupCode_withMimetype_returnFoundResult(String code) {
		// setup
		final String system = MIMETYPES_CODESYSTEM_URL;

		// test
		LookupCodeResult result = mySvc.lookupCode(newSupport(), new LookupCodeRequest(system, code));

		// verify
		assertNotNull(result);
		assertEquals(system, result.getSearchedForSystem());
		assertEquals(code, result.getSearchedForCode());
		assertTrue(result.isFound());
		assertNotNull(result.getCodeDisplay());
	}

	@Test
	public void testLookupCode_withMimetype_returnNotFoundResult() {
		// setup
		final String system = MIMETYPES_CODESYSTEM_URL;
		final String code = "someCode";

		// test
		LookupCodeResult result = mySvc.lookupCode(newSupport(), new LookupCodeRequest(system, code));

		// verify
		assertNotNull(result);
		assertEquals(system, result.getSearchedForSystem());
		assertEquals(code, result.getSearchedForCode());
		assertFalse(result.isFound());
		assertNull(result.getCodeDisplay());
		assertEquals(mySvc.getErrorMessage("invalidCodeInSystem", system, code), result.getErrorMessage());
	}

	private ValidationSupportContext newSupport() {
		return new ValidationSupportContext(myCtx.getValidationSupport());
	}

	private ConceptValidationOptions newOptions() {
		return new ConceptValidationOptions();
	}


}
