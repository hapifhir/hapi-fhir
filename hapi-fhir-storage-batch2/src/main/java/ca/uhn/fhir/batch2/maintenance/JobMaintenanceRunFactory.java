package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;

public class JobMaintenanceRunFactory {
	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final BatchJobSender myBatchJobSender;

	public JobMaintenanceRunFactory(IJobPersistence theJobPersistence, JobDefinitionRegistry theJobDefinitionRegistry, BatchJobSender theBatchJobSender) {
		myJobPersistence = theJobPersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myBatchJobSender = theBatchJobSender;
	}

	public JobMaintenanceRun newInstance() {
		return new JobMaintenanceRun(myJobPersistence, myJobDefinitionRegistry, myBatchJobSender);
	}
}
