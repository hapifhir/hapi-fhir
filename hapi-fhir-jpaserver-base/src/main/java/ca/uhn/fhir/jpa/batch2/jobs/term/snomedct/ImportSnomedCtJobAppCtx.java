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
package ca.uhn.fhir.jpa.batch2.jobs.term.snomedct;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyStepChunkConceptsForGeneratingClosure;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyStepFinalize;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyStepGenerateConceptClosures;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_FINALIZE_IMPORT;

/**
 * This file is the Batch2 Job Definition for the SNOMED CT Import job.
 * See the intividual step bean definitions, starting with {@link #importSnomedCtStep1ExpandDistributionIntoFiles()}
 * to see descriptions of how this job works.
 */
@Configuration
public class ImportSnomedCtJobAppCtx {

	public static final String JOB_ID_IMPORT_TERM_SNOMED_CT = "IMPORT_TERM_SNOMED_CT";

	private final DaoRegistry myDaoRegistry;
	private final ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	private final IJobPersistence myJobPersistence;
	private final IHapiTransactionService myTxService;

	public ImportSnomedCtJobAppCtx(
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
	 * See the intividual step bean definitions, starting with {@link #importSnomedCtStep1ExpandDistributionIntoFiles()}
	 * to see descriptions of how this job works.
	 */
	@Bean
	public JobDefinition<ImportTerminologyJobParameters> importSnomedJobDefinition() {
		return JobDefinition.newBuilder()
				.setInitialStatus(StatusEnum.BUILDING)
				.setJobDefinitionId(JOB_ID_IMPORT_TERM_SNOMED_CT)
				.setJobDescription("Import Terminology - SNOMED CT")
				.setJobDefinitionVersion(1)
				.gatedExecution()
				.setParametersType(ImportTerminologyJobParameters.class)
				.setParametersValidator(new ImportSnomedCtJobParametersValidator())
				.addFirstStep(
						"expand-zip",
						"Expand Snomed distribution",
						TerminologyFileSetJson.class,
						importSnomedCtStep1ExpandDistributionIntoFiles())
				.addIntermediateStep(
						"import-concepts",
						"Import SNOMED CT concepts",
						TerminologyFileSetJson.class,
						importSnomedCtStep2Descriptions())
				.addIntermediateStep(
						"import-relationships",
						"Import SNOMED CT Relationships",
						TerminologyFileSetJson.class,
						importSnomedCtStep3Relationship())
				.addIntermediateStep(
						STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION,
						"Create work chunks for calculating concept closures",
						TerminologyFileSetJson.class,
						importSnomedStep21ChunkConceptsForClosureGeneration())
				.addIntermediateStep(
						"generate-concept-closures",
						"Generate concept closures",
						TerminologyFileSetJson.class,
						importSnomedStep22GenerateConceptClosures())
				.addFinalReducerStep(
						STEP_ID_FINALIZE_IMPORT,
						"Finalize Snomed Import",
						ImportTerminologyResultJson.class,
						importSnomedStep23Finalize())
				.build();
	}

	/**
	 * Step 1: Expand SNOMED CT distribution ZIP into files.
	 * This step extracts the various CSV files from within the Snomed.zip distribution into fragments
	 * (i.e. it splits each CSV we care about into smaller subsets for processing), then attaches
	 * those fragments to the job as
	 * {@link IJobPersistence#storeNewAttachment(String, AttachmentDetails) attachments}
	 * so that they can be processed by the subsequent steps. Finally, it sends notifications to the
	 * steps that follow about those fragments.
	 */
	@Bean
	public IJobStepWorker<ImportTerminologyJobParameters, VoidModel, TerminologyFileSetJson>
			importSnomedCtStep1ExpandDistributionIntoFiles() {
		return new ImportSnomedCtStep1ExpandDistributionIntoFilesStep();
	}

	/**
	 * Step 2: Import SNOMED CT concepts.
	 * This step processes the primary "sct2_Description_Full" file, which contains the main Snomed concepts.
	 */
	@Bean
	public ImportSnomedCtStep2HandleDescription importSnomedCtStep2Descriptions() {
		return new ImportSnomedCtStep2HandleDescription();
	}

	/**
	 * Step 3: Import SNOMED CT relationships.
	 * This step processes the primary "Snomed.csv" file, which contains the main Snomed concepts.
	 */
	@Bean
	public ImportSnomedCtStep3HandleRelationship importSnomedCtStep3Relationship() {
		return new ImportSnomedCtStep3HandleRelationship();
	}

	/**
	 * Step 21: Chunk Concepts for Closure Generation
	 * When importing properties, the {@link TermConcept#getParentPidsAsString()} property contains a closure of
	 * all the parent concepts. We don't calculate it initially because we add hirarchy over multiple work chunks,
	 * so instead we calculate it at the end. This step queries the database for the full set of concepts, and
	 * creates work chunks for {@link #importSnomedStep22GenerateConceptClosures()} to process.
	 */
	@Bean
	public ImportTerminologyStepChunkConceptsForGeneratingClosure<ImportTerminologyJobParameters>
			importSnomedStep21ChunkConceptsForClosureGeneration() {
		return new ImportTerminologyStepChunkConceptsForGeneratingClosure<>();
	}

	/**
	 * Step 22: Generate concept closures
	 * This step receives work chunks from {@link #importSnomedStep21ChunkConceptsForClosureGeneration()},
	 * and calculates the closure of parent concepts for each concept. The closure is a list of all parent
	 * concept PIDs and is stored in {@link TermConcept#getParentPidsAsString()}.
	 */
	@Bean
	public ImportTerminologyStepGenerateConceptClosures<ImportTerminologyJobParameters>
			importSnomedStep22GenerateConceptClosures() {
		return new ImportTerminologyStepGenerateConceptClosures<>();
	}

	/**
	 * Final reducer step
	 * This step has a few responsibilities:
	 * <ul>
	 *     <li>
	 *         Any previous steps which created ValueSets have created them with status 'draft' so that
	 *     	   they are not candidates for pre-expansion until we are done importing and updating concepts.
	 *     	   This step moves them to 'active' status.
	 *     </li>
	 *     <li>
	 *         Aggregates step outcomes and generates a report
	 *     </li>
	 * </ul>
	 */
	@Bean
	public ImportTerminologyStepFinalize<ImportTerminologyJobParameters> importSnomedStep23Finalize() {
		return new ImportTerminologyStepFinalize<>(
				myDaoRegistry, myTermCodeSystemStorageSvc, myJobPersistence, myTxService);
	}
}
