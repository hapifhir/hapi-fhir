package ca.uhn.fhir.rest.server.provider;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IMultiUrlJobSubmitter;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;

public class MultiUrlProcessor {
	private final FhirContext myFhirContext;
	private final IMultiUrlJobSubmitter myMultiUrlProcessorJobSubmitter;

	public MultiUrlProcessor(FhirContext theFhirContext, IMultiUrlJobSubmitter theMultiUrlProcessorJobSubmitter) {
		myMultiUrlProcessorJobSubmitter = theMultiUrlProcessorJobSubmitter;
		myFhirContext = theFhirContext;
	}

	public IBaseParameters processUrls(List<String> theUrlsToProcess, Integer theBatchSize, RequestDetails theRequestDetails) {
		try {
			JobExecution jobExecution = myMultiUrlProcessorJobSubmitter.submitJob(theBatchSize, theUrlsToProcess, theRequestDetails);
			IBaseParameters retval = ParametersUtil.newInstance(myFhirContext);
			ParametersUtil.addParameterToParametersLong(myFhirContext, retval, ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID, jobExecution.getJobId());
			return retval;
		} catch (JobParametersInvalidException e) {
			throw new InvalidRequestException(Msg.code(309) + "Invalid job parameters: " + e.getMessage(), e);
		}
	}

	@Nullable
	public Integer getBatchSize(IPrimitiveType<BigDecimal> theBatchSize) {
		Integer batchSize = null;
		if (theBatchSize != null && !theBatchSize.isEmpty()) {
			batchSize = theBatchSize.getValue().intValue();
		}
		return batchSize;
	}
}
