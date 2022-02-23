package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
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
import ca.uhn.fhir.test.utilities.JettyUtil;
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
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InterceptorDstu3Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer ourServlet;
	private static Patient ourLastPatient;
	private IServerInterceptor myInterceptor1;
	private IServerInterceptor myInterceptor2;

	@AfterEach
	public void after() {
		ourServlet.getInterceptorService().unregisterAllInterceptors();
	}

	@BeforeEach
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
	public void testServerPreHandledOnOperationCapturesResource() throws IOException {

		AtomicReference<IBaseResource> resource = new AtomicReference<>();
		IAnonymousInterceptor interceptor = (thePointcut, theArgs) -> {
			RequestDetails requestDetails = theArgs.get(RequestDetails.class);
			resource.set(requestDetails.getResource());
		};

		ourServlet.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED, interceptor);
		try {
			Parameters p = new Parameters();
			p.addParameter().setName("limit").setValue(new IntegerType(123));
			String input = ourCtx.newJsonParser().encodeResourceToString(p);

			HttpPost post = new HttpPost("http://localhost:" + ourPort + "/Patient/$postOperation");
			post.setEntity(new StringEntity(input, ContentType.create("application/fhir+json", Constants.CHARSET_UTF8)));
			try (CloseableHttpResponse status = ourClient.execute(post)) {
				assertEquals(200, status.getStatusLine().getStatusCode());
				IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			}
		} finally {
			ourServlet.unregisterInterceptor(interceptor);
		}

		assertNotNull(resource.get());
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
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(IBaseResource.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);

		when(myInterceptor2.incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor2.incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor2.outgoingResponse(nullable(RequestDetails.class))).thenReturn(true);
		when(myInterceptor2.outgoingResponse(nullable(RequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);
		when(myInterceptor2.outgoingResponse(nullable(RequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor2.outgoingResponse(nullable(RequestDetails.class), nullable(IBaseResource.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor2.outgoingResponse(nullable(RequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);

		doAnswer(t->{
			RestOperationTypeEnum type = (RestOperationTypeEnum) t.getArguments()[0];
			ActionRequestDetails det = (ActionRequestDetails) t.getArguments()[1];
			return null;
		}).when(myInterceptor1).incomingRequestPreHandled(any(), any());

		String input = createInput();

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$validate");
		httpPost.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {
			IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		}

		InOrder order = inOrder(myInterceptor1, myInterceptor2);
		order.verify(myInterceptor1, times(1)).incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		order.verify(myInterceptor2, times(1)).incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		order.verify(myInterceptor1, times(1)).incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		order.verify(myInterceptor2, times(1)).incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		ArgumentCaptor<RestOperationTypeEnum> opTypeCapt = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		ArgumentCaptor<ActionRequestDetails> arTypeCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		order.verify(myInterceptor1, times(1)).incomingRequestPreHandled(opTypeCapt.capture(), arTypeCapt.capture());
		order.verify(myInterceptor2, times(1)).incomingRequestPreHandled(nullable(RestOperationTypeEnum.class), nullable(ActionRequestDetails.class));

		assertEquals(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE, opTypeCapt.getValue());
		assertNotNull(arTypeCapt.getValue().getResource());
	}

	@Test
	public void testExceptionInProcessingCompletedNormally() throws Exception {
		ourServlet.setInterceptors(myInterceptor1);

		when(myInterceptor1.incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(IBaseResource.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);

		String input = createInput();

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {
			IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(status.getStatusLine().getStatusCode(), either(equalTo(200)).or(equalTo(201)));
		}
	}

	@Test
	public void testResponseWithNothing() throws Exception {
		ourServlet.setInterceptors(myInterceptor1);

		when(myInterceptor1.incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(IBaseResource.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);

		String input = createInput();

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {
			assertEquals(201, status.getStatusLine().getStatusCode());
			IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
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
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(IBaseResource.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);

		String input = createInput();

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$validate");
		httpPost.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {
			IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		}

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
			return new MethodOutcome().setCreated(true);
		}

		@Operation(name="$postOperation")
		public Parameters postOperation(
			@OperationParam(name = "limit") IntegerType theLimit
			) {
			return new Parameters();
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

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setResourceProviders(patientProvider);
		ourServlet.setDefaultResponseEncoding(EncodingEnum.XML);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

}
