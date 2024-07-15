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
package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.jobs.parameters.UrlPartitioner;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.base.Ascii;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;

import static ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters.OPTIMIZE_STORAGE;
import static ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters.REINDEX_SEARCH_PARAMETERS;

public class ReindexProvider {

	private final FhirContext myFhirContext;
	private final IJobCoordinator myJobCoordinator;
	private final IJobPartitionProvider myJobPartitionProvider;
	private final UrlPartitioner myUrlPartitioner;

	/**
	 * Constructor
	 */
	public ReindexProvider(
			FhirContext theFhirContext,
			IJobCoordinator theJobCoordinator,
			IJobPartitionProvider theJobPartitionProvider,
			UrlPartitioner theUrlPartitioner) {
		myFhirContext = theFhirContext;
		myJobCoordinator = theJobCoordinator;
		myJobPartitionProvider = theJobPartitionProvider;
		myUrlPartitioner = theUrlPartitioner;
	}

	@Operation(name = ProviderConstants.OPERATION_REINDEX, idempotent = false)
	public IBaseParameters reindex(
			@Description(
							"Optionally provides one ore more relative search parameter URLs (e.g. \"Patient?active=true\" or \"Observation?\") that will be reindexed. Note that the URL applies to the resources as they are currently indexed, so you should not use a search parameter that needs reindexing in the URL or some resources may be missed. If no URLs are provided, all resources of all types will be reindexed.")
					@OperationParam(
							name = ProviderConstants.OPERATION_REINDEX_PARAM_URL,
							typeName = "string",
							min = 0,
							max = OperationParam.MAX_UNLIMITED)
					List<IPrimitiveType<String>> theUrlsToReindex,
			@Description("Should search parameters be reindexed (default: "
							+ ReindexParameters.REINDEX_SEARCH_PARAMETERS_DEFAULT_STRING + ")")
					@OperationParam(name = REINDEX_SEARCH_PARAMETERS, typeName = "code", min = 0, max = 1)
					IPrimitiveType<String> theReindexSearchParameters,
			@Description("Should we attempt to optimize storage for resources (default: "
							+ ReindexParameters.OPTIMIZE_STORAGE_DEFAULT_STRING + ")")
					@OperationParam(name = OPTIMIZE_STORAGE, typeName = "code", min = 0, max = 1)
					IPrimitiveType<String> theOptimizeStorage,
			@Description(
							"Should we attempt to optimistically lock resources being reindexed in order to avoid concurrency issues (default: "
									+ ReindexParameters.OPTIMISTIC_LOCK_DEFAULT + ")")
					@OperationParam(name = ReindexJobParameters.OPTIMISTIC_LOCK, typeName = "boolean", min = 0, max = 1)
					IPrimitiveType<Boolean> theOptimisticLock,
			RequestDetails theRequestDetails) {

		ReindexJobParameters params = new ReindexJobParameters();

		if (theReindexSearchParameters != null) {
			String value = theReindexSearchParameters.getValue();
			if (value != null) {
				value = Ascii.toUpperCase(value);
				ValidateUtil.isTrueOrThrowInvalidRequest(
						EnumUtils.isValidEnum(ReindexParameters.ReindexSearchParametersEnum.class, value),
						"Invalid " + REINDEX_SEARCH_PARAMETERS + " value: "
								+ UrlUtil.sanitizeUrlPart(theReindexSearchParameters.getValue()));
				params.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.valueOf(value));
			}
		}
		if (theOptimizeStorage != null) {
			String value = theOptimizeStorage.getValue();
			if (value != null) {
				value = Ascii.toUpperCase(value);
				ValidateUtil.isTrueOrThrowInvalidRequest(
						EnumUtils.isValidEnum(ReindexParameters.OptimizeStorageModeEnum.class, value),
						"Invalid " + OPTIMIZE_STORAGE + " value: "
								+ UrlUtil.sanitizeUrlPart(theOptimizeStorage.getValue()));
				params.setOptimizeStorage(ReindexParameters.OptimizeStorageModeEnum.valueOf(value));
			}
		}
		if (theOptimisticLock != null && theOptimisticLock.getValue() != null) {
			params.setOptimisticLock(theOptimisticLock.getValue());
		}

		if (theUrlsToReindex != null) {
			theUrlsToReindex.stream()
					.map(IPrimitiveType::getValue)
					.filter(StringUtils::isNotBlank)
					.map(url -> myUrlPartitioner.partitionUrl(url, theRequestDetails))
					.forEach(params::addPartitionedUrl);
		}

		myJobPartitionProvider
				.getPartitions(theRequestDetails, ProviderConstants.OPERATION_REINDEX)
				.forEach(params::addRequestPartitionId);

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		request.setParameters(params);
		Batch2JobStartResponse response = myJobCoordinator.startInstance(theRequestDetails, request);

		IBaseParameters retVal = ParametersUtil.newInstance(myFhirContext);
		ParametersUtil.addParameterToParametersString(
				myFhirContext, retVal, ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID, response.getInstanceId());
		return retVal;
	}
}
