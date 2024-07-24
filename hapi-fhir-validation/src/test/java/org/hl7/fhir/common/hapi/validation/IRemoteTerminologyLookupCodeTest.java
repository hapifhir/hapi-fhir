package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.support.IValidationSupport.LookupCodeResult;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.junit.jupiter.api.Test;

/**
 * Additional tests specific for Remote Terminology $lookup operation.
 * Please see base interface for additional tests, implementation agnostic.
 */
public interface IRemoteTerminologyLookupCodeTest extends ILookupCodeTest {

	String MESSAGE_RESPONSE_NOT_FOUND = "Code {0} was not found";
	String MESSAGE_RESPONSE_EMPTY = "Code {0} got an empty response";

	@Override
	RemoteTerminologyServiceValidationSupport getService();

	@Test
	default void lookupCode_forCodeSystemWithCodeNotFound_returnsNotFound() {
		String baseUrl = getService().getBaseUrl();
		final String codeNotFound = "a";
		final String system = CODE_SYSTEM;
		final String codeAndSystem = system + "#" + codeNotFound;
		LookupCodeResult result = new LookupCodeResult()
				.setFound(false)
				.setSearchedForCode(codeNotFound)
				.setSearchedForSystem(system)
				.setErrorMessage("Unknown code \"" + codeAndSystem + "\". The Remote Terminology server " + baseUrl + " returned 404: HTTP 404 Not Found: Code a was not found.");
		getCodeSystemProvider().setLookupCodeResult(result);

		LookupCodeRequest request =  new LookupCodeRequest(system, codeNotFound, null, null);
		verifyLookupCodeResult(request, result);
	}

	@Test
	default void lookupCode_forCodeSystemWithInvalidRequest_returnsNotFound() {
		String baseUrl = getService().getBaseUrl();
		final String codeNotFound = "a";
		final String system = null;
		final String codeAndSystem = system + "#" + codeNotFound;
		LookupCodeResult result = new LookupCodeResult()
				.setFound(false)
				.setSearchedForCode(codeNotFound)
				.setSearchedForSystem(system)
				.setErrorMessage("Unknown code \"" + codeAndSystem + "\". The Remote Terminology server " + baseUrl + " returned 400: HTTP 400 Bad Request: Code a got an empty response.");
		getCodeSystemProvider().setLookupCodeResult(result);

		LookupCodeRequest request =  new LookupCodeRequest(system, codeNotFound, null, null);
		verifyLookupCodeResult(request, result);
	}
}
