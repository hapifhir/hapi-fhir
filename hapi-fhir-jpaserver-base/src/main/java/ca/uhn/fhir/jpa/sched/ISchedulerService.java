package ca.uhn.fhir.jpa.sched;

import org.quartz.SchedulerException;

import javax.annotation.PreDestroy;

public interface ISchedulerService {
	@PreDestroy
	void stop() throws SchedulerException;

	void scheduleFixedDelay(long theIntervalMillis, boolean theClusteredTask, ScheduledJobDefinition theJobDefinition);
}
