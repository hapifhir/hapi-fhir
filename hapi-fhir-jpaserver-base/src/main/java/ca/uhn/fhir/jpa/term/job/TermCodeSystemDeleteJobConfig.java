package ca.uhn.fhir.jpa.term.job;

import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_DELETE_STEP_NAME;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_VERSION_DELETE_STEP_NAME;

/**
 * Configuration for batch job which deletes a TermCodeSystem and its related TermCodeSystemVersion(s),
 * TermConceptProperty(es), TermConceptDesignation(s), and TermConceptParentChildLink(s)
 **/
@Configuration
public class TermCodeSystemDeleteJobConfig {

	@Autowired
	private JobBuilderFactory myJobBuilderFactory;

	@Autowired
	private StepBuilderFactory myStepBuilderFactory;

	@Autowired
	private ITermCodeSystemVersionDao myITermCodeSystemVersionDao;


	@Bean(name = TERM_CODE_SYSTEM_DELETE_JOB_NAME)
	@Lazy
	public Job termCodeSystemDeleteJob() {
		return myJobBuilderFactory.get(TERM_CODE_SYSTEM_DELETE_JOB_NAME)
			.validator(termCodeSystemDeleteJobParameterValidator())
			.start(termCodeSystemVersionsDeleteStep())
			.next(termCodeSystemDeleteStep())
			.build();
	}

	@Bean
	public JobParametersValidator termCodeSystemDeleteJobParameterValidator() {
		return new TermCodeSystemDeleteJobParameterValidator();
	}


	@Bean
	public Step termCodeSystemVersionsDeleteStep() {
		return myStepBuilderFactory.get(TERM_CODE_SYSTEM_VERSION_DELETE_STEP_NAME)
			.<Long, Long>chunk(1)
			.reader(batchTermCodeSystemVersionDeleteReader())
			.writer(batchTermCodeSystemVersionDeleteWriter())
			.build();
	}


	@Bean
	@StepScope
	public BatchTermCodeSystemVersionDeleteReader batchTermCodeSystemVersionDeleteReader() {
		return new BatchTermCodeSystemVersionDeleteReader();
	}


	@Bean
	public BatchTermCodeSystemVersionDeleteWriter batchTermCodeSystemVersionDeleteWriter() {
		return new BatchTermCodeSystemVersionDeleteWriter();
	}


	@Bean
	public Step termCodeSystemDeleteStep() {
		return myStepBuilderFactory.get(TERM_CODE_SYSTEM_DELETE_STEP_NAME)
			.tasklet(termCodeSystemDeleteTasklet())
			.build();
	}


	@Bean
	public TermCodeSystemDeleteTasklet termCodeSystemDeleteTasklet() {
		return new TermCodeSystemDeleteTasklet();
	}


}
