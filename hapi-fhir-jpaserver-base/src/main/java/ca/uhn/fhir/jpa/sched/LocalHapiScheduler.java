package ca.uhn.fhir.jpa.sched;

public class LocalHapiScheduler extends BaseHapiScheduler {
	public LocalHapiScheduler(String theThreadNamePrefix, AutowiringSpringBeanJobFactory theSpringBeanJobFactory) {
		super(theThreadNamePrefix, theSpringBeanJobFactory);
		setInstanceName("local");
	}
}
