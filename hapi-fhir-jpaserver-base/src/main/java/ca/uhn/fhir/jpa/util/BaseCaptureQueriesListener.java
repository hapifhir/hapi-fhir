package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.proxy.ParameterSetOperation;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.trim;

public abstract class BaseCaptureQueriesListener implements ProxyDataSourceBuilder.SingleQueryExecution {

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

		for (QueryInfo next : theQueryInfoList) {
			String sql = trim(next.getQuery());
			List<String> params;
			int size = 0;
			if (next.getParametersList().size() > 0 && next.getParametersList().get(0).size() > 0) {
				size = next.getParametersList().size();
				List<ParameterSetOperation> values = next
					.getParametersList()
					.get(0);
				params = values.stream()
					.map(t -> t.getArgs()[1])
					.map(t -> t != null ? t.toString() : "NULL")
					.collect(Collectors.toList());
			} else {
				params = Collections.emptyList();
			}

			StackTraceElement[] stackTraceElements = null;
			if (isCaptureQueryStackTrace()) {
				stackTraceElements = Thread.currentThread().getStackTrace();
			}

			long elapsedTime = theExecutionInfo.getElapsedTime();
			long startTime = System.currentTimeMillis() - elapsedTime;
			queryList.add(new SqlQuery(sql, params, startTime, elapsedTime, stackTraceElements, size));
		}
	}

	protected abstract Queue<SqlQuery> provideQueryList();

}
