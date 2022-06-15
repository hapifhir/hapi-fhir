package ca.uhn.fhir.batch2.progress;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;

import java.util.Iterator;
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

		Iterator<WorkChunk> workChunkIterator = myJobPersistence.fetchAllWorkChunksIterator(myInstance.getInstanceId(), false);

		while (workChunkIterator.hasNext()) {
			WorkChunk next = workChunkIterator.next();
			myProgressAccumulator.addChunk(next);
			instanceProgress.addChunk(next);
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
