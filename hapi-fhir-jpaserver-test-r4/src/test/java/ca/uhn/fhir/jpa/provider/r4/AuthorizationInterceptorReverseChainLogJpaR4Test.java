package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.matcher.AuthorizationSearchParamMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationFlagsEnum.ENFORCE_REVERSE_CHAINED_AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * The troubleshooting log explains why each <code>_has</code> parameter was allowed or denied.
 */
// Created by Claude Opus 5.5
public class AuthorizationInterceptorReverseChainLogJpaR4Test extends BaseResourceProviderR4Test {

	private static final String LOINC = "http://loinc.org";
	private static final String HIV_VIRAL_LOAD = "20447-9";

	private static final String ALLOWED_BY_RULE = "Search parameter {} is allowed by rule {}";
	private static final String ALLOWED_BY_DEFAULT_POLICY = "Search parameter {} is allowed by the default policy";
	private static final String DENIED_BY_RULE = "Search parameter {} is denied by rule {}";
	private static final String DENIED_WITHOUT_RULE = "Search parameter {} is denied: no rule allows reading every";

	@Autowired
	private SearchParamMatcher mySearchParamMatcher;

	private final Logger myTroubleshootingLog = mock(Logger.class);

	@BeforeEach
	void createData() {
		IIdType patient = createPatient(withId("pA"));
		createObservation(withId("obsA"), withSubject(patient), withObservationCode(LOINC, HIV_VIRAL_LOAD));
	}

	@Test
	void testHas_readPatientAndObservationType_logsAllowedByRule() {
		registerRules(PolicyEnum.DENY, new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").withAnyId().andThen()
			.build());

		search();

		verify(myTroubleshootingLog).debug(eq(ALLOWED_BY_RULE), eq("_has:Observation:subject"), any(Object.class));
		verify(myTroubleshootingLog, never()).debug(eq(DENIED_BY_RULE), anyString(), any(Object.class));
	}

	@Test
	void testHas_defaultPolicyAllowWithNoObservationRule_logsAllowedByDefaultPolicy() {
		registerRules(PolicyEnum.ALLOW, new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.build());

		search();

		verify(myTroubleshootingLog).debug(ALLOWED_BY_DEFAULT_POLICY, "_has:Observation:subject");
	}

	@Test
	void testHas_observationDenied_logsDeniedByRule() {
		registerRules(PolicyEnum.DENY, new RuleBuilder()
			.deny().read().resourcesOfType("Observation").withAnyId().andThen()
			.allow().read().allResources().withAnyId().andThen()
			.build());

		assertThatThrownBy(this::search).isInstanceOf(ForbiddenOperationException.class);

		verify(myTroubleshootingLog).debug(eq(DENIED_BY_RULE), eq("_has:Observation:subject"), any(Object.class));
		verify(myTroubleshootingLog, never()).debug(eq(ALLOWED_BY_RULE), anyString(), any(Object.class));
	}

	@Test
	void testHas_readPatientOnly_logsDeniedWithoutRule() {
		registerRules(PolicyEnum.DENY, new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.build());

		assertThatThrownBy(this::search).isInstanceOf(ForbiddenOperationException.class);

		verify(myTroubleshootingLog)
			.debug(startsWith(DENIED_WITHOUT_RULE), eq("_has:Observation:subject"), eq("Observation"), eq("Observation"));
		verify(myTroubleshootingLog, never()).debug(eq(ALLOWED_BY_RULE), anyString(), any(Object.class));
	}

	private void registerRules(PolicyEnum theDefaultPolicy, List<IAuthRule> theRules) {
		AuthorizationInterceptor interceptor = new AuthorizationInterceptor(theDefaultPolicy) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return theRules;
			}
		};
		interceptor.setAuthorizationSearchParamMatcher(new AuthorizationSearchParamMatcher(mySearchParamMatcher));
		interceptor.setFlags(ENFORCE_REVERSE_CHAINED_AUTHORIZATION);
		interceptor.setTroubleshootingLog(myTroubleshootingLog);
		myServer.getRestfulServer().registerInterceptor(interceptor);
	}

	private Bundle search() {
		return myClient.search().byUrl("Patient?_has:Observation:subject:code=20447-9").returnBundle(Bundle.class).execute();
	}
}
