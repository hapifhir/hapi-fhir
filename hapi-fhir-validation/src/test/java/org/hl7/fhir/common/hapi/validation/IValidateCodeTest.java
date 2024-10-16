package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.context.support.IValidationSupport.IssueSeverity.ERROR;
import static org.hl7.fhir.common.hapi.validation.IValidationProviders.CODE;
import static org.hl7.fhir.common.hapi.validation.IValidationProviders.CODE_SYSTEM;
import static org.hl7.fhir.common.hapi.validation.IValidationProviders.CODE_SYSTEM_VERSION;
import static org.hl7.fhir.common.hapi.validation.IValidationProviders.DISPLAY;
import static org.hl7.fhir.common.hapi.validation.IValidationProviders.ERROR_MESSAGE;
import static org.hl7.fhir.common.hapi.validation.IValidationProviders.VALUE_SET_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public interface IValidateCodeTest {

	IValidationProviders.IMyCodeSystemProvider getCodeSystemProvider();
	IValidationProviders.IMyValueSetProvider getValueSetProvider();
	IValidationSupport getService();
	IBaseParameters createParameters(Boolean theResult, String theDisplay, String theMessage, IBaseResource theIssuesResource);
	String getCodeSystemError();
	String getValueSetError();
	IBaseOperationOutcome getCodeSystemInvalidCodeOutcome();
	IBaseOperationOutcome getValueSetInvalidCodeOutcome();

	default void createCodeSystemReturnParameters(Boolean theResult, String theDisplay, String theMessage, IBaseResource theIssuesResource) {
		getCodeSystemProvider().setReturnParams(createParameters(theResult, theDisplay, theMessage, theIssuesResource));
	}

	default void createValueSetReturnParameters(Boolean theResult, String theDisplay, String theMessage, IBaseResource theIssuesResource) {
		getValueSetProvider().setReturnParams(createParameters(theResult, theDisplay, theMessage, theIssuesResource));
	}

	@Test
	default void validateCode_withCodeSystemBlankCode_ReturnsNull() {
		CodeValidationResult outcome = getService()
				.validateCode(null, null, CODE_SYSTEM, null, DISPLAY, null);
		assertNull(outcome);
	}

	@Test
	default void validateCode_withValueSetBlankCode_returnsNull() {
		CodeValidationResult outcome = getService().validateCode(null, null, CODE_SYSTEM, "", DISPLAY, VALUE_SET_URL);
		assertNull(outcome);
	}

	static Stream<Arguments> getRemoteTerminologyServerResponses() {
		return Stream.of(
				Arguments.of(new ResourceNotFoundException("System Not Present"), "404 Not Found: System Not Present",
						"Unknown code \"null#CODE\". The Remote Terminology server", null, null),
				Arguments.of(new InvalidRequestException("Invalid Request"), "400 Bad Request: Invalid Request",
						"Unknown code \"null#CODE\". The Remote Terminology server", null, null),
				Arguments.of(new ResourceNotFoundException("System Not Present"), "404 Not Found: System Not Present",
						"Unknown code \"NotFoundSystem#CODE\". The Remote Terminology server", "NotFoundSystem", null),
				Arguments.of(new InvalidRequestException("Invalid Request"), "400 Bad Request: Invalid Request",
						"Unknown code \"InvalidSystem#CODE\". The Remote Terminology server", "InvalidSystem", null),
				Arguments.of(new ResourceNotFoundException("System Not Present"), "404 Not Found: System Not Present",
						"Unknown code \"null#CODE\" for ValueSet with URL \"NotFoundValueSetUrl\". The Remote Terminology server",
						null, "NotFoundValueSetUrl"),
				Arguments.of(new InvalidRequestException("Invalid Request"), "400 Bad Request: Invalid Request",
						"Unknown code \"null#CODE\" for ValueSet with URL \"InvalidValueSetUrl\". The Remote Terminology server", null, "InvalidValueSetUrl"),
				Arguments.of(new ResourceNotFoundException("System Not Present"), "404 Not Found: System Not Present",
						"Unknown code \"NotFoundSystem#CODE\" for ValueSet with URL \"NotFoundValueSetUrl\". The Remote Terminology server",
						"NotFoundSystem", "NotFoundValueSetUrl"),
				Arguments.of(new InvalidRequestException("Invalid Request"), "400 Bad Request: Invalid Request",
						"Unknown code \"InvalidSystem#CODE\" for ValueSet with URL \"InvalidValueSetUrl\". The Remote Terminology server", "InvalidSystem", "InvalidValueSetUrl")
		);
	}

	@ParameterizedTest
	@MethodSource(value = "getRemoteTerminologyServerResponses")
	default void validateCode_codeSystemAndValueSetUrlAreIncorrect_returnsValidationResultWithError(Exception theException,
																																	String theServerMessage,
																																	String theValidationMessage,
																																	String theCodeSystem,
																																	String theValueSetUrl) {
		getCodeSystemProvider().setException(theException);
		getValueSetProvider().setException(theException);
		CodeValidationResult outcome = getService().validateCode(null, null, theCodeSystem, CODE, DISPLAY, theValueSetUrl);

		verifyErrorResultFromException(outcome, theValidationMessage, theServerMessage);
	}

	default void verifyErrorResultFromException(CodeValidationResult outcome, String... theMessages) {
		assertNotNull(outcome);
		assertEquals(ERROR, outcome.getSeverity());
		assertNotNull(outcome.getMessage());
		for (String message : theMessages) {
			assertTrue(outcome.getMessage().contains(message));
		}
		assertFalse(outcome.getCodeValidationIssues().isEmpty());
	}

	@Test
	default void validateCode_withMissingResult_returnsCorrectly() {
		createCodeSystemReturnParameters(null, null, null, null);
		IValidationSupport service = getService();
		try {
			service.validateCode(null, null, CODE_SYSTEM, CODE, null, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("HAPI-2560: Parameter `result` is missing from the $validate-code response.", e.getMessage());
		}
	}

	@Test
	default void validateCode_withValueSetSuccess_returnsCorrectly() {
		createValueSetReturnParameters(true, DISPLAY, null, null);

		CodeValidationResult outcome = getService().validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, VALUE_SET_URL);
		assertNotNull(outcome);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertNull(outcome.getSeverity());
		assertNull(outcome.getMessage());
		assertTrue(outcome.getCodeValidationIssues().isEmpty());

		assertEquals(CODE, getValueSetProvider().getCode());
		assertEquals(DISPLAY, getValueSetProvider().getDisplay());
		assertEquals(VALUE_SET_URL, getValueSetProvider().getValueSet());
	}

	@Test
	default void validateCode_withCodeSystemSuccess_returnsCorrectly() {
		createCodeSystemReturnParameters(true, DISPLAY, null, null);

		CodeValidationResult outcome = getService().validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, null);
		assertNotNull(outcome);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertNull(outcome.getSeverity());
		assertNull(outcome.getMessage());
		assertTrue(outcome.getCodeValidationIssues().isEmpty());

		assertEquals(CODE, getCodeSystemProvider().getCode());
	}

	@Test
	default void validateCode_withCodeSystemProvidingMinimalInputs_ReturnsSuccess() {
		createCodeSystemReturnParameters(true, null, null, null);

		CodeValidationResult outcome = getService()
				.validateCode(null, null, CODE_SYSTEM, CODE, null, null);
		assertNotNull(outcome);
		assertEquals(CODE_SYSTEM, outcome.getCodeSystemName());
		assertEquals(CODE_SYSTEM_VERSION, outcome.getCodeSystemVersion());
		assertEquals(CODE, outcome.getCode());
		assertNull(outcome.getDisplay());
		assertNull(outcome.getSeverity());
		assertNull(outcome.getMessage());
		assertTrue(outcome.getCodeValidationIssues().isEmpty());

		assertEquals(CODE, getCodeSystemProvider().getCode());
		assertEquals(CODE_SYSTEM, getCodeSystemProvider().getSystem());
	}

	@Test
	default void validateCode_withCodeSystemSuccessWithMessageValue_returnsCorrectly() {
		createCodeSystemReturnParameters(true, DISPLAY, null, null);

		CodeValidationResult outcome = getService()
				.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, null);
		assertNotNull(outcome);
		assertEquals(CODE_SYSTEM, outcome.getCodeSystemName());
		assertEquals(CODE_SYSTEM_VERSION, outcome.getCodeSystemVersion());
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertNull(outcome.getSeverity());
		assertNull(outcome.getMessage());
		assertTrue(outcome.getCodeValidationIssues().isEmpty());

		assertEquals(CODE, getCodeSystemProvider().getCode());
		assertEquals(DISPLAY, getCodeSystemProvider().getDisplay());
		assertEquals(CODE_SYSTEM, getCodeSystemProvider().getSystem());
	}

	@Test
	default void validateCode_withCodeSystemError_returnsCorrectly() {
		IBaseOperationOutcome invalidCodeOutcome = getCodeSystemInvalidCodeOutcome();
		createCodeSystemReturnParameters(false, null, ERROR_MESSAGE, invalidCodeOutcome);

		CodeValidationResult outcome = getService()
				.validateCode(null, null, CODE_SYSTEM, CODE, null, null);
		assertNotNull(outcome);
		assertEquals(CODE_SYSTEM, outcome.getCodeSystemName());
		assertEquals(CODE_SYSTEM_VERSION, outcome.getCodeSystemVersion());
		// assertEquals(CODE, outcome.getCode());
		assertEquals(ERROR, outcome.getSeverity());
		assertEquals(getCodeSystemError(), outcome.getMessage());
		assertFalse(outcome.getCodeValidationIssues().isEmpty());
		verifyIssues(invalidCodeOutcome, outcome);
	}

	@Test
	default void validateCode_withCodeSystemErrorAndIssues_returnsCorrectly() {
		createCodeSystemReturnParameters(false, null, ERROR_MESSAGE, null);

		CodeValidationResult outcome = getService()
				.validateCode(null, null, CODE_SYSTEM, CODE, null, null);

		String expectedError = getCodeSystemError();
		assertNotNull(outcome);
		assertEquals(CODE_SYSTEM, outcome.getCodeSystemName());
		assertEquals(CODE_SYSTEM_VERSION, outcome.getCodeSystemVersion());
		// assertEquals(CODE, outcome.getCode());
		assertNull(outcome.getDisplay());
		assertEquals(ERROR, outcome.getSeverity());
		assertEquals(expectedError, outcome.getMessage());
		assertFalse(outcome.getCodeValidationIssues().isEmpty());
		assertEquals(1, outcome.getCodeValidationIssues().size());
		assertEquals(expectedError, outcome.getCodeValidationIssues().get(0).getMessage());
		assertEquals(ERROR, outcome.getCodeValidationIssues().get(0).getSeverity());
	}

	@Test
	default void validateCode_withValueSetProvidingMinimalInputsSuccess_returnsCorrectly() {
		createValueSetReturnParameters(true, null, null, null);

		CodeValidationResult outcome = getService()
				.validateCode(null, null, CODE_SYSTEM, CODE, null, VALUE_SET_URL);
		assertNotNull(outcome);
		assertEquals(CODE_SYSTEM, outcome.getCodeSystemName());
		assertEquals(CODE_SYSTEM_VERSION, outcome.getCodeSystemVersion());
		assertEquals(CODE, outcome.getCode());
		assertNull(outcome.getDisplay());
		assertNull(outcome.getSeverity());
		assertNull(outcome.getMessage());
		assertTrue(outcome.getCodeValidationIssues().isEmpty());

		assertEquals(CODE, getValueSetProvider().getCode());
		assertEquals(VALUE_SET_URL, getValueSetProvider().getValueSet());
	}

	@Test
	default void validateCode_withValueSetSuccessWithMessage_returnsCorrectly() {
		createValueSetReturnParameters(true, DISPLAY, null, null);

		CodeValidationResult outcome = getService()
				.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, VALUE_SET_URL);
		assertNotNull(outcome);
		assertEquals(CODE_SYSTEM, outcome.getCodeSystemName());
		assertEquals(CODE_SYSTEM_VERSION, outcome.getCodeSystemVersion());
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertNull(outcome.getSeverity());
		assertNull(outcome.getMessage());
		assertTrue(outcome.getCodeValidationIssues().isEmpty());

		assertEquals(CODE, getValueSetProvider().getCode());
		assertEquals(DISPLAY, getValueSetProvider().getDisplay());
		assertEquals(VALUE_SET_URL, getValueSetProvider().getValueSet());
	}

	@Test
	default void validateCode_withValueSetError_returnsCorrectly() {
		createValueSetReturnParameters(false, DISPLAY, ERROR_MESSAGE, null);

		CodeValidationResult outcome = getService()
				.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, VALUE_SET_URL);

		String expectedError = getValueSetError();
		assertNotNull(outcome);
		assertEquals(CODE_SYSTEM, outcome.getCodeSystemName());
		assertEquals(CODE_SYSTEM_VERSION, outcome.getCodeSystemVersion());
		// assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertEquals(ERROR, outcome.getSeverity());
		assertEquals(expectedError, outcome.getMessage());
		assertEquals(1, outcome.getCodeValidationIssues().size());
		assertEquals(expectedError, outcome.getCodeValidationIssues().get(0).getMessage());
		assertEquals(ERROR, outcome.getCodeValidationIssues().get(0).getSeverity());

		assertEquals(CODE, getValueSetProvider().getCode());
		assertEquals(DISPLAY, getValueSetProvider().getDisplay());
		assertEquals(VALUE_SET_URL, getValueSetProvider().getValueSet());
	}

	@Test
	default void validateCode_withValueSetErrorWithIssues_returnsCorrectly() {
		IBaseOperationOutcome invalidCodeOutcome = getValueSetInvalidCodeOutcome();
		createValueSetReturnParameters(false, DISPLAY, ERROR_MESSAGE, invalidCodeOutcome);

		CodeValidationResult outcome = getService()
				.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, VALUE_SET_URL);
		assertNotNull(outcome);
		assertEquals(CODE_SYSTEM, outcome.getCodeSystemName());
		assertEquals(CODE_SYSTEM_VERSION, outcome.getCodeSystemVersion());
		// assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertEquals(ERROR, outcome.getSeverity());
		assertEquals(getValueSetError(), outcome.getMessage());
		assertFalse(outcome.getCodeValidationIssues().isEmpty());
		verifyIssues(invalidCodeOutcome, outcome);

		assertEquals(CODE, getValueSetProvider().getCode());
		assertEquals(DISPLAY, getValueSetProvider().getDisplay());
		assertEquals(VALUE_SET_URL, getValueSetProvider().getValueSet());
	}

	default void verifyIssues(IBaseOperationOutcome theOperationOutcome, CodeValidationResult theResult) {
		List<IValidationSupport.CodeValidationIssue> issues = getCodeValidationIssues(theOperationOutcome);
		assertEquals(issues.size(), theResult.getCodeValidationIssues().size());
		for (int i = 0; i < issues.size(); i++) {
			IValidationSupport.CodeValidationIssue expectedIssue = issues.get(i);
			IValidationSupport.CodeValidationIssue actualIssue = theResult.getCodeValidationIssues().get(i);
			assertEquals(expectedIssue.getCode(), actualIssue.getCode());
			assertEquals(expectedIssue.getSeverity(), actualIssue.getSeverity());
			assertEquals(expectedIssue.getCoding(), actualIssue.getCoding());
			assertEquals(expectedIssue.getMessage(), actualIssue.getMessage());
		}
	}

	List<IValidationSupport.CodeValidationIssue> getCodeValidationIssues(IBaseOperationOutcome theOperationOutcome);
}
