package ca.uhn.fhir.model.api;

public interface ISmartLifecyclePhase {
	// POST_CONSTRUCT is here as a marker for where post-construct fits into the smart lifecycle.  Beans with negative phases
	// will be started before @PostConstruct are called
	int POST_CONSTRUCT = 0;

	// We want to start scheduled tasks fairly late in the startup process
	int SCHEDULER_1000 = 1000;
}
