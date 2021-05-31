package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch.BatchJobsConfig;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.delete.job.DeleteExpungeJobConfig;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteExpungeJobSubmitterImpl implements IDeleteExpungeJobSubmitter {
	@Autowired
	private IBatchJobSubmitter myBatchJobSubmitter;
	@Autowired
	@Qualifier(BatchJobsConfig.DELETE_EXPUNGE_JOB_NAME)
	private Job myDeleteExpungeJob;
	@Autowired
	FhirContext myFhirContext;

	@Override
	public IBaseParameters submitJob(List<IPrimitiveType<String>> theUrlsToExpungeDelete) throws JobParametersInvalidException {
		List<String> urlList = theUrlsToExpungeDelete.stream().map(IPrimitiveType::getValue).collect(Collectors.toList());
		JobParameters jobParameters = DeleteExpungeJobConfig.buildJobParameters(urlList);
		// execute
		JobExecution jobExecution = myBatchJobSubmitter.runJob(myDeleteExpungeJob, jobParameters);

		IBaseParameters retval = ParametersUtil.newInstance(myFhirContext);
		ParametersUtil.addParameterToParametersLong(myFhirContext, retval, "jobId", jobExecution.getJobId());
		return retval;
	}
}
