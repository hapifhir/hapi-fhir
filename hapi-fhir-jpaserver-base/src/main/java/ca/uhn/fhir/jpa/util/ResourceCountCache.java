package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

public class ResourceCountCache {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceCountCache.class);
	private static Long ourNowForUnitTest;
	private final Callable<Map<String, Long>> myFetcher;
	private volatile long myCacheMillis;
	private AtomicReference<Map<String, Long>> myCapabilityStatement = new AtomicReference<>();
	private long myLastFetched;
	@Autowired
	private ISchedulerService mySchedulerService;

	/**
	 * Constructor
	 */
	public ResourceCountCache(Callable<Map<String, Long>> theFetcher) {
		myFetcher = theFetcher;
	}

	public synchronized void clear() {
		ourLog.info("Clearing cache");
		myCapabilityStatement.set(null);
		myLastFetched = 0;
	}

	public synchronized Map<String, Long> get() {
		return myCapabilityStatement.get();
	}

	private Map<String, Long> refresh() {
		Map<String, Long> retVal;
		try {
			retVal = myFetcher.call();
		} catch (Exception e) {
			throw new InternalErrorException(Msg.code(799) + e);
		}

		myCapabilityStatement.set(retVal);
		myLastFetched = now();
		return retVal;
	}

	public void setCacheMillis(long theCacheMillis) {
		myCacheMillis = theCacheMillis;
	}

	public void update() {
		if (myCacheMillis > 0) {
			long now = now();
			long expiry = now - myCacheMillis;
			if (myLastFetched < expiry) {
				refresh();
			}
		}
	}

	@PostConstruct
	public void scheduleJob() {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleLocalJob(10 * DateUtils.MILLIS_PER_MINUTE, jobDetail);
	}

	public static class Job implements HapiJob {
		@Autowired
		private ResourceCountCache myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.update();
		}
	}

	private static long now() {
		if (ourNowForUnitTest != null) {
			return ourNowForUnitTest;
		}
		return System.currentTimeMillis();
	}

	@VisibleForTesting
	static void setNowForUnitTest(Long theNowForUnitTest) {
		ourNowForUnitTest = theNowForUnitTest;
	}


}
