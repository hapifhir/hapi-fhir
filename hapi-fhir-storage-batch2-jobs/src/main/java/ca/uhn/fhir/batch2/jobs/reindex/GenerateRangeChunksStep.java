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

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.time.DateUtils.addDays;

public class GenerateRangeChunksStep implements IFirstJobStepWorker<ReindexJobParameters, ReindexChunkRange> {
	private static final Logger ourLog = LoggerFactory.getLogger(GenerateRangeChunksStep.class);

	@Autowired
	private IResourceReindexSvc myResourceReindexSvc;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ReindexJobParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<ReindexChunkRange> theDataSink) throws JobExecutionFailedException {
		ReindexJobParameters params = theStepExecutionDetails.getParameters();
		String resourceType = null;
		String url = defaultIfBlank(params.getUrl(), null);

		if (url != null) {
			resourceType = url.substring(0, url.indexOf('?'));
		}

		Date start = myResourceReindexSvc.getOldestTimestamp(resourceType);
		Date end = new Date();

		ourLog.info("Initiating reindex of {} from {}", url != null ? url : "All Resources", start);

		for (Date nextStart = start; nextStart.before(end); nextStart = addDays(nextStart, 1)) {

			ReindexChunkRange nextRange = new ReindexChunkRange();
			nextRange.setUrl(url);
			nextRange.setStart(nextStart);
			nextRange.setEnd(addDays(nextStart, 1));
			theDataSink.accept(nextRange);
		}

		return RunOutcome.SUCCESS;
	}

}
