package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;

public class LoadIdsStep implements IJobStepWorker<ReindexJobParameters, ReindexChunkRange, ReindexChunkIds> {

	@Autowired
	private IResourceReindexSvc myResourceReindexSvc;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ReindexJobParameters, ReindexChunkRange> theStepExecutionDetails, @Nonnull IJobDataSink<ReindexChunkIds> theDataSink) throws JobExecutionFailedException {

		ReindexChunkRange data = theStepExecutionDetails.getData();

		Date start = data.getStart();
		Date end = data.getEnd();

		Date nextStart = start;
		while (true) {
			List<ResourcePersistentId> nextChunk = myResourceReindexSvc.fetchResourceIdsPage(nextStart, end);
		}

		return RunOutcome.SUCCESS;
	}

}
