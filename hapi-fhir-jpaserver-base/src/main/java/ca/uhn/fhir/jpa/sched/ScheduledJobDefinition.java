package ca.uhn.fhir.jpa.sched;

import org.apache.commons.lang3.Validate;
import org.quartz.Job;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ScheduledJobDefinition {


	private Class<? extends Job> myJobClass;
	private String myId;
	private Map<String, String> myJobData;

	public Map<String, String> getJobData() {
		Map<String, String> retVal = myJobData;
		if (retVal == null) {
			retVal = Collections.emptyMap();
		}
		return Collections.unmodifiableMap(retVal);
	}

	public Class<? extends Job> getJobClass() {
		return myJobClass;
	}

	public ScheduledJobDefinition setJobClass(Class<? extends Job> theJobClass) {
		myJobClass = theJobClass;
		return this;
	}

	public String getId() {
		return myId;
	}

	public ScheduledJobDefinition setId(String theId) {
		myId = theId;
		return this;
	}

	public void addJobData(String thePropertyName, String thePropertyValue) {
		Validate.notBlank(thePropertyName);
		if (myJobData == null) {
			myJobData = new HashMap<>();
		}
		Validate.isTrue(myJobData.containsKey(thePropertyName) == false);
		myJobData.put(thePropertyName, thePropertyValue);
	}
}
