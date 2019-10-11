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

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public abstract class FireAtIntervalJob implements Job {

	public static final String NEXT_EXECUTION_TIME = "NEXT_EXECUTION_TIME";
	private static final Logger ourLog = LoggerFactory.getLogger(FireAtIntervalJob.class);
	private final long myMillisBetweenExecutions;

	public FireAtIntervalJob(long theMillisBetweenExecutions) {
		myMillisBetweenExecutions = theMillisBetweenExecutions;
	}

	@Override
	public final void execute(JobExecutionContext theContext) {
		Long nextExecution = (Long) theContext.getJobDetail().getJobDataMap().get(NEXT_EXECUTION_TIME);

		if (nextExecution != null) {
			long cutoff = System.currentTimeMillis();
			if (nextExecution >= cutoff) {
				return;
			}
		}

		try {
			doExecute(theContext);
		} catch (Throwable t) {
			ourLog.error("Job threw uncaught exception", t);
		} finally {
			long newNextExecution = System.currentTimeMillis() + myMillisBetweenExecutions;
			theContext.getJobDetail().getJobDataMap().put(NEXT_EXECUTION_TIME, newNextExecution);
		}
	}

	protected abstract void doExecute(JobExecutionContext theContext);

}
