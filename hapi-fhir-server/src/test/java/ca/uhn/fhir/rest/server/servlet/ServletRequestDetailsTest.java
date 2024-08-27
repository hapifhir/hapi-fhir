package ca.uhn.fhir.rest.server.servlet;

import ca.uhn.fhir.rest.api.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.iterators.IteratorEnumeration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServletRequestDetailsTest {

	@Mock
	private HttpServletRequest myHttpServletRequest;

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

	@Test
	public void testAddHeader() {
		ServletRequestDetails srd = new ServletRequestDetails();
		srd.setServletRequest(myHttpServletRequest);
        when(myHttpServletRequest.getHeaderNames()).thenReturn(new IteratorEnumeration<>(List.of("Foo").iterator()));
		when(myHttpServletRequest.getHeaders(eq("Foo"))).thenReturn(new IteratorEnumeration<>(List.of("Bar", "Baz").iterator()));

		srd.addHeader("Name", "Value");
		srd.addHeader("Name", "Value2");

		// Verify added headers (make sure we're case insensitive)
		assertEquals("Value", srd.getHeader("NAME"));
		assertThat(srd.getHeaders("name")).contains("Value", "Value2");

		// Verify original headers (make sure we're case insensitive)
		assertEquals("Bar", srd.getHeader("FOO"));
		assertThat(srd.getHeaders("foo")).contains("Bar", "Baz");
	}


}

