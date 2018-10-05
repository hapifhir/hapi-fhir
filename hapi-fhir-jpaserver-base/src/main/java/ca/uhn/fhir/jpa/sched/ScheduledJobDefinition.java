package ca.uhn.fhir.jpa.sched;

import org.quartz.Job;

public class ScheduledJobDefinition {


	private Class<? extends Job> myJobClass;
	private String myName;

	public Class<? extends Job> getJobClass() {
		return myJobClass;
	}

	public ScheduledJobDefinition setJobClass(Class<? extends Job> theJobClass) {
		myJobClass = theJobClass;
		return this;
	}

	public String getName() {
		return myName;
	}

	public ScheduledJobDefinition setName(String theName) {
		myName = theName;
		return this;
	}
}
