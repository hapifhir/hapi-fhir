package ca.uhn.fhir.batch2.progress;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.IJobErrorHandler;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.api.JobErrorDetails;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.model.api.IModelJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobInstanceStatusUpdater {
	private static final Logger ourLog = LoggerFactory.getLogger(JobInstanceStatusUpdater.class);
	private final IJobPersistence myJobPersistence;

	public JobInstanceStatusUpdater(IJobPersistence theJobPersistence) {
		myJobPersistence = theJobPersistence;
	}

	public boolean updateInstanceStatus(JobInstance theJobInstance, StatusEnum theNewStatus) {
		theJobInstance.setStatus(theNewStatus);
		return updateInstance(theJobInstance);
	}

	public boolean updateInstance(JobInstance theJobInstance) {
		boolean statusChanged = myJobPersistence.updateInstance(theJobInstance);

		// This code can be called by both the maintenance service and the fast track work step executor.
		// We only want to call the completion handler if the status was changed to COMPLETED in this thread.  We use the
		// record changed count from of a sql update change status to rely on the database to tell us which thread
		// the status change happened in.
		if (statusChanged) {
				ourLog.info("Marking job instance {} of type {} as {}", theJobInstance.getInstanceId(), theJobInstance.getJobDefinitionId(), theJobInstance.getStatus());
				handleStatusChange(theJobInstance);
		}
		return statusChanged;
	}

	private void handleStatusChange(JobInstance theJobInstance) {
		switch (theJobInstance.getStatus()) {
			case COMPLETED:
				invokeCompletionHandler(theJobInstance);
				break;
			case FAILED:
			case ERRORED:
			case CANCELLED:
				invokeErrorHandler(theJobInstance);
				break;
			case QUEUED:
			case IN_PROGRESS:
			default:
				// do nothing
		}
	}


	private  <PT extends IModelJson> void invokeCompletionHandler(JobInstance theJobInstance) {
		JobDefinition<PT> definition = (JobDefinition<PT>) theJobInstance.getJobDefinition();
		IJobCompletionHandler<PT> completionHandler = definition.getCompletionHandler();
		if (completionHandler != null) {
			String instanceId = theJobInstance.getInstanceId();
			PT jobParameters = theJobInstance.getParameters(definition.getParametersType());
			JobCompletionDetails<PT> completionDetails = new JobCompletionDetails<>(jobParameters, instanceId);
			completionHandler.jobComplete(completionDetails);
		}
	}

	private  <PT extends IModelJson> void invokeErrorHandler(JobInstance theJobInstance) {
		JobDefinition<PT> definition = (JobDefinition<PT>) theJobInstance.getJobDefinition();
		IJobErrorHandler<PT> errorHandler = definition.getErrorHandler();
		if (errorHandler != null) {
			String instanceId = theJobInstance.getInstanceId();
			PT jobParameters = theJobInstance.getParameters(definition.getParametersType());
			String errorMessage = theJobInstance.getErrorMessage();
			int errorCount = theJobInstance.getErrorCount();
			StatusEnum status = theJobInstance.getStatus();
			JobErrorDetails<PT> errorDetails = new JobErrorDetails<>(jobParameters, instanceId, errorMessage, errorCount, status);
			errorHandler.jobFailed(errorDetails);
		}
	}
}
