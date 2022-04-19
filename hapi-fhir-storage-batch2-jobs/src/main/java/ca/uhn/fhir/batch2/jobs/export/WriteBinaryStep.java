package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportExpandedResources;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.BinaryUtil;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class WriteBinaryStep implements IJobStepWorker<BulkExportJobParameters, BulkExportExpandedResources, VoidModel> {
	private static final Logger ourLog = getLogger(WriteBinaryStep.class);

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IBulkExportProcessor myBulkExportProcessor;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<BulkExportJobParameters, BulkExportExpandedResources> theStepExecutionDetails,
								 @Nonnull IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {

		BulkExportExpandedResources expandedResources = theStepExecutionDetails.getData();
		@SuppressWarnings("unchecked")
		IFhirResourceDao<IBaseBinary> binaryDao = myDaoRegistry.getResourceDao("Binary");

		IBaseBinary binary = BinaryUtil.newBinary(myFhirContext);
		binary.setContentType(Constants.CT_FHIR_NDJSON);

		List<String> errors = new ArrayList<>();
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			try (OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream, Constants.CHARSET_UTF8)) {
				for (String stringified : expandedResources.getStringifiedResources()) {
					streamWriter.append(stringified);
					streamWriter.append("\n");
				}
				streamWriter.flush();
				outputStream.flush();
			}
			binary.setContent(outputStream.toByteArray());
		}
		catch (IOException ex) {
			ourLog.error("Failure to process resource of type {} : {}",
				expandedResources.getResourceType(),
				ex.getMessage());
			errors.add(ex.getMessage());

			// processing will continue
			// but we'll set the job to error so user knows why it's incomplete
			myBulkExportProcessor.setJobStatus(expandedResources.getJobId(),
				BulkExportJobStatusEnum.ERROR);
		}

		DaoMethodOutcome outcome = binaryDao.create(binary,
			new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.defaultPartition()));
		IIdType id = outcome.getId();

		String jobId = expandedResources.getJobId();

		// save the binary to the file collection
		myBulkExportProcessor.addFileToCollection(jobId,
			expandedResources.getResourceType(),
			id);

		int errorCount = errors.size();
		int totalProcessed = expandedResources.getStringifiedResources().size();
		ourLog.trace("Binary writing complete for {} resources of type {}. {} errors.",
			totalProcessed - errorCount,
			expandedResources.getResourceType(),
			errorCount);

		return RunOutcome.SUCCESS;
	}
}
