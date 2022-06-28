package ca.uhn.fhir.rest.server.interceptor.consent;

import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IRuleApplier;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Very limited test since we can't reference real resources.
 */
@MockitoSettings(strictness = Strictness.LENIENT)
class RuleFilteringConsentServiceTest {

	@Mock
	IRuleApplier myRuleApplier;
	RuleFilteringConsentService myRuleFilteringConsentService;

	@BeforeEach
	void setUp() {
		myRuleFilteringConsentService = new RuleFilteringConsentService(myRuleApplier);
	}

	@Test
	void allowPasses() {
		when(myRuleApplier.applyRulesAndReturnDecision(any(), any(), any(), any(), any(), any()))
			.thenReturn(new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, null));

		ConsentOutcome consentDecision = myRuleFilteringConsentService.canSeeResource(null, null, null);

		assertThat(consentDecision.getStatus(), equalTo(ConsentOperationStatusEnum.PROCEED));

	}

	@Test
	void denyIsRejected() {
		when(myRuleApplier.applyRulesAndReturnDecision(any(), any(), any(), any(), any(), any()))
			.thenReturn(new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, null));

		ConsentOutcome consentDecision = myRuleFilteringConsentService.canSeeResource(null, null, null);

		assertThat(consentDecision.getStatus(), equalTo(ConsentOperationStatusEnum.REJECT));
	}
}
