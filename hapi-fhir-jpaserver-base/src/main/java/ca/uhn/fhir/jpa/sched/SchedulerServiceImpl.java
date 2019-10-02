package ca.uhn.fhir.jpa.sched;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.Validate;
import org.quartz.Calendar;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME;

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
	public static final String SCHEDULING_DISABLED = "scheduling_disabled";
	public static final String SCHEDULING_DISABLED_EQUALS_TRUE = SCHEDULING_DISABLED + "=true";

	private static final Logger ourLog = LoggerFactory.getLogger(SchedulerServiceImpl.class);
	private static int ourNextSchedulerId = 0;
	private Scheduler myLocalScheduler;
	private Scheduler myClusteredScheduler;
	private String myThreadNamePrefix;
	private boolean myLocalSchedulingEnabled;
	private boolean myClusteredSchedulingEnabled;
	@Autowired
	private AutowiringSpringBeanJobFactory mySpringBeanJobFactory;
	private AtomicBoolean myStopping = new AtomicBoolean(false);
	@Autowired
	private Environment myEnvironment;

	/**
	 * Constructor
	 */
	public SchedulerServiceImpl() {
		setThreadNamePrefix("hapi-fhir-jpa-scheduler");
		setLocalSchedulingEnabled(true);
		setClusteredSchedulingEnabled(true);
	}

	public boolean isLocalSchedulingEnabled() {
		return myLocalSchedulingEnabled;
	}

	public void setLocalSchedulingEnabled(boolean theLocalSchedulingEnabled) {
		myLocalSchedulingEnabled = theLocalSchedulingEnabled;
	}

	public boolean isClusteredSchedulingEnabled() {
		return myClusteredSchedulingEnabled;
	}

	public void setClusteredSchedulingEnabled(boolean theClusteredSchedulingEnabled) {
		myClusteredSchedulingEnabled = theClusteredSchedulingEnabled;
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
		myStopping.set(false);
	}

	/**
	 * We defer startup of executing started tasks until we're sure we're ready for it
	 * and the startup is completely done
	 */
	@EventListener
	public void contextStarted(ContextRefreshedEvent theEvent) throws SchedulerException {
		try {
			ourLog.info("Starting task schedulers for context {}", theEvent != null ? theEvent.getApplicationContext().getId() : "null");
			if (myLocalScheduler != null) {
				myLocalScheduler.start();
			}
			if (myClusteredScheduler != null) {
				myClusteredScheduler.start();
			}
		} catch (Exception e) {
			ourLog.error("Failed to start context", e);
			throw new SchedulerException(e);
		}
	}

	private Scheduler createLocalScheduler() throws SchedulerException {
		if (!isLocalSchedulingEnabled() || isSchedulingDisabledForUnitTests()) {
			return new NullScheduler();
		}
		Properties localProperties = new Properties();
		localProperties.setProperty(PROP_SCHED_INSTANCE_NAME, "local-" + ourNextSchedulerId++);
		quartzPropertiesCommon(localProperties);
		quartzPropertiesLocal(localProperties);
		StdSchedulerFactory factory = new StdSchedulerFactory();
		factory.initialize(localProperties);
		Scheduler scheduler = factory.getScheduler();
		configureSchedulerCommon(scheduler);
		scheduler.standby();
		return scheduler;
	}

	private Scheduler createClusteredScheduler() throws SchedulerException {
		if (!isClusteredSchedulingEnabled() || isSchedulingDisabledForUnitTests()) {
			return new NullScheduler();
		}
		Properties clusteredProperties = new Properties();
		clusteredProperties.setProperty(PROP_SCHED_INSTANCE_NAME, "clustered-" + ourNextSchedulerId++);
		quartzPropertiesCommon(clusteredProperties);
		quartzPropertiesClustered(clusteredProperties);
		StdSchedulerFactory factory = new StdSchedulerFactory();
		factory.initialize(clusteredProperties);
		Scheduler scheduler = factory.getScheduler();
		configureSchedulerCommon(scheduler);
		scheduler.standby();
		return scheduler;
	}

	private void configureSchedulerCommon(Scheduler theScheduler) throws SchedulerException {
		theScheduler.setJobFactory(mySpringBeanJobFactory);
	}

	@PreDestroy
	public void stop() throws SchedulerException {
		ourLog.info("Shutting down task scheduler...");

		myStopping.set(true);
		myLocalScheduler.shutdown(true);
		myClusteredScheduler.shutdown(true);
	}

	@Override
	public void purgeAllScheduledJobsForUnitTest() throws SchedulerException {
		myLocalScheduler.clear();
		myClusteredScheduler.clear();
	}

	@Override
	public void logStatus() {
		try {
			Set<JobKey> keys = myLocalScheduler.getJobKeys(GroupMatcher.anyGroup());
			String keysString = keys.stream().map(t -> t.getName()).collect(Collectors.joining(", "));
			ourLog.info("Local scheduler has jobs: {}", keysString);

			keys = myClusteredScheduler.getJobKeys(GroupMatcher.anyGroup());
			keysString = keys.stream().map(t -> t.getName()).collect(Collectors.joining(", "));
			ourLog.info("Clustered scheduler has jobs: {}", keysString);
		} catch (SchedulerException e) {
			throw new InternalErrorException(e);
		}
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

	@Override
	public boolean isStopping() {
		return myStopping.get();
	}

	/**
	 * Properties for the local scheduler (see the class docs to learn what this means)
	 */
	protected void quartzPropertiesLocal(Properties theProperties) {
		// nothing
	}

	/**
	 * Properties for the cluster scheduler (see the class docs to learn what this means)
	 */
	protected void quartzPropertiesClustered(Properties theProperties) {
//		theProperties.put("org.quartz.jobStore.tablePrefix", "QRTZHFJC_");
	}

	protected void quartzPropertiesCommon(Properties theProperties) {
		theProperties.put("org.quartz.threadPool.threadCount", "4");
		theProperties.put("org.quartz.threadPool.threadNamePrefix", getThreadNamePrefix() + "-" + theProperties.get(PROP_SCHED_INSTANCE_NAME));
	}

	private boolean isSchedulingDisabledForUnitTests() {
		String schedulingDisabled = myEnvironment.getProperty(SCHEDULING_DISABLED);
		return "true".equals(schedulingDisabled);
	}

	private static class NonConcurrentJobDetailImpl extends JobDetailImpl {
		private static final long serialVersionUID = 5716197221121989740L;

		// All HAPI FHIR jobs shouldn't allow concurrent execution
		@Override
		public boolean isConcurrentExectionDisallowed() {
			return true;
		}
	}

	private static class NullScheduler implements Scheduler {
		@Override
		public String getSchedulerName() {
			return null;
		}

		@Override
		public String getSchedulerInstanceId() {
			return null;
		}

		@Override
		public SchedulerContext getContext() {
			return null;
		}

		@Override
		public void start() {

		}

		@Override
		public void startDelayed(int seconds) {

		}

		@Override
		public boolean isStarted() {
			return false;
		}

		@Override
		public void standby() {

		}

		@Override
		public boolean isInStandbyMode() {
			return false;
		}

		@Override
		public void shutdown() {

		}

		@Override
		public void shutdown(boolean waitForJobsToComplete) {

		}

		@Override
		public boolean isShutdown() {
			return false;
		}

		@Override
		public SchedulerMetaData getMetaData() {
			return null;
		}

		@Override
		public List<JobExecutionContext> getCurrentlyExecutingJobs() {
			return null;
		}

		@Override
		public void setJobFactory(JobFactory factory) {

		}

		@Override
		public ListenerManager getListenerManager() {
			return null;
		}

		@Override
		public Date scheduleJob(JobDetail jobDetail, Trigger trigger) {
			return null;
		}

		@Override
		public Date scheduleJob(Trigger trigger) {
			return null;
		}

		@Override
		public void scheduleJobs(Map<JobDetail, Set<? extends Trigger>> triggersAndJobs, boolean replace) {

		}

		@Override
		public void scheduleJob(JobDetail jobDetail, Set<? extends Trigger> triggersForJob, boolean replace) {

		}

		@Override
		public boolean unscheduleJob(TriggerKey triggerKey) {
			return false;
		}

		@Override
		public boolean unscheduleJobs(List<TriggerKey> triggerKeys) {
			return false;
		}

		@Override
		public Date rescheduleJob(TriggerKey triggerKey, Trigger newTrigger) {
			return null;
		}

		@Override
		public void addJob(JobDetail jobDetail, boolean replace) {

		}

		@Override
		public void addJob(JobDetail jobDetail, boolean replace, boolean storeNonDurableWhileAwaitingScheduling) {

		}

		@Override
		public boolean deleteJob(JobKey jobKey) {
			return false;
		}

		@Override
		public boolean deleteJobs(List<JobKey> jobKeys) {
			return false;
		}

		@Override
		public void triggerJob(JobKey jobKey) {

		}

		@Override
		public void triggerJob(JobKey jobKey, JobDataMap data) {

		}

		@Override
		public void pauseJob(JobKey jobKey) {

		}

		@Override
		public void pauseJobs(GroupMatcher<JobKey> matcher) {

		}

		@Override
		public void pauseTrigger(TriggerKey triggerKey) {

		}

		@Override
		public void pauseTriggers(GroupMatcher<TriggerKey> matcher) {

		}

		@Override
		public void resumeJob(JobKey jobKey) {

		}

		@Override
		public void resumeJobs(GroupMatcher<JobKey> matcher) {

		}

		@Override
		public void resumeTrigger(TriggerKey triggerKey) {

		}

		@Override
		public void resumeTriggers(GroupMatcher<TriggerKey> matcher) {

		}

		@Override
		public void pauseAll() {

		}

		@Override
		public void resumeAll() {

		}

		@Override
		public List<String> getJobGroupNames() {
			return null;
		}

		@Override
		public Set<JobKey> getJobKeys(GroupMatcher<JobKey> matcher) {
			return null;
		}

		@Override
		public List<? extends Trigger> getTriggersOfJob(JobKey jobKey) {
			return null;
		}

		@Override
		public List<String> getTriggerGroupNames() {
			return null;
		}

		@Override
		public Set<TriggerKey> getTriggerKeys(GroupMatcher<TriggerKey> matcher) {
			return null;
		}

		@Override
		public Set<String> getPausedTriggerGroups() {
			return null;
		}

		@Override
		public JobDetail getJobDetail(JobKey jobKey) {
			return null;
		}

		@Override
		public Trigger getTrigger(TriggerKey triggerKey) {
			return null;
		}

		@Override
		public Trigger.TriggerState getTriggerState(TriggerKey triggerKey) {
			return null;
		}

		@Override
		public void resetTriggerFromErrorState(TriggerKey triggerKey) {

		}

		@Override
		public void addCalendar(String calName, Calendar calendar, boolean replace, boolean updateTriggers) {

		}

		@Override
		public boolean deleteCalendar(String calName) {
			return false;
		}

		@Override
		public Calendar getCalendar(String calName) {
			return null;
		}

		@Override
		public List<String> getCalendarNames() {
			return null;
		}

		@Override
		public boolean interrupt(JobKey jobKey) throws UnableToInterruptJobException {
			return false;
		}

		@Override
		public boolean interrupt(String fireInstanceId) throws UnableToInterruptJobException {
			return false;
		}

		@Override
		public boolean checkExists(JobKey jobKey) {
			return false;
		}

		@Override
		public boolean checkExists(TriggerKey triggerKey) {
			return false;
		}

		@Override
		public void clear() {

		}
	}


}
