package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher.MatchResult.buildMatched;
import static ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher.MatchResult.buildUnmatched;
import static ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher.MatchResult.buildUnsupported;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@MockitoSettings
class FhirQueryRuleTesterTest  {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirQueryRuleTesterTest.class);
	FhirQueryRuleTester myTester = new FhirQueryRuleTester("Observation", "code=foo");

	IAuthRuleTester.RuleTestRequest myTestRequest;
	@Mock
	IBaseResource myResource;
	@Mock
	RequestDetails myRequestDetails;
	@Mock
	IRuleApplier myRuleApplier;
	@Mock
	IAuthorizationSearchParamMatcher mySearchParamMatcher;

	@BeforeEach
	void stubConfig() {
		when(myRuleApplier.getSearchParamMatcher()).thenReturn(mySearchParamMatcher);
	}

	void stubMatchResult(IAuthorizationSearchParamMatcher.MatchResult result) {
		when(mySearchParamMatcher.match("Observation?code=foo", myResource)).thenReturn(result);
	}

	private void stubLogForWarning() {
		when(myRuleApplier.getTroubleshootingLog()).thenReturn(ourLog);
	}


	@Test
	public void matchesFilter_true() {

		myTestRequest = new IAuthRuleTester.RuleTestRequest(PolicyEnum.ALLOW, RestOperationTypeEnum.SEARCH_TYPE,
			myRequestDetails, new IdDt("Observation/1"), myResource, myRuleApplier);
		stubMatchResult(buildMatched());

		boolean matches = myTester.matchesOutput(myTestRequest);

		assertTrue(matches);
	}


	@Test
	public void notMatchesFilter_false() {
		//when(myRuleApplier.getSearchParamMatcher()).thenReturn(mySearchParamMatcher);

		myTestRequest = new IAuthRuleTester.RuleTestRequest(PolicyEnum.ALLOW, RestOperationTypeEnum.SEARCH_TYPE,
			myRequestDetails, new IdDt("Observation/1"), myResource, myRuleApplier);
		stubMatchResult(buildUnmatched());

		boolean matches = myTester.matchesOutput(myTestRequest);

		assertFalse(matches);
	}

	@Test
	public void unsupportedAllow_false() {

		myTestRequest = new IAuthRuleTester.RuleTestRequest(PolicyEnum.ALLOW, RestOperationTypeEnum.SEARCH_TYPE,
			myRequestDetails, new IdDt("Observation/1"), myResource, myRuleApplier);
		stubMatchResult(buildUnsupported("a message"));
		stubLogForWarning();

		boolean matches = myTester.matchesOutput(myTestRequest);

		assertFalse(matches);
	}

	@Test
	public void unsupportedDeny_true() {

		myTestRequest = new IAuthRuleTester.RuleTestRequest(PolicyEnum.DENY, RestOperationTypeEnum.SEARCH_TYPE,
			myRequestDetails, new IdDt("Observation/1"), myResource, myRuleApplier);
		stubMatchResult(buildUnsupported("a message"));
		stubLogForWarning();

		boolean matches = myTester.matchesOutput(myTestRequest);

		assertTrue(matches);
	}


}
