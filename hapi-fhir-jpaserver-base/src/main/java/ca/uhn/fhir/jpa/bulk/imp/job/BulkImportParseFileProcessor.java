package ca.uhn.fhir.jpa.bulk.imp.job;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
import ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobFileJson;
import com.google.common.io.LineReader;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class BulkImportParseFileProcessor implements ItemProcessor<BulkImportJobFileJson, List<IBaseResource>> {

	@Value("#{stepExecutionContext['" + BulkExportJobConfig.JOB_UUID_PARAMETER + "']}")
	private String myJobUuid;
	@Value("#{stepExecutionContext['" + BulkImportPartitioner.FILE_INDEX + "']}")
	private int myFileIndex;

	@Autowired
	private FhirContext myFhirContext;

	private static final Logger ourLog = LoggerFactory.getLogger(BulkImportParseFileProcessor.class);

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public List<IBaseResource> process(BulkImportJobFileJson theInput) throws Exception {
		ourLog.info("Parsing file index {} for job: {}", myFileIndex, myJobUuid);

		String contents = theInput.getContents();
		LineReader reader = new LineReader(new StringReader(contents));

		List<IBaseResource> resources = new ArrayList<>();
		String nextLine;
		while ((nextLine = reader.readLine()) != null) {
			IBaseResource nextResource = myFhirContext.newJsonParser().parseResource(nextLine);
			resources.add(nextResource);
		}

		return resources;
	}
}
