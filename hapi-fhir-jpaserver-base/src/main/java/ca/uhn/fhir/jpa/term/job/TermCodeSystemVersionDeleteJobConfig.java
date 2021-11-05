package ca.uhn.fhir.jpa.term.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_VERSION_DELETE_STEP_NAME;

/**
 * Configuration for batch job which deletes a specific TermCodeSystemVersion and its related,
 * TermConceptProperty(es), TermConceptDesignation(s), and TermConceptParentChildLink(s)
 **/
@Configuration
public class TermCodeSystemVersionDeleteJobConfig {

	@Autowired
	private JobBuilderFactory myJobBuilderFactory;

	@Autowired
	private StepBuilderFactory myStepBuilderFactory;


	@Bean(name = TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)
	@Lazy
	public Job termCodeSystemVersionDeleteJob() {
		return myJobBuilderFactory.get(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)
			.validator(termCodeSystemVersionDeleteJobParameterValidator())
			.start(termCodeSystemVersionDeleteStep())
			.build();
	}


	@Bean
	public JobParametersValidator termCodeSystemVersionDeleteJobParameterValidator() {
		return new TermCodeSystemVersionDeleteJobParameterValidator();
	}


	@Bean
	public Step termCodeSystemVersionDeleteStep() {
		return myStepBuilderFactory.get(TERM_CODE_SYSTEM_VERSION_DELETE_STEP_NAME)
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


	@Bean
	public ItemWriter<? super Long> batchTermCodeSystemVersionDeleteWriter() {
		return new BatchTermCodeSystemVersionDeleteWriter();
	}



}
