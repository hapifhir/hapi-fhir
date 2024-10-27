package ca.uhn.fhir.jpa.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.FhirQueryRuleTester;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashSet;

import static ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher.MatchResult.buildMatched;
import static ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher.MatchResult.buildUnmatched;
import static ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher.MatchResult.buildUnsupported;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

// TODO: Is there a better home for this test? It can't live in hapi-fhir-server since we need a real FhirContext for the compartment checks.
@MockitoSettings
class FhirQueryRuleImplTest implements ITestDataBuilder {

	final private TestRuleApplier myMockRuleApplier = new TestRuleApplier() {
		@Override
		public @Nullable IAuthorizationSearchParamMatcher getSearchParamMatcher() {
			return myMatcher;
		}
	};

	@RegisterExtension
	LogbackTestExtension myLogCapture = new LogbackTestExtension(myMockRuleApplier.getTroubleshootingLog().getName());

	private IAuthRule myRule;
	IIdType myPatientId = new IdDt("Patient/1");
	private IBaseResource myPatient;
	private IBaseResource myObservation;
	@Mock
	private RequestDetails myRequestDetails;
	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	@Mock
	private IAuthorizationSearchParamMatcher myMatcher;

	@BeforeEach
	public void setUp() {
		when(myRequestDetails.getFhirContext()).thenReturn(myFhirContext);
	}


	@Nested
	public class MatchingLogic {

		@Test
		public void typeWithFilter_whenMatch_allow() {
			// given
			withPatientWithNameAndId();

			RuleBuilder b = new RuleBuilder();
			myRule = b.allow()
				.read()
				.resourcesOfType("Patient")
				.withFilter( "family=Smith")
				.andThen().build().get(0);

			stubMatcherCall("Patient?family=Smith", myPatient, buildMatched());

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource(myPatient);

			// then
			assertNotNull(verdict);
			assertEquals(PolicyEnum.ALLOW, verdict.getDecision());
		}

		@Test
		public void anyTypewithQueryFilter_whenMatch_allow() {
			// given
			withPatientWithNameAndId();

			RuleBuilder b = new RuleBuilder();
			myRule = b.allow()
				.read()
				.allResources()
				.withFilter( "family=Smith")
				.andThen().build().get(0);

			stubMatcherCall("Patient?family=Smith", myPatient, buildMatched());

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource(myPatient);

			// then
			assertNotNull(verdict);
			assertEquals(PolicyEnum.ALLOW, verdict.getDecision());
		}

		@Test
		public void typeWithQuery_noQueryMatch_noVerdict() {
			// given
			withPatientWithNameAndId();
			myRule = new RuleBuilder().allow().read().resourcesOfType("Patient")
				.withFilter( "family=smi")
				.andThen().build().get(0);
			stubMatcherCall("Patient?family=smi", myPatient, buildUnmatched());

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource(myPatient);

			// then
			assertNull(verdict);
		}

		@Test
		public void typeWithQuery_wrongType_noVerdict() {
			// given
			withPatientWithNameAndId();
			withObservationWithSubjectAndCode(myPatientId);
			myRule = new RuleBuilder().allow().read().resourcesOfType("Patient")
				.withFilter( "family=smi")
				.andThen().build().get(0);
			//stubMatcherCall("Patient?family=smi", myPatient, buildUnmatched());

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource(myObservation);

			// then
			assertNull(verdict);
		}

		@Test
		public void inCompartmentwithQueryFilter_resourceNotInCompartmentButMatchFilter_noVerdict() {
			// given
			withPatientWithNameAndId();
			// create patient for observation to point to so that the observation isn't in our main patient compartment
			withObservationWithSubjectAndCode(myPatient.getIdElement());

			myRule = new RuleBuilder().allow().read().resourcesOfType("Observation")
				.inCompartmentWithFilter("patient", myPatient.getIdElement().withResourceType("Patient"), "code=28521000087105")
				.andThen().build().get(0);
			// matcher won't be called since not in compartment

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource(myObservation);

			// then
			assertNull(verdict);
		}

		@Test
		public void observation_noMatchFilter_noVerdict() {
			// given
			withPatientWithNameAndId();
			withObservationWithSubjectAndCode(myPatient.getIdElement());

			myRule = new RuleBuilder().allow().read().resourcesOfType("Observation")
				.withFilter("code=12")
				.andThen().build().get(0);

			stubMatcherCall("Observation?code=12", myObservation, buildUnmatched());

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource(myObservation);

			// then
			assertNull(verdict);
		}

		@Test
		public void denyTypeWithQueryFilter_match_deny() {
			// given
			withPatientWithNameAndId();
			withObservationWithSubjectAndCode(myPatient.getIdElement());

			myRule = new RuleBuilder().deny().read().resourcesOfType("Observation")
				.withFilter("code=28521000087105")
				.andThen().build().get(0);
			stubMatcherCall("Observation?code=28521000087105", myObservation, buildMatched());

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource(myObservation);

			// then
			assertNotNull(verdict);
			assertEquals(PolicyEnum.DENY, verdict.getDecision());
		}


		@Test
		public void allowIdwithQueryFilter_matchesIdAndFilter_allow() {
			// given
			withPatientWithNameAndId();

			myRule = new RuleBuilder()
				.allow()
				.read().instance(myPatient.getIdElement())
				.withTester(new FhirQueryRuleTester("name=smith"))
				.andThen().build().get(0);

			stubMatcherCall("Patient?name=smith", myPatient, buildMatched());

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource(myPatient);

			// then
			assertNotNull(verdict);
			assertEquals(PolicyEnum.ALLOW, verdict.getDecision());
		}


		@Test
		public void allowIdwithQueryFilter_matchesJustIdNotFilter_abstain() {
			// given
			withPatientWithNameAndId();

			myRule = new RuleBuilder()
				.allow()
				.read().instance(myPatient.getIdElement())
				.withTester(new FhirQueryRuleTester("name=smith"))
				.andThen().build().get(0);

			stubMatcherCall("Patient?name=smith", myPatient, buildUnmatched());

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource(myPatient);

			// then
			assertNull(verdict);
		}

	}

	private void stubMatcherCall(String expectedQuery, IBaseResource theTargetResource, IAuthorizationSearchParamMatcher.MatchResult theStubResult) {
		when(myMatcher.match(ArgumentMatchers.eq(expectedQuery), ArgumentMatchers.same(theTargetResource)))
			.thenReturn(theStubResult);
	}


	@Nested
	public class MisconfigurationChecks {
		/**
		 * in case an unsupported perm snuck through the front door.
		 * Each scope provides positive perm, so unsupported means we can't vote yes.  Abstain.
		 */
		@Test
		public void givenAllowRule_whenUnsupportedQuery_noVerdict() {
			withPatientWithNameAndId();
			myRule = new RuleBuilder().allow().read().resourcesOfType("Patient")
				.inCompartmentWithFilter("patient", myPatient.getIdElement().withResourceType("Patient"), "unsupported.chain=smi").andThen().build().get(0);
			stubMatcherCall("Patient?unsupported.chain=smi", myPatient, buildUnsupported("I'm broken unsupported chain XXX"));

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource(myPatient);

			// then
			assertNull(verdict);
			LogbackTestExtensionAssert.assertThat(myLogCapture).hasWarnMessage("unsupported chain XXX");
		}

		@Test
		public void givenDenyRule_whenUnsupportedQuery_reject() {
			withPatientWithNameAndId();
			myRule = new RuleBuilder().deny().read().resourcesOfType("Patient")
				.inCompartmentWithFilter("patient", myPatientId, "unsupported.chain=smi").andThen().build().get(0);
			stubMatcherCall("Patient?unsupported.chain=smi", myPatient, buildUnsupported("I'm broken unsupported chain XXX"));

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource(myPatient);

			// then
			assertEquals(PolicyEnum.DENY, verdict.getDecision());
			LogbackTestExtensionAssert.assertThat(myLogCapture).hasWarnMessage("unsupported chain XXX");
		}

		/**
		 * for backwards compatibility, if the IRuleApplier doesn't provide a matcher service,
		 * log a warning, and return no verdict.
		 */
		@Test
		public void noMatcherService_unsupportedPerm_noVerdict() {
			withPatientWithNameAndId();
			myMatcher = null;
			myRule = new RuleBuilder().allow().read().resourcesOfType("Patient")
				.inCompartmentWithFilter("patient", myPatient.getIdElement().withResourceType("Patient"), "code:in=foo").andThen().build().get(0);

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource(myPatient);

			// then
			assertNull(verdict);
			LogbackTestExtensionAssert.assertThat(myLogCapture).hasWarnMessage("No matcher provided");
		}

	}
	// We need the builder to set AppliesTypeEnum, and the use that to build the matcher expression.

	private AuthorizationInterceptor.Verdict applyRuleToResource(IBaseResource theResource) {
		return myRule.applyRule(RestOperationTypeEnum.SEARCH_TYPE, myRequestDetails, null, null, theResource, myMockRuleApplier, new HashSet<>(), Pointcut.STORAGE_PRESHOW_RESOURCES);
	}

	private void withPatientWithNameAndId() {
		myPatient = buildPatient(withId(myPatientId));
	}

	// Use in sequence with above
	private void withObservationWithSubjectAndCode(IIdType theIdElement) {
		String snomedUriString = "https://snomed.info/sct";
		String insulin2hCode = "28521000087105";
		myObservation = buildResource("Observation", withObservationCode(snomedUriString, insulin2hCode), withSubject(theIdElement));
	}


	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return null;
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return null;
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

}
