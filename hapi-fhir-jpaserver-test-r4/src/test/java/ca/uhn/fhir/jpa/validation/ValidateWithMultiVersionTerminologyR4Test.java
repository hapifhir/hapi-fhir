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
 * Validation on a server that holds two versions of the same canonical, where a binding names one of them. The
 * nested fixtures put the second version on either side of the binding, then on both sides at once:
 * <ul>
 *     <li>{@link MultiVersionCodeSystemTest} - two CodeSystem versions, one ValueSet naming one of them in
 *     {@literal compose.include.version}, and a profile bound to that ValueSet</li>
 *     <li>{@link MultiVersionValueSetTest} - one unversioned CodeSystem holding every code, two ValueSet
 *     versions, and a profile bound to one of them by {@literal url|version}</li>
 *     <li>{@link MultiVersionCodeSystemAndValueSetTest} - both at once: two versions of each, the binding
 *     pinning the ValueSet and the ValueSet pinning the CodeSystem. Neither link is ambiguous on its own, and
 *     the whole chain has to hold for a caller to get the answer they asked for</li>
 * </ul>
 * Every fixture runs twice, once pinning each version, and <em>always writes the unpinned version last</em>.
 * That ordering is what gives the tests teeth: an unversioned canonical resolves by {@literal meta.lastUpdated},
 * so the version written last is the one a lookup that ignores the pin would land on. Pinning the
 * last-written version instead would let an implementation that ignores pins altogether pass. Running both
 * directions also rules out a fix that merely prefers the highest business version.
 * <p/>
 * Every test asserts the <em>correct</em> behaviour, so a failure is a bug reproducing.
 */
// Created by Claude Opus 5
public class ValidateWithMultiVersionTerminologyR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ValidateWithMultiVersionTerminologyR4Test.class);

	private static final String CS_URL = "http://example.org/fhir/CodeSystem/colour";
	private static final String VS_URL = "http://example.org/fhir/ValueSet/colour";
	private static final String PROFILE_URL = "http://example.org/fhir/StructureDefinition/ColourObservation";

	private static final String VERSION_OLDER = "1.0.0";
	private static final String VERSION_NEWER = "1.0.1";

	/** Reachable only through version 1.0.0. */
	private static final String CODE_IN_OLDER_VERSION = "vermilion";

	/** Reachable only through version 1.0.1. */
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
		 * The terminology layer on its own gets this right, which is what makes the validation result below a
		 * contradiction rather than a missing feature.
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
		 * The same layer, asked about the code the pinned CodeSystem version does not carry, says no. Without
		 * this, the assertion above would also hold against a terminology layer that accepted everything.
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
		 * The code is in the CodeSystem version the ValueSet pins, and therefore in the bound ValueSet.
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
		 * The other direction, so that resolving to the last-written version is not mistaken for a fix: a code
		 * that exists only in the unpinned CodeSystem version is outside the ValueSet.
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
		 * Isolates resolution from validation: asking for the pinned ValueSet should hand back that version,
		 * not the one written after it.
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
		 * The terminology layer, handed the pinned ValueSet, accepts the code it enumerates.
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
		 * The same layer, asked about the code only the unpinned ValueSet version enumerates, says no.
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
		 * The binding names one version of the ValueSet and the code is in that version, so validation has to
		 * accept it even though the other version was written afterwards.
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
		 * The other direction: a code only the unpinned ValueSet version carries is outside the binding.
		 * Passing the case above by quietly validating against the last-written version would fail here.
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
		 * Isolates storage and resolution from validation: the pinned ValueSet comes back at that version and
		 * still names its own CodeSystem version, so the chain the validator has to walk is intact before it
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
		 * The acceptance case: every link named explicitly, and the code reachable through all of them.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeReachableThroughTheWholePinnedChain_hasNoErrors(String thePinnedVersion) {
			// Setup
			setUpPinning(thePinnedVersion);

			// Test
			OperationOutcome oo = validateObservationWithCode(codeIn(thePinnedVersion));

			// Verify
			assertThat(errorDiagnostics(oo)).isEmpty();
		}

		/**
		 * The other direction: a code reachable only through the unpinned ValueSet and its unpinned CodeSystem
		 * is outside the chain at both links.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeReachableOnlyThroughTheUnpinnedChain_hasErrors(String thePinnedVersion) {
			// Setup
			setUpPinning(thePinnedVersion);
			String unpinnedCode = codeIn(otherThan(thePinnedVersion));

			// Test
			OperationOutcome oo = validateObservationWithCode(unpinnedCode);

			// Verify
			assertThat(errorDiagnostics(oo)).anyMatch(t -> t.contains(unpinnedCode));
		}
	}

	/** The code that only the given version carries. */
	private static String codeIn(String theVersion) {
		return VERSION_OLDER.equals(theVersion) ? CODE_IN_OLDER_VERSION : CODE_IN_NEWER_VERSION;
	}

	/** The version the fixture does not pin, and therefore the one it writes last. */
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
