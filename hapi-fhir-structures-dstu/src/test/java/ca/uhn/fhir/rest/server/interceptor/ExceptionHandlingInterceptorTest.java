package ca.uhn.fhir.rest.server.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.core.StringContains;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;

import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.PortUtil;

public class ExceptionHandlingInterceptorTest {

	private static CloseableHttpClient ourClient;
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer servlet;
	private IServerInterceptor myInterceptor;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExceptionHandlingInterceptorTest.class);

	@Test
	public void testThrowUnprocessableEntityException() throws Exception {

		when(myInterceptor.incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor.incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor.handleException(any(RequestDetails.class), any(Throwable.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=throwUnprocessableEntityException");
		HttpResponse status = ourClient.execute(httpGet);
		ourLog.info(IOUtils.toString(status.getEntity().getContent()));
		assertEquals(422, status.getStatusLine().getStatusCode());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ArgumentCaptor<Throwable> captor = ArgumentCaptor.forClass(Throwable.class);
		verify(myInterceptor, times(1)).handleException(any(RequestDetails.class), captor.capture(), any(HttpServletRequest.class), any(HttpServletResponse.class));

		assertEquals(UnprocessableEntityException.class, captor.getValue().getClass());
	}
	
	
	@Test
	public void testThrowUnprocessableEntityExceptionAndOverrideResponse() throws Exception {

		when(myInterceptor.incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor.incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		
		when(myInterceptor.handleException(any(RequestDetails.class), any(Throwable.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock theInvocation) throws Throwable {
				HttpServletResponse resp = (HttpServletResponse) theInvocation.getArguments()[3];
				resp.setStatus(405);
				resp.setContentType("text/plain");
				resp.getWriter().write("HELP IM A BUG");
				resp.getWriter().close();
				return false;
			}
		});

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=throwUnprocessableEntityException");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(405, status.getStatusLine().getStatusCode());
		IOUtils.closeQuietly(status.getEntity().getContent());
		
		assertEquals("HELP IM A BUG", responseContent);
		
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@Before
	public void before() {
		myInterceptor = mock(IServerInterceptor.class);
		servlet.setInterceptors(Collections.singletonList(myInterceptor));
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		servlet = new RestfulServer();
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *            The resource identity
		 * @return The resource
		 */
		@Search(queryName = "throwUnprocessableEntityException")
		public List<Patient> throwUnprocessableEntityException() {
			throw new UnprocessableEntityException("Unprocessable!");
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

	}

}
