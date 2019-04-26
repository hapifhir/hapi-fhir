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
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.trim;

public abstract class BaseCaptureQueriesListener implements ProxyDataSourceBuilder.SingleQueryExecution {

	private boolean myCaptureQueryStackTrace;

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
		final Queue<Query> queryList = provideQueryList();
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
			queryList.add(new Query(sql, params, startTime, elapsedTime, stackTraceElements, size));
		}
	}

	protected abstract Queue<Query> provideQueryList();

	public static class Query {
		private final String myThreadName = Thread.currentThread().getName();
		private final String mySql;
		private final List<String> myParams;
		private final long myQueryTimestamp;
		private final long myElapsedTime;
		private final StackTraceElement[] myStackTrace;
		private final int mySize;

		Query(String theSql, List<String> theParams, long theQueryTimestamp, long theElapsedTime, StackTraceElement[] theStackTraceElements, int theSize) {
			mySql = theSql;
			myParams = Collections.unmodifiableList(theParams);
			myQueryTimestamp = theQueryTimestamp;
			myElapsedTime = theElapsedTime;
			myStackTrace = theStackTraceElements;
			mySize = theSize;
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
				int idx = 0;
				while (nextParams.size() > 0) {
					idx = retVal.indexOf("?", idx);
					if (idx == -1) {
						break;
					}
					String nextSubstitution = "'" + nextParams.remove(0) + "'";
					retVal = retVal.substring(0, idx) + nextSubstitution + retVal.substring(idx + 1);
					idx += nextSubstitution.length();
				}
			}

			if (mySize > 1) {
				retVal += "\nsize: " + mySize + "\n";
			}
			return trim(retVal);

		}

		public StackTraceElement[] getStackTrace() {
			return myStackTrace;
		}

		public int getSize() {
			return mySize;
		}
	}

}
