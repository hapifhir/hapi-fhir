package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.DeleteConflict;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import java.util.Iterator;
import java.util.List;

@Service
public class DeleteConflictService {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteConflictService.class);
	public static final int MIN_QUERY_RESULT_COUNT = 1;
	public static final int RETRY_RESULT_COUNT = 60;
	public static final int MAX_RETRIES = 10;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	protected IResourceLinkDao myResourceLinkDao;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;

	public void validateOkToDelete(DeleteConflictList theDeleteConflicts, ResourceTable theEntity, boolean theForValidate) {
		DeleteConflictList newConflicts = new DeleteConflictList();
		boolean tryAgain = findAndHandleConflicts(newConflicts, theEntity, theForValidate, MIN_QUERY_RESULT_COUNT);

		int retryCount = 0;
		while (tryAgain && retryCount < MAX_RETRIES) {
			newConflicts = new DeleteConflictList();
			tryAgain = findAndHandleConflicts(newConflicts, theEntity, theForValidate, RETRY_RESULT_COUNT);
			++retryCount;
		}
		theDeleteConflicts.addAll(newConflicts);
	}

	private boolean findAndHandleConflicts(DeleteConflictList theDeleteConflicts, ResourceTable theEntity, boolean theForValidate, int theMinQueryResultCount) {
		List<ResourceLink> resultList = findConflicts(theEntity, theMinQueryResultCount);
		if (resultList.isEmpty()) {
			return false;
		}
		return handleConflicts(theDeleteConflicts, theEntity, theForValidate, resultList);
	}

	private List<ResourceLink> findConflicts(ResourceTable theEntity, int maxResults) {
		TypedQuery<ResourceLink> query = myEntityManager.createQuery("SELECT l FROM ResourceLink l WHERE l.myTargetResourcePid = :target_pid", ResourceLink.class);
		query.setParameter("target_pid", theEntity.getId());
		query.setMaxResults(maxResults);
		return query.getResultList();
	}

	private boolean handleConflicts(DeleteConflictList theDeleteConflicts, ResourceTable theEntity, boolean theForValidate, List<ResourceLink> theResultList) {
		if (!myDaoConfig.isEnforceReferentialIntegrityOnDelete() && !theForValidate) {
			ourLog.debug("Deleting {} resource dependencies which can no longer be satisfied", theResultList.size());
			myResourceLinkDao.deleteAll(theResultList);
			return false;
		}

		addConflictsToList(theDeleteConflicts, theEntity, theResultList);

		// Notify Interceptors about pre-action call
		HookParams hooks = new HookParams()
			.add(DeleteConflictList.class, theDeleteConflicts);
		return myInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRESTORAGE_DELETE_CONFLICTS, hooks);
	}

	private void addConflictsToList(DeleteConflictList theDeleteConflicts, ResourceTable theEntity, List<ResourceLink> theResultList) {
		for (ResourceLink link : theResultList) {
			IdDt targetId = theEntity.getIdDt();
			IdDt sourceId = link.getSourceResource().getIdDt();
			String sourcePath = link.getSourcePath();
			theDeleteConflicts.add(new DeleteConflict(sourceId, sourcePath, targetId));
		}
	}

	public void validateDeleteConflictsEmptyOrThrowException(DeleteConflictList theDeleteConflicts) {
		if (theDeleteConflicts.isEmpty()) {
			return;
		}

		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(myFhirContext);
		String firstMsg = null;

		Iterator<DeleteConflict> iterator = theDeleteConflicts.iterator();
		while (iterator.hasNext()) {
			DeleteConflict next = iterator.next();
			StringBuilder b = new StringBuilder();
			b.append("Unable to delete ");
			b.append(next.getTargetId().toUnqualifiedVersionless().getValue());
			b.append(" because at least one resource has a reference to this resource. First reference found was resource ");
			b.append(next.getSourceId().toUnqualifiedVersionless().getValue());
			b.append(" in path ");
			b.append(next.getSourcePath());
			String msg = b.toString();
			if (firstMsg == null) {
				firstMsg = msg;
			}
			OperationOutcomeUtil.addIssue(myFhirContext, oo, BaseHapiFhirDao.OO_SEVERITY_ERROR, msg, null, "processing");
		}

		throw new ResourceVersionConflictException(firstMsg, oo);
	}
}
