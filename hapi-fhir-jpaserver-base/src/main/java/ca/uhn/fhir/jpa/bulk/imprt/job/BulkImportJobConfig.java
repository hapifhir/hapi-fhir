package ca.uhn.fhir.jpa.bulk.imprt.job;

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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.bulk.imprt.model.ParsedBulkImportRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.step.item.KeyGenerator;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.CompositeRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;

import javax.batch.api.chunk.listener.RetryProcessListener;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.BULK_IMPORT_JOB_NAME;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.BULK_IMPORT_PROCESSING_STEP;

/**
 * Spring batch Job configuration file. Contains all necessary plumbing to run a
 * Bulk Export job.
 */
@Configuration
public class BulkImportJobConfig {

	public static final String JOB_PARAM_COMMIT_INTERVAL = "commitInterval";
	private static final Logger ourLog = LoggerFactory.getLogger(BulkImportJobConfig.class);
	@Autowired
	private StepBuilderFactory myStepBuilderFactory;
	@Autowired
	private JobBuilderFactory myJobBuilderFactory;
	@Autowired
	@Qualifier(BatchConstants.JOB_LAUNCHING_TASK_EXECUTOR)
	private TaskExecutor myTaskExecutor;
	@Autowired
	private DaoConfig myDaoConfig;

	@Bean(name = BULK_IMPORT_JOB_NAME)
	@Lazy
	public Job bulkImportJob() throws Exception {
		return myJobBuilderFactory.get(BULK_IMPORT_JOB_NAME)
			.validator(bulkImportJobParameterValidator())
			.start(bulkImportProcessingStep())
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
	public Step bulkImportProcessingStep() throws Exception {
		return myStepBuilderFactory.get(BULK_IMPORT_PROCESSING_STEP)
			.partitioner(BULK_IMPORT_PROCESSING_STEP, bulkImportPartitioner())
			.partitionHandler(partitionHandler())
			.listener(activateBulkImportEntityStepListener())
			.listener(errorLisener())
			.gridSize(10)
			.build();
	}

	private ChunkAroundListener errorLisener() {
		return new ChunkAroundListener();
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

		return myStepBuilderFactory.get("bulkImportProcessFilesStep")
			.<ParsedBulkImportRecord, ParsedBulkImportRecord>chunk(completionPolicy())
			.reader(bulkImportFileReader())
			.writer(bulkImportFileWriter())
			.listener(bulkImportStepListener())
			.listener(completionPolicy())
			.faultTolerant()
			.retryPolicy(bulkImportProcessFilesStepRetryPolicy())
			.build();
	}

	private RetryPolicy bulkImportProcessFilesStepRetryPolicy() {
		TimeoutRetryPolicy timeoutPolicy = new TimeoutRetryPolicy();
		timeoutPolicy.setTimeout(10000);

		SimpleRetryPolicy countRetryPolicy = new SimpleRetryPolicy(myDaoConfig.getBulkImportMaxRetryCount());

		CompositeRetryPolicy compositeRetryPolicy = new CompositeRetryPolicy();
		compositeRetryPolicy.setPolicies(new RetryPolicy[]{timeoutPolicy, countRetryPolicy});
		return compositeRetryPolicy;
	}

	@Bean
	@StepScope
	public CompletionPolicy completionPolicy() {
		return new BulkImportProcessStepCompletionPolicy();
	}

	@Bean
	@StepScope
	public BulkImportFileWriter bulkImportFileWriter() {
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

	public static class ChunkAroundListener implements RetryProcessListener {

		@Override
		public void onRetryProcessException(Object item, Exception ex) throws Exception {
			throw ex;
		}
	}

}
