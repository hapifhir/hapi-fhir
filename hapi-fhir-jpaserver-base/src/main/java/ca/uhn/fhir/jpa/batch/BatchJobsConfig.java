package ca.uhn.fhir.jpa.batch;

import ca.uhn.fhir.jpa.bulk.job.BulkExportJobConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
//When you define a new batch job, add it here.
@Import({
	CommonBatchJobConfig.class,
	BulkExportJobConfig.class,})
public class BatchJobsConfig {
	//Empty config, as this is just an aggregator for all the various batch jobs defined around the system.
}
