package ca.uhn.fhir.batch2.jobs.config;

import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SharedCtx {

	@Bean
	public LoadIdsStep loadIdsStep(IBatch2DaoSvc theBatch2DaoSvc) {
		return new LoadIdsStep(theBatch2DaoSvc);
	}
}
