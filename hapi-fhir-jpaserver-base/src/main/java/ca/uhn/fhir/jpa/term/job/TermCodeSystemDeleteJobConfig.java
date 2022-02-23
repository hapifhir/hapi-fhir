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

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_DELETE_STEP_NAME;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_VERSION_DELETE_STEP_NAME;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CONCEPTS_DELETE_STEP_NAME;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CONCEPT_RELATIONS_DELETE_STEP_NAME;

/**
 * Configuration for batch job which deletes a TermCodeSystem and its related TermCodeSystemVersion(s),
 * TermConceptProperty(es), TermConceptDesignation(s), and TermConceptParentChildLink(s)
 **/
@Configuration
public class TermCodeSystemDeleteJobConfig extends BaseTermCodeSystemDeleteJobConfig {


	@Bean(name = TERM_CODE_SYSTEM_DELETE_JOB_NAME)
	@Lazy
	public Job termCodeSystemDeleteJob() {
		return myJobBuilderFactory.get(TERM_CODE_SYSTEM_DELETE_JOB_NAME)
			.validator(termCodeSystemDeleteJobParameterValidator())
			.start(termConceptRelationsDeleteStep())
			.next(termConceptsDeleteStep())
			.next(termCodeSystemVersionDeleteStep())
			.next(termCodeSystemDeleteStep())
			.build();
	}

	@Bean
	public JobParametersValidator termCodeSystemDeleteJobParameterValidator() {
		return new TermCodeSystemDeleteJobParameterValidator();
	}

	/**
	 * This steps deletes TermConceptParentChildLink(s), TermConceptProperty(es) and TermConceptDesignation(s)
	 * related to TermConcept(s) of the TermCodeSystemVersion being deleted
	 */
	@Bean(name = TERM_CONCEPT_RELATIONS_DELETE_STEP_NAME)
	public Step termConceptRelationsDeleteStep() {
		return myStepBuilderFactory.get(TERM_CONCEPT_RELATIONS_DELETE_STEP_NAME)
			.<Long, Long>chunk(1)
			.reader(batchTermCodeSystemVersionDeleteReader())
			.writer(batchConceptRelationsDeleteWriter())
			.build();
	}

	/**
	 * This steps deletes TermConcept(s) of the TermCodeSystemVersion being deleted
	 */
	@Bean(name = TERM_CONCEPTS_DELETE_STEP_NAME)
	public Step termConceptsDeleteStep() {
		DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
		attribute.setTimeout(TERM_CONCEPT_DELETE_TIMEOUT);

		return myStepBuilderFactory.get(TERM_CONCEPTS_DELETE_STEP_NAME)
			.<Long, Long>chunk(1)
			.reader(batchTermCodeSystemVersionDeleteReader())
			.writer(batchTermConceptsDeleteWriter())
			.transactionAttribute(attribute)
			.build();
	}

	/**
	 * This steps deletes the TermCodeSystemVersion
	 */
	@Bean(name = TERM_CODE_SYSTEM_VERSION_DELETE_STEP_NAME)
	public Step termCodeSystemVersionDeleteStep() {
		return myStepBuilderFactory.get(TERM_CODE_SYSTEM_VERSION_DELETE_STEP_NAME)
			.<Long, Long>chunk(1)
			.reader(batchTermCodeSystemVersionDeleteReader())
			.writer(batchTermCodeSystemVersionDeleteWriter())
			.build();
	}

	@Bean(name = TERM_CODE_SYSTEM_DELETE_STEP_NAME)
	public Step termCodeSystemDeleteStep() {
		return myStepBuilderFactory.get(TERM_CODE_SYSTEM_DELETE_STEP_NAME)
			.tasklet(termCodeSystemDeleteTasklet())
			.build();
	}

	@Bean
	@StepScope
	public BatchTermCodeSystemVersionDeleteReader batchTermCodeSystemVersionDeleteReader() {
		return new BatchTermCodeSystemVersionDeleteReader();
	}

	@Bean
	public TermCodeSystemDeleteTasklet termCodeSystemDeleteTasklet() {
		return new TermCodeSystemDeleteTasklet();
	}

}
