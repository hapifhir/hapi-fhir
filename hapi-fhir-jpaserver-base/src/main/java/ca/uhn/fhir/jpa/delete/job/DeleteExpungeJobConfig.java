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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.listener.PidReaderCounterListener;
import ca.uhn.fhir.jpa.batch.reader.ReverseCronologicalBatchResourcePidReader;
import ca.uhn.fhir.jpa.batch.writer.SqlExecutorWriter;
import ca.uhn.fhir.jpa.delete.model.RequestListJson;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.jpa.batch.BatchJobsConfig.DELETE_EXPUNGE_JOB_NAME;

/**
 * Spring batch Job configuration file. Contains all necessary plumbing to run a
 * Delete Expunge job.
 */
@Configuration
public class DeleteExpungeJobConfig {
	public static final String DELETE_EXPUNGE_URL_LIST_STEP_NAME = "delete-expunge-url-list-step";
	private static final int MINUTES_IN_FUTURE_TO_DELETE_FROM = 1;

	@Autowired
	private StepBuilderFactory myStepBuilderFactory;
	@Autowired
	private JobBuilderFactory myJobBuilderFactory;

	@Bean(name = DELETE_EXPUNGE_JOB_NAME)
	@Lazy
	public Job deleteExpungeJob(FhirContext theFhirContext, MatchUrlService theMatchUrlService, DaoRegistry theDaoRegistry) throws Exception {
		return myJobBuilderFactory.get(DELETE_EXPUNGE_JOB_NAME)
			.validator(deleteExpungeJobParameterValidator(theFhirContext, theMatchUrlService, theDaoRegistry))
			.start(deleteExpungeUrlListStep())
			.build();
	}

	@Nonnull
	public static JobParameters buildJobParameters(Integer theBatchSize, List<String> theUrlList, List<RequestPartitionId> theRequestPartitionIds) {
		Map<String, JobParameter> map = new HashMap<>();
		RequestListJson requestListJson = RequestListJson.fromUrlStringsAndRequestPartitionIds(theUrlList, theRequestPartitionIds);
		map.put(ReverseCronologicalBatchResourcePidReader.JOB_PARAM_REQUEST_LIST, new JobParameter(requestListJson.toString()));
		map.put(ReverseCronologicalBatchResourcePidReader.JOB_PARAM_START_TIME, new JobParameter(DateUtils.addMinutes(new Date(), MINUTES_IN_FUTURE_TO_DELETE_FROM)));
		if (theBatchSize != null) {
			map.put(ReverseCronologicalBatchResourcePidReader.JOB_PARAM_BATCH_SIZE, new JobParameter(theBatchSize.longValue()));
		}
		JobParameters parameters = new JobParameters(map);
		return parameters;
	}

	@Bean
	public Step deleteExpungeUrlListStep() {
		return myStepBuilderFactory.get(DELETE_EXPUNGE_URL_LIST_STEP_NAME)
			.<List<Long>, List<String>>chunk(1)
			.reader(reverseCronologicalBatchResourcePidReader())
			.processor(deleteExpungeProcessor())
			.writer(sqlExecutorWriter())
			.listener(pidCountRecorderListener())
			.listener(promotionListener())
			.build();
	}

	@Bean
	@StepScope
	public PidReaderCounterListener pidCountRecorderListener() {
		return new PidReaderCounterListener();
	}

	@Bean
	@StepScope
	public ReverseCronologicalBatchResourcePidReader reverseCronologicalBatchResourcePidReader() {
		return new ReverseCronologicalBatchResourcePidReader();
	}

	@Bean
	@StepScope
	public DeleteExpungeProcessor deleteExpungeProcessor() {
		return new DeleteExpungeProcessor();
	}

	@Bean
	@StepScope
	public SqlExecutorWriter sqlExecutorWriter() {
		return new SqlExecutorWriter();
	}

	@Bean
	public JobParametersValidator deleteExpungeJobParameterValidator(FhirContext theFhirContext, MatchUrlService theMatchUrlService, DaoRegistry theDaoRegistry) {
		return new DeleteExpungeJobParameterValidator(theMatchUrlService, theDaoRegistry);
	}

	@Bean
	public ExecutionContextPromotionListener promotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

		listener.setKeys(new String[]{SqlExecutorWriter.ENTITY_TOTAL_UPDATED_OR_DELETED, PidReaderCounterListener.RESOURCE_TOTAL_PROCESSED});

		return listener;
	}
}
