/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.bulkmodify.reindex;

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyOrRewriteProvider;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexUtils;
import ca.uhn.fhir.batch2.jobs.step.ResourceIdListStep;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.base.Ascii;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.EnumUtils;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.IOException;
import java.util.List;

import static ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters.OPTIMIZE_STORAGE;
import static ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters.REINDEX_SEARCH_PARAMETERS;

public class ReindexProvider extends BaseBulkModifyOrRewriteProvider {

	/**
	 * Constructor
	 */
	public ReindexProvider() {
		super();
	}

	@Operation(name = ProviderConstants.OPERATION_REINDEX, idempotent = false, manualResponse = true)
	public void reindex(
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
			@Description(
							"Should the indexer check and correct resources with an invalid current version pointer (default: "
									+ ReindexParameters.CORRECT_CURRENT_VERSION_DEFAULT_STRING + ")")
					@OperationParam(
							name = ReindexJobParameters.CORRECT_CURRENT_VERSION,
							typeName = "code",
							min = 0,
							max = 1)
					IPrimitiveType<String> theCorrectCurrentVersion,
			@Description(
							"How many resources should be reindexed in a single batch. This can be reduced if you need to reindex large resources and therefore need to reduce memory footprint. Values larger than "
									+ ResourceIdListStep.MAX_BATCH_OF_IDS + " or less than 1 will be ignored (default: "
									+ ResourceIdListStep.MAX_BATCH_OF_IDS + ")")
					@OperationParam(
							name = ProviderConstants.OPERATION_REINDEX_PARAM_BATCH_SIZE,
							typeName = "integer",
							min = 0,
							max = 1)
					IPrimitiveType<Integer> theBatchSize,
			ServletRequestDetails theRequestDetails,
			// partitionId
			@OperationParam(
				name = JpaConstants.OPERATION_BULK_PATCH_PARAM_PARTITION_ID,
				typeName = "string",
				min = 0,
				max = OperationParam.MAX_UNLIMITED)
			List<IPrimitiveType<String>> thePartitionIds) throws IOException {

		ReindexJobParameters params = new ReindexJobParameters();

		if (theReindexSearchParameters != null) {
			String value = theReindexSearchParameters.getValue();
			if (value != null) {
				value = Ascii.toUpperCase(value);
				ValidateUtil.isTrueOrThrowInvalidRequest(
						EnumUtils.isValidEnum(ReindexParameters.ReindexSearchParametersEnum.class, value),
						"Invalid " + REINDEX_SEARCH_PARAMETERS + " value: %s",
						UrlUtil.sanitizeUrlPart(theReindexSearchParameters.getValue()));
				params.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.valueOf(value));
			}
		}
		if (theOptimizeStorage != null) {
			String value = theOptimizeStorage.getValue();
			if (value != null) {
				value = Ascii.toUpperCase(value);
				ValidateUtil.isTrueOrThrowInvalidRequest(
						EnumUtils.isValidEnum(ReindexParameters.OptimizeStorageModeEnum.class, value),
						"Invalid " + OPTIMIZE_STORAGE + " value: %s",
						UrlUtil.sanitizeUrlPart(theOptimizeStorage.getValue()));
				params.setOptimizeStorage(ReindexParameters.OptimizeStorageModeEnum.valueOf(value));
			}
		}

		if (theOptimisticLock != null && theOptimisticLock.getValue() != null) {
			params.setOptimisticLock(theOptimisticLock.getValue());
		}

		if (theCorrectCurrentVersion != null && theCorrectCurrentVersion.getValue() != null) {
			String value = Ascii.toUpperCase(theCorrectCurrentVersion.getValueAsString());
			ValidateUtil.isTrueOrThrowInvalidRequest(
					EnumUtils.isValidEnum(ReindexParameters.CorrectCurrentVersionModeEnum.class, value),
					"Invalid " + ReindexJobParameters.CORRECT_CURRENT_VERSION + " value: %s",
					UrlUtil.sanitizeUrlPart(theCorrectCurrentVersion.getValueAsString()));
			params.setCorrectCurrentVersion(ReindexParameters.CorrectCurrentVersionModeEnum.valueOf(value));

			if (theOptimisticLock == null || theOptimisticLock.getValue() == null) {
				params.setOptimisticLock(false);
			}
		}

		startJobAndReturnResponse(
			theRequestDetails,
			theUrlsToReindex,
			null,
			null,
			theBatchSize,
			null,
			null,
			thePartitionIds,
			params);

	}

	/**
	 * Operation: $hapi.fhir.reindex-status
	 */
	@Operation(name = JpaConstants.OPERATION_BULK_PATCH_STATUS, idempotent = true, manualResponse = true)
	public void bulkPatchStatus(
		ServletRequestDetails theRequestDetails,
		// _jobId=
		@Description("Query the server for the status of a reindex operation")
		@OperationParam(
			name = JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID,
			typeName = "string",
			min = 1,
			max = 1)
		IPrimitiveType<String> theJobId,

		// _return
		@Description("If provided, specifies that a specific part of the job report should be returned")
		@OperationParam(
			name = JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN,
			typeName = "code",
			min = 0,
			max = 1)
		IPrimitiveType<String> theReturn)
		throws IOException {

		pollForJobStatus(theRequestDetails, theJobId, theReturn);
	}

	@Nonnull
	@Override
	protected String getOperationPollForStatusStatus() {
		return JpaConstants.OPERATION_BULK_PATCH_STATUS;
	}

	@Nonnull
	@Override
	protected String getJobId() {
		return ReindexUtils.JOB_REINDEX;
	}

	@Nonnull
	@Override
	protected String getOperationName() {
		return ProviderConstants.OPERATION_REINDEX;
	}

	/**
	 * This operation is kicking off a batch job and should really be requiring the Prefer: respond-async
	 * header, but it didn't historically want that so we don't require it just to be kind.
	 */
	@Override
	protected boolean isRequirePreferAsyncHeader(ServletRequestDetails theRequestDetails) {
		return false;
	}
}
