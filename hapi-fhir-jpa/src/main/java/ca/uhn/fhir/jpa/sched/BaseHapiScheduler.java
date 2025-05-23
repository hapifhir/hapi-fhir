/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.sched.IHapiScheduler;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.system.HapiSystemProperties;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseHapiScheduler implements IHapiScheduler {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseHapiScheduler.class);

	private final String myThreadNamePrefix;
	private final AutowiringSpringBeanJobFactory mySpringBeanJobFactory;
	private final SchedulerFactoryBean myFactory = new SchedulerFactoryBean();
	private final Properties myProperties = new Properties();

	private Scheduler myScheduler;
	private String myInstanceName;

	public BaseHapiScheduler(String theThreadNamePrefix, AutowiringSpringBeanJobFactory theSpringBeanJobFactory) {
		myThreadNamePrefix = theThreadNamePrefix;
		mySpringBeanJobFactory = theSpringBeanJobFactory;
	}

	void setInstanceName(String theInstanceName) {
		myInstanceName = theInstanceName;
	}

	@Override
	public void init() throws SchedulerException {

		setProperties();
		myFactory.setQuartzProperties(myProperties);
		myFactory.setBeanName(myInstanceName);
		myFactory.setSchedulerName(myThreadNamePrefix);
		myFactory.setJobFactory(mySpringBeanJobFactory);
		massageJobFactory(myFactory);

		try {
			Validate.notBlank(myInstanceName, "No instance name supplied");
			myFactory.afterPropertiesSet();
		} catch (Exception e) {
			throw new SchedulerException(Msg.code(1633) + e);
		}

		myScheduler = myFactory.getScheduler();
		myScheduler.standby();
	}

	protected void massageJobFactory(SchedulerFactoryBean theFactory) {
		// nothing by default
	}

	protected void setProperties() {
		addProperty("org.quartz.threadPool.threadCount", "4");
		// Note that we use a common name, with no suffixed ID for the name, as per the quartz docs:
		// https://www.quartz-scheduler.org/documentation/quartz-2.1.7/configuration/ConfigMain.html
		if (myInstanceName != null) {
			myProperties.setProperty(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME, myInstanceName);
		}

		// By Default, the scheduler ID is not set, which will cause quartz to set it to the string NON_CLUSTERED. Here
		// we are setting it explicitly as an indication to implementers that if they want a different ID, they should
		// set it using this below property.
		addProperty(StdSchedulerFactory.PROP_SCHED_INSTANCE_ID, StdSchedulerFactory.DEFAULT_INSTANCE_ID);

		addProperty("org.quartz.threadPool.threadNamePrefix", getThreadPrefix());
	}

	@Nonnull
	private String getThreadPrefix() {
		return myThreadNamePrefix + "-" + myInstanceName;
	}

	protected void addProperty(String key, String value) {
		myProperties.put(key, value);
	}

	@Override
	public void start() {
		if (myScheduler == null) {
			throw new ConfigurationException(Msg.code(1634) + "Attempt to start uninitialized scheduler");
		}
		try {
			ourLog.info("Starting scheduler {}", getThreadPrefix());
			myScheduler.start();
		} catch (SchedulerException e) {
			ourLog.error("Failed to start up scheduler", e);
			throw new ConfigurationException(Msg.code(1635) + "Failed to start up scheduler", e);
		}
	}

	@Override
	public void shutdown() {
		if (myScheduler == null) {
			return;
		}
		try {
			if (HapiSystemProperties.isUnitTestModeEnabled()) {
				myScheduler.shutdown(false);
			} else {
				myScheduler.shutdown(true);
			}
		} catch (SchedulerException e) {
			ourLog.error("Failed to shut down scheduler", e);
			throw new ConfigurationException(Msg.code(1636) + "Failed to shut down scheduler", e);
		}
	}

	@Override
	public boolean isStarted() {
		try {
			return myScheduler != null && myScheduler.isStarted();
		} catch (SchedulerException e) {
			ourLog.error("Failed to determine scheduler status", e);
			return false;
		}
	}

	public void pause() {
		int delay = 100;
		String errorMsg = null;
		Throwable ex = null;
		try {
			int count = 0;
			myScheduler.standby();
			while (count < 3) {
				if (!hasRunningJobs()) {
					break;
				}
				Thread.sleep(delay);
				count++;
			}
			if (count >= 3) {
				errorMsg = "Scheduler on standby. But after  " + (count + 1) * delay
						+ " ms there are still jobs running. Execution will continue, but may cause bugs.";
			}
		} catch (Exception x) {
			ex = x;
			errorMsg = "Failed to set to standby. Execution will continue, but may cause bugs.";
		}

		if (isNotBlank(errorMsg)) {
			if (ex != null) {
				ourLog.warn(errorMsg, ex);
			} else {
				ourLog.warn(errorMsg);
			}
		}
	}

	public void unpause() {
		start();
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
			throw new InternalErrorException(Msg.code(1637) + "Failed to get log status for scheduler", e);
		}
	}

	private boolean hasRunningJobs() {
		try {
			List<JobExecutionContext> currentlyExecutingJobs = myScheduler.getCurrentlyExecutingJobs();
			ourLog.info("Checking for running jobs. Found {} running.", currentlyExecutingJobs);
			return !currentlyExecutingJobs.isEmpty();
		} catch (SchedulerException ex) {
			throw new RuntimeException(Msg.code(2521) + " Failed during  check for scheduled jobs", ex);
		}
	}

	@Override
	public void scheduleJob(long theIntervalMillis, ScheduledJobDefinition theJobDefinition) {
		Validate.isTrue(theIntervalMillis >= 100);

		Validate.notNull(theJobDefinition);
		Validate.notNull(theJobDefinition.getJobClass());
		Validate.notBlank(theJobDefinition.getId());

		TriggerKey triggerKey = theJobDefinition.toTriggerKey();
		JobDetailImpl jobDetail = buildJobDetail(theJobDefinition);

		ScheduleBuilder<? extends Trigger> schedule = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInMilliseconds(theIntervalMillis)
				.withMisfireHandlingInstructionIgnoreMisfires() // We ignore misfires in cases of multiple JVMs each
				// trying to fire.
				.repeatForever();

		Trigger trigger = TriggerBuilder.newTrigger()
				.forJob(jobDetail)
				.withIdentity(triggerKey)
				.startNow()
				.withSchedule(schedule)
				.build();

		Set<? extends Trigger> triggers = Sets.newHashSet(trigger);
		try {
			myScheduler.scheduleJob(jobDetail, triggers, true);
		} catch (SchedulerException e) {
			ourLog.error("Failed to schedule job", e);
			throw new InternalErrorException(Msg.code(1638) + e);
		}
	}

	@Nonnull
	private JobDetailImpl buildJobDetail(ScheduledJobDefinition theJobDefinition) {
		JobDetailImpl jobDetail = new NonConcurrentJobDetailImpl();
		jobDetail.setJobClass(theJobDefinition.getJobClass());
		jobDetail.setKey(theJobDefinition.toJobKey());
		jobDetail.setJobDataMap(new JobDataMap(theJobDefinition.getJobData()));
		return jobDetail;
	}

	@VisibleForTesting
	@Override
	public Set<JobKey> getJobKeysForUnitTest() throws SchedulerException {
		return myScheduler.getJobKeys(GroupMatcher.anyGroup());
	}

	private static class NonConcurrentJobDetailImpl extends JobDetailImpl {
		private static final long serialVersionUID = 5716197221121989740L;

		// All HAPI FHIR jobs shouldn't allow concurrent execution
		@Override
		public boolean isConcurrentExectionDisallowed() {
			return true;
		}
	}

	@Override
	public void triggerJobImmediately(ScheduledJobDefinition theJobDefinition) {
		try {
			myScheduler.triggerJob(theJobDefinition.toJobKey());
		} catch (SchedulerException e) {
			ourLog.error("Error triggering scheduled job with key {}", theJobDefinition);
		}
	}

	/**
	 * Retrieves a clone of the properties required for unit testing.
	 *
	 * @return The properties for unit testing.
	 */
	@VisibleForTesting
	protected Properties getPropertiesForUnitTest() {
		return myProperties;
	}
}
