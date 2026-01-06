/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.export.v2;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.export.v3.ExpandResourceAndWriteBinaryStep;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;

public class ExpandResourceAndWriteBinaryV2Step extends ExpandResourceAndWriteBinaryStep {

	@Override
	protected RequestDetails newRequestDetails(
			StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
			BulkExportJobParameters jobParameters) {
		RequestPartitionId partitionId = jobParameters.getPartitionIdForSecurity();
		return new SystemRequestDetails().setRequestPartitionId(partitionId);
	}
}
