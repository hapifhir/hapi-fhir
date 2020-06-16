package ca.uhn.fhir.jpa.batch.config;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class TestBatchConfig {
	@Bean
	public BatchConfigurer batchConfigurer() {
		return new NonPersistedBatchConfigurer();
	}
}
