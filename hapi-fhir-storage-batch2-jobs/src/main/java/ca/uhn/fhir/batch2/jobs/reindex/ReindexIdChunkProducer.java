package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.jobs.step.IIdChunkProducer;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;

public class ReindexIdChunkProducer implements IIdChunkProducer<ReindexChunkRangeJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexIdChunkProducer.class);
	private final IResourceReindexSvc myResourceReindexSvc;

	public ReindexIdChunkProducer(IResourceReindexSvc theResourceReindexSvc) {
		myResourceReindexSvc = theResourceReindexSvc;
	}

	@Override
	public IResourcePidList fetchResourceIdsPage(Date theNextStart, Date theEnd, @Nonnull Integer thePageSize, @Nullable RequestPartitionId theRequestPartitionId, ReindexChunkRangeJson theData) {
		String url = theData.getUrl();

		ourLog.info("Fetching resource ID chunk for URL {} - Range {} - {}", url, theNextStart, theEnd);
		return myResourceReindexSvc.fetchResourceIdsPage(theNextStart, theEnd, thePageSize, theRequestPartitionId, url);
	}
}
