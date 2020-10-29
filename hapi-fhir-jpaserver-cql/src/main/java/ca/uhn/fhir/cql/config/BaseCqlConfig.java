package ca.uhn.fhir.cql.config;

import ca.uhn.fhir.cql.provider.CqlProviderLoader;
import org.springframework.context.annotation.Bean;

public abstract class BaseCqlConfig {
	@Bean
	CqlProviderLoader cqlProviderLoader() {
		return new CqlProviderLoader();
	}

}
