package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.LookupCodeResult;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport.createConceptProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.fail;


public interface ILookupCodeTest {
	String DISPLAY = "DISPLAY";
	String LANGUAGE = "en";
	String CODE_SYSTEM = "CODE_SYS";
	String CODE_SYSTEM_VERSION = "CODE_SYS_VERSION";
	String CODE_SYSTEM_NAME = "Code System";
	String CODE = "CODE";

	interface IValidationTest {

		RemoteTerminologyServiceValidationSupport getService();
		IResourceProvider getCodeSystemProvider();
	}

	@Nested
	interface ILookupCodeUnsupportedPropertyTypeTest extends IValidationTest {

		String getInvalidValueErrorCode();

		String getInvalidValueErrorCodeForConvert();

		@Override
		IMySimpleCodeSystemProvider getCodeSystemProvider();

		@Test
		default void testLookupCode_forCodeSystemWithPropertyInvalidValue_throwsException() {
			// test and verify
			try {
				getService().lookupCode(null, new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, null));
				fail("");			} catch (InternalErrorException e) {
				assertTrue(e.getMessage().contains(getInvalidValueErrorCode() + ": Property type " + getCodeSystemProvider().getPropertyValue().fhirType() + " is not supported"));
			}
		}

		@Test
		default void testCreateConceptProperty_forCodeSystemWithPropertyInvalidValue_throwsException() {
			// test and verify
			try {
				RemoteTerminologyServiceValidationSupport.createConceptProperty("property", getCodeSystemProvider().getPropertyValue());
				fail("");			} catch (InternalErrorException e) {
				assertTrue(e.getMessage().contains(getInvalidValueErrorCodeForConvert() + ": Property type " + getCodeSystemProvider().getPropertyValue().fhirType() + " is not supported"));
			}
		}
	}

	@Nested
	interface ILookupCodeSupportedPropertyTest extends IValidationTest {
		IMyCodeSystemProvider getCodeSystemProvider();

		 Stream<Arguments> getEmptyPropertyValues();

		Stream<Arguments> getPropertyValues();

		Stream<Arguments> getDesignations();

		void verifyProperty(IValidationSupport.BaseConceptProperty theConceptProperty, String theExpectedPropertName, IBaseDatatype theExpectedValue);

		@Test
		default void testLookupCode_forCodeSystemWithBlankCode_throwsException() {
			try {
				getService().lookupCode(null, new LookupCodeRequest(CODE_SYSTEM, ""));
				fail("");			} catch (IllegalArgumentException e) {
				assertEquals("theCode must be provided", e.getMessage());
			}
		}

		@Test
		default void testLookupCode_forCodeSystemWithPropertyInvalidType_throwsException() {
			LookupCodeResult result = new LookupCodeResult();
			result.getProperties().add(new IValidationSupport.BaseConceptProperty("someProperty") {
				public String getType() {
					return "someUnsupportedType";
				}
			});
			getCodeSystemProvider().setLookupCodeResult(result);

			try {
				getService().lookupCode(null, new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, null));
				fail("");			} catch (InternalErrorException e) {
				assertTrue(e.getMessage().contains("HAPI-1739: Don't know how to handle "));
			}
		}

		@ParameterizedTest
		@MethodSource(value = "getEmptyPropertyValues")
		default void testLookupCode_forCodeSystemWithPropertyEmptyValue_returnsCorrectParameters(IBaseDatatype thePropertyValue) {
			// setup
			final String propertyName = "someProperty";
			IValidationSupport.BaseConceptProperty property = createConceptProperty(propertyName, thePropertyValue);
			LookupCodeResult result = new LookupCodeResult();
			result.getProperties().add(property);
			getCodeSystemProvider().setLookupCodeResult(result);

			// test
			LookupCodeResult outcome = getService().lookupCode(null, new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, List.of(propertyName)));

			// verify
			assertNotNull(outcome);
			Optional<IValidationSupport.BaseConceptProperty> propertyOptional = outcome.getProperties().stream().findFirst().filter(a -> propertyName.equals(a.getPropertyName()));
			assertFalse(propertyOptional.isPresent());
		}

		@Test
		default void testLookupCode_forCodeSystemWithParameters_returnsCorrectParameters() {
			// setup
			LookupCodeResult result = new LookupCodeResult().setFound(true).setSearchedForCode(CODE).setSearchedForSystem(CODE_SYSTEM);
			result.setCodeIsAbstract(false);
			result.setCodeSystemVersion(CODE_SYSTEM_VERSION);
			result.setCodeSystemDisplayName(CODE_SYSTEM_NAME);
			result.setCodeDisplay(DISPLAY);
			getCodeSystemProvider().setLookupCodeResult(result);

			// test
			LookupCodeResult outcome = getService().lookupCode(null, new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, null));

			// verify
			assertNotNull(outcome);
			assertEquals(CODE, getCodeSystemProvider().getCode());
			assertEquals(CODE_SYSTEM, getCodeSystemProvider().getSystem());
			assertEquals(result.getCodeSystemDisplayName(), outcome.getCodeSystemDisplayName());
			assertEquals(result.getCodeDisplay(), outcome.getCodeDisplay());
			assertEquals(result.getCodeSystemVersion(), outcome.getCodeSystemVersion());
			assertEquals(result.isCodeIsAbstract(), outcome.isCodeIsAbstract());
		}

		@ParameterizedTest
		@MethodSource(value = "getPropertyValues")
		default void testLookupCode_forCodeSystemWithProperty_returnsCorrectProperty(IBaseDatatype thePropertyValue) {
			// setup
			final String propertyName = "someProperty";
			LookupCodeResult result = new LookupCodeResult()
				.setFound(true).setSearchedForCode(CODE).setSearchedForSystem(CODE_SYSTEM);
			result.setCodeIsAbstract(false);
			result.setCodeSystemVersion(CODE_SYSTEM_VERSION);
			result.setCodeSystemDisplayName(CODE_SYSTEM_NAME);
			result.setCodeDisplay(DISPLAY);
			IValidationSupport.BaseConceptProperty property = createConceptProperty(propertyName, thePropertyValue);
			result.getProperties().add(property);
			getCodeSystemProvider().setLookupCodeResult(result);

			// test
			LookupCodeResult outcome = getService().lookupCode(null, new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, List.of(propertyName)));

			// verify
			assertNotNull(outcome);
			assertEquals(CODE, getCodeSystemProvider().getCode());
			assertEquals(CODE_SYSTEM, getCodeSystemProvider().getSystem());
			assertEquals(result.getCodeSystemDisplayName(), outcome.getCodeSystemDisplayName());
			assertEquals(result.getCodeDisplay(), outcome.getCodeDisplay());
			assertEquals(result.getCodeSystemVersion(), outcome.getCodeSystemVersion());
			assertEquals(result.isCodeIsAbstract(), outcome.isCodeIsAbstract());

			Optional<IValidationSupport.BaseConceptProperty> propertyOptional = outcome.getProperties().stream().findFirst().filter(a -> propertyName.equals(a.getPropertyName()));
			assertTrue(propertyOptional.isPresent());
			IValidationSupport.BaseConceptProperty outputProperty = propertyOptional.get();

			verifyProperty(outputProperty, propertyName, thePropertyValue);
		}

		@ParameterizedTest
		@MethodSource(value = "getDesignations")
		default void testLookupCode_withCodeSystemWithDesignation_returnsCorrectDesignation(final IValidationSupport.ConceptDesignation theConceptDesignation) {
			// setup
			LookupCodeResult result = new LookupCodeResult();
			result.getDesignations().add(theConceptDesignation);
			getCodeSystemProvider().setLookupCodeResult(result);

			// test
			LookupCodeResult outcome = getService().lookupCode(null, new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, null));

			// verify
			assertNotNull(outcome);

			Collection<IValidationSupport.ConceptDesignation> designations = outcome.getDesignations();
			assertEquals(1, designations.size());

			IValidationSupport.ConceptDesignation designation = designations.iterator().next();
			assertEquals(theConceptDesignation.getValue(), designation.getValue());
			assertEquals(theConceptDesignation.getLanguage(), designation.getLanguage());
			assertEquals(theConceptDesignation.getUseCode(), designation.getUseCode());
			assertEquals(theConceptDesignation.getUseSystem(), designation.getUseSystem());
			assertEquals(theConceptDesignation.getUseDisplay(), designation.getUseDisplay());
		}

		@Test
		default void testLookupCode_withCodeSystemWithMultipleDesignations_returnsCorrectDesignations() {
			// setup
			final String code1 = "code1";
			final String code2 = "code2";

			IValidationSupport.ConceptDesignation designation1 = new IValidationSupport.ConceptDesignation().setUseCode(code1).setUseSystem("system1").setValue("value1").setLanguage("en");
			IValidationSupport.ConceptDesignation designation2 = new IValidationSupport.ConceptDesignation().setUseCode(code2).setUseSystem("system2").setValue("value2").setLanguage("es");
			LookupCodeResult result = new LookupCodeResult();
			result.getDesignations().add(designation1);
			result.getDesignations().add(designation2);
			getCodeSystemProvider().setLookupCodeResult(result);

			// test
			LookupCodeResult outcome = getService().lookupCode(null, new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, null));

			// verify
			assertNotNull(outcome);

			Collection<IValidationSupport.ConceptDesignation> designations = outcome.getDesignations();
			assertEquals(2, designations.size());

			for (IValidationSupport.ConceptDesignation designation : designations) {
				IValidationSupport.ConceptDesignation expectedDesignation = code1.equals(designation.getUseCode()) ? designation1 : designation2;
				assertEquals(expectedDesignation.getValue(), designation.getValue());
				assertEquals(expectedDesignation.getLanguage(), designation.getLanguage());
				assertEquals(expectedDesignation.getUseCode(), designation.getUseCode());
				assertEquals(expectedDesignation.getUseSystem(), designation.getUseSystem());
				assertEquals(expectedDesignation.getUseDisplay(), designation.getUseDisplay());
			}
		}
	}


	interface IMyCodeSystemProvider extends IResourceProvider {
		String getCode();
		String getSystem();

		void setLookupCodeResult(LookupCodeResult theLookupCodeResult);
	}

	interface IMySimpleCodeSystemProvider extends IResourceProvider {
		IBaseDatatype getPropertyValue();
	}
}
