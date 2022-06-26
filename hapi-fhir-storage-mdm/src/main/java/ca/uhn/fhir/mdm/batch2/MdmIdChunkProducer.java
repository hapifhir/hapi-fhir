package ca.uhn.fhir.mdm.batch2;

import ca.uhn.fhir.batch2.jobs.step.IIdChunkProducer;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.jpa.api.svc.IGoldenResourceSearchSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Date;

public class MdmIdChunkProducer implements IIdChunkProducer<MdmChunkRangeJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmIdChunkProducer.class);
	private final IGoldenResourceSearchSvc myGoldenResourceSearchSvc;

	public MdmIdChunkProducer(IGoldenResourceSearchSvc theGoldenResourceSearchSvc) {
		myGoldenResourceSearchSvc = theGoldenResourceSearchSvc;
	}

	@Override
	public IResourcePidList fetchResourceIdsPage(Date theNextStart, Date theEnd, @Nonnull Integer thePageSize, RequestPartitionId theRequestPartitionId, MdmChunkRangeJson theData) {
		String resourceType = theData.getResourceType();

		ourLog.info("Fetching golden resource ID chunk for resource type {} - Range {} - {}", resourceType, theNextStart, theEnd);

		return myGoldenResourceSearchSvc.fetchGoldenResourceIdsPage(theNextStart, theEnd, thePageSize, theRequestPartitionId, resourceType);
	}
}
