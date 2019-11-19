package ca.uhn.fhir.jpa.sched;

import java.util.Properties;

class NullScheduler extends BaseHapiScheduler {

	public NullScheduler(String theThreadNamePrefix, AutowiringSpringBeanJobFactory theSpringBeanJobFactory) {
		super(theThreadNamePrefix, theSpringBeanJobFactory);
	}

	@Override
	void addProperties(Properties theProperties) {
		//nothing
	}
}
