package ca.uhn.fhir.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public abstract class BaseHapiLifecycle {
	@PostConstruct
	public abstract void startup();

	@PreDestroy
	public abstract void shutdown();

}
