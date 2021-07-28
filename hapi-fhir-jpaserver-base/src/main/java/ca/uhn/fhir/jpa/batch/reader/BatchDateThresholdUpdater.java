package ca.uhn.fhir.jpa.batch.reader;

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

	// FIXME KHS test
	public Date updateThresholdAndCache(Date thePrevThreshold, Set<Long> theAlreadyProcessedPids, List<Long> theNewPids) {
		if (theNewPids.isEmpty()) {
			return thePrevThreshold;
		}

		// Adjust the low threshold to be the latest resource in the batch we found
		Long pidOfLatestResourceInBatch = theNewPids.get(theNewPids.size() - 1);
		Date latestUpdatedDate = myDateFromPid.apply(pidOfLatestResourceInBatch);

		// The latest date has changed, create a new cache to store pids with that date
		if (thePrevThreshold != latestUpdatedDate) {
			theAlreadyProcessedPids.clear();
		}
		theAlreadyProcessedPids.add(pidOfLatestResourceInBatch);

		Date newThreshold = latestUpdatedDate;
		if (theNewPids.size() <= 1) {
			return newThreshold;
		}

		// There is more than one resource in this batch
		for (int index = theNewPids.size() - 2; index >= 0; --index) {
			Long pid = theNewPids.get(index);
			Date newDate = myDateFromPid.apply(pid);
			if (!latestUpdatedDate.equals(newDate)) {
				break;
			}
			theAlreadyProcessedPids.add(pid);
		}

		return newThreshold;
	}

	public BatchDateThresholdUpdater setDateFromPid(Function<Long, Date> theDateFromPid) {
		myDateFromPid = theDateFromPid;
		return this;
	}
}
