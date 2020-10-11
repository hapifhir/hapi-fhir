package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExpungeResourceService {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeResourceService.class);

	@Autowired
	PlatformTransactionManager myPlatformTransactionManager;
	@Autowired
	PartitionRunner myPartitionRunner;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	public DeleteMethodOutcome expungeByResourcePids(Slice<Long> thePids) {
		if (thePids.isEmpty()) {
			return new DeleteMethodOutcome();
		}
		ourLog.info("Expunging all records linking to {} resources...", thePids.getNumber());
		List<ResourceForeignKey> resourceForeignKeys = getResourceForeignKeys();

		AtomicInteger expungedEntitiesCount = new AtomicInteger();
		for (ResourceForeignKey resourceForeignKey : resourceForeignKeys) {
			myPartitionRunner.runInPartitionedThreads(thePids, pidChunk -> deleteInTransaction(resourceForeignKey, pidChunk, expungedEntitiesCount));
		}

		// Lastly we need to delete records from the resource table all of these other tables link to:
		ResourceForeignKey resourceTablePk = new ResourceForeignKey("HFJ_RESOURCE", "RES_ID");
		AtomicInteger expungedResourcesCount = new AtomicInteger();
		myPartitionRunner.runInPartitionedThreads(thePids, pidChunk -> deleteInTransaction(resourceTablePk, pidChunk, expungedResourcesCount));
		expungedEntitiesCount.addAndGet(expungedResourcesCount.get());

		ourLog.info("Expunged a total of {} records", expungedEntitiesCount);
		DeleteMethodOutcome retval = new DeleteMethodOutcome();
		retval.setExpungedResourcesCount(expungedResourcesCount.get());
		retval.setExpungedEntitiesCount(expungedEntitiesCount.get());
		return retval;
	}

	@Nonnull
	protected List<ResourceForeignKey> getResourceForeignKeys() {
		// To find all the FKs that need to be included here, run the following SQL in the INFORMATION_SCHEMA:
		// SELECT FKTABLE_NAME, FKCOLUMN_NAME FROM CROSS_REFERENCES WHERE PKTABLE_NAME = 'HFJ_RESOURCE'
		List<ResourceForeignKey> resourceForeignKeys = new ArrayList<>();
		// FIXME KHS hook
//		resourcePidLinks.add(new ResourcePidLink("CDH_LB_REF", "LB_RES_ID"));
//		resourcePidLinks.add(new ResourcePidLink("CDH_LB_REF", "ROOT_RES_ID"));
//		resourcePidLinks.add(new ResourcePidLink("CDH_LB_REF", "SUBS_RES_ID"));
//		resourcePidLinks.add(new ResourcePidLink("CDH_LB_SUB_GROUP", "SUBS_RES_ID"));
//		resourcePidLinks.add(new ResourcePidLink("CDH_LB_WL_SUBS", "SUBS_RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_FORCED_ID", "RESOURCE_PID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_IDX_CMP_STRING_UNIQ", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_RES_LINK", "SRC_RESOURCE_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_RES_LINK", "TARGET_RESOURCE_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_RES_PARAM_PRESENT", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_RES_TAG", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_RES_VER", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_RES_VER_PROV", "RES_PID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SPIDX_COORDS", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SPIDX_DATE", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SPIDX_NUMBER", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SPIDX_QUANTITY", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SPIDX_STRING", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SPIDX_TOKEN", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SPIDX_URI", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SUBSCRIPTION_STATS", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("MPI_LINK", "PERSON_PID"));
		resourceForeignKeys.add(new ResourceForeignKey("MPI_LINK", "TARGET_PID"));
		resourceForeignKeys.add(new ResourceForeignKey("NPM_PACKAGE_VER", "BINARY_RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("NPM_PACKAGE_VER_RES", "BINARY_RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("TRM_CODESYSTEM", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("TRM_CODESYSTEM_VER", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("TRM_CONCEPT_MAP", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("TRM_VALUESET", "RES_ID"));

		return resourceForeignKeys;
	}

	private void deleteInTransaction(ResourceForeignKey theResourceForeignKey, List<Long> thePidChunk, AtomicInteger theACount) {
		TransactionTemplate txTemplate = new TransactionTemplate(myPlatformTransactionManager);

		Integer count = txTemplate.execute(t -> delete(theResourceForeignKey, thePidChunk));
		if (count != null) {
			theACount.addAndGet(count);
		}
	}

	private Integer delete(ResourceForeignKey resourceForeignKey, List<Long> thePids) {
		String pids = thePids.toString().replace("[", "(").replace("]", ")");
		int retval = myEntityManager.createNativeQuery("DELETE FROM " + resourceForeignKey.table + " WHERE " + resourceForeignKey.key + " IN " + pids).executeUpdate();
		ourLog.info("Expunged {} records from {}", retval, resourceForeignKey.table);
		return retval;
	}

	private static class ResourceForeignKey {
		public final String table;
		public final String key;

		private ResourceForeignKey(String theTable, String theKey) {
			table = theTable;
			key = theKey;
		}
	}
}
