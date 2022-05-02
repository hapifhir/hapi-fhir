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
		String jobId = expandedResources.getJobId();

		ourLog.info(jobId + " final step started");

		@SuppressWarnings("unchecked")
		IFhirResourceDao<IBaseBinary> binaryDao = myDaoRegistry.getResourceDao("Binary");

		IBaseBinary binary = BinaryUtil.newBinary(myFhirContext);
		// should be dependent on the
		// output format in parameters
		// but for now, only NDJSON is supported
		binary.setContentType(Constants.CT_FHIR_NDJSON);

		int processedRecordsCount = 0;
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			try (OutputStreamWriter streamWriter = getStreamWriter(outputStream)) {
				for (String stringified : expandedResources.getStringifiedResources()) {
					streamWriter.append(stringified);
					streamWriter.append("\n");
					processedRecordsCount++;
				}
				streamWriter.flush();
				outputStream.flush();
			}
			binary.setContent(outputStream.toByteArray());
		} catch (IOException ex) {
			ourLog.error("Failure to process resource of type {} : {}",
				expandedResources.getResourceType(),
				ex.getMessage());

			// processing will continue
			// but we'll set the job to error so user knows why it's incomplete
			myBulkExportProcessor.setJobStatus(expandedResources.getJobId(),
				BulkExportJobStatusEnum.ERROR);

			// failure - return -1?
			return new RunOutcome(-1);
		}

		DaoMethodOutcome outcome = binaryDao.create(binary,
			new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.defaultPartition()));
		IIdType id = outcome.getId();

		// save the binary to the file collection
		myBulkExportProcessor.addFileToCollection(jobId,
			expandedResources.getResourceType(),
			id);

		ourLog.trace("Binary writing complete for {} resources of type {}.",
			processedRecordsCount,
			expandedResources.getResourceType());

		return RunOutcome.SUCCESS;
	}

	/**
	 * Returns an output stream writer
	 * (exposed for testing)
	 */
	protected OutputStreamWriter getStreamWriter(ByteArrayOutputStream theOutputStream) {
		return new OutputStreamWriter(theOutputStream, Constants.CHARSET_UTF8);
	}
}
