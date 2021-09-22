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
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IReindexJobSubmitter;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ReindexProvider {
	private final FhirContext myFhirContext;
	private final IReindexJobSubmitter myReindexJobSubmitter;
	private final MultiUrlProcessor myMultiUrlProcessor;

	public ReindexProvider(FhirContext theFhirContext, IReindexJobSubmitter theReindexJobSubmitter) {
		myFhirContext = theFhirContext;
		myMultiUrlProcessor = new MultiUrlProcessor(theFhirContext, theReindexJobSubmitter);
		myReindexJobSubmitter = theReindexJobSubmitter;
	}

	@Operation(name = ProviderConstants.OPERATION_REINDEX, idempotent = false)
	public IBaseParameters Reindex(
		@OperationParam(name = ProviderConstants.OPERATION_REINDEX_PARAM_URL, typeName = "string", min = 0, max = 1) List<IPrimitiveType<String>> theUrlsToReindex,
		@OperationParam(name = ProviderConstants.OPERATION_REINDEX_PARAM_BATCH_SIZE, typeName = "decimal", min = 0, max = 1) IPrimitiveType<BigDecimal> theBatchSize,
		@OperationParam(name = ProviderConstants.OPERATION_REINDEX_PARAM_EVERYTHING, typeName = "boolean", min = 0, max = 1) IPrimitiveType<Boolean> theEverything,
		RequestDetails theRequestDetails
	) {
		boolean everything = theEverything != null && theEverything.getValue();
		@Nullable Integer batchSize = myMultiUrlProcessor.getBatchSize(theBatchSize);
		if (everything) {
			return processEverything(batchSize, theRequestDetails);
		} else if (theUrlsToReindex != null && !theUrlsToReindex.isEmpty()) {
			List<String> urls = theUrlsToReindex.stream().map(IPrimitiveType::getValue).collect(Collectors.toList());
			return myMultiUrlProcessor.processUrls(urls, batchSize, theRequestDetails);
		} else {
			throw new InvalidRequestException(ProviderConstants.OPERATION_REINDEX + " must specify either everything=true or provide at least one value for " + ProviderConstants.OPERATION_REINDEX_PARAM_URL);
		}
	}

	private IBaseParameters processEverything(Integer theBatchSize, RequestDetails theRequestDetails) {
		try {
			JobExecution jobExecution = myReindexJobSubmitter.submitEverythingJob(theBatchSize, theRequestDetails);
			IBaseParameters retVal = ParametersUtil.newInstance(myFhirContext);
			ParametersUtil.addParameterToParametersLong(myFhirContext, retVal, ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID, jobExecution.getJobId());
			return retVal;
		} catch (JobParametersInvalidException e) {
			throw new InvalidRequestException("Invalid job parameters: " + e.getMessage(), e);
		}
	}


}
