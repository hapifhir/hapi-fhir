/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.expunge;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteExpungeProvider {
	private final FhirContext myFhirContext;
	private final IDeleteExpungeJobSubmitter myDeleteExpungeJobSubmitter;

	public DeleteExpungeProvider(FhirContext theFhirContext, IDeleteExpungeJobSubmitter theDeleteExpungeJobSubmitter) {
		myFhirContext = theFhirContext;
		myDeleteExpungeJobSubmitter = theDeleteExpungeJobSubmitter;
	}

	@Operation(name = ProviderConstants.OPERATION_DELETE_EXPUNGE, idempotent = false)
	public IBaseParameters deleteExpunge(
			@OperationParam(name = ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, typeName = "string", min = 1)
					List<IPrimitiveType<String>> theUrlsToDeleteExpunge,
			@OperationParam(
							name = ProviderConstants.OPERATION_DELETE_BATCH_SIZE,
							typeName = "integer",
							min = 0,
							max = 1)
					IPrimitiveType<Integer> theBatchSize,
			@OperationParam(name = ProviderConstants.OPERATION_DELETE_CASCADE, typeName = "boolean", min = 0, max = 1)
					IPrimitiveType<Boolean> theCascade,
			@OperationParam(
							name = ProviderConstants.OPERATION_DELETE_CASCADE_MAX_ROUNDS,
							typeName = "integer",
							min = 0,
							max = 1)
					IPrimitiveType<Integer> theCascadeMaxRounds,
			RequestDetails theRequestDetails) {
		if (theUrlsToDeleteExpunge == null) {
			throw new InvalidRequestException(
					Msg.code(2101) + "At least one `url` parameter to $delete-expunge must be provided.");
		}
		List<String> urls = theUrlsToDeleteExpunge.stream()
				.map(IPrimitiveType::getValue)
				.filter(StringUtils::isNotBlank)
				.collect(Collectors.toList());

		Integer batchSize = null;
		if (theBatchSize != null && theBatchSize.getValue() != null && theBatchSize.getValue() > 0) {
			batchSize = theBatchSize.getValue();
		}

		boolean cascase = false;
		if (theCascade != null && theCascade.hasValue()) {
			cascase = theCascade.getValue();
		}

		Integer cascadeMaxRounds = null;
		if (theCascadeMaxRounds != null) {
			cascadeMaxRounds = theCascadeMaxRounds.getValue();
		}

		String jobId =
				myDeleteExpungeJobSubmitter.submitJob(batchSize, urls, cascase, cascadeMaxRounds, theRequestDetails);

		IBaseParameters retval = ParametersUtil.newInstance(myFhirContext);
		ParametersUtil.addParameterToParametersString(
				myFhirContext, retval, ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID, jobId);
		return retval;
	}
}
