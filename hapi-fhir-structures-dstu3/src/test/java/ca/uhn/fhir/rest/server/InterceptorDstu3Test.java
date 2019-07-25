package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InterceptorDstu3Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer ourServlet;
	private static Patient ourLastPatient;
	private IServerInterceptor myInterceptor1;
	private IServerInterceptor myInterceptor2;

	@After
	public void after() {
		for (IServerInterceptor next : new ArrayList<>(ourServlet.getInterceptors())) {
			ourServlet.unregisterInterceptor(next);
		}
	}

	@Before
	public void before() {
		myInterceptor1 = mock(IServerInterceptor.class);
		myInterceptor2 = mock(IServerInterceptor.class);
	}

	private String createInput() {
		return "{\n" +
			"   \"resourceType\":\"Patient\",\n" +
			"   \"id\":\"1855669\",\n" +
			"   \"meta\":{\n" +
			"      \"versionId\":\"1\",\n" +
			"      \"lastUpdated\":\"2016-02-18T07:41:35.953-05:00\"\n" +
			"   },\n" +
			"   \"active\":true\n" +
			"}";
	}

	@Test
	public void testModifyResponse() throws IOException {
		InterceptorAdapter interceptor = new InterceptorAdapter() {
			@Override
			public boolean outgoingResponse(RequestDetails theRequestDetails, ResponseDetails theResponseDetails, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException {
				Patient retVal = new Patient();
				retVal.setId(theResponseDetails.getResponseResource().getIdElement());
				retVal.addName().setFamily("NAME1");
				theResponseDetails.setResponseResource(retVal);
				theResponseDetails.setResponseCode(202);
				return true;
			}
		};
		ourServlet.registerInterceptor(interceptor);
		try {

			HttpGet get = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
			try (CloseableHttpResponse status = ourClient.execute(get)) {
				String response = IOUtils.toString(status.getEntity().getContent(), Constants.CHARSET_UTF8);
				assertThat(response, containsString("NAME1"));
				assertEquals(202, status.getStatusLine().getStatusCode());
				assertEquals("Accepted", status.getStatusLine().getReasonPhrase());
			}

		} finally {
			ourServlet.unregisterInterceptor(interceptor);
		}
	}

	@Test
	public void testResourceResponseIncluded() throws Exception {
		ourServlet.setInterceptors(myInterceptor1, myInterceptor2);

		when(myInterceptor1.incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(ServletRequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(ServletRequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor2.incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor2.incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor2.outgoingResponse(nullable(ServletRequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);
		when(myInterceptor2.outgoingResponse(nullable(ServletRequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);

		String input = createInput();

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$validate");
		httpPost.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);
		IOUtils.closeQuietly(status.getEntity().getContent());

		InOrder order = inOrder(myInterceptor1, myInterceptor2);
		order.verify(myInterceptor1, times(1)).incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		order.verify(myInterceptor2, times(1)).incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		order.verify(myInterceptor1, times(1)).incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		order.verify(myInterceptor2, times(1)).incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		ArgumentCaptor<RestOperationTypeEnum> opTypeCapt = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		ArgumentCaptor<ActionRequestDetails> arTypeCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		order.verify(myInterceptor1, times(1)).incomingRequestPreHandled(opTypeCapt.capture(), arTypeCapt.capture());
		order.verify(myInterceptor2, times(1)).incomingRequestPreHandled(nullable(RestOperationTypeEnum.class), nullable(ActionRequestDetails.class));
		order.verify(myInterceptor2, times(1)).outgoingResponse(nullable(ServletRequestDetails.class), nullable(OperationOutcome.class));
		order.verify(myInterceptor2, times(1)).outgoingResponse(nullable(ServletRequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		order.verify(myInterceptor1, times(1)).outgoingResponse(nullable(ServletRequestDetails.class), nullable(OperationOutcome.class));
		order.verify(myInterceptor1, times(1)).outgoingResponse(nullable(ServletRequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));

		// Avoid concurrency issues
		Thread.sleep(500);

		order.verify(myInterceptor2, times(1)).processingCompletedNormally(nullable(ServletRequestDetails.class));
		order.verify(myInterceptor1, times(1)).processingCompletedNormally(nullable(ServletRequestDetails.class));
		verifyNoMoreInteractions(myInterceptor1);
		verifyNoMoreInteractions(myInterceptor2);

		assertEquals(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE, opTypeCapt.getValue());
		assertNotNull(arTypeCapt.getValue().getResource());
	}

	@Test
	public void testExceptionInProcessingCompletedNormally() throws Exception {
		ourServlet.setInterceptors(myInterceptor1);

		when(myInterceptor1.incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(ServletRequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(ServletRequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		doThrow(new NullPointerException("FOO")).when(myInterceptor1).processingCompletedNormally(any());

		String input = createInput();

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);
		try {
			assertEquals(201, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
	}

	@Test
	public void testResponseWithNothing() throws Exception {
		ourServlet.setInterceptors(myInterceptor1);

		when(myInterceptor1.incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(ServletRequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(ServletRequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);

		String input = createInput();

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);
		try {
			assertEquals(201, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

		InOrder order = inOrder(myInterceptor1);
		verify(myInterceptor1, times(1)).incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		verify(myInterceptor1, times(1)).incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		ArgumentCaptor<RestOperationTypeEnum> opTypeCapt = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		ArgumentCaptor<ActionRequestDetails> arTypeCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		ArgumentCaptor<ServletRequestDetails> rdCapt = ArgumentCaptor.forClass(ServletRequestDetails.class);
		ArgumentCaptor<OperationOutcome> resourceCapt = ArgumentCaptor.forClass(OperationOutcome.class);
		verify(myInterceptor1, times(1)).incomingRequestPreHandled(opTypeCapt.capture(), arTypeCapt.capture());
		verify(myInterceptor1, times(1)).outgoingResponse(nullable(ServletRequestDetails.class), resourceCapt.capture());

		assertEquals(1, resourceCapt.getAllValues().size());
		assertEquals(null, resourceCapt.getAllValues().get(0));
//		assertEquals("", rdCapt.getAllValues().get(0).get)
	}

	@Test
	public void testResponseWithOperationOutcome() throws Exception {
		ourServlet.setInterceptors(myInterceptor1);

		when(myInterceptor1.incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(ServletRequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);

		String input = createInput();

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$validate");
		httpPost.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);
		IOUtils.closeQuietly(status.getEntity().getContent());

		InOrder order = inOrder(myInterceptor1);
		order.verify(myInterceptor1, times(1)).incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		order.verify(myInterceptor1, times(1)).incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		ArgumentCaptor<RestOperationTypeEnum> opTypeCapt = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		ArgumentCaptor<ActionRequestDetails> arTypeCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		ArgumentCaptor<OperationOutcome> resourceCapt = ArgumentCaptor.forClass(OperationOutcome.class);
		order.verify(myInterceptor1, times(1)).incomingRequestPreHandled(opTypeCapt.capture(), arTypeCapt.capture());
		order.verify(myInterceptor1, times(1)).outgoingResponse(nullable(ServletRequestDetails.class), resourceCapt.capture());

		assertEquals(1, resourceCapt.getAllValues().size());
		assertEquals(OperationOutcome.class, resourceCapt.getAllValues().get(0).getClass());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testServerOperationInterceptorAdapterMethods() {
		ServerOperationInterceptorAdapter i = new ServerOperationInterceptorAdapter();
		i.resourceCreated(null, null);
		i.resourceDeleted(null, null);
		i.resourceUpdated(null, null);
		i.resourceUpdated(null, null, null);
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Patient theResource) {
			ourLastPatient = theResource;
			return new MethodOutcome();
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdType theId) {
			Patient retVal = new Patient();
			retVal.setId(theId);
			retVal.addName().setFamily("NAME0");
			return retVal;
		}

		@Validate()
		public MethodOutcome validate(@ResourceParam Patient theResource) {
			return new MethodOutcome();
		}

	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

}
