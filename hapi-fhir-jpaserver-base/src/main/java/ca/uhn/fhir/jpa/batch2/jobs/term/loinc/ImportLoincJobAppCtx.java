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
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportLoincJobAppCtx {

	public static final String JOB_ID_IMPORT_TERM_LOINC = "IMPORT_TERM_LOINC";
	public static final String STEP_ID_FINALIZE_IMPORT = "finalize-import";
	public static final String STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION = "chunk-concepts-for-closure-generation";
	/**
	 * This is <b>NOT</b> the LOINC CodeSystem URI! It is just
	 * the website URL to LOINC.
	 */
	public static final String LOINC_WEBSITE_URL = "https://loinc.org";
	public static final String REGENSTRIEF_INSTITUTE_INC = "Regenstrief Institute, Inc.";
	public static final String LOINC_IEEE_CM_ID = "loinc-to-ieee-11073-10101";
	public static final String LOINC_IEEE_CM_URI = "http://loinc.org/cm/loinc-to-ieee-11073-10101";
	public static final String LOINC_IEEE_CM_NAME = "LOINC/IEEE Device Code Mappings";
	public static final String CM_COPYRIGHT =
			"The LOINC/IEEE Medical Device Code Mapping Table contains content from IEEE (http://ieee.org), copyright © 2017 IEEE.";
	public static final String CM_RSNA_COPYRIGHT =
			"The LOINC/RSNA Radiology Playbook and the LOINC Part File contain content from RadLex® (http://rsna.org/RadLex.aspx), copyright © 2005-2017, The Radiological Society of North America, Inc., available at no cost under the license at http://www.rsna.org/uploadedFiles/RSNA/Content/Informatics/RadLex_License_Agreement_and_Terms_of_Use_V2_Final.pdf.";
	public static final String LOINC_SCT_PART_MAP_ID = "loinc-parts-to-snomed-ct";
	public static final String LOINC_SCT_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-snomed-ct";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_ID = "loinc-to-radlex";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_URI = "http://loinc.org/cm/loinc-to-radlex";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_NAME = "LOINC Terms to RadLex RPIDs";
	public static final String LOINC_PART_TO_RID_PART_MAP_ID = "loinc-parts-to-radlex";
	public static final String LOINC_PART_TO_RID_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-radlex";
	public static final String LOINC_PART_TO_RID_PART_MAP_NAME = "LOINC Parts to RadLex RIDs";
	public static final String LOINC_SCT_PART_MAP_NAME = "LOINC Part Map to SNOMED CT";
	public static final String LOINC_RXNORM_PART_MAP_ID = "loinc-parts-to-rxnorm";
	public static final String LOINC_RXNORM_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-rxnorm";
	public static final String LOINC_RXNORM_PART_MAP_NAME = "LOINC Part Map to RxNORM";
	public static final String LOINC_PUBCHEM_PART_MAP_ID = "loinc-parts-to-pubchem";
	public static final String LOINC_PUBCHEM_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-pubchem";
	public static final String LOINC_PUBCHEM_PART_MAP_NAME = "LOINC Part Map to PubChem";
	public static final String CM_SCT_COPYRIGHT =
			"The LOINC Part File, LOINC/SNOMED CT Expression Association and Map Sets File, RELMA database and associated search index files include SNOMED Clinical Terms (SNOMED CT®) which is used by permission of the International Health Terminology Standards Development Organisation (IHTSDO) under license. All rights are reserved. SNOMED CT® was originally created by The College of American Pathologists. “SNOMED” and “SNOMED CT” are registered trademarks of the IHTSDO. Use of SNOMED CT content is subject to the terms and conditions set forth in the SNOMED CT Affiliate License Agreement.  It is the responsibility of those implementing this product to ensure they are appropriately licensed and for more information on the license, including how to register as an Affiliate Licensee, please refer to http://www.snomed.org/snomed-ct/get-snomed-ct or info@snomed.org. Under the terms of the Affiliate License, use of SNOMED CT in countries that are not IHTSDO Members is subject to reporting and fee payment obligations. However, IHTSDO agrees to waive the requirements to report and pay fees for use of SNOMED CT content included in the LOINC Part Mapping and LOINC Term Associations for purposes that support or enable more effective use of LOINC. This material includes content from the US Edition to SNOMED CT, which is developed and maintained by the U.S. National Library of Medicine and is available to authorized UMLS Metathesaurus Licensees from the UTS Downloads site at https://uts.nlm.nih.gov.";
	public static final String RSNA_CODES_VS_ID = "loinc-rsna-radiology-playbook";
	public static final String RSNA_CODES_VS_URI = "http://loinc.org/vs/loinc-rsna-radiology-playbook";
	public static final String RSNA_CODES_VS_NAME = "LOINC/RSNA Radiology Playbook";
	public static final String RID_CS_URI = "http://www.radlex.org";
	/**
	 * About these being the same - Per Dan Vreeman:
	 * We had some discussion about this, and both
	 * RIDs (RadLex clinical terms) and RPIDs (Radlex Playbook Ids)
	 * belong to the same "code system" since they will never collide.
	 * The codesystem uri is "http://www.radlex.org". FYI, that's
	 * now listed on the FHIR page:
	 * https://www.hl7.org/fhir/terminologies-systems.html
	 * -ja
	 */
	public static final String RPID_CS_URI = RID_CS_URI;
	public static final String DOCUMENT_ONTOLOGY_CODES_VS_ID = "loinc-document-ontology";
	public static final String DOCUMENT_ONTOLOGY_CODES_VS_URI = "http://loinc.org/vs/loinc-document-ontology";
	public static final String DOCUMENT_ONTOLOGY_CODES_VS_NAME = "LOINC Document Ontology Codes";
	public static final String ASK_AT_ORDER_ENTRY_PROP_NAME = "AskAtOrderEntry";
	public static final String ASSOCIATED_OBSERVATIONS_PROP_NAME = "AssociatedObservations";

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

	@Bean
	public JobDefinition<ImportLoincJobParameters> importLoincJobDefinition() {
		return JobDefinition.newBuilder()
				.setInitialStatus(StatusEnum.BUILDING)
				.setJobDefinitionId(JOB_ID_IMPORT_TERM_LOINC)
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
						"import-part-file",
						"Import LOINC Part File",
						TerminologyFileSetJson.class,
						importLoincStep16aPartFile())
				.addIntermediateStep(
						"import-part-link-file",
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
				.addIntermediateStep(
					STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION,
						"Create work chunks for calculating concept closures",
						TerminologyFileSetJson.class,
						importLoincStep20ChunkConceptsForClosureGeneration())
				.addIntermediateStep(
						"generate-concept-closures",
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

	@Bean
	public ImportLoincStep16PartFile importLoincStep16aPartFile() {
		return new ImportLoincStep16PartFile();
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
		return new ImportLoincStep22Finalize(myDaoRegistry, myTermCodeSystemStorageSvc, myJobPersistence, myTxService);
	}
}
