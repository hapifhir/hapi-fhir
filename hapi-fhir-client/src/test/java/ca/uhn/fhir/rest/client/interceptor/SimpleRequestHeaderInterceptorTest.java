package ca.uhn.fhir.rest.client.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
		assertNull(i.getHeaderValue());
	}

}
