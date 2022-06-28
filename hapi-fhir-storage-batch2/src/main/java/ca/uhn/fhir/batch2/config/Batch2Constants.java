package ca.uhn.fhir.batch2.config;

import org.hl7.fhir.r4.model.InstantType;

import java.util.Date;

public class Batch2Constants {
	/**
	 * The batch 2 system assumes that all records have a start date later than this date.  This date is used as a starting
	 * date when performing operations that pull resources by time windows.
	 */
	public static final Date BATCH_START_DATE = new InstantType("2000-01-01T00:00:00Z").getValue();
}
