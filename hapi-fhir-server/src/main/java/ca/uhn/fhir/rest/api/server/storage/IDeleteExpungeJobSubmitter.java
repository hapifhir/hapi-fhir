package ca.uhn.fhir.rest.api.server.storage;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;

import java.util.List;

public interface IDeleteExpungeJobSubmitter {
	/**
	 * @param theBatchSize           For each pass, when synchronously searching for resources, limit the number of matching resources to this number
	 * @param theTenantId            The tenant to perform the searches on
	 * @param theUrlsToDeleteExpunge A list of strings of the form "/Patient?active=true"
	 * @return The Spring Batch JobExecution that was started to run this batch job
	 * @throws JobParametersInvalidException
	 */
	JobExecution submitJob(Integer theBatchSize, RequestDetails theRequest, List<String> theUrlsToDeleteExpunge) throws JobParametersInvalidException;
}
