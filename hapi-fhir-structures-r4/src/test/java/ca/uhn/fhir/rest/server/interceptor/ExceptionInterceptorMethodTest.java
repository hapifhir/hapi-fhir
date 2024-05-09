package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExceptionInterceptorMethodTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExceptionInterceptorMethodTest.class);
	private IServerInterceptor myInterceptor;
	
	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML);
	
	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();
	
	@BeforeEach
	public void before() {
		myInterceptor = mock(IServerInterceptor.class);
		ourServer.getInterceptorService().registerInterceptor(myInterceptor);
	}

	@AfterEach
	public void after() {
		ourServer.getInterceptorService().unregisterInterceptor(myInterceptor);
	}
	
	@Test
	public void testThrowUnprocessableEntityException() throws Exception {

		when(myInterceptor.incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor.incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor.handleException(any(RequestDetails.class), any(BaseServerResponseException.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=throwUnprocessableEntityException");
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

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=throwUnprocessableEntityException");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(405, status.getStatusLine().getStatusCode());
			assertEquals("HELP IM A BUG", responseContent);
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	

	private static class DummyPatientResourceProvider implements IResourceProvider {

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
