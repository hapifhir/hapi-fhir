package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.PartitionedUrlChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class ReindexGenerateRangeChunksStep extends GenerateRangeChunksStep<ReindexJobParameters> {
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexGenerateRangeChunksStep.class);

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ReindexJobParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<PartitionedUrlChunkRangeJson> theDataSink) throws JobExecutionFailedException {

		ReindexJobParameters parameters = theStepExecutionDetails.getParameters();
		ourLog.info("Beginning reindex job - OptimizeStorage[{}] - ReindexSearchParameters[{}]", parameters.isOptimizeStorage(), parameters.isReindexSearchParameters());

		return super.run(theStepExecutionDetails, theDataSink);
	}
}
