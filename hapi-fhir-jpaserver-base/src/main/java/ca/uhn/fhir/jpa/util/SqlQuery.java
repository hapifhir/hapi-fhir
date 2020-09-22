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

import ca.uhn.fhir.util.UrlUtil;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.trim;

public class SqlQuery {
	private final String myThreadName = Thread.currentThread().getName();
	private final String mySql;
	private final List<String> myParams;
	private final long myQueryTimestamp;
	private final long myElapsedTime;
	private final StackTraceElement[] myStackTrace;
	private final int mySize;

	SqlQuery(String theSql, List<String> theParams, long theQueryTimestamp, long theElapsedTime, StackTraceElement[] theStackTraceElements, int theSize) {
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
		return getSql(theInlineParams, theFormat, false);
	}

	public String getSql(boolean theInlineParams, boolean theFormat, boolean theSanitizeParams) {
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
				String nextParamValue = nextParams.remove(0);
				if (theSanitizeParams) {
					nextParamValue = UrlUtil.sanitizeUrlPart(nextParamValue);
				}
				String nextSubstitution = "'" + nextParamValue + "'";
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
