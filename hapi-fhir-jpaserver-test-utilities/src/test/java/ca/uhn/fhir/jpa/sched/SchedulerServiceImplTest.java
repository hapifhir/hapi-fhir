package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AopTestUtils;

import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.fail;

@ContextConfiguration(classes = SchedulerServiceImplTest.TestConfiguration.class)
@ExtendWith(SpringExtension.class)
@DirtiesContext
public class SchedulerServiceImplTest {

	private static final Logger ourLog = LoggerFactory.getLogger(SchedulerServiceImplTest.class);
	private static long ourTaskDelay;
	@Autowired
	private ISchedulerService mySvc;

	@BeforeEach
	public void before() {
		ourTaskDelay = 0;
	}

	@Test
	public void testScheduleTask() {

		ScheduledJobDefinition def = new ScheduledJobDefinition()
			.setId(CountingJob.class.getName())
			.setJobClass(CountingJob.class);

		mySvc.scheduleLocalJob(100, def);

		sleepAtLeast(1000);

		ourLog.info("Fired {} times", CountingJob.ourCount);

		assertThat(CountingJob.ourCount, greaterThan(3));
		assertThat(CountingJob.ourCount, lessThan(20));
	}

	@Test
	public void testStopAndStartService() throws SchedulerException {

		ScheduledJobDefinition def = new ScheduledJobDefinition()
			.setId(CountingJob.class.getName())
			.setJobClass(CountingJob.class);

		BaseSchedulerServiceImpl svc = AopTestUtils.getTargetObject(mySvc);
		svc.stop();
		svc.create();
		svc.start();

		mySvc.scheduleLocalJob(100, def);

		sleepAtLeast(1000);

		ourLog.info("Fired {} times", CountingJob.ourCount);

		await().until(() -> CountingJob.ourCount, greaterThan(3));
		assertThat(CountingJob.ourCount, lessThan(50));
	}

	@Test
	public void testScheduleTaskLongRunningDoesntRunConcurrently() {

		ScheduledJobDefinition def = new ScheduledJobDefinition()
			.setId(CountingJob.class.getName())
			.setJobClass(CountingJob.class);
		ourTaskDelay = 500;

		mySvc.scheduleLocalJob(100, def);

		sleepAtLeast(1000);

		ourLog.info("Fired {} times", CountingJob.ourCount);

		await().until(() -> CountingJob.ourCount, greaterThanOrEqualTo(1));
		assertThat(CountingJob.ourCount, lessThan(5));
	}

	@Test
	public void testIntervalJob() {

		ScheduledJobDefinition def = new ScheduledJobDefinition()
			.setId(CountingIntervalJob.class.getName())
			.setJobClass(CountingIntervalJob.class);
		ourTaskDelay = 500;

		mySvc.scheduleLocalJob(100, def);

		sleepAtLeast(2000);

		ourLog.info("Fired {} times", CountingIntervalJob.ourCount);

		await().until(() -> CountingIntervalJob.ourCount, greaterThanOrEqualTo(2));
		assertThat(CountingIntervalJob.ourCount, lessThan(6));
	}

	@AfterEach
	public void after() throws SchedulerException {
		CountingJob.ourCount = 0;
		CountingIntervalJob.ourCount = 0;
		mySvc.purgeAllScheduledJobsForUnitTest();
	}

	@DisallowConcurrentExecution
	public static class CountingJob implements Job, ApplicationContextAware {

		private static int ourCount;

		@Autowired
		@Qualifier("stringBean")
		private String myStringBean;
		private ApplicationContext myAppCtx;

		@Override
		public void execute(JobExecutionContext theContext) {
			if (!"String beans are good.".equals(myStringBean)) {
				fail("Did not autowire stringBean correctly, found: " + myStringBean);
			}
			if (myAppCtx == null) {
				fail("Did not populate appctx");
			}
			if (ourTaskDelay > 0) {
				ourLog.info("Job has fired, going to sleep for {}ms", ourTaskDelay);
				sleepAtLeast(ourTaskDelay);
				ourLog.info("Done sleeping");
			} else {
				ourLog.info("Job has fired...");
			}
			ourCount++;
		}

		@Override
		public void setApplicationContext(ApplicationContext theAppCtx) throws BeansException {
			myAppCtx = theAppCtx;
		}
	}

	public static class CountingIntervalJob implements HapiJob {

		private static int ourCount;

		@Autowired
		@Qualifier("stringBean")
		private String myStringBean;
		private ApplicationContext myAppCtx;

		@Override
		public void execute(JobExecutionContext theContext) {
			ourLog.info("Job has fired, going to sleep for {}ms", ourTaskDelay);
			sleepAtLeast(ourTaskDelay);
			ourCount++;
		}
	}

	@Configuration
	public static class TestConfiguration {

		@Bean
		public ISchedulerService schedulerService() {
			return new HapiSchedulerServiceImpl();
		}

		@Bean
		public String stringBean() {
			return "String beans are good.";
		}

		@Bean
		public AutowiringSpringBeanJobFactory springBeanJobFactory() {
			return new AutowiringSpringBeanJobFactory();
		}

	}
}
