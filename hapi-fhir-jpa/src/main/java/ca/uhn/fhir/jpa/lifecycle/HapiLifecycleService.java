package ca.uhn.fhir.jpa.lifecycle;

import ca.uhn.fhir.jpa.lifecycle.event.HapiLifecycleSchedulerStartedEvent;
import ca.uhn.fhir.jpa.lifecycle.event.HapiLifecycleStartSchedulerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public class HapiLifecycleService {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiLifecycleService.class);
	private final ApplicationEventPublisher myApplicationEventPublisher;

	public HapiLifecycleService(ApplicationEventPublisher theApplicationEventPublisher) {
		myApplicationEventPublisher = theApplicationEventPublisher;
	}

	@EventListener(ContextRefreshedEvent.class)
	public void onContextRefreshed() {
		// Start all the schedulers
		myApplicationEventPublisher.publishEvent(new HapiLifecycleStartSchedulerEvent(this));

		// Notify that the schedulers so the jobs can be scheduled
		myApplicationEventPublisher.publishEvent(new HapiLifecycleSchedulerStartedEvent(this));
	}
}
