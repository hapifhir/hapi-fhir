package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.util.StopWatch;
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

import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.fail;

@ContextConfiguration(classes = SchedulerServiceImplTest.TestConfiguration.class)
@ExtendWith(SpringExtension.class)
@DirtiesContext
public class SchedulerServiceImplTest {

	private static final Logger ourLog = LoggerFactory.getLogger(SchedulerServiceImplTest.class);
	public static final String SCHEDULED_JOB_ID = CountingJob.class.getName();
	private static final AtomicInteger ourNameCounter = new AtomicInteger();
	private static long ourTaskDelay;
	@Autowired
	private ISchedulerService mySvc;

	@BeforeEach
	public void before() {
		ourTaskDelay = 0;
	}

	@Test
	public void testScheduleTask() {

		ScheduledJobDefinition def = buildJobDefinition();

		StopWatch sw = new StopWatch();
		mySvc.scheduleLocalJob(100, def);
		await().until(CountingJob.ourCount::get, count -> count > 5);

		ourLog.info("Fired {} times in {}", CountingJob.ourCount, sw);
		assertThat(sw.getMillis()).isGreaterThan(500L);
		assertThat(sw.getMillis()).isLessThan(1000L);
	}

	@Test
	public void triggerImmediately_runsJob() {

		ScheduledJobDefinition def = buildJobDefinition();

		StopWatch sw = new StopWatch();
		mySvc.scheduleLocalJob(100, def);
		for (int i = 0; i < 20; ++i) {
			mySvc.triggerLocalJobImmediately(def);
		}
		await().until(CountingJob.ourCount::get, count -> count > 25);

		ourLog.info("Fired {} times in {}", CountingJob.ourCount, sw);
		assertThat(sw.getMillis()).isGreaterThan(500L);
		assertThat(sw.getMillis()).isLessThan(1000L);

	}

	private static ScheduledJobDefinition buildJobDefinition() {
		return new ScheduledJobDefinition()
			.setId(SCHEDULED_JOB_ID + ourNameCounter.incrementAndGet())
			.setJobClass(CountingJob.class);
	}

	@Test
	public void testStopAndStartService() throws SchedulerException {

		ScheduledJobDefinition def = buildJobDefinition();

		BaseSchedulerServiceImpl svc = AopTestUtils.getTargetObject(mySvc);

		svc.stop();
		svc.create();
		svc.start();

		StopWatch sw = new StopWatch();
		mySvc.scheduleLocalJob(100, def);

		await().until(CountingJob.ourCount::get, count -> count > 5);

		ourLog.info("Fired {} times in {}", CountingJob.ourCount, sw);
		assertThat(sw.getMillis()).isGreaterThan(0L);
		assertThat(sw.getMillis()).isLessThan(1000L);
	}

	@Test
	public void testScheduleTaskLongRunningDoesntRunConcurrently() {
		ScheduledJobDefinition def = buildJobDefinition();
		ourTaskDelay = 500;

		StopWatch sw = new StopWatch();
		mySvc.scheduleLocalJob(100, def);

		await().until(CountingJob.ourCount::get, count -> count > 5);

		ourLog.info("Fired {} times in {}", CountingJob.ourCount, sw);
		assertThat(sw.getMillis()).isGreaterThan(3000L);
		assertThat(sw.getMillis()).isLessThan(3500L);
	}

	@Test
	public void testScheduleTaskLongRunningDoesntRunConcurrentlyWithTrigger() {
		ScheduledJobDefinition def = buildJobDefinition();
		ourTaskDelay = 500;

		StopWatch sw = new StopWatch();
		mySvc.scheduleLocalJob(100, def);
		mySvc.triggerLocalJobImmediately(def);
		mySvc.triggerLocalJobImmediately(def);

		await().until(CountingJob.ourCount::get, count -> count > 5);

		ourLog.info("Fired {} times in {}", CountingJob.ourCount, sw);
		assertThat(sw.getMillis()).isGreaterThan(3000L);
		assertThat(sw.getMillis()).isLessThan(3500L);
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

		await().until(() -> CountingIntervalJob.ourCount, count -> count >= 2);
		assertThat(CountingIntervalJob.ourCount).isLessThan(6);
	}

	@AfterEach
	public void after() throws SchedulerException {
		CountingJob.resetCount();
		CountingIntervalJob.ourCount = 0;
		mySvc.purgeAllScheduledJobsForUnitTest();
	}

	@DisallowConcurrentExecution
	public static class CountingJob implements Job, ApplicationContextAware {

		private static AtomicInteger ourCount = new AtomicInteger();
		private static boolean ourRunning = false;

		@Autowired
		@Qualifier("stringBean")
		private String myStringBean;
		private ApplicationContext myAppCtx;

		public static void resetCount() {
			ourCount = new AtomicInteger();
		}

		@Override
		public void execute(JobExecutionContext theContext) {
			if (ourRunning) {
				fail("");
			}
			ourRunning = true;
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
			ourCount.incrementAndGet();
			ourRunning = false;
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
