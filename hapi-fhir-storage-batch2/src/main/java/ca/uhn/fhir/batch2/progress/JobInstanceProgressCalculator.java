package ca.uhn.fhir.batch2.progress;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.maintenance.JobMaintenanceServiceImpl;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;

import java.util.List;

import static ca.uhn.fhir.batch2.maintenance.JobInstanceProcessor.updateInstanceStatus;

public class JobInstanceProgressCalculator {
	private final IJobPersistence myJobPersistence;
	private final JobInstance myInstance;
	private final JobChunkProgressAccumulator myProgressAccumulator;

	public JobInstanceProgressCalculator(IJobPersistence theJobPersistence, JobInstance theInstance, JobChunkProgressAccumulator theProgressAccumulator) {
		myJobPersistence = theJobPersistence;
		myInstance = theInstance;
		myProgressAccumulator = theProgressAccumulator;
	}

	public void calculateAndStoreInstanceProgress() {
		InstanceProgress instanceProgress = new InstanceProgress();

		List<WorkChunk> chunks = myJobPersistence.fetchAllWorkChunks(myInstance.getInstanceId(), false);
		for (WorkChunk chunk : chunks) {
			myProgressAccumulator.addChunk(chunk);
			instanceProgress.addChunk(chunk);
		}

		instanceProgress.updateInstance(myInstance);

		if (instanceProgress.failed()) {
			updateInstanceStatus(myInstance, StatusEnum.FAILED);
			myJobPersistence.updateInstance(myInstance);
			return;
		}

		if (instanceProgress.changed()) {
			myJobPersistence.updateInstance(myInstance);
		}
	}
}
