package ca.uhn.fhir.jpa.batch;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.batch.mdm.job.MdmClearJobConfig;
import ca.uhn.fhir.jpa.batch.svc.BatchJobSubmitterImpl;
import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
import ca.uhn.fhir.jpa.bulk.imprt.job.BulkImportJobConfig;
import ca.uhn.fhir.jpa.config.BatchJobRegisterer;
import ca.uhn.fhir.jpa.delete.job.DeleteExpungeJobConfig;
import ca.uhn.fhir.jpa.term.job.TermCodeSystemDeleteJobConfig;
import ca.uhn.fhir.jpa.term.job.TermCodeSystemVersionDeleteJobConfig;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableBatchProcessing
@Import({
	CommonBatchJobConfig.class,
	BulkExportJobConfig.class,
	BulkImportJobConfig.class,
	DeleteExpungeJobConfig.class,
	MdmClearJobConfig.class,
	TermCodeSystemDeleteJobConfig.class,
	TermCodeSystemVersionDeleteJobConfig.class
  // When you define a new batch job, add it here.
})
public class BatchJobsConfig {
	@Bean
	public IBatchJobSubmitter batchJobSubmitter() {
		return new BatchJobSubmitterImpl();
	}

	@Bean
	public BatchJobRegisterer batchJobRegisterer() {
		return new BatchJobRegisterer();
	}

	@Bean
	public SimpleJobOperator jobOperator(JobExplorer theJobExplorer, JobRepository theJobRepository, JobRegistry theJobRegistry, JobLauncher theJobLauncher) {
		SimpleJobOperator jobOperator = new SimpleJobOperator();

		jobOperator.setJobExplorer(theJobExplorer);
		jobOperator.setJobRepository(theJobRepository);
		jobOperator.setJobRegistry(theJobRegistry);
		jobOperator.setJobLauncher(theJobLauncher);

		return jobOperator;
	}
}
