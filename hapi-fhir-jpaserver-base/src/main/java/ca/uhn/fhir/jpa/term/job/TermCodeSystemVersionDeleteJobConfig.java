package ca.uhn.fhir.jpa.term.job;

/*
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

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_UNIQUE_VERSION_DELETE_STEP_NAME;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CONCEPTS_UNIQUE_VERSION_DELETE_STEP_NAME;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CONCEPT_RELATIONS_UNIQUE_VERSION_DELETE_STEP_NAME;

/**
 * Configuration for batch job which deletes a specific TermCodeSystemVersion and its related,
 * TermConceptProperty(es), TermConceptDesignation(s), and TermConceptParentChildLink(s)
 **/
@Configuration
public class TermCodeSystemVersionDeleteJobConfig extends BaseTermCodeSystemDeleteJobConfig {


	@Bean(name = TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)
	@Lazy
	public Job termCodeSystemVersionDeleteJob() {
		return myJobBuilderFactory.get(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)
			.validator(termCodeSystemVersionDeleteJobParameterValidator())
			.start(termConceptRelationsUniqueVersionDeleteStep())
			.next(termConceptsUniqueVersionDeleteStep())
			.next(termCodeSystemUniqueVersionDeleteStep())
			.build();
	}


	@Bean
	public JobParametersValidator termCodeSystemVersionDeleteJobParameterValidator() {
		return new TermCodeSystemVersionDeleteJobParameterValidator();
	}


	@Bean(name = TERM_CONCEPT_RELATIONS_UNIQUE_VERSION_DELETE_STEP_NAME)
	public Step termConceptRelationsUniqueVersionDeleteStep() {
		return myStepBuilderFactory.get(TERM_CONCEPT_RELATIONS_UNIQUE_VERSION_DELETE_STEP_NAME)
			.<Long, Long>chunk(1)
			.reader(batchTermCodeSystemUniqueVersionDeleteReader())
			.writer(batchConceptRelationsDeleteWriter())
			.build();
	}


	@Bean(name = TERM_CONCEPTS_UNIQUE_VERSION_DELETE_STEP_NAME)
	public Step termConceptsUniqueVersionDeleteStep() {
		DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
		attribute.setTimeout(TERM_CONCEPT_DELETE_TIMEOUT);

		return myStepBuilderFactory.get(TERM_CONCEPTS_UNIQUE_VERSION_DELETE_STEP_NAME)
			.<Long, Long>chunk(1)
			.reader(batchTermCodeSystemUniqueVersionDeleteReader())
			.writer(batchTermConceptsDeleteWriter())
			.transactionAttribute(attribute)
			.build();
	}


	@Bean(name = TERM_CODE_SYSTEM_UNIQUE_VERSION_DELETE_STEP_NAME)
	public Step termCodeSystemUniqueVersionDeleteStep() {
		return myStepBuilderFactory.get(TERM_CODE_SYSTEM_UNIQUE_VERSION_DELETE_STEP_NAME)
			.<Long, Long>chunk(1)
			.reader(batchTermCodeSystemUniqueVersionDeleteReader())
			.writer(batchTermCodeSystemVersionDeleteWriter())
			.build();
	}


	@Bean
	@StepScope
	public BatchTermCodeSystemUniqueVersionDeleteReader batchTermCodeSystemUniqueVersionDeleteReader() {
		return new BatchTermCodeSystemUniqueVersionDeleteReader();
	}



}
