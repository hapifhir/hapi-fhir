package ca.uhn.fhir.jpa.delete.job;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch.BatchConstants;
import ca.uhn.fhir.jpa.delete.model.ParsedDeleteExpungeRecord;
import ca.uhn.fhir.jpa.delete.model.UrlListJson;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;

import static ca.uhn.fhir.jpa.batch.BatchJobsConfig.DELETE_EXPUNGE_JOB_NAME;

/**
 * Spring batch Job configuration file. Contains all necessary plumbing to run a
 * Delete Expunge job.
 */
@Configuration
public class DeleteExpungeJobConfig {

	public static final String JOB_PARAM_URL_LIST = "urlList";
	// FIXME KHS remove
	public static final String JOB_UUID_PARAMETER = "uuid";
	public static final String DELETE_EXPUNGE_URL_LIST_STEP = "deleteExpungeUrlListStep";

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private StepBuilderFactory myStepBuilderFactory;
	@Autowired
	private JobBuilderFactory myJobBuilderFactory;
	@Autowired
	@Qualifier(BatchConstants.JOB_LAUNCHING_TASK_EXECUTOR)
	private TaskExecutor myTaskExecutor;

	@Bean(name = DELETE_EXPUNGE_JOB_NAME)
	@Lazy
	public Job deleteExpungeJob(FhirContext theFhirContext, MatchUrlService theMatchUrlService) throws Exception {
		// FIXME KHS implement this.  Current thinking:
		// 1. validate URLs are valid
		// 2. Create 1 step for each URL
		// 2.1 For each URL:
		// 2.2 Count the total number of resources to be deleted, and get date of oldest one.
		// 2.3 Search forward by date in batches of BATCH_SIZE (configurable, default 100,000), delete expunge them, update the date.
		// 2.4 Update total count deleted

		return myJobBuilderFactory.get(DELETE_EXPUNGE_JOB_NAME)
			.validator(deleteExpungeJobParameterValidator(theFhirContext, theMatchUrlService))
			.start(deleteExpungeUrlListStep())
			.build();
	}

	@Bean
	@JobScope
	public Step deleteExpungeUrlListStep() {
		return myStepBuilderFactory.get(DELETE_EXPUNGE_URL_LIST_STEP)
			.<String, String>chunk(1)
			.reader(urlListReader())
			.processor(deleteExpungeProcessor())
			.writer(deleteExpungeResultWriter())
			.build();
	}

	@Bean
	@StepScope
	public UrlListReader urlListReader() {
		return new UrlListReader();
	}

	@Bean
	@StepScope
	public DeleteExpungeProcessor deleteExpungeProcessor() {
		return new DeleteExpungeProcessor();
	}

	@Bean
	@StepScope
	public DeleteExpungeResultWriter deleteExpungeResultWriter() {
		return new DeleteExpungeResultWriter();
	}

	@Bean
	public JobParametersValidator deleteExpungeJobParameterValidator(FhirContext theFhirContext, MatchUrlService theMatchUrlService) {
		return new DeleteExpungeJobParameterValidator(theFhirContext, theMatchUrlService);
	}
}
