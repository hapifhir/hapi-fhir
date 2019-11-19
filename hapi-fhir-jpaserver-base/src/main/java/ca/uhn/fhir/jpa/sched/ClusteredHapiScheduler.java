package ca.uhn.fhir.jpa.sched;

import java.util.Properties;

public class ClusteredHapiScheduler extends BaseHapiScheduler {
	public ClusteredHapiScheduler(String theThreadNamePrefix, AutowiringSpringBeanJobFactory theSpringBeanJobFactory) {
		super(theThreadNamePrefix, theSpringBeanJobFactory);
		setInstanceName("clustered");
	}

	/**
	 * Properties for the cluster scheduler (see the class docs to learn what this means)
	 */
	@Override
	void addProperties(Properties theProperties) {
//		theProperties.put("org.quartz.jobStore.tablePrefix", "QRTZHFJC_");
	}
}
