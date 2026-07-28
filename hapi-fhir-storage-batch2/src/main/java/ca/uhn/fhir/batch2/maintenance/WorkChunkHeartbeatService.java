/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.api.IWorkChunkPersistence;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import jakarta.annotation.Nullable;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

/**
 * Heartbeat factory service to manage
 * batch job 'heartbeat' maintenance.
 */
public class WorkChunkHeartbeatService {
	public static final String SCHEDULED_JOB_ID_PREFIX = "BATCH2-HEARTBEAT";
	private static final String CHUNK_ID = "chunk-id";

	private final ISchedulerService myScheduleSvc;
	/**
	 * We use the timeout of the message delivery system (kafka, activemq, pulsar, etc)
	 * to determine the heartbeat timeout.
	 * This is to allow us to schedule a heartbeat before the redelibery.
	 * NB: There's no consistent 'acktimeout' value for all delivery systems,
	 * so it might not always be an acktimeout, but could be some other metric
	 * for how long a message is expected to take to process.
	 */
	private Duration myHeartbeatInterval = Duration.ofMillis(1001);

	public WorkChunkHeartbeatService(ISchedulerService theSchedulerService) {
		myScheduleSvc = theSchedulerService;
	}

	/**
	 * Set the timeout (setting comes from queuing service.
	 * It will be set on startup after said service is online
	 * so we don't have it until the service starts up)
	 */
	public void setAckTimeout(Duration theAckTimeout) {
		if (theAckTimeout != null) {
			// we don't want a time that's too small (eg <100ms)
			long ackTimeout = Math.max(theAckTimeout.toMillis() / 3, 500);
			myHeartbeatInterval = Duration.ofMillis(ackTimeout);
		}
	}

	public HeartbeatHandle scheduleHeartbeatJob(String theInstanceId, @Nullable String theChunkId) {
		if (theChunkId == null) {
			// no-op for cases when no workchunk exists
			// eg: reducerjobs that aren't backed by jpa
			//     or are outstanding (pre-update) jobs partially through running
			return () -> {};
		}
		String jobId = String.format("%s-%s-%s", SCHEDULED_JOB_ID_PREFIX, theInstanceId, theChunkId);
		ScheduledJobDefinition definition = new ScheduledJobDefinition();
		definition.setJobClass(HeartbeatJob.class);
		definition.setId(jobId);
		definition.addJobData(CHUNK_ID, theChunkId);
		TriggerKey key = definition.toTriggerKey();
		myScheduleSvc.scheduleLocalJob(myHeartbeatInterval.toMillis(), definition);
		return () -> myScheduleSvc.unscheduleLocalJobs(key);
	}

	public interface HeartbeatHandle extends AutoCloseable {
		@Override
		void close();
	}

	public static class HeartbeatJob implements HapiJob {

		@Autowired
		private IWorkChunkPersistence myWorkChunkPersistence;

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			String workchunkId = (String) context.getMergedJobDataMap().get(CHUNK_ID);

			myWorkChunkPersistence.onWorkChunkHeartbeat(workchunkId);
		}
	}
}
