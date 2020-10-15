package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DeleteExpungeService {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeService.class);

	@Autowired
	protected PlatformTransactionManager myPlatformTransactionManager;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;
	@Autowired
	private PartitionRunner myPartitionRunner;
	@Autowired
	private ResourceTableFKProvider myResourceTableFKProvider;
	@Autowired
	private IResourceTableDao myResourceTableDao;
	@Autowired
	private IResourceLinkDao myResourceLinkDao;
	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;

	public DeleteMethodOutcome expungeByResourcePids(String theUrl, Slice<Long> thePids, RequestDetails theRequest) {
		if (thePids.isEmpty()) {
			return new DeleteMethodOutcome();
		}

		HookParams params = new HookParams()
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest)
			.add(String.class, theUrl);
		JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_DELETE_EXPUNGE, params);

		validateOkToDeleteAndExpunge(thePids);
		ourLog.info("Expunging all records linking to {} resources...", thePids.getNumber());
		List<ResourceForeignKey> resourceForeignKeys = myResourceTableFKProvider.getResourceForeignKeys();

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

	public void validateOkToDeleteAndExpunge(Slice<Long> theAllTargetPids) {
		List<Long> conflictSourcePids = Collections.synchronizedList(new ArrayList<>());
		myPartitionRunner.runInPartitionedThreads(theAllTargetPids, someTargetPids -> findSourcePidsWithTargetPidIn(theAllTargetPids.getContent(), someTargetPids, conflictSourcePids));

		if (conflictSourcePids.isEmpty()) {
			return;
		}

		ResourceTable firstConflict = myResourceTableDao.getOne(conflictSourcePids.get(0));
		throw new InvalidRequestException("Other resources reference the resource(s) you are trying to delete.  Aborting delete operation.  First delete conflict is " + firstConflict.getIdDt().toVersionless().getValue());
	}

	private void findSourcePidsWithTargetPidIn(List<Long> theAllTargetPids, List<Long> theSomeTargetPids, List<Long> theSourcePids) {
		// We only need to find one conflict, so if we found one already in an earlier partition run, we can skip the rest of the searches
		if (theSourcePids.isEmpty()) {
			List<Long> someSourcePids = myResourceLinkDao.findSourcePidWithTargetPidIn(theSomeTargetPids);
			// Remove sources we're planning to delete, since those conflicts don't matter
			someSourcePids.removeAll(theAllTargetPids);
			if (!someSourcePids.isEmpty()) {
				theSourcePids.addAll(someSourcePids);
			}
		}
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
}
