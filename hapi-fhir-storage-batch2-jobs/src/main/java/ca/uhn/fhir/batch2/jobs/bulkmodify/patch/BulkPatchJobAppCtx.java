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

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyJobAppCtx;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyOrRewriteGenerateReportStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyCommonJobAppCtx;
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
public class BulkPatchJobAppCtx extends BaseBulkModifyJobAppCtx<BulkPatchJobParameters> {

	public static final String JOB_ID = "BULK_MODIFY_PATCH";
	public static final int JOB_VERSION = 1;
	private final IBatch2DaoSvc myBatch2DaoSvc;
	private final FhirContext myFhirContext;
	private final IDaoRegistry myDaoRegistry;

	/**
	 * Constructor
	 */
	public BulkPatchJobAppCtx(IBatch2DaoSvc theBatch2DaoSvc, FhirContext theFhirContext, IDaoRegistry theDaoRegistry) {
		myBatch2DaoSvc = theBatch2DaoSvc;
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
	}

	@Bean("bulkModifyJsonPatchJobDefinition")
	public JobDefinition<BulkPatchJobParameters> jobDefinition() {
		return super.buildJobDefinition();
	}

	@Override
	protected BulkPatchJobParametersValidator<BulkPatchJobParameters> getJobParameterValidator() {
		return new BulkPatchJobParametersValidator<>(myFhirContext, myDaoRegistry);
	}

	@Override
	protected Class<BulkPatchJobParameters> getParametersType() {
		return BulkPatchJobParameters.class;
	}

	@Override
	protected String getJobDescription() {
		return "Apply a patch to a collection of resources";
	}

	@Override
	protected String getJobId() {
		return JOB_ID;
	}

	@Override
	protected int getJobDefinitionVersion() {
		return JOB_VERSION;
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
