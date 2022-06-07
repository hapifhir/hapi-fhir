package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.FhirQueryRuleImpl;
import ca.uhn.fhir.rest.server.interceptor.auth.IRuleApplier;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleOpEnum;
import ca.uhn.fhir.rest.server.interceptor.matching.ISearchParamMatcher;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

// fixme where should this test live?
@ExtendWith(MockitoExtension.class)
class FhirQueryRuleImplTest implements ITestDataBuilder {

	private FhirQueryRuleImpl myRule;
	private IBaseResource myResource;
	@Mock
	private IRuleApplier myMockRuleApplier;
	private SystemRequestDetails mySrd = new SystemRequestDetails();

	@Mock
	private ISearchParamMatcher myMockSearchParamMatcher;

	@BeforeEach
	public void setupMocks() {
		when(myMockRuleApplier.getSearchParamMatcher()).thenReturn(myMockSearchParamMatcher);
	}

	@Test
	public void simpleStringSearch_match_allow() {
		// given
		myResource = buildResource("Patient", withFamily("Smith"), withId("some-id"));
		myRule = new FhirQueryRuleImpl("hmm - what goes here?");
		myRule.setOp(RuleOpEnum.READ);
		myRule.setFilter("family=smith");

		// when
		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(RestOperationTypeEnum.SEARCH_TYPE, mySrd, null, null, myResource, myMockRuleApplier, new HashSet<>(), Pointcut.STORAGE_PRESHOW_RESOURCES);

		// then
		assertThat(verdict, notNullValue());
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
		return FhirContext.forR4Cached();
	}
}
