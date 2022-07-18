package ca.uhn.fhir.batch2.jobs.termcodesystem;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemVersionPidResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;

@Configuration
public class TermCodeSystemJobConfig {

	/**
	 * Delete code system version
	 */
	/*
	 * TermCodeSystemVersionDeleteJobParameterValidator
	 * 1 single TermCodeSystemVersionPid (read in as parameter...)
	 * ... same 3 steps as below
	 *
	 */

	/**
	 * Delete code system
	 * @return
	 */
	@Bean
	public JobDefinition<TermCodeSystemDeleteJobParameters> termCodeSystemDeleteJobDefinition() {
		return JobDefinition
			.newBuilder()
			.setJobDefinitionId(TERM_CODE_SYSTEM_DELETE_JOB_NAME)
			.setJobDescription("Term code system job delete")
			.setJobDefinitionVersion(1)
			.gatedExecution()
			.setParametersType(TermCodeSystemDeleteJobParameters.class)
			.setParametersValidator(validator())
			.addFirstStep(
				"FetchVersionsStep",
				"Fetches all term code system version PIDs",
				TermCodeSystemVersionPidResult.class,
				readVersionsStep()
			)
			.addIntermediateStep(
				"DeleteLinksPropsAndDesignationsStep",
				"Deletes the links, properties, and designations associated with any given code system version PID",
				TermCodeSystemVersionPidResult.class,
				deleteLinksPropertiesAndDesignationsStep()
			)
			.addIntermediateStep(
				"DeleteCodeSystemVersionStep",
				"Deletes the specified code system version",
				TermCodeSystemVersionPidResult.class,
				deleteCodeSystemVersionsStep()
			)
			.addLastStep(
				"DeleteCodeSystemStep",
				"Deletes the code system proper",
				deleteCodeSystemFinalStep()
			)
			.build();
	}

	@Bean
	private TermCodeSystemDeleteJobParametersValidator validator() {
		return new TermCodeSystemDeleteJobParametersValidator(); // TermCodeSystemDeleteJobParameterValidator
	}

	@Bean
	private ReadTermConceptVersionsStep readVersionsStep() {
		return new ReadTermConceptVersionsStep();
	}

	@Bean
	private DeleteLinksPropertiesAndDesignationsStep deleteLinksPropertiesAndDesignationsStep() {
		return new DeleteLinksPropertiesAndDesignationsStep();
	}

	@Bean
	private DeleteCodeSystemVersionStep deleteCodeSystemVersionsStep() {
		return new DeleteCodeSystemVersionStep();
	}

	@Bean
	private DeleteCodeSystemStep deleteCodeSystemFinalStep() {
		return new DeleteCodeSystemStep();
	}
}
