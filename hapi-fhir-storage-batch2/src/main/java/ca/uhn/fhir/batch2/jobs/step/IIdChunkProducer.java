package ca.uhn.fhir.batch2.jobs.step;

import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;

import java.util.Date;

public interface IIdChunkProducer<IT extends ChunkRangeJson> {
	IResourcePidList fetchResourceIdsPage(Date theNextStart, Date theEnd, RequestPartitionId theRequestPartitionId, IT theData);
}
