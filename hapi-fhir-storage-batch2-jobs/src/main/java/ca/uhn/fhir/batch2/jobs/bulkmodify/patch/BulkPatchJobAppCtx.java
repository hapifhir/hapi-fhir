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

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyOrRewriteGenerateReportStep;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BulkPatchJobAppCtx extends BaseBulkPatchJobAppCtx {

	/**
	 * Constructor
	 */
	public BulkPatchJobAppCtx(IBatch2DaoSvc theBatch2DaoSvc, FhirContext theFhirContext, IDaoRegistry theDaoRegistry) {
		super(theBatch2DaoSvc, theFhirContext, theDaoRegistry);
	}

	@Bean("bulkModifyJsonPatchJobDefinition")
	public JobDefinition<BulkPatchJobParameters> jobDefinition() {
		return super.buildJobDefinition();
	}

	@Bean("bulkModifyPatchModifyResourcesStep")
	@Override
	public BulkPatchModifyResourcesStep<BulkPatchJobParameters> modifyResourcesStep() {
		return new BulkPatchModifyResourcesStep<>(false);
	}

	@Bean("bulkModifyPatchGenerateRangesStep")
	@Override
	public GenerateRangeChunksStep<BulkPatchJobParameters> generateRangesStep() {
		return new GenerateRangeChunksStep<>();
	}

	@Bean("bulkModifyPatchGenerateReportStep")
	@Override
	public BaseBulkModifyOrRewriteGenerateReportStep<BulkPatchJobParameters> generateReportStep() {
		return new BulkPatchGenerateReportStep();
	}

	@Bean("bulkModifyPatchLoadIdsStep")
	@Override
	public LoadIdsStep<BulkPatchJobParameters> loadIdsStep() {
		return new LoadIdsStep<>(myBatch2DaoSvc);
	}

	@Bean("bulkModifyPatchProvider")
	public BulkPatchProvider bulkPatchProvider() {
		return new BulkPatchProvider();
	}
}
