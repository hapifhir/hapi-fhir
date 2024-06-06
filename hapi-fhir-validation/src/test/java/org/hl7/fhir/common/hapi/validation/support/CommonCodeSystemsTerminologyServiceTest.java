package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.IValidationSupport.IssueSeverity;
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
import org.hl7.fhir.r5.model.Enumerations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
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

	@ParameterizedTest
	@CsvSource({"Cel, (degree Celsius)", "kg/m2, (kilogram) / (meter ^ 2)"})
	public void testLookupCode_withUnitsOfMeasureWithKnownCode_returnsFound(final String theCode, final String theDisplay) {
		final String system = UCUM_CODESYSTEM_URL;
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest(system, theCode));
		lookupCodeResultOk(outcome, theCode, system, theDisplay);
	}

	@Test
	public void testLookupCode_withUnitsOfMeasureWithUnknownCode_returnsNotFound() {
		final String code = "someCode";
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest(UCUM_CODESYSTEM_URL, code));
		lookupCodeResultError(outcome, code, "Error processing unit '" + code +"': The unit '" + code + "' is unknown' at position 0");
	}

	@Test
	public void testLookupCode_withUnknownSystem_returnsNull() {
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest("http://foo", "someCode"));
		assertNull(outcome);
	}

	@ParameterizedTest
	@CsvSource({"SGN, Sign languages", "sgn, Sign languages", "EN-US, English United States", "en-us, English United States"})
	public void testLookupCode_withLanguageOnlyWithKnownCode_returnsFound(final String theCode, final String theDisplay) {
		final String system = LANGUAGES_CODESYSTEM_URL;
		LookupCodeResult outcome = mySvc.lookupCode(newSupport(), new LookupCodeRequest(system, theCode, null, null));
		lookupCodeResultOk(outcome, theCode, system, theDisplay);
	}

	@Test
	public void testValidateCode_withUnitsOfMeasureWithKnownCode_returnsValid() {
		final String code = "mg";
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), UCUM_CODESYSTEM_URL, code, null, UCUM_VALUESET_URL);
		validateCodeResultOk(result, code, "(milligram)");
	}

	@Test
	public void testValidateCodeInValueSet_withUnitsOfMeasureWithKnownCode_returnsValid() {
		final ValueSet vs = new ValueSet().setUrl(UCUM_VALUESET_URL);
		final String code = "mg";
		CodeValidationResult result = mySvc.validateCodeInValueSet(newSupport(), newOptions(), UCUM_CODESYSTEM_URL, code, null, vs);
		validateCodeResultOk(result, code, "(milligram)");
	}

	@Test
	public void testValidateCodeInValueSet_withUnitsOfMeasureWithInferSystem_returnsValid() {
		final ValueSet vs = new ValueSet().setUrl(UCUM_VALUESET_URL);
		final String code = "mg";
		CodeValidationResult result = mySvc.validateCodeInValueSet(newSupport(), newOptions().setInferSystem(true), null, code, null, vs);
		validateCodeResultOk(result, code, "(milligram)");
	}

	@Test
	public void testValidateCodeInValueSet_withUnitsOfMeasureWithUnknownCode_returnsInvalid() {
		final String code = "FOO";
		final ValueSet vs = new ValueSet().setUrl(UCUM_VALUESET_URL);
		CodeValidationResult result = mySvc.validateCodeInValueSet(newSupport(), newOptions(), UCUM_CODESYSTEM_URL, code, null, vs);
		validateCodeResultError(result, "Error processing unit '" + code +"': The unit '" + code + "' is unknown' at position 0 (for 'http://unitsofmeasure.org#"+code+"')");
	}

	@ParameterizedTest
	@CsvSource({"en-CA, English Canada", "en-US, English United States"})
	public void testValidateLookupCode_withLanguagesWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		CodeValidationResult result = mySvc.validateCodeUsingSystemLookup(newSupport(), theCode, LANGUAGES_CODESYSTEM_URL);
		validateCodeResultOk(result, theCode, theDisplay);
	}

	@ParameterizedTest
	@CsvSource({"en-CA, English (Canada)", "en-US, English (United States)"})
	public void testValidateCode_withLanguagesWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), LANGUAGES_CODESYSTEM_URL, theCode, null, LANGUAGES_VALUESET_URL);
		validateCodeResultOk(result, theCode, theDisplay);
	}

	@Test
	public void testValidateCode_withLanguagesWithUnknownCode_returnsInvalid() {
		final String code = "FOO";
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), LANGUAGES_CODESYSTEM_URL, code, null, LANGUAGES_VALUESET_URL);
		validateCodeResultError(result, "Code \""+ code +"\" is not in valueset: " + LANGUAGES_VALUESET_URL);
	}

	@Test
	public void testValidateCode_withLanguagesWithIncorrectSystem_returnsInvalid() {
		final String system = "FOO";
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), system, "en-US", null, LANGUAGES_VALUESET_URL);
		validateCodeResultError(result, "Inappropriate CodeSystem URL \"" + system + "\" for ValueSet: " + LANGUAGES_VALUESET_URL);
	}

	@ParameterizedTest
	@CsvSource({"en-CA, English Canada", "en-US, English United States"})
	public void testValidateCode_withAllLanguagesWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), LANGUAGES_CODESYSTEM_URL, theCode, null, ALL_LANGUAGES_VALUESET_URL);
		validateCodeResultOk(result, theCode, theDisplay);
	}


	@Test
	public void testValidateCode_withAllLanguagesWithUnknownCode_returnsInvalid() {
		final String code = "FOO";
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), LANGUAGES_CODESYSTEM_URL, code, null, ALL_LANGUAGES_VALUESET_URL);
		validateCodeResultError(result, "Code \"" + code + "\" is not in valueset: " + ALL_LANGUAGES_VALUESET_URL);
	}

	@Test
	public void testValidateCode_withAllLanguagesWithIncorrectSystem_returnsInvalid() {
		final String system = "FOO";
		final String valueSet = ALL_LANGUAGES_VALUESET_URL;
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), system, "en-US", null, valueSet);
		validateCodeResultError(result, "Inappropriate CodeSystem URL \"" + system + "\" for ValueSet: " + valueSet);
	}

	@ParameterizedTest
	@CsvSource({"nl, Dutch", "nl-NL, Dutch Netherlands"})
	public void testLookupCode_withLanguagesWithKnownLanguageOnlyCode_returnsFound(final String theCode, final String theDisplay) {
		final String system = LANGUAGES_CODESYSTEM_URL;
		LookupCodeResult result = mySvc.lookupCode(newSupport(), new LookupCodeRequest(system, theCode));
		lookupCodeResultOk(result, theCode, system, theDisplay);
	}

	@Test
	public void testFetchCodeSystem_withCountriesForDSTU3_returnsOk() {
		CommonCodeSystemsTerminologyService svc = new CommonCodeSystemsTerminologyService(FhirContext.forDstu3Cached());
		org.hl7.fhir.dstu3.model.CodeSystem cs = (org.hl7.fhir.dstu3.model.CodeSystem) svc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assertNotNull(cs);
		assertThat(cs.getConcept()).hasSize(498);
	}

	@Test
	public void testFetchCodeSystem_withCountriesForR4_returnsOk() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assertNotNull(cs);
		assertThat(cs.getConcept()).hasSize(498);
	}

	@Test
	public void testFetchCodeSystem_withCountriesForR5_returnsOk() {
		CommonCodeSystemsTerminologyService svc = new CommonCodeSystemsTerminologyService(FhirContext.forR5Cached());
		org.hl7.fhir.r5.model.CodeSystem cs = (org.hl7.fhir.r5.model.CodeSystem) svc.fetchCodeSystem(CommonCodeSystemsTerminologyService.COUNTRIES_CODESYSTEM_URL);
		assertNotNull(cs);
		assertEquals(498, cs.getConcept().size());
		assertEquals(Enumerations.CodeSystemContentMode.COMPLETE, cs.getContent());
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
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), USPS_CODESYSTEM_URL, theCode, null, null);
		validateCodeResultOk(result, theCode, theDisplay);
	}

	@ParameterizedTest
	@CsvSource({"WA, Washington", "PR, Puerto Rico"})
	public void testValidateCode_withUSPostalValueSetWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), USPS_CODESYSTEM_URL, theCode, null, USPS_VALUESET_URL);
		validateCodeResultOk(result, theCode, theDisplay);
	}

	@Test
	public void testValidateCode_withUSPostalValueSetWithUnknownCode_returnsInvalid() {
		final String system = USPS_CODESYSTEM_URL;
		final String code = "FOO";
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), system, code, null, USPS_VALUESET_URL);
		validateCodeResultError(result, "Unknown code \"" + system + "#" + code + "\"");
	}

	@ParameterizedTest
	@CsvSource({"WA, Washington", "PR, Puerto Rico"})
	public void testLookupCode_withUSPostalWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		final String system = USPS_CODESYSTEM_URL;
		LookupCodeResult result = mySvc.lookupCode(newSupport(), new LookupCodeRequest(system, theCode));
		lookupCodeResultOk(result, theCode, system, theDisplay);
	}

	@Test
	public void testLookupCode_withUSPostalWithUnknownCode_returnsNotFound() {
		final String system = USPS_CODESYSTEM_URL;
		final String code = "invalidUSPS";
		LookupCodeResult result = mySvc.lookupCode(newSupport(), new LookupCodeRequest(system, code));
		lookupCodeResultError(result, code, "Code " + code + " is not valid for system: " + system);
	}

	@ParameterizedTest
	@CsvSource({"USD, United States dollar", "CAD, Canadian dollar", "EUR, Euro"})
	public void testValidateCode_withCurrenciesWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), CURRENCIES_CODESYSTEM_URL, theCode, null, null);
		validateCodeResultOk(result, theCode, theDisplay);
	}

	@ParameterizedTest
	@CsvSource({"USD, United States dollar", "CAD, Canadian dollar", "EUR, Euro"})
	public void testValidateCode_withCurrenciesValueSetWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), CURRENCIES_CODESYSTEM_URL, theCode, null, CURRENCIES_VALUESET_URL);
		validateCodeResultOk(result, theCode, theDisplay);
	}

	@Test
	public void testValidateCode_withCurrenciesValueSetWithUnknownCode_returnsInvalid() {
		final String system = CURRENCIES_CODESYSTEM_URL;
		final String code = "invalidCurrency";
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), system, code, null, CURRENCIES_VALUESET_URL);
		validateCodeResultError(result, "Unknown code \"" + system + "#" + code + "\"");
	}

	@ParameterizedTest
	@CsvSource({"USD, United States dollar", "CAD, Canadian dollar", "EUR, Euro"})
	public void testLookupCode_withCurrenciesWithKnownCode_returnsValid(final String theCode, final String theDisplay) {
		final String system = CURRENCIES_CODESYSTEM_URL;
		LookupCodeResult result = mySvc.lookupCode(newSupport(), new LookupCodeRequest(system, theCode));
		lookupCodeResultOk(result, theCode, system, theDisplay);
	}

	@Test
	public void testLookupCode_withCurrenciesWithUnknownCode_returnsNotFound() {
		final String system = CURRENCIES_CODESYSTEM_URL;
		final String code = "FOO";
		LookupCodeResult result = mySvc.lookupCode(newSupport(), new LookupCodeRequest(system, code));
		lookupCodeResultError(result, code, "Code " + code + " is not valid for system: " + system);
	}

	@Test
	public void testFetchCodeSystem_withCurrencies_returnsOk() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem(CURRENCIES_CODESYSTEM_URL);
		assertNotNull(cs);
		assertThat(cs.getConcept()).hasSize(182);
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
			fail();		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(696) + "Can not handle version: DSTU3", e.getMessage());
		}
	}

	@Test
	public void testFetchCodeSystem_withMimeType_returnsOk() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem(MIMETYPES_CODESYSTEM_URL);
		assertTrue(cs.getConcept().isEmpty());
		assertEquals(CodeSystem.CodeSystemContentMode.NOTPRESENT, cs.getContent());
	}

	@ParameterizedTest
	@ValueSource(strings = { EncodingEnum.JSON_PLAIN_STRING, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_JSON })
	public void testValidateCode_withMimetypesValueSetWithStandardCode_returnsValid(String theCode) {
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), MIMETYPES_CODESYSTEM_URL, theCode, null, MIMETYPES_VALUESET_URL);
		validateCodeResultOk(result, theCode, null);
	}

	@ParameterizedTest
	@ValueSource(strings = { EncodingEnum.JSON_PLAIN_STRING, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_JSON })
	public void testValidateCode_withMimetypesValueSetWithInferSystemWithStandardCode_returnsValid(String theCode) {
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions().setInferSystem(true), null, theCode, null, MIMETYPES_VALUESET_URL);
		validateCodeResultOk(result, theCode, null);
	}

	@Test
	public void testValidateCode_withMimetypesValueSetWithMismatchSystem_returnsInvalid() {
		final String system = "someSystem";
		final String valueSet = MIMETYPES_VALUESET_URL;
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), system, system, null, valueSet);
		validateCodeResultError(result, "Inappropriate CodeSystem URL \"" + system + "\" for ValueSet: " + valueSet);
	}

	@ParameterizedTest
	@ValueSource(strings = { EncodingEnum.JSON_PLAIN_STRING, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_JSON })
	public void testValidateCode_withMimetypesWithStandardCode_returnsValid(String theCode) {
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), MIMETYPES_CODESYSTEM_URL, theCode, null, null);
		validateCodeResultOk(result, theCode, null);
	}

	@Test
	public void testValidateCode_withMimetypeValueSetWithArbitraryCode_returnsValid() {
		final String code = "someCode";
		final String display = "displayValue";
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), MIMETYPES_CODESYSTEM_URL, code, display, MIMETYPES_VALUESET_URL);
		validateCodeResultOk(result, code, display);
	}

	@Test
	public void testValidateCode_withMimetypesWithArbitraryCode_returnsValid() {
		final String code = "someCode";
		final String display = "displayValue";
		CodeValidationResult result = mySvc.validateCode(newSupport(), newOptions(), MIMETYPES_CODESYSTEM_URL, code, display, null);
		validateCodeResultOk(result, code, null);

		// the display null in result bug is reported here:  https://github.com/hapifhir/hapi-fhir/issues/5643
	}

	@ParameterizedTest
	@ValueSource(strings = { EncodingEnum.JSON_PLAIN_STRING, Constants.FORMAT_TURTLE, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_JSON })
	public void testLookupCode_withMimetypesWithStandardCode_returnFound(String code) {
		final String system = MIMETYPES_CODESYSTEM_URL;
		LookupCodeResult result = mySvc.lookupCode(newSupport(), new LookupCodeRequest(system, code));
		lookupCodeResultOk(result, code, system, null);
	}

	@Test
	public void testLookupCode_withMimetypesWithArbitraryCode_returnsFound() {
		final String system = MIMETYPES_CODESYSTEM_URL;
		final String code = "someCode";
		LookupCodeResult result = mySvc.lookupCode(newSupport(), new LookupCodeRequest(system, code));
		lookupCodeResultOk(result, code, system, null);
	}

	private void validateCodeResultOk(final CodeValidationResult theResult, final String theCode, final String theDisplay) {
		assertNotNull(theResult);
		assertTrue(theResult.isOk());
		assertEquals(theCode, theResult.getCode());
		assertEquals(theDisplay, theResult.getDisplay());
		assertNull(theResult.getSeverity());
		assertNull(theResult.getMessage());
	}

	private void validateCodeResultError(final CodeValidationResult theResult, final String theError) {
		assertNotNull(theResult);
		assertFalse(theResult.isOk());
		assertEquals(IssueSeverity.ERROR, theResult.getSeverity());
		assertEquals(theError, theResult.getMessage());
	}

	private void lookupCodeResultOk(final LookupCodeResult theResult, final String theCode, final String theSystem, final String theDisplay) {
		assertNotNull(theResult);
		assertEquals(theSystem, theResult.getSearchedForSystem());
		assertEquals(theCode, theResult.getSearchedForCode());
		assertTrue(theResult.isFound());
		assertEquals(theDisplay, theResult.getCodeDisplay());
	}

	private void lookupCodeResultError(final LookupCodeResult theResult, final String theCode, final String theMessage) {
		assertNotNull(theResult);
		assertEquals(theCode, theResult.getSearchedForCode());
		assertFalse(theResult.isFound());
		assertEquals(theMessage, theResult.getErrorMessage());
		assertNull(theResult.getCodeDisplay());
	}


	private ValidationSupportContext newSupport() {
		return new ValidationSupportContext(myCtx.getValidationSupport());
	}

	private ConceptValidationOptions newOptions() {
		return new ConceptValidationOptions();
	}


}
