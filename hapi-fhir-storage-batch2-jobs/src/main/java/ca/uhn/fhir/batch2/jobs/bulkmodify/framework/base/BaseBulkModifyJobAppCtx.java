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
package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesResultsJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.TypedPidToTypedPidAndNullVersionStep;
import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;

/**
 * Bulk modify jobs should create a Spring AppCtx class which extends this class, then
 * call {@link #buildJobDefinition()} to build a job definition bean.
 *
 * @param <T> The job parameters type
 */
public abstract class BaseBulkModifyJobAppCtx<T extends BaseBulkModifyJobParameters> {

	protected JobDefinition<T> buildJobDefinition() {
		JobDefinition.Builder<T, BulkModifyResourcesResultsJson> jobBuilder = JobDefinition.newBuilder()
				.setJobDefinitionId(getJobId())
				.setJobDescription(getJobDescription())
				.setJobDefinitionVersion(getJobDefinitionVersion())
				.gatedExecution()
				.setParametersType(getParametersType())
				.addFirstStep(
						"generate-ranges", "Generate data ranges to modify", ChunkRangeJson.class, generateRangesStep())
				.addIntermediateStep(
						"load-ids", "Load IDs of resources to modify", ResourceIdListWorkChunkJson.class, loadIdsStep())
				.addIntermediateStep(
						"expand-id-versions",
						"Generate versioned ID batches",
						TypedPidAndVersionListWorkChunkJson.class,
						expandIdVersionsStep())
				.addIntermediateStep(
						"modify-resources",
						"Modify resources",
						BulkModifyResourcesChunkOutcomeJson.class,
						modifyResourcesStep())
				.addFinalReducerStep(
						"generate-report",
						"Generate a report outlining the changes made",
						BulkModifyResourcesResultsJson.class,
						generateReportStep());

		jobBuilder.setParametersValidator(getJobParameterValidator());

		return jobBuilder.build();
	}

	/**
	 * This is intended to be overridden by {@link BaseBulkRewriteJobAppCtx}
	 * for jobs that need versioned PIDs.
	 */
	protected IJobStepWorker<T, ResourceIdListWorkChunkJson, TypedPidAndVersionListWorkChunkJson>
			expandIdVersionsStep() {
		return new TypedPidToTypedPidAndNullVersionStep<>();
	}

	protected abstract IJobParametersValidator<T> getJobParameterValidator();

	protected abstract Class<T> getParametersType();

	protected abstract int getJobDefinitionVersion();

	protected abstract String getJobDescription();

	protected abstract String getJobId();

	public abstract BaseBulkModifyResourcesStep<T, ?> modifyResourcesStep();

	public abstract GenerateRangeChunksStep<T> generateRangesStep();

	public abstract BaseBulkModifyOrRewriteGenerateReportStep<T> generateReportStep();

	public abstract LoadIdsStep<T> loadIdsStep();
}
