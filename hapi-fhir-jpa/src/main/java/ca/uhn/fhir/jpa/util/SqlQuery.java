package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * hapi-fhir-jpa
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

import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.Validate;
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
	private final LanguageEnum myLanguage;

	public SqlQuery(String theSql, List<String> theParams, long theQueryTimestamp, long theElapsedTime, StackTraceElement[] theStackTraceElements, int theSize) {
		this(theSql,  theParams,  theQueryTimestamp,  theElapsedTime,  theStackTraceElements, theSize, LanguageEnum.SQL);
	}

	public SqlQuery(String theSql, List<String> theParams, long theQueryTimestamp, long theElapsedTime, StackTraceElement[] theStackTraceElements, int theSize, LanguageEnum theLanguage) {
		Validate.notNull(theLanguage, "theLanguage must not be null");

		mySql = theSql;
		myParams = Collections.unmodifiableList(theParams);
		myQueryTimestamp = theQueryTimestamp;
		myElapsedTime = theElapsedTime;
		myStackTrace = theStackTraceElements;
		mySize = theSize;
		myLanguage = theLanguage;
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

	public LanguageEnum getLanguage() {
		return myLanguage;
	}

	public String getSql(boolean theInlineParams, boolean theFormat, boolean theSanitizeParams) {
		String retVal = mySql;
		if (theFormat) {
			if (getLanguage() == LanguageEnum.SQL) {
				retVal = new BasicFormatterImpl().format(retVal);

				// BasicFormatterImpl annoyingly adds a newline at the very start of its output
				while (retVal.startsWith("\n")) {
					retVal = retVal.substring(1);
				}
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

		return trim(retVal);
	}

	public StackTraceElement[] getStackTrace() {
		return myStackTrace;
	}

	public int getSize() {
		return mySize;
	}


	public enum LanguageEnum {

		SQL,
		JSON

	}

	@Override
	public String toString() {
		return getSql(true, true);
	}
}
