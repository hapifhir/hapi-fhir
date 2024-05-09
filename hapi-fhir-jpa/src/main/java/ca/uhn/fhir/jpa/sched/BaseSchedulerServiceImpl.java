/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

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
public abstract class BaseSchedulerServiceImpl implements ISchedulerService {
	public static final String SCHEDULING_DISABLED = "scheduling_disabled";
	public static final String SCHEDULING_DISABLED_EQUALS_TRUE = SCHEDULING_DISABLED + "=true";

	private static final Logger ourLog = LoggerFactory.getLogger(BaseSchedulerServiceImpl.class);
	private IHapiScheduler myLocalScheduler;
	private IHapiScheduler myClusteredScheduler;
	private boolean myLocalSchedulingEnabled;
	private boolean myClusteredSchedulingEnabled;
	private AtomicBoolean myStopping = new AtomicBoolean(false);
	private String myDefaultGroup;

	@Autowired
	private Environment myEnvironment;

	@Autowired
	private ApplicationContext myApplicationContext;

	@Autowired
	protected AutowiringSpringBeanJobFactory mySchedulerJobFactory;

	public BaseSchedulerServiceImpl() {
		setLocalSchedulingEnabled(true);
		setClusteredSchedulingEnabled(true);
	}

	public BaseSchedulerServiceImpl setDefaultGroup(String theDefaultGroup) {
		myDefaultGroup = theDefaultGroup;
		return this;
	}

	public boolean isLocalSchedulingEnabled() {
		return myLocalSchedulingEnabled;
	}

	public void setLocalSchedulingEnabled(boolean theLocalSchedulingEnabled) {
		myLocalSchedulingEnabled = theLocalSchedulingEnabled;
	}

	@Override
	public boolean isClusteredSchedulingEnabled() {
		return myClusteredSchedulingEnabled;
	}

	public void setClusteredSchedulingEnabled(boolean theClusteredSchedulingEnabled) {
		myClusteredSchedulingEnabled = theClusteredSchedulingEnabled;
	}

	@PostConstruct
	public void create() throws SchedulerException {
		myLocalScheduler = createScheduler(false);
		myClusteredScheduler = createScheduler(true);
		if (isSchedulingDisabled()) {
			setLocalSchedulingEnabled(false);
			setClusteredSchedulingEnabled(false);
		}
		myStopping.set(false);
	}

	private IHapiScheduler createScheduler(boolean theClustered) throws SchedulerException {
		if (isSchedulingDisabled()) {
			ourLog.info("Scheduling is disabled on this server");
			return new HapiNullScheduler();
		}
		IHapiScheduler retval;
		if (theClustered) {
			ourLog.info("Creating Clustered Scheduler");
			retval = getClusteredScheduler();
		} else {
			ourLog.info("Creating Local Scheduler");
			retval = getLocalHapiScheduler();
		}
		retval.init();
		return retval;
	}

	public boolean isSchedulingDisabled() {
		return !isLocalSchedulingEnabled() || isSchedulingDisabledForUnitTests();
	}

	protected abstract IHapiScheduler getLocalHapiScheduler();

	protected abstract IHapiScheduler getClusteredScheduler();

	@EventListener(ContextRefreshedEvent.class)
	public void start() {

		// Jobs are scheduled first to avoid a race condition that occurs if jobs are scheduled
		// after the scheduler starts for the first time. This race condition results in duplicate
		// TRIGGER_ACCESS entries being added to the QRTZ_LOCKS table.
		// Note - Scheduling jobs before the scheduler has started is supported by Quartz
		// http://www.quartz-scheduler.org/documentation/quartz-2.3.0/cookbook/CreateScheduler.html
		scheduleJobs();

		myStopping.set(false);

		try {
			ourLog.info("Starting task schedulers for context {}", myApplicationContext.getId());
			if (myLocalScheduler != null) {
				myLocalScheduler.start();
			}
			if (myClusteredScheduler != null) {
				myClusteredScheduler.start();
			}
		} catch (Exception e) {
			ourLog.error("Failed to start scheduler", e);
			throw new ConfigurationException(Msg.code(1632) + "Failed to start scheduler", e);
		}
	}

	private void scheduleJobs() {
		Collection<IHasScheduledJobs> values =
				myApplicationContext.getBeansOfType(IHasScheduledJobs.class).values();
		ourLog.info("Scheduling {} jobs in {}", values.size(), myApplicationContext.getId());
		values.forEach(t -> t.scheduleJobs(this));
	}

	@PreDestroy
	public void stop() {
		ourLog.info("Shutting down task scheduler...");

		myStopping.set(true);
		myLocalScheduler.shutdown();
		myClusteredScheduler.shutdown();
	}

	@Override
	public void purgeAllScheduledJobsForUnitTest() throws SchedulerException {
		myLocalScheduler.clear();
		myClusteredScheduler.clear();
	}

	@Override
	public void logStatusForUnitTest() {
		myLocalScheduler.logStatusForUnitTest();
		myClusteredScheduler.logStatusForUnitTest();
	}

	@Override
	public void pause() {
		myLocalScheduler.pause();
		myClusteredScheduler.pause();
	}

	@Override
	public void unpause() {
		myLocalScheduler.unpause();
		myClusteredScheduler.unpause();
	}

	@Override
	public void scheduleLocalJob(long theIntervalMillis, ScheduledJobDefinition theJobDefinition) {
		scheduleJob("local", myLocalScheduler, theIntervalMillis, theJobDefinition);
	}

	@Override
	public void scheduleClusteredJob(long theIntervalMillis, ScheduledJobDefinition theJobDefinition) {
		scheduleJob("clustered", myClusteredScheduler, theIntervalMillis, theJobDefinition);
	}

	private void scheduleJob(
			String theInstanceName,
			IHapiScheduler theScheduler,
			long theIntervalMillis,
			ScheduledJobDefinition theJobDefinition) {
		if (isSchedulingDisabled()) {
			return;
		}

		assert theJobDefinition.getId() != null;
		assert theJobDefinition.getJobClass() != null;

		ourLog.info(
				"Scheduling {} job {} with interval {}",
				theInstanceName,
				theJobDefinition.getId(),
				StopWatch.formatMillis(theIntervalMillis));
		defaultGroup(theJobDefinition);
		theScheduler.scheduleJob(theIntervalMillis, theJobDefinition);
	}

	@VisibleForTesting
	@Override
	public Set<JobKey> getLocalJobKeysForUnitTest() throws SchedulerException {
		return myLocalScheduler.getJobKeysForUnitTest();
	}

	@VisibleForTesting
	@Override
	public Set<JobKey> getClusteredJobKeysForUnitTest() throws SchedulerException {
		return myClusteredScheduler.getJobKeysForUnitTest();
	}

	private boolean isSchedulingDisabledForUnitTests() {
		String schedulingDisabled = myEnvironment.getProperty(SCHEDULING_DISABLED);
		return "true".equals(schedulingDisabled);
	}

	@Override
	public boolean isStopping() {
		return myStopping.get();
	}

	@Override
	public void triggerClusteredJobImmediately(ScheduledJobDefinition theJobDefinition) {
		defaultGroup(theJobDefinition);
		myClusteredScheduler.triggerJobImmediately(theJobDefinition);
	}

	@Override
	public void triggerLocalJobImmediately(ScheduledJobDefinition theJobDefinition) {
		defaultGroup(theJobDefinition);
		myLocalScheduler.triggerJobImmediately(theJobDefinition);
	}

	private void defaultGroup(ScheduledJobDefinition theJobDefinition) {
		if (theJobDefinition.getGroup() == null) {
			theJobDefinition.setGroup(myDefaultGroup);
		}
	}
}
