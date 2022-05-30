package ca.uhn.fhir.batch2.jobs.services;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.api.model.BulkExportParameters;
import ca.uhn.fhir.jpa.api.model.StartNewJobParameters;
import ca.uhn.fhir.jpa.api.svc.IBatch2JobRunner;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import static org.slf4j.LoggerFactory.getLogger;

public class Batch2JobRunnerImpl implements IBatch2JobRunner {
	private static final Logger ourLog = getLogger(IBatch2JobRunner.class);

	@Autowired
	private IJobCoordinator myJobCoordinator;

	@Override
	public String startNewJob(StartNewJobParameters theParameters) {
		switch (theParameters.getJobDefinitionId()) {
			case Batch2JobDefinitionConstants.BULK_EXPORT:
				if (theParameters instanceof BulkExportParameters) {
					return startBatch2BulkExportJob((BulkExportParameters) theParameters);
				}
				else {
					ourLog.error("Invalid parameters for " + Batch2JobDefinitionConstants.BULK_EXPORT);
				}
				break;
			default:
				// Dear future devs - add your case above
				ourLog.error("Invalid JobDefinitionId " + theParameters.getJobDefinitionId());
				break;
		}
		return null;
	}

	@Override
	public void startExistingJob(StartExistingJobParameters theParameters) {
		switch (theParameters.getJobId()) {
			case Batch2JobDefinitionConstants.BULK_EXPORT:
				myJobCoordinator.startExistingInstance(theParameters.getJobId());
				break;
			default:
				ourLog.info("Unknown job id {}", theParameters.getJobId());
		}
	}

	private String startBatch2BulkExportJob(BulkExportParameters theParameters) {
		JobInstanceStartRequest request = createStartRequest(theParameters);
		request.setParameters(BulkExportJobParameters.createFromExportJobParameters(theParameters));

		if (theParameters.isStartJobImmediately()) {
			return myJobCoordinator.startInstance(request);
		} else {
			return myJobCoordinator.storeInstance(request);
		}
	}

	private JobInstanceStartRequest createStartRequest(StartNewJobParameters theParameters) {
		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(theParameters.getJobDefinitionId());
		return request;
	}
}
