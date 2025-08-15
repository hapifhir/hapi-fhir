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
package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyOrRewriteGenerateReportStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesResultsJson;
import jakarta.annotation.Nonnull;

public class BulkPatchGenerateReportStep extends BaseBulkModifyOrRewriteGenerateReportStep<BulkPatchJobParameters> {

	@Nonnull
	@Override
	protected String provideJobName() {
		return "Bulk Patch";
	}

	@Override
	public IReductionStepWorker<
					BulkPatchJobParameters, BulkModifyResourcesChunkOutcomeJson, BulkModifyResourcesResultsJson>
			newInstance() {
		return new BulkPatchGenerateReportStep();
	}
}
