package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.MissingCodeSystemPolicy;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ValidationSupportUtilsTest {

	public static final String SYSTEM_URL = "http://hl7.org/fhir/ValueSet/administrative-gender";
	public static final String SYSTEM_VERSION = "3.0.2";
	public static final String SYSTEM_URL_2 = "http://hl7.org/fhir/ValueSet/other-valueset";
	public static final String SYSTEM_VERSION_2 = "4.0.1";
	public static final String VALUE_SET_URL = "http://value.set/url";
	public static final String CODE = "CODE";
	public static final String NOT_THE_CODE = "not-the-code";

	@Test
	public void extractCodeSystemForCode_nullValueSet_returnsNull() {
		String result = ValidationSupportUtils.extractCodeSystemForCode(null, CODE);

		assertNull(result);
	}

	private static Stream<Arguments> extractCodeSystemForCodeDSTU3TestCases() {
		List<org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent> conceptWithCode = Lists.newArrayList(
			new org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent().setCode(NOT_THE_CODE),
			new org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent().setCode(CODE));

		List<org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent> conceptNoCode = Lists.newArrayList(
			new org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent().setCode(NOT_THE_CODE));

		return Stream.of(
			Arguments.of(Collections.emptyList(), null, "Empty ValueSet includes"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent(),
					new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent()),
				null, "ValueSet includes without system"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent()),
				null, "ValueSet include without system"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL)),
				SYSTEM_URL, "ValueSet include with one system and no code"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setConcept(conceptWithCode)),
				SYSTEM_URL, "ValueSet include with one system and code"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setVersion(SYSTEM_VERSION)),
				SYSTEM_URL + "|" + SYSTEM_VERSION, "ValueSet include with one versioned system and no code"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL),
					new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL_2)),
				null, "ValueSet includes with two systems and no code"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setConcept(conceptWithCode),
					new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL_2)),
				SYSTEM_URL, "ValueSet includes with two systems and correct code"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setVersion(SYSTEM_VERSION).setConcept(conceptWithCode),
					new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL_2).setVersion(SYSTEM_VERSION_2)),
				SYSTEM_URL + "|" + SYSTEM_VERSION, "ValueSet includes with two systems with versions and correct code"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setConcept(conceptNoCode),
					new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL_2)),
				null, "ValueSet includes with two systems and different code"));
	}

	@ParameterizedTest
	@MethodSource("extractCodeSystemForCodeDSTU3TestCases")
	public void extractCodeSystemForCodeDSTU3_withDifferentValueSetIncludes_returnsCorrectResult(List<org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent> theValueSetComponents,
																								 String theExpectedCodeSystem, String theMessage) {
		// setup
		org.hl7.fhir.dstu3.model.ValueSet valueSet = new org.hl7.fhir.dstu3.model.ValueSet();
		valueSet.setUrl(VALUE_SET_URL);
		valueSet.setCompose(new org.hl7.fhir.dstu3.model.ValueSet.ValueSetComposeComponent().setInclude(theValueSetComponents));

		// execute
		String result = ValidationSupportUtils.extractCodeSystemForCode(valueSet, CODE);

		// validate
		assertEquals(theExpectedCodeSystem, result, theMessage);
	}

	private static Stream<Arguments> extractCodeSystemForCodeR4TestCases() {
		List<ValueSet.ConceptReferenceComponent> conceptWithCode = Lists.newArrayList(
			new ValueSet.ConceptReferenceComponent().setCode(NOT_THE_CODE),
			new ValueSet.ConceptReferenceComponent().setCode(CODE));

		List<ValueSet.ConceptReferenceComponent> conceptNoCode = Lists.newArrayList(
			new ValueSet.ConceptReferenceComponent().setCode(NOT_THE_CODE));

		return Stream.of(
			Arguments.of(Collections.emptyList(), null, "Empty ValueSet includes"),
			Arguments.of(Lists.newArrayList(new ValueSet.ConceptSetComponent(), new ValueSet.ConceptSetComponent()),
				null, "ValueSet includes without system"),
			Arguments.of(Lists.newArrayList(new ValueSet.ConceptSetComponent()),
				null, "ValueSet include without system"),
			Arguments.of(Lists.newArrayList(new ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL)),
				SYSTEM_URL, "ValueSet include with one system and no code"),
			Arguments.of(Lists.newArrayList(new ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setConcept(conceptWithCode)),
				SYSTEM_URL, "ValueSet include with one system and code"),
			Arguments.of(Lists.newArrayList(new ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setVersion(SYSTEM_VERSION)),
				SYSTEM_URL + "|" + SYSTEM_VERSION, "ValueSet include with one versioned system and no code"),
			// compose.include.system is a plain URI, but guides are authored with the version packed into it
			Arguments.of(Lists.newArrayList(new ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL + "|" + SYSTEM_VERSION)),
				SYSTEM_URL + "|" + SYSTEM_VERSION, "ValueSet include with the version packed into the system"),
			Arguments.of(Lists.newArrayList(new ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL + "|" + SYSTEM_VERSION).setVersion(SYSTEM_VERSION_2)),
				SYSTEM_URL + "|" + SYSTEM_VERSION_2, "ValueSet include whose packed system and version element disagree"),
			Arguments.of(Lists.newArrayList(new ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL),
					new ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL_2)),
				null, "ValueSet includes with two systems and no code"),
			Arguments.of(Lists.newArrayList(new ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setConcept(conceptWithCode),
					new ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL_2)),
				SYSTEM_URL, "ValueSet includes with two systems and correct code"),
			Arguments.of(Lists.newArrayList(new ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setVersion(SYSTEM_VERSION).setConcept(conceptWithCode),
					new ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL_2).setVersion(SYSTEM_VERSION_2)),
				SYSTEM_URL + "|" + SYSTEM_VERSION, "ValueSet includes with two systems with versions and correct code"),
			Arguments.of(Lists.newArrayList(new ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setConcept(conceptNoCode),
					new ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL_2)),
				null, "ValueSet includes with two systems and different code"));
	}

	@ParameterizedTest
	@MethodSource("extractCodeSystemForCodeR4TestCases")
	public void extractCodeSystemForCodeR4_withDifferentValueSetIncludes_returnsCorrectResult(List<ValueSet.ConceptSetComponent> theValueSetComponents,
																							  String theExpectedCodeSystem, String theMessage) {
		// setup
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(VALUE_SET_URL);
		valueSet.setCompose(new ValueSet.ValueSetComposeComponent().setInclude(theValueSetComponents));

		// execute
		String result = ValidationSupportUtils.extractCodeSystemForCode(valueSet, CODE);

		// validate
		assertEquals(theExpectedCodeSystem, result, theMessage);
	}

	private static Stream<Arguments> extractCodeSystemForCodeR5TestCases() {
		List<org.hl7.fhir.r5.model.ValueSet.ConceptReferenceComponent> conceptWithCode = Lists.newArrayList(
			new org.hl7.fhir.r5.model.ValueSet.ConceptReferenceComponent().setCode(NOT_THE_CODE),
			new org.hl7.fhir.r5.model.ValueSet.ConceptReferenceComponent().setCode(CODE));

		List<org.hl7.fhir.r5.model.ValueSet.ConceptReferenceComponent> conceptNoCode = Lists.newArrayList(
			new org.hl7.fhir.r5.model.ValueSet.ConceptReferenceComponent().setCode(NOT_THE_CODE));

		return Stream.of(
			Arguments.of(Collections.emptyList(), null, "Empty ValueSet includes"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent(),
					new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent()),
				null, "ValueSet includes without system"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent()),
				null, "ValueSet include without system"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL)),
				SYSTEM_URL, "ValueSet include with one system and no code"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setConcept(conceptWithCode)),
				SYSTEM_URL, "ValueSet include with one system and code"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setVersion(SYSTEM_VERSION)),
				SYSTEM_URL + "|" + SYSTEM_VERSION, "ValueSet include with one versioned system and no code"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL),
					new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL_2)),
				null, "ValueSet includes with two systems and no code"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setConcept(conceptWithCode),
					new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL_2)),
				SYSTEM_URL, "ValueSet includes with two systems and correct code"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setVersion(SYSTEM_VERSION).setConcept(conceptWithCode),
					new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL_2).setVersion(SYSTEM_VERSION_2)),
				SYSTEM_URL + "|" + SYSTEM_VERSION, "ValueSet includes with two systems with versions and correct code"),
			Arguments.of(Lists.newArrayList(new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL).setConcept(conceptNoCode),
					new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(SYSTEM_URL_2)),
				null, "ValueSet includes with two systems and different code"));
	}

	@ParameterizedTest
	@MethodSource("extractCodeSystemForCodeR5TestCases")
	public void extractCodeSystemForCodeR5_withDifferentValueSetIncludes_returnsCorrectResult(List<org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent> theValueSetComponents,
																							  String theExpectedCodeSystem, String theMessage) {
		// setup
		org.hl7.fhir.r5.model.ValueSet valueSet = new org.hl7.fhir.r5.model.ValueSet();
		valueSet.setUrl(VALUE_SET_URL);
		valueSet.setCompose(new org.hl7.fhir.r5.model.ValueSet.ValueSetComposeComponent().setInclude(theValueSetComponents));

		// execute
		String result = ValidationSupportUtils.extractCodeSystemForCode(valueSet, CODE);

		// validate
		assertEquals(theExpectedCodeSystem, result, theMessage);
	}
	// Created by Claude Opus 5.5
	@Nested
	class IsNamedCodeSystemVersionReplaced {
		private static final String CS_URL = "http://example.org/cs";

		private final SingleVersionValidationSupport mySupport = new SingleVersionValidationSupport(CS_URL, "2");
		private final ValidationSupportContext myContext = new ValidationSupportContext(mySupport);

		@ParameterizedTest
		@EnumSource(
				value = MissingCodeSystemPolicy.class,
				names = {"USE_INSTALLED_VERSION", "ACCEPT_LISTED_CODES"})
		void isNamedCodeSystemVersionReplaced_versionNotSupported_returnsTrue(MissingCodeSystemPolicy thePolicy) {
			assertThat(ValidationSupportUtils.isNamedCodeSystemVersionReplaced(thePolicy, myContext, CS_URL, "1"))
					.isTrue();
		}

		@Test
		void isNamedCodeSystemVersionReplaced_reject_returnsFalse() {
			assertThat(ValidationSupportUtils.isNamedCodeSystemVersionReplaced(
							MissingCodeSystemPolicy.REJECT, myContext, CS_URL, "1"))
					.isFalse();
		}

		@Test
		void isNamedCodeSystemVersionReplaced_versionSupported_returnsFalse() {
			assertThat(ValidationSupportUtils.isNamedCodeSystemVersionReplaced(
							MissingCodeSystemPolicy.USE_INSTALLED_VERSION, myContext, CS_URL, "2"))
					.isFalse();
		}

		@ParameterizedTest
		@CsvSource(
				value = {"null, 1", "'', 1", "http://example.org/cs, null", "http://example.org/cs, ''"},
				nullValues = "null")
		void isNamedCodeSystemVersionReplaced_noSystemOrNoVersion_returnsFalse(String theSystem, String theVersion) {
			assertThat(ValidationSupportUtils.isNamedCodeSystemVersionReplaced(
							MissingCodeSystemPolicy.USE_INSTALLED_VERSION, myContext, theSystem, theVersion))
					.isFalse();
		}
	}

	// Created by Claude Opus 5.5
	@Nested
	class Messages {
		private final FhirContext myFhirContext = FhirContext.forR4Cached();

		@Test
		void createCodeSystemVersionReplacedMessage_usedVersion_namesBothVersions() {
			assertThat(ValidationSupportUtils.createCodeSystemVersionReplacedMessage(
							myFhirContext, "http://example.org/cs", "1", "2"))
					.isEqualTo(
							"CodeSystem 'http://example.org/cs' version '1' is not installed, so version '2' was used instead");
		}

		@ParameterizedTest
		@CsvSource(value = {"null", "''"}, nullValues = "null")
		void createCodeSystemVersionReplacedMessage_noUsedVersion_namesTheInstalledVersion(String theUsedVersion) {
			assertThat(ValidationSupportUtils.createCodeSystemVersionReplacedMessage(
							myFhirContext, "http://example.org/cs", "1", theUsedVersion))
					.isEqualTo(
							"CodeSystem 'http://example.org/cs' version '1' is not installed, so the installed version was used instead");
		}

		@Test
		void createListedCodesUncheckedMessage_codeSystem_namesIt() {
			assertThat(ValidationSupportUtils.createListedCodesUncheckedMessage(myFhirContext, "http://example.org/cs|1"))
					.isEqualTo("CodeSystem 'http://example.org/cs|1' is not available, so the codes the ValueSet lists"
							+ " from it were accepted without being checked");
		}
	}

	/**
	 * Supports one code system at one version only
	 */
	private static class SingleVersionValidationSupport implements IValidationSupport {
		private final String myCodeSystemUrl;
		private final String myVersion;

		private SingleVersionValidationSupport(String theCodeSystemUrl, String theVersion) {
			myCodeSystemUrl = theCodeSystemUrl;
			myVersion = theVersion;
		}

		@Override
		public FhirContext getFhirContext() {
			return null;
		}

		@Override
		public boolean isCodeSystemSupported(
				ValidationSupportContext theValidationSupportContext, String theSystem, String theVersion) {
			return myCodeSystemUrl.equals(theSystem) && myVersion.equals(theVersion);
		}
	}
}
