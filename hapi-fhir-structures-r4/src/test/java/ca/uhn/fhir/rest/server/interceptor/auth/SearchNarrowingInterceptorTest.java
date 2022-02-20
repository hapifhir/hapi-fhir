package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.param.BaseAndListParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.UrlUtil.escapeUrlParam;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class SearchNarrowingInterceptorTest {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchNarrowingInterceptorTest.class);

	private static String ourLastHitMethod;
	private static FhirContext ourCtx;
	private static TokenAndListParam ourLastIdParam;
	private static TokenAndListParam ourLastCodeParam;
	private static ReferenceAndListParam ourLastSubjectParam;
	private static ReferenceAndListParam ourLastPatientParam;
	private static ReferenceAndListParam ourLastPerformerParam;
	private static StringAndListParam ourLastNameParam;
	private static List<Resource> ourReturn;
	private static Server ourServer;
	private static IGenericClient ourClient;
	private static AuthorizedList ourNextAuthorizedList;
	private static Bundle.BundleEntryRequestComponent ourLastBundleRequest;


	@BeforeEach
	public void before() {
		ourLastHitMethod = null;
		ourReturn = Collections.emptyList();
		ourLastIdParam = null;
		ourLastNameParam = null;
		ourLastSubjectParam = null;
		ourLastPatientParam = null;
		ourLastPerformerParam = null;
		ourLastCodeParam = null;
		ourNextAuthorizedList = null;
	}

	@Test
	public void testReturnNull() {

		ourNextAuthorizedList = null;

		ourClient
			.search()
			.forResource("Patient")
			.execute();

		assertEquals("Patient.search", ourLastHitMethod);
		assertNull(ourLastCodeParam);
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
		assertNull(ourLastIdParam);
	}

	@Test
	public void testNarrowCode_NotInSelected_ClientRequestedNoParams() {
		ourNextAuthorizedList = new AuthorizedList()
			.addCodeNotInValueSet("Observation", "code", "http://myvs");

		ourClient
			.search()
			.forResource("Observation")
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertEquals(1, ourLastCodeParam.size());
		assertEquals(1, ourLastCodeParam.getValuesAsQueryTokens().get(0).size());
		assertEquals(TokenParamModifier.NOT_IN, ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getModifier());
		assertEquals("http://myvs", ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
		assertEquals(null, ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getSystem());
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
		assertNull(ourLastIdParam);
	}


	@Test
	public void testNarrowCode_InSelected_ClientRequestedNoParams() {
		ourNextAuthorizedList = new AuthorizedList()
			.addCodeInValueSet("Observation", "code", "http://myvs");

		ourClient
			.search()
			.forResource("Observation")
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertEquals(1, ourLastCodeParam.size());
		assertEquals(1, ourLastCodeParam.getValuesAsQueryTokens().get(0).size());
		assertEquals(TokenParamModifier.IN, ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getModifier());
		assertEquals("http://myvs", ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
		assertEquals(null, ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getSystem());
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
		assertNull(ourLastIdParam);
	}

	@Test
	public void testNarrowCode_InSelected_ClientRequestedNoParams_LargeValueSet() {
		ourNextAuthorizedList = new AuthorizedList()
			.addCodeInValueSet("Observation", "code", "http://large-vs");

		ourClient
			.search()
			.forResource("Observation")
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertEquals(1, ourLastCodeParam.size());
		assertEquals(1, ourLastCodeParam.getValuesAsQueryTokens().get(0).size());
		assertEquals(TokenParamModifier.IN, ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getModifier());
		assertEquals("http://myvs", ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
		assertEquals(null, ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getSystem());
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
		assertNull(ourLastIdParam);
	}

	@Test
	public void testNarrowCode_InSelected_ClientRequestedBundleWithNoParams() {
		ourNextAuthorizedList = new AuthorizedList()
			.addCodeInValueSet("Observation", "code", "http://myvs");

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.GET).setUrl("Observation?subject=Patient/123");
		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		ourClient
			.transaction()
			.withBundle(bundle)
			.execute();

		assertEquals("transaction", ourLastHitMethod);
		String expectedUrl = "Observation?" +
			escapeUrlParam("code:in") +
			"=" +
			escapeUrlParam("http://myvs") +
			"&subject=" +
			escapeUrlParam("Patient/123");
		assertEquals(expectedUrl, ourLastBundleRequest.getUrl());

	}

	@Test
	public void testNarrowCode_InSelected_ClientRequestedOtherInParam() {
		ourNextAuthorizedList = new AuthorizedList()
			.addCodeInValueSet("Observation", "code", "http://myvs");

		ourClient.registerInterceptor(new LoggingInterceptor(false));
		ourClient
			.search()
			.forResource("Observation")
			.where(singletonMap("code", singletonList(new TokenParam("http://othervs").setModifier(TokenParamModifier.IN))))
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertEquals(2, ourLastCodeParam.size());
		assertEquals(1, ourLastCodeParam.getValuesAsQueryTokens().get(0).size());
		assertEquals(TokenParamModifier.IN, ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getModifier());
		assertEquals("http://othervs", ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
		assertEquals(null, ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getSystem());
		assertEquals(1, ourLastCodeParam.getValuesAsQueryTokens().get(1).size());
		assertEquals(TokenParamModifier.IN, ourLastCodeParam.getValuesAsQueryTokens().get(1).getValuesAsQueryTokens().get(0).getModifier());
		assertEquals("http://myvs", ourLastCodeParam.getValuesAsQueryTokens().get(1).getValuesAsQueryTokens().get(0).getValue());
		assertEquals(null, ourLastCodeParam.getValuesAsQueryTokens().get(1).getValuesAsQueryTokens().get(0).getSystem());
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
		assertNull(ourLastIdParam);
	}

	@Test
	public void testNarrowCode_InSelected_DifferentResource() {
		ourNextAuthorizedList = new AuthorizedList()
			.addCodeInValueSet("Procedure", "code", "http://myvs");

		ourClient
			.search()
			.forResource("Observation")
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertEquals(null, ourLastCodeParam);
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedNoParams() {
		ourNextAuthorizedList = new AuthorizedList()
			.addCompartments("Patient/123", "Patient/456");

		ourClient
			.search()
			.forResource("Observation")
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertNull(ourLastIdParam);
		assertNull(ourLastCodeParam);
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertThat(toStrings(ourLastPatientParam), Matchers.contains("Patient/123,Patient/456"));
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedBundleNoParams() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.GET).setUrl("Patient");
		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		ourClient
			.transaction()
			.withBundle(bundle)
			.execute();

		assertEquals("transaction", ourLastHitMethod);
		assertEquals("Patient?_id=" + URLEncoder.encode("Patient/123,Patient/456"), ourLastBundleRequest.getUrl());
	}

	@Test
	public void testNarrowCompartment_PatientByPatientContext_ClientRequestedNoParams() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		ourClient
			.search()
			.forResource("Patient")
			.execute();

		assertEquals("Patient.search", ourLastHitMethod);
		assertNull(ourLastNameParam);
		assertThat(toStrings(ourLastIdParam), Matchers.contains("Patient/123,Patient/456"));
	}

	@Test
	public void testNarrowCompartment_PatientByPatientContext_ClientRequestedSomeOverlap() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		ourClient
			.search()
			.forResource("Patient")
			.where(IAnyResource.RES_ID.exactly().codes("Patient/123", "Patient/999"))
			.execute();

		assertEquals("Patient.search", ourLastHitMethod);
		assertNull(ourLastNameParam);
		assertThat(toStrings(ourLastIdParam), Matchers.contains("Patient/123"));
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedSomeOverlap() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		ourClient
			.search()
			.forResource("Observation")
			.where(Observation.PATIENT.hasAnyOfIds("Patient/456", "Patient/777"))
			.and(Observation.PATIENT.hasAnyOfIds("Patient/456", "Patient/888"))
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertNull(ourLastIdParam);
		assertNull(ourLastCodeParam);
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertThat(toStrings(ourLastPatientParam), Matchers.contains("Patient/456", "Patient/456"));
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedSomeOverlap_ShortIds() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		ourClient
			.search()
			.forResource("Observation")
			.where(Observation.PATIENT.hasAnyOfIds("456", "777"))
			.and(Observation.PATIENT.hasAnyOfIds("456", "888"))
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertNull(ourLastIdParam);
		assertNull(ourLastCodeParam);
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertThat(toStrings(ourLastPatientParam), Matchers.contains("456", "456"));
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedSomeOverlap_UseSynonym() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		ourClient
			.search()
			.forResource("Observation")
			.where(Observation.SUBJECT.hasAnyOfIds("Patient/456", "Patient/777"))
			.and(Observation.SUBJECT.hasAnyOfIds("Patient/456", "Patient/888"))
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertNull(ourLastIdParam);
		assertNull(ourLastCodeParam);
		assertThat(toStrings(ourLastSubjectParam), Matchers.contains("Patient/456", "Patient/456"));
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedNoOverlap() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		try {
			ourClient
				.search()
				.forResource("Observation")
				.where(Observation.PATIENT.hasAnyOfIds("Patient/111", "Patient/777"))
				.and(Observation.PATIENT.hasAnyOfIds("Patient/111", "Patient/888"))
				.execute();

			fail("Expected a 403 error");
		} catch (ForbiddenOperationException e) {
			assertEquals(Constants.STATUS_HTTP_403_FORBIDDEN, e.getStatusCode());
		}

		assertNull(ourLastHitMethod);
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedNoOverlap_UseSynonym() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		try {
			ourClient
				.search()
				.forResource("Observation")
				.where(Observation.SUBJECT.hasAnyOfIds("Patient/111", "Patient/777"))
				.and(Observation.SUBJECT.hasAnyOfIds("Patient/111", "Patient/888"))
				.execute();

			fail("Expected a 403 error");
		} catch (ForbiddenOperationException e) {
			assertEquals(Constants.STATUS_HTTP_403_FORBIDDEN, e.getStatusCode());
		}

		assertNull(ourLastHitMethod);
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedBadParameter() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		try {
			ourClient
				.search()
				.forResource("Observation")
				.where(Observation.PATIENT.hasAnyOfIds("Patient/"))
				.execute();

			fail("Expected a 403 error");
		} catch (ForbiddenOperationException e) {
			assertEquals(Constants.STATUS_HTTP_403_FORBIDDEN, e.getStatusCode());
		}

		assertNull(ourLastHitMethod);
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedBadPermission() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/");

		try {
			ourClient
				.search()
				.forResource("Observation")
				.where(Observation.PATIENT.hasAnyOfIds("Patient/111", "Patient/777"))
				.execute();

			fail("Expected a 403 error");
		} catch (ForbiddenOperationException e) {
			assertEquals(Constants.STATUS_HTTP_403_FORBIDDEN, e.getStatusCode());
		}

		assertNull(ourLastHitMethod);
	}

	/**
	 * Should not make any changes
	 */
	@Test
	public void testNarrowResources_ObservationsByPatientResources_ClientRequestedNoParams() {
		ourNextAuthorizedList = new AuthorizedList()
			.addResources("Patient/123", "Patient/456");

		ourClient
			.search()
			.forResource("Observation")
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertNull(ourLastIdParam);
		assertNull(ourLastCodeParam);
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
	}

	@Test
	public void testNarrowResources_PatientByPatientResources_ClientRequestedNoParams() {
		ourNextAuthorizedList = new AuthorizedList()
			.addResources("Patient/123", "Patient/456");

		ourClient
			.search()
			.forResource("Patient")
			.execute();

		assertEquals("Patient.search", ourLastHitMethod);
		assertNull(ourLastCodeParam);
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
		assertThat(toStrings(ourLastIdParam), Matchers.contains("Patient/123,Patient/456"));
	}

	private List<String> toStrings(BaseAndListParam<? extends IQueryParameterOr<?>> theParams) {
		List<? extends IQueryParameterOr<? extends IQueryParameterType>> valuesAsQueryTokens = theParams.getValuesAsQueryTokens();

		return valuesAsQueryTokens
			.stream()
			.map(IQueryParameterOr::getValuesAsQueryTokens)
			.map(t -> t
				.stream()
				.map(j -> j.getValueAsQueryToken(ourCtx))
				.collect(Collectors.joining(",")))
			.collect(Collectors.toList());
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Search()
		public List<Resource> search(
			@OptionalParam(name = "_id") TokenAndListParam theIdParam,
			@OptionalParam(name = "name") StringAndListParam theNameParam
		) {
			ourLastHitMethod = "Patient.search";
			ourLastIdParam = theIdParam;
			ourLastNameParam = theNameParam;
			return ourReturn;
		}

	}

	public static class DummyObservationResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}


		@Search()
		public List<Resource> search(
			@OptionalParam(name = "_id") TokenAndListParam theIdParam,
			@OptionalParam(name = Observation.SP_SUBJECT) ReferenceAndListParam theSubjectParam,
			@OptionalParam(name = Observation.SP_PATIENT) ReferenceAndListParam thePatientParam,
			@OptionalParam(name = Observation.SP_PERFORMER) ReferenceAndListParam thePerformerParam,
			@OptionalParam(name = Observation.SP_CODE) TokenAndListParam theCodeParam
		) {
			ourLastHitMethod = "Observation.search";
			ourLastIdParam = theIdParam;
			ourLastSubjectParam = theSubjectParam;
			ourLastPatientParam = thePatientParam;
			ourLastPerformerParam = thePerformerParam;
			ourLastCodeParam = theCodeParam;
			return ourReturn;
		}

	}

	public static class DummySystemProvider {
		@Transaction
		public Bundle transaction(@TransactionParam Bundle theInput) {
			ourLastHitMethod = "transaction";
			ourLastBundleRequest = theInput.getEntry().get(0).getRequest();
			return theInput;
		}
	}

	private static class MySearchNarrowingInterceptor extends SearchNarrowingInterceptor {
		@Override
		protected AuthorizedList buildAuthorizedList(RequestDetails theRequestDetails) {
			if (ourNextAuthorizedList == null) {
				return null;
			}
			return ourNextAuthorizedList;
		}
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourCtx = FhirContext.forR4();

		ourServer = new Server(0);

		DummyPatientResourceProvider patProvider = new DummyPatientResourceProvider();
		DummyObservationResourceProvider obsProv = new DummyObservationResourceProvider();
		DummySystemProvider systemProv = new DummySystemProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer ourServlet = new RestfulServer(ourCtx);
		ourServlet.setFhirContext(ourCtx);
		ourServlet.registerProviders(systemProv);
		ourServlet.setResourceProviders(patProvider, obsProv);
		ourServlet.setPagingProvider(new FifoMemoryPagingProvider(100));
		ourServlet.registerInterceptor(new MySearchNarrowingInterceptor());
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        int ourPort = JettyUtil.getPortForStartedServer(ourServer);

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ourCtx.getRestfulClientFactory().setSocketTimeout(1000000);
		ourClient = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort);
	}


}
