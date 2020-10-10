package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.xml.transform.SourceLocator;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpungeResourceService {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeResourceService.class);

	@Autowired
	PlatformTransactionManager myPlatformTransactionManager;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	public long expungeByResourcePids(List<Long> thePids) {
		if (thePids.size() <= 0) {
			return 0;
		}
		ourLog.info("Expunging all records linking to {} resources...", thePids.size());
		long deleteCount = 0;
		// To find all the FKs that need to be included here, run the following SQL in the INFORMATION_SCHEMA:
		// SELECT FKTABLE_NAME, FKCOLUMN_NAME FROM CROSS_REFERENCES WHERE PKTABLE_NAME = 'HFJ_RESOURCE'
		List<ResourcePidLink> links = new ArrayList<>();
		// FIXME KHS hook
//		links.add(new ResourcePidLink("CDH_LB_REF", "LB_RES_ID"));
//		links.add(new ResourcePidLink("CDH_LB_REF", "ROOT_RES_ID"));
//		links.add(new ResourcePidLink("CDH_LB_REF", "SUBS_RES_ID"));
//		links.add(new ResourcePidLink("CDH_LB_SUB_GROUP", "SUBS_RES_ID"));
//		links.add(new ResourcePidLink("CDH_LB_WL_SUBS", "SUBS_RES_ID"));
		links.add(new ResourcePidLink("HFJ_FORCED_ID", "RESOURCE_PID"));
		links.add(new ResourcePidLink("HFJ_IDX_CMP_STRING_UNIQ", "RES_ID"));
		links.add(new ResourcePidLink("HFJ_RES_LINK", "SRC_RESOURCE_ID"));
		links.add(new ResourcePidLink("HFJ_RES_LINK", "TARGET_RESOURCE_ID"));
		links.add(new ResourcePidLink("HFJ_RES_PARAM_PRESENT", "RES_ID"));
		links.add(new ResourcePidLink("HFJ_RES_TAG", "RES_ID"));
		links.add(new ResourcePidLink("HFJ_RES_VER", "RES_ID"));
		links.add(new ResourcePidLink("HFJ_RES_VER_PROV", "RES_PID"));
		links.add(new ResourcePidLink("HFJ_SPIDX_COORDS", "RES_ID"));
		links.add(new ResourcePidLink("HFJ_SPIDX_DATE", "RES_ID"));
		links.add(new ResourcePidLink("HFJ_SPIDX_NUMBER", "RES_ID"));
		links.add(new ResourcePidLink("HFJ_SPIDX_QUANTITY", "RES_ID"));
		links.add(new ResourcePidLink("HFJ_SPIDX_STRING", "RES_ID"));
		links.add(new ResourcePidLink("HFJ_SPIDX_TOKEN", "RES_ID"));
		links.add(new ResourcePidLink("HFJ_SPIDX_URI", "RES_ID"));
		links.add(new ResourcePidLink("HFJ_SUBSCRIPTION_STATS", "RES_ID"));
		links.add(new ResourcePidLink("MPI_LINK", "PERSON_PID"));
		links.add(new ResourcePidLink("MPI_LINK", "TARGET_PID"));
		links.add(new ResourcePidLink("NPM_PACKAGE_VER", "BINARY_RES_ID"));
		links.add(new ResourcePidLink("NPM_PACKAGE_VER_RES", "BINARY_RES_ID"));
		links.add(new ResourcePidLink("TRM_CODESYSTEM", "RES_ID"));
		links.add(new ResourcePidLink("TRM_CODESYSTEM_VER", "RES_ID"));
		links.add(new ResourcePidLink("TRM_CONCEPT_MAP", "RES_ID"));
		links.add(new ResourcePidLink("TRM_VALUESET", "RES_ID"));

		// Lastly we need to delete records from the resource table all of these other tables link to:
		links.add(new ResourcePidLink("HFJ_RESOURCE", "RES_ID"));

		TransactionTemplate txTemplate = new TransactionTemplate(myPlatformTransactionManager);
		for (ResourcePidLink link : links) {
			Integer count = txTemplate.execute(t -> delete(link, thePids));
			if (count != null && count > 0) {
				ourLog.info("Expunged {} records from {}", count, link.table);
				deleteCount += count;
			}
		}
		ourLog.info("Expunged a total of {} records", deleteCount);
		return deleteCount;
	}

	private Integer delete(ResourcePidLink theLink, List<Long> thePids) {
		// FIXME KHS chunk this
		String pids = thePids.toString().replace("[", "(").replace("]", ")");
		return myEntityManager.createNativeQuery("DELETE FROM " + theLink.table + " WHERE " + theLink.key + " IN " + pids).executeUpdate();
	}

	private static class ResourcePidLink {
		public final String table;
		public final String key;

		private ResourcePidLink(String theTable, String theKey) {
			table = theTable;
			key = theKey;
		}
	}
}
