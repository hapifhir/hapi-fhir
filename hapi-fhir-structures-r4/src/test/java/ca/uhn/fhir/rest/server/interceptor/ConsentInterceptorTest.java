package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentInterceptor;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentOperationStatusEnum;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentOutcome;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentService;
import ca.uhn.fhir.test.utilities.JettyUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//import static org.hamcrest.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ConsentInterceptorTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ConsentInterceptorTest.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static int ourPort;
	private static RestfulServer ourServlet;
	private static Server ourServer;
	private static List<IBaseResource> ourNextReturnList;
	@Mock
	private IConsentService myConsentSvc;
	private ConsentInterceptor myInterceptor;
	@Captor
	private ArgumentCaptor<BaseServerResponseException> myExceptionCaptor;

	@After
	public void after() {
		ourServlet.unregisterInterceptor(myInterceptor);
	}

	@Before
	public void before() {
		myInterceptor = new ConsentInterceptor(myConsentSvc);
		ourServlet.registerInterceptor(myInterceptor);
		ourNextReturnList = new ArrayList<>();
	}

	@Test
	public void testOutcomeSuccess() throws IOException {
		when(myConsentSvc.startOperation(any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.seeResource(any(), any())).thenReturn(ConsentOutcome.PROCEED);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?searchReturnNormal=1");

		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
		}

		verify(myConsentSvc, times(1)).completeOperationSuccess(any());
		verify(myConsentSvc, times(0)).completeOperationFailure(any(), any());
	}

	@Test
	public void testSeeResourceAuthorizesOuterBundle() throws IOException {
		ourNextReturnList.add(new Patient().setActive(true).setId("PTA"));
		ourNextReturnList.add(new Patient().setActive(false).setId("PTB"));

		when(myConsentSvc.startOperation(any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.seeResource(any(RequestDetails.class), any(IBaseResource.class))).thenAnswer(t-> ConsentOutcome.AUTHORIZED);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?searchReturnList=1");

		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			assertThat(responseContent, containsString("PTA"));
		}

		verify(myConsentSvc, times(1)).startOperation(any());
		verify(myConsentSvc, times(1)).seeResource(any(), any());
		verify(myConsentSvc, times(1)).completeOperationSuccess(any());
		verify(myConsentSvc, times(0)).completeOperationFailure(any(), any());
		verifyNoMoreInteractions(myConsentSvc);
	}


	@Test
	public void testSeeResourceRejectsOuterBundle_ProvidesOperationOutcome() throws IOException {
		ourNextReturnList.add(new Patient().setActive(true).setId("PTA"));
		ourNextReturnList.add(new Patient().setActive(false).setId("PTB"));

		when(myConsentSvc.startOperation(any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.seeResource(any(RequestDetails.class), any(IBaseResource.class))).thenAnswer(t->{
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics("A DIAG");
			return new ConsentOutcome(ConsentOperationStatusEnum.REJECT, oo);
		});

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?searchReturnList=1");

		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			assertThat(responseContent, containsString("A DIAG"));
		}

		verify(myConsentSvc, times(1)).startOperation(any());
		verify(myConsentSvc, times(1)).seeResource(any(), any());
		verify(myConsentSvc, times(1)).completeOperationSuccess(any());
		verify(myConsentSvc, times(0)).completeOperationFailure(any(), any());
		verifyNoMoreInteractions(myConsentSvc);
	}

	@Test
	public void testSeeResourceRejectsOuterBundle_ProvidesNothing() throws IOException {
		ourNextReturnList.add(new Patient().setActive(true).setId("PTA"));
		ourNextReturnList.add(new Patient().setActive(false).setId("PTB"));

		when(myConsentSvc.startOperation(any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.seeResource(any(RequestDetails.class), any(IBaseResource.class))).thenAnswer(t-> ConsentOutcome.REJECT);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?searchReturnList=1");

		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(204, status.getStatusLine().getStatusCode());
			assertNull(status.getEntity());
			assertNull(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE));
		}

		verify(myConsentSvc, times(1)).startOperation(any());
		verify(myConsentSvc, times(1)).seeResource(any(), any());
		verify(myConsentSvc, times(1)).completeOperationSuccess(any());
		verify(myConsentSvc, times(0)).completeOperationFailure(any(), any());
		verifyNoMoreInteractions(myConsentSvc);
	}

	@Test
	public void testSeeResourceRejectsInnerResource_ProvidesOperationOutcome() throws IOException {
		ourNextReturnList.add(new Patient().setActive(true).setId("PTA"));
		ourNextReturnList.add(new Patient().setActive(false).setId("PTB"));

		when(myConsentSvc.startOperation(any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.seeResource(any(RequestDetails.class), any(IBaseResource.class))).thenAnswer(t->{
			IBaseResource resource = (IBaseResource) t.getArguments()[1];
			if (resource == ourNextReturnList.get(0)) {
				OperationOutcome oo = new OperationOutcome();
				oo.addIssue().setDiagnostics("A DIAG");
				return new ConsentOutcome(ConsentOperationStatusEnum.REJECT, oo);
			}
			return ConsentOutcome.PROCEED;
		});

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?searchReturnList=1");

		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			Bundle resoonse = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
			assertEquals(OperationOutcome.class, resoonse.getEntry().get(0).getResource().getClass());
			assertEquals("A DIAG", ((OperationOutcome)resoonse.getEntry().get(0).getResource()).getIssue().get(0).getDiagnostics());
			assertEquals(Patient.class, resoonse.getEntry().get(1).getResource().getClass());
			assertEquals("PTB", resoonse.getEntry().get(1).getResource().getIdElement().getIdPart());
		}

		verify(myConsentSvc, times(1)).startOperation(any());
		verify(myConsentSvc, times(3)).seeResource(any(), any());
		verify(myConsentSvc, times(1)).completeOperationSuccess(any());
		verify(myConsentSvc, times(0)).completeOperationFailure(any(), any());
		verifyNoMoreInteractions(myConsentSvc);
	}

	@Test
	public void testSeeResourceReplacesInnerResource() throws IOException {
		ourNextReturnList.add(new Patient().setActive(true).setId("PTA"));
		ourNextReturnList.add(new Patient().setActive(false).setId("PTB"));

		when(myConsentSvc.startOperation(any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.seeResource(any(RequestDetails.class), any(IBaseResource.class))).thenAnswer(t->{
			IBaseResource resource = (IBaseResource) t.getArguments()[1];
			if (resource == ourNextReturnList.get(0)) {
				Patient replacement = new Patient();
				replacement.setId("PTA");
				replacement.addIdentifier().setSystem("REPLACEMENT");
				return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED, replacement);
			}
			return ConsentOutcome.PROCEED;
		});

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?searchReturnList=1");

		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			Bundle resoonse = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
			assertEquals(Patient.class, resoonse.getEntry().get(0).getResource().getClass());
			assertEquals("PTA", resoonse.getEntry().get(0).getResource().getIdElement().getIdPart());
			assertEquals("REPLACEMENT", ((Patient)resoonse.getEntry().get(0).getResource()).getIdentifierFirstRep().getSystem());
			assertEquals(Patient.class, resoonse.getEntry().get(1).getResource().getClass());
			assertEquals("PTB", resoonse.getEntry().get(1).getResource().getIdElement().getIdPart());
		}

		verify(myConsentSvc, times(1)).startOperation(any());
		verify(myConsentSvc, times(3)).seeResource(any(), any());
		verify(myConsentSvc, times(1)).completeOperationSuccess(any());
		verify(myConsentSvc, times(0)).completeOperationFailure(any(), any());
		verifyNoMoreInteractions(myConsentSvc);
	}

	@Test
	public void testSeeResourceModifiesInnerResource() throws IOException {
		ourNextReturnList.add(new Patient().setActive(true).setId("PTA"));
		ourNextReturnList.add(new Patient().setActive(false).setId("PTB"));

		when(myConsentSvc.startOperation(any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.seeResource(any(RequestDetails.class), any(IBaseResource.class))).thenAnswer(t->{
			IBaseResource resource = (IBaseResource) t.getArguments()[1];
			if (resource == ourNextReturnList.get(0)) {
				((Patient)resource).addIdentifier().setSystem("REPLACEMENT");
			}
			return ConsentOutcome.PROCEED;
		});

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?searchReturnList=1");

		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			Bundle resoonse = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
			assertEquals(Patient.class, resoonse.getEntry().get(0).getResource().getClass());
			assertEquals("PTA", resoonse.getEntry().get(0).getResource().getIdElement().getIdPart());
			assertEquals("REPLACEMENT", ((Patient)resoonse.getEntry().get(0).getResource()).getIdentifierFirstRep().getSystem());
			assertEquals(Patient.class, resoonse.getEntry().get(1).getResource().getClass());
			assertEquals("PTB", resoonse.getEntry().get(1).getResource().getIdElement().getIdPart());
		}

		verify(myConsentSvc, times(1)).startOperation(any());
		verify(myConsentSvc, times(3)).seeResource(any(), any());
		verify(myConsentSvc, times(1)).completeOperationSuccess(any());
		verify(myConsentSvc, times(0)).completeOperationFailure(any(), any());
		verifyNoMoreInteractions(myConsentSvc);
	}

	@Test
	public void testOutcomeException() throws IOException {
		when(myConsentSvc.startOperation(any())).thenReturn(ConsentOutcome.PROCEED);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?searchThrowNullPointerException=1");

		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(500, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
		}

		verify(myConsentSvc, times(0)).completeOperationSuccess(any());
		verify(myConsentSvc, times(1)).completeOperationFailure(any(), myExceptionCaptor.capture());

		assertEquals("Failed to call access method: java.lang.NullPointerException: A MESSAGE", myExceptionCaptor.getValue().getMessage());
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {


		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		/*
		 * searchThrowNullPointerException
		 */
		@Search
		public List<IBaseResource> searchWithWildcardRetVal(@RequiredParam(name = "searchThrowNullPointerException") StringParam theValue) {
			throw new NullPointerException("A MESSAGE");
		}

		/**
		 * searchReturnNormal
		 */
		@Search
		public List<IBaseResource> searchReturnNormal(@RequiredParam(name = "searchReturnNormal") StringParam theParam) {
			Patient patientA = new Patient();
			patientA.setId("Patient/A");
			patientA.setActive(true);
			patientA.addName().setFamily("FAMILY").addGiven("GIVEN");
			patientA.addIdentifier().setSystem("SYSTEM").setValue("VALUEA");

			Patient patientB = new Patient();
			patientB.setId("Patient/B");
			patientB.setActive(true);
			patientB.addName().setFamily("FAMILY").addGiven("GIVEN");
			patientB.addIdentifier().setSystem("SYSTEM").setValue("VALUEB");

			return Lists.newArrayList(patientB);
		}

		/**
		 * searchReturnList
		 */
		@Search
		public List<IBaseResource> searchReturnList(@RequiredParam(name = "searchReturnList") StringParam theParam) {
			return ourNextReturnList;
		}

	}

	@AfterClass
	public static void afterClass() throws Exception {
		JettyUtil.closeServer(ourServer);
		ourClient.close();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler servletHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setDefaultPrettyPrint(true);
		ourServlet.setResourceProviders(patientProvider);
		ourServlet.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		servletHandler.addServletWithMapping(servletHolder, "/*");

		ourServer.setHandler(servletHandler);
		ourServer.start();
		ourPort = JettyUtil.getPortForStartedServer(ourServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

}
