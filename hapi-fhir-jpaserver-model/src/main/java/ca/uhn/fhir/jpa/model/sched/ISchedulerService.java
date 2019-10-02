package ca.uhn.fhir.jpa.model.sched;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import org.quartz.SchedulerException;

public interface ISchedulerService {

	@VisibleForTesting
	void purgeAllScheduledJobsForUnitTest() throws SchedulerException;

	void logStatus();

	/**
	 * @param theIntervalMillis How many milliseconds between passes should this job run
	 * @param theClusteredTask  If <code>true</code>, only one instance of this task will fire across the whole cluster (when running in a clustered environment). If <code>false</code>, or if not running in a clustered environment, this task will execute locally (and should execute on all nodes of the cluster)
	 * @param theJobDefinition  The Job to fire
	 */
	void scheduleFixedDelay(long theIntervalMillis, boolean theClusteredTask, ScheduledJobDefinition theJobDefinition);

	boolean isStopping();
}
