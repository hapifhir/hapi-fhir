package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.StatusEnum;

import java.util.Date;

public class BatchInstanceStatusDTO {
	public final String id;
	public final StatusEnum status;
	public final Date start;
	public final Date stop;

	public BatchInstanceStatusDTO(String theId, StatusEnum theStatus, Date theStart, Date theStop) {
		id = theId;
		status = theStatus;
		start = theStart;
		stop = theStop;
	}
}
