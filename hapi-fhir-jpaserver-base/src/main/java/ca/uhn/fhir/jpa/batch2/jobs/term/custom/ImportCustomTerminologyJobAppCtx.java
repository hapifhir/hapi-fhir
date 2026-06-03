/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.batch2.jobs.term.custom;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.*;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_FINALIZE_IMPORT;

@Configuration
public class ImportCustomTerminologyJobAppCtx {

	public static final String JOB_ID_IMPORT_CUSTOM_TERMINOLOGY = "IMPORT_CUSTOM_TERMINOLOGY";

	private final DaoRegistry myDaoRegistry;
	private final ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	private final IJobPersistence myJobPersistence;
	private final IHapiTransactionService myTxService;

	public ImportCustomTerminologyJobAppCtx(
			DaoRegistry myDaoRegistry,
			ITermCodeSystemStorageSvc theTermCodeSystemStorageSvc,
			IJobPersistence theJobPersistence,
			IHapiTransactionService theTxService) {
		this.myDaoRegistry = myDaoRegistry;
		this.myTermCodeSystemStorageSvc = theTermCodeSystemStorageSvc;
		this.myJobPersistence = theJobPersistence;
		this.myTxService = theTxService;
	}

	/**
	 * ICD-10 Job Definition
	 */
	@Bean
	public JobDefinition<ImportTerminologyJobParameters> importCustomTerminologyJobDefinition() {
		return JobDefinition.newBuilder()
				.setInitialStatus(StatusEnum.BUILDING)
				.setJobDefinitionId(JOB_ID_IMPORT_CUSTOM_TERMINOLOGY)
				.setJobDescription("Import Terminology - Custom")
				.setJobDefinitionVersion(1)
				.gatedExecution()
				.setParametersType(ImportTerminologyJobParameters.class)
				.setParametersValidator(new ImportTerminologyJobParametersValidator())
				.addFirstStep(
						"expand-zip",
						"Expand custom terminology distribution file",
						TerminologyFileSetJson.class,
						importCustomTerminologyStep1ExpandDistributionIntoFiles())
				.addIntermediateStep(
						"import-concepts",
						"Import custom terminology concepts",
						TerminologyFileSetJson.class,
						importCustomTerminologyStep2Concepts())
				.addIntermediateStep(
						"import-properties",
						"Import custom terminology properties",
						TerminologyFileSetJson.class,
						importCustomTerminologyStep3Properties())
				.addIntermediateStep(
						"import-hierarchy",
						"Import custom terminology hierarchy",
						TerminologyFileSetJson.class,
						importCustomTerminologyStep4Hierarchy())
				.addIntermediateStep(
						STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION,
						"Create work chunks for calculating concept closures",
						TerminologyFileSetJson.class,
						importIcdStepChunkConceptsForClosureGeneration())
				.addIntermediateStep(
						"generate-concept-closures",
						"Generate concept closures",
						TerminologyFileSetJson.class,
						importIcdStepGenerateConceptClosures())
				.addFinalReducerStep(
						STEP_ID_FINALIZE_IMPORT,
						"Finalize ICD-10 Import",
						ImportTerminologyResultJson.class,
						importCustomTerminologyStepFinalize())
				.build();
	}

	@Bean
	public ImportCustomTerminologyStep1ExpandDistributionIntoFilesStep importCustomTerminologyStep1ExpandDistributionIntoFiles() {
		return new ImportCustomTerminologyStep1ExpandDistributionIntoFilesStep();
	}

	@Bean
	public ImportCustomTerminologyStep2HandleConcepts importCustomTerminologyStep2Concepts() {
		return new ImportCustomTerminologyStep2HandleConcepts();
	}

	@Bean
	public ImportCustomTerminologyStep3HandleProperties importCustomTerminologyStep3Properties() {
		return new ImportCustomTerminologyStep3HandleProperties();
	}

	@Bean
	public ImportCustomTerminologyStep4HandleHierarchy importCustomTerminologyStep4Hierarchy() {
		return new ImportCustomTerminologyStep4HandleHierarchy();
	}


	@Bean
	public ImportTerminologyStepChunkConceptsForGeneratingClosure<ImportTerminologyJobParameters>
			importIcdStepChunkConceptsForClosureGeneration() {
		return new ImportTerminologyStepChunkConceptsForGeneratingClosure<>();
	}

	@Bean
	public ImportTerminologyStepGenerateConceptClosures<ImportTerminologyJobParameters> importIcdStepGenerateConceptClosures() {
		return new ImportTerminologyStepGenerateConceptClosures<>();
	}

	@Bean
	public ImportTerminologyStepFinalize<ImportTerminologyJobParameters> importCustomTerminologyStepFinalize() {
		return new ImportTerminologyStepFinalize<>(
				myDaoRegistry, myTermCodeSystemStorageSvc, myJobPersistence, myTxService);
	}

}
