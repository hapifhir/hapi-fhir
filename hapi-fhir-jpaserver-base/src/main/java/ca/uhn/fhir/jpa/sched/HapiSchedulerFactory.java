package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.rest.server.sched.IHapiScheduler;
import ca.uhn.fhir.rest.server.sched.ISchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class HapiSchedulerFactory implements ISchedulerFactory {
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
