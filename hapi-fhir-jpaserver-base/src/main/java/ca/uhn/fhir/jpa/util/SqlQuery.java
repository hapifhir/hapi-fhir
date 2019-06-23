package ca.uhn.fhir.jpa.util;

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
