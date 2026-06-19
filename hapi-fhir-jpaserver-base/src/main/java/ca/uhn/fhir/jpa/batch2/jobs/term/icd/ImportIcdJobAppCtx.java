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
package ca.uhn.fhir.jpa.batch2.jobs.term.icd;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParametersValidator;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyStepChunkConceptsForGeneratingClosure;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyStepFinalize;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyStepGenerateConceptClosures;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10.ImportIcd10Step1ExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10.ImportIcd10Step2HandleConcepts;
import ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10cm.ImportIcd10CmStep1ExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10cm.ImportIcd10CmStep2HandleConcepts;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.STEP_ID_FINALIZE_IMPORT;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.STEP_ID_GENERATE_CONCEPT_CLOSURES;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.STEP_WEIGHT_FINALIZE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.STEP_WEIGHT_GENERATE_CONCEPT_CLOSURES;

/**
 * This file is the Batch2 Job Definition for the SNOMED CT Import job.
 * See the individual step bean definitions, starting with {@link #importIcd10Step1ExpandDistributionIntoFiles()}
 * to see descriptions of how this job works.
 */
@Configuration
public class ImportIcdJobAppCtx {

	public static final String JOB_ID_IMPORT_ICD_10 = "IMPORT_ICD_10";
	public static final String JOB_ID_IMPORT_ICD_10_CM = "IMPORT_ICD_10_CM";

	private final DaoRegistry myDaoRegistry;
	private final ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	private final IJobPersistence myJobPersistence;
	private final IHapiTransactionService myTxService;

	public ImportIcdJobAppCtx(
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
	public JobDefinition<ImportTerminologyJobParameters> importIcd10JobDefinition() {
		return JobDefinition.newBuilder()
				.setInitialStatus(StatusEnum.BUILDING)
				.setJobDefinitionId(JOB_ID_IMPORT_ICD_10)
				.setJobDescription("Import Terminology - ICD-10")
				.setJobDefinitionVersion(1)
				.gatedExecution()
				.setParametersType(ImportTerminologyJobParameters.class)
				.setParametersValidator(new ImportTerminologyJobParametersValidator())
				.addFirstStep(
						"expand-zip",
						"Expand ICD-10 distribution file",
						TerminologyFileSetJson.class,
						importIcd10Step1ExpandDistributionIntoFiles())
				.addIntermediateStep(
						"import-concepts",
						"Import ICD-10 concepts",
						TerminologyFileSetJson.class,
						importIcd10Step2Concepts())
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
						importIcd10StepFinalize())
				.setStepWeightForProgressCalculator(STEP_ID_FINALIZE_IMPORT, 0.01)
				.build();
	}

	/**
	 * ICD-10-CM Job Definition
	 */
	@Bean
	public JobDefinition<ImportTerminologyJobParameters> importIcd10CmJobDefinition() {
		return JobDefinition.newBuilder()
				.setInitialStatus(StatusEnum.BUILDING)
				.setJobDefinitionId(JOB_ID_IMPORT_ICD_10_CM)
				.setJobDescription("Import Terminology - ICD-10-CM")
				.setJobDefinitionVersion(1)
				.gatedExecution()
				.setParametersType(ImportTerminologyJobParameters.class)
				.setParametersValidator(new ImportTerminologyJobParametersValidator())
				.addFirstStep(
						"expand-zip",
						"Expand ICD-10-CM distribution file",
						TerminologyFileSetJson.class,
						importIcd10CmStep1ExpandDistributionIntoFiles())
				.addIntermediateStep(
						"import-concepts",
						"Import ICD-10-CM concepts",
						TerminologyFileSetJson.class,
						importIcd10CmStep2Concepts())
				.addIntermediateStep(
						STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION,
						"Create work chunks for calculating concept closures",
						TerminologyFileSetJson.class,
						importIcdStepChunkConceptsForClosureGeneration())
				.addIntermediateStep(
						STEP_ID_GENERATE_CONCEPT_CLOSURES,
						"Generate concept closures",
						TerminologyFileSetJson.class,
						importIcdStepGenerateConceptClosures())
				// This step doesn't gain any work chunks until the previous step, we want to give it
				// a fixed portion of the overall progress
				.setStepWeightForProgressCalculator(STEP_ID_GENERATE_CONCEPT_CLOSURES, STEP_WEIGHT_GENERATE_CONCEPT_CLOSURES)
				.addFinalReducerStep(
						STEP_ID_FINALIZE_IMPORT,
						"Finalize ICD-10 Import",
						ImportTerminologyResultJson.class,
						importIcd10StepFinalize())
				// This step takes very little time and shouldn't factor significantly into the progress
				.setStepWeightForProgressCalculator(STEP_ID_FINALIZE_IMPORT, STEP_WEIGHT_FINALIZE)
				.build();
	}

	@Bean
	public ImportIcd10Step1ExpandDistributionIntoFilesStep importIcd10Step1ExpandDistributionIntoFiles() {
		return new ImportIcd10Step1ExpandDistributionIntoFilesStep();
	}

	@Bean
	public ImportIcd10Step2HandleConcepts importIcd10Step2Concepts() {
		return new ImportIcd10Step2HandleConcepts();
	}

	@Bean
	public ImportIcd10CmStep1ExpandDistributionIntoFilesStep importIcd10CmStep1ExpandDistributionIntoFiles() {
		return new ImportIcd10CmStep1ExpandDistributionIntoFilesStep();
	}

	@Bean
	public ImportIcd10CmStep2HandleConcepts importIcd10CmStep2Concepts() {
		return new ImportIcd10CmStep2HandleConcepts();
	}

	@Bean
	public ImportTerminologyStepChunkConceptsForGeneratingClosure<ImportTerminologyJobParameters>
			importIcdStepChunkConceptsForClosureGeneration() {
		return new ImportTerminologyStepChunkConceptsForGeneratingClosure<>();
	}

	@Bean
	public ImportTerminologyStepGenerateConceptClosures<ImportTerminologyJobParameters>
			importIcdStepGenerateConceptClosures() {
		return new ImportTerminologyStepGenerateConceptClosures<>();
	}

	@Bean
	public ImportTerminologyStepFinalize<ImportTerminologyJobParameters> importIcd10StepFinalize() {
		return new ImportTerminologyStepFinalize<>(
				myDaoRegistry, myTermCodeSystemStorageSvc, myJobPersistence, myTxService);
	}

	@Bean
	public ImportTerminologyStepFinalize<ImportTerminologyJobParameters> importIcd10CmStepFinalize() {
		return new ImportTerminologyStepFinalize<>(
				myDaoRegistry, myTermCodeSystemStorageSvc, myJobPersistence, myTxService);
	}
}
