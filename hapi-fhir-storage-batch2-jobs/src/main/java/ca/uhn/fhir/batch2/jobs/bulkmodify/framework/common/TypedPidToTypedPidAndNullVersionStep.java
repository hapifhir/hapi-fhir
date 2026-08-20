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
package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyJobParameters;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;

import java.util.List;

public class TypedPidToTypedPidAndNullVersionStep<PT extends BaseBulkModifyJobParameters>
		implements IJobStepWorker<PT, ResourceIdListWorkChunkJson, TypedPidAndVersionListWorkChunkJson> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PT, ResourceIdListWorkChunkJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<TypedPidAndVersionListWorkChunkJson> theDataSink)
			throws JobExecutionFailedException {
		ourLog.trace("Starting step " + theStepExecutionDetails.getCurrentStepId());
		ResourceIdListWorkChunkJson data = theStepExecutionDetails.getData();

		RequestPartitionId requestPartitionId = data.getRequestPartitionId();
		List<TypedPidAndVersionJson> pids = data.getTypedPids().stream()
				.map(t -> new TypedPidAndVersionJson(t.getResourceType(), t.getPartitionId(), t.getPid(), null))
				.toList();

		theDataSink.accept(new TypedPidAndVersionListWorkChunkJson(requestPartitionId, pids));

		return RunOutcome.SUCCESS;
	}
}
