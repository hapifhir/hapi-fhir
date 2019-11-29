package ca.uhn.fhir.rest.server.sched;

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
