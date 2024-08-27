/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import com.google.common.annotations.VisibleForTesting;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import java.util.Set;

public interface ISchedulerService {

	@VisibleForTesting
	void purgeAllScheduledJobsForUnitTest() throws SchedulerException;

	void logStatusForUnitTest();

	/**
	 * Pauses the scheduler so no new jobs will run.
	 * Useful in tests when cleanup needs to happen but scheduled jobs may
	 * be running
	 */
	@VisibleForTesting
	void pause();

	/**
	 * Restarts the scheduler after a previous call to {@link #pause()}.
	 */
	@VisibleForTesting
	void unpause();

	/**
	 * This task will execute locally (and should execute on all nodes of the cluster if there is a cluster)
	 * @param theIntervalMillis How many milliseconds between passes should this job run
	 * @param theJobDefinition  The Job to fire
	 */
	void scheduleLocalJob(long theIntervalMillis, ScheduledJobDefinition theJobDefinition);

	/**
	 * Only one instance of this task will fire across the whole cluster (when running in a clustered environment).
	 * @param theIntervalMillis How many milliseconds between passes should this job run
	 * @param theJobDefinition  The Job to fire
	 */
	void scheduleClusteredJob(long theIntervalMillis, ScheduledJobDefinition theJobDefinition);

	@VisibleForTesting
	Set<JobKey> getLocalJobKeysForUnitTest() throws SchedulerException;

	@VisibleForTesting
	Set<JobKey> getClusteredJobKeysForUnitTest() throws SchedulerException;

	@VisibleForTesting
	boolean isSchedulingDisabled();

	boolean isStopping();

	/**
	 * Rather than waiting for the job to fire at its scheduled time, fire it immediately.
	 * @param theJobDefinition
	 */
	default void triggerLocalJobImmediately(ScheduledJobDefinition theJobDefinition) {}

	/**
	 * Rather than waiting for the job to fire at its scheduled time, fire it immediately.
	 * @param theJobDefinition
	 */
	default void triggerClusteredJobImmediately(ScheduledJobDefinition theJobDefinition) {}

	/**
	 * @return true if this server supports clustered scheduling
	 */
	default boolean isClusteredSchedulingEnabled() {
		return false;
	}
}
