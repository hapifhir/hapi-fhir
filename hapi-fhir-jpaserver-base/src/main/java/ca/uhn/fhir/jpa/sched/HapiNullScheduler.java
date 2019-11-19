package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HapiNullScheduler implements IHapiScheduler {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiNullScheduler.class);

	@Override
	public void init() {
		// nothing
	}

	@Override
	public void start() {

	}

	@Override
	public void shutdown() {

	}

	@Override
	public boolean isStarted() {
		return true;
	}

	@Override
	public void clear() throws SchedulerException {

	}

	@Override
	public void logStatusForUnitTest() {

	}

	@Override
	public void scheduleFixedDelay(long theIntervalMillis, ScheduledJobDefinition theJobDefinition) {
		ourLog.debug("Skipping scheduling job {} since scheduling is disabled", theJobDefinition.getId());
	}
}
