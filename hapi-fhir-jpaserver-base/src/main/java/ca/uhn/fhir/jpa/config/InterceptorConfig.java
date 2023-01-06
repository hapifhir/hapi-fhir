package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterceptorConfig {
	@Bean
	public IInterceptorService jpaInterceptorService() {
		return new InterceptorService("JPA");
	}
}
