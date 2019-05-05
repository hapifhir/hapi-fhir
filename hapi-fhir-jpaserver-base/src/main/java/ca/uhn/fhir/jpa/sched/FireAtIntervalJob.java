package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.util.StopWatch;
import org.quartz.*;
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
		Long nextExecution = (Long) theContext.get(NEXT_EXECUTION_TIME);
		if (nextExecution != null) {
			long cutoff = System.currentTimeMillis();
			if (nextExecution >= cutoff) {
				ourLog.info("NOT FIRING JOB FOR ANOTHER {}", StopWatch.formatMillis(nextExecution - cutoff));
				return;
			}
		}

		try {
			doExecute(theContext);
		} catch (Throwable t) {
			ourLog.error("Job threw uncaught exception", t);
		} finally {
			long newNextExecution = System.currentTimeMillis() + myMillisBetweenExecutions;
			theContext.put(NEXT_EXECUTION_TIME, newNextExecution);
		}
	}

	protected abstract void doExecute(JobExecutionContext theContext);

}
