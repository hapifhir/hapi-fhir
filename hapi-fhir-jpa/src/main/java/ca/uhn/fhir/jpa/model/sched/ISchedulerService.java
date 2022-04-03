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

import com.google.common.annotations.VisibleForTesting;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import java.util.Set;

public interface ISchedulerService {

	@VisibleForTesting
	void purgeAllScheduledJobsForUnitTest() throws SchedulerException;

	void logStatusForUnitTest();

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

	boolean isStopping();
}
