package ca.uhn.fhir.rest.server.servlet;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests of {@link ServletRestfulResponse}.
 */
public class ServletRestfulResponseTest {
	@Mock
	private RestfulServer server;

	@Mock
	private ServletOutputStream servletOutputStream;

	@Mock
	private HttpServletResponse servletResponse;

	private ServletRequestDetails requestDetails;

	private ServletRestfulResponse response;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void init() throws IOException {
		Mockito.when(servletResponse.getOutputStream()).thenReturn(servletOutputStream);

		requestDetails = new ServletRequestDetails(mock(IInterceptorBroadcaster.class));
		requestDetails.setServer(server);
		requestDetails.setServletResponse(servletResponse);
		response = new ServletRestfulResponse(requestDetails);
	}

	@Test
	public void addMultipleHeaderValues() throws IOException {
		final ServletRestfulResponse response = new ServletRestfulResponse(requestDetails);
		response.addHeader("Authorization", "Basic");
		response.addHeader("Authorization", "Bearer");
		response.addHeader("Cache-Control", "no-cache, no-store");

		response.getResponseWriter(200, "Status", "text/plain", "UTF-8", false);

		final InOrder orderVerifier = Mockito.inOrder(servletResponse);
		orderVerifier.verify(servletResponse).setHeader(eq("Authorization"), eq("Basic"));
		orderVerifier.verify(servletResponse).addHeader(eq("Authorization"), eq("Bearer"));
		verify(servletResponse).setHeader(eq("Cache-Control"), eq("no-cache, no-store"));
	}
}
