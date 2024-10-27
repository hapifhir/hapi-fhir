/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.termcodesystem;

import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete.DeleteCodeSystemCompletionHandler;
import ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete.DeleteCodeSystemConceptsByVersionStep;
import ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete.DeleteCodeSystemStep;
import ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete.DeleteCodeSystemVersionStep;
import ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete.ReadTermConceptVersionsStep;
import ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete.TermCodeSystemDeleteJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemversiondelete.DeleteCodeSystemVersionCompletionHandler;
import ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemversiondelete.DeleteCodeSystemVersionFinalStep;
import ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemversiondelete.DeleteCodeSystemVersionFirstStep;
import ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemversiondelete.DeleteCodeSystemVersionParameterValidator;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.jpa.term.models.CodeSystemVersionPIDResult;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteVersionJobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TermCodeSystemJobConfig {
	/**
	 * TermCodeSystem delete
	 */
	public static final String TERM_CODE_SYSTEM_DELETE_JOB_NAME = "termCodeSystemDeleteJob";

	/**
	 * TermCodeSystemVersion delete
	 */
	public static final String TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME = "termCodeSystemVersionDeleteJob";

	@Autowired
	private ITermCodeSystemDeleteJobSvc myITermCodeSystemSvc;

	/**
	 * Delete code system version job.
	 * Deletes only a specific code system version
	 */
	@Bean
	public JobDefinition<TermCodeSystemDeleteVersionJobParameters> termCodeSystemVersionDeleteJobDefinition() {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME)
				.setJobDescription("Term code system version job delete")
				.setJobDefinitionVersion(1)
				.gatedExecution()
				.setParametersType(TermCodeSystemDeleteVersionJobParameters.class)
				.setParametersValidator(deleteCodeSystemVersionPrameterValidator())
				.addFirstStep(
						"DeleteCodeSystemVersionFirstStep",
						"A first step for deleting code system versions; deletes the concepts for a provided code system version",
						CodeSystemVersionPIDResult.class,
						deleteCodeSystemVersionFirstStep())
				.addLastStep(
						"DeleteCodeSystemVersionFinalStep",
						"Deletes the code system version",
						deleteCodeSystemVersionFinalStep())
				.completionHandler(deleteCodeSystemVersionCompletionHandler())
				.errorHandler(deleteCodeSystemVersionCompletionHandler())
				.build();
	}

	/**
	 * Delete Code System Job
	 * Deletes all code system versions, before deleting the code system itself
	 */
	@Bean
	public JobDefinition<TermCodeSystemDeleteJobParameters> termCodeSystemDeleteJobDefinition() {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(TERM_CODE_SYSTEM_DELETE_JOB_NAME)
				.setJobDescription("Term code system job delete")
				.setJobDefinitionVersion(1)
				.gatedExecution()
				.setParametersType(TermCodeSystemDeleteJobParameters.class)
				.setParametersValidator(codeSystemDeleteParameterValidator())
				.addFirstStep(
						"FetchVersionsStep",
						"Fetches all term code system version PIDs for given Code System PID",
						CodeSystemVersionPIDResult.class,
						readCodeSystemVersionsStep())
				.addIntermediateStep(
						"DeleteCodeSystemConceptsByVersionPidStep",
						"Deletes the concept links, concept properties, concept designations, and concepts associated with a given code system version PID",
						CodeSystemVersionPIDResult.class,
						deleteCodeSystemConceptsStep())
				.addIntermediateStep(
						"DeleteCodeSystemVersionStep",
						"Deletes the specified code system version",
						CodeSystemVersionPIDResult.class,
						deleteCodeSystemVersionsStep())
				.addFinalReducerStep(
						"DeleteCodeSystemStep",
						"Deletes the code system itself",
						VoidModel.class,
						deleteCodeSystemFinalStep())
				.completionHandler(deleteCodeSystemCompletionHandler())
				.errorHandler(deleteCodeSystemCompletionHandler())
				.build();
	}

	/** delete codesystem job **/
	@Bean
	public TermCodeSystemDeleteJobParametersValidator codeSystemDeleteParameterValidator() {
		return new TermCodeSystemDeleteJobParametersValidator();
	}

	@Bean
	public ReadTermConceptVersionsStep readCodeSystemVersionsStep() {
		return new ReadTermConceptVersionsStep(myITermCodeSystemSvc);
	}

	@Bean
	public DeleteCodeSystemConceptsByVersionStep deleteCodeSystemConceptsStep() {
		return new DeleteCodeSystemConceptsByVersionStep(myITermCodeSystemSvc);
	}

	@Bean
	public DeleteCodeSystemVersionStep deleteCodeSystemVersionsStep() {
		return new DeleteCodeSystemVersionStep(myITermCodeSystemSvc);
	}

	@Bean
	public DeleteCodeSystemStep deleteCodeSystemFinalStep() {
		return new DeleteCodeSystemStep(myITermCodeSystemSvc);
	}

	@Bean
	public DeleteCodeSystemCompletionHandler deleteCodeSystemCompletionHandler() {
		return new DeleteCodeSystemCompletionHandler(myITermCodeSystemSvc);
	}

	/** Delete code system version job **/
	@Bean
	public DeleteCodeSystemVersionParameterValidator deleteCodeSystemVersionPrameterValidator() {
		return new DeleteCodeSystemVersionParameterValidator();
	}

	@Bean
	public DeleteCodeSystemVersionFirstStep deleteCodeSystemVersionFirstStep() {
		return new DeleteCodeSystemVersionFirstStep(myITermCodeSystemSvc);
	}

	@Bean
	public DeleteCodeSystemVersionFinalStep deleteCodeSystemVersionFinalStep() {
		return new DeleteCodeSystemVersionFinalStep(myITermCodeSystemSvc);
	}

	@Bean
	public DeleteCodeSystemVersionCompletionHandler deleteCodeSystemVersionCompletionHandler() {
		return new DeleteCodeSystemVersionCompletionHandler(myITermCodeSystemSvc);
	}
}
