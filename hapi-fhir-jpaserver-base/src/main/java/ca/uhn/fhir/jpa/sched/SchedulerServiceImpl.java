package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.Validate;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;
import java.util.Set;

/**
 * This class provides task scheduling for the entire module using the Quartz library.
 * Inside here, we have two schedulers:
 * <ul>
 * <li>
 * The <b>Local Scheduler</b> handles tasks that need to execute locally. This
 * typically means things that should happen on all nodes in a clustered
 * environment.
 * </li>
 * <li>
 * The <b>Cluster Scheduler</b> handles tasks that are distributed and should be
 * handled by only one node in the cluster (assuming a clustered server). If the
 * server is not clustered, this scheduler acts the same way as the
 * local scheduler.
 * </li>
 * </ul>
 */
public class SchedulerServiceImpl implements ISchedulerService {

	private static final Logger ourLog = LoggerFactory.getLogger(SchedulerServiceImpl.class);
	private Scheduler myLocalScheduler;
	private Scheduler myClusteredScheduler;
	private String myThreadNamePrefix;
	@Autowired
	private AutowiringSpringBeanJobFactory mySpringBeanJobFactory;

	/**
	 * Constructor
	 */
	public SchedulerServiceImpl() {
		setThreadNamePrefix("hapi-fhir-jpa-scheduler");
	}

	public String getThreadNamePrefix() {
		return myThreadNamePrefix;
	}

	public void setThreadNamePrefix(String theThreadNamePrefix) {
		myThreadNamePrefix = theThreadNamePrefix;
	}

	@PostConstruct
	public void start() throws SchedulerException {

		myLocalScheduler = createLocalScheduler();
		myClusteredScheduler = createClusteredScheduler();

		ourLog.info("Starting task scheduler");
		myLocalScheduler.start();
	}

	private Scheduler createLocalScheduler() throws SchedulerException {
		Properties localProperties = new Properties();
		quartzPropertiesCommon(localProperties);
		quartzPropertiesLocal(localProperties);
		StdSchedulerFactory factory = new StdSchedulerFactory();
		factory.initialize(localProperties);
		Scheduler scheduler = factory.getScheduler();
		configureSchedulerCommon(scheduler);
		return scheduler;
	}

	private void configureSchedulerCommon(Scheduler theScheduler) throws SchedulerException {
		theScheduler.setJobFactory(mySpringBeanJobFactory);
	}

	private Scheduler createClusteredScheduler() throws SchedulerException {
		Properties clusteredProperties = new Properties();
		quartzPropertiesCommon(clusteredProperties);
		quartzPropertiesClustered(clusteredProperties);
		StdSchedulerFactory factory = new StdSchedulerFactory();
		factory.initialize(clusteredProperties);
		Scheduler scheduler = factory.getScheduler();
		configureSchedulerCommon(scheduler);
		return scheduler;
	}

	@Override
	@PreDestroy
	public void stop() throws SchedulerException {
		ourLog.info("Shutting down task scheduler...");
		myLocalScheduler.shutdown(true);
	}

	@Override
	public void scheduleFixedDelay(long theIntervalMillis, boolean theClusteredTask, ScheduledJobDefinition theJobDefinition) {
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
			Scheduler scheduler;
			if (theClusteredTask) {
				scheduler = myClusteredScheduler;
			} else {
				scheduler = myLocalScheduler;
			}
			scheduler.scheduleJob(jobDetail, triggers, true);
		} catch (SchedulerException e) {
			ourLog.error("Failed to schedule job", e);
			throw new InternalErrorException(e);
		}
	}


	/**
	 * Properties for the local scheduler (see the class docs to learn what this means)
	 */
	protected void quartzPropertiesLocal(Properties theProperties) {
		theProperties.put("org.quartz.threadPool.threadNamePrefix", getThreadNamePrefix() + "-local");
	}

	/**
	 * Properties for the cluster scheduler (see the class docs to learn what this means)
	 */
	protected void quartzPropertiesClustered(Properties theProperties) {
		theProperties.put("org.quartz.threadPool.threadNamePrefix", getThreadNamePrefix() + "-clustered");
		theProperties.put("org.quartz.jobStore.tablePrefix", "QRTZHFJC_");
	}

	protected void quartzPropertiesCommon(Properties theProperties) {
		theProperties.put("org.quartz.threadPool.threadCount", "4");
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
