package ca.uhn.fhir.jpa.lifecycle.event;

public class HapiLifecycleSchedulerStartedEvent extends BaseHapiApplicationEvent {
	public HapiLifecycleSchedulerStartedEvent(Object theSource) {
		super(theSource);
	}
}
