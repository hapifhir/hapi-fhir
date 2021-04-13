package ca.uhn.fhir.jpa.bulk.imprt.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.batch.BatchConstants;
import ca.uhn.fhir.jpa.bulk.imprt.model.ParsedBulkImportRecord;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;

import static ca.uhn.fhir.jpa.batch.BatchJobsConfig.BULK_IMPORT_JOB_NAME;

/**
 * Spring batch Job configuration file. Contains all necessary plumbing to run a
 * Bulk Export job.
 */
@Configuration
public class BulkImportJobConfig {

	public static final String JOB_PARAM_COMMIT_INTERVAL = "commitInterval";

	@Autowired
	private StepBuilderFactory myStepBuilderFactory;

	@Autowired
	private JobBuilderFactory myJobBuilderFactory;

	@Autowired
	@Qualifier(BatchConstants.JOB_LAUNCHING_TASK_EXECUTOR)
	private TaskExecutor myTaskExecutor;

	@Bean(name = BULK_IMPORT_JOB_NAME)
	@Lazy
	public Job bulkImportJob() throws Exception {
		return myJobBuilderFactory.get(BULK_IMPORT_JOB_NAME)
			.validator(bulkImportJobParameterValidator())
			.start(bulkImportPartitionStep())
			.next(bulkImportCloseJobStep())
			.build();
	}

	@Bean
	public JobParametersValidator bulkImportJobParameterValidator() {
		return new BulkImportJobParameterValidator();
	}

	@Bean
	public CreateBulkImportEntityTasklet createBulkImportEntityTasklet() {
		return new CreateBulkImportEntityTasklet();
	}

	@Bean
	@JobScope
	public ActivateBulkImportEntityStepListener activateBulkImportEntityStepListener() {
		return new ActivateBulkImportEntityStepListener();
	}

	@Bean
	public Step bulkImportPartitionStep() throws Exception {
		return myStepBuilderFactory.get("bulkImportPartitionStep")
			.partitioner("bulkImportPartitionStep", bulkImportPartitioner())
			.partitionHandler(partitionHandler())
			.listener(activateBulkImportEntityStepListener())
			.gridSize(10)
			.build();
	}

	private PartitionHandler partitionHandler() throws Exception {
		assert myTaskExecutor != null;

		TaskExecutorPartitionHandler retVal = new TaskExecutorPartitionHandler();
		retVal.setStep(bulkImportProcessFilesStep());
		retVal.setTaskExecutor(myTaskExecutor);
		retVal.afterPropertiesSet();
		return retVal;
	}

	@Bean
	public Step bulkImportCloseJobStep() {
		return myStepBuilderFactory.get("bulkImportCloseJobStep")
			.tasklet(bulkImportJobCloser())
			.build();
	}

	@Bean
	@JobScope
	public BulkImportJobCloser bulkImportJobCloser() {
		return new BulkImportJobCloser();
	}

	@Bean
	@JobScope
	public BulkImportPartitioner bulkImportPartitioner() {
		return new BulkImportPartitioner();
	}


	@Bean
	public Step bulkImportProcessFilesStep() {
		CompletionPolicy completionPolicy = completionPolicy();

		return myStepBuilderFactory.get("bulkImportProcessFilesStep")
			.<ParsedBulkImportRecord, ParsedBulkImportRecord>chunk(completionPolicy)
			.reader(bulkImportFileReader())
			.writer(bulkImportFileWriter())
			.listener(bulkImportStepListener())
			.listener(completionPolicy)
			.build();
	}

	@Bean
	@StepScope
	public CompletionPolicy completionPolicy() {
		return new BulkImportProcessStepCompletionPolicy();
	}

	@Bean
	@StepScope
	public ItemWriter<ParsedBulkImportRecord> bulkImportFileWriter() {
		return new BulkImportFileWriter();
	}


	@Bean
	@StepScope
	public BulkImportFileReader bulkImportFileReader() {
		return new BulkImportFileReader();
	}

	@Bean
	@StepScope
	public BulkImportStepListener bulkImportStepListener() {
		return new BulkImportStepListener();
	}


}
