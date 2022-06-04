package ca.uhn.fhir.batch2.jobs.mdm;

import ca.uhn.fhir.batch2.jobs.step.IIdChunkProducer;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.BatchIdChunk;
import ca.uhn.fhir.jpa.api.svc.IGoldenResourceSearchSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class MdmIdChunkProducer implements IIdChunkProducer<MdmChunkRange> {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmIdChunkProducer.class);
	private final IGoldenResourceSearchSvc myGoldenResourceSearchSvc;

	public MdmIdChunkProducer(IGoldenResourceSearchSvc theGoldenResourceSearchSvc) {
		myGoldenResourceSearchSvc = theGoldenResourceSearchSvc;
	}

	@Override
	public BatchIdChunk fetchResourceIdsPage(Date theNextStart, Date theEnd, RequestPartitionId theRequestPartitionId, MdmChunkRange theData) {
		String resourceType = theData.getResourceType();

		ourLog.info("Fetching golden resource ID chunk for resource type {} - Range {} - {}", resourceType, theNextStart, theEnd);

		return myGoldenResourceSearchSvc.fetchGoldenResourceIdsPage(theNextStart, theEnd, theRequestPartitionId, resourceType);
	}
}
