package ca.uhn.fhir.jpa.model.sched;

import org.quartz.JobKey;
import org.quartz.SchedulerException;

import java.util.Set;

public interface IHapiScheduler {
	void init() throws SchedulerException;

	void start();

	void shutdown();

	boolean isStarted();

	void clear() throws SchedulerException;

	void logStatusForUnitTest();

	void scheduleJob(long theIntervalMillis, ScheduledJobDefinition theJobDefinition);

	Set<JobKey> getJobKeysForUnitTest() throws SchedulerException;
}
