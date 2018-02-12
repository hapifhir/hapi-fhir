package ca.uhn.fhir.jpa.util;

import org.apache.commons.lang3.time.DateUtils;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public class StopWatch {

	private long myStarted = System.currentTimeMillis();

	/**
	 * Constructor
	 */
	public StopWatch() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param theStart The time to record as the start for this timer
	 */
	public StopWatch(Date theStart) {
		myStarted = theStart.getTime();
	}

	public long getMillis() {
		long now = System.currentTimeMillis();
		return now - myStarted;
	}

	public long getMillis(Date theNow) {
		return theNow.getTime() - myStarted;
	}

	public long getMillisAndRestart() {
		long now = System.currentTimeMillis();
		long retVal = now - myStarted;
		myStarted = now;
		return retVal;
	}

	/**
	 * @param theNumOperations Ok for this to be 0, it will be treated as 1
	 */
	public int getMillisPerOperation(int theNumOperations) {
		return (int) (((double) getMillis()) / Math.max(1.0, theNumOperations));
	}

	public Date getStartedDate() {
		return new Date(myStarted);
	}

	public double getThroughput(int theNumOperations, TimeUnit theUnit) {
		if (theNumOperations <= 0) {
			return 0.0f;
		}

		long millisElapsed = Math.max(1, getMillis());
		long periodMillis = theUnit.toMillis(1);

		double numerator = theNumOperations;
		double denominator = ((double)millisElapsed)/((double)periodMillis);

		return numerator / denominator;
	}

	public String formatThroughput(int theNumOperations, TimeUnit theUnit) {
		double throughput = getThroughput(theNumOperations, theUnit);
		return new DecimalFormat("0.0").format(throughput);
	}

	public void restart() {
		myStarted = System.currentTimeMillis();
	}

	/**
	 * Formats value in the format [DD d ]HH:mm:ss.SSSS
	 */
	@Override
	public String toString() {
		return formatMillis(getMillis());
	}

	/**
	 * Append a right-aligned and zero-padded numeric value to a `StringBuilder`.
	 */
	static private void append(StringBuilder tgt, String pfx, int dgt, long val) {
		tgt.append(pfx);
		if (dgt > 1) {
			int pad = (dgt - 1);
			for (long xa = val; xa > 9 && pad > 0; xa /= 10) {
				pad--;
			}
			for (int xa = 0; xa < pad; xa++) {
				tgt.append('0');
			}
		}
		tgt.append(val);
	}

	static public String formatMillis(long val) {
		StringBuilder buf = new StringBuilder(20);
		if (val >= DateUtils.MILLIS_PER_DAY) {
			long days = val / DateUtils.MILLIS_PER_DAY;
			append(buf, "", 1, days);
			if (days > 1) {
				buf.append(" days ");
			} else if (days == 1) {
				buf.append(" day ");
			}
			append(buf, "", 2, ((val % DateUtils.MILLIS_PER_DAY) / DateUtils.MILLIS_PER_HOUR));
		} else {
			append(buf, "", 2, ((val % DateUtils.MILLIS_PER_DAY) / DateUtils.MILLIS_PER_HOUR));
		}
		append(buf, ":", 2, ((val % DateUtils.MILLIS_PER_HOUR) / DateUtils.MILLIS_PER_MINUTE));
		append(buf, ":", 2, ((val % DateUtils.MILLIS_PER_MINUTE) / DateUtils.MILLIS_PER_SECOND));
		append(buf, ".", 3, (val % DateUtils.MILLIS_PER_SECOND));
		return buf.toString();
	}

}
