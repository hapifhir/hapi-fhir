package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.jpa.util.TestUtil;
import org.junit.After;
import org.junit.Test;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class SchedulerServiceTest {

	private SchedulerService mySvc;



	@Test
	public void testScheduleTask() throws SchedulerException {
		mySvc = new SchedulerService();
		mySvc.start();

		ScheduledJobDefinition def = new ScheduledJobDefinition()
			.setName("TESTER")
			.setJobClass(CountingJob.class);

		mySvc.scheduleFixedDelay(100, def);

		TestUtil.sleepAtLeast(1000);

		ourLog.info("Fired {} times", CountingJob.ourCount);

		assertThat(CountingJob.ourCount, greaterThan(3));
		assertThat(CountingJob.ourCount, lessThan(20));
	}

	@After
	public void after() throws SchedulerException {
		CountingJob.ourCount = 0;

		mySvc.stop();
	}

	public static class CountingJob implements Job{

		private static int ourCount;

		@Override
		public void execute(JobExecutionContext theContext) {
			ourLog.info("Job has fired...");
			ourCount++;
		}
	}
	private static final Logger ourLog = LoggerFactory.getLogger(SchedulerServiceTest.class);
}
