package ca.uhn.fhir.rest.server.interceptor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;

public class CorsInterceptorTest {

	private static final Logger ourLog = LoggerFactory.getLogger(CorsInterceptorTest.class);

	@Test
	public void testCustomCorsConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.applyPermitDefaultValues();
		corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Content-Location",
			"Date",
			"ETag",
			"Location",
			"X-Request-Id",
			"X-Correlation-Id"));
		CorsInterceptor corsInterceptor = new CorsInterceptor(corsConfiguration);

		assertThat(corsInterceptor.getConfig()).isSameAs(corsConfiguration);
		assertThat(corsConfiguration.getAllowCredentials()).isNull();
		assertThat(corsConfiguration.getAllowedHeaders()).isNotNull();
		assertThat(corsConfiguration.getAllowedMethods()).isNotNull();
		assertThat(corsConfiguration.getAllowedOrigins()).isNotNull();
		assertThat(corsConfiguration.getExposedHeaders()).isNotNull();
		assertThat(corsConfiguration.getMaxAge()).isEqualTo(Long.valueOf(1800L));
		assertThat(corsConfiguration.checkHeaders(Collections.singletonList("Content-Type"))).isNotNull();
		assertThat(corsConfiguration.checkHeaders(Collections.singletonList("Authorization"))).isNotNull();
		assertThat(corsConfiguration.checkHeaders(Arrays.asList("Authorization", "Content-Type"))).isNotNull();
		assertThat(corsConfiguration.checkHttpMethod(HttpMethod.GET)).isNotNull();
		assertThat(corsConfiguration.checkOrigin("http://clinfhir.com")).isNotNull();

		ourLog.info("Custom CorsConfiguration:  allowCredentials = {};  allowedHeaders = {};  " +
			"allowedMethods = {};  allowedOrigins = {};  exposedHeaders = {};  maxAge = {}",
			corsConfiguration.getAllowCredentials(),
			Arrays.toString(corsConfiguration.getAllowedHeaders().toArray()),
			Arrays.toString(corsConfiguration.getAllowedMethods().toArray()),
			Arrays.toString(corsConfiguration.getAllowedOrigins().toArray()),
			Arrays.toString(corsConfiguration.getExposedHeaders().toArray()),
			corsConfiguration.getMaxAge());
	}

	@Test
	public void testDefaultCorsConfig() {
		CorsInterceptor corsInterceptor = new CorsInterceptor();
		CorsConfiguration corsConfiguration = corsInterceptor.getConfig();

		assertThat(corsConfiguration.getAllowCredentials()).isNull();
		assertThat(corsConfiguration.getAllowedHeaders()).isNotNull();
		assertThat(corsConfiguration.getAllowedMethods()).isNotNull();
		assertThat(corsConfiguration.getAllowedOrigins()).isNotNull();
		assertThat(corsConfiguration.getExposedHeaders()).isNotNull();
		assertThat(corsConfiguration.getMaxAge()).isNull();
		assertThat(corsConfiguration.checkHeaders(Collections.singletonList("Content-Type"))).isNotNull();
//		assertNotNull(corsConfiguration.checkHeaders(Arrays.asList(new String[] {"Authorization"})));
		assertThat(corsConfiguration.checkHeaders(Arrays.asList("Authorization", "Content-Type"))).isNotNull();
		assertThat(corsConfiguration.checkHttpMethod(HttpMethod.GET)).isNotNull();
		assertThat(corsConfiguration.checkOrigin("http://clinfhir.com")).isNotNull();

		ourLog.info("Default CorsConfiguration:  allowCredentials = {};  allowedHeaders = {};  " +
			"allowedMethods = {};  allowedOrigins = {};  exposedHeaders = {};  maxAge = {}",
			corsConfiguration.getAllowCredentials(),
			Arrays.toString(corsConfiguration.getAllowedHeaders().toArray()),
			Arrays.toString(corsConfiguration.getAllowedMethods().toArray()),
			Arrays.toString(corsConfiguration.getAllowedOrigins().toArray()),
			Arrays.toString(corsConfiguration.getExposedHeaders().toArray()),
			corsConfiguration.getMaxAge());
	}
}
