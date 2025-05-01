package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.support.IValidationSupport.LookupCodeResult;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.test.utilities.validation.IValidationProviders;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

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


}
