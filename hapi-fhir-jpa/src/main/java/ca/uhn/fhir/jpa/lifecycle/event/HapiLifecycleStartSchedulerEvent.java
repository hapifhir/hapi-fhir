package ca.uhn.fhir.jpa.lifecycle.event;

public class HapiLifecycleStartSchedulerEvent extends BaseHapiApplicationEvent {
	public HapiLifecycleStartSchedulerEvent(Object theSource) {
		super(theSource);
	}
}
