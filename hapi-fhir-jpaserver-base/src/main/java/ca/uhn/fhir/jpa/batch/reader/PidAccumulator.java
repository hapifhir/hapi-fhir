package ca.uhn.fhir.jpa.batch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class PidAccumulator {
	private static final Logger ourLog = LoggerFactory.getLogger(PidAccumulator.class);

	private Function<Long, Date> myDateFromPid;

	public PidAccumulator() {
	}

	public PidAccumulator(Function<Long, Date> theDateFromPid) {
		myDateFromPid = theDateFromPid;
	}

	// FIXME KHS test
	public Date setThresholds(Date theThreshold, Set<Long> theAlreadySeenPids, List<Long> theNewPids) {
		Date retval = theThreshold;
		if (theNewPids.isEmpty()) {
			return retval;
		}

		// Adjust the low threshold to be the latest resource in the batch we found
		Long pidOfLatestResourceInBatch = theNewPids.get(theNewPids.size() - 1);
		Date latestUpdatedDate = myDateFromPid.apply(pidOfLatestResourceInBatch);

		// The latest date has changed, create a new cache to store pids with that date
		if (theThreshold != latestUpdatedDate) {
			theAlreadySeenPids.clear();
		}
		theAlreadySeenPids.add(pidOfLatestResourceInBatch);
		retval = latestUpdatedDate;
		if (theNewPids.size() <= 1) {
			return theThreshold;
		}

		// There is more than one resource in this batch
		for (int index = theNewPids.size() - 2; index >= 0; --index) {
			Long pid = theNewPids.get(index);
			Date newDate = myDateFromPid.apply(pid);
			if (!latestUpdatedDate.equals(newDate)) {
				break;
			}
			theAlreadySeenPids.add(pid);
		}

		return retval;
	}

	public PidAccumulator setDateFromPid(Function<Long, Date> theDateFromPid) {
		myDateFromPid = theDateFromPid;
		return this;
	}
}
