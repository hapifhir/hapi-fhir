package ca.uhn.fhir.jpa.sched;

import java.util.Properties;

public class LocalHapiScheduler extends BaseHapiScheduler {
	public LocalHapiScheduler(String theThreadNamePrefix, AutowiringSpringBeanJobFactory theSpringBeanJobFactory) {
		super(theThreadNamePrefix, theSpringBeanJobFactory);
		setInstanceName("local");
	}

	/**
	 * Properties for the local scheduler (see the class docs to learn what this means)
	 */
	@Override
	void addProperties(Properties theProperties) {
		// nothing
	}
}
