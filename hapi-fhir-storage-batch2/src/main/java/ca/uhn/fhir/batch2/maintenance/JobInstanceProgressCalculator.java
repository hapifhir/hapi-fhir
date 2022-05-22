package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ca.uhn.fhir.batch2.maintenance.JobInstanceProcessor.updateInstanceStatus;

class JobInstanceProgressCalculator {
	private static final Logger ourLog = LoggerFactory.getLogger(JobInstanceProgressCalculator.class);

	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final JobInstance myInstance;
	private final JobChunkProgressAccumulator myProgressAccumulator;

	JobInstanceProgressCalculator(IJobPersistence theJobPersistence, JobDefinitionRegistry theJobDefinitionRegistry, JobInstance theInstance, JobChunkProgressAccumulator theProgressAccumulator) {
		myJobPersistence = theJobPersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myInstance = theInstance;
		myProgressAccumulator = theProgressAccumulator;
	}

	void calculateInstanceProgress() {
		InstanceProgress instanceProgress = new InstanceProgress();

		for (int page = 0; ; page++) {
			List<WorkChunk> chunks = myJobPersistence.fetchWorkChunksWithoutData(myInstance.getInstanceId(), JobMaintenanceServiceImpl.INSTANCES_PER_PASS, page);

			for (WorkChunk chunk : chunks) {
				myProgressAccumulator.addChunk(chunk);
				instanceProgress.addChunk(chunk);
			}

			if (chunks.size() < JobMaintenanceServiceImpl.INSTANCES_PER_PASS) {
				break;
			}
		}

		instanceProgress.updateInstance(myJobDefinitionRegistry, myInstance);

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
