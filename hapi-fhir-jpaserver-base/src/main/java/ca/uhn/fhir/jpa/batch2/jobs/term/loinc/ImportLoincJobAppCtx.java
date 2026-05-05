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

import ca.uhn.fhir.batch2.api.IJobStepWorker;
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

}
