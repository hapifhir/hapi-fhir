package ca.uhn.fhir.cql.config;

import ca.uhn.fhir.cql.provider.CqlProviderLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CqlConfig {
	@Bean
	CqlProviderLoader cqlProviderLoader() {
		return new CqlProviderLoader();
	}
}
