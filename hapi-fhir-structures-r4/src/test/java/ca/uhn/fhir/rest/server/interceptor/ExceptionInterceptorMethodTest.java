package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExceptionInterceptorMethodTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExceptionInterceptorMethodTest.class);
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer servlet;
	private IServerInterceptor myInterceptor;
	
	@BeforeEach
	public void before() {
		myInterceptor = mock(IServerInterceptor.class);
		servlet.getInterceptorService().registerInterceptor(myInterceptor);
	}

	@AfterEach
	public void after() {
		servlet.getInterceptorService().unregisterInterceptor(myInterceptor);
	}
	
	@Test
	public void testThrowUnprocessableEntityException() throws Exception {

		when(myInterceptor.incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor.incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor.handleException(any(RequestDetails.class), any(BaseServerResponseException.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=throwUnprocessableEntityException");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			ourLog.info(IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8));
			assertEquals(422, status.getStatusLine().getStatusCode());
		}

		ArgumentCaptor<BaseServerResponseException> captor = ArgumentCaptor.forClass(BaseServerResponseException.class);
		verify(myInterceptor, times(1)).handleException(any(RequestDetails.class), captor.capture(), any(HttpServletRequest.class), any(HttpServletResponse.class));

		assertEquals(UnprocessableEntityException.class, captor.getValue().getClass());
	}

	@Test
	public void testThrowUnprocessableEntityExceptionAndOverrideResponse() throws Exception {

		when(myInterceptor.incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor.incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		
		when(myInterceptor.handleException(any(RequestDetails.class), any(BaseServerResponseException.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenAnswer(theInvocation -> {
			HttpServletResponse resp = (HttpServletResponse) theInvocation.getArguments()[3];
			resp.setStatus(405);
			resp.setContentType("text/plain");
			resp.getWriter().write("HELP IM A BUG");
			resp.getWriter().close();
			return false;
		});

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=throwUnprocessableEntityException");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(405, status.getStatusLine().getStatusCode());
			assertEquals("HELP IM A BUG", responseContent);
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
		servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

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

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @return The resource
		 */
		@Search(queryName = "throwUnprocessableEntityException")
		public List<Patient> throwUnprocessableEntityException() {
			throw new UnprocessableEntityException("Unprocessable!");
		}

	}


}
