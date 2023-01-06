package ca.uhn.fhir.jpa.lifecycle.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public abstract class BaseHapiApplicationEvent extends ApplicationEvent {
	public BaseHapiApplicationEvent(Object source) {
		super(source, Clock.systemDefaultZone());
	}

}
