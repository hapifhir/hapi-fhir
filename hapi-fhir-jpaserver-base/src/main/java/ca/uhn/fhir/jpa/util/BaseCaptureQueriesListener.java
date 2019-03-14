package ca.uhn.fhir.jpa.util;

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

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.proxy.ParameterSetOperation;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public abstract class BaseCaptureQueriesListener implements ProxyDataSourceBuilder.SingleQueryExecution {

	@Override
	public void execute(ExecutionInfo theExecutionInfo, List<QueryInfo> theQueryInfoList) {
		final Queue<Query> queryList = provideQueryList();
		for (QueryInfo next : theQueryInfoList) {
			String sql = StringUtils.trim(next.getQuery());
			List<String> params;
			if (next.getParametersList().size() > 0 && next.getParametersList().get(0).size() > 0) {
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

			long elapsedTime = theExecutionInfo.getElapsedTime();
			long startTime = System.currentTimeMillis() - elapsedTime;
			queryList.add(new Query(sql, params, startTime, elapsedTime));
		}
	}

	protected abstract Queue<Query> provideQueryList();

	public static class Query {
		private final String myThreadName = Thread.currentThread().getName();
		private final String mySql;
		private final List<String> myParams;
		private final long myQueryTimestamp;
		private final long myElapsedTime;

		Query(String theSql, List<String> theParams, long theQueryTimestamp, long theElapsedTime) {
			mySql = theSql;
			myParams = Collections.unmodifiableList(theParams);
			myQueryTimestamp = theQueryTimestamp;
			myElapsedTime = theElapsedTime;
		}

		public long getQueryTimestamp() {
			return myQueryTimestamp;
		}

		public long getElapsedTime() {
			return myElapsedTime;
		}

		public String getThreadName() {
			return myThreadName;
		}

		public String getSql(boolean theInlineParams, boolean theFormat) {
			String retVal = mySql;
			if (theFormat) {
				retVal = new BasicFormatterImpl().format(retVal);

				// BasicFormatterImpl annoyingly adds a newline at the very start of its output
				while (retVal.startsWith("\n")) {
					retVal = retVal.substring(1);
				}
			}

			if (theInlineParams) {
				List<String> nextParams = new ArrayList<>(myParams);
				while (retVal.contains("?") && nextParams.size() > 0) {
					int idx = retVal.indexOf("?");
					retVal = retVal.substring(0, idx) + "'" + nextParams.remove(0) + "'" + retVal.substring(idx + 1);
				}
			}

			return retVal;

		}

	}

}
