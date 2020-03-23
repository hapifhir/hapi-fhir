package ca.uhn.fhir.jpa.empi.config;

import ca.uhn.fhir.jpa.empi.dao.ResourceTableHelper;
import org.springframework.context.annotation.Bean;

public class EmpiTestConfig {
	@Bean
	public ResourceTableHelper resourceTableHelper() {
		return new ResourceTableHelper();
	}
}
