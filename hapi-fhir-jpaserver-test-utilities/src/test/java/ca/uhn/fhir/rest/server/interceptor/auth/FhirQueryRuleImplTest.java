package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.test.util.LogbackCaptureTestExtension;
import ch.qos.logback.classic.Level;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

// fixme where should this test live?
// fixme can we mock the resource?  We just use it for stubbing here. If so, move this back to hapi-fhir-server ca.uhn.fhir.rest.server.interceptor.auth
@MockitoSettings(strictness= Strictness.LENIENT)
class FhirQueryRuleImplTest implements ITestDataBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirQueryRuleImplTest.class);

	@RegisterExtension
	LogbackCaptureTestExtension myLogCapture = new LogbackCaptureTestExtension(FhirQueryRuleImplTest.class.getName());

	private FhirQueryRuleImpl myRule;
	private IBaseResource myResource;
	@Mock
	private IRuleApplier myMockRuleApplier;
	private SystemRequestDetails mySrd = new SystemRequestDetails();
	private FhirContext myFhirContext = FhirContext.forR4Cached();


	@Mock
	private IAuthorizationSearchParamMatcher myMockSearchParamMatcher;

	@BeforeEach
	public void setUp() {
		when(myMockRuleApplier.getTroubleshootingLog()).thenReturn(ourLog);
		mySrd.setFhirContext(myFhirContext);
	}

	void withSearchParamMatcherPresent() {
		when(myMockRuleApplier.getSearchParamMatcher()).thenReturn(myMockSearchParamMatcher);
	}

	@Nested
	public class MatchingLogic {
		@BeforeEach
		public void setUp() {
			withSearchParamMatcherPresent();
		}

		@Test
		public void simpleStringSearch_match_allow() {
			// patient.r  patient/Patient.rs?name=smi
			// given
			withPatientWithNameAndId();

			RuleBuilder b = new RuleBuilder();
			myRule = (FhirQueryRuleImpl) b.allow()
				.read()
				.resourcesOfType("Patient")
				.inCompartmentWithFilter("patient", myResource.getIdElement().withResourceType("Patient"), "family=smi")
				.andThen().build().get(0);

			when(myMockSearchParamMatcher.match(anyString(), ArgumentMatchers.same(myResource))).thenReturn(IAuthorizationSearchParamMatcher.MatchResult.makeMatched());

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource();

			// then
			assertThat(verdict, notNullValue());
			assertThat(verdict.getDecision(), equalTo(PolicyEnum.ALLOW));
		}

		@Test
		public void simpleStringSearch_noMatch_noVerdict() {
			// patient.r  patient/Patient.rs?name=smi
			// given
			withPatientWithNameAndId();
			myRule = (FhirQueryRuleImpl) new RuleBuilder().allow().read().resourcesOfType("Patient")
				.inCompartmentWithFilter("patient", myResource.getIdElement().withResourceType("Patient"), "family=smi").andThen().build().get(0);
			// fixme validate the various translations of scope to query string.
			when(myMockSearchParamMatcher.match(anyString()/*ArgumentMatchers.eq("Patient?family=smi")*/, ArgumentMatchers.same(myResource))).thenReturn(IAuthorizationSearchParamMatcher.MatchResult.makeUnmatched());

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource();

			// then
			assertThat(verdict, nullValue());
		}

		public void observation_inCompartmentMatchFilter_allowVerdict() {
		}

		public void observation_notInCompartmentMatchFilter_noVerdict() {
		}

	}

	private AuthorizationInterceptor.Verdict applyRuleToResource() {
		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(RestOperationTypeEnum.SEARCH_TYPE, mySrd, null, null, myResource, myMockRuleApplier, new HashSet<>(), Pointcut.STORAGE_PRESHOW_RESOURCES);
		return verdict;
	}

	private void withPatientWithNameAndId() {
		myResource = buildResource("Patient", withFamily("Smith"), withId("some-id"));
	}

	@Nested
	public class MisconfigurationChecks {


		// fixme check for unsupported params during CdrAuthInterceptor scopes->perms translation.

		/**
		 * in case an unsupported perm snuck through the front door.
		 * Each scope provides positive perm, so unsupported means we can't vote yes.  Abstain.
		 */
		@Test
		public void observation_unsupportedChain_noVerdict() {
			withSearchParamMatcherPresent();
			withPatientWithNameAndId();
			myRule = (FhirQueryRuleImpl) new RuleBuilder().allow().read().resourcesOfType("Patient")
				.inCompartmentWithFilter("patient", myResource.getIdElement().withResourceType("Patient"), "family=smi").andThen().build().get(0);
			when(myMockSearchParamMatcher.match(anyString()/*ArgumentMatchers.eq("Patient?family=smi")*/, ArgumentMatchers.same(myResource))).thenReturn(IAuthorizationSearchParamMatcher.MatchResult.makeUnsupported("I'm broken unsupported chain XXX"));

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource();

			// then
			assertThat(verdict, nullValue());
			assertThat(myLogCapture.getLogEvents(),
				hasItem(myLogCapture.eventWithLevelAndMessageContains(Level.WARN, "unsupported chain XXX")));
		}

		/**
		 * for backwards compatibility, if the IRuleApplier doesn't provide a matcher service,
		 * log a warning, and return no verdict.
		 */
		@Test
		public void noMatcherService_unsupportedPerm_noVerdict() {
			withPatientWithNameAndId();
			myRule = (FhirQueryRuleImpl) new RuleBuilder().allow().read().resourcesOfType("Patient")
				.inCompartmentWithFilter("patient", myResource.getIdElement().withResourceType("Patient"), "family=smi").andThen().build().get(0);
			when(myMockSearchParamMatcher.match(anyString()/*ArgumentMatchers.eq("Patient?family=smi")*/, ArgumentMatchers.same(myResource))).thenReturn(IAuthorizationSearchParamMatcher.MatchResult.makeUnmatched());

			// when
			AuthorizationInterceptor.Verdict verdict = applyRuleToResource();

			// then
			assertThat(verdict, nullValue());
			assertThat(myLogCapture.getLogEvents(),
				hasItem(myLogCapture.eventWithLevelAndMessageContains(Level.WARN, "no matcher configured")));
		}

	}
	// fixme how to test the difference between patient/*.rs?code=foo and patient/Observation.rs?code=foo?
	// We need the builder to set AppliesTypeEnum, and the use that to build the matcher expression.

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
		return FhirContext.forR4Cached();
	}
}
