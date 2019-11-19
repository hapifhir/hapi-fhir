package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import org.quartz.SchedulerException;

public interface IHapiScheduler {
	void init() throws SchedulerException;

	void start();

	void shutdown();

	boolean isStarted();

	void clear() throws SchedulerException;

	void logStatusForUnitTest();

	void scheduleFixedDelay(long theIntervalMillis, ScheduledJobDefinition theJobDefinition);
}
