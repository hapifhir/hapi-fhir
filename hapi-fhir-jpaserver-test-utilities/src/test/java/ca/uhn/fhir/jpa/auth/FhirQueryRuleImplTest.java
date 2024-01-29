package ca.uhn.fhir.jpa.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.FhirQueryRuleTester;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher;
import ca.uhn.fhir.rest.server.interceptor.auth.IRuleApplier;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.test.util.LogbackCaptureTestExtension;
import ch.qos.logback.classic.Level;
import jakarta.annotation.Nullable;
import org.hamcrest.MatcherAssert;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
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
import static ca.uhn.test.util.LogbackCaptureTestExtension.eventWithLevelAndMessageContains;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

// TODO: Is there a better home for this test? It can't live in hapi-fhir-server since we need a real FhirContext for the compartment checks.
@MockitoSettings
class FhirQueryRuleImplTest implements ITestDataBuilder {

	private static final String OPERATION = "operation";
	private static final String TYPE = "type";
	private static final String PATH = "path";
	private static final String VALUE = "value";
	private static final String REPLACE = "replace";
	private static final String PATIENT_BIRTH_DATE = "Patient.birthDate";
	private static final String BUNDLE = "Bundle";
	final private TestRuleApplier myMockRuleApplier = new TestRuleApplier() {
		@Override
		public @Nullable IAuthorizationSearchParamMatcher getSearchParamMatcher() {
			return myMatcher;
		}
	};

	@RegisterExtension
	LogbackCaptureTestExtension myLogCapture = new LogbackCaptureTestExtension(myMockRuleApplier.getTroubleshootingLog().getName());

	private IAuthRule myRule;
	IIdType myPatientId = new IdDt("Patient/1");
	private IBaseResource myPatient;
	private IBaseResource myObservation;
	@Mock
	private RequestDetails myRequestDetails;
	private final FhirContext myFhirContext = spy(FhirContext.forR4Cached());
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
			assertThat(verdict, notNullValue());
			assertThat(verdict.getDecision(), equalTo(PolicyEnum.ALLOW));
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
			assertThat(verdict, notNullValue());
			assertThat(verdict.getDecision(), equalTo(PolicyEnum.ALLOW));
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
			assertThat(verdict, nullValue());
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
			assertThat(verdict, nullValue());
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
			assertThat(verdict, nullValue());
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
			assertThat(verdict, nullValue());
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
			assertThat(verdict, notNullValue());
			assertThat(verdict.getDecision(), equalTo(PolicyEnum.DENY));
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
			assertThat(verdict, notNullValue());
			assertThat(verdict.getDecision(), equalTo(PolicyEnum.ALLOW));
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
			assertThat(verdict, nullValue());
		}

	}

	@Nested
	public class TransactionBundle {
		@Mock
		private IRuleApplier myMockRuleApplier;

		@BeforeEach
		void beforeEach() {
			withPatientWithNameAndId();

			myRule = new RuleBuilder()
				.allow()
				.transaction()
				.withAnyOperation()
				.andApplyNormalRules()
				.build()
				.get(0);
		}

		@Test
		void testTransactionBundleUpdateWithParameters() {
			final Parameters parameters = buildParameters();

			try {
				applyRule(getBundle(false, parameters));
				fail("Expected an InvalidRequestException");
			} catch (InvalidRequestException exception) {
				assertEquals("HAPI-0339: Can not handle transaction with nested resource of type Parameters", exception.getMessage());
			}
		}

		@Test
		void testTransactionBundleWithNestedBundle() {
			final Parameters parameters = buildParameters();

			final IBaseBundle bundle = getBundle(true, parameters);

			// There's no better way to "put a Bundle inside a Bundle" with the BundleBuilder
			final RuntimeResourceDefinition mockRuntimeResourceDefinition  = mock(RuntimeResourceDefinition.class);
			when(mockRuntimeResourceDefinition.getName()).thenReturn(BUNDLE);
			doReturn(mockRuntimeResourceDefinition).when(myFhirContext).getResourceDefinition(parameters);

			try {
				applyRule(bundle);
				fail("Expected an InvalidRequestException");
			} catch (InvalidRequestException exception) {
				assertEquals("HAPI-0339: Can not handle transaction with nested resource of type Bundle", exception.getMessage());
			}
		}

		@Test
		void testTransactionBundlePatchWithParameters() {
			final Parameters parameters = buildParameters();

			when(myMockRuleApplier.applyRulesAndReturnDecision(eq(RestOperationTypeEnum.PATCH), eq(myRequestDetails), eq(parameters), isNull(), isNull(), isNull()))
				.thenReturn(new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, null));

			final AuthorizationInterceptor.Verdict verdict = applyRule(getBundle(true, parameters));

			assertThat(verdict.getDecision(), equalTo(PolicyEnum.ALLOW));
		}

		private AuthorizationInterceptor.Verdict applyRule(IBaseBundle theBundle) {
			return myRule.applyRule(RestOperationTypeEnum.TRANSACTION, myRequestDetails, theBundle, myPatientId, myPatient, myMockRuleApplier, new HashSet<>(), null);
		}

		private IBaseBundle getBundle(boolean isPatch, @Nullable Parameters theParameters) {
			final BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
			final Parameters parameters = theParameters == null ? buildParameters() : theParameters;

			if (isPatch) {
				bundleBuilder.addTransactionFhirPatchEntry(parameters);
			} else {
				bundleBuilder.addTransactionUpdateEntry(parameters);
			}

			return bundleBuilder.getBundle();
		}

		private Parameters buildParameters() {
			final Parameters patch = new Parameters();

			final Parameters.ParametersParameterComponent op = patch.addParameter().setName(OPERATION);
			op.addPart().setName(TYPE).setValue(new CodeType(REPLACE));
			op.addPart().setName(PATH).setValue(new CodeType(PATIENT_BIRTH_DATE));
			op.addPart().setName(VALUE).setValue(new StringType("1912-04-14"));

			return patch;
		}
	}


	private void stubMatcherCall(String expectedQuery, IBaseResource theTargetResource, IAuthorizationSearchParamMatcher.MatchResult theStubResult) {
		when(myMatcher.match(eq(expectedQuery), ArgumentMatchers.same(theTargetResource)))
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
			assertThat(verdict, nullValue());
			MatcherAssert.assertThat(myLogCapture.getLogEvents(),
				hasItem(eventWithLevelAndMessageContains(Level.WARN, "unsupported chain XXX")));
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
			assertThat(verdict.getDecision(), equalTo(PolicyEnum.DENY));
			MatcherAssert.assertThat(myLogCapture.getLogEvents(),
				hasItem(eventWithLevelAndMessageContains(Level.WARN, "unsupported chain XXX")));
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
			assertThat(verdict, nullValue());
			MatcherAssert.assertThat(myLogCapture.getLogEvents(),
				hasItem(eventWithLevelAndMessageContains(Level.WARN, "No matcher provided")));
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
