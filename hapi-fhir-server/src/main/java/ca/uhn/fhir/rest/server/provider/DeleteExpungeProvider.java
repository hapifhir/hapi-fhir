package ca.uhn.fhir.rest.server.provider;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
