package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.Date;

public class GenerateRangeChunksStep implements IFirstJobStepWorker<ReindexJobParameters, ReindexChunkRange> {
	private static final Logger ourLog = LoggerFactory.getLogger(GenerateRangeChunksStep.class);

	@Autowired
	private IResourceReindexSvc myResourceReindexSvc;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ReindexJobParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<ReindexChunkRange> theDataSink) throws JobExecutionFailedException {
		ReindexJobParameters params = theStepExecutionDetails.getParameters();
		Date end = new Date();

		if (params.getUrl().isEmpty()) {
			Date start = myResourceReindexSvc.getOldestTimestamp(null);
			ourLog.info("Initiating reindex of All Resources from {} to {}", start, end);
			ReindexChunkRange nextRange = new ReindexChunkRange();
			nextRange.setStart(start);
			nextRange.setEnd(end);
			theDataSink.accept(nextRange);
		} else {
			for (String nextUrl : params.getUrl()) {
				String nextResourceType = nextUrl.substring(0, nextUrl.indexOf('?'));
				Date start = myResourceReindexSvc.getOldestTimestamp(nextResourceType);
				ourLog.info("Initiating reindex of [{}]] from {} to {}", nextUrl, start, end);
				ReindexChunkRange nextRange = new ReindexChunkRange();
				nextRange.setUrl(nextUrl);
				nextRange.setStart(start);
				nextRange.setEnd(end);
				theDataSink.accept(nextRange);
			}
		}

		return RunOutcome.SUCCESS;
	}

}
