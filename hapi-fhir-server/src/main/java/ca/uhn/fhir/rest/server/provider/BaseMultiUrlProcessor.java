package ca.uhn.fhir.rest.server.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IMultiUrlJobSubmitter;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class BaseMultiUrlProcessor {
	private final FhirContext myFhirContext;
	private final IMultiUrlJobSubmitter myMultiUrlProcessorJobSubmitter;

	public BaseMultiUrlProcessor(FhirContext theFhirContext, IMultiUrlJobSubmitter theMultiUrlProcessorJobSubmitter) {
		myMultiUrlProcessorJobSubmitter = theMultiUrlProcessorJobSubmitter;
		myFhirContext = theFhirContext;
	}

	protected IBaseParameters processUrls(List<IPrimitiveType<String>> theUrlsToDeleteExpunge, IPrimitiveType<BigDecimal> theBatchSize, RequestDetails theRequestDetails) {
		try {
			List<String> urls = theUrlsToDeleteExpunge.stream().map(IPrimitiveType::getValue).collect(Collectors.toList());
			Integer batchSize = null;
			if (theBatchSize != null && !theBatchSize.isEmpty()) {
				batchSize = theBatchSize.getValue().intValue();
			}
			JobExecution jobExecution = myMultiUrlProcessorJobSubmitter.submitJob(batchSize, urls, theRequestDetails);
			IBaseParameters retval = ParametersUtil.newInstance(myFhirContext);
			ParametersUtil.addParameterToParametersLong(myFhirContext, retval, ProviderConstants.OPERATION_DELETE_EXPUNGE_RESPONSE_JOB_ID, jobExecution.getJobId());
			return retval;
		} catch (JobParametersInvalidException e) {
			throw new InvalidRequestException("Invalid job parameters: " + e.getMessage(), e);
		}
	}
}
