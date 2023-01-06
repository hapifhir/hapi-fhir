package ca.uhn.fhir.jpa.lifecycle;

import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.sched.SchedulerJobsLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public class SchedulerLifecycleService {
	private static final Logger ourLog = LoggerFactory.getLogger(SchedulerLifecycleService.class);
	private final ISchedulerService mySchedulerService;
	private final SchedulerJobsLoader mySchedulerJobsLoader;

	public SchedulerLifecycleService(ISchedulerService theSchedulerService, SchedulerJobsLoader theSchedulerJobsLoader) {
		mySchedulerService = theSchedulerService;
		mySchedulerJobsLoader = theSchedulerJobsLoader;
	}

	@EventListener(ContextRefreshedEvent.class)
	public void onContextRefreshed() {
		// Start the schedulers
		mySchedulerService.start();

		// Schedule all the jobs
		mySchedulerJobsLoader.scheduleJobs();
	}
}
