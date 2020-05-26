package ca.uhn.fhir.jpa.batch.config;

import ca.uhn.fhir.jpa.batch.svc.DummyService;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Bean
	public DummyService dummyService() {
		return new DummyService();
	}

	@Bean
	public BatchConfigurer jpaBatchConfigurer() {
		return new JpaBatchConfigurer();
	}


}
