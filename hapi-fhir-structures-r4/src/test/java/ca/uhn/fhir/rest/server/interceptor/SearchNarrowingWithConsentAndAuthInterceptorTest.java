package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizedList;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.rest.server.interceptor.auth.SearchNarrowingConsentService;
import ca.uhn.fhir.rest.server.interceptor.auth.SearchNarrowingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentInterceptor;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
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
import java.util.Set;

import static ca.uhn.fhir.test.utilities.SearchTestUtil.toUnqualifiedVersionlessIdValues;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * A test to ensure that Search Narrowing with Consent Interceptor for post filtering and AuthorizationInterceptor for
 * final checking all works.
 */
@ExtendWith(MockitoExtension.class)
public class SearchNarrowingWithConsentAndAuthInterceptorTest {

	public static final String VALUESET_1_URL = "http://valueset-1";
	public static final String CODESYSTEM_URL = "http://codesystem-1";
	public static final String CODE_PREFIX = "code";
	public static final String nonMatchingCode = CODE_PREFIX + 99;
	public static final String matchingCode = CODE_PREFIX + 1;
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	@RegisterExtension
	private final HttpClientExtension myClient = new HttpClientExtension();
	private final ObservationHashMapResourceProvider myObservationProvider = new ObservationHashMapResourceProvider();
	private final MyPatientProvider myPatientProvider = new MyPatientProvider();
	private final ConsentInterceptor myConsentInterceptor = new ConsentInterceptor();
	@Mock
	private IValidationSupport myValidationSupport;
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
	private final RestfulServerExtension myServer = new RestfulServerExtension(ourCtx)
		.registerInterceptor(myAuthorizationInterceptor)
		.registerInterceptor(myConsentInterceptor)
		.registerInterceptor(mySearchNarrowingInterceptor)
		.registerProvider(myPatientProvider)
		.registerProvider(myObservationProvider);

	private IGenericClient myFhirClient;
	private List<IBaseResource> myNextPatientResponse;

	@BeforeEach
	public void before() {
		myConsentInterceptor.registerConsentService(new SearchNarrowingConsentService(myValidationSupport, ourCtx));
		myAuthorizationInterceptor.setValidationSupport(myValidationSupport);
		mySearchNarrowingInterceptor.setValidationSupport(myValidationSupport);
		myFhirClient = myServer.getFhirClient();
		myObservationProvider.clear();
		myNextPatientResponse = null;

		when(myValidationSupport.getFhirContext()).thenReturn(ourCtx);
	}

	@Test
	public void testSearch_AllowOnlyCodeInValueSet_LargeCodeSystem() {

		// Setup

		mySearchNarrowingInterceptorAuthorizedList = new AuthorizedList()
			.addCodeInValueSet("Observation", "code", VALUESET_1_URL);
		myAuthorizationInterceptorRuleList = new RuleBuilder()
			.allow().read().resourcesOfType("Observation").withCodeInValueSet("code", VALUESET_1_URL).andThen()
			.denyAll()
			.build();

		mySearchNarrowingInterceptor.setPostFilterLargeValueSetThreshold(5);
		when(myValidationSupport.expandValueSet(any(), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.ValueSetExpansionOutcome(createValueSet()));
		when(myValidationSupport.validateCode(any(), any(), eq(CODESYSTEM_URL), eq(matchingCode), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.CodeValidationResult()
			.setCode(matchingCode));
		when(myValidationSupport.validateCode(any(), any(), eq(CODESYSTEM_URL), eq(nonMatchingCode), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.CodeValidationResult()
			.setSeverity(IValidationSupport.IssueSeverity.ERROR)
			.setMessage("Code could not be found!"));

		Observation obs0 = new Observation();
		obs0.setId("Observation/O0");
		obs0.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(matchingCode);
		myObservationProvider.store(obs0);

		Observation obs1 = new Observation();
		obs1.setId("Observation/O1");
		obs1.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(nonMatchingCode);
		myObservationProvider.store(obs1);

		// Execute

		Bundle response = myFhirClient
			.search()
			.forResource(Observation.class)
			.returnBundle(Bundle.class)
			.execute();

		// Verify

		assertEquals(1, myObservationProvider.getRequestParams().size());
		assertTrue(myObservationProvider.getRequestParams().get(0).isEmpty(), myObservationProvider.getRequestParams().toString());
		assertThat(toUnqualifiedVersionlessIdValues(response), contains("Observation/O0"));

	}

	@Test
	public void testSearch_AllowOnlyCodeInValueSet_LargeCodeSystem_MultipleCodesWithOnlySomeInValueSet() {

		// Setup

		mySearchNarrowingInterceptorAuthorizedList = new AuthorizedList()
			.addCodeInValueSet("Observation", "code", VALUESET_1_URL);
		myAuthorizationInterceptorRuleList = new RuleBuilder()
			.allow().read().resourcesOfType("Observation").withCodeInValueSet("code", VALUESET_1_URL).andThen()
			.denyAll()
			.build();

		mySearchNarrowingInterceptor.setPostFilterLargeValueSetThreshold(5);
		when(myValidationSupport.expandValueSet(any(), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.ValueSetExpansionOutcome(createValueSet()));
		when(myValidationSupport.validateCode(any(), any(), eq(CODESYSTEM_URL), eq(matchingCode), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.CodeValidationResult()
			.setCode(matchingCode));
		when(myValidationSupport.validateCode(any(), any(), eq(CODESYSTEM_URL), eq(nonMatchingCode), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.CodeValidationResult()
			.setSeverity(IValidationSupport.IssueSeverity.ERROR)
			.setMessage("Code could not be found!"));

		Observation obs0 = new Observation();
		obs0.setId("Observation/O0");
		obs0.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(matchingCode);
		obs0.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(nonMatchingCode);
		myObservationProvider.store(obs0);

		Observation obs1 = new Observation();
		obs1.setId("Observation/O1");
		obs1.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(nonMatchingCode);
		myObservationProvider.store(obs1);

		// Execute

		Bundle response = myFhirClient
			.search()
			.forResource(Observation.class)
			.returnBundle(Bundle.class)
			.execute();

		// Verify

		assertEquals(1, myObservationProvider.getRequestParams().size());
		assertTrue(myObservationProvider.getRequestParams().get(0).isEmpty(), myObservationProvider.getRequestParams().toString());
		assertThat(toUnqualifiedVersionlessIdValues(response), contains("Observation/O0"));

	}

	@Test
	public void testSearch_AllowOnlyCodeNotInValueSet_LargeCodeSystem() {

		// Setup

		mySearchNarrowingInterceptorAuthorizedList = new AuthorizedList()
			.addCodeNotInValueSet("Observation", "code", VALUESET_1_URL);
		myAuthorizationInterceptorRuleList = new RuleBuilder()
			.allow().read().resourcesOfType("Observation").withCodeNotInValueSet("code", VALUESET_1_URL).andThen()
			.deny().read().resourcesOfType("Observation").withCodeInValueSet("code", VALUESET_1_URL).andThen()
			.denyAll()
			.build();

		mySearchNarrowingInterceptor.setPostFilterLargeValueSetThreshold(5);
		when(myValidationSupport.expandValueSet(any(), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.ValueSetExpansionOutcome(createValueSet()));
		when(myValidationSupport.validateCode(any(), any(), eq(CODESYSTEM_URL), eq(matchingCode), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.CodeValidationResult()
			.setCode(matchingCode));
		when(myValidationSupport.validateCode(any(), any(), eq(CODESYSTEM_URL), eq(nonMatchingCode), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.CodeValidationResult()
			.setSeverity(IValidationSupport.IssueSeverity.ERROR)
			.setMessage("Code could not be found!"));

		Observation obs0 = new Observation();
		obs0.setId("Observation/O0");
		obs0.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(matchingCode);
		myObservationProvider.store(obs0);

		Observation obs1 = new Observation();
		obs1.setId("Observation/O1");
		obs1.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(nonMatchingCode);
		myObservationProvider.store(obs1);

		// Execute

		Bundle response = myFhirClient
			.search()
			.forResource(Observation.class)
			.returnBundle(Bundle.class)
			.execute();

		// Verify

		assertEquals(1, myObservationProvider.getRequestParams().size());
		assertTrue(myObservationProvider.getRequestParams().get(0).isEmpty(), myObservationProvider.getRequestParams().toString());
		assertThat(toUnqualifiedVersionlessIdValues(response), contains("Observation/O1"));

	}

	@Test
	public void testSearch_AllowOnlyCodeNotInValueSet_LargeCodeSystem_MultipleCodesWithOnlySomeInValueSet() {

		// Setup

		mySearchNarrowingInterceptorAuthorizedList = new AuthorizedList()
			.addCodeNotInValueSet("Observation", "code", VALUESET_1_URL);
		myAuthorizationInterceptorRuleList = new RuleBuilder()
			.allow().read().resourcesOfType("Observation").withCodeNotInValueSet("code", VALUESET_1_URL).andThen()
			.deny().read().resourcesOfType("Observation").withCodeInValueSet("code", VALUESET_1_URL).andThen()
			.denyAll()
			.build();

		mySearchNarrowingInterceptor.setPostFilterLargeValueSetThreshold(5);
		when(myValidationSupport.expandValueSet(any(), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.ValueSetExpansionOutcome(createValueSet()));
		when(myValidationSupport.validateCode(any(), any(), eq(CODESYSTEM_URL), eq(matchingCode), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.CodeValidationResult()
			.setCode(matchingCode));
		when(myValidationSupport.validateCode(any(), any(), eq(CODESYSTEM_URL), eq(nonMatchingCode), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.CodeValidationResult()
			.setSeverity(IValidationSupport.IssueSeverity.ERROR)
			.setMessage("Code could not be found!"));

		Observation obs0 = new Observation();
		obs0.setId("Observation/O0");
		obs0.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(matchingCode);
		obs0.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(nonMatchingCode);
		myObservationProvider.store(obs0);

		Observation obs1 = new Observation();
		obs1.setId("Observation/O1");
		obs1.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(nonMatchingCode);
		myObservationProvider.store(obs1);

		// Execute

		Bundle response = myFhirClient
			.search()
			.forResource(Observation.class)
			.returnBundle(Bundle.class)
			.execute();

		// Verify

		assertEquals(1, myObservationProvider.getRequestParams().size());
		assertTrue(myObservationProvider.getRequestParams().get(0).isEmpty(), myObservationProvider.getRequestParams().toString());
		assertThat(toUnqualifiedVersionlessIdValues(response), contains("Observation/O1"));

	}


	@Test
	public void testSearchWithRevInclude_AllowOnlyCodeInValueSet_LargeCodeSystem() {

		// Setup

		mySearchNarrowingInterceptorAuthorizedList = new AuthorizedList()
			.addCodeInValueSet("Observation", "code", VALUESET_1_URL);
		myAuthorizationInterceptorRuleList = new RuleBuilder()
			.allow().read().resourcesOfType("Observation").withCodeInValueSet("code", VALUESET_1_URL).andThen()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.denyAll()
			.build();

		mySearchNarrowingInterceptor.setPostFilterLargeValueSetThreshold(5);
		when(myValidationSupport.validateCode(any(), any(), eq(CODESYSTEM_URL), eq(matchingCode), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.CodeValidationResult()
			.setCode(matchingCode));
		when(myValidationSupport.validateCode(any(), any(), eq(CODESYSTEM_URL), eq(nonMatchingCode), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.CodeValidationResult()
			.setSeverity(IValidationSupport.IssueSeverity.ERROR)
			.setMessage("Code could not be found!"));

		myNextPatientResponse = new ArrayList<>();

		Patient p = new Patient();
		p.setId("Patient/P0");
		p.setActive(true);
		myNextPatientResponse.add(p);

		Observation obs0 = new Observation();
		obs0.setId("Observation/O0");
		obs0.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(matchingCode);
		myNextPatientResponse.add(obs0);

		Observation obs1 = new Observation();
		obs1.setId("Observation/O1");
		obs1.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(nonMatchingCode);
		myNextPatientResponse.add(obs1);

		// Execute

		Bundle response = myFhirClient
			.search()
			.forResource(Patient.class)
			.revInclude(Patient.INCLUDE_ALL)
			.returnBundle(Bundle.class)
			.execute();

		// Verify

		assertThat(toUnqualifiedVersionlessIdValues(response), contains("Patient/P0", "Observation/O0"));

	}

	@Test
	public void testEverythingOperation_AllowOnlyCodeInValueSet_LargeCodeSystem() {

		// Setup

		mySearchNarrowingInterceptorAuthorizedList = new AuthorizedList()
			.addCodeInValueSet("Observation", "code", VALUESET_1_URL);
		myAuthorizationInterceptorRuleList = new RuleBuilder()
			.allow().read().resourcesOfType("Observation").withCodeInValueSet("code", VALUESET_1_URL).andThen()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().operation().named("$everything").onAnyInstance().andRequireExplicitResponseAuthorization().andThen()
			.denyAll()
			.build();

		mySearchNarrowingInterceptor.setPostFilterLargeValueSetThreshold(5);
		when(myValidationSupport.validateCode(any(), any(), eq(CODESYSTEM_URL), eq(matchingCode), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.CodeValidationResult()
			.setCode(matchingCode));
		when(myValidationSupport.validateCode(any(), any(), eq(CODESYSTEM_URL), eq(nonMatchingCode), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.CodeValidationResult()
			.setSeverity(IValidationSupport.IssueSeverity.ERROR)
			.setMessage("Code could not be found!"));

		myNextPatientResponse = new ArrayList<>();

		Patient p = new Patient();
		p.setId("Patient/P0");
		p.setActive(true);
		myNextPatientResponse.add(p);

		Observation obs0 = new Observation();
		obs0.setId("Observation/O0");
		obs0.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(matchingCode);
		myNextPatientResponse.add(obs0);

		Observation obs1 = new Observation();
		obs1.setId("Observation/O1");
		obs1.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(nonMatchingCode);
		myNextPatientResponse.add(obs1);

		// Execute

		Bundle response = myFhirClient
			.operation()
			.onInstance(new IdType("Patient/P0"))
			.named("$everything")
			.withNoParameters(Parameters.class)
			.returnResourceType(Bundle.class)
			.execute();

		// Verify

		assertThat(toUnqualifiedVersionlessIdValues(response), contains("Patient/P0", "Observation/O0"));

	}

	@Test
	public void testEverythingOperation_AllowOnlyCodeInValueSet_NoNarrowing() {

		// Setup

		mySearchNarrowingInterceptorAuthorizedList = new AuthorizedList();
		myAuthorizationInterceptorRuleList = new RuleBuilder()
			.allow().read().resourcesOfType("Observation").withCodeInValueSet("code", VALUESET_1_URL).andThen()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().operation().named("$everything").onAnyInstance().andRequireExplicitResponseAuthorization().andThen()
			.denyAll()
			.build();

		mySearchNarrowingInterceptor.setPostFilterLargeValueSetThreshold(5);
		when(myValidationSupport.validateCode(any(), any(), eq(CODESYSTEM_URL), eq(matchingCode), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.CodeValidationResult()
			.setCode(matchingCode));
		when(myValidationSupport.validateCode(any(), any(), eq(CODESYSTEM_URL), eq(nonMatchingCode), any(), eq(VALUESET_1_URL))).thenReturn(new IValidationSupport.CodeValidationResult()
			.setSeverity(IValidationSupport.IssueSeverity.ERROR)
			.setMessage("Code could not be found!"));

		myNextPatientResponse = new ArrayList<>();

		Patient p = new Patient();
		p.setId("Patient/P0");
		p.setActive(true);
		myNextPatientResponse.add(p);

		Observation obs0 = new Observation();
		obs0.setId("Observation/O0");
		obs0.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(matchingCode);
		myNextPatientResponse.add(obs0);

		Observation obs1 = new Observation();
		obs1.setId("Observation/O1");
		obs1.getCode().addCoding().setSystem(CODESYSTEM_URL).setCode(nonMatchingCode);
		myNextPatientResponse.add(obs1);

		// Execute

		try {
			myFhirClient
				.operation()
				.onInstance(new IdType("Patient/P0"))
				.named("$everything")
				.withNoParameters(Parameters.class)
				.returnResourceType(Bundle.class)
				.execute();
			fail();
		} catch (ForbiddenOperationException e) {

			// Verify
			assertThat(e.getMessage(), containsString("Access denied by"));

		}


	}

	@Nonnull
	private ValueSet createValueSet() {
		ValueSet vs = new ValueSet();
		vs.setUrl(VALUESET_1_URL);
		for (int i = 0; i < 10; i++) {
			vs.getExpansion().addContains().setSystem(CODESYSTEM_URL).setCode(CODE_PREFIX + i);
		}
		return vs;
	}

	private class MyPatientProvider implements IResourceProvider {

		@Search(allowUnknownParams = true)
		public List<IBaseResource> searchAll(RequestDetails theRequestDetails, @IncludeParam(reverse = true) Set<Include> theRevIncludes) {
			assertNotNull(myNextPatientResponse);
			myNextPatientResponse = ServerInterceptorUtil.fireStoragePreshowResource(myNextPatientResponse, theRequestDetails, myServer.getRestfulServer().getInterceptorService());
			return myNextPatientResponse;
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}


		@Operation(name = "$everything")
		public IBundleProvider everything(RequestDetails theRequestDetails, @IdParam IdType theId) {
			assertNotNull(myNextPatientResponse);
			myNextPatientResponse = ServerInterceptorUtil.fireStoragePreshowResource(myNextPatientResponse, theRequestDetails, myServer.getRestfulServer().getInterceptorService());
			return new SimpleBundleProvider(myNextPatientResponse);
		}

	}


	private static class ObservationHashMapResourceProvider extends HashMapResourceProvider<Observation> {
		private final List<Map<String, String[]>> myRequestParams = new ArrayList<>();

		public ObservationHashMapResourceProvider() {
			super(ourCtx, Observation.class);
		}

		public List<Map<String, String[]>> getRequestParams() {
			return myRequestParams;
		}

		@Override
		public synchronized void clear() {
			super.clear();
			if (myRequestParams != null) {
				myRequestParams.clear();
			}
		}

		@Search(allowUnknownParams = true)
		@Override
		public synchronized List<IBaseResource> searchAll(RequestDetails theRequestDetails) {
			myRequestParams.add(theRequestDetails.getParameters());
			return super.searchAll(theRequestDetails);
		}
	}


}
