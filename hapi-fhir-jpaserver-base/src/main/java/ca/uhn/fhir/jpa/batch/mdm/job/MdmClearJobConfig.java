package ca.uhn.fhir.jpa.batch.mdm.job;

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

import ca.uhn.fhir.jpa.batch.job.MultiUrlJobParameterValidator;
import ca.uhn.fhir.jpa.batch.listener.PidReaderCounterListener;
import ca.uhn.fhir.jpa.batch.writer.SqlExecutorWriter;
import ca.uhn.fhir.jpa.delete.job.DeleteExpungeProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.MDM_CLEAR_JOB_NAME;

/**
 * Spring batch Job configuration file. Contains all necessary plumbing to run a
 * $mdm-clear job.
 */
@Configuration
public class MdmClearJobConfig {
	public static final String MDM_CLEAR_RESOURCE_LIST_STEP_NAME = "mdm-clear-resource-list-step";

	@Autowired
	private StepBuilderFactory myStepBuilderFactory;
	@Autowired
	private JobBuilderFactory myJobBuilderFactory;
	@Autowired
	private DeleteExpungeProcessor myDeleteExpungeProcessor;

	@Autowired
	@Qualifier("deleteExpungePromotionListener")
	private ExecutionContextPromotionListener myDeleteExpungePromotionListener;

	@Autowired
	private MultiUrlJobParameterValidator myMultiUrlProcessorParameterValidator;

	@Autowired
	private PidReaderCounterListener myPidCountRecorderListener;

	@Autowired
	private SqlExecutorWriter mySqlExecutorWriter;

	@Bean(name = MDM_CLEAR_JOB_NAME)
	@Lazy
	public Job mdmClearJob() {
		return myJobBuilderFactory.get(MDM_CLEAR_JOB_NAME)
			.validator(myMultiUrlProcessorParameterValidator)
			.start(mdmClearUrlListStep())
			.build();
	}

	@Bean
	public Step mdmClearUrlListStep() {
		return myStepBuilderFactory.get(MDM_CLEAR_RESOURCE_LIST_STEP_NAME)
			.<List<Long>, List<String>>chunk(1)
			.reader(reverseCronologicalBatchMdmLinkPidReader())
			.processor(deleteThenExpungeCompositeProcessor())
			.writer(mySqlExecutorWriter)
			.listener(myPidCountRecorderListener)
			.listener(myDeleteExpungePromotionListener)
			.build();
	}

	@Bean
	@StepScope
	public CompositeItemProcessor<List<Long>, List<String>> deleteThenExpungeCompositeProcessor() {
		CompositeItemProcessor<List<Long>, List<String>> compositeProcessor = new CompositeItemProcessor<>();
		List itemProcessors = new ArrayList<>();
		itemProcessors.add(mdmLinkDeleter());
		itemProcessors.add(myDeleteExpungeProcessor);
		compositeProcessor.setDelegates(itemProcessors);
		return compositeProcessor;
	}

	@Bean
	@StepScope
	public ReverseCronologicalBatchMdmLinkPidReader reverseCronologicalBatchMdmLinkPidReader() {
		return new ReverseCronologicalBatchMdmLinkPidReader();
	}

	@Bean
	public MdmLinkDeleter mdmLinkDeleter() {
		return new MdmLinkDeleter();
	}

	@Bean
	public ExecutionContextPromotionListener mdmClearPromotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

		listener.setKeys(new String[]{PidReaderCounterListener.RESOURCE_TOTAL_PROCESSED});

		return listener;
	}
}
