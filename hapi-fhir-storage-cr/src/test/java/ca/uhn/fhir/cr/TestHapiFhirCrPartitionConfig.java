package ca.uhn.fhir.cr;

import org.springframework.context.annotation.Bean;

public class TestHapiFhirCrPartitionConfig {
	@Bean
	public PartitionHelper partitionHelper() {
		return new PartitionHelper();
	}
}
