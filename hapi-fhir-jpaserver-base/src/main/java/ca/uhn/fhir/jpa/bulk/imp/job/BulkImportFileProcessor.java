package ca.uhn.fhir.jpa.bulk.imp.job;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class BulkImportFileProcessor implements ItemProcessor<String, List<IBaseResource>> {

	private static final Logger ourLog = LoggerFactory.getLogger(BulkImportFileProcessor.class);
	@Value("#{stepExecutionContext['" + BulkExportJobConfig.JOB_UUID_PARAMETER + "']}")
	private String myJobUuid;
	@Value("#{stepExecutionContext['" + BulkImportPartitioner.FILE_INDEX + "']}")
	private int myFileIndex;

	@Autowired
	private FhirContext myFhirContext;

	@Override
	public List<IBaseResource> process(@Nonnull String theLine) throws Exception {
		ourLog.info("Parsing file index {} for job: {}", myFileIndex, myJobUuid);

		IBaseResource resource = myFhirContext.newJsonParser().parseResource(theLine);
		return Collections.singletonList(resource);

	}
}
