package ca.uhn.fhir.jpa.sched;

import org.springframework.beans.factory.annotation.Autowired;

public class SchedulerFactory {
	public static final String THREAD_NAME_PREFIX = "hapi-fhir-jpa-scheduler";

	@Autowired
	private AutowiringSpringBeanJobFactory mySpringBeanJobFactory;

	public IHapiScheduler newClusteredHapiScheduler() {
		return new ClusteredHapiScheduler(THREAD_NAME_PREFIX, mySpringBeanJobFactory);
	}

	public IHapiScheduler newLocalHapiScheduler() {
		return new LocalHapiScheduler(THREAD_NAME_PREFIX, mySpringBeanJobFactory);
	}
}
