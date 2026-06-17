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
package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobPersistence;
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

/**
 * This file is the Batch2 Job Definition for the LOINC Import job.
 * See the individual step bean definitions, starting with {@link #importLoincStep1ExpandDistributionIntoFiles()}
 * to see descriptions of how this job works.
 */
@Configuration
public class ImportLoincJobAppCtx {

	public static final String JOB_ID_IMPORT_TERM_LOINC = "IMPORT_TERM_LOINC";
	public static final String STEP_ID_FINALIZE_IMPORT = "finalize-import";
	public static final String STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION = "chunk-concepts-for-closure-generation";
	public static final String STEP_ID_GENERATE_CONCEPT_CLOSURES = "generate-concept-closures";
	public static final String STEP_ID_IMPORT_PART_LINK_FILE = "import-part-link-file";

	private final DaoRegistry myDaoRegistry;
	private final ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	private final IJobPersistence myJobPersistence;
	private final IHapiTransactionService myTxService;

	public ImportLoincJobAppCtx(
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
	 * See the individual step bean definitions, starting with {@link #importLoincStep1ExpandDistributionIntoFiles()}
	 * to see descriptions of how this job works.
	 */
	@Bean
	public JobDefinition<ImportTerminologyJobParameters> importLoincJobDefinition() {
		return JobDefinition.newBuilder()
				.setInitialStatus(StatusEnum.BUILDING)
				.setJobDefinitionId(JOB_ID_IMPORT_TERM_LOINC)
				.setJobDescription("Import Terminology - LOINC")
				.setJobDefinitionVersion(1)
				.gatedExecution()
				.setParametersType(ImportTerminologyJobParameters.class)
				.setParametersValidator(new ImportLoincJobParametersValidator())
				.addFirstStep(
						"expand-zip",
						"Expand LOINC distribution",
						TerminologyFileSetJson.class,
						importLoincStep1ExpandDistributionIntoFiles())
				.addIntermediateStep(
						"import-concepts",
						"Import LOINC concepts",
						TerminologyFileSetJson.class,
						importLoincStep2Concepts())
				.addIntermediateStep(
						"import-hierarchy-concepts",
						"Import LOINC hierarchy Concepts",
						TerminologyFileSetJson.class,
						importLoincStep3HandleHierarchyConcepts())
				.addIntermediateStep(
						"import-hierarchy",
						"Import LOINC hierarchy",
						TerminologyFileSetJson.class,
						importLoincStep4HandleHierarchy())
				.addIntermediateStep(
						"import-answer-lists",
						"Import LOINC answer lists",
						TerminologyFileSetJson.class,
						importLoincStep5AnswerLists())
				.addIntermediateStep(
						"import-answer-list-links",
						"Import LOINC answer list links",
						TerminologyFileSetJson.class,
						importLoincStep6AnswerListLinks())
				.addIntermediateStep(
						"import-rsna-playbook",
						"Import LOINC RSNA playbook",
						TerminologyFileSetJson.class,
						importLoincStep7RsnaPlaybook())
				.addIntermediateStep(
						"import-part-related-code-mapping",
						"Import LOINC Part Related Code Mappings",
						TerminologyFileSetJson.class,
						importLoincStep8PartRelatedCodeMapping())
				.addIntermediateStep(
						"import-document-ontology",
						"Import LOINC Document Ontology",
						TerminologyFileSetJson.class,
						importLoincStep9HandleDocumentOntology())
				.addIntermediateStep(
						"import-univeral-lab-orderset",
						"Import LOINC Lab Order Set",
						TerminologyFileSetJson.class,
						importLoincStep10HandleUniversalLabOrderSet())
				.addIntermediateStep(
						"import-ieee-medical-device-code",
						"Import LOINC IEEE Medical Device Codes",
						TerminologyFileSetJson.class,
						importLoincStep11HandleIeeeMedicalDeviceCode())
				.addIntermediateStep(
						"import-imaging-document-code",
						"Import LOINC Imaging Document Codes",
						TerminologyFileSetJson.class,
						importLoincStep12ImagingDocumentCode())
				.addIntermediateStep(
						"import-group-file",
						"Import LOINC Group File",
						TerminologyFileSetJson.class,
						importLoincStep13GroupFile())
				.addIntermediateStep(
						"import-group-terms-file",
						"Import LOINC Group Terms File",
						TerminologyFileSetJson.class,
						importLoincStep14GroupTermsFile())
				.addIntermediateStep(
						"import-parent-group-file",
						"Import LOINC Parent Group File",
						TerminologyFileSetJson.class,
						importLoincStep15ParentGroupFile())
				.addIntermediateStep(
						"import-part-file",
						"Import LOINC Part File",
						TerminologyFileSetJson.class,
						importLoincStep16PartFile())
				.addIntermediateStep(
						STEP_ID_IMPORT_PART_LINK_FILE,
						"Import LOINC Part Link File",
						TerminologyFileSetJson.class,
						importLoincStep17PartLink())
				// Part link file uses a large number of smaller files, so limit its weight
				.setStepWeightForProgressCalculator(STEP_ID_IMPORT_PART_LINK_FILE, 0.1)
				.addIntermediateStep(
						"import-consumer-name",
						"Import LOINC Consumer Names",
						TerminologyFileSetJson.class,
						importLoincStep18ConsumerName())
				.addIntermediateStep(
						"import-coding-properties",
						"Import LOINC Coding Properties",
						TerminologyFileSetJson.class,
						importLoincStep19CodingProperties())
				.addIntermediateStep(
						"import-linguistic-variant",
						"Import LOINC Linguistic Variants",
						TerminologyFileSetJson.class,
						importLoincStep20LinguisticVariant())
				.addIntermediateStep(
						STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION,
						"Create work chunks for calculating concept closures",
						TerminologyFileSetJson.class,
						importLoincStep21ChunkConceptsForClosureGeneration())
				.addIntermediateStep(
						STEP_ID_GENERATE_CONCEPT_CLOSURES,
						"Generate concept closures",
						TerminologyFileSetJson.class,
						importLoincStep22GenerateConceptClosures())
				.setStepWeightForProgressCalculator(STEP_ID_GENERATE_CONCEPT_CLOSURES, 0.3)
				.addFinalReducerStep(
						STEP_ID_FINALIZE_IMPORT,
						"Finalize LOINC Import",
						ImportTerminologyResultJson.class,
						importLoincStep23Finalize())
				.setStepWeightForProgressCalculator(STEP_ID_FINALIZE_IMPORT, 0.01)
				.build();
	}

	/**
	 * Step 1: Expand LOINC distribution ZIP into files.
	 * This step extracts the various CSV files from within the Loinc.zip distribution into fragments
	 * (i.e. it splits each CSV we care about into smaller subsets for processing), then attaches
	 * those fragments to the job as
	 * {@link IJobPersistence#storeNewAttachment(String, AttachmentDetails) attachments}
	 * so that they can be processed by the subsequent steps. Finally, it sends notifications to the
	 * steps that follow about those fragments.
	 */
	@Bean
	public ImportLoincStep1ExpandDistributionIntoFilesStep importLoincStep1ExpandDistributionIntoFiles() {
		return new ImportLoincStep1ExpandDistributionIntoFilesStep();
	}

	/**
	 * Step 2: Import LOINC concepts.
	 * This step processes the primary "Loinc.csv" file, which contains the main LOINC concepts.
	 */
	@Bean
	public ImportLoincStep2HandleConcepts importLoincStep2Concepts() {
		return new ImportLoincStep2HandleConcepts();
	}

	/**
	 * Step 3: Import LOINC hierarchy Concepts
	 * This step processes the "MultiAxialHierarchy.csv" file, which contains the LOINC hierarchy concepts.
	 * In this step we are just loading the hierarchy concepts, not the hierarchies. The hierarchies have LOINC
	 * concepts from {@link ImportLoincStep2HandleConcepts Step 2} as leaf nodes, but many of the parents are new
	 * concepts defined by the hierarchy file itself.
	 * We add the concepts first so that we don't get collisions when adding the parent/child links.
	 */
	@Bean
	public ImportLoincStep3HandleHierarchyConcepts importLoincStep3HandleHierarchyConcepts() {
		return new ImportLoincStep3HandleHierarchyConcepts();
	}

	/**
	 * Step 4: Import LOINC hierarchy
	 * This step processes the "MultiAxialHierarchy.csv" file, adding the actual parent/child links.
	 */
	@Bean
	public ImportLoincStep4HandleHierarchy importLoincStep4HandleHierarchy() {
		return new ImportLoincStep4HandleHierarchy();
	}

	/**
	 * Step 5: Import LOINC answer lists
	 * This step creates LOINC "Answer List" ValueSets.
	 */
	@Bean
	public ImportLoincStep5HandleAnswerLists importLoincStep5AnswerLists() {
		return new ImportLoincStep5HandleAnswerLists();
	}

	/**
	 * Step 6: Import LOINC answer list links
	 * This step adds properties to LOINC concepts that associate them with specific answer lists.
	 */
	@Bean
	public ImportLoincStep6HandleAnswerListLinks importLoincStep6AnswerListLinks() {
		return new ImportLoincStep6HandleAnswerListLinks();
	}

	/**
	 * Step 7: Import RSNA Playbook
	 * This step adds ConceptMap mappings between LOINC concepts and the RadLex codesystem
	 */
	@Bean
	public ImportLoincStep7HandleRsnaPlaybook importLoincStep7RsnaPlaybook() {
		return new ImportLoincStep7HandleRsnaPlaybook();
	}

	/**
	 * Step 8: Import Part-Related Code Mappings
	 * This step adds ConceptMap mappings between LOINC concepts and a few other codesystems.
	 */
	@Bean
	public ImportLoincStep8HandlePartRelatedCodeMapping importLoincStep8PartRelatedCodeMapping() {
		return new ImportLoincStep8HandlePartRelatedCodeMapping();
	}

	/**
	 * Step 9: Import Document Ontology
	 * This step creates ValueSets and adds properties to some LOINC concepts relating to the
	 * LOINC Document Ontology.
	 */
	@Bean
	public ImportLoincStep9HandleDocumentOntology importLoincStep9HandleDocumentOntology() {
		return new ImportLoincStep9HandleDocumentOntology();
	}

	/**
	 * Step 10: Universal Lab Order Set
	 * This step creates a ValueSet for the Universal Lab Order Set.
	 */
	@Bean
	public ImportLoincStep10HandleUniversalLabOrderSet importLoincStep10HandleUniversalLabOrderSet() {
		return new ImportLoincStep10HandleUniversalLabOrderSet();
	}

	/**
	 * Step 11: IEEE Medical Device Code
	 * This step adds ConceptMap mappings between LOINC concepts and the IEEE Medical Device Code system.
	 */
	@Bean
	public ImportLoincStep11HandleIeeeMedicalDeviceCode importLoincStep11HandleIeeeMedicalDeviceCode() {
		return new ImportLoincStep11HandleIeeeMedicalDeviceCode();
	}

	/**
	 * Step 12: Imaging Document Code
	 * This step creates a ValueSet for the Imaging Document Code system.
	 */
	@Bean
	public ImportLoincStep12ImagingDocumentCode importLoincStep12ImagingDocumentCode() {
		return new ImportLoincStep12ImagingDocumentCode();
	}

	/**
	 * Step 13: Group File
	 * This step creates ValueSets for the LOINC Group File.
	 */
	@Bean
	public ImportLoincStep13GroupFile importLoincStep13GroupFile() {
		return new ImportLoincStep13GroupFile();
	}

	/**
	 * Step 14: Group Terms File
	 * This step adds codes to the ValueSets created by {@link ImportLoincStep13GroupFile}
	 */
	@Bean
	public ImportLoincStep14GroupTermsFile importLoincStep14GroupTermsFile() {
		return new ImportLoincStep14GroupTermsFile();
	}

	/**
	 * Step 15: Parent Group File
	 * This step creates ValueSets for the LOINC Parent Group File.
	 */
	@Bean
	public ImportLoincStep15ParentGroupFile importLoincStep15ParentGroupFile() {
		return new ImportLoincStep15ParentGroupFile();
	}

	/**
	 * Step 16: Part File
	 * This step adds properties to specific LOINC concepts from the LOINC Part File.
	 */
	@Bean
	public ImportLoincStep16PartFile importLoincStep16PartFile() {
		return new ImportLoincStep16PartFile();
	}

	/**
	 * Step 17: Part Link
	 * This step adds properties to specific LOINC concepts from the LOINC Part Link Files.
	 */
	@Bean
	public ImportLoincStep17PartLink importLoincStep17PartLink() {
		return new ImportLoincStep17PartLink();
	}

	/**
	 * Step 18: Consumer Name
	 * This step adds properties to specific LOINC concepts from the LOINC Consumer Name File.
	 */
	@Bean
	public ImportLoincStep18ConsumerName importLoincStep18ConsumerName() {
		return new ImportLoincStep18ConsumerName();
	}

	/**
	 * Step 19: Coding Properties
	 * This step adds properties to specific LOINC concepts from the LOINC Coding Properties File.
	 */
	@Bean
	public ImportLoincStep19CodingProperties importLoincStep19CodingProperties() {
		return new ImportLoincStep19CodingProperties();
	}

	/**
	 * Step 20: Linguistic Variant
	 * This step adds additional designations to concepts from the LOINC Linguistic Variant File.
	 * By default we don't add anything in this step, because these files are massive and add a long time
	 * to process, and it's unlikely that most people want all possible linguistic variants imported.
	 * Specific linguistic variants can be added via job properties.
	 */
	@Bean
	public ImportLoincStep20LinguisticVariant importLoincStep20LinguisticVariant() {
		return new ImportLoincStep20LinguisticVariant();
	}

	/**
	 * Step 21: Chunk Concepts for Closure Generation
	 * When importing properties, the {@link TermConcept#getParentPidsAsString()} property contains a closure of
	 * all the parent concepts. We don't calculate it initially because we add hirarchy over multiple work chunks,
	 * so instead we calculate it at the end. This step queries the database for the full set of concepts, and
	 * creates work chunks for {@link #importLoincStep22GenerateConceptClosures()} to process.
	 */
	@Bean
	public ImportTerminologyStepChunkConceptsForGeneratingClosure<ImportTerminologyJobParameters>
			importLoincStep21ChunkConceptsForClosureGeneration() {
		return new ImportTerminologyStepChunkConceptsForGeneratingClosure<>();
	}

	/**
	 * Step 22: Generate concept closures
	 * This step receives work chunks from {@link #importLoincStep21ChunkConceptsForClosureGeneration()},
	 * and calculates the closure of parent concepts for each concept. The closure is a list of all parent
	 * concept PIDs and is stored in {@link TermConcept#getParentPidsAsString()}.
	 */
	@Bean
	public ImportTerminologyStepGenerateConceptClosures<ImportTerminologyJobParameters>
			importLoincStep22GenerateConceptClosures() {
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
	public ImportTerminologyStepFinalize<ImportTerminologyJobParameters> importLoincStep23Finalize() {
		return new ImportTerminologyStepFinalize<>(
				myDaoRegistry, myTermCodeSystemStorageSvc, myJobPersistence, myTxService);
	}
}
