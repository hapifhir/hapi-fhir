package ca.uhn.fhir.rest.server.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IMultiUrlJobSubmitter;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.jetbrains.annotations.Nullable;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BaseMultiUrlProcessor {
	private final FhirContext myFhirContext;
	private final IMultiUrlJobSubmitter myMultiUrlProcessorJobSubmitter;

	public BaseMultiUrlProcessor(FhirContext theFhirContext, IMultiUrlJobSubmitter theMultiUrlProcessorJobSubmitter) {
		myMultiUrlProcessorJobSubmitter = theMultiUrlProcessorJobSubmitter;
		myFhirContext = theFhirContext;
	}

	protected IBaseParameters processUrls(List<String> theUrlsToProcess, Integer theBatchSize, RequestDetails theRequestDetails) {
		try {
			JobExecution jobExecution = myMultiUrlProcessorJobSubmitter.submitJob(theBatchSize, theUrlsToProcess, theRequestDetails);
			IBaseParameters retval = ParametersUtil.newInstance(myFhirContext);
			ParametersUtil.addParameterToParametersLong(myFhirContext, retval, ProviderConstants.OPERATION_DELETE_EXPUNGE_RESPONSE_JOB_ID, jobExecution.getJobId());
			return retval;
		} catch (JobParametersInvalidException e) {
			throw new InvalidRequestException("Invalid job parameters: " + e.getMessage(), e);
		}
	}

	protected IBaseParameters processEverything(Integer theBatchSize, RequestDetails theRequestDetails) {
		try {
			JobExecution jobExecution = myMultiUrlProcessorJobSubmitter.submitJob(theBatchSize, new ArrayList<>(), theRequestDetails);
			IBaseParameters retval = ParametersUtil.newInstance(myFhirContext);
			ParametersUtil.addParameterToParametersLong(myFhirContext, retval, ProviderConstants.OPERATION_DELETE_EXPUNGE_RESPONSE_JOB_ID, jobExecution.getJobId());
			return retval;
		} catch (JobParametersInvalidException e) {
			throw new InvalidRequestException("Invalid job parameters: " + e.getMessage(), e);
		}
	}

	@Nullable
	protected Integer getBatchSize(IPrimitiveType<BigDecimal> theBatchSize) {
		Integer batchSize = null;
		if (theBatchSize != null && !theBatchSize.isEmpty()) {
			batchSize = theBatchSize.getValue().intValue();
		}
		return batchSize;
	}
}
