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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDstu3Config.class, DaoTestDataBuilder.Config.class})
public class SmartV2AuthServiceTest implements ITestDataBuilder, IRuleApplier {
	private static final Logger ourLog = LoggerFactory.getLogger(SmartV2AuthServiceTest.class);

	@Autowired
	SearchParamMatcher mySearchParamMatcher;
	@Autowired
	DaoTestDataBuilder myDaoTestDataBuilder;
	@Autowired
	FhirContext myFhirContext;


	private AuthorizationSearchParamMatcher myMatcher;
	private IBaseResource myResource;
	private IAuthorizationSearchParamMatcher.MatchResult result;
	private SystemRequestDetails mySrd = new SystemRequestDetails();

	@BeforeEach
	void setUp() {
		// fixme should we add this to the context and autowire it?  publish it to the AuthInterceptor?
		myMatcher = new AuthorizationSearchParamMatcher(mySearchParamMatcher);
	}

	@Nested
	public class BasicMatches {
		public void createPatient() {
			myResource = buildResource("Patient", withId("anId"), withFamily("Smith"));
		}

		@Test
		public void basicMatch_matches() {
			// given
			createPatient();

			// when
			result = myMatcher.match("Patient?family=Smith", myResource);

			// then
			assertThat(result, notNullValue());
			assertThat(result.getMatch(), equalTo(IAuthorizationSearchParamMatcher.Match.MATCH));
		}

		@Test
		public void basicMatch_noMatch() {
			// given
			createPatient();

			// when
			result = myMatcher.match("Patient?family=Jones", myResource);

			// then
			assertThat(result, notNullValue());
			assertThat(result.getMatch(), equalTo(IAuthorizationSearchParamMatcher.Match.NO_MATCH));
		}

		@Test
		public void basicMatch_unsupported() {
			// given
			createPatient();

			// when
			result = myMatcher.match("Patient?birthDate=ap2020", myResource);

			// then
			assertThat(result, notNullValue());
			assertThat(result.getMatch(), equalTo(IAuthorizationSearchParamMatcher.Match.UNSUPPORTED));
		}
	}

	@Test
	public void fullRule_match_allow() {
	   // given
		myResource = buildResource("Patient", withId("anId"), withFamily("Smith"));
		FhirQueryRuleImpl rule = (FhirQueryRuleImpl) new RuleBuilder().allow().read().resourcesOfType("Patient")
			.inCompartmentWithFilter("patient", myResource.getIdElement().withResourceType("Patient"), "family=smi").andThen().build().get(0);

	   // when
		AuthorizationInterceptor.Verdict verdict = rule.applyRule(RestOperationTypeEnum.SEARCH_TYPE, mySrd, null, null, myResource, this, new HashSet<>(), Pointcut.STORAGE_PRESHOW_RESOURCES);

		// then
		assertThat(verdict, notNullValue());
		assertThat(verdict.getDecision(), equalTo(PolicyEnum.ALLOW));
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
}
