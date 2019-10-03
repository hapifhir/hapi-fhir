package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.param.BaseAndListParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.TestUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import ca.uhn.fhir.test.utilities.JettyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static AuthorizedList ourNextCompartmentList;
	private static Bundle.BundleEntryRequestComponent ourLastBundleRequest;


	@Before
	public void before() {
		ourLastHitMethod = null;
		ourReturn = Collections.emptyList();
		ourLastIdParam = null;
		ourLastNameParam = null;
		ourLastSubjectParam = null;
		ourLastPatientParam = null;
		ourLastPerformerParam = null;
		ourLastCodeParam = null;
		ourNextCompartmentList = null;
	}

	@Test
	public void testReturnNull() {

		ourNextCompartmentList = null;

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
	public void testNarrowObservationsByPatientContext_ClientRequestedNoParams() {

		ourNextCompartmentList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

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
	public void testNarrowObservationsByPatientContext_ClientRequestedBundleNoParams() {

		ourNextCompartmentList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

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

	/**
	 * Should not make any changes
	 */
	@Test
	public void testNarrowObservationsByPatientResources_ClientRequestedNoParams() {

		ourNextCompartmentList = new AuthorizedList().addResources("Patient/123", "Patient/456");

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
	public void testNarrowPatientByPatientResources_ClientRequestedNoParams() {

		ourNextCompartmentList = new AuthorizedList().addResources("Patient/123", "Patient/456");

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

	@Test
	public void testNarrowPatientByPatientContext_ClientRequestedNoParams() {

		ourNextCompartmentList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		ourClient
			.search()
			.forResource("Patient")
			.execute();

		assertEquals("Patient.search", ourLastHitMethod);
		assertNull(ourLastNameParam);
		assertThat(toStrings(ourLastIdParam), Matchers.contains("Patient/123,Patient/456"));
	}

	@Test
	public void testNarrowPatientByPatientContext_ClientRequestedSomeOverlap() {

		ourNextCompartmentList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

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
	public void testNarrowObservationsByPatientContext_ClientRequestedSomeOverlap() {

		ourNextCompartmentList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

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
	public void testNarrowObservationsByPatientContext_ClientRequestedNoOverlap() {

		ourNextCompartmentList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		ourClient
			.search()
			.forResource("Observation")
			.where(Observation.PATIENT.hasAnyOfIds("Patient/111", "Patient/777"))
			.and(Observation.PATIENT.hasAnyOfIds("Patient/111", "Patient/888"))
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertNull(ourLastIdParam);
		assertNull(ourLastCodeParam);
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertThat(toStrings(ourLastPatientParam), Matchers.contains("Patient/111,Patient/777", "Patient/111,Patient/888", "Patient/123,Patient/456"));
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
			@OptionalParam(name = "code") TokenAndListParam theCodeParam
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
			if (ourNextCompartmentList == null) {
				return null;
			}
			return ourNextCompartmentList;
		}
	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
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
