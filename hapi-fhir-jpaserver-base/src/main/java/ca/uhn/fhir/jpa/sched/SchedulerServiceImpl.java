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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.ISmartLifecyclePhase;
import ca.uhn.fhir.rest.server.sched.IHapiScheduler;
import ca.uhn.fhir.rest.server.sched.ISchedulerFactory;
import ca.uhn.fhir.rest.server.sched.ISchedulerService;
import ca.uhn.fhir.rest.server.sched.ScheduledJobDefinition;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
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
public class SchedulerServiceImpl implements ISchedulerService, SmartLifecycle {
	public static final String SCHEDULING_DISABLED = "scheduling_disabled";
	public static final String SCHEDULING_DISABLED_EQUALS_TRUE = SCHEDULING_DISABLED + "=true";

	private static final Logger ourLog = LoggerFactory.getLogger(SchedulerServiceImpl.class);
	private IHapiScheduler myLocalScheduler;
	private IHapiScheduler myClusteredScheduler;
	private boolean myLocalSchedulingEnabled;
	private boolean myClusteredSchedulingEnabled;
	private AtomicBoolean myStopping = new AtomicBoolean(false);


	private final ISchedulerFactory mySchedulerFactory;
	@Autowired
	private Environment myEnvironment;
	@Autowired
	private ApplicationContext myApplicationContext;

	/**
	 * Constructor
	 * @param theSchedulerFactory
	 */
	public SchedulerServiceImpl(ISchedulerFactory theSchedulerFactory) {
		mySchedulerFactory = theSchedulerFactory;
		setLocalSchedulingEnabled(true);
		setClusteredSchedulingEnabled(true);
	}

	public ISchedulerFactory getSchedulerFactory() {
		return mySchedulerFactory;
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

	@PostConstruct
	public void create() throws SchedulerException {
		myLocalScheduler = createScheduler(false);
		myClusteredScheduler = createScheduler(true);
		myStopping.set(false);
	}

	private IHapiScheduler createScheduler(boolean theClustered) throws SchedulerException {
		if (!isLocalSchedulingEnabled() || isSchedulingDisabledForUnitTests()) {
			return new HapiNullScheduler();
		}
		IHapiScheduler retval;
		if (theClustered) {
			retval = mySchedulerFactory.newClusteredHapiScheduler();
		} else {
			retval = mySchedulerFactory.newLocalHapiScheduler();
		}
		retval.init();
		return retval;
	}

	/**
	 * We defer startup of executing started tasks until we're sure we're ready for it
	 * and the startup is completely done
	 */

	@Override
	public int getPhase() {
		return ISmartLifecyclePhase.SCHEDULER_1000;
	}

	@Override
	public void start() {
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
			throw new ConfigurationException("Failed to start scheduler", e);
		}
	}

	@Override
	public void stop() {
		ourLog.info("Shutting down task scheduler...");

		myStopping.set(true);
		myLocalScheduler.shutdown();
		myClusteredScheduler.shutdown();
	}

	@Override
	public boolean isRunning() {
		return !myStopping.get() && myLocalScheduler.isStarted() && myClusteredScheduler.isStarted();
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
	public void scheduleFixedDelayLocal(long theIntervalMillis, ScheduledJobDefinition theJobDefinition) {
		myLocalScheduler.scheduleFixedDelay(theIntervalMillis, theJobDefinition);
	}

	@Override
	public void scheduleFixedDelayClustered(long theIntervalMillis, ScheduledJobDefinition theJobDefinition) {
		myClusteredScheduler.scheduleFixedDelay(theIntervalMillis, theJobDefinition);
	}

	private boolean isSchedulingDisabledForUnitTests() {
		String schedulingDisabled = myEnvironment.getProperty(SCHEDULING_DISABLED);
		return "true".equals(schedulingDisabled);
	}

	@Override
	public boolean isStopping() {
		return myStopping.get();
	}

}
