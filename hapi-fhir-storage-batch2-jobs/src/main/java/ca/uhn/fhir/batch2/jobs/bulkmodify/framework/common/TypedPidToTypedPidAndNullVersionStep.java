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
import jakarta.annotation.Nonnull;

import java.util.List;

public class TypedPidToTypedPidAndNullVersionStep<PT extends BaseBulkModifyJobParameters> implements IJobStepWorker<PT, ResourceIdListWorkChunkJson, TypedPidAndVersionListWorkChunkJson> {

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PT, ResourceIdListWorkChunkJson> theStepExecutionDetails, @Nonnull IJobDataSink<TypedPidAndVersionListWorkChunkJson> theDataSink) throws JobExecutionFailedException {
		ResourceIdListWorkChunkJson data = theStepExecutionDetails.getData();

		RequestPartitionId requestPartitionId = data.getRequestPartitionId();
		List<TypedPidAndVersionJson> pids = data.getTypedPids()
			.stream()
			.map(t->new TypedPidAndVersionJson(t.getResourceType(), t.getPartitionId(), t.getPid(), null))
			.toList();

		theDataSink.accept(new TypedPidAndVersionListWorkChunkJson(requestPartitionId, pids));

		return RunOutcome.SUCCESS;
	}

}
