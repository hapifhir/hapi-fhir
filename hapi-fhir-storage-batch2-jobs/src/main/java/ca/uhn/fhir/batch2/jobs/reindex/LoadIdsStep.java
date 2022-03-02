package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LoadIdsStep implements IJobStepWorker<ReindexJobParameters, ReindexChunkRange, ReindexChunkIds> {
	private static final Logger ourLog = LoggerFactory.getLogger(LoadIdsStep.class);

	@Autowired
	private IResourceReindexSvc myResourceReindexSvc;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ReindexJobParameters, ReindexChunkRange> theStepExecutionDetails, @Nonnull IJobDataSink<ReindexChunkIds> theDataSink) throws JobExecutionFailedException {

		ReindexChunkRange data = theStepExecutionDetails.getData();

		Date start = data.getStart();
		Date end = data.getEnd();

		ourLog.info("Beginning scan for reindex IDs in range {} to {}", start, end);

		Date nextStart = start;
		Set<ReindexChunkIds.Id> idBuffer = new HashSet<>();
		while(true) {
			IResourceReindexSvc.IdChunk nextChunk = myResourceReindexSvc.fetchResourceIdsPage(nextStart, end);
			if (nextChunk.getIds().isEmpty()) {
				break;
			}
			if (nextChunk.getLastDate().getTime() == nextStart.getTime()) {
				break;
			}

			for (int i = 0; i < nextChunk.getIds().size(); i++) {
				ReindexChunkIds.Id nextId = new ReindexChunkIds.Id();
				nextId.setResourceType(nextChunk.getResourceTypes().get(i));
				nextId.setId(nextChunk.getIds().get(i).getId().toString());
				idBuffer.add(nextId);
			}

			nextStart = nextChunk.getLastDate();

			if (idBuffer.size() >= 1000) {

				List<ReindexChunkIds.Id> submissionIds = new ArrayList<>();
				for (Iterator<ReindexChunkIds.Id> iter = idBuffer.iterator(); iter.hasNext(); ) {
					submissionIds.add(iter.next());
					iter.remove();
					if (submissionIds.size() >= 1000) {
						break;
					}
				}

				submitWorkChunk(submissionIds, theDataSink);
			}
		}

		submitWorkChunk(idBuffer, theDataSink);

		return RunOutcome.SUCCESS;
	}

	private void submitWorkChunk(Collection<ReindexChunkIds.Id> theIdBuffer, IJobDataSink<ReindexChunkIds> theDataSink) {
		if (theIdBuffer.isEmpty()) {
			return;
		}
		ReindexChunkIds data = new ReindexChunkIds();
		data.getIds().addAll(theIdBuffer);
		theDataSink.accept(data);
	}

}
