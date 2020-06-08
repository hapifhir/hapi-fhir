package ca.uhn.fhir.jpa.batch;

import ca.uhn.fhir.jpa.batch.processors.PidToIBaseResourceProcessor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonBatchJobConfig {

	@Bean
	@StepScope
	public PidToIBaseResourceProcessor pidToResourceProcessor() {
		return new PidToIBaseResourceProcessor();
	}

}
