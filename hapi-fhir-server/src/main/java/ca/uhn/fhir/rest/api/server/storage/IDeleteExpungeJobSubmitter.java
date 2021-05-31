package ca.uhn.fhir.rest.api.server.storage;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;

import java.util.List;

public interface IDeleteExpungeJobSubmitter {
	/**
	 * @param theBatchSize           For each pass, when synchronously searching for resources, limit the number of matching resources to this number
	 * @param theUrlsToDeleteExpunge A list of strings of the form "/Patient?active=true"
	 * @return the executing jon
	 * @throws JobParametersInvalidException
	 */
	JobExecution submitJob(Long theBatchSize, List<String> theUrlsToDeleteExpunge) throws JobParametersInvalidException;
}
