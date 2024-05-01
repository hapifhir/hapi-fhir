package ca.uhn.fhir.rest.client.interceptor;

import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleRequestHeaderInterceptorTest {
	@Test
	public void testParseComnpleteHeader(){
		SimpleRequestHeaderInterceptor i = new SimpleRequestHeaderInterceptor("Authorization: Bearer 123");
		assertThat(i.getHeaderName()).isEqualTo("Authorization");
		assertThat(i.getHeaderValue()).isEqualTo("Bearer 123");
	}

	@Test
	public void testParseComnpleteHeaderNameOnly(){
		SimpleRequestHeaderInterceptor i = new SimpleRequestHeaderInterceptor("Authorization");
		assertThat(i.getHeaderName()).isEqualTo("Authorization");
		assertNull(i.getHeaderValue());
	}

}
