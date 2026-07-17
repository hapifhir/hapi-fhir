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
	private Duration myAckTimeout = Duration.ofMillis(1001);

	public WorkChunkHeartbeatService(ISchedulerService theSchedulerService) {
		myScheduleSvc = theSchedulerService;
	}

	/**
	 * Set the timeout (done asynchronously because we do not have the
	 * timeout value at initialization)
	 */
	public void setAckTimeout(Duration theAckTimeout) {
		if (theAckTimeout != null) {
			// we don't want a time that's <100ms
			long ackTimeout = Math.max(theAckTimeout.toMillis() / 3, 500);
			myAckTimeout = Duration.ofMillis(ackTimeout);
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
		myScheduleSvc.scheduleLocalJob(myAckTimeout.toMillis(), definition);
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
