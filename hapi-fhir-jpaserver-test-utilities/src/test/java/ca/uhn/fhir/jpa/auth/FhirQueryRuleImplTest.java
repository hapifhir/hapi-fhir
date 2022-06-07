package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FhirQueryRuleImplTest implements ITestDataBuilder {

	private FhirQueryRuleImpl myRule;
	private IBaseResource myResource;
	@Mock
	private IRuleApplier myMockRuleApplier;
	@Mock
	private RequestDetails mySrd;
	@Mock
	private IAuthRuleSearchParamMatcher myMockSearchParamMatcher;

	@BeforeEach
	public void setupMocks() {
		when(myMockRuleApplier.getSearchParamMatcher()).thenReturn(myMockSearchParamMatcher);
	}

	@Test
	public void simpleStringSearch_match_allow() {
		// given
		myResource = buildResource("Patient", withFamily("Smith"), withId("some-id"));
		myRule = new FhirQueryRuleImpl("hmm - what goes here?");
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
		return new FhirContext();
	}
}
