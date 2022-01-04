package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ServerActionInterceptorTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static int ourPort;
	private static Server ourServer;
	private static IServerInterceptor ourInterceptor;
	private static IGenericClient ourFhirClient;

	@AfterAll
	public static void afterClassClearContext() throws Exception {
        JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}


	@Test
	public void testRead() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(ourInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.READ), detailsCapt.capture());

		ActionRequestDetails details = detailsCapt.getValue();
		assertEquals("Patient/123", details.getId().getValue());
	}

	@Test
	public void testVRead() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/_history/456");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(ourInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.VREAD), detailsCapt.capture());

		ActionRequestDetails details = detailsCapt.getValue();
		assertEquals("Patient/123/_history/456", details.getId().getValue());
	}

	@Test
	public void testCreate() throws Exception {
		Patient patient = new Patient();
		patient.addName().addFamily("FAMILY");
		ourFhirClient.create().resource(patient).execute();

		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(ourInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.CREATE), detailsCapt.capture());

		ActionRequestDetails details = detailsCapt.getValue();
		assertEquals("Patient", details.getResourceType());
		assertEquals(Patient.class, details.getResource().getClass());
		assertEquals("FAMILY", ((Patient) details.getResource()).getName().get(0).getFamily().get(0).getValue());
	}

	@Test
	public void testCreateWhereMethodHasNoResourceParam() throws Exception {
		Observation observation = new Observation();
		observation.getCode().setText("OBSCODE");
		ourFhirClient.create().resource(observation).execute();

		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(ourInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.CREATE), detailsCapt.capture());

		ActionRequestDetails details = detailsCapt.getValue();
		assertEquals("Observation", details.getResourceType());
		assertEquals(Observation.class, details.getResource().getClass());
		assertEquals("OBSCODE", ((Observation) details.getResource()).getCode().getText());
	}

	@Test
	public void testUpdate() throws Exception {
		Patient patient = new Patient();
		patient.addName().addFamily("FAMILY");
		patient.setId("Patient/123");
		ourFhirClient.update().resource(patient).execute();

		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(ourInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.UPDATE), detailsCapt.capture());

		ActionRequestDetails details = detailsCapt.getValue();
		assertEquals("Patient", details.getResourceType());
		assertEquals("Patient/123", details.getId().getValue());
		assertEquals(Patient.class, details.getResource().getClass());
		assertEquals("FAMILY", ((Patient) details.getResource()).getName().get(0).getFamily().get(0).getValue());
		assertEquals("Patient/123", ((Patient) details.getResource()).getId().getValue());
	}

	@Test
	public void testHistorySystem() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/_history");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(ourInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.HISTORY_SYSTEM), detailsCapt.capture());
	}

	@Test
	public void testHistoryType() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_history");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(ourInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.HISTORY_TYPE), detailsCapt.capture());
		assertEquals("Patient", detailsCapt.getValue().getResourceType());
	}

	@Test
	public void testHistoryInstance() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/_history");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(ourInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.HISTORY_INSTANCE), detailsCapt.capture());
		assertEquals("Patient", detailsCapt.getValue().getResourceType());
		assertEquals("Patient/123", detailsCapt.getValue().getId().getValue());
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.registerInterceptor(new ResponseHighlighterInterceptor());
		servlet.setResourceProviders(new DummyPatientResourceProvider(), new DummyObservationResourceProvider());
		servlet.setPlainProviders(new PlainProvider());
		servlet.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE);
		servlet.setDefaultResponseEncoding(EncodingEnum.XML);

		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

		ourInterceptor = mock(InterceptorAdapter.class);
		servlet.registerInterceptor(ourInterceptor);

		ourCtx.getRestfulClientFactory().setSocketTimeout(240*1000);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ourFhirClient = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort);

	}

	@BeforeEach
	public void before() {
		reset(ourInterceptor);

		when(ourInterceptor.incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(ourInterceptor.incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(ourInterceptor.outgoingResponse(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(ourInterceptor.outgoingResponse(any(RequestDetails.class), any(IBaseResource.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(ourInterceptor.outgoingResponse(any(RequestDetails.class), any(ResponseDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
	}

	public static class PlainProvider {

		@History()
		public List<IBaseResource> history() {
			Patient retVal = new Patient();
			retVal.setId("Patient/123/_history/2");
			return Collections.singletonList((IBaseResource) retVal);
		}

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Read(version = true)
		public Patient read(@IdParam IdDt theId) {
			Patient retVal = new Patient();
			retVal.setId(theId);
			return retVal;
		}

		@History()
		public List<Patient> history() {
			Patient retVal = new Patient();
			retVal.setId("Patient/123/_history/2");
			return Collections.singletonList(retVal);
		}

		@History()
		public List<Patient> history(@IdParam IdDt theId) {
			Patient retVal = new Patient();
			retVal.setId("Patient/123/_history/2");
			return Collections.singletonList(retVal);
		}

		@Create()
		public MethodOutcome create(@ResourceParam Patient thePatient) {
			Patient retVal = new Patient();
			retVal.setId("Patient/123/_history/2");
			return new MethodOutcome(retVal.getId());
		}

		@Update()
		public MethodOutcome update(@IdParam IdDt theId, @ResourceParam Patient thePatient) {
			Patient retVal = new Patient();
			retVal.setId("Patient/123/_history/2");
			return new MethodOutcome(retVal.getId());
		}

	}


	public static class DummyObservationResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}


		@Create()
		public MethodOutcome create(@ResourceParam String theBody) {
			Observation retVal = new Observation();
			retVal.setId("Observation/123/_history/2");
			return new MethodOutcome(retVal.getId());
		}
	}
	

}
