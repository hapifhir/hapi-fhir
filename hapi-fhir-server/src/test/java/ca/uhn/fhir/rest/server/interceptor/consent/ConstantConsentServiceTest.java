package ca.uhn.fhir.rest.server.interceptor.consent;

import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static ca.uhn.fhir.rest.server.interceptor.consent.IConsentContextServices.NULL_IMPL;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstantConsentServiceTest {
	SystemRequestDetails mySrd = new SystemRequestDetails();

	@ParameterizedTest
	@EnumSource(ConsentOperationStatusEnum.class)
	void testStartOperation(ConsentOperationStatusEnum theStatus) {
	    // given
		var svc = ConstantConsentService.constantService(theStatus);

	    // when
		var outcome = svc.startOperation(mySrd,NULL_IMPL);

	    // then
	    assertEquals(outcome.getStatus(), theStatus);
	}


	/**
	 * There's no point calling canSee if we return PROCEED.
	 */
	@ParameterizedTest
	@EnumSource(ConsentOperationStatusEnum.class)
	void testShouldProcessCanSeeResource(ConsentOperationStatusEnum theStatus) {
		// given
		var svc = ConstantConsentService.constantService(theStatus);

		// when
		boolean outcome = svc.shouldProcessCanSeeResource(mySrd,NULL_IMPL);

		// then there's no point calling canSee if we return PROCEED.
		boolean isNotAbstain = theStatus != ConsentOperationStatusEnum.PROCEED;
		assertEquals(outcome, isNotAbstain);
	}

	@ParameterizedTest
	@EnumSource(ConsentOperationStatusEnum.class)
	void testCanSeeResource(ConsentOperationStatusEnum theStatus) {
		// given
		var svc = ConstantConsentService.constantService(theStatus);

		// when
		var outcome = svc.canSeeResource(mySrd, null, NULL_IMPL);

		// then
		assertEquals(outcome.getStatus(), theStatus);
	}


	@ParameterizedTest
	@EnumSource(ConsentOperationStatusEnum.class)
	void testWillSeeResource(ConsentOperationStatusEnum theStatus) {
		// given
		var svc = ConstantConsentService.constantService(theStatus);

		// when
		var outcome = svc.willSeeResource(mySrd, null, NULL_IMPL);

		// then
		assertEquals(outcome.getStatus(), theStatus);
	}
}
