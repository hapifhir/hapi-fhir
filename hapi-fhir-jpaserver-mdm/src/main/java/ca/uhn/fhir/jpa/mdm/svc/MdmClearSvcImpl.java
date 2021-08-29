package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.mdm.api.IMdmClearSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MdmClearSvcImpl implements IMdmClearSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmClearSvcImpl.class);

	@Autowired
	MdmLinkDaoSvc myMdmLinkDaoSvc;
	@Autowired
	MdmGoldenResourceDeletingSvc myMdmGoldenResourceDeletingSvc;

	@Override
	public void removeLinkAndGoldenResources(List<Long> thePidList) {
		List<Long> goldenResourcePids = myMdmLinkDaoSvc.deleteAllMdmLinksAndReturnGoldenResourcePids();
		DeleteMethodOutcome deleteOutcome = myMdmGoldenResourceDeletingSvc.expungeGoldenResourcePids(goldenResourcePids, null, new SystemRequestDetails());
		ourLog.debug("Removed {} MDM links and expunged {} Golden resources.", goldenResourcePids.size(), deleteOutcome.getExpungedResourcesCount());
	}
}
