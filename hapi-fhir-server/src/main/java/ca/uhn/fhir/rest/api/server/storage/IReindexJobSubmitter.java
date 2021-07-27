package ca.uhn.fhir.rest.api.server.storage;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;

// Tag interface for Spring wiring
public interface IReindexJobSubmitter extends IMultiUrlJobSubmitter {
	JobExecution submitEverythingJob(Integer theBatchSize, RequestDetails theRequest) throws JobParametersInvalidException;
}
