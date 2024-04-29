/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import jakarta.annotation.Nullable;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.MethodExecutionContext;
import net.ttddyy.dsproxy.proxy.ParameterSetOperation;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.trim;

public abstract class BaseCaptureQueriesListener
		implements ProxyDataSourceBuilder.SingleQueryExecution, ProxyDataSourceBuilder.SingleMethodExecution {

	private boolean myCaptureQueryStackTrace = false;

	/**
	 * This has an impact on performance! Use with caution.
	 */
	public boolean isCaptureQueryStackTrace() {
		return myCaptureQueryStackTrace;
	}

	/**
	 * This has an impact on performance! Use with caution.
	 */
	public void setCaptureQueryStackTrace(boolean theCaptureQueryStackTrace) {
		myCaptureQueryStackTrace = theCaptureQueryStackTrace;
	}

	@Override
	public void execute(ExecutionInfo theExecutionInfo, List<QueryInfo> theQueryInfoList) {
		final Queue<SqlQuery> queryList = provideQueryList();
		if (queryList == null) {
			return;
		}

		RequestPartitionId requestPartitionId = HapiTransactionService.getRequestPartitionAssociatedWithThread();

		/*
		 * Note on how this works:
		 *
		 * Hibernate batches queries where the SQL is identical other than parameter values.
		 *
		 * So for example, if we call "select PID from SOMETABLE where NAME = ?" twice with
		 * two different name values, this method will be called only once with 2 parameter
		 * lists. In this case, we say size=2 and we capture the first parameters in case
		 * we want to log them. We don't capture subsequent parameters currently because
		 * this is only used for troubleshooting logging anyhow so the values are used
		 * only as a representative example only.
		 */

		for (QueryInfo next : theQueryInfoList) {
			String sql = trim(next.getQuery());
			List<String> params;
			int size;
			if (next.getParametersList().size() > 0
					&& next.getParametersList().get(0).size() > 0) {
				size = next.getParametersList().size();
				List<ParameterSetOperation> values = next.getParametersList().get(0);
				params = values.stream()
						.map(t -> t.getArgs()[1])
						.map(t -> t != null ? t.toString() : "NULL")
						.collect(Collectors.toList());
			} else {
				params = Collections.emptyList();
				size = next.getParametersList().size();
			}

			StackTraceElement[] stackTraceElements = null;
			if (isCaptureQueryStackTrace()) {
				stackTraceElements = Thread.currentThread().getStackTrace();
			}

			long elapsedTime = theExecutionInfo.getElapsedTime();
			long startTime = System.currentTimeMillis() - elapsedTime;
			SqlQuery sqlQuery =
					new SqlQuery(sql, params, startTime, elapsedTime, stackTraceElements, size, requestPartitionId);
			queryList.add(sqlQuery);
		}
	}

	protected abstract Queue<SqlQuery> provideQueryList();

	@Nullable
	protected abstract AtomicInteger provideCommitCounter();

	@Nullable
	protected abstract AtomicInteger provideRollbackCounter();

	@Override
	public void execute(MethodExecutionContext executionContext) {
		AtomicInteger counter = null;
		switch (executionContext.getMethod().getName()) {
			case "commit":
				counter = provideCommitCounter();
				break;
			case "rollback":
				counter = provideRollbackCounter();
				break;
		}

		if (counter != null) {
			counter.incrementAndGet();
		}
	}

	public int countCommits() {
		return provideCommitCounter().get();
	}

	public int countRollbacks() {
		return provideRollbackCounter().get();
	}
}
