package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.interceptor.matching.ISearchParamMatcher;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

// fixme where should this test live?
@MockitoSettings(strictness= Strictness.LENIENT)
class FhirQueryRuleImplTest implements ITestDataBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirQueryRuleImplTest.class);

	private IAuthRule myRule;
	private IBaseResource myResource;
	@Mock
	private IRuleApplier myMockRuleApplier;
	private SystemRequestDetails mySrd = new SystemRequestDetails();
	private FhirContext myFhirContext = FhirContext.forR4Cached();


	@Mock
	private ISearchParamMatcher myMockSearchParamMatcher;

	@BeforeEach
	public void setupMocks() {
		when(myMockRuleApplier.getSearchParamMatcher()).thenReturn(myMockSearchParamMatcher);
		when(myMockRuleApplier.getTroubleshootingLog()).thenReturn(ourLog);
	}

	@Test
	public void simpleStringSearch_match_allow() {
		// patient.r  patient/Patient.rs?name=smi
		// given
		myResource = buildResource("Patient", withFamily("Smith"), withId("some-id"));
		RuleBuilder b = new RuleBuilder();
		myRule = b.allow().read().resourcesOfType("Patient").inCompartmentWithFilter("patient", myResource.getIdElement().withResourceType("Patient"), "family=smi").andThen().build().get(0);

		mySrd.setFhirContext(myFhirContext);

		// when
		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(RestOperationTypeEnum.SEARCH_TYPE, mySrd, null, null, myResource, myMockRuleApplier, new HashSet<>(), Pointcut.STORAGE_PRESHOW_RESOURCES);

		// then
		assertThat(verdict, notNullValue());
	}

	@Test
	public void simpleStringSearch_noMatch_noVerdict() {
		// patient.r  patient/Patient.rs?name=smi
		// given
		myResource = buildResource("Patient", withFamily("Jones"), withId("some-id"));
		RuleBuilder b = new RuleBuilder();
		myRule = b.allow().read().resourcesOfType("Patient").inCompartmentWithFilter("patient", myResource.getIdElement().withResourceType("Patient"), "family=smi").andThen().build().get(0);

		mySrd.setFhirContext(myFhirContext);

		// when
		AuthorizationInterceptor.Verdict verdict = myRule.applyRule(RestOperationTypeEnum.SEARCH_TYPE, mySrd, null, null, myResource, myMockRuleApplier, new HashSet<>(), Pointcut.STORAGE_PRESHOW_RESOURCES);

		// then
		assertThat(verdict, nullValue());
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
