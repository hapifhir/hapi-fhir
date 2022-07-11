package ca.uhn.fhir.batch2.jobs.config;

import ca.uhn.fhir.batch2.jobs.parameters.UrlPartitioner;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import org.springframework.context.annotation.Bean;

public class BatchCommonCtx {
	@Bean
	UrlPartitioner urlPartitioner(MatchUrlService theMatchUrlService, IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		return new UrlPartitioner(theMatchUrlService, theRequestPartitionHelperSvc);
	}
}
