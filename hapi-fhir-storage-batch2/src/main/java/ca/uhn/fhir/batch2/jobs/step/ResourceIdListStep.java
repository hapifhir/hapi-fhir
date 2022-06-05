package ca.uhn.fhir.batch2.jobs.step;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedJobParameters;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.jpa.api.pid.TypedResourcePid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ResourceIdListStep<PT extends PartitionedJobParameters, IT extends ChunkRangeJson> implements IJobStepWorker<PT, IT, ResourceIdListWorkChunkJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceIdListStep.class);

	private final IIdChunkProducer<IT> myIdChunkProducer;

	public ResourceIdListStep(IIdChunkProducer<IT> theIdChunkProducer) {
		myIdChunkProducer = theIdChunkProducer;
	}

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PT, IT> theStepExecutionDetails, @Nonnull IJobDataSink<ResourceIdListWorkChunkJson> theDataSink) throws JobExecutionFailedException {
		IT data = theStepExecutionDetails.getData();

		Date start = data.getStart();
		Date end = data.getEnd();

		ourLog.info("Beginning scan for reindex IDs in range {} to {}", start, end);

		Date nextStart = start;
		RequestPartitionId requestPartitionId = theStepExecutionDetails.getParameters().getRequestPartitionId();
		Set<TypedPidJson> idBuffer = new LinkedHashSet<>();
		long previousLastTime = 0L;
		int totalIdsFound = 0;
		int chunkCount = 0;
		while (true) {
			IResourcePidList nextChunk = myIdChunkProducer.fetchResourceIdsPage(nextStart, end, requestPartitionId, theStepExecutionDetails.getData());

			if (nextChunk.isEmpty()) {
				ourLog.info("No data returned");
				break;
			}

			ourLog.info("Found {} IDs from {} to {}", nextChunk.size(), nextStart, nextChunk.getLastDate());

			for (TypedResourcePid typedResourcePid : nextChunk.getTypedResourcePids()) {
				TypedPidJson nextId = new TypedPidJson(typedResourcePid);
				idBuffer.add(nextId);
			}

			// If we get the same last time twice in a row, we've clearly reached the end
			if (nextChunk.getLastDate().getTime() == previousLastTime) {
				ourLog.info("Matching final timestamp of {}, loading is completed", new Date(previousLastTime));
				break;
			}

			previousLastTime = nextChunk.getLastDate().getTime();
			nextStart = nextChunk.getLastDate();

			while (idBuffer.size() >= 1000) {

				List<TypedPidJson> submissionIds = new ArrayList<>();
				for (Iterator<TypedPidJson> iter = idBuffer.iterator(); iter.hasNext(); ) {
					submissionIds.add(iter.next());
					iter.remove();
					if (submissionIds.size() >= 1000) {
						break;
					}
				}

				totalIdsFound += submissionIds.size();
				chunkCount++;
				submitWorkChunk(submissionIds, theDataSink);
			}
		}

		totalIdsFound += idBuffer.size();
		chunkCount++;
		submitWorkChunk(idBuffer, theDataSink);

		ourLog.info("Submitted {} chunks with {} resource IDs", chunkCount, totalIdsFound);
		return RunOutcome.SUCCESS;
	}

	private void submitWorkChunk(Collection<TypedPidJson> theTypedPids, IJobDataSink<ResourceIdListWorkChunkJson> theDataSink) {
		if (theTypedPids.isEmpty()) {
			return;
		}
		ourLog.info("Submitting work chunk with {} IDs", theTypedPids.size());

		ResourceIdListWorkChunkJson data = new ResourceIdListWorkChunkJson(theTypedPids);
		theDataSink.accept(data);
	}
}
