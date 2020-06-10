package ca.uhn.fhir.jpa.bulk.job;

import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * This class will prevent job running if the UUID is found to be non-existent, or invalid.
 */
public class JobExistsParameterValidator implements JobParametersValidator {
	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;

	@Override
	public void validate(JobParameters theJobParameters) throws JobParametersInvalidException {
		if (theJobParameters == null) {
			throw new JobParametersInvalidException("This job requires Parameters: [readChunkSize] and [jobUUID]");
		}

		Long readChunkSize = theJobParameters.getLong("readChunkSize");
		String errorMessage = "";
		if (readChunkSize == null || readChunkSize < 1) {
			errorMessage += "There must be a valid number for readChunkSize, which is at least 1. ";
		}
		String jobUUID = theJobParameters.getString("jobUUID");
		if (StringUtils.isBlank(jobUUID)) {
			errorMessage += "You did not pass a jobUUID to this job! ";
		}

		Optional<BulkExportJobEntity> oJob = myBulkExportJobDao.findByJobId(jobUUID);
		if (!oJob.isPresent()) {
			errorMessage += "There is no persisted job that exists with UUID: " + jobUUID + ". ";
		}
		if (!StringUtils.isEmpty(errorMessage)) {
			throw new JobParametersInvalidException(errorMessage);
		}
	}
}
