package ca.uhn.fhir.rest.server.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteExpungeProvider {
	private final IDeleteExpungeJobSubmitter myDeleteExpungeJobSubmitter;

	private final FhirContext myFhirContext;

	public DeleteExpungeProvider(FhirContext theFhirContext, IDeleteExpungeJobSubmitter theDeleteExpungeJobSubmitter) {
		myDeleteExpungeJobSubmitter = theDeleteExpungeJobSubmitter;
		myFhirContext = theFhirContext;
	}

	@Operation(name = ProviderConstants.OPERATION_DELETE_EXPUNGE, idempotent = false)
	public IBaseParameters deleteExpunge(
		@OperationParam(name = ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, typeName = "string", min = 1) List<IPrimitiveType<String>> theUrlsToDeleteExpunge,
		@OperationParam(name = ProviderConstants.OPERATION_DELETE_BATCH_SIZE, typeName = "decimal", min = 0, max = 1) IPrimitiveType<BigDecimal> theBatchSize,
		RequestDetails theRequestDetails
	) {
		try {
			List<String> urls = theUrlsToDeleteExpunge.stream().map(IPrimitiveType::getValue).collect(Collectors.toList());
			Integer batchSize = null;
			if (theBatchSize != null && !theBatchSize.isEmpty()) {
				batchSize = theBatchSize.getValue().intValue();
			}
			JobExecution jobExecution = myDeleteExpungeJobSubmitter.submitJob(batchSize, theRequestDetails, urls);
			IBaseParameters retval = ParametersUtil.newInstance(myFhirContext);
			ParametersUtil.addParameterToParametersLong(myFhirContext, retval, ProviderConstants.OPERATION_DELETE_EXPUNGE_RESPONSE_JOB_ID, jobExecution.getJobId());
			return retval;
		} catch (JobParametersInvalidException e) {
			throw new InvalidRequestException("Invalid job parameters: " + e.getMessage(), e);
		}
	}
}
