package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.model.sched.IHapiScheduler;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.Validate;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME;

public abstract class BaseHapiScheduler implements IHapiScheduler {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseHapiScheduler.class);

	private static final AtomicInteger ourNextSchedulerId = new AtomicInteger();

	private final String myThreadNamePrefix;
	private final AutowiringSpringBeanJobFactory mySpringBeanJobFactory;
	private final StdSchedulerFactory myFactory = new StdSchedulerFactory();
	private final Properties myProperties = new Properties();

	private Scheduler myScheduler;

	public BaseHapiScheduler(String theThreadNamePrefix, AutowiringSpringBeanJobFactory theSpringBeanJobFactory) {
		myThreadNamePrefix = theThreadNamePrefix;
		mySpringBeanJobFactory = theSpringBeanJobFactory;
	}


	void setInstanceName(String theName) {
		myProperties.setProperty(PROP_SCHED_INSTANCE_NAME, theName + "-" + nextSchedulerId());
	}


	int nextSchedulerId() {
		return ourNextSchedulerId.getAndIncrement();
	}

	@Override
	public void init() throws SchedulerException {
		setProperties();
		myFactory.initialize(myProperties);
		myScheduler = myFactory.getScheduler();
		myScheduler.setJobFactory(mySpringBeanJobFactory);
		myScheduler.standby();
	}

	protected void setProperties() {
		addProperty("org.quartz.threadPool.threadCount", "4");
		addProperty("org.quartz.threadPool.threadNamePrefix", myThreadNamePrefix + "-" + myProperties.get(PROP_SCHED_INSTANCE_NAME));
	}

	protected void addProperty(String key, String value) {
		myProperties.put(key, value);
	}

	@Override
	public void start() {
		try {
			myScheduler.start();
		} catch (SchedulerException e) {
			ourLog.error("Failed to start up scheduler", e);
			throw new ConfigurationException("Failed to start up scheduler", e);
		}
	}

	@Override
	public void shutdown() {
		try {
			myScheduler.shutdown(true);
		} catch (SchedulerException e) {
			ourLog.error("Failed to shut down scheduler", e);
			throw new ConfigurationException("Failed to shut down scheduler", e);
		}
	}

	@Override
	public boolean isStarted() {
		try {
			return myScheduler.isStarted();
		} catch (SchedulerException e) {
			ourLog.error("Failed to determine scheduler status", e);
			return false;
		}
	}

	@Override
	public void clear() throws SchedulerException {
		myScheduler.clear();
	}

	@Override
	public void logStatusForUnitTest() {
		try {
			Set<JobKey> keys = myScheduler.getJobKeys(GroupMatcher.anyGroup());
			String keysString = keys.stream().map(t -> t.getName()).collect(Collectors.joining(", "));
			ourLog.info("Local scheduler has jobs: {}", keysString);
		} catch (SchedulerException e) {
			ourLog.error("Failed to get log status for scheduler", e);
			throw new InternalErrorException("Failed to get log status for scheduler", e);
		}
	}

	@Override
	public void scheduleJob(long theIntervalMillis, ScheduledJobDefinition theJobDefinition) {
		Validate.isTrue(theIntervalMillis >= 100);

		Validate.notNull(theJobDefinition);
		Validate.notNull(theJobDefinition.getJobClass());
		Validate.notBlank(theJobDefinition.getId());

		JobKey jobKey = new JobKey(theJobDefinition.getId());

		JobDetailImpl jobDetail = new NonConcurrentJobDetailImpl();
		jobDetail.setJobClass(theJobDefinition.getJobClass());
		jobDetail.setKey(jobKey);
		jobDetail.setName(theJobDefinition.getId());
		jobDetail.setJobDataMap(new JobDataMap(theJobDefinition.getJobData()));

		ScheduleBuilder<? extends Trigger> schedule = SimpleScheduleBuilder
			.simpleSchedule()
			.withIntervalInMilliseconds(theIntervalMillis)
			.repeatForever();

		Trigger trigger = TriggerBuilder.newTrigger()
			.forJob(jobDetail)
			.startNow()
			.withSchedule(schedule)
			.build();

		Set<? extends Trigger> triggers = Sets.newHashSet(trigger);
		try {
			myScheduler.scheduleJob(jobDetail, triggers, true);
		} catch (SchedulerException e) {
			ourLog.error("Failed to schedule job", e);
			throw new InternalErrorException(e);
		}

	}

	private static class NonConcurrentJobDetailImpl extends JobDetailImpl {
		private static final long serialVersionUID = 5716197221121989740L;

		// All HAPI FHIR jobs shouldn't allow concurrent execution
		@Override
		public boolean isConcurrentExectionDisallowed() {
			return true;
		}
	}
}
