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

import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyJobAppCtx;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyOrRewriteGenerateReportStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyResourcesStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyCommonJobAppCtx;
import ca.uhn.fhir.batch2.jobs.parameters.UrlListValidator;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexUtils;
import ca.uhn.fhir.batch2.jobs.reindex.svcs.ReindexJobService;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BulkModifyCommonJobAppCtx.class})
public class ReindexV3JobAppCtx extends BaseBulkModifyJobAppCtx<ReindexJobParameters> {

	public static final int JOB_VERSION = 3;
	protected final IBatch2DaoSvc myBatch2DaoSvc;
	private final IDaoRegistry myDaoRegistry;
	protected final IJobPartitionProvider myJobPartitionProvider;
	private final ReindexJobService myReindexJobSvc;

	/**
	 * Constructor
	 */
	public ReindexV3JobAppCtx(
			IBatch2DaoSvc theBatch2DaoSvc,
			IDaoRegistry theDaoRegistry,
			IJobPartitionProvider theJobPartitionProvider,
			ReindexJobService theReindexJobSvc) {
		myBatch2DaoSvc = theBatch2DaoSvc;
		myDaoRegistry = theDaoRegistry;
		myJobPartitionProvider = theJobPartitionProvider;
		myReindexJobSvc = theReindexJobSvc;
	}

	@Bean("reindexV3JobDefinition")
	public JobDefinition<ReindexJobParameters> jobDefinition() {
		return super.buildJobDefinition();
	}

	@Override
	protected ReindexJobParametersValidatorV3 getJobParameterValidator() {
		return new ReindexJobParametersValidatorV3(
				myDaoRegistry, new UrlListValidator(ProviderConstants.OPERATION_REINDEX, myBatch2DaoSvc));
	}

	@Override
	protected Class<ReindexJobParameters> getParametersType() {
		return ReindexJobParameters.class;
	}

	@Override
	protected String getJobDescription() {
		return "Reindex one or more resources";
	}

	@Override
	protected String getJobId() {
		return ReindexUtils.JOB_REINDEX;
	}

	@Bean("reindexV3ModifyResourcesStep")
	@Override
	public BaseBulkModifyResourcesStep<ReindexJobParameters, ?> modifyResourcesStep() {
		return new ReindexV3ModifyResourcesStep(myReindexJobSvc);
	}

	@Override
	public GenerateRangeChunksStep<ReindexJobParameters> generateRangesStep() {
		return new GenerateRangeChunksStep<>(myJobPartitionProvider);
	}

	@Override
	public BaseBulkModifyOrRewriteGenerateReportStep<ReindexJobParameters> generateReportStep() {
		return new ReindexV3GenerateReportStep();
	}

	@Override
	public LoadIdsStep<ReindexJobParameters> loadIdsStep() {
		return new LoadIdsStep<>(myBatch2DaoSvc);
	}

	@Override
	protected int getJobDefinitionVersion() {
		return JOB_VERSION;
	}

	// FIXME: implement CheckPendingReindexWorkStep equivalent

}
