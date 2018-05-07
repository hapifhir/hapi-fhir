package ca.uhn.fhir.rest.client.interceptor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleRequestHeaderInterceptorTest {
	@Test
	public void testParseComnpleteHeader(){
		SimpleRequestHeaderInterceptor i = new SimpleRequestHeaderInterceptor("Authorization: Bearer 123");
		assertEquals("Authorization", i.getHeaderName());
		assertEquals("Bearer 123", i.getHeaderValue());
	}

	@Test
	public void testParseComnpleteHeaderNameOnly(){
		SimpleRequestHeaderInterceptor i = new SimpleRequestHeaderInterceptor("Authorization");
		assertEquals("Authorization", i.getHeaderName());
		assertEquals(null, i.getHeaderValue());
	}

}
