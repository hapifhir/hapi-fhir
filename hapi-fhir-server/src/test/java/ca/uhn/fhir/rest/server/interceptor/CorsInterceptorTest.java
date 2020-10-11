package ca.uhn.fhir.rest.server.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;

public class CorsInterceptorTest {

	@Test
	public void testCustomCorsConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.applyPermitDefaultValues();
		corsConfiguration.setAllowedMethods(Arrays.asList(new String[] { "*" }));
		CorsInterceptor corsInterceptor = new CorsInterceptor(corsConfiguration);

System.err.println("Custom CorsConfiguration");
System.err.println("allowCredentials = " + corsConfiguration.getAllowCredentials());
System.err.println("allowedHeaders = " + Arrays.toString(corsConfiguration.getAllowedHeaders().toArray()));
System.err.println("allowedMethods = " + Arrays.toString(corsConfiguration.getAllowedMethods().toArray()));
System.err.println("allowedOrigins = " + Arrays.toString(corsConfiguration.getAllowedOrigins().toArray()));
System.err.println("exposedHeaders = " + corsConfiguration.getExposedHeaders());
System.err.println("maxAge = " + corsConfiguration.getMaxAge());

		assertSame(corsConfiguration, corsInterceptor.getConfig());
		assertNull(corsConfiguration.getAllowCredentials());
		assertNotNull(corsConfiguration.getAllowedHeaders());
		assertNotNull(corsConfiguration.getAllowedMethods());
		assertNotNull(corsConfiguration.getAllowedOrigins());
		assertNull(corsConfiguration.getExposedHeaders());
		assertEquals(Long.valueOf(1800l),corsConfiguration.getMaxAge());
		assertNotNull(corsConfiguration.checkHeaders(Arrays.asList(new String[] {"Content-Type"})));
		assertNotNull(corsConfiguration.checkHeaders(Arrays.asList(new String[] {"Authorization"})));
		assertNotNull(corsConfiguration.checkHeaders(Arrays.asList(new String[] {"Authorization", "Content-Type"})));
		assertNotNull(corsConfiguration.checkHttpMethod(HttpMethod.GET));
		assertNotNull(corsConfiguration.checkOrigin("http://clinfhir.com"));
	}

	@Test
	public void testDefaultCorsConfig() {
		CorsInterceptor corsInterceptor = new CorsInterceptor();
		CorsConfiguration corsConfiguration = corsInterceptor.getConfig();

System.err.println("Default CorsConfiguration");
System.err.println("allowCredentials = " + corsConfiguration.getAllowCredentials());
System.err.println("allowedHeaders = " + Arrays.toString(corsConfiguration.getAllowedHeaders().toArray()));
System.err.println("allowedMethods = " + Arrays.toString(corsConfiguration.getAllowedMethods().toArray()));
System.err.println("allowedOrigins = " + Arrays.toString(corsConfiguration.getAllowedOrigins().toArray()));
System.err.println("exposedHeaders = " + Arrays.toString(corsConfiguration.getExposedHeaders().toArray()));
System.err.println("maxAge = " + corsConfiguration.getMaxAge());

		assertNull(corsConfiguration.getAllowCredentials());
		assertNotNull(corsConfiguration.getAllowedHeaders());
		assertNotNull(corsConfiguration.getAllowedMethods());
		assertNotNull(corsConfiguration.getAllowedOrigins());
		assertNotNull(corsConfiguration.getExposedHeaders());
		assertNull(corsConfiguration.getMaxAge());
		assertNotNull(corsConfiguration.checkHeaders(Arrays.asList(new String[] {"Content-Type"})));
//		assertNotNull(corsConfiguration.checkHeaders(Arrays.asList(new String[] {"Authorization"})));
		assertNotNull(corsConfiguration.checkHeaders(Arrays.asList(new String[] {"Authorization", "Content-Type"})));
		assertNotNull(corsConfiguration.checkHttpMethod(HttpMethod.GET));
		assertNotNull(corsConfiguration.checkOrigin("http://clinfhir.com"));
	}
}
