package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.jpa.model.sched.FireAtIntervalJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.ProxyUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.AopTestUtils;

import static ca.uhn.fhir.jpa.util.TestUtil.sleepAtLeast;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@ContextConfiguration(classes = SchedulerServiceImplTest.TestConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SchedulerServiceImplTest {

	private static final Logger ourLog = LoggerFactory.getLogger(SchedulerServiceImplTest.class);

	@Autowired
	private ISchedulerService mySvc;
	private static long ourTaskDelay;

	@Before
	public void before() {
		ourTaskDelay = 0;
	}

	@Test
	public void testScheduleTask() {

		ScheduledJobDefinition def = new ScheduledJobDefinition()
			.setId(CountingJob.class.getName())
			.setJobClass(CountingJob.class);

		mySvc.scheduleFixedDelay(100, false, def);

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

		SchedulerServiceImpl svc = AopTestUtils.getTargetObject(mySvc);
		svc.stop();
		svc.start();
		svc.contextStarted(null);

		mySvc.scheduleFixedDelay(100, false, def);

		sleepAtLeast(1000);

		ourLog.info("Fired {} times", CountingJob.ourCount);

		assertThat(CountingJob.ourCount, greaterThan(3));
		assertThat(CountingJob.ourCount, lessThan(20));
	}

	@Test
	public void testScheduleTaskLongRunningDoesntRunConcurrently() {

		ScheduledJobDefinition def = new ScheduledJobDefinition()
			.setId(CountingJob.class.getName())
			.setJobClass(CountingJob.class);
		ourTaskDelay = 500;

		mySvc.scheduleFixedDelay(100, false, def);

		sleepAtLeast(1000);

		ourLog.info("Fired {} times", CountingJob.ourCount);

		assertThat(CountingJob.ourCount, greaterThanOrEqualTo(1));
		assertThat(CountingJob.ourCount, lessThan(5));
	}

	@Test
	public void testIntervalJob() {

		ScheduledJobDefinition def = new ScheduledJobDefinition()
			.setId(CountingIntervalJob.class.getName())
			.setJobClass(CountingIntervalJob.class);
		ourTaskDelay = 500;

		mySvc.scheduleFixedDelay(100, false, def);

		sleepAtLeast(2000);

		ourLog.info("Fired {} times", CountingIntervalJob.ourCount);

		assertThat(CountingIntervalJob.ourCount, greaterThanOrEqualTo(2));
		assertThat(CountingIntervalJob.ourCount, lessThan(6));
	}

	@After
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


	@DisallowConcurrentExecution
	@PersistJobDataAfterExecution
	public static class CountingIntervalJob extends FireAtIntervalJob {

		private static int ourCount;

		@Autowired
		@Qualifier("stringBean")
		private String myStringBean;
		private ApplicationContext myAppCtx;

		public CountingIntervalJob() {
			super(500);
		}

		@Override
		public void doExecute(JobExecutionContext theContext) {
				ourLog.info("Job has fired, going to sleep for {}ms", ourTaskDelay);
				sleepAtLeast(ourTaskDelay);
			ourCount++;
		}

	}


	@Configuration
	public static class TestConfiguration {

		@Bean
		public ISchedulerService schedulerService() {
			return new SchedulerServiceImpl();
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
