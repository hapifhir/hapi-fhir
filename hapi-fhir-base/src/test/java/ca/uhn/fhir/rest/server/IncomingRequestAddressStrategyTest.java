package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

public class IncomingRequestAddressStrategyTest {

	@Test
	public void testAwsUrl() {
		
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getRequestURI()).thenReturn("/FhirStorm/fhir/Patient/_search");
		when(req.getServletPath()).thenReturn("/fhir");
		when(req.getRequestURL()).thenReturn(new StringBuffer().append("http://fhirstorm.dyndns.org:8080/FhirStorm/fhir/Patient/_search"));
		when(req.getContextPath()).thenReturn("/FhirStorm");
		
		IncomingRequestAddressStrategy incomingRequestAddressStrategy = new IncomingRequestAddressStrategy();
		String actual = incomingRequestAddressStrategy.determineServerBase(req);
		assertEquals("http://fhirstorm.dyndns.org:8080/FhirStorm/fhir", actual);
	}
	
	/*
	IncomingRequestAddressStrategy15:29:02.876 [http-bio-8080-exec-3] TRACE c.uhn.fhir.rest.server.RestfulServer -
	Request FullPath: /FhirStorm/fhir/Patient/_search
	15:29:02.876 [http-bio-8080-exec-3] TRACE c.uhn.fhir.rest.server.RestfulServer -
	Servlet Path: /fhir
	15:29:02.876 [http-bio-8080-exec-3] TRACE c.uhn.fhir.rest.server.RestfulServer -
	Request Url: http://fhirstorm.dyndns.org:8080/FhirStorm/fhir/Patient/_search
	15:29:02.876 [http-bio-8080-exec-3] TRACE c.uhn.fhir.rest.server.RestfulServer -
	Context Path: /FhirStorm
	*/
}
