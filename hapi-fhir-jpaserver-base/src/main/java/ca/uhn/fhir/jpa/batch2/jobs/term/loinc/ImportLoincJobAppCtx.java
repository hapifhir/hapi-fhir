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

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.StatusEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportLoincJobAppCtx {

	public static final String IMPORT_TERM_LOINC = "IMPORT_TERM_LOINC";
	public static final String DISTRIBUTION_FILE_ATTACHMENT_FILENAME = "loinc.zip";

	@Bean
	public JobDefinition<LoincJobImportParameters> importLoincJobDefinition() {
		return JobDefinition.newBuilder()
			.setInitialStatus(StatusEnum.BUILDING)
			.setJobDefinitionId(IMPORT_TERM_LOINC)
			.setJobDescription("Import Terminology - LOINC")
			.setJobDefinitionVersion(1)
			.gatedExecution()
			.setParametersType(LoincJobImportParameters.class)
			.addFirstStep("expand-zip", "Expand LOINC distribution", ImportLoincFileSetJson.class, importLoincStep1ExpandDistributionIntoFiles())
			.addIntermediateStep("import-concepts", "Import LOINC concepts", ImportLoincFileSetJson.class, importLoincStep2Concepts())
			.addIntermediateStep("import-hierarchy", "Import LOINC hierarchy", ImportLoincFileSetJson.class, importLoincStep3HandleHierarchy())
			.addIntermediateStep("import-answer-lists", "Import LOINC answer lists", ImportLoincFileSetJson.class, importLoincStep4AnswerLists())
			.addIntermediateStep("import-answer-list-links", "Import LOINC answer list links", ImportLoincFileSetJson.class, importLoincStep5AnswerListLinks())
			.addIntermediateStep("import-rsna-playbook", "Import LOINC RSNA playbook", ImportLoincFileSetJson.class, importLoincStep6RsnaPlaybook())
			.addIntermediateStep("import-part-related-code-mapping", "Import LOINC Part Related Code Mappings", ImportLoincFileSetJson.class, importLoincStep7PartRelatedCodeMapping())
			.addIntermediateStep("import-document-ontology", "Import LOINC Document Ontology", ImportLoincFileSetJson.class, importLoincStep8HandleDocumentOntology())
			.addIntermediateStep("import-top-2000-codes-us", "Import LOINC Top 2000 Codes - US", ImportLoincFileSetJson.class, importLoincStep9HandleTop2000CodesUs())
			.addIntermediateStep("import-top-2000-codes-si", "Import LOINC Top 2000 Codes - SI", ImportLoincFileSetJson.class, importLoincStep10HandleTop2000CodesSi())
			.addIntermediateStep("import-univeral-lab-orderset", "Import LOINC Lab Order Set", ImportLoincFileSetJson.class, importLoincStep11HandleUniversalLabOrderSet())
			.addIntermediateStep("import-ieee-medical-device-code", "Import LOINC IEEE Medical Device Codes", ImportLoincFileSetJson.class, importLoincStep12HandleIeeeMedicalDeviceCode())
			.addIntermediateStep("import-imaging-document-code", "Import LOINC Imaging Document Codes", ImportLoincFileSetJson.class, importLoincStep13ImagingDocumentCode())
			.addIntermediateStep("import-group-file", "Import LOINC Group File", ImportLoincFileSetJson.class, importLoincStep14GroupFile())
			.addIntermediateStep("import-group-terms-file", "Import LOINC Group Terms File", ImportLoincFileSetJson.class, importLoincStep15GroupTermsFile())
			.addIntermediateStep("import-parent-group-file", "Import LOINC Parent Group File", ImportLoincFileSetJson.class, importLoincStep16ParentGroupFile())
//				.addLastStep("process-files", "Process files", bulkImport2ConsumeFilesV1())
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
	 * Step 4: Import LOINC answer lists
	 */
	@Bean
	public ImportLoincStep4HandleAnswerLists importLoincStep4AnswerLists() {
		return new ImportLoincStep4HandleAnswerLists();
	}

	/**
	 * Step 5: Import LOINC answer list links
	 */
	@Bean
	public ImportLoincStep5HandleAnswerListLinks importLoincStep5AnswerListLinks() {
		return new ImportLoincStep5HandleAnswerListLinks();
	}


	/**
	 * Step 6: Import RSNA Playbook
	 */
	@Bean
	public ImportLoincStep6HandleRsnaPlaybook importLoincStep6RsnaPlaybook() {
		return new ImportLoincStep6HandleRsnaPlaybook();
	}

	/**
	 * Step 7: Import Part-Related Code Mappings
	 */
	@Bean
	public ImportLoincStep7HandlePartRelatedCodeMapping importLoincStep7PartRelatedCodeMapping() {
		return new ImportLoincStep7HandlePartRelatedCodeMapping();
	}

	/**
	 * Step 8: Import Document Ontology
	 */
	@Bean
	public ImportLoincStep8HandleDocumentOntology importLoincStep8HandleDocumentOntology() {
		return new ImportLoincStep8HandleDocumentOntology();
	}

	/**
	 * Step 9: Top 2000 Codes (US)
	 */
	@Bean
	public ImportLoincStep9HandleTop2000CodesUs importLoincStep9HandleTop2000CodesUs() {
		return new ImportLoincStep9HandleTop2000CodesUs();
	}

	/**
	 * Step 10: Top 2000 Codes (SI)
	 */
	@Bean
	public ImportLoincStep10HandleTop2000CodesSi importLoincStep10HandleTop2000CodesSi() {
		return new ImportLoincStep10HandleTop2000CodesSi();
	}

	/**
	 * Step 11: Universal Lab Order Set
	 */
	@Bean
	public ImportLoincStep11HandleUniversalLabOrderSet importLoincStep11HandleUniversalLabOrderSet() {
		return new ImportLoincStep11HandleUniversalLabOrderSet();
	}

	/**
	 * Step 12: IEEE Medical Device Code
	 */
	@Bean
	public ImportLoincStep12HandleIeeeMedicalDeviceCode importLoincStep12HandleIeeeMedicalDeviceCode() {
		return new ImportLoincStep12HandleIeeeMedicalDeviceCode();
	}

	/**
	 * Step 13: Imaging Document Code
	 */
	@Bean
	public ImportLoincStep13ImagingDocumentCode importLoincStep13ImagingDocumentCode() {
		return new ImportLoincStep13ImagingDocumentCode();
	}

	/**
	 * Step 14: Group File
	 */
	@Bean
	public ImportLoincStep14GroupFile importLoincStep14GroupFile() {
		return new ImportLoincStep14GroupFile();
	}

	/**
	 * Step 15: Group Terms File
	 */
	@Bean
	public ImportLoincStep15GroupTermsFile importLoincStep15GroupTermsFile() {
		return new ImportLoincStep15GroupTermsFile();
	}

	/**
	 * Step 16: Parent Group File
	 */
	@Bean
	public ImportLoincStep16ParentGroupFile importLoincStep16ParentGroupFile() {
		return new ImportLoincStep16ParentGroupFile();
	}

}
