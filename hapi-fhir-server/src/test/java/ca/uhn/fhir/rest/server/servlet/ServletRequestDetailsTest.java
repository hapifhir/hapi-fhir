package ca.uhn.fhir.rest.server.servlet;

import ca.uhn.fhir.rest.api.Constants;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServletRequestDetailsTest {
	@Test
	public void testRewriteHistoryHeader() {
		ServletRequestDetails servletRequestDetails = new ServletRequestDetails();
		HttpServletRequest httpRequest = mock(HttpServletRequest.class);
		servletRequestDetails.setServletRequest(httpRequest);
		when(httpRequest.getHeader(Constants.HEADER_REWRITE_HISTORY)).thenReturn("true");
		servletRequestDetails.setServletRequest(httpRequest);
		assertTrue(servletRequestDetails.isRewriteHistory());
	}

	@Test
	public void testRewriteHistoryHeaderNull() {
		ServletRequestDetails servletRequestDetails = new ServletRequestDetails();
		HttpServletRequest httpRequest = mock(HttpServletRequest.class);
		servletRequestDetails.setServletRequest(httpRequest);
		when(httpRequest.getHeader(Constants.HEADER_REWRITE_HISTORY)).thenReturn(null);
		servletRequestDetails.setServletRequest(httpRequest);
		assertFalse(servletRequestDetails.isRewriteHistory());
	}

	@Test
	public void testRewriteHistoryHeaderFalse() {
		ServletRequestDetails servletRequestDetails = new ServletRequestDetails();
		HttpServletRequest httpRequest = mock(HttpServletRequest.class);
		servletRequestDetails.setServletRequest(httpRequest);
		when(httpRequest.getHeader(Constants.HEADER_REWRITE_HISTORY)).thenReturn("false");
		servletRequestDetails.setServletRequest(httpRequest);
		assertFalse(servletRequestDetails.isRewriteHistory());
	}

}
