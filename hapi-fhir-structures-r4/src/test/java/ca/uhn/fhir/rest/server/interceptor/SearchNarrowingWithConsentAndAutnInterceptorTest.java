package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizedList;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.SearchNarrowingConsentService;
import ca.uhn.fhir.rest.server.interceptor.auth.SearchNarrowingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentInterceptor;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.test.utilities.SearchTestUtil.toUnqualifiedVersionlessIdValues;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * A test to ensure that Search Narrowing with Consent Interceptor for post filtering and AuthorizationInterceptor for
 * final checking all works.
 */
@ExtendWith(MockitoExtension.class)
public class SearchNarrowingWithConsentAndAutnInterceptorTest {

	public static final String VALUESET_1_URL = "http://valueset-1";
	public static final String CODESYSTEM_URL = "http://codesystem-1";
	public static final String CODE_PREFIX = "code";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchNarrowingWithConsentAndAutnInterceptorTest.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	@RegisterExtension
	private final HttpClientExtension myClient = new HttpClientExtension();
	private final HashMapResourceProvider<Patient> myPatientProvider = new HashMapResourceProvider<>(ourCtx, Patient.class);
	private final ObservationHashMapResourceProvider myObservationProvider = new ObservationHashMapResourceProvider();
	private final ConsentInterceptor myConsentInterceptor = new ConsentInterceptor()
		.registerConsentService(new SearchNarrowingConsentService());
	@Mock
	private IValidationSupport myValidationSupport;
	private int myPort;
	private List<IAuthRule> myAuthorizationInterceptorRuleList;
	private final AuthorizationInterceptor myAuthorizationInterceptor = new AuthorizationInterceptor() {
		@Override
		public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
			assertNotNull(myAuthorizationInterceptorRuleList);
			return myAuthorizationInterceptorRuleList;
		}
	};
	private AuthorizedList mySearchNarrowingInterceptorAuthorizedList;
	private final SearchNarrowingInterceptor mySearchNarrowingInterceptor = new SearchNarrowingInterceptor() {
		@Override
		protected AuthorizedList buildAuthorizedList(RequestDetails theRequestDetails) {
			assertNotNull(mySearchNarrowingInterceptorAuthorizedList);
			return mySearchNarrowingInterceptorAuthorizedList;
		}
	};
	@RegisterExtension
	private RestfulServerExtension myServer = new RestfulServerExtension(ourCtx)
		.registerInterceptor(myAuthorizationInterceptor)
		.registerInterceptor(myConsentInterceptor)
		.registerInterceptor(mySearchNarrowingInterceptor)
		.registerProvider(myPatientProvider)
		.registerProvider(myObservationProvider);

	private IGenericClient myFhirClient;

	@BeforeEach
	public void before() {
		mySearchNarrowingInterceptor.setValidationSupport(myValidationSupport);
		myPort = myServer.getPort();
		myFhirClient = myServer.getFhirClient();
		myPatientProvider.clear();
		myObservationProvider.clear();
	}

	@Test
	public void testAllowOnlyCodeInValueSet_LargeCodeSystem() {

		// Setup

		mySearchNarrowingInterceptor.setPostFilterLargeValueSetThreshold(5);
		when(myValidationSupport.fetchValueSet(eq(VALUESET_1_URL))).thenReturn(createValueSet(10));

		Observation obs0 = new Observation();
		obs0.setId("Observation/O0");
		obs0.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(CODE_PREFIX + 1);
		myObservationProvider.store(obs0);

		Observation obs1 = new Observation();
		obs1.setId("Observation/O1");
		obs1.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(CODE_PREFIX + 99);
		myObservationProvider.store(obs1);

		// Execute

		Bundle response = myFhirClient
			.search()
			.forResource(Observation.class)
			.returnBundle(Bundle.class)
			.execute();

		// Verify

		assertEquals(1, myObservationProvider.getRequestParams().size());
		assertEquals(true, myObservationProvider.getRequestParams().get(0).isEmpty());
		assertThat(toUnqualifiedVersionlessIdValues(response), contains("Observation/O0"));

	}

	@Nonnull
	private ValueSet createValueSet(int valueSetSize) {
		ValueSet vs = new ValueSet();
		vs.setUrl(VALUESET_1_URL);
		for (int i = 0; i < valueSetSize; i++) {
			vs.getExpansion().addContains().setSystem(CODESYSTEM_URL).setCode(CODE_PREFIX + i);
		}
		return vs;
	}


	private static class ObservationHashMapResourceProvider extends HashMapResourceProvider<Observation> {
		private List<Map<String, String[]>> myRequestParams = new ArrayList<>();

		public ObservationHashMapResourceProvider() {
			super(ourCtx, Observation.class);
		}

		public List<Map<String, String[]>> getRequestParams() {
			return myRequestParams;
		}

		@Override
		public synchronized void clear() {
			super.clear();
			myRequestParams.clear();
		}

		@Search(allowUnknownParams = true)
		@Override
		public synchronized List<IBaseResource> searchAll(RequestDetails theRequestDetails) {
			myRequestParams.add(theRequestDetails.getParameters());
			return super.searchAll(theRequestDetails);
		}
	}
}
