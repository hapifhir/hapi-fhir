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

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration artifacts common to TermCodeSystemDeleteJobConfig and TermCodeSystemVersionDeleteJobConfig
 **/
@Configuration
public class BaseTermCodeSystemDeleteJobConfig {

	protected static final int TERM_CONCEPT_DELETE_TIMEOUT = 60 * 2; // two minutes

	@Autowired
	protected JobBuilderFactory myJobBuilderFactory;

	@Autowired
	protected StepBuilderFactory myStepBuilderFactory;


	@Bean
	public BatchTermCodeSystemVersionDeleteWriter batchTermCodeSystemVersionDeleteWriter() {
		return new BatchTermCodeSystemVersionDeleteWriter();
	}

	@Bean
	public BatchConceptRelationsDeleteWriter batchConceptRelationsDeleteWriter() {
		return new BatchConceptRelationsDeleteWriter();
	}

	@Bean
	public BatchTermConceptsDeleteWriter batchTermConceptsDeleteWriter() {
		return new BatchTermConceptsDeleteWriter();
	}




}
