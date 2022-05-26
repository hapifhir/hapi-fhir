package ca.uhn.fhir.batch2.jobs;

import org.hl7.fhir.r4.model.InstantType;

import java.util.Date;

public class Batch2Constants {
	public static final Date BATCH_START_DATE = new InstantType("2000-01-01T00:00:00Z").getValue();
}
