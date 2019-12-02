package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.jpa.model.sched.IHapiScheduler;

public class HapiSchedulerServiceImpl extends BaseSchedulerServiceImpl {
	public static final String THREAD_NAME_PREFIX = "hapi-fhir-jpa-scheduler";

	@Override
	protected IHapiScheduler getLocalHapiScheduler() {
		return new LocalHapiScheduler(THREAD_NAME_PREFIX, mySchedulerJobFactory);
	}

	@Override
	protected IHapiScheduler getClusteredScheduler() {
		return new ClusteredHapiScheduler(THREAD_NAME_PREFIX, mySchedulerJobFactory);
	}
}
