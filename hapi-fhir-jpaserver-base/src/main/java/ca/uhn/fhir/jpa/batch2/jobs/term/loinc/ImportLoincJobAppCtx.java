package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.jobs.imprt.BulkImportJobParameters;
import ca.uhn.fhir.batch2.jobs.imprt.NdJsonFileJson;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.StatusEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportLoincJobAppCtx {

	/**
	 * Pre-HAPI FHIR 8.2.0 definition
	 */
//	@Bean
//	public JobDefinition<BulkImportJobParameters> bulkImport2JobDefinition() {
//		return JobDefinition.newBuilder()
//			.setInitialStatus(StatusEnum.QUEUED)
//			.setJobDefinitionId("IMPORT_TERM_LOINC")
//			.setJobDescription("Import Terminology - LOINC")
//			.setJobDefinitionVersion(1)
//			.setParametersType(BulkImportJobParameters.class)
//			.addFirstStep("fetch-files", "Fetch files for import", NdJsonFileJson.class, bulkImport2FetchFiles())
//			.addLastStep("process-files", "Process files", bulkImport2ConsumeFilesV1())
//			.build();
//	}

}
