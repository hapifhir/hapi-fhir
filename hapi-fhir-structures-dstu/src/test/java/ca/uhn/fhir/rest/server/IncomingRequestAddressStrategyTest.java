package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

public class IncomingRequestAddressStrategyTest {

	/**
	 * This is an incoming request from an instance of Tomcat on AWS, provided by 
	 * Simon Ling of Systems Made Simple
	 */
	@Test
	public void testAwsUrl() {
		
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getRequestURI()).thenReturn("/FhirStorm/fhir/Patient/_search");
		when(req.getServletPath()).thenReturn("/fhir");
		when(req.getRequestURL()).thenReturn(new StringBuffer().append("http://fhirstorm.dyndns.org:8080/FhirStorm/fhir/Patient/_search"));
		when(req.getContextPath()).thenReturn("/FhirStorm");
		
		IncomingRequestAddressStrategy incomingRequestAddressStrategy = new IncomingRequestAddressStrategy();
		String actual = incomingRequestAddressStrategy.determineServerBase(null,req);
		assertEquals("http://fhirstorm.dyndns.org:8080/FhirStorm/fhir", actual);
	}
	
}
