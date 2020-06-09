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
 * This class will prevent a job from running if the UUID does not exist or is invalid.
 */
public class JobExistsParameterValidator implements JobParametersValidator {
	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;

	@Override
	public void validate(JobParameters theJobParameters) throws JobParametersInvalidException {
		String jobUUID = theJobParameters.getString("jobUUID");
		if (StringUtils.isBlank(jobUUID)) {
			throw new JobParametersInvalidException("You did not pass a jobUUID to this job!");
		}

		Optional<BulkExportJobEntity> oJob = myBulkExportJobDao.findByJobId(jobUUID);
		if (!oJob.isPresent()) {
			throw new JobParametersInvalidException("There is no persisted job that exists with UUID: " + jobUUID);
		}
	}
}
