package ca.uhn.fhir.jpa.model.sched;

import org.quartz.SchedulerException;

public interface IHapiScheduler {
	void init() throws SchedulerException;

	void start();

	void shutdown();

	boolean isStarted();

	void clear() throws SchedulerException;

	void logStatusForUnitTest();

	void scheduleJob(long theIntervalMillis, ScheduledJobDefinition theJobDefinition);
}
