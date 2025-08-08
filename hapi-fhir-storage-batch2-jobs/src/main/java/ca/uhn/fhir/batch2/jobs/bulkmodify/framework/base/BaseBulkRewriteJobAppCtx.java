package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base;

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.TypedPidToTypedPidAndVersionStep;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is the base AppCtx for bulk rewrite jobs (jobs that rewrite history across all
 * resources or a selection of resources)
 */
public abstract class BaseBulkRewriteJobAppCtx<T extends BaseBulkModifyJobParameters> extends BaseBulkModifyJobAppCtx<T> {

	@Autowired
	private TypedPidToTypedPidAndVersionStep myTypedPidToTypedPidAndVersionStep;

	/**
	 * This step takes in collections of {@link ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson}
	 * objects, fetches all of the assocated versions for each resource, and submits
	 * collections of {@link ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionJson} objects.
	 */
	@Override
	protected IJobStepWorker<T, ResourceIdListWorkChunkJson, TypedPidAndVersionListWorkChunkJson> expandIdVersionsStep() {
		return myTypedPidToTypedPidAndVersionStep;
	}

}
