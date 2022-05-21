package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.model.api.IModelJson;

import javax.annotation.Nonnull;

public class JobStepExecutorFactory {
	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;

	public JobStepExecutorFactory(@Nonnull IJobPersistence theJobPersistence, @Nonnull BatchJobSender theBatchJobSender) {
		myJobPersistence = theJobPersistence;
		myBatchJobSender = theBatchJobSender;
	}

	public <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> JobStepExecutor<PT,IT,OT> newJobStepExecutor(@Nonnull JobInstance theInstance, @Nonnull WorkChunk theWorkChunk, @Nonnull JobWorkCursor<PT, IT, OT> theCursor) {
		return new JobStepExecutor<>(myJobPersistence, myBatchJobSender, theInstance, theWorkChunk, theCursor);
	}
}
