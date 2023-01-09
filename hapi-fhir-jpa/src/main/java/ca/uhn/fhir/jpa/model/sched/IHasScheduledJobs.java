package ca.uhn.fhir.jpa.model.sched;

/**
 * This interface is implemented by classes that have scheduled jobs
 */
public interface IHasScheduledJobs {
	void scheduleJobs(ISchedulerService theSchedulerService);
}
