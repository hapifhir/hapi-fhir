package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validation on a server that holds two versions of the same CodeSystem or ValueSet, where a profile asks for
 * one of them. The nested classes cover the three shapes this takes:
 * <ul>
 *     <li>{@link MultiVersionCodeSystemTest} - two CodeSystem versions, one ValueSet that names one of them in
 *     {@literal compose.include.version}, and a profile bound to that ValueSet</li>
 *     <li>{@link MultiVersionValueSetTest} - one CodeSystem with no version holding every code, two ValueSet
 *     versions, and a profile bound to one of them by {@literal url|version}</li>
 *     <li>{@link MultiVersionCodeSystemAndValueSetTest} - both at once: two versions of each, with the profile
 *     naming a ValueSet version and that ValueSet naming a CodeSystem version. Every step has to keep the
 *     version for the caller to get the answer they asked for</li>
 * </ul>
 * Each test runs twice, once for each version, and <em>always saves the version it did not ask for last</em>.
 * That ordering is what lets these tests fail: a URL with no version resolves by {@literal meta.lastUpdated},
 * so the version saved last is the one a lookup that drops the version finds. Asking for the last-saved
 * version instead would pass even against code that ignores versions altogether, and running both directions
 * rules out a fix that just picks the highest version number.
 * <p/>
 * Every test asserts the behaviour we want, so a failing test means the bug is present.
 */
// Created by Claude Opus 5
public class ValidateWithMultiVersionTerminologyR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ValidateWithMultiVersionTerminologyR4Test.class);

	private static final String CS_URL = "http://example.org/fhir/CodeSystem/colour";
	private static final String VS_URL = "http://example.org/fhir/ValueSet/colour";
	private static final String PROFILE_URL = "http://example.org/fhir/StructureDefinition/ColourObservation";

	private static final String VERSION_OLDER = "1.0.0";
	private static final String VERSION_NEWER = "1.0.1";

	/** A code that only version 1.0.0 has. */
	private static final String CODE_IN_OLDER_VERSION = "vermilion";

	/** A code that only version 1.0.1 has. */
	private static final String CODE_IN_NEWER_VERSION = "cerulean";

	@Nested
	class MultiVersionCodeSystemTest {

		void setUpPinning(String thePinnedVersion) {
			createCodeSystem(thePinnedVersion, codeIn(thePinnedVersion));
			sleepUntilTimeChange();
			createCodeSystem(otherThan(thePinnedVersion), codeIn(otherThan(thePinnedVersion)));

			createValueSetIncludingCodeSystemVersion(VERSION_OLDER, thePinnedVersion);
			createProfileBoundTo(VS_URL);

			myTerminologyDeferredStorageSvc.saveAllDeferred();
		}

		/**
		 * The terminology layer gets this right on its own, so the validation failures below are a bug and not
		 * a missing feature.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validateCodeInValueSet_codeFromPinnedCodeSystemVersion_isValid(String thePinnedVersion) {
			// Setup
			setUpPinning(thePinnedVersion);
			ValueSet valueSet = (ValueSet) myValidationSupport.fetchValueSet(VS_URL);
			assertThat(valueSet).isNotNull();

			// Test
			IValidationSupport.CodeValidationResult result =
				validateCodeInValueSet(valueSet, codeIn(thePinnedVersion));

			// Verify
			assertThat(result).isNotNull();
			assertThat(result.isOk()).isTrue();
		}

		/**
		 * The same call for a code the named CodeSystem version does not have, which must be rejected. Without
		 * this, the test above would also pass against a terminology layer that accepted every code.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validateCodeInValueSet_codeOutsidePinnedCodeSystemVersion_isNotValid(String thePinnedVersion) {
			// Setup
			setUpPinning(thePinnedVersion);
			ValueSet valueSet = (ValueSet) myValidationSupport.fetchValueSet(VS_URL);
			assertThat(valueSet).isNotNull();

			// Test
			IValidationSupport.CodeValidationResult result =
				validateCodeInValueSet(valueSet, codeIn(otherThan(thePinnedVersion)));

			// Verify
			assertThat(result).isNotNull();
			assertThat(result.isOk()).isFalse();
		}

		/**
		 * The code is in the CodeSystem version the ValueSet names, so it is in the bound ValueSet and
		 * validation must accept it.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeFromPinnedCodeSystemVersion_hasNoErrors(String thePinnedVersion) {
			// Setup
			setUpPinning(thePinnedVersion);

			// Test
			OperationOutcome oo = validateObservationWithCode(codeIn(thePinnedVersion));

			// Verify
			assertThat(errorDiagnostics(oo)).isEmpty();
		}

		/**
		 * The other direction, so that resolving to the last-saved version is not mistaken for a fix: a code
		 * that exists only in the other CodeSystem version is not in the ValueSet.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeOutsidePinnedCodeSystemVersion_hasErrors(String thePinnedVersion) {
			// Setup
			setUpPinning(thePinnedVersion);
			String unpinnedCode = codeIn(otherThan(thePinnedVersion));

			// Test
			OperationOutcome oo = validateObservationWithCode(unpinnedCode);

			// Verify
			assertThat(errorDiagnostics(oo)).anyMatch(t -> t.contains(unpinnedCode));
		}
	}

	@Nested
	class MultiVersionValueSetTest {

		void setUpPinning(String thePinnedVersion) {
			createCodeSystem(null, CODE_IN_OLDER_VERSION, CODE_IN_NEWER_VERSION);

			createValueSetIncludingCodes(thePinnedVersion, codeIn(thePinnedVersion));
			sleepUntilTimeChange();
			createValueSetIncludingCodes(otherThan(thePinnedVersion), codeIn(otherThan(thePinnedVersion)));

			createProfileBoundTo(VS_URL + "|" + thePinnedVersion);

			myTerminologyDeferredStorageSvc.saveAllDeferred();
		}

		/**
		 * Checks resolution on its own, before validation: asking for a ValueSet by version must return that
		 * version, not the one saved after it.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void fetchValueSet_pinnedVersionWrittenFirst_returnsThatVersion(String thePinnedVersion) {
			// Setup
			setUpPinning(thePinnedVersion);

			// Test
			ValueSet valueSet = (ValueSet) myValidationSupport.fetchValueSet(VS_URL + "|" + thePinnedVersion);

			// Verify
			assertThat(valueSet).isNotNull();
			assertThat(valueSet.getVersion()).isEqualTo(thePinnedVersion);
		}

		/**
		 * The terminology layer, given that ValueSet version, accepts the code it lists.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validateCodeInValueSet_codeFromPinnedValueSetVersion_isValid(String thePinnedVersion) {
			// Setup
			setUpPinning(thePinnedVersion);
			ValueSet valueSet = (ValueSet) myValidationSupport.fetchValueSet(VS_URL + "|" + thePinnedVersion);
			assertThat(valueSet).isNotNull();

			// Test
			IValidationSupport.CodeValidationResult result =
				validateCodeInValueSet(valueSet, codeIn(thePinnedVersion));

			// Verify
			assertThat(result).isNotNull();
			assertThat(result.isOk()).isTrue();
		}

		/**
		 * The same call for a code that only the other ValueSet version lists, which must be rejected.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validateCodeInValueSet_codeOnlyInUnpinnedValueSetVersion_isNotValid(String thePinnedVersion) {
			// Setup
			setUpPinning(thePinnedVersion);
			ValueSet valueSet = (ValueSet) myValidationSupport.fetchValueSet(VS_URL + "|" + thePinnedVersion);
			assertThat(valueSet).isNotNull();

			// Test
			IValidationSupport.CodeValidationResult result =
				validateCodeInValueSet(valueSet, codeIn(otherThan(thePinnedVersion)));

			// Verify
			assertThat(result).isNotNull();
			assertThat(result.isOk()).isFalse();
		}

		/**
		 * The profile names one version of the ValueSet and the code is in that version, so validation has to
		 * accept it even though the other version was saved afterwards.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeFromPinnedValueSetVersion_hasNoErrors(String thePinnedVersion) {
			// Setup
			setUpPinning(thePinnedVersion);

			// Test
			OperationOutcome oo = validateObservationWithCode(codeIn(thePinnedVersion));

			// Verify
			assertThat(errorDiagnostics(oo)).isEmpty();
		}

		/**
		 * The other direction: a code that only the other ValueSet version has must be rejected. Code that
		 * passes the test above by validating against the last-saved version fails here.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeOnlyInUnpinnedValueSetVersion_hasErrors(String thePinnedVersion) {
			// Setup
			setUpPinning(thePinnedVersion);
			String unpinnedCode = codeIn(otherThan(thePinnedVersion));

			// Test
			OperationOutcome oo = validateObservationWithCode(unpinnedCode);

			// Verify
			assertThat(errorDiagnostics(oo)).anyMatch(t -> t.contains(unpinnedCode));
		}
	}

	@Nested
	class MultiVersionCodeSystemAndValueSetTest {

		void setUpPinning(String thePinnedVersion) {
			String unpinnedVersion = otherThan(thePinnedVersion);

			createCodeSystem(thePinnedVersion, codeIn(thePinnedVersion));
			sleepUntilTimeChange();
			createCodeSystem(unpinnedVersion, codeIn(unpinnedVersion));

			createValueSetIncludingCodeSystemVersion(thePinnedVersion, thePinnedVersion);
			sleepUntilTimeChange();
			createValueSetIncludingCodeSystemVersion(unpinnedVersion, unpinnedVersion);

			createProfileBoundTo(VS_URL + "|" + thePinnedVersion);

			myTerminologyDeferredStorageSvc.saveAllDeferred();
		}

		/**
		 * Checks storage and resolution on their own: the ValueSet comes back at the version asked for and
		 * still names its own CodeSystem version, so what the validator reads is correct before validation
		 * starts.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void fetchValueSet_pinnedVersionWrittenFirst_returnsThatVersionStillPinningItsCodeSystem(
				String thePinnedVersion) {
			// Setup
			setUpPinning(thePinnedVersion);

			// Test
			ValueSet valueSet = (ValueSet) myValidationSupport.fetchValueSet(VS_URL + "|" + thePinnedVersion);

			// Verify
			assertThat(valueSet).isNotNull();
			assertThat(valueSet.getVersion()).isEqualTo(thePinnedVersion);
			assertThat(valueSet.getCompose().getIncludeFirstRep().getVersion()).isEqualTo(thePinnedVersion);
		}

		/**
		 * The accepted case: the profile names a ValueSet version, that ValueSet names a CodeSystem version,
		 * and the code is in it.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeInEveryVersionThatWasNamed_hasNoErrors(String thePinnedVersion) {
			// Setup
			setUpPinning(thePinnedVersion);

			// Test
			OperationOutcome oo = validateObservationWithCode(codeIn(thePinnedVersion));

			// Verify
			assertThat(errorDiagnostics(oo)).isEmpty();
		}

		/**
		 * The other direction: a code that only the other ValueSet version and its CodeSystem version have
		 * must be rejected.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeOnlyInTheVersionsThatWereNotNamed_hasErrors(String thePinnedVersion) {
			// Setup
			setUpPinning(thePinnedVersion);
			String unpinnedCode = codeIn(otherThan(thePinnedVersion));

			// Test
			OperationOutcome oo = validateObservationWithCode(unpinnedCode);

			// Verify
			assertThat(errorDiagnostics(oo)).anyMatch(t -> t.contains(unpinnedCode));
		}
	}

	/** The code that only the given version has. */
	private static String codeIn(String theVersion) {
		return VERSION_OLDER.equals(theVersion) ? CODE_IN_OLDER_VERSION : CODE_IN_NEWER_VERSION;
	}

	/** The version the test does not ask for, and therefore the one it saves last. */
	private static String otherThan(String theVersion) {
		return VERSION_OLDER.equals(theVersion) ? VERSION_NEWER : VERSION_OLDER;
	}

	private void createCodeSystem(String theVersion, String... theCodes) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setVersion(theVersion);
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		for (String code : theCodes) {
			codeSystem.addConcept().setCode(code).setDisplay(code);
		}
		myCodeSystemDao.create(codeSystem, mySrd);
	}

	private void createValueSetIncludingCodeSystemVersion(String theValueSetVersion, String theCodeSystemVersion) {
		ValueSet valueSet = newValueSet(theValueSetVersion);
		valueSet.getCompose().addInclude().setSystem(CS_URL).setVersion(theCodeSystemVersion);
		myValueSetDao.create(valueSet, mySrd);
	}

	private void createValueSetIncludingCodes(String theValueSetVersion, String... theCodes) {
		ValueSet valueSet = newValueSet(theValueSetVersion);
		ValueSet.ConceptSetComponent include = valueSet.getCompose().addInclude().setSystem(CS_URL);
		for (String code : theCodes) {
			include.addConcept().setCode(code);
		}
		myValueSetDao.create(valueSet, mySrd);
	}

	private ValueSet newValueSet(String theVersion) {
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(VS_URL);
		valueSet.setVersion(theVersion);
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		return valueSet;
	}

	private void createProfileBoundTo(String theValueSetUrl) {
		StructureDefinition profile = getStructureDefinition();

		ElementDefinition root = profile.getDifferential().addElement();
		root.setId("Observation");
		root.setPath("Observation");

		ElementDefinition code = profile.getDifferential().addElement();
		code.setId("Observation.code");
		code.setPath("Observation.code");
		code.getBinding().setStrength(Enumerations.BindingStrength.REQUIRED).setValueSet(theValueSetUrl);

		myStructureDefinitionDao.create(profile, mySrd);
	}

	private static @NonNull StructureDefinition getStructureDefinition() {
		StructureDefinition profile = new StructureDefinition();
		profile.setUrl(PROFILE_URL);
		profile.setName("ColourObservation");
		profile.setStatus(Enumerations.PublicationStatus.ACTIVE);
		profile.setFhirVersion(Enumerations.FHIRVersion._4_0_1);
		profile.setKind(StructureDefinition.StructureDefinitionKind.RESOURCE);
		profile.setAbstract(false);
		profile.setType("Observation");
		profile.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Observation");
		profile.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		return profile;
	}

	private IValidationSupport.CodeValidationResult validateCodeInValueSet(ValueSet theValueSet, String theCode) {
		return myValidationSupport.validateCodeInValueSet(
			new ValidationSupportContext(myValidationSupport),
			new ConceptValidationOptions(),
			CS_URL,
			theCode,
			null,
			theValueSet);
	}

	private OperationOutcome validateObservationWithCode(String theCode) {
		Observation observation = new Observation();
		observation.getMeta().addProfile(PROFILE_URL);
		observation.setStatus(Observation.ObservationStatus.FINAL);
		observation.getCode().addCoding().setSystem(CS_URL).setCode(theCode).setDisplay(theCode);

		OperationOutcome oo;
		try {
			MethodOutcome outcome =
				myObservationDao.validate(observation, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
			oo = (OperationOutcome) outcome.getOperationOutcome();
		} catch (PreconditionFailedException e) {
			oo = (OperationOutcome) e.getOperationOutcome();
		}
		ourLog.info("Validation errors: {}", errorDiagnostics(oo));
		return oo;
	}

	private List<String> errorDiagnostics(OperationOutcome theOutcome) {
		return theOutcome.getIssue().stream()
			.filter(t -> t.getSeverity() == OperationOutcome.IssueSeverity.ERROR
				|| t.getSeverity() == OperationOutcome.IssueSeverity.FATAL)
			.map(OperationOutcome.OperationOutcomeIssueComponent::getDiagnostics)
			.toList();
	}
}
