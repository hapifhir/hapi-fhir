package ca.uhn.fhir.jpa.model.sched;

import org.quartz.SchedulerException;

import javax.annotation.PreDestroy;

public interface ISchedulerService {
	@PreDestroy
	void stop() throws SchedulerException;

	/**
	 * @param theIntervalMillis How many milliseconds between passes should this job run
	 * @param theClusteredTask  If <code>true</code>, only one instance of this task will fire across the whole cluster (when running in a clustered environment). If <code>false</code>, or if not running in a clustered environment, this task will execute locally (and should execute on all nodes of the cluster)
	 * @param theJobDefinition  The Job to fire
	 */
	void scheduleFixedDelay(long theIntervalMillis, boolean theClusteredTask, ScheduledJobDefinition theJobDefinition);
}
