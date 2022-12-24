package ca.uhn.fhir.lifecycle;

import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

public abstract class BaseHapiLifecycle {
	@EventListener(classes = {ContextStartedEvent.class})
	@Order
	public abstract void startup();

	@EventListener(classes = {ContextStoppedEvent.class})
	@Order
	public abstract void shutdown();

}
