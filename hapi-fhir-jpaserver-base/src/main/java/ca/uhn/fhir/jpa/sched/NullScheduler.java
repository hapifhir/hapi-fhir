package ca.uhn.fhir.jpa.sched;

class NullScheduler extends BaseHapiScheduler {
	public NullScheduler(String theThreadNamePrefix, AutowiringSpringBeanJobFactory theSpringBeanJobFactory) {
		super(theThreadNamePrefix, theSpringBeanJobFactory);
	}
}
