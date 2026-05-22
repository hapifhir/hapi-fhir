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

import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportLoincJobAppCtx {

	public static final String IMPORT_TERM_LOINC = "IMPORT_TERM_LOINC";

	private final DaoRegistry myDaoRegistry;
	private final ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;

	public ImportLoincJobAppCtx(DaoRegistry myDaoRegistry, ITermCodeSystemStorageSvc theTermCodeSystemStorageSvc) {
		this.myDaoRegistry = myDaoRegistry;
		this.myTermCodeSystemStorageSvc = theTermCodeSystemStorageSvc;
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
						ImportLoincFileSetJson.class,
						importLoincStep1ExpandDistributionIntoFiles())
				.addIntermediateStep(
						"import-concepts",
						"Import LOINC concepts",
						ImportLoincFileSetJson.class,
						importLoincStep2Concepts())
				.addIntermediateStep(
						"import-hierarchy-concepts",
						"Import LOINC hierarchy Concepts",
						ImportLoincFileSetJson.class,
					importLoincStep4HandleHierarchyConcepts())
				.addIntermediateStep(
						"import-hierarchy",
						"Import LOINC hierarchy",
						ImportLoincFileSetJson.class,
						importLoincStep3HandleHierarchy())
				.addIntermediateStep(
						"import-answer-lists",
						"Import LOINC answer lists",
						ImportLoincFileSetJson.class,
						importLoincStep5AnswerLists())
				.addIntermediateStep(
						"import-answer-list-links",
						"Import LOINC answer list links",
						ImportLoincFileSetJson.class,
						importLoincStep6AnswerListLinks())
				.addIntermediateStep(
						"import-rsna-playbook",
						"Import LOINC RSNA playbook",
						ImportLoincFileSetJson.class,
						importLoincStep7RsnaPlaybook())
				.addIntermediateStep(
						"import-part-related-code-mapping",
						"Import LOINC Part Related Code Mappings",
						ImportLoincFileSetJson.class,
						importLoincStep8PartRelatedCodeMapping())
				.addIntermediateStep(
						"import-document-ontology",
						"Import LOINC Document Ontology",
						ImportLoincFileSetJson.class,
						importLoincStep9HandleDocumentOntology())
				.addIntermediateStep(
						"import-univeral-lab-orderset",
						"Import LOINC Lab Order Set",
						ImportLoincFileSetJson.class,
						importLoincStep10HandleUniversalLabOrderSet())
				.addIntermediateStep(
						"import-ieee-medical-device-code",
						"Import LOINC IEEE Medical Device Codes",
						ImportLoincFileSetJson.class,
						importLoincStep11HandleIeeeMedicalDeviceCode())
				.addIntermediateStep(
						"import-imaging-document-code",
						"Import LOINC Imaging Document Codes",
						ImportLoincFileSetJson.class,
						importLoincStep12ImagingDocumentCode())
				.addIntermediateStep(
						"import-group-file",
						"Import LOINC Group File",
						ImportLoincFileSetJson.class,
						importLoincStep13GroupFile())
				.addIntermediateStep(
						"import-group-terms-file",
						"Import LOINC Group Terms File",
						ImportLoincFileSetJson.class,
						importLoincStep14GroupTermsFile())
				.addIntermediateStep(
						"import-parent-group-file",
						"Import LOINC Parent Group File",
						ImportLoincFileSetJson.class,
						importLoincStep15ParentGroupFile())
				.addIntermediateStep(
						"import-part-link",
						"Import LOINC Part Link File",
						ImportLoincFileSetJson.class,
						importLoincStep16PartLink())
				.addIntermediateStep(
						"import-consumer-name",
						"Import LOINC Consumer Names",
						ImportLoincFileSetJson.class,
						importLoincStep17ConsumerName())
				.addIntermediateStep(
						"import-coding-properties",
						"Import LOINC Coding Properties",
						ImportLoincFileSetJson.class,
						importLoincStep18CodingProperties())
				.addIntermediateStep(
						"import-linguistic-variant",
						"Import LOINC Linguistic Variants",
						ImportLoincFileSetJson.class,
						importLoincStep19LinguisticVariant())
				.addFinalReducerStep(
						"finalize-import",
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

	@Bean
	public IReductionStepWorker<ImportLoincJobParameters, ImportLoincFileSetJson, ImportTerminologyResultJson> importLoincStep21Finalize() {
		return new ImportLoincStep21Finalize(myDaoRegistry, myTermCodeSystemStorageSvc);
	}

}
