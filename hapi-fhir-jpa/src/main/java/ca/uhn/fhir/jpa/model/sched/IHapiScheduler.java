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
package ca.uhn.fhir.jpa.model.sched;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

import java.util.Set;

public interface IHapiScheduler {
	void init() throws SchedulerException;

	void start();

	void shutdown();

	boolean isStarted();

	void clear() throws SchedulerException;

	void logStatusForUnitTest();

	/**
	 * Pauses this scheduler (and thus all scheduled jobs).
	 * To restart call {@link #unpause()}
	 */
	void pause();

	/**
	 * Restarts this scheduler after {@link #pause()}
	 */
	void unpause();

	void scheduleJob(long theIntervalMillis, ScheduledJobDefinition theJobDefinition);

	/**
	 * Unschedules job(s) by their trigger keys (usually group + id, but this is provided
	 * by ScheduledJobDefinition).
	 * @param theTriggerKeys - keys of jobs to unschedule
	 */
	void unscheduleJobs(TriggerKey... theTriggerKeys);

	/**
	 * Schedules a job on this scheduler to run on a schedule defined by the Quartz
	 * cron expression.
	 * @param theCronExpression The Quartz cron expression
	 * @param theJobDefinition the JobDefinition to run
	 */
	void scheduleCronJob(String theCronExpression, ScheduledJobDefinition theJobDefinition);

	Set<JobKey> getJobKeysForUnitTest() throws SchedulerException;

	default void triggerJobImmediately(ScheduledJobDefinition theJobDefinition) {}
}
