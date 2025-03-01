package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.BaseConceptProperty;
import ca.uhn.fhir.context.support.IValidationSupport.CodingConceptProperty;
import ca.uhn.fhir.context.support.IValidationSupport.ConceptDesignation;
import ca.uhn.fhir.context.support.IValidationSupport.GroupConceptProperty;
import ca.uhn.fhir.context.support.IValidationSupport.LookupCodeResult;
import ca.uhn.fhir.context.support.IValidationSupport.StringConceptProperty;
import ca.uhn.fhir.context.support.IValidationSupport.BooleanConceptProperty;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.utilities.validation.IValidationProviders;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.context.support.IValidationSupport.TYPE_BOOLEAN;
import static ca.uhn.fhir.context.support.IValidationSupport.TYPE_CODING;
import static ca.uhn.fhir.context.support.IValidationSupport.TYPE_GROUP;
import static ca.uhn.fhir.context.support.IValidationSupport.TYPE_STRING;
import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.CODE;
import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.CODE_SYSTEM;
import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.CODE_SYSTEM_NAME;
import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.CODE_SYSTEM_VERSION;
import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.DISPLAY;
import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.LANGUAGE;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport.createConceptProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Contains basic test setup for the $lookup operation against a {@link org.hl7.fhir.dstu3.model.CodeSystem}.
 * The provider for CodeSystem and the validation service needs to be configured by the implementing class.
 * This test interface contains the following:
 * (1) basic tests which are version independent, without any parameters, declared as default with @Test annotation
 * e.g. lookupCode_forCodeSystemWithBlankCode_throwsException
 * (2) test template methods for running version dependent tests with parameters, declared as default, without annotations
 * e.g. @see #verifyLookupCodeResult
 * (3) methods which help to assert part of the output (designation, property), declared as private
 * e.g. assertEqualConceptProperty
 */
public interface ILookupCodeTest {
	IValidationSupport getService();
	IValidationProviders.IMyLookupCodeProvider getLookupCodeProvider();

	@Test
	default void lookupCode_forCodeSystemWithBlankCode_throwsException() {
		IValidationSupport service = getService();
		LookupCodeRequest request = new LookupCodeRequest(CODE_SYSTEM, "");
		try {
			service.lookupCode(null, request);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("theCode must be provided", e.getMessage());
		}
	}

	@Test
	default void lookupCode_forCodeSystemWithPropertyInvalidType_throwsException() {
		// test
		LookupCodeResult result = new LookupCodeResult();
		result.setFound(true);
		result.getProperties().add(new BaseConceptProperty("someProperty") {
			public String getType() {
				return "someUnsupportedType";
			}
		});
		getLookupCodeProvider().setLookupCodeResult(result);

		IValidationSupport service = getService();
		LookupCodeRequest request = new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, null);

		// test and verify
		try {
			service.lookupCode(null, request);
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage()).contains("HAPI-1739: Don't know how to handle ");
		}
	}

	@Test
	default void lookupCode_forCodeSystem_returnsCorrectResult() {
		LookupCodeResult result = new LookupCodeResult().setFound(true).setSearchedForCode(CODE).setSearchedForSystem(CODE_SYSTEM);
		result.setCodeIsAbstract(false);
		result.setCodeSystemVersion(CODE_SYSTEM_VERSION);
		result.setCodeSystemDisplayName(CODE_SYSTEM_NAME);
		result.setCodeDisplay(DISPLAY);
		getLookupCodeProvider().setLookupCodeResult(result);

		// test and verify
		LookupCodeRequest request =  new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, null);
		verifyLookupCodeResult(request, result);
	}

	@Test
	default void lookupCode_withCodeSystemWithMultipleDesignations_returnsCorrectDesignations() {
		// setup
		final String code1 = "code1";
		final String code2 = "code2";

		ConceptDesignation designation1 = new ConceptDesignation().setUseCode(code1).setUseSystem("system1").setValue("value1").setLanguage("en");
		ConceptDesignation designation2 = new ConceptDesignation().setUseCode(code2).setUseSystem("system2").setValue("value2").setLanguage("es");
		LookupCodeResult result = new LookupCodeResult();
		result.setFound(true);
		result.getDesignations().add(designation1);
		result.getDesignations().add(designation2);
		getLookupCodeProvider().setLookupCodeResult(result);

		// test and verify
		LookupCodeRequest request = new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, null);
		verifyLookupCodeResult(request, result);
	}

	default void verifyLookupWithEmptyPropertyValue(IBaseDatatype thePropertyValue) {
		// setup
		final String propertyName = "someProperty";
		BaseConceptProperty property = createConceptProperty(propertyName, thePropertyValue);
		LookupCodeResult result = new LookupCodeResult();
		result.getProperties().add(property);
		getLookupCodeProvider().setLookupCodeResult(result);

		// test
		LookupCodeRequest request = new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, List.of(propertyName));
		LookupCodeResult outcome = getService().lookupCode(null, request);

		// verify
		assertNotNull(outcome);
		Optional<BaseConceptProperty> propertyOptional = outcome.getProperties().stream().findFirst().filter(a -> propertyName.equals(a.getPropertyName()));
		assertFalse(propertyOptional.isPresent());
	}

	default void verifyLookupWithProperty(List<IBaseDatatype> thePropertyValues, List<Integer> thePropertyIndexesToFilter) {
		// setup
		final String propertyName = "someProperty";
		LookupCodeResult result = new LookupCodeResult().setFound(true).setSearchedForCode(CODE).setSearchedForSystem(CODE_SYSTEM);
		result.setCodeIsAbstract(false);
		result.setCodeSystemVersion(CODE_SYSTEM_VERSION);
		result.setCodeSystemDisplayName(CODE_SYSTEM_NAME);
		result.setCodeDisplay(DISPLAY);
		List<String> propertyNamesToFilter = new ArrayList<>();
		for (int i = 0; i < thePropertyValues.size(); i++) {
			String currentPropertyName = propertyName + i;
			result.getProperties().add(createConceptProperty(currentPropertyName, thePropertyValues.get(i)));
			if (thePropertyIndexesToFilter.contains(i)) {
				propertyNamesToFilter.add(currentPropertyName);
			}
		}
		getLookupCodeProvider().setLookupCodeResult(result);

		// test
		LookupCodeRequest request = new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, propertyNamesToFilter);

		// verify
		result.getProperties().removeIf(p -> !propertyNamesToFilter.contains(p.getPropertyName()));
		verifyLookupCodeResult(request, result);
	}

	default void verifyLookupWithSubProperties(List<IBaseDatatype> thePropertyValues) {
		// setup
		final String groupName = "group";
		LookupCodeResult result = new LookupCodeResult().setFound(true).setSearchedForCode(CODE).setSearchedForSystem(CODE_SYSTEM);
		result.setCodeIsAbstract(false);
		result.setCodeSystemVersion(CODE_SYSTEM_VERSION);
		result.setCodeSystemDisplayName(CODE_SYSTEM_NAME);
		result.setCodeDisplay(DISPLAY);
		final String subPropertyName = "someSubProperty";
		GroupConceptProperty group = new GroupConceptProperty(groupName);
		for (int i = 0; i < thePropertyValues.size(); i++) {
			group.addSubProperty(createConceptProperty(subPropertyName + i, thePropertyValues.get(i)));
		}
		result.getProperties().add(group);
		getLookupCodeProvider().setLookupCodeResult(result);

		// test and verify
		LookupCodeRequest request = new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, List.of(groupName));
		verifyLookupCodeResult(request, result);
	}

	default void verifyLookupCodeResult(LookupCodeRequest theRequest, LookupCodeResult theExpectedResult) {
		// test
		LookupCodeResult outcome = getService().lookupCode(null, theRequest);
		assertNotNull(outcome);

		// verify
		assertNotNull(outcome);
		assertEquals(theExpectedResult.isFound(), outcome.isFound());
		assertEquals(theExpectedResult.getErrorMessage(), outcome.getErrorMessage());
		assertEquals(theExpectedResult.getCodeSystemDisplayName(), outcome.getCodeSystemDisplayName());
		assertEquals(theExpectedResult.getCodeDisplay(), outcome.getCodeDisplay());
		assertEquals(theExpectedResult.getCodeSystemVersion(), outcome.getCodeSystemVersion());
		assertEquals(theExpectedResult.isCodeIsAbstract(), outcome.isCodeIsAbstract());

		assertEquals(theExpectedResult.getProperties().size(), outcome.getProperties().size());
		range(0, outcome.getProperties().size()).forEach(i ->
			assertEqualConceptProperty(theExpectedResult.getProperties().get(i), outcome.getProperties().get(i)));

		assertEquals(theExpectedResult.getDesignations().size(), outcome.getDesignations().size());
		range(0, outcome.getDesignations().size()).forEach(i -> assertEqualConceptDesignation(theExpectedResult.getDesignations().get(i), outcome.getDesignations().get(i)));
	}

	default void verifyLookupWithConceptDesignation(final ConceptDesignation theConceptDesignation) {
		// setup
		LookupCodeResult result = new LookupCodeResult();
		result.setFound(true);
		result.getDesignations().add(theConceptDesignation);
		getLookupCodeProvider().setLookupCodeResult(result);

		// test and verify
		LookupCodeRequest request = new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, null);
		verifyLookupCodeResult(request, result);
	}

	private void assertEqualConceptProperty(BaseConceptProperty theProperty, BaseConceptProperty theExpectedProperty) {
		assertEquals(theExpectedProperty.getPropertyName(), theProperty.getPropertyName());
		assertEquals(theExpectedProperty.getType(), theProperty.getType());
		switch (theProperty.getType()) {
			case TYPE_STRING -> {
				StringConceptProperty expected = (StringConceptProperty) theExpectedProperty;
				StringConceptProperty actual = (StringConceptProperty) theProperty;
				assertEquals(expected.getValue(), actual.getValue());
			}
			case TYPE_BOOLEAN -> {
				BooleanConceptProperty expected = (BooleanConceptProperty) theExpectedProperty;
				IValidationSupport.BooleanConceptProperty actual = (BooleanConceptProperty) theProperty;
				assertEquals(expected.getValue(), actual.getValue());
			}
			case TYPE_CODING -> {
				CodingConceptProperty expected = (CodingConceptProperty) theExpectedProperty;
				CodingConceptProperty actual = (CodingConceptProperty) theProperty;
				assertEquals(expected.getCode(), actual.getCode());
				assertEquals(expected.getCodeSystem(), actual.getCodeSystem());
				assertEquals(expected.getDisplay(), actual.getDisplay());
			}
			case TYPE_GROUP -> {
				GroupConceptProperty expected = (GroupConceptProperty) theExpectedProperty;
				GroupConceptProperty actual = (GroupConceptProperty) theProperty;
				assertEquals(expected.getSubProperties().size(), actual.getSubProperties().size());
				range(0, actual.getSubProperties().size()).forEach(i -> assertEqualConceptProperty(expected.getSubProperties().get(i), actual.getSubProperties().get(i)));
			}
			default -> fail();
		}
	}

	private void assertEqualConceptDesignation(final ConceptDesignation theActualDesignation, final ConceptDesignation theExpectedDesignation) {
		assertEquals(theActualDesignation.getValue(), theExpectedDesignation.getValue());
		assertEquals(theActualDesignation.getLanguage(), theExpectedDesignation.getLanguage());
		assertEquals(theActualDesignation.getUseCode(), theExpectedDesignation.getUseCode());
		assertEquals(theActualDesignation.getUseSystem(), theExpectedDesignation.getUseSystem());
		assertEquals(theActualDesignation.getUseDisplay(), theExpectedDesignation.getUseDisplay());
	}
}
