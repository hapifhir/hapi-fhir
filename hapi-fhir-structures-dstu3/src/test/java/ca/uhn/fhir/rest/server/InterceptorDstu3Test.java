package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class InterceptorDstu3Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer ourServlet;
	private IServerInterceptor myInterceptor1;
	private IServerInterceptor myInterceptor2;

	@After
	public void after() {
		for (IServerInterceptor next : new ArrayList<IServerInterceptor>(ourServlet.getInterceptors())) {
			ourServlet.unregisterInterceptor(next);
		}
	}

	@Before
	public void before() {
		myInterceptor1 = mock(IServerInterceptor.class);
		myInterceptor2 = mock(IServerInterceptor.class);
		ourServlet.setInterceptors(myInterceptor1, myInterceptor2);
	}

	@Test
	public void testServerOperationInterceptorAdapterMethods() {
		ServerOperationInterceptorAdapter i = new ServerOperationInterceptorAdapter();
		i.resourceCreated(null, null);
		i.resourceDeleted(null, null);
		i.resourceUpdated(null, null);
	}

	@Test
	public void testValidate() throws Exception {
		when(myInterceptor1.incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(any(RequestDetails.class), any(IResource.class))).thenReturn(true);
		when(myInterceptor2.incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor2.incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor2.outgoingResponse(any(RequestDetails.class), any(IResource.class))).thenReturn(true);

		//@formatter:off
		String input = 
				"{\n" + 
				"   \"resourceType\":\"Observation\",\n" + 
				"   \"id\":\"1855669\",\n" + 
				"   \"meta\":{\n" + 
				"      \"versionId\":\"1\",\n" + 
				"      \"lastUpdated\":\"2016-02-18T07:41:35.953-05:00\"\n" + 
				"   },\n" + 
				"   \"status\":\"final\",\n" + 
				"   \"subject\":{\n" + 
				"      \"reference\":\"Patient/1\"\n" + 
				"   },\n" + 
				"   \"effectiveDateTime\":\"2016-02-18T07:45:36-05:00\",\n" + 
				"   \"valueQuantity\":{\n" + 
				"      \"value\":57,\n" + 
				"      \"system\":\"http://unitsofmeasure.org\",\n" + 
				"      \"code\":\"{Beats}/min\"\n" + 
				"   }\n" + 
				"}";
		//@formatter:on

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$validate");
		httpPost.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);
		IOUtils.closeQuietly(status.getEntity().getContent());

		InOrder order = inOrder(myInterceptor1, myInterceptor2);
		order.verify(myInterceptor1, times(1)).incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class));
		order.verify(myInterceptor2, times(1)).incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class));
		order.verify(myInterceptor1, times(1)).incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class));
		order.verify(myInterceptor2, times(1)).incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class));
		ArgumentCaptor<RestOperationTypeEnum> opTypeCapt = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		ArgumentCaptor<ActionRequestDetails> arTypeCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		order.verify(myInterceptor1, times(1)).incomingRequestPreHandled(opTypeCapt.capture(), arTypeCapt.capture());
		order.verify(myInterceptor2, times(1)).incomingRequestPreHandled(any(RestOperationTypeEnum.class), any(ActionRequestDetails.class));
		order.verify(myInterceptor2, times(1)).outgoingResponse(any(RequestDetails.class), any(IResource.class));
		order.verify(myInterceptor1, times(1)).outgoingResponse(any(RequestDetails.class), any(IResource.class));

		// Avoid concurrency issues
		Thread.sleep(500);

		order.verify(myInterceptor2, times(1)).processingCompletedNormally(any(ServletRequestDetails.class));
		order.verify(myInterceptor1, times(1)).processingCompletedNormally(any(ServletRequestDetails.class));
		verifyNoMoreInteractions(myInterceptor1);
		verifyNoMoreInteractions(myInterceptor2);

		assertEquals(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE, opTypeCapt.getValue());
		assertNotNull(arTypeCapt.getValue().getResource());
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

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@SuppressWarnings("unused")
		@Validate()
		public MethodOutcome validate(@ResourceParam Patient theResource) {
			return new MethodOutcome();
		}
	}

}
