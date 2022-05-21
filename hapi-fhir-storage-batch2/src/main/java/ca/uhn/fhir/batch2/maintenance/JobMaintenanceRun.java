package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class JobMaintenanceRun {
	private static final Logger ourLog = LoggerFactory.getLogger(JobMaintenanceRun.class);

	public static final int INSTANCES_PER_PASS = 100;

	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final BatchJobSender myBatchJobSender;

	private final Set<String> myProcessedInstanceIds = new HashSet<>();
	private final JobChunkProgressAccumulator myProgressAccumulator = new JobChunkProgressAccumulator();

	JobMaintenanceRun(IJobPersistence theJobPersistence, JobDefinitionRegistry theJobDefinitionRegistry, BatchJobSender theBatchJobSender) {
		myJobPersistence = theJobPersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myBatchJobSender = theBatchJobSender;
	}

	// NB: If you add any new logic, update the class javadoc
	void updateInstances() {
		for (int page = 0; ; page++) {
			List<JobInstance> instances = myJobPersistence.fetchInstances(INSTANCES_PER_PASS, page);

			for (JobInstance instance : instances) {
				if (myProcessedInstanceIds.add(instance.getInstanceId())) {
					JobInstanceProcessor jobInstanceProcessor = new JobInstanceProcessor(myJobPersistence, myJobDefinitionRegistry, myBatchJobSender, instance, myProgressAccumulator);
					jobInstanceProcessor.process();
				}
			}

			if (instances.size() < INSTANCES_PER_PASS) {
				break;
			}
		}
	}
}
