package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common;

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyJobParameters;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BulkModifyCommonJobAppCtx {

	private final IBatch2DaoSvc myBatch2DaoSvc;

	/**
	 * Constructor
	 */
	public BulkModifyCommonJobAppCtx(IBatch2DaoSvc theBatch2DaoSvc) {
		myBatch2DaoSvc = theBatch2DaoSvc;
	}

	/**
	 * Step 1
	 */
	@Bean("bulkModifyGenerateRangesStep")
	public GenerateRangeChunksStep<BaseBulkModifyJobParameters> generateRangesStep() {
		return new GenerateRangeChunksStep<>();
	}

	/**
	 * Step 2
	 */
	@Bean("bulkModifyLoadIdsStep")
	public LoadIdsStep<BaseBulkModifyJobParameters> loadIdsStep() {
		return new LoadIdsStep<>(myBatch2DaoSvc);
	}

	/**
	 * Step 3
	 */
	@Bean("bulkModifyExpandIdsStep")
	public TypedPidToTypedPidAndVersionStep<BaseBulkModifyJobParameters> expandIdVersionsStep() {
		return new TypedPidToTypedPidAndVersionStep<>();
	}

	/*
	 * Step 4 is provided by the specific job definition
	 */

	/**
	 * Step 5
	 */
	@Bean("bulkModifyGenerateReportStep")
	public BulkModifyGenerateReportStep generateReportStep() {
		return new BulkModifyGenerateReportStep();
	}



}
