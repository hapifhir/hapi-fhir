package ca.uhn.fhir.jpa.bulk.imp.job;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Nonnull;

public class BulkImportFileProcessor implements ItemProcessor<String, IBaseResource> {

	@Value("#{stepExecutionContext['" + BulkExportJobConfig.JOB_UUID_PARAMETER + "']}")
	private String myJobUuid;
	@Value("#{stepExecutionContext['" + BulkImportPartitioner.FILE_INDEX + "']}")
	private int myFileIndex;

	@Autowired
	private FhirContext myFhirContext;

	@Override
	public IBaseResource process(@Nonnull String theLine) throws Exception {
		Logs.getBatchTroubleshootingLog().debug("Parsing file index {} for job: {}", myFileIndex, myJobUuid);

		return myFhirContext.newJsonParser().parseResource(theLine);
	}

}
