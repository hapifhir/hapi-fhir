package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.WorkChunk;

import javax.annotation.Nonnull;

public class JobStepExecutorFactory {
	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;

	public JobStepExecutorFactory(@Nonnull IJobPersistence theJobPersistence, @Nonnull BatchJobSender theBatchJobSender) {
		myJobPersistence = theJobPersistence;
		myBatchJobSender = theBatchJobSender;
	}

	public JobStepExecutor newJobStepExecutor(JobDefinition theDefinition, JobInstance theInstance, WorkChunk theWorkChunk, JobWorkCursor theCursor) {
		return new JobStepExecutor(myJobPersistence, myBatchJobSender, theDefinition, theInstance, theWorkChunk, theCursor);
	}
}
