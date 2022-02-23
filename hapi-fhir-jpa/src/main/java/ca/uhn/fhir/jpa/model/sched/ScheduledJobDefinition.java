package ca.uhn.fhir.jpa.model.sched;

/*-
 * #%L
 * hapi-fhir-jpa
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.quartz.Job;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ScheduledJobDefinition {
	private Class<? extends Job> myJobClass;
	private String myId;
	private String myGroup;
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

	public String getGroup() {
		return myGroup;
	}

	public ScheduledJobDefinition setGroup(String theGroup) {
		myGroup = theGroup;
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

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myJobClass", myJobClass)
			.append("myId", myId)
			.append("myGroup", myGroup)
			.toString();
	}
}
