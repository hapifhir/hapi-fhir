package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.matcher.AuthorizationSearchParamMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.test.config.TestDstu3Config;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.FhirQueryRuleImpl;
import ca.uhn.fhir.rest.server.interceptor.auth.IRuleApplier;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.test.util.LogbackCaptureTestExtension;
import ch.qos.logback.classic.Level;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDstu3Config.class, DaoTestDataBuilder.Config.class})
public class SmartV2AuthServiceTest implements ITestDataBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(SmartV2AuthServiceTest.class);

	@Autowired
	SearchParamMatcher mySearchParamMatcher;
	@Autowired
	DaoTestDataBuilder myDaoTestDataBuilder;
	@Autowired
	FhirContext myFhirContext;

	protected IRuleApplier myRuleApplier = new IRuleApplier() {
		@NotNull
		@Override
		public Logger getTroubleshootingLog() {
			return ourLog;
		}

		@Override
		public AuthorizationInterceptor.Verdict applyRulesAndReturnDecision(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, Pointcut thePointcut) {
			return null;
		}

		@Nullable
		@Override
		public IValidationSupport getValidationSupport() {
			return null;
		}

		@Override
		public AuthorizationSearchParamMatcher getSearchParamMatcher() {
			return myMatcher;
		}
	};

	@RegisterExtension
	LogbackCaptureTestExtension myLogCapture = new LogbackCaptureTestExtension(SmartV2AuthServiceTest.class.getName());


	private AuthorizationSearchParamMatcher myMatcher;
	private IBaseResource myResource;
	private IAuthorizationSearchParamMatcher.MatchResult result;
	private SystemRequestDetails mySrd = new SystemRequestDetails();

	@BeforeEach
	void setUp() {
		// wipjv What about Mongo? Also add to Mongo persistence and export.
		myMatcher = new AuthorizationSearchParamMatcher(mySearchParamMatcher);
	}

	@Nested
	public class BasicMatches {

		@Test
		public void basicMatch_matches() {
			// given
			helpCreatePatient();

			// when
			result = myMatcher.match("Patient?family=Smith", myResource);

			// then
			assertThat(result, notNullValue());
			assertThat(result.getMatch(), equalTo(IAuthorizationSearchParamMatcher.Match.MATCH));
		}

		@Test
		public void basicMatch_noMatch() {
			// given
			helpCreatePatient();

			// when
			result = myMatcher.match("Patient?family=Jones", myResource);

			// then
			assertThat(result, notNullValue());
			assertThat(result.getMatch(), equalTo(IAuthorizationSearchParamMatcher.Match.NO_MATCH));
		}

		@Test
		public void basicMatch_unsupported() {
			// given
			helpCreatePatient();

			// when
			result = myMatcher.match("Patient?birthdate=ap2020", myResource);

			// then
			assertThat(result, notNullValue());
			assertThat(result.getMatch(), equalTo(IAuthorizationSearchParamMatcher.Match.UNSUPPORTED));
		}
	}

	@Nested
	public class FullRule {

		@Test
		public void fullRule_match_allow() {
			// given
			helpCreatePatient();
			FhirQueryRuleImpl rule = buildSimplePatientRuleWithFilter("family=Smith");

			// when
			AuthorizationInterceptor.Verdict verdict = rule.applyRule(RestOperationTypeEnum.SEARCH_TYPE, mySrd, null, null, myResource, myRuleApplier, new HashSet<>(), Pointcut.STORAGE_PRESHOW_RESOURCES);

			// then
			assertThat(verdict, notNullValue());
			assertThat(verdict.getDecision(), equalTo(PolicyEnum.ALLOW));
		}

		@Test
		public void fullRule_noMatch_pass() {
			// given
			helpCreatePatient();
			FhirQueryRuleImpl rule = buildSimplePatientRuleWithFilter("family=Jones");

			// when
			AuthorizationInterceptor.Verdict verdict = rule.applyRule(RestOperationTypeEnum.SEARCH_TYPE, mySrd, null, null, myResource, myRuleApplier, new HashSet<>(), Pointcut.STORAGE_PRESHOW_RESOURCES);

			// then
			assertThat(verdict, nullValue());
		}

		@Test
		public void fullRule_unsupported_passAndWarn() {
			// given
			helpCreatePatient();
			FhirQueryRuleImpl rule = buildSimplePatientRuleWithFilter("birthdate=ap2020");

			// when
			AuthorizationInterceptor.Verdict verdict = rule.applyRule(RestOperationTypeEnum.SEARCH_TYPE, mySrd, null, null, myResource, myRuleApplier, new HashSet<>(), Pointcut.STORAGE_PRESHOW_RESOURCES);

			// then
			assertThat(verdict, nullValue());
			assertThat(myLogCapture.getLogEvents(),
				hasItem(myLogCapture.eventWithLevelAndMessageContains(Level.WARN, "Unsupported matcher expression")));
		}

	}

	private FhirQueryRuleImpl buildSimplePatientRuleWithFilter(String theFilter) {
		FhirQueryRuleImpl rule = (FhirQueryRuleImpl) new RuleBuilder()
			.allow()
			.read()
			.resourcesOfType("Patient")
			.inCompartmentWithFilter("patient", myResource.getIdElement().withResourceType("Patient"), theFilter)
			.andThen().build().get(0);
		return rule;
	}


	// Inherited convenience methods
	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return myDaoTestDataBuilder.doCreateResource(theResource);
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return myDaoTestDataBuilder.doUpdateResource(theResource);
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	public void helpCreatePatient() {
		myResource = buildResource("Patient", withId("anId"), withFamily("Smith"));
	}
}
