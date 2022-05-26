package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.jobs.chunk.ChunkRange;
import ca.uhn.fhir.batch2.jobs.step.IIdChunkProducer;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import ca.uhn.fhir.jpa.api.svc.IdChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class ReindexIdChunkProducer implements IIdChunkProducer<ReindexChunkRange> {
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexIdChunkProducer.class);
	private final IResourceReindexSvc myResourceReindexSvc;

	public ReindexIdChunkProducer(IResourceReindexSvc theResourceReindexSvc) {
		myResourceReindexSvc = theResourceReindexSvc;
	}

	@Override
	public IdChunk fetchResourceIdsPage(Date theNextStart, Date theEnd, RequestPartitionId theRequestPartitionId, ReindexChunkRange theData) {
		String url = theData.getUrl();

		ourLog.info("Fetching resource ID chunk for URL {} - Range {} - {}", url, theNextStart, theEnd);
		return myResourceReindexSvc.fetchResourceIdsPage(theNextStart, theEnd, theRequestPartitionId, url);
	}
}
