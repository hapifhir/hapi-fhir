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

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportLoincJobAppCtx {

	public static final String IMPORT_TERM_LOINC = "IMPORT_TERM_LOINC";
	public static final String STEP_ID_FINALIZE_IMPORT = "finalize-import";
	public static final String STEP_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION = "chunk-concepts-for-closure-generation";

	private final DaoRegistry myDaoRegistry;
	private final ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	private final IJobPersistence myJobPersistence;

	public ImportLoincJobAppCtx(DaoRegistry myDaoRegistry, ITermCodeSystemStorageSvc theTermCodeSystemStorageSvc, IJobPersistence theJobPersistence) {
		this.myDaoRegistry = myDaoRegistry;
		this.myTermCodeSystemStorageSvc = theTermCodeSystemStorageSvc;
		this.myJobPersistence = theJobPersistence;
	}

	@Bean
	public JobDefinition<ImportLoincJobParameters> importLoincJobDefinition() {
		return JobDefinition.newBuilder()
			.setInitialStatus(StatusEnum.BUILDING)
			.setJobDefinitionId(IMPORT_TERM_LOINC)
			.setJobDescription("Import Terminology - LOINC")
			.setJobDefinitionVersion(1)
			.gatedExecution()
			.setParametersType(ImportLoincJobParameters.class)
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
				importLoincStep4HandleHierarchyConcepts())
			.addIntermediateStep(
				"import-hierarchy",
				"Import LOINC hierarchy",
				TerminologyFileSetJson.class,
				importLoincStep3HandleHierarchy())
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
				"import-part-link",
				"Import LOINC Part Link File",
				TerminologyFileSetJson.class,
				importLoincStep16PartLink())
			.addIntermediateStep(
				"import-consumer-name",
				"Import LOINC Consumer Names",
				TerminologyFileSetJson.class,
				importLoincStep17ConsumerName())
			.addIntermediateStep(
				"import-coding-properties",
				"Import LOINC Coding Properties",
				TerminologyFileSetJson.class,
				importLoincStep18CodingProperties())
			.addIntermediateStep(
				"import-linguistic-variant",
				"Import LOINC Linguistic Variants",
				TerminologyFileSetJson.class,
				importLoincStep19LinguisticVariant())
			.addIntermediateStep(STEP_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION,
				"Create work chunks for calculating concept closures",
				TerminologyFileSetJson.class,
				importLoincStep20ChunkConceptsForClosureGeneration())
			.addIntermediateStep("generate-concept-closures",
				"Generate concept closures",
				TerminologyFileSetJson.class,
				importLoincStep21GenerateConceptClosures())
			.addFinalReducerStep(
				STEP_ID_FINALIZE_IMPORT,
				"Finalize LOINC Import",
				ImportTerminologyResultJson.class,
				importLoincStep21Finalize())
			.build();
	}

	/**
	 * Step 1: Expand LOINC distribution ZIP into files
	 */
	@Bean
	public ImportLoincStep1ExpandDistributionIntoFilesStep importLoincStep1ExpandDistributionIntoFiles() {
		return new ImportLoincStep1ExpandDistributionIntoFilesStep();
	}

	/**
	 * Step 2: Import LOINC concepts
	 */
	@Bean
	public ImportLoincStep2HandleConcepts importLoincStep2Concepts() {
		return new ImportLoincStep2HandleConcepts();
	}

	/**
	 * Step 3: Import LOINC hierarchy
	 */
	@Bean
	public ImportLoincStep3HandleHierarchy importLoincStep3HandleHierarchy() {
		return new ImportLoincStep3HandleHierarchy();
	}

	/**
	 * Step 4: Import LOINC hierarchy Concepts
	 */
	@Bean
	public ImportLoincStep4HandleHierarchyConcepts importLoincStep4HandleHierarchyConcepts() {
		return new ImportLoincStep4HandleHierarchyConcepts();
	}

	/**
	 * Step 5: Import LOINC answer lists
	 */
	@Bean
	public ImportLoincStep5HandleAnswerLists importLoincStep5AnswerLists() {
		return new ImportLoincStep5HandleAnswerLists();
	}

	/**
	 * Step 6: Import LOINC answer list links
	 */
	@Bean
	public ImportLoincStep6HandleAnswerListLinks importLoincStep6AnswerListLinks() {
		return new ImportLoincStep6HandleAnswerListLinks();
	}

	/**
	 * Step 7: Import RSNA Playbook
	 */
	@Bean
	public ImportLoincStep7HandleRsnaPlaybook importLoincStep7RsnaPlaybook() {
		return new ImportLoincStep7HandleRsnaPlaybook();
	}

	/**
	 * Step 8: Import Part-Related Code Mappings
	 */
	@Bean
	public ImportLoincStep8HandlePartRelatedCodeMapping importLoincStep8PartRelatedCodeMapping() {
		return new ImportLoincStep8HandlePartRelatedCodeMapping();
	}

	/**
	 * Step 9: Import Document Ontology
	 */
	@Bean
	public ImportLoincStep9HandleDocumentOntology importLoincStep9HandleDocumentOntology() {
		return new ImportLoincStep9HandleDocumentOntology();
	}

	/**
	 * Step 10: Universal Lab Order Set
	 */
	@Bean
	public ImportLoincStep10HandleUniversalLabOrderSet importLoincStep10HandleUniversalLabOrderSet() {
		return new ImportLoincStep10HandleUniversalLabOrderSet();
	}

	/**
	 * Step 11: IEEE Medical Device Code
	 */
	@Bean
	public ImportLoincStep11HandleIeeeMedicalDeviceCode importLoincStep11HandleIeeeMedicalDeviceCode() {
		return new ImportLoincStep11HandleIeeeMedicalDeviceCode();
	}

	/**
	 * Step 12: Imaging Document Code
	 */
	@Bean
	public ImportLoincStep12ImagingDocumentCode importLoincStep12ImagingDocumentCode() {
		return new ImportLoincStep12ImagingDocumentCode();
	}

	/**
	 * Step 13: Group File
	 */
	@Bean
	public ImportLoincStep13GroupFile importLoincStep13GroupFile() {
		return new ImportLoincStep13GroupFile();
	}

	/**
	 * Step 14: Group Terms File
	 */
	@Bean
	public ImportLoincStep14GroupTermsFile importLoincStep14GroupTermsFile() {
		return new ImportLoincStep14GroupTermsFile();
	}

	/**
	 * Step 15: Parent Group File
	 */
	@Bean
	public ImportLoincStep15ParentGroupFile importLoincStep15ParentGroupFile() {
		return new ImportLoincStep15ParentGroupFile();
	}

	/**
	 * Step 16: Part Link
	 */
	@Bean
	public ImportLoincStep16PartLink importLoincStep16PartLink() {
		return new ImportLoincStep16PartLink();
	}

	/**
	 * Step 17: Consumer Name
	 */
	@Bean
	public ImportLoincStep17ConsumerName importLoincStep17ConsumerName() {
		return new ImportLoincStep17ConsumerName();
	}

	/**
	 * Step 18: Coding Properties
	 */
	@Bean
	public ImportLoincStep18CodingProperties importLoincStep18CodingProperties() {
		return new ImportLoincStep18CodingProperties();
	}

	/**
	 * Step 19: Linguistic Variant
	 */
	@Bean
	public ImportLoincStep19LinguisticVariant importLoincStep19LinguisticVariant() {
		return new ImportLoincStep19LinguisticVariant();
	}

	/**
	 * Step 20: Chunk Concepts for Closure Generation
	 */
	@Bean
	public ImportLoincStep20ChunkConceptsForGeneratingClosure importLoincStep20ChunkConceptsForClosureGeneration() {
		return new ImportLoincStep20ChunkConceptsForGeneratingClosure();
	}

	/**
	 * Step 21: Generate concept closures
	 */
	@Bean
	public ImportLoincStep21GenerateConceptClosures importLoincStep21GenerateConceptClosures() {
		return new ImportLoincStep21GenerateConceptClosures();
	}

	/**
	 * Final reducer step
	 */
	@Bean
	public ImportLoincStep22Finalize importLoincStep21Finalize() {
		return new ImportLoincStep22Finalize(myDaoRegistry, myTermCodeSystemStorageSvc, myJobPersistence);
	}

}
