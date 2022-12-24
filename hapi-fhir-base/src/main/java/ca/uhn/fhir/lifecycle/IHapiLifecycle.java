package ca.uhn.fhir.lifecycle;

public interface IHapiLifecycle {
	default void start() {}

	default void scheduleJobs() {}
	default void stop() {}
}
