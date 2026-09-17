package org.hl7.fhir.common.hapi.validation.support;

import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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

	private static Stream<Arguments> getVersionedCodeSystemTestCases() {
		return Stream.of(
			Arguments.of(SYSTEM_URL, SYSTEM_VERSION, SYSTEM_URL + "|" + SYSTEM_VERSION, "System and version are joined"),
			Arguments.of(SYSTEM_URL, null, SYSTEM_URL, "No version leaves the system alone"),
			Arguments.of(SYSTEM_URL, "", SYSTEM_URL, "Blank version leaves the system alone"),
			Arguments.of(SYSTEM_URL + "|" + SYSTEM_VERSION, SYSTEM_VERSION_2, SYSTEM_URL + "|" + SYSTEM_VERSION,
				"A system which already names a version does not get a second one"),
			Arguments.of(null, SYSTEM_VERSION, null, "Null system is returned as-is rather than throwing"),
			Arguments.of("", SYSTEM_VERSION, "", "Blank system is returned as-is"));
	}

	@ParameterizedTest
	@MethodSource("getVersionedCodeSystemTestCases")
	public void getVersionedCodeSystem_withDifferentSystemsAndVersions_returnsCorrectResult(String theCodeSystem,
																							String theVersion, String theExpectedCodeSystem, String theMessage) {
		// execute
		String result = ValidationSupportUtils.getVersionedCodeSystem(theCodeSystem, theVersion);

		// validate
		assertEquals(theExpectedCodeSystem, result, theMessage);
	}

	private static Stream<Arguments> getVersionedValueSetTestCases() {
		return Stream.of(
			Arguments.of(VALUE_SET_URL, SYSTEM_VERSION, VALUE_SET_URL + "|" + SYSTEM_VERSION, "URL and version are joined"),
			Arguments.of(VALUE_SET_URL, null, VALUE_SET_URL, "No version leaves the URL alone"),
			Arguments.of(VALUE_SET_URL, "", VALUE_SET_URL, "Blank version leaves the URL alone"),
			Arguments.of(VALUE_SET_URL + "|" + SYSTEM_VERSION, SYSTEM_VERSION_2, VALUE_SET_URL + "|" + SYSTEM_VERSION,
				"A URL which already names a version does not get a second one"),
			Arguments.of(null, SYSTEM_VERSION, null, "Null URL is returned as-is rather than throwing"));
	}

	@ParameterizedTest
	@MethodSource("getVersionedValueSetTestCases")
	public void getVersionedValueSet_withDifferentUrlsAndVersions_returnsCorrectResult(String theValueSetUrl,
																					   String theVersion, String theExpectedUrl, String theMessage) {
		// execute
		String result = ValidationSupportUtils.getVersionedValueSet(theValueSetUrl, theVersion);

		// validate
		assertEquals(theExpectedUrl, result, theMessage);
	}
}
