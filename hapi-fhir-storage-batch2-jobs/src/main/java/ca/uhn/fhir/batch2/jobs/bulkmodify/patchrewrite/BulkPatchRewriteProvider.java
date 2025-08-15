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
package ca.uhn.fhir.batch2.jobs.bulkmodify.patchrewrite;

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyOrRewriteProvider;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.IOException;
import java.util.List;

/**
 * Plain provider for the <code>$bulk-patch-rewrite-history</code> operation
 *
 * @since 8.6.0
 */
public class BulkPatchRewriteProvider extends BaseBulkModifyOrRewriteProvider {

	/**
	 * Operation: $bulk-patch-rewrite-history
	 */
	@Operation(name = JpaConstants.OPERATION_BULK_PATCH_REWRITE, manualResponse = true)
	public void bulkPatch(
			ServletRequestDetails theRequestDetails,
			@Description("The FHIRPatch document to apply to resources. Must be a Parameters resource.")
					@OperationParam(
							name = JpaConstants.OPERATION_BULK_PATCH_PARAM_PATCH,
							typeName = "Parameters",
							min = 1,
							max = 1)
					IBaseResource thePatch,
			@Description(
							"One ore more relative search parameter URLs (e.g. \"Patient?active=true\" or \"Observation?\") that will be reindexed.")
					@OperationParam(
							name = JpaConstants.OPERATION_BULK_PATCH_PARAM_URL,
							typeName = "string",
							min = 1,
							max = OperationParam.MAX_UNLIMITED)
					List<IPrimitiveType<String>> theUrlsToReindex) {
		BulkPatchRewriteJobParameters jobParameters = new BulkPatchRewriteJobParameters();
		jobParameters.setFhirPatch(myContext, thePatch);

		startJobAndReturnResponse(theRequestDetails, theUrlsToReindex, jobParameters);
	}

	/**
	 * Operation: $bulk-patch-status
	 */
	@Operation(name = JpaConstants.OPERATION_BULK_PATCH_REWRITE_STATUS, idempotent = true, manualResponse = true)
	public void bulkPatchStatus(
			ServletRequestDetails theRequestDetails,
			// _jobId=
			@Description("Query the server for the status of a bulk patch operation")
					@OperationParam(
							name = JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID,
							typeName = "string",
							min = 1,
							max = 1)
					IPrimitiveType<String> theJobId)
			throws IOException {

		pollForJobStatus(theRequestDetails, theJobId);
	}

	@Nonnull
	@Override
	protected String getOperationPollForStatusStatus() {
		return JpaConstants.OPERATION_BULK_PATCH_REWRITE_STATUS;
	}

	@Nonnull
	@Override
	protected String getJobId() {
		return BulkPatchRewriteJobAppCtx.JOB_ID;
	}

	@Nonnull
	@Override
	protected String getOperationName() {
		return JpaConstants.OPERATION_BULK_PATCH_REWRITE;
	}
}
