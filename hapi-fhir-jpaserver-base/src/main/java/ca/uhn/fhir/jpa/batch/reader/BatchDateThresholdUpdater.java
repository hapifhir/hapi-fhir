package ca.uhn.fhir.jpa.batch.reader;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class BatchDateThresholdUpdater {
	private static final Logger ourLog = LoggerFactory.getLogger(BatchDateThresholdUpdater.class);

	private Function<Long, Date> myDateFromPid;

	public BatchDateThresholdUpdater() {
	}

	public BatchDateThresholdUpdater(Function<Long, Date> theDateFromPid) {
		myDateFromPid = theDateFromPid;
	}

	/**
	 * This method is used by batch jobs that process resource pids by date in multiple passes.  It's used to ensure
	 * the same resource isn't processed twice.  What it does is after a pass of processing pids, it sets
	 * the threshold date for the next pass from the last resource on the list and collects all of the resources that have that date into a temporary cache
	 * so that the caller can exclude those from the next pass.
	 *
	 * @param thePrevThreshold                         the date threshold from the previous pass
	 * @param theAlreadyProcessedPidsWithThresholdDate the set to load pids into that have the new threshold
	 * @param theProcessedPidsOrderedByDate            the pids ordered by date (can be ascending or descending)
	 * @return the new date threshold (can be the same as the old threshold if all pids on the list share the same date)
	 */

	public Date updateThresholdAndCache(Date thePrevThreshold, Set<Long> theAlreadyProcessedPidsWithThresholdDate, List<Long> theProcessedPidsOrderedByDate) {
		if (theProcessedPidsOrderedByDate.isEmpty()) {
			return thePrevThreshold;
		}

		// Adjust the low threshold to be the last resource in the batch we found
		Long pidOfLatestResourceInBatch = theProcessedPidsOrderedByDate.get(theProcessedPidsOrderedByDate.size() - 1);
		Date latestUpdatedDate = myDateFromPid.apply(pidOfLatestResourceInBatch);

		// The latest date has changed, create a new cache to store pids with that date
		if (thePrevThreshold != latestUpdatedDate) {
			theAlreadyProcessedPidsWithThresholdDate.clear();
		}
		theAlreadyProcessedPidsWithThresholdDate.add(pidOfLatestResourceInBatch);

		Date newThreshold = latestUpdatedDate;
		if (theProcessedPidsOrderedByDate.size() <= 1) {
			return newThreshold;
		}

		// There is more than one resource in this batch, add any others with the same date.  Assume the list is ordered by date.
		for (int index = theProcessedPidsOrderedByDate.size() - 2; index >= 0; --index) {
			Long pid = theProcessedPidsOrderedByDate.get(index);
			Date newDate = myDateFromPid.apply(pid);
			if (!latestUpdatedDate.equals(newDate)) {
				break;
			}
			theAlreadyProcessedPidsWithThresholdDate.add(pid);
		}

		return newThreshold;
	}

	/**
	 * @param theDateFromPid this is a Function to extract a date from a resource id
	 * @return
	 */
	public BatchDateThresholdUpdater setDateFromPid(Function<Long, Date> theDateFromPid) {
		myDateFromPid = theDateFromPid;
		return this;
	}
}
