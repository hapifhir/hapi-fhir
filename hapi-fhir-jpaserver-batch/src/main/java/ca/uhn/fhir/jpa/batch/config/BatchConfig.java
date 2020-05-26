package ca.uhn.fhir.jpa.batch.config;

import ca.uhn.fhir.jpa.batch.svc.DummyService;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private JobRepository myJobRepository;

	@Bean
	public DummyService dummyService() {
		return new DummyService();
	}

}
