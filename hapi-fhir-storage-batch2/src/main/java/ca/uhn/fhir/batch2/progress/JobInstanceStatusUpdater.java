package ca.uhn.fhir.batch2.progress;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
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

	private <PT extends IModelJson> void handleStatusChange(JobInstance theJobInstance) {
		JobDefinition<PT> definition = (JobDefinition<PT>) theJobInstance.getJobDefinition();
		switch (theJobInstance.getStatus()) {
			case COMPLETED:
				invokeCompletionHandler(theJobInstance, definition, definition.getCompletionHandler());
				break;
			case FAILED:
			case ERRORED:
			case CANCELLED:
				invokeCompletionHandler(theJobInstance, definition, definition.getErrorHandler());
				break;
			case QUEUED:
			case IN_PROGRESS:
			default:
				// do nothing
		}
	}


	private <PT extends IModelJson> void invokeCompletionHandler(JobInstance theJobInstance, JobDefinition<PT> theJobDefinition, IJobCompletionHandler<PT> theJobCompletionHandler) {
		if (theJobCompletionHandler == null) {
			return;
		}
		PT jobParameters = theJobInstance.getParameters(theJobDefinition.getParametersType());
		JobCompletionDetails<PT> completionDetails = new JobCompletionDetails<>(jobParameters, theJobInstance);
		theJobCompletionHandler.jobComplete(completionDetails);
	}
}
