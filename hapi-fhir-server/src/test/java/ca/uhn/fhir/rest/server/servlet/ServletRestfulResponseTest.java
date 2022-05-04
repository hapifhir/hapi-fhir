package ca.uhn.fhir.rest.server.servlet;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests of {@link ServletRestfulResponse}.
 */
@ExtendWith(MockitoExtension.class)
public class ServletRestfulResponseTest {

	@Mock
	private RestfulServer server;

	@Mock
	private HttpServletResponse servletResponse;

	private ServletRequestDetails requestDetails;

	@BeforeEach
	public void init() {
		requestDetails = new ServletRequestDetails(mock(IInterceptorBroadcaster.class));
		requestDetails.setServer(server);
		requestDetails.setServletResponse(servletResponse);
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

	@Test
	public void testSanitizeHeaderField() {
		assertEquals("AB", ServletRestfulResponse.sanitizeHeaderField("A\nB"));
		assertEquals("AB", ServletRestfulResponse.sanitizeHeaderField("A\r\r\rB"));
		assertEquals("AB", ServletRestfulResponse.sanitizeHeaderField("AB"));
	}

}
