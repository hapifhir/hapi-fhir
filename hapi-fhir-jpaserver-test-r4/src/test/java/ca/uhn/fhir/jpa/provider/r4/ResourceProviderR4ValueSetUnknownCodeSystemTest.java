package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@code ValueSet/$validate-code} over HTTP against ValueSets whose CodeSystem cannot be resolved (#8415): the code
 * is rejected and each code system the server could not reach is named in {@code x-caused-by-unknown-system}, for
 * every way the request can identify the ValueSet and supply the code. Pre-expansion is enabled, as by default.
 */
// Created by Claude Opus 5.5
class ResourceProviderR4ValueSetUnknownCodeSystemTest extends BaseResourceProviderR4Test {
	private static final String CS_URL = "http://example.org/cs/colours";
	private static final String UNKNOWN_CS_URL = "http://example.org/cs/not-loaded";
	private static final String NOT_PRESENT_CS_URL = "http://example.org/cs/not-present";
	private static final String VS_VERSION_URL = "http://example.org/vs/vs-version";
	private static final String VS_UNKNOWN_URL = "http://example.org/vs/vs-unknown";
	private static final String VS_MIXED_URL = "http://example.org/vs/vs-mixed";
	private static final String VS_EXCLUDE_URL = "http://example.org/vs/vs-exclude";
	private static final String VS_NOT_PRESENT_URL = "http://example.org/vs/vs-not-present";
	private static final String VS_BCP47_URL = "http://example.org/vs/vs-bcp47";

	@BeforeEach
	void beforeCreateTerminology() {
		myStorageSettings.setPreExpandValueSets(true);

		CodeSystem colours = new CodeSystem();
		colours.setUrl(CS_URL);
		colours.setVersion("1.0");
		colours.setStatus(Enumerations.PublicationStatus.ACTIVE);
		colours.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		colours.addConcept().setCode("red");
		colours.addConcept().setCode("blue");
		myCodeSystemDao.create(colours, mySrd);

		CodeSystem notPresent = new CodeSystem();
		notPresent.setUrl(NOT_PRESENT_CS_URL);
		notPresent.setStatus(Enumerations.PublicationStatus.ACTIVE);
		notPresent.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		myCodeSystemDao.create(notPresent, mySrd);

		ValueSet versionVs = newValueSet("vs-version", VS_VERSION_URL);
		versionVs.setVersion("1");
		versionVs.getCompose().addInclude().setSystem(CS_URL).setVersion("2.0").addConcept().setCode("red");
		myValueSetDao.update(versionVs, mySrd);

		ValueSet unknownVs = newValueSet("vs-unknown", VS_UNKNOWN_URL);
		unknownVs.getCompose().addInclude().setSystem(UNKNOWN_CS_URL).addConcept().setCode("x1");
		myValueSetDao.update(unknownVs, mySrd);

		ValueSet mixedVs = newValueSet("vs-mixed", VS_MIXED_URL);
		mixedVs.getCompose().addInclude().setSystem(CS_URL).setVersion("1.0").addConcept().setCode("red");
		mixedVs.getCompose().addInclude().setSystem(UNKNOWN_CS_URL).addConcept().setCode("x1");
		myValueSetDao.update(mixedVs, mySrd);

		ValueSet excludeVs = newValueSet("vs-exclude", VS_EXCLUDE_URL);
		excludeVs.getCompose().addInclude().setSystem(CS_URL).setVersion("1.0");
		excludeVs.getCompose().addExclude().setSystem(CS_URL).setVersion("2.0").addConcept().setCode("red");
		myValueSetDao.update(excludeVs, mySrd);

		ValueSet notPresentVs = newValueSet("vs-not-present", VS_NOT_PRESENT_URL);
		notPresentVs.getCompose().addInclude().setSystem(NOT_PRESENT_CS_URL);
		myValueSetDao.update(notPresentVs, mySrd);

		ValueSet bcp47Vs = newValueSet("vs-bcp47", VS_BCP47_URL);
		bcp47Vs.getCompose().addInclude().setSystem("urn:ietf:bcp:47").addConcept().setCode("en-CA");
		myValueSetDao.update(bcp47Vs, mySrd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myBatch2JobHelper.awaitNoJobsRunning();
	}

	@AfterEach
	void afterResetPreExpansion() {
		myStorageSettings.setPreExpandValueSets(new JpaStorageSettings().isPreExpandValueSets());
	}

	@Test
	void validateCode_instanceWithUninstalledCodeSystemVersion_rejectsAndNamesTheVersion() {
		Parameters outcome = myClient.operation()
				.onInstance(new IdType("ValueSet/vs-version"))
				.named("validate-code")
				.withParameter(Parameters.class, "code", new CodeType("red"))
				.andParameter("system", new UriType(CS_URL))
				.execute();

		assertRejectedNaming(outcome, CS_URL + "|2.0");
	}

	@Test
	void validateCode_urlWithUninstalledCodeSystemVersion_rejectsAndNamesTheVersion() {
		Parameters outcome = validateByUrl(VS_VERSION_URL)
				.andParameter("code", new CodeType("red"))
				.andParameter("system", new UriType(CS_URL))
				.execute();

		assertRejectedNaming(outcome, CS_URL + "|2.0");
	}

	@Test
	void validateCode_urlAndValueSetVersionWithUninstalledCodeSystemVersion_rejectsAndNamesTheVersion() {
		Parameters outcome = validateByUrl(VS_VERSION_URL)
				.andParameter("valueSetVersion", new StringType("1"))
				.andParameter("code", new CodeType("red"))
				.andParameter("system", new UriType(CS_URL))
				.execute();

		assertRejectedNaming(outcome, CS_URL + "|2.0");
	}

	@Test
	void validateCode_codingWithUninstalledCodeSystemVersion_rejectsAndNamesTheVersion() {
		Parameters outcome = validateByUrl(VS_VERSION_URL)
				.andParameter("coding", new Coding(CS_URL, "red", null))
				.execute();

		assertRejectedNaming(outcome, CS_URL + "|2.0");
	}

	@Test
	void validateCode_codingNamingTheUninstalledVersion_rejectsAndNamesTheVersion() {
		Parameters outcome = validateByUrl(VS_VERSION_URL)
				.andParameter("coding", new Coding(CS_URL, "red", null).setVersion("2.0"))
				.execute();

		assertRejectedNaming(outcome, CS_URL + "|2.0");
	}

	@Test
	void validateCode_unknownCodeSystem_rejectsAndNamesTheSystem() {
		Parameters outcome = validateByUrl(VS_UNKNOWN_URL)
				.andParameter("code", new CodeType("x1"))
				.andParameter("system", new UriType(UNKNOWN_CS_URL))
				.execute();

		assertRejectedNaming(outcome, UNKNOWN_CS_URL);
	}

	/**
	 * The request names a version of a code system the server does not have at all, against an include that names
	 * no version: the code system is still the reason the code cannot be validated.
	 */
	@Test
	void validateCode_systemVersionOfAnUnknownCodeSystem_rejectsAndNamesTheSystem() {
		Parameters outcome = validateByUrl(VS_UNKNOWN_URL)
				.andParameter("code", new CodeType("x1"))
				.andParameter("system", new UriType(UNKNOWN_CS_URL))
				.andParameter("systemVersion", new StringType("1.0"))
				.execute();

		assertThat(result(outcome)).isFalse();
		assertThat(causedByUnknownSystem(outcome)).singleElement().asString().startsWith(UNKNOWN_CS_URL);
	}

	/**
	 * A CodeableConcept is valid if any coding is; when none is, a coding from an unknown code system still names
	 * that system, whichever position it holds.
	 */
	@Test
	void validateCode_codeableConceptWithUnknownSystemCodingThenNonMemberCoding_rejectsAndNamesTheSystem() {
		CodeableConcept concept = new CodeableConcept();
		concept.addCoding(new Coding(UNKNOWN_CS_URL, "x1", null));
		concept.addCoding(new Coding(CS_URL, "blue", null));

		Parameters outcome = validateByUrl(VS_UNKNOWN_URL)
				.andParameter("codeableConcept", concept)
				.execute();

		assertRejectedNaming(outcome, UNKNOWN_CS_URL);
	}

	@Test
	void validateCode_codeableConceptWithUnknownSystemCodingAndMemberCoding_accepts() {
		CodeableConcept concept = new CodeableConcept();
		concept.addCoding(new Coding(UNKNOWN_CS_URL, "x1", null));
		concept.addCoding(new Coding(CS_URL, "red", null));

		Parameters outcome = validateByUrl(VS_MIXED_URL)
				.andParameter("codeableConcept", concept)
				.execute();

		assertThat(result(outcome)).isTrue();
	}

	@Test
	void validateCode_mixedValueSetWithCodeFromTheResolvableInclude_accepts() {
		Parameters outcome = validateByUrl(VS_MIXED_URL)
				.andParameter("code", new CodeType("red"))
				.andParameter("system", new UriType(CS_URL))
				.execute();

		assertThat(result(outcome)).isTrue();
		assertThat(causedByUnknownSystem(outcome)).isEmpty();
	}

	@Test
	void validateCode_mixedValueSetWithCodeFromTheUnresolvableInclude_rejectsAndNamesTheSystem() {
		Parameters outcome = validateByUrl(VS_MIXED_URL)
				.andParameter("code", new CodeType("x1"))
				.andParameter("system", new UriType(UNKNOWN_CS_URL))
				.execute();

		assertRejectedNaming(outcome, UNKNOWN_CS_URL);
	}

	@Test
	void validateCode_mixedValueSetWithCodeTheResolvableIncludeDoesNotList_rejectsWithoutNamingASystem() {
		Parameters outcome = validateByUrl(VS_MIXED_URL)
				.andParameter("code", new CodeType("blue"))
				.andParameter("system", new UriType(CS_URL))
				.execute();

		assertThat(result(outcome)).isFalse();
		assertThat(causedByUnknownSystem(outcome)).isEmpty();
	}

	/**
	 * An exclude naming an uninstalled version cannot be applied, so membership of any code from that code system
	 * cannot be established.
	 */
	@Test
	void validateCode_excludeNamingAnUninstalledVersion_rejectsAndNamesTheVersion() {
		Parameters outcome = validateByUrl(VS_EXCLUDE_URL)
				.andParameter("code", new CodeType("blue"))
				.andParameter("system", new UriType(CS_URL))
				.execute();

		assertRejectedNaming(outcome, CS_URL + "|2.0");
	}

	/**
	 * In JPA a not-present CodeSystem resource is how externally loaded terminology is stored, its concepts held in
	 * the terminology tables, so the whole-system include expands from those tables. With none loaded the code is
	 * simply not a member; the code system is not reported as unknown.
	 */
	@Test
	void validateCode_wholeSystemIncludeOfANotPresentCodeSystemWithNoLoadedConcepts_rejectsWithoutNamingASystem() {
		Parameters outcome = validateByUrl(VS_NOT_PRESENT_URL)
				.andParameter("code", new CodeType("anything"))
				.andParameter("system", new UriType(NOT_PRESENT_CS_URL))
				.execute();

		assertThat(result(outcome)).isFalse();
		assertThat(causedByUnknownSystem(outcome)).isEmpty();
	}

	/**
	 * Control: a code system served without a CodeSystem resource is understood, so its listed codes validate.
	 */
	@Test
	void validateCode_listedBcp47Code_accepts() {
		Parameters outcome = validateByUrl(VS_BCP47_URL)
				.andParameter("code", new CodeType("en-CA"))
				.andParameter("system", new UriType("urn:ietf:bcp:47"))
				.execute();

		assertThat(result(outcome)).isTrue();
		assertThat(causedByUnknownSystem(outcome)).isEmpty();
	}

	private IOperationUntypedWithInputAndPartialOutput<Parameters> validateByUrl(String theValueSetUrl) {
		return myClient.operation()
				.onType(ValueSet.class)
				.named("validate-code")
				.withParameter(Parameters.class, "url", new UriType(theValueSetUrl));
	}

	private void assertRejectedNaming(Parameters theOutcome, String theUnknownSystem) {
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(theOutcome));
		assertThat(result(theOutcome)).isFalse();
		assertThat(causedByUnknownSystem(theOutcome)).containsExactly(theUnknownSystem);
	}

	private static boolean result(Parameters theOutcome) {
		return ((BooleanType) theOutcome.getParameterValue("result")).booleanValue();
	}

	private List<String> causedByUnknownSystem(Parameters theOutcome) {
		return ParametersUtil.getNamedParameterValuesAsString(
				myFhirContext, theOutcome, IValidationSupport.CodeValidationResult.CAUSED_BY_UNKNOWN_SYSTEM);
	}

	private static ValueSet newValueSet(String theId, String theUrl) {
		ValueSet vs = new ValueSet();
		vs.setId(theId);
		vs.setUrl(theUrl);
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		return vs;
	}
}
