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

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyOrRewriteGenerateReportStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyCommonJobAppCtx;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.TypedPidToTypedPidAndVersionStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchModifyResourcesStep;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BulkModifyCommonJobAppCtx.class})
public class BulkPatchRewriteJobAppCtx extends BaseBulkPatchRewriteJobAppCtx {

	/**
	 * Constructor
	 */
	public BulkPatchRewriteJobAppCtx(
			IBatch2DaoSvc theBatch2DaoSvc, FhirContext theFhirContext, IDaoRegistry theDaoRegistry) {
		super(theBatch2DaoSvc, theFhirContext, theDaoRegistry);
	}

	@Bean("bulkModifyPatchRewriteJobDefinition")
	public JobDefinition<BulkPatchRewriteJobParameters> jobDefinition() {
		return super.buildJobDefinition();
	}

	@Bean("bulkModifyPatchRewriteModifyResourcesStep")
	@Override
	public BulkPatchModifyResourcesStep<BulkPatchRewriteJobParameters> modifyResourcesStep() {
		return new BulkPatchModifyResourcesStep<>(true);
	}

	@Bean("bulkModifyPatchRewriteGenerateRangesStep")
	@Override
	public GenerateRangeChunksStep<BulkPatchRewriteJobParameters> generateRangesStep() {
		return new GenerateRangeChunksStep<>();
	}

	@Bean("bulkModifyPatchRewriteGenerateReportStep")
	@Override
	public BaseBulkModifyOrRewriteGenerateReportStep<BulkPatchRewriteJobParameters> generateReportStep() {
		return new BulkPatchRewriteGenerateReportStep();
	}

	@Bean("bulkModifyPatchRewriteLoadIdsStep")
	@Override
	public LoadIdsStep<BulkPatchRewriteJobParameters> loadIdsStep() {
		return new LoadIdsStep<>(myBatch2DaoSvc);
	}

	@Bean("bulkModifyPatchRewriteExpandIdVersionsStep")
	@Override
	public IJobStepWorker<
					BulkPatchRewriteJobParameters, ResourceIdListWorkChunkJson, TypedPidAndVersionListWorkChunkJson>
			expandIdVersionsStep() {
		return new TypedPidToTypedPidAndVersionStep<>();
	}

	@Bean("bulkModifyPatchRewriteProvider")
	public BulkPatchRewriteProvider bulkPatchRewriteProvider() {
		return new BulkPatchRewriteProvider();
	}
}
