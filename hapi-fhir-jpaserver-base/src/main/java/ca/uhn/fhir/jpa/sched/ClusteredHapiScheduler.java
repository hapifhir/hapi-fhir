package ca.uhn.fhir.jpa.sched;

public class ClusteredHapiScheduler extends BaseHapiScheduler {
	public ClusteredHapiScheduler(String theThreadNamePrefix, AutowiringSpringBeanJobFactory theSpringBeanJobFactory) {
		super(theThreadNamePrefix, theSpringBeanJobFactory);
		setInstanceName("clustered");
	}
}
