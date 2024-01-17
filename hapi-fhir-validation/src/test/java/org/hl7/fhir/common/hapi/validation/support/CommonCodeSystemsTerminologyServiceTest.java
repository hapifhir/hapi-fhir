package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
<<<<<<< HEAD
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.IValidationSupport.IssueSeverity;
=======
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
import ca.uhn.fhir.context.support.IValidationSupport.LookupCodeResult;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
<<<<<<< HEAD
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.ALL_LANGUAGES_VALUESET_URL;
import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.CURRENCIES_CODESYSTEM_URL;
import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.CURRENCIES_VALUESET_URL;
import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.LANGUAGES_CODESYSTEM_URL;
import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.LANGUAGES_VALUESET_URL;
import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.MIMETYPES_CODESYSTEM_URL;
import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.MIMETYPES_VALUESET_URL;
import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.UCUM_CODESYSTEM_URL;
import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.UCUM_VALUESET_URL;
import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.USPS_CODESYSTEM_URL;
import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.USPS_VALUESET_URL;
=======
import org.junit.jupiter.params.provider.ValueSource;

import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.MIMETYPES_CODESYSTEM_URL;
import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.MIMETYPES_VALUESET_URL;
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class CommonCodeSystemsTerminologyServiceTest extends BaseValidationTestWithInlineMocks {

	private CommonCodeSystemsTerminologyService mySvc;
	private FhirContext myCtx;

	@BeforeEach
	public void before() {
		myCtx = FhirContext.forR4();
		mySvc = new CommonCodeSystemsTerminologyService(myCtx);
	}

<<<<<<< HEAD
	@ParameterizedTest
	@CsvSource({"Cel, (degree Celsius)", "kg/m2, (kilogram) / (meter ^ 2)"})
	public void testLookupCode_withUnitsOfMeasureWithKnownCode_returnsFound(final String theCode, final String theDisplay) {
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest(UCUM_CODESYSTEM_URL, theCode));
		assertNotNull(outcome);
=======
	@Test
	public void testUcum_LookupCode_Good() {
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest("http://unitsofmeasure.org", "Cel"));
		assert outcome != null;
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
		assertTrue(outcome.isFound());
		assertEquals(theCode, outcome.getSearchedForCode());
		assertEquals(theDisplay, outcome.getCodeDisplay());
	}

	@Test
<<<<<<< HEAD
	public void testLookupCode_withUnitsOfMeasureWithUnknownCode_returnsNotFound() {
		final String code = "someCode";
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest(UCUM_CODESYSTEM_URL, code));
		assertNotNull(outcome);
=======
	public void testUcum_LookupCode_Good2() {
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest("http://unitsofmeasure.org", "kg/m2"));
		assert outcome != null;
		assertTrue(outcome.isFound());
	}

	@Test
	public void testUcum_LookupCode_Bad() {
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest("http://unitsofmeasure.org", "AAAAA"));
		assert outcome != null;
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
		assertFalse(outcome.isFound());
		assertEquals(code, outcome.getSearchedForCode());
		assertEquals("Error processing unit '" + code +"': The unit '" + code + "' is unknown' at position 0", outcome.getErrorMessage());
	}

	@Test
<<<<<<< HEAD
	public void testLookupCode_withUnknownSystem_returnsNull() {
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest("http://foo", "someCode"));
		assertNull(outcome);
	}

	@ParameterizedTest
	@CsvSource({"SGN, Sign languages", "sgn, Sign languages", "EN-US, English United States", "en-us, English United States"})
	public void testLookupCode_withLanguageOnlyWithKnownCode_returnsFound(final String theCode, final String theDisplay) {
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest(CommonCodeSystemsTerminologyService.LANGUAGES_CODESYSTEM_URL, theCode, null, null));
		assertNotNull(outcome);
		assertTrue(outcome.isFound());
		assertEquals(theCode, outcome.getSearchedForCode());
		assertEquals(theDisplay, outcome.getCodeDisplay());
	}

	@Test
	public void testValidateCode_withUnitsOfMeasureWithKnownCode_returnsValid() {
		final String code = "mg";
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), UCUM_CODESYSTEM_URL, code, null, UCUM_VALUESET_URL);
		assertNotNull(outcome);
=======
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
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
		assertTrue(outcome.isOk());
		assertEquals(code, outcome.getCode());
		assertEquals("(milligram)", outcome.getDisplay());
	}

	@Test
<<<<<<< HEAD
	public void testValidateCodeInValueSet_withUnitsOfMeasureWithKnownCode_returnsValid() {
		final ValueSet vs = new ValueSet().setUrl(UCUM_VALUESET_URL);
		final String code = "mg";
		CodeValidationResult outcome = mySvc.validateCodeInValueSet(newSupport(), newOptions(), UCUM_CODESYSTEM_URL, code, null, vs);
		assertNotNull(outcome);
=======
	public void testUcum_ValidateCode_Good_SystemInferred() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://hl7.org/fhir/ValueSet/ucum-units");
		CodeValidationResult outcome = mySvc.validateCodeInValueSet(newSupport(), newOptions().setInferSystem(true), null, "mg", null, vs);
		assert outcome != null;
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
		assertTrue(outcome.isOk());
		assertEquals(code, outcome.getCode());
		assertEquals("(milligram)", outcome.getDisplay());
	}

	@Test
<<<<<<< HEAD
	public void testValidateCodeInValueSet_withUnitsOfMeasureWithInferSystem_returnsValid() {
		final ValueSet vs = new ValueSet().setUrl(UCUM_VALUESET_URL);
		final String code = "mg";
		CodeValidationResult outcome = mySvc.validateCodeInValueSet(newSupport(), newOptions().setInferSystem(true), null, code, null, vs);
		assertNotNull(outcome);
		assertTrue(outcome.isOk());
		assertEquals(code, outcome.getCode());
		assertEquals("(milligram)", outcome.getDisplay());
	}

	@Test
	public void testValidateCodeInValueSet_withUnitsOfMeasureWithUnknownCode_returnsInvalid() {
		final String code = "FOO";
		final ValueSet vs = new ValueSet().setUrl(UCUM_VALUESET_URL);
		CodeValidationResult outcome = mySvc.validateCodeInValueSet(newSupport(), newOptions(), UCUM_CODESYSTEM_URL, code, null, vs);
=======
	public void testUcum_ValidateCode_Bad() {
		ValueSet vs = new ValueSet();
		vs.setUrl("http://hl7.org/fhir/ValueSet/ucum-units");
		CodeValidationResult outcome = mySvc.validateCodeInValueSet(newSupport(), newOptions(), "http://unitsofmeasure.org", "aaaaa", null, vs);
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertEquals(IssueSeverity.ERROR, outcome.getSeverity());
		assertEquals("Error processing unit '" + code +"': The unit '" + code + "' is unknown' at position 0", outcome.getMessage());
	}

<<<<<<< HEAD
	@ParameterizedTest
	@CsvSource({"en-CA, English Canada", "en-US, English United States"})
	public void testValidateLookupCode_withLanguagesWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		CodeValidationResult outcome = mySvc.validateLookupCode(newSupport(), theCode, LANGUAGES_CODESYSTEM_URL);
		assertNotNull(outcome);
=======
	@Test
	public void testLanguagesLanguagesCs_GoodCode() {
		CodeValidationResult outcome = mySvc.validateLookupCode(newSupport(), "en-CA", "urn:ietf:bcp:47");
		assert outcome != null;
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
		assertTrue(outcome.isOk());
		assertEquals(theCode, outcome.getCode());
		assertEquals(theDisplay, outcome.getDisplay());
	}

<<<<<<< HEAD
	@ParameterizedTest
	@CsvSource({"en-CA, English (Canada)", "en-US, English (United States)"})
	public void testValidateCode_withLanguagesWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), LANGUAGES_CODESYSTEM_URL, theCode, null, LANGUAGES_VALUESET_URL);
		assertNotNull(outcome);
=======
	@Test
	public void testLanguagesLanguagesCs_BadCode() {
		CodeValidationResult outcome = mySvc.validateLookupCode(newSupport(), "en-FOO", "urn:ietf:bcp:47");
		assertNull(outcome);
	}

	@Test
	public void testLanguages_CommonLanguagesVs_GoodCode() {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "urn:ietf:bcp:47", "en-US", null, "http://hl7.org/fhir/ValueSet/languages");
		assert outcome != null;
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
		assertTrue(outcome.isOk());
		assertEquals(theCode, outcome.getCode());
		assertEquals(theDisplay, outcome.getDisplay());
	}

	@Test
<<<<<<< HEAD
	public void testValidateCode_withLanguagesWithUnknownCode_returnsInvalid() {
		final String code = "FOO";
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), LANGUAGES_CODESYSTEM_URL, code, null, LANGUAGES_VALUESET_URL);
		assertNotNull(outcome);
=======
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
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
		assertFalse(outcome.isOk());
		assertEquals(IssueSeverity.ERROR, outcome.getSeverity());
		assertEquals("Code \""+ code +"\" is not in valueset: " + LANGUAGES_VALUESET_URL, outcome.getMessage());
	}

	@Test
<<<<<<< HEAD
	public void testValidateCode_withLanguagesWithIncorrectSystem_returnsInvalid() {
		final String system = "FOO";
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), system, "en-US", null, LANGUAGES_VALUESET_URL);
		assertNotNull(outcome);
=======
	public void testLanguages_CommonLanguagesVs_BadSystem() {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "FOO", "en-US", null, "http://hl7.org/fhir/ValueSet/languages");
		assert outcome != null;
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
		assertFalse(outcome.isOk());
		assertEquals(IssueSeverity.ERROR, outcome.getSeverity());
		assertEquals("Inappropriate CodeSystem URL \"" + system + "\" for ValueSet: " + LANGUAGES_VALUESET_URL, outcome.getMessage());
	}

<<<<<<< HEAD
	@ParameterizedTest
	@CsvSource({"en-CA, English Canada", "en-US, English United States"})
	public void testValidateCode_withAllLanguagesWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), LANGUAGES_CODESYSTEM_URL, theCode, null, ALL_LANGUAGES_VALUESET_URL);
		assertNotNull(outcome);
=======
	@Test
	public void testLanguages_AllLanguagesVs_GoodCode() {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "urn:ietf:bcp:47", "en-US", null, "http://hl7.org/fhir/ValueSet/all-languages");
		assert outcome != null;
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
		assertTrue(outcome.isOk());
		assertEquals(theCode, outcome.getCode());
		assertEquals(theDisplay, outcome.getDisplay());
	}


	@Test
<<<<<<< HEAD
	public void testValidateCode_withAllLanguagesWithUnknownCode_returnsInvalid() {
		final String code = "FOO";
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), LANGUAGES_CODESYSTEM_URL, code, null, ALL_LANGUAGES_VALUESET_URL);
		assertNotNull(outcome);
=======
	public void testLanguages_AllLanguagesVs_BadCode() {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "urn:ietf:bcp:47", "FOO", null, "http://hl7.org/fhir/ValueSet/all-languages");
		assert outcome != null;
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
		assertFalse(outcome.isOk());
		assertEquals(IssueSeverity.ERROR, outcome.getSeverity());
		assertEquals("Code \"" + code + "\" is not in valueset: " + ALL_LANGUAGES_VALUESET_URL, outcome.getMessage());
	}

	@Test
<<<<<<< HEAD
	public void testValidateCode_withAllLanguagesWithIncorrectSystem_returnsInvalid() {
		final String system = "FOO";
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), system, "en-US", null, ALL_LANGUAGES_VALUESET_URL);
		assertNotNull(outcome);
=======
	public void testLanguages_AllLanguagesVs_BadSystem() {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), "FOO", "en-US", null, "http://hl7.org/fhir/ValueSet/all-languages");
		assert outcome != null;
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
		assertFalse(outcome.isOk());
		assertEquals(IssueSeverity.ERROR, outcome.getSeverity());
		assertEquals("Inappropriate CodeSystem URL \"" + system + "\" for ValueSet: " + ALL_LANGUAGES_VALUESET_URL, outcome.getMessage());
	}

	@ParameterizedTest
	@CsvSource({"nl, Dutch", "nl-NL, Dutch Netherlands"})
	public void testLookupCode_withLanguagesWithKnownLanguageOnlyCode_returnsCode(final String theCode, final String theDisplay) {
		LookupCodeResult nl = mySvc.lookupCode(newSupport(), new LookupCodeRequest(LANGUAGES_CODESYSTEM_URL, theCode));
		assertTrue(nl != null && nl.isFound());
		assertEquals(theCode, nl.getSearchedForCode());
		assertEquals(theDisplay, nl.getCodeDisplay());
	}

	@Test
	public void testFetchCodeSystem_withCountriesForDSTU3_returnsOk() {
		CommonCodeSystemsTerminologyService svc = new CommonCodeSystemsTerminologyService(FhirContext.forDstu3Cached());
		org.hl7.fhir.dstu3.model.CodeSystem cs = (org.hl7.fhir.dstu3.model.CodeSystem) svc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assertNotNull(cs);
		assertEquals(498, cs.getConcept().size());
	}

	@Test
	public void testFetchCodeSystem_withCountriesForR4_returnsOk() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assertNotNull(cs);
		assertEquals(498, cs.getConcept().size());
	}

	@Test
	public void testFetchCodeSystem_withCountriesForR5_returnsOk() {
		CommonCodeSystemsTerminologyService svc = new CommonCodeSystemsTerminologyService(FhirContext.forR5Cached());
		org.hl7.fhir.r5.model.CodeSystem cs = (org.hl7.fhir.r5.model.CodeSystem) svc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assertNotNull(cs);
		assertEquals(498, cs.getConcept().size());
	}

	@Test
	public void testFetchCodeSystem_withCountriesForDSTU2_returnsOk() {
		CommonCodeSystemsTerminologyService svc = new CommonCodeSystemsTerminologyService(FhirContext.forDstu2Cached());
		IBaseResource cs = svc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assertNull(cs);
	}

	@ParameterizedTest
	@CsvSource({"WA, Washington", "PR, Puerto Rico"})
	public void testValidateCode_withUSPostalWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), USPS_CODESYSTEM_URL, theCode, null, null);
		assertNotNull(outcome);
		assertTrue(outcome.isOk());
		assertEquals(theCode, outcome.getCode());
		assertEquals(theDisplay, outcome.getDisplay());
	}

	@ParameterizedTest
	@CsvSource({"WA, Washington", "PR, Puerto Rico"})
	public void testValidateCode_withUSPostalValueSetWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), USPS_CODESYSTEM_URL, theCode, null, USPS_VALUESET_URL);
		assertNotNull(outcome);
		assertTrue(outcome.isOk());
		assertEquals(theCode, outcome.getCode());
		assertEquals(theDisplay, outcome.getDisplay());
	}

	@Test
	public void testValidateCode_withUSPostalValueSetWithUnknownCode_returnsInvalid() {
		final String system = USPS_CODESYSTEM_URL;
		final String code = "FOO";
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), system, code, null, USPS_VALUESET_URL);
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertEquals(IssueSeverity.ERROR, outcome.getSeverity());
		assertEquals("Unknown code \"" + system + "#" + code + "\"", outcome.getMessage());
	}

	@ParameterizedTest
	@CsvSource({"WA, Washington", "PR, Puerto Rico"})
	public void testLookupCode_withUSPostalWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest(USPS_CODESYSTEM_URL, theCode));
		assertNotNull(outcome);
		assertTrue(outcome.isFound());
		assertEquals(theCode, outcome.getSearchedForCode());
		assertEquals(theDisplay, outcome.getCodeDisplay());
	}

	@Test
	public void testLookupCode_withUSPostalWithUnknownCode_returnsNotFound() {
		final String system = USPS_CODESYSTEM_URL;
		final String code = "FOO";
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest(system, code));
		assertNotNull(outcome);
		assertFalse(outcome.isFound());
		assertEquals(code, outcome.getSearchedForCode());
		assertEquals("Code " + code + " is not valid for system: " + system, outcome.getErrorMessage());
	}

	@ParameterizedTest
	@CsvSource({"USD, United States dollar", "CAD, Canadian dollar", "EUR, Euro"})
	public void testValidateCode_withCurrenciesWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), CURRENCIES_CODESYSTEM_URL, theCode, null, null);
		assertNotNull(outcome);
		assertTrue(outcome.isOk());
		assertEquals(theCode, outcome.getCode());
		assertEquals(theDisplay, outcome.getDisplay());
	}

	@ParameterizedTest
	@CsvSource({"USD, United States dollar", "CAD, Canadian dollar", "EUR, Euro"})
	public void testValidateCode_withCurrenciesValueSetWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), CURRENCIES_CODESYSTEM_URL, theCode, null, CURRENCIES_VALUESET_URL);
		assertNotNull(outcome);
		assertTrue(outcome.isOk());
		assertEquals(theCode, outcome.getCode());
		assertEquals(theDisplay, outcome.getDisplay());
	}

	@Test
	public void testValidateCode_withCurrenciesValueSetWithUnknownCode_returnsInvalid() {
		final String system = CURRENCIES_CODESYSTEM_URL;
		final String code = "FOO";
		CodeValidationResult outcome = mySvc.validateCode(newSupport(), newOptions(), system, code, null, CURRENCIES_VALUESET_URL);
		assertNotNull(outcome);
		assertFalse(outcome.isOk());
		assertEquals(IssueSeverity.ERROR, outcome.getSeverity());
		assertEquals("Unknown code \"" + system + "#" + code + "\"", outcome.getMessage());
	}

	@ParameterizedTest
	@CsvSource({"USD, United States dollar", "CAD, Canadian dollar", "EUR, Euro"})
	public void testLookupCode_withCurrenciesWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest(CURRENCIES_CODESYSTEM_URL, theCode));
		assertNotNull(outcome);
		assertTrue(outcome.isFound());
		assertEquals(theCode, outcome.getSearchedForCode());
		assertEquals(theDisplay, outcome.getCodeDisplay());
	}

	@Test
	public void testLookupCode_withCurrenciesWithUnknownCode_returnsNotFound() {
		final String system = CURRENCIES_CODESYSTEM_URL;
		final String code = "FOO";
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest(system, code));
		assertNotNull(outcome);
		assertFalse(outcome.isFound());
		assertEquals(code, outcome.getSearchedForCode());
		assertEquals("Code " + code + " is not valid for system: " + system, outcome.getErrorMessage());
	}

	@Test
	public void testFetchCodeSystem_withUSPostalCodes_returnsOk() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem(USPS_CODESYSTEM_URL);
		assertNotNull(cs);
		assertEquals(60, cs.getConcept().size());
	}

	@Test
	public void testFetchCodeSystem_withCurrencies_returnsOk() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem(CURRENCIES_CODESYSTEM_URL);
		assertNotNull(cs);
		assertEquals(182, cs.getConcept().size());
	}

	@Test
	public void testFetchCodeSystem_withUnknownSystem_returnsNull() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem("http://foo");
		assertNull(cs);
	}

	@Test
	public void testGetCodeSystemUrl_forDSTU3_throwsException() {
		try {
			CommonCodeSystemsTerminologyService.getCodeSystemUrl(myCtx, new org.hl7.fhir.dstu3.model.CodeSystem());
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(696) + "Can not handle version: DSTU3", e.getMessage());
		}
	}

	@Test
<<<<<<< HEAD
	public void testGetMimetypesFromFile_withValidFormat_returnsOk() {
		Map<String, String> mimetypeMap = CommonCodeSystemsTerminologyService.getMimetypesFromFile("/org/hl7/fhir/common/hapi/validation/support/mimetype-test/test-valid.csv");
		assertFalse(mimetypeMap.isEmpty());
		assertEquals(3, mimetypeMap.size());
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"/org/hl7/fhir/common/hapi/validation/support/mimetype-test/test-invalid.json",
		"/org/hl7/fhir/common/hapi/validation/support/mimetype-test/test-invalid.csv"
	})
	public void testGetMimetypesFromFile_withInvalidFormat_returnsEmpty(String theConfigFile) {
		Map<String, String> mimetypeMap = CommonCodeSystemsTerminologyService.getMimetypesFromFile(theConfigFile);
		assertTrue(mimetypeMap.isEmpty());
	}

	@Test
	public void testFetchCodeSystem_withMimeType_returnsOk() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem(MIMETYPES_CODESYSTEM_URL);
		assertNotNull(cs);
		assertEquals(2086, cs.getConcept().size());
	}

	@ParameterizedTest
	@ValueSource(strings = { EncodingEnum.JSON_PLAIN_STRING, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_JSON })
	public void testValidateCode_withMimetypesValueSetWithStandardCode_returnsValid(String code) {
=======
	public void testFetchCodeSystem_withMimeType_returnsResult() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem(MIMETYPES_CODESYSTEM_URL);
		assertNotNull(cs);
		assertEquals(2085, cs.getConcept().size());
	}

	@ParameterizedTest
	@ValueSource(strings = { EncodingEnum.JSON_PLAIN_STRING, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_JSON, Constants.CT_TEXT })
	public void testValidateCode_withMimetypeKnown_returnValidResult(String code) {
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
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

<<<<<<< HEAD
	@ParameterizedTest
	@ValueSource(strings = { EncodingEnum.JSON_PLAIN_STRING, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_JSON })
	public void testValidateCode_withMimetypesValueSetWithInferSystemWithStandardCode_returnsValid(String code) {
		// test
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions().setInferSystem(true), null, code, null, MIMETYPES_VALUESET_URL);

		// verify
		assertNotNull(result);
		assertEquals(code, result.getCode());
		assertTrue(result.isOk());
		assertNull(result.getSeverity());
		assertNull(result.getMessage());
		assertNotNull(result.getDisplay());
	}

	@ParameterizedTest
	@ValueSource(strings = { EncodingEnum.JSON_PLAIN_STRING, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_JSON })
	public void testValidateCode_withMimetypesWithStandardCode_returnsValid(String code) {
		// test
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), MIMETYPES_CODESYSTEM_URL, code, null, null);

		// verify
		assertNotNull(result);
		assertEquals(code, result.getCode());
		assertTrue(result.isOk());
		assertNull(result.getSeverity());
		assertNull(result.getMessage());
		assertNotNull(result.getDisplay());
	}

	@Test
	public void testValidateCode_withMimetypeValueSetWithArbitraryCode_returnsValid() {
		// setup
		final String code = "someCode";
		final String display = "displayValue";

		// test
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), MIMETYPES_CODESYSTEM_URL, code, display, MIMETYPES_VALUESET_URL);

		// verify
		assertNotNull(result);
		assertEquals(code, result.getCode());
		assertTrue(result.isOk());
		assertEquals(display, result.getDisplay());
	}

	@Test
	public void testValidateCode_withMimetypesWithArbitraryCode_returnsValid() {
		// setup
		final String code = "someCode";
		final String display = "displayValue";

		// test
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), MIMETYPES_CODESYSTEM_URL, code, display, null);

		// verify
		assertNotNull(result);
		assertEquals(code, result.getCode());
		assertTrue(result.isOk());
		assertNull(result.getDisplay());
	}

	@ParameterizedTest
	@ValueSource(strings = { EncodingEnum.JSON_PLAIN_STRING, Constants.FORMAT_TURTLE, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_JSON })
	public void testLookupCode_withMimetypesWithStandardCode_returnFound(String code) {
=======
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
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
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
<<<<<<< HEAD
	public void testLookupCode_withMimetypesWithArbitraryCode_returnsFound() {
=======
	public void testLookupCode_withMimetype_returnNotFoundResult() {
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
		// setup
		final String system = MIMETYPES_CODESYSTEM_URL;
		final String code = "someCode";

		// test
		LookupCodeResult result = mySvc.lookupCode(newSupport(), new LookupCodeRequest(system, code));

		// verify
		assertNotNull(result);
		assertEquals(system, result.getSearchedForSystem());
		assertEquals(code, result.getSearchedForCode());
<<<<<<< HEAD
		assertTrue(result.isFound());
		assertNull(result.getCodeDisplay());
=======
		assertFalse(result.isFound());
		assertNull(result.getCodeDisplay());
		assertEquals(mySvc.getErrorMessage("invalidCodeInSystem", system, code), result.getErrorMessage());
>>>>>>> e2667e6b28d (Add support for mimetype codes from system urn:ietf:bcp:13)
	}

	private ValidationSupportContext newSupport() {
		return new ValidationSupportContext(myCtx.getValidationSupport());
	}

	private ConceptValidationOptions newOptions() {
		return new ConceptValidationOptions();
	}


}
