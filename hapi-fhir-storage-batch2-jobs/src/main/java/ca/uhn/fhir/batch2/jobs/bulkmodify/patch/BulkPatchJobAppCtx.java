package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.jobs.bulkmodify.base.BaseBulkModifyJobAppCtx;
import ca.uhn.fhir.batch2.jobs.bulkmodify.base.BulkModifyGenerateReportStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.base.BaseBulkModifyResourcesStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.common.BulkModifyJobAppCtx;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BulkModifyJobAppCtx.class})
public class BulkPatchJobAppCtx extends BaseBulkModifyJobAppCtx<BulkPatchJobParameters> {

	public static final String JOB_ID = "BULK_MODIFY_PATCH";
	private final IBatch2DaoSvc myBatch2DaoSvc;

	/**
	 * Constructor
	 */
	public BulkPatchJobAppCtx(IBatch2DaoSvc theBatch2DaoSvc) {
		myBatch2DaoSvc = theBatch2DaoSvc;
	}

	@Bean("bulkModifyJsonPatchJobDefinition")
	public JobDefinition<BulkPatchJobParameters> jobDefinition() {
		return super.buildJobDefinition();
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

	@Bean("bulkModifyPatchModifyResourcesStep")
	@Override
	public BulkPatchModificationService modifyResourcesStep() {
		return new BulkPatchModificationService();
	}

	@Bean("bulkModifyPatchGenerateRangesStep")
	@Override
	public GenerateRangeChunksStep<BulkPatchJobParameters> generateRangesStep() {
		return new GenerateRangeChunksStep<>();
	}

	@Bean("bulkModifyPatchGenerateReportStep")
	@Override
	public BulkModifyGenerateReportStep<BulkPatchJobParameters> generateReportStep() {
		return new BulkModifyGenerateReportStep<>();
	}

	@Bean("bulkModifyPatchLoadIdsStep")
	@Override
	public LoadIdsStep<BulkPatchJobParameters> loadIdsStep() {
		return new LoadIdsStep<>(myBatch2DaoSvc);
	}


}
