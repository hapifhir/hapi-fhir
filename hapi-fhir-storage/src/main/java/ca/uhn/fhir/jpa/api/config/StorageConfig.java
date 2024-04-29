package ca.uhn.fhir.jpa.api.config;

import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.searchparam.SearchParamSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
	@Bean
	SearchParamSvc searchParamSvc() {
		return new SearchParamSvc();
	}

	@Bean
	public SearchBuilderFactory<?> searchBuilderFactory() {
		return new SearchBuilderFactory<>();
	}
}
