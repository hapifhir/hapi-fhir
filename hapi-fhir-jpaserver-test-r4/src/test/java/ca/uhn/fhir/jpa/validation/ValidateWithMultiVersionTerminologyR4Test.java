package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValidateCodeRequest;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.UriType;
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
 *     <li>{@link ValueSetValidateCodeOperationTest} - the same two versions of each reached through the
 *     {@literal ValueSet/$validate-code} operation rather than through {@literal $validate}</li>
 * </ul>
 * Each test runs twice, once for each version, and <em>always saves the version it did not ask for last</em>.
 * That ordering is what lets these tests fail: a URL with no version resolves by {@literal meta.lastUpdated},
 * so the version saved last is the one a lookup that drops the version finds. Asking for the last-saved
 * version instead would pass even against code that ignores versions altogether, and running both directions
 * rules out a fix that just picks the highest version number.
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

		void setUpWithSpecifiedVersion(String theSpecifiedVersion) {
			createCodeSystem(theSpecifiedVersion, codeIn(theSpecifiedVersion));
			sleepUntilTimeChange();
			createCodeSystem(otherThan(theSpecifiedVersion), codeIn(otherThan(theSpecifiedVersion)));

			createValueSetIncludingCodeSystemVersion(VERSION_OLDER, theSpecifiedVersion);
			createProfileBoundTo(VS_URL);

			myTerminologyDeferredStorageSvc.saveAllDeferred();
		}

		/**
		 * Checks the terminology layer on its own, before validation: given the ValueSet, it accepts the code
		 * the CodeSystem version that ValueSet names holds.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validateCodeInValueSet_codeFromSpecifiedCodeSystemVersion_isValid(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);
			ValueSet valueSet = (ValueSet) myValidationSupport.fetchValueSet(VS_URL);
			assertThat(valueSet).isNotNull();

			// Test
			IValidationSupport.CodeValidationResult result =
				validateCodeInValueSet(valueSet, codeIn(theSpecifiedVersion));

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
		void validateCodeInValueSet_codeOutsideSpecifiedCodeSystemVersion_isNotValid(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);
			ValueSet valueSet = (ValueSet) myValidationSupport.fetchValueSet(VS_URL);
			assertThat(valueSet).isNotNull();

			// Test
			IValidationSupport.CodeValidationResult result =
				validateCodeInValueSet(valueSet, codeIn(otherThan(theSpecifiedVersion)));

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
		void validate_codeFromSpecifiedCodeSystemVersion_hasNoErrors(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);

			// Test
			OperationOutcome oo = validateObservationWithCode(codeIn(theSpecifiedVersion));

			// Verify
			assertThat(errorDiagnostics(oo)).isEmpty();
		}

		/**
		 * The other direction, so that resolving to the last-saved version is not mistaken for a fix: a code
		 * that exists only in the other CodeSystem version is not in the ValueSet.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeOutsideSpecifiedCodeSystemVersion_hasErrors(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);
			String codeFromOtherVersion = codeIn(otherThan(theSpecifiedVersion));

			// Test
			OperationOutcome oo = validateObservationWithCode(codeFromOtherVersion);

			// Verify
			assertThat(errorDiagnostics(oo)).anyMatch(t -> t.contains(codeFromOtherVersion));
		}
	}

	@Nested
	class MultiVersionValueSetTest {

		void setUpWithSpecifiedVersion(String theSpecifiedVersion) {
			createCodeSystem(null, CODE_IN_OLDER_VERSION, CODE_IN_NEWER_VERSION);

			createValueSetIncludingCodes(theSpecifiedVersion, codeIn(theSpecifiedVersion));
			sleepUntilTimeChange();
			createValueSetIncludingCodes(otherThan(theSpecifiedVersion), codeIn(otherThan(theSpecifiedVersion)));

			createProfileBoundTo(VS_URL + "|" + theSpecifiedVersion);

			myTerminologyDeferredStorageSvc.saveAllDeferred();
		}

		/**
		 * Checks resolution on its own, before validation: asking for a ValueSet by version must return that
		 * version, not the one saved after it.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void fetchValueSet_specifiedVersionWrittenFirst_returnsThatVersion(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);

			// Test
			ValueSet valueSet = (ValueSet) myValidationSupport.fetchValueSet(VS_URL + "|" + theSpecifiedVersion);

			// Verify
			assertThat(valueSet).isNotNull();
			assertThat(valueSet.getVersion()).isEqualTo(theSpecifiedVersion);
		}

		/**
		 * The terminology layer, given that ValueSet version, accepts the code it lists.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validateCodeInValueSet_codeFromSpecifiedValueSetVersion_isValid(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);
			ValueSet valueSet = (ValueSet) myValidationSupport.fetchValueSet(VS_URL + "|" + theSpecifiedVersion);
			assertThat(valueSet).isNotNull();

			// Test
			IValidationSupport.CodeValidationResult result =
				validateCodeInValueSet(valueSet, codeIn(theSpecifiedVersion));

			// Verify
			assertThat(result).isNotNull();
			assertThat(result.isOk()).isTrue();
		}

		/**
		 * The same call for a code that only the other ValueSet version lists, which must be rejected.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validateCodeInValueSet_codeOnlyInUnspecifiedValueSetVersion_isNotValid(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);
			ValueSet valueSet = (ValueSet) myValidationSupport.fetchValueSet(VS_URL + "|" + theSpecifiedVersion);
			assertThat(valueSet).isNotNull();

			// Test
			IValidationSupport.CodeValidationResult result =
				validateCodeInValueSet(valueSet, codeIn(otherThan(theSpecifiedVersion)));

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
		void validate_codeFromSpecifiedValueSetVersion_hasNoErrors(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);

			// Test
			OperationOutcome oo = validateObservationWithCode(codeIn(theSpecifiedVersion));

			// Verify
			assertThat(errorDiagnostics(oo)).isEmpty();
		}

		/**
		 * The other direction: a code that only the other ValueSet version has must be rejected. Code that
		 * passes the test above by validating against the last-saved version fails here.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeOnlyInUnspecifiedValueSetVersion_hasErrors(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);
			String codeFromOtherVersion = codeIn(otherThan(theSpecifiedVersion));

			// Test
			OperationOutcome oo = validateObservationWithCode(codeFromOtherVersion);

			// Verify
			assertThat(errorDiagnostics(oo)).anyMatch(t -> t.contains(codeFromOtherVersion));
		}
	}

	@Nested
	class MultiVersionCodeSystemAndValueSetTest {

		void setUpWithSpecifiedVersion(String theSpecifiedVersion) {
			String otherVersion = otherThan(theSpecifiedVersion);

			createCodeSystem(theSpecifiedVersion, codeIn(theSpecifiedVersion));
			sleepUntilTimeChange();
			createCodeSystem(otherVersion, codeIn(otherVersion));

			createValueSetIncludingCodeSystemVersion(theSpecifiedVersion, theSpecifiedVersion);
			sleepUntilTimeChange();
			createValueSetIncludingCodeSystemVersion(otherVersion, otherVersion);

			createProfileBoundTo(VS_URL + "|" + theSpecifiedVersion);

			myTerminologyDeferredStorageSvc.saveAllDeferred();
		}

		/**
		 * Checks storage and resolution on their own: the ValueSet comes back at the version asked for and
		 * still names its own CodeSystem version, so what the validator reads is correct before validation
		 * starts.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void fetchValueSet_specifiedVersionWrittenFirst_returnsThatVersionStillNamingItsCodeSystemVersion(
				String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);

			// Test
			ValueSet valueSet = (ValueSet) myValidationSupport.fetchValueSet(VS_URL + "|" + theSpecifiedVersion);

			// Verify
			assertThat(valueSet).isNotNull();
			assertThat(valueSet.getVersion()).isEqualTo(theSpecifiedVersion);
			assertThat(valueSet.getCompose().getIncludeFirstRep().getVersion()).isEqualTo(theSpecifiedVersion);
		}

		/**
		 * The accepted case: the profile names a ValueSet version, that ValueSet names a CodeSystem version,
		 * and the code is in it.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeInEveryVersionThatWasNamed_hasNoErrors(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);

			// Test
			OperationOutcome oo = validateObservationWithCode(codeIn(theSpecifiedVersion));

			// Verify
			assertThat(errorDiagnostics(oo)).isEmpty();
		}

		/**
		 * The other direction: a code that only the other ValueSet version and its CodeSystem version have
		 * must be rejected.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validate_codeOnlyInTheVersionsThatWereNotNamed_hasErrors(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);
			String codeFromOtherVersion = codeIn(otherThan(theSpecifiedVersion));

			// Test
			OperationOutcome oo = validateObservationWithCode(codeFromOtherVersion);

			// Verify
			assertThat(errorDiagnostics(oo)).anyMatch(t -> t.contains(codeFromOtherVersion));
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

	/**
	 * The code system version named on {@link IValidationSupport#validateCode(ValidationSupportContext,
	 * ConceptValidationOptions, String, String, String, String, String)} itself, with no ValueSet involved.
	 * This is the overload the validator uses to re-check a code against its code system after a ValueSet
	 * accepted it, so dropping the version here rejects codes the ValueSet allowed.
	 */
	@Nested
	class CodeSystemVersionNamedOnValidateCodeTest {

		void setUpWithSpecifiedVersion(String theSpecifiedVersion) {
			createCodeSystem(theSpecifiedVersion, codeIn(theSpecifiedVersion));
			sleepUntilTimeChange();
			createCodeSystem(otherThan(theSpecifiedVersion), codeIn(otherThan(theSpecifiedVersion)));

			myTerminologyDeferredStorageSvc.saveAllDeferred();
		}

		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validateCode_codeFromTheSpecifiedCodeSystemVersion_isValid(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);

			// Test
			IValidationSupport.CodeValidationResult result =
				validateCodeInCodeSystem(theSpecifiedVersion, codeIn(theSpecifiedVersion));

			// Verify
			assertThat(result).isNotNull();
			assertThat(result.isOk()).isTrue();
		}

		/**
		 * Without this, the test above would also pass against code which accepted every code regardless of
		 * the version it was asked for.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validateCode_codeOutsideTheSpecifiedCodeSystemVersion_isNotValid(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);

			// Test
			IValidationSupport.CodeValidationResult result =
				validateCodeInCodeSystem(theSpecifiedVersion, codeIn(otherThan(theSpecifiedVersion)));

			// Verify
			assertThat(result).isNotNull();
			assertThat(result.isOk()).isFalse();
		}

		/**
		 * Naming no version has to keep resolving to whichever version is current, which setUpWithSpecifiedVersion always
		 * saves last. A version must not be invented when the caller named none.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validateCode_withoutACodeSystemVersion_usesTheCurrentVersion(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);
			String lastSavedVersion = otherThan(theSpecifiedVersion);

			// Test
			IValidationSupport.CodeValidationResult result =
				validateCodeInCodeSystem(null, codeIn(lastSavedVersion));

			// Verify
			assertThat(result).isNotNull();
			assertThat(result.isOk()).isTrue();
		}
	}

	private IValidationSupport.CodeValidationResult validateCodeInCodeSystem(
			String theCodeSystemVersion, String theCode) {
		return myValidationSupport.validateCode(
			new ValidationSupportContext(myValidationSupport),
			new ConceptValidationOptions(),
			new ValidateCodeRequest(CS_URL, theCodeSystemVersion, theCode, null, null));
	}

	/**
	 * {@literal ValueSet/$validate-code} rather than {@literal $validate}. ValueSetOperationProvider joins its
	 * {@literal url}/{@literal valueSetVersion} and {@literal system}/{@literal systemVersion} parameters into
	 * {@literal url|version} canonicals before calling the DAO, so this is the shape the DAO receives them in.
	 */
	@Nested
	class ValueSetValidateCodeOperationTest {

		void setUpWithSpecifiedVersion(String theSpecifiedVersion) {
			String otherVersion = otherThan(theSpecifiedVersion);

			createCodeSystem(theSpecifiedVersion, codeIn(theSpecifiedVersion));
			sleepUntilTimeChange();
			createCodeSystem(otherVersion, codeIn(otherVersion));

			createValueSetIncludingCodeSystemVersion(theSpecifiedVersion, theSpecifiedVersion);
			sleepUntilTimeChange();
			createValueSetIncludingCodeSystemVersion(otherVersion, otherVersion);

			myTerminologyDeferredStorageSvc.saveAllDeferred();
		}

		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validateCode_codeInTheSpecifiedValueSetVersion_isValid(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);

			// Test
			IValidationSupport.CodeValidationResult result = validateCodeOnValueSet(
				VS_URL + "|" + theSpecifiedVersion, CS_URL + "|" + theSpecifiedVersion, codeIn(theSpecifiedVersion));

			// Verify
			assertThat(result).isNotNull();
			assertThat(result.isOk()).isTrue();
		}

		/**
		 * Without this, the test above would also pass against code which resolved both canonicals to whichever
		 * version was saved last.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validateCode_codeOnlyInTheOtherValueSetVersion_isNotValid(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);

			// Test
			IValidationSupport.CodeValidationResult result = validateCodeOnValueSet(
				VS_URL + "|" + theSpecifiedVersion,
				CS_URL + "|" + theSpecifiedVersion,
				codeIn(otherThan(theSpecifiedVersion)));

			// Verify
			assertThat(result).isNotNull();
			assertThat(result.isOk()).isFalse();
		}

		/**
		 * A canonical whose separator arrives percent-encoded, which is what a client sends when it puts the
		 * whole canonical in a URL parameter. It has to name the same version as the literal pipe does.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validateCode_codeSystemVersionSeparatorPercentEncoded_isValid(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);

			// Test
			IValidationSupport.CodeValidationResult result = validateCodeOnValueSet(
				VS_URL + "|" + theSpecifiedVersion, CS_URL + "%7C" + theSpecifiedVersion, codeIn(theSpecifiedVersion));

			// Verify
			assertThat(result).isNotNull();
			assertThat(result.isOk()).isTrue();
		}

		/**
		 * The system parameter is optional on the operation, and omitting it reaches the support with a null
		 * system, where the in-memory expansion cannot match the code. Pinned rather than endorsed: the DAO
		 * passes null for an absent system whether it parses the canonical or not, so this is the behaviour
		 * that was already there, and the assertion is here to catch the canonical parsing changing it.
		 */
		@ParameterizedTest
		@ValueSource(strings = {VERSION_OLDER, VERSION_NEWER})
		void validateCode_withoutACodeSystem_isNotValid(String theSpecifiedVersion) {
			// Setup
			setUpWithSpecifiedVersion(theSpecifiedVersion);

			// Test
			IValidationSupport.CodeValidationResult result =
				validateCodeOnValueSet(VS_URL + "|" + theSpecifiedVersion, null, codeIn(theSpecifiedVersion));

			// Verify
			assertThat(result).isNotNull();
			assertThat(result.isOk()).isFalse();
			assertThat(result.getMessage()).contains("for in-memory expansion of ValueSet");
		}
	}

	private IValidationSupport.CodeValidationResult validateCodeOnValueSet(
			String theValueSetIdentifier, String theCodeSystemIdentifier, String theCode) {
		return myValueSetDao.validateCode(
			new UriType(theValueSetIdentifier),
			null,
			new CodeType(theCode),
			theCodeSystemIdentifier == null ? null : new UriType(theCodeSystemIdentifier),
			null,
			null,
			null,
			mySrd);
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
