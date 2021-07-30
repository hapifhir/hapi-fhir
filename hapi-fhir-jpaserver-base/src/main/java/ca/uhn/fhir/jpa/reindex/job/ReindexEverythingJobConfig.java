package ca.uhn.fhir.jpa.reindex.job;

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

import ca.uhn.fhir.jpa.batch.listener.PidReaderCounterListener;
import ca.uhn.fhir.jpa.batch.reader.CronologicalBatchAllResourcePidReader;
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

import static ca.uhn.fhir.jpa.batch.BatchJobsConfig.REINDEX_EVERYTHING_JOB_NAME;

/**
 * Spring batch Job configuration file. Contains all necessary plumbing to run a
 * Reindex job.
 */
@Configuration
public class ReindexEverythingJobConfig {
	public static final String REINDEX_EVERYTHING_STEP_NAME = "reindex-everything-step";

	@Autowired
	private StepBuilderFactory myStepBuilderFactory;
	@Autowired
	private JobBuilderFactory myJobBuilderFactory;

	@Bean(name = REINDEX_EVERYTHING_JOB_NAME)
	@Lazy
	public Job reindexJob() {
		return myJobBuilderFactory.get(REINDEX_EVERYTHING_JOB_NAME)
			.start(reindexEverythingStep())
			.build();
	}

	@Bean
	public Step reindexEverythingStep() {
		return myStepBuilderFactory.get(REINDEX_EVERYTHING_STEP_NAME)
			.<List<Long>, List<Long>>chunk(1)
			.reader(cronologicalBatchAllResourcePidReader())
			.writer(reindexWriter())
			.listener(reindexEverythingPidCountRecorderListener())
			.listener(reindexEverythingPromotionListener())
			.build();
	}

	@Bean
	@StepScope
	public CronologicalBatchAllResourcePidReader cronologicalBatchAllResourcePidReader() {
		return new CronologicalBatchAllResourcePidReader();
	}

	@Bean
	@StepScope
	public ReindexWriter reindexWriter() {
		return new ReindexWriter();
	}

	@Bean
	@StepScope
	public PidReaderCounterListener reindexEverythingPidCountRecorderListener() {
		return new PidReaderCounterListener();
	}

	@Bean
	public ExecutionContextPromotionListener reindexEverythingPromotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

		listener.setKeys(new String[]{PidReaderCounterListener.RESOURCE_TOTAL_PROCESSED});

		return listener;
	}
}
