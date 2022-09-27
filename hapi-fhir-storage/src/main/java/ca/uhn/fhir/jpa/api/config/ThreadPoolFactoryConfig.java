package ca.uhn.fhir.jpa.api.config;

import ca.uhn.fhir.jpa.dao.ThreadPoolFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class exists to help generating thread pools for other parts of the code.
 */
@Configuration
public class ThreadPoolFactoryConfig {

	@Bean
	public ThreadPoolFactory threadPoolFactory() {
		return new ThreadPoolFactory();
	}
}
