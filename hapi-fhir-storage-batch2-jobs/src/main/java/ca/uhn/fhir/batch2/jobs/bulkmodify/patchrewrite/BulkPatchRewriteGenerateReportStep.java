package ca.uhn.fhir.batch2.jobs.bulkmodify.patchrewrite;

import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyOrRewriteGenerateReportStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesResultsJson;
import jakarta.annotation.Nonnull;

public class BulkPatchRewriteGenerateReportStep
		extends BaseBulkModifyOrRewriteGenerateReportStep<BulkPatchRewriteJobParameters> {

	@Nonnull
	@Override
	protected String provideJobName() {
		return "Bulk Patch (Rewrite History)";
	}

	@Override
	public IReductionStepWorker<
					BulkPatchRewriteJobParameters, BulkModifyResourcesChunkOutcomeJson, BulkModifyResourcesResultsJson>
			newInstance() {
		return new BulkPatchRewriteGenerateReportStep();
	}
}
