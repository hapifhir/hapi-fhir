/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.jpa.model.sched.IHapiScheduler;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class HapiNullScheduler implements IHapiScheduler {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiNullScheduler.class);

	@Override
	public void init() {
		// nothing
	}

	@Override
	public void start() {}

	@Override
	public void shutdown() {}

	@Override
	public boolean isStarted() {
		return true;
	}

	@Override
	public void clear() throws SchedulerException {}

	@Override
	public void logStatusForUnitTest() {}

	@Override
	public void pause() {
		// nothing to do
	}

	@Override
	public void unpause() {
		// nothing to do
	}

	@Override
	public void scheduleJob(long theIntervalMillis, ScheduledJobDefinition theJobDefinition) {
		scheduleJob(theJobDefinition.getId());
	}

	@Override
	public void scheduleCronJob(String theCronExpression, ScheduledJobDefinition theJobDefinition) {
		scheduleJob(theJobDefinition.getId());
	}

	private void scheduleJob(String theJobId) {
		ourLog.debug("Skipping scheduling job {} since scheduling is disabled", theJobId);
	}

	@Override
	public void unscheduleJobs(TriggerKey... theTriggerKeys) {
		// no real scheduler, but we'll log in case testers / users require knowledge that
		// this method was called
		ourLog.debug(
				"Unscheduling job(s) {}. This is a null scheduler so this is a no-op.",
				String.join(
						", ",
						Arrays.stream(theTriggerKeys)
								.map(j -> j.getGroup() + "|" + j.getName())
								.collect(Collectors.toSet())));
	}

	@Override
	public Set<JobKey> getJobKeysForUnitTest() {
		return null;
	}
}
