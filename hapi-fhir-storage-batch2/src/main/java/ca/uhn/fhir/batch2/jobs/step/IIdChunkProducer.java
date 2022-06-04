package ca.uhn.fhir.batch2.jobs.step;

import ca.uhn.fhir.batch2.jobs.chunk.ChunkRange;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.BatchIdChunk;

import java.util.Date;

public interface IIdChunkProducer<IT extends ChunkRange> {
	BatchIdChunk fetchResourceIdsPage(Date theNextStart, Date theEnd, RequestPartitionId theRequestPartitionId, IT theData);
}
