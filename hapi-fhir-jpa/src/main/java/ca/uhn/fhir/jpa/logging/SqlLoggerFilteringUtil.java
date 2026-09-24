/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.logging;

import ca.uhn.fhir.i18n.Msg;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Utility for the Hibernate SQL log filtering feature
 */
public class SqlLoggerFilteringUtil {
	private static final Logger ourLog = LoggerFactory.getLogger(SqlLoggerFilteringUtil.class);

	@SuppressWarnings("FieldMayBeFinal") // so test can inject mocks
	public static int FILTER_UPDATE_INTERVAL_SECS = 5;

	public static final String FILTER_FILE_PATH = "hibernate-sql-log-filters.txt";
	private static final AtomicInteger ourRefreshCount = new AtomicInteger();

	/**
	 * All refresh executors ever started, tracked so that tests can deterministically shut every one of them
	 * down. This exists because each test works against a fresh (often Mockito-spied) instance, and a spied
	 * {@link UpdateFiltersTask} can keep running against a stubbed {@code refreshFilters}, leaking a scheduled
	 * executor that keeps incrementing {@link #ourRefreshCount} into subsequent tests. See
	 * {@link #shutdownRefreshExecutorsForTests()}.
	 */
	private static final Set<ScheduledThreadPoolExecutor> ourRefreshExecutorsForTests =
			Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

	private final Logger hibernateLogger = LoggerFactory.getLogger("org.hibernate.SQL");

	private final List<ISqlLoggerFilter> mySqlLoggerFilters;

	private CountDownLatch myRefreshDoneLatch;
	private volatile ScheduledThreadPoolExecutor myRefreshExecutor;

	private static final SqlLoggerFilteringUtil myInstance = new SqlLoggerFilteringUtil();
	// singleton
	public static SqlLoggerFilteringUtil getInstance() {
		return myInstance;
	}

	private SqlLoggerFilteringUtil() {
		ourRefreshCount.set(0);
		mySqlLoggerFilters = List.of(
				new SqlLoggerStartsWithFilter(), new SqlLoggerFragmentFilter(), new SqlLoggerStackTraceFilter());
	}

	public boolean allowLog(String theStatement) {
		// Invoked when org.hibernate.SQL logger is DEBUG enabled
		// Only initialize if method ever invoked, as it is usually not the case.
		if (myRefreshExecutor == null || myRefreshExecutor.isShutdown()) {
			startFilterRefreshExecutor();
		}

		boolean allowLog = isStatementAllowed(theStatement);
		ourLog.trace("SQL log {}: {}", allowLog ? "allowed" : "filtered out", theStatement);
		return allowLog;
	}

	private boolean isStatementAllowed(String theStatement) {
		return mySqlLoggerFilters.stream().noneMatch(f -> f.match(theStatement));
	}

	private synchronized void startFilterRefreshExecutor() {
		if (myRefreshExecutor != null && !myRefreshExecutor.isShutdown()) {
			ourLog.debug(
					"myRefreshExecutor terminated state: {}, terminating state: {}",
					myRefreshExecutor.isTerminated(),
					myRefreshExecutor.isTerminating());
			return;
		}

		myRefreshDoneLatch = new CountDownLatch(1);
		myRefreshExecutor = new ScheduledThreadPoolExecutor(1);
		ourRefreshExecutorsForTests.add(myRefreshExecutor);
		myRefreshExecutor.scheduleAtFixedRate(
				new UpdateFiltersTask(), 0, FILTER_UPDATE_INTERVAL_SECS, TimeUnit.SECONDS);
		ourLog.info("Starting SQL log filters refresh executor");

		// wait for first refresh cycle to complete
		try {
			// reset to use in case executor is restarted
			myRefreshDoneLatch.await();

		} catch (InterruptedException ignored) {
			ourLog.warn("Interrupted from sleep");
		}
	}

	private synchronized void stopFilterRefreshExecutor() {
		if (myRefreshExecutor == null || myRefreshExecutor.isShutdown()) {
			return;
		}
		ourLog.info("Stopping SQL log filters refresh executor");
		myRefreshExecutor.shutdown();
	}

	private class UpdateFiltersTask implements Runnable {
		@Override
		public void run() {
			ourLog.debug("\n\n\t\t\tRefreshing hibernate SQL filters!\n");

			try {
				refreshFilters(FILTER_FILE_PATH);

			} catch (Exception theE) {
				ourLog.error("Hibernate SQL log filters not refreshed. Exception: {} \n{}", theE, theE.getStackTrace());
				throw new RuntimeException(Msg.code(2778) + theE);
			} finally {
				myRefreshDoneLatch.countDown();
			}

			int count = ourRefreshCount.getAndIncrement();
			ourLog.debug("SQL logging filters {}. Refresh count: {}", count == 0 ? "initialized" : "refreshed", count);
		}
	}

	@VisibleForTesting
	public void refreshFilters(String theFilterFilePath) throws IOException {
		ourLog.debug("SQL log DEBUG enabled: {}", hibernateLogger.isDebugEnabled());
		if (!hibernateLogger.isDebugEnabled()) {
			// in case startFilterRefreshExecutor is waiting for refresh to finish
			myRefreshDoneLatch.countDown();
			stopFilterRefreshExecutor();
			return;
		}

		ourLog.debug("Starting filters refresh");
		List<String> filterDefinitionLines = readFilterDefinitionLines(new ClassPathResource(theFilterFilePath));

		for (ISqlLoggerFilter filter : mySqlLoggerFilters) {
			synchronized (filter.getLockingObject()) {
				filter.clearDefinitions();
				filterDefinitionLines.stream()
						.filter(l -> !l.startsWith("#"))
						.filter(filterDef -> StringUtils.isNotBlank(filterDef.trim()))
						.forEach(filterLine -> presentFilterDefinitionLineToFilters(filterLine, mySqlLoggerFilters));
			}
		}
		ourLog.debug("Ended filter refresh");
	}

	/**
	 * Reads the filter definition lines from a classpath resource. Uses a stream rather than
	 * {@link ClassPathResource#getFile()} so the resource can be read when it is packaged inside a JAR.
	 */
	@VisibleForTesting
	List<String> readFilterDefinitionLines(ClassPathResource theResource) throws IOException {
		try (BufferedReader reader =
				new BufferedReader(new InputStreamReader(theResource.getInputStream(), StandardCharsets.UTF_8))) {
			return reader.lines().collect(Collectors.toList());
		}
	}

	private void presentFilterDefinitionLineToFilters(String theFilterLine, List<ISqlLoggerFilter> theFilterList) {
		for (ISqlLoggerFilter filterRef : theFilterList) {
			if (filterRef.evaluateFilterLine(theFilterLine)) {
				// only one filter takes a filter line
				return;
			}
		}

		int spaceIdx = theFilterLine.indexOf(" ");
		ourLog.warn(
				"SQL log filtering line prefix not recognized: '{}'. Must be one of: '#', {}",
				theFilterLine.substring(0, spaceIdx == -1 ? theFilterLine.length() : spaceIdx),
				theFilterList.stream().map(ISqlLoggerFilter::getPrefix).collect(Collectors.joining("', '", "'", "'")));
	}

	@VisibleForTesting
	public static int getRefreshCountForTests() {
		return ourRefreshCount.get();
	}

	/**
	 * Shuts down every refresh executor that has ever been started and resets the refresh counter. Tests call
	 * this to isolate themselves from executors leaked by earlier tests (see {@link #ourRefreshExecutorsForTests}).
	 */
	@VisibleForTesting
	public static void shutdownRefreshExecutorsForTests() {
		synchronized (ourRefreshExecutorsForTests) {
			ourRefreshExecutorsForTests.forEach(ScheduledThreadPoolExecutor::shutdownNow);
			ourRefreshExecutorsForTests.clear();
		}
		ourRefreshCount.set(0);
	}

	@VisibleForTesting
	public static void setFilterUpdateIntervalSecs(int theFilterUpdateIntervalSecs) {
		FILTER_UPDATE_INTERVAL_SECS = theFilterUpdateIntervalSecs;
	}

	@VisibleForTesting
	public List<ISqlLoggerFilter> getSqlLoggerFilters() {
		return mySqlLoggerFilters;
	}
}
