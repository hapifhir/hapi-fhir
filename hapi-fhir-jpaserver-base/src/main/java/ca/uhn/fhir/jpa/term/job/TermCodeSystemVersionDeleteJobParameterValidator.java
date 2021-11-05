package ca.uhn.fhir.jpa.term.job;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.JOB_PARAM_CODE_SYSTEM_ID;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.JOB_PARAM_CODE_SYSTEM_VERSION_ID;

/**
 * Validates that a TermCodeSystem parameter is present
 */
public class TermCodeSystemVersionDeleteJobParameterValidator implements JobParametersValidator {

	@Override
	public void validate(JobParameters theJobParameters) throws JobParametersInvalidException {
		if (theJobParameters == null) {
			throw new JobParametersInvalidException("This job needs Parameter: '" + JOB_PARAM_CODE_SYSTEM_VERSION_ID + "'");
		}

		if ( ! theJobParameters.getParameters().containsKey(JOB_PARAM_CODE_SYSTEM_VERSION_ID)) {
			throw new JobParametersInvalidException("This job needs Parameter: '" + JOB_PARAM_CODE_SYSTEM_VERSION_ID + "'");
		}

		Long termCodeSystemPid = theJobParameters.getLong(JOB_PARAM_CODE_SYSTEM_VERSION_ID);
		if (termCodeSystemPid == null) {
			throw new JobParametersInvalidException("'" + JOB_PARAM_CODE_SYSTEM_VERSION_ID + "' parameter is null");
		}

		if (termCodeSystemPid <= 0) {
			throw new JobParametersInvalidException(
				"Invalid parameter '" + JOB_PARAM_CODE_SYSTEM_VERSION_ID + "' value: " + termCodeSystemPid);
		}
	}
}
