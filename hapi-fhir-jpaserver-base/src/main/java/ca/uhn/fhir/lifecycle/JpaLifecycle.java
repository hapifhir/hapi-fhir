package ca.uhn.fhir.lifecycle;

import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import org.springframework.beans.factory.annotation.Autowired;

public class JpaLifecycle extends BaseHapiLifecycle {
	@Autowired
	ISchedulerService mySchedulerService;

	@Override
	public void startup() {
		mySchedulerService.start();
	}

	@Override
	public void shutdown() {
		mySchedulerService.stop();
	}
}
