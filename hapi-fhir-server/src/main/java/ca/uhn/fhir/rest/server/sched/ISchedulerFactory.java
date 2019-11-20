package ca.uhn.fhir.rest.server.sched;

public interface ISchedulerFactory {
	IHapiScheduler newClusteredHapiScheduler();

	IHapiScheduler newLocalHapiScheduler();
}
