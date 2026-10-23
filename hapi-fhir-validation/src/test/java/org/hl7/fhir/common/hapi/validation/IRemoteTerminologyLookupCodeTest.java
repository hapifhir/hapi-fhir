package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.support.IValidationSupport.BaseConceptProperty;
import ca.uhn.fhir.context.support.IValidationSupport.LookupCodeResult;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.test.utilities.validation.IValidationProviders;
import ca.uhn.fhir.test.utilities.validation.IValidationProviders.FailEveryConnectionInterceptor;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.CODE;
import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.CODE_SYSTEM;
import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.LANGUAGE;
import static ca.uhn.fhir.test.utilities.validation.IValidationProviders.REMOTE_FAILURE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Additional tests specific for Remote Terminology $lookup operation.
 * Please see base interface for additional tests, implementation agnostic.
 */
public interface IRemoteTerminologyLookupCodeTest extends ILookupCodeTest {

	String MESSAGE_RESPONSE_NOT_FOUND = "Code {0} was not found";
	String MESSAGE_RESPONSE_INVALID = "Code {0} lookup is missing a system";

	@Override
	RemoteTerminologyServiceValidationSupport getService();

	@Test
	default void lookupCode_forCodeSystemWithCodeNotFound_returnsNotFound() {
		String baseUrl = getService().getBaseUrl();
		final String codeNotFound = "a";
		final String system = IValidationProviders.CODE_SYSTEM;
		final String codeAndSystem = system + "#" + codeNotFound;
		final String exceptionMessage = MessageFormat.format(MESSAGE_RESPONSE_NOT_FOUND, codeNotFound);
		LookupCodeResult result = new LookupCodeResult()
				.setFound(false)
				.setSearchedForCode(codeNotFound)
				.setSearchedForSystem(system)
				.setErrorMessage("Unknown code \"" + codeAndSystem + "\". The Remote Terminology server " + baseUrl + " returned HTTP 404 Not Found: " + exceptionMessage);
		getLookupCodeProvider().setLookupCodeResult(result);

		LookupCodeRequest request =  new LookupCodeRequest(system, codeNotFound, null, null);
		verifyLookupCodeResult(request, result);
	}

	@Test
	default void lookupCode_forCodeSystemWithInvalidRequest_returnsNotFound() {
		String baseUrl = getService().getBaseUrl();
		final String codeNotFound = "a";
		final String system = null;
		final String codeAndSystem = system + "#" + codeNotFound;
		final String exceptionMessage = MessageFormat.format(MESSAGE_RESPONSE_INVALID, codeNotFound);
		LookupCodeResult result = new LookupCodeResult()
				.setFound(false)
				.setSearchedForCode(codeNotFound)
				.setSearchedForSystem(system)
				.setErrorMessage("Unknown code \"" + codeAndSystem + "\". The Remote Terminology server " + baseUrl + " returned HTTP 400 Bad Request: " + exceptionMessage);
		getLookupCodeProvider().setLookupCodeResult(result);

		LookupCodeRequest request =  new LookupCodeRequest(system, codeNotFound, null, null);
		verifyLookupCodeResult(request, result);
	}

	/**
	 * A remote server that fails with HTTP 500, here because it cannot encode the property it was asked for, has
	 * said nothing about the code. The lookup reports the code as not found, with the remote server and its error
	 * in the message, rather than throwing the failure into the caller's request.
	 */
	@Test
	default void lookupCode_remoteServerFails_returnsNotFoundNamingTheFailure() {
		// setup
		LookupCodeResult result = new LookupCodeResult();
		result.setFound(true);
		result.getProperties().add(new BaseConceptProperty("someProperty") {
			public String getType() {
				return "someUnsupportedType";
			}
		});
		getLookupCodeProvider().setLookupCodeResult(result);
		LookupCodeRequest request = new LookupCodeRequest(CODE_SYSTEM, CODE, LANGUAGE, null);

		// execute
		LookupCodeResult outcome = getService().lookupCode(null, request);

		// verify
		assertThat(outcome.isFound()).isFalse();
		assertThat(outcome.getErrorMessage())
				.contains(getService().getBaseUrl(), "HAPI-1739: Don't know how to handle ");
	}

	// Created by Claude Opus 5.5
	@Test
	default void lookupCode_remoteServerUnreachable_returnsNotFoundNamingTheFailure() {
		// setup
		getService().addClientInterceptor(new FailEveryConnectionInterceptor());
		LookupCodeRequest request = new LookupCodeRequest(CODE_SYSTEM, CODE);

		// execute
		LookupCodeResult outcome = getService().lookupCode(null, request);

		// verify
		assertThat(outcome.isFound()).isFalse();
		assertThat(outcome.getErrorMessage()).contains(getService().getBaseUrl(), REMOTE_FAILURE);
	}
}
