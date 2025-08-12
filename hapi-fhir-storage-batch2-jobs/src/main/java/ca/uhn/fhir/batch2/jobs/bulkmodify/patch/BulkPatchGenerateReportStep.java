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
