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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.job.MultiUrlProcessorJobConfig;
import ca.uhn.fhir.jpa.batch.listener.PidReaderCounterListener;
import ca.uhn.fhir.jpa.batch.writer.SqlExecutorWriter;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

import static ca.uhn.fhir.jpa.batch.BatchJobsConfig.DELETE_EXPUNGE_JOB_NAME;

/**
 * Spring batch Job configuration file. Contains all necessary plumbing to run a
 * Delete Expunge job.
 */
@Configuration
public class DeleteExpungeJobConfig extends MultiUrlProcessorJobConfig {
	public static final String DELETE_EXPUNGE_URL_LIST_STEP_NAME = "delete-expunge-url-list-step";

	@Autowired
	private StepBuilderFactory myStepBuilderFactory;
	@Autowired
	private JobBuilderFactory myJobBuilderFactory;

	@Bean(name = DELETE_EXPUNGE_JOB_NAME)
	@Lazy
	public Job deleteExpungeJob(FhirContext theFhirContext, MatchUrlService theMatchUrlService, DaoRegistry theDaoRegistry) {
		return myJobBuilderFactory.get(DELETE_EXPUNGE_JOB_NAME)
			.validator(multiUrlProcessorParameterValidator(theFhirContext, theMatchUrlService, theDaoRegistry))
			.start(deleteExpungeUrlListStep())
			.build();
	}

	@Bean
	public Step deleteExpungeUrlListStep() {
		return myStepBuilderFactory.get(DELETE_EXPUNGE_URL_LIST_STEP_NAME)
			.<List<Long>, List<String>>chunk(1)
			.reader(reverseCronologicalBatchResourcePidReader())
			.processor(deleteExpungeProcessor())
			.writer(sqlExecutorWriter())
			.listener(pidCountRecorderListener())
			.listener(deleteExpungePromotionListener())
			.build();
	}

	@Bean
	public ExecutionContextPromotionListener deleteExpungePromotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

		listener.setKeys(new String[]{SqlExecutorWriter.ENTITY_TOTAL_UPDATED_OR_DELETED, PidReaderCounterListener.RESOURCE_TOTAL_PROCESSED});

		return listener;
	}

	@Bean
	@StepScope
	public DeleteExpungeProcessor deleteExpungeProcessor() {
		return new DeleteExpungeProcessor();
	}
}
