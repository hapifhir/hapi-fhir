package ca.uhn.fhir.jpa.dao.method;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.MatchResourceUrlService;
import ca.uhn.fhir.jpa.delete.DeleteConflictService;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Scope("prototype")
public class ResourceDeleter<T extends IBaseResource> extends BaseMethodService<T> {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceDeleter.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	private DeleteConflictService myDeleteConflictService;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private MatchResourceUrlService myMatchResourceUrlService;

	protected ResourceDeleter(BaseHapiFhirResourceDao<T> theDao) {
		super(theDao);
	}

	public DaoMethodOutcome delete(IIdType theId) {
		return delete(theId, null);
	}

	public DaoMethodOutcome delete(IIdType theId, RequestDetails theRequestDetails) {
		validateIdPresentForDelete(theId);
		validateDeleteEnabled();

		return myTransactionService.execute(theRequestDetails, tx -> {
			DeleteConflictList deleteConflicts = new DeleteConflictList();
			if (isNotBlank(theId.getValue())) {
				deleteConflicts.setResourceIdMarkedForDeletion(theId);
			}

			StopWatch w = new StopWatch();

			DaoMethodOutcome retVal = delete(theId, deleteConflicts, theRequestDetails, new TransactionDetails());

			DeleteConflictService.validateDeleteConflictsEmptyOrThrowException(myFhirContext, deleteConflicts);

			ourLog.debug("Processed delete on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
			return retVal;
		});
	}

	public DaoMethodOutcome delete(IIdType theId, DeleteConflictList theDeleteConflicts, RequestDetails theRequestDetails, TransactionDetails theTransactionDetails) {
		validateIdPresentForDelete(theId);
		validateDeleteEnabled();

		final ResourceTable entity = myDao.readEntityLatestVersion(theId, theRequestDetails);
		if (theId.hasVersionIdPart() && Long.parseLong(theId.getVersionIdPart()) != entity.getVersion()) {
			throw new ResourceVersionConflictException("Trying to delete " + theId + " but this is not the current version");
		}

		// Don't delete again if it's already deleted
		if (entity.getDeleted() != null) {
			DaoMethodOutcome outcome = new DaoMethodOutcome();
			outcome.setEntity(entity);

			IIdType id = myFhirContext.getVersion().newIdType();
			id.setValue(entity.getIdDt().getValue());
			outcome.setId(id);

			IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(myFhirContext);
			String message = myFhirContext.getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulDeletes", 1, 0);
			String severity = "information";
			String code = "informational";
			OperationOutcomeUtil.addIssue(myFhirContext, oo, severity, message, null, code);
			outcome.setOperationOutcome(oo);

			return outcome;
		}

		StopWatch w = new StopWatch();

		T resourceToDelete = myDao.toResource(getResourceType(), entity, null, false);
		theDeleteConflicts.setResourceIdMarkedForDeletion(theId);

		// Notify IServerOperationInterceptors about pre-action call
		HookParams hook = new HookParams()
			.add(IBaseResource.class, resourceToDelete)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
			.add(TransactionDetails.class, theTransactionDetails);
		doCallHooks(theRequestDetails, Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED, hook);

		myDeleteConflictService.validateOkToDelete(theDeleteConflicts, entity, false, theRequestDetails, theTransactionDetails);

		myDao.preDelete(resourceToDelete, entity);

		// Notify interceptors
		if (theRequestDetails != null) {
			IServerInterceptor.ActionRequestDetails requestDetails = new IServerInterceptor.ActionRequestDetails(theRequestDetails, myFhirContext, theId.getResourceType(), theId);
			myDao.notifyInterceptors(RestOperationTypeEnum.DELETE, requestDetails);
		}

		ResourceTable savedEntity = myDao.updateEntityForDelete(theRequestDetails, theTransactionDetails, entity);
		resourceToDelete.setId(entity.getIdDt());

		// Notify JPA interceptors
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void beforeCommit(boolean readOnly) {
				HookParams hookParams = new HookParams()
					.add(IBaseResource.class, resourceToDelete)
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
					.add(TransactionDetails.class, theTransactionDetails);
				doCallHooks(theRequestDetails, Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED, hookParams);
			}
		});

		DaoMethodOutcome outcome = myDao.toMethodOutcome(theRequestDetails, savedEntity, resourceToDelete).setCreated(true);

		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(myFhirContext);
		String message = myFhirContext.getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulDeletes", 1, w.getMillis());
		String severity = "information";
		String code = "informational";
		OperationOutcomeUtil.addIssue(myFhirContext, oo, severity, message, null, code);
		outcome.setOperationOutcome(oo);

		return outcome;
	}

	public DeleteMethodOutcome deleteByUrl(String theUrl, RequestDetails theRequestDetails) {
		validateDeleteEnabled();

		return myTransactionService.execute(theRequestDetails, tx -> {
			DeleteConflictList deleteConflicts = new DeleteConflictList();
			DeleteMethodOutcome outcome = deleteByUrl(theUrl, deleteConflicts, theRequestDetails);
			DeleteConflictService.validateDeleteConflictsEmptyOrThrowException(myFhirContext, deleteConflicts);
			return outcome;
		});
	}

	public DeleteMethodOutcome deleteByUrl(String theUrl, DeleteConflictList theDeleteConflicts, RequestDetails theRequestDetails) {
		validateDeleteEnabled();

		return myTransactionService.execute(theRequestDetails, tx -> doDeleteByUrl(theUrl, theDeleteConflicts, theRequestDetails));
	}


	@Nonnull
	private DeleteMethodOutcome doDeleteByUrl(String theUrl, DeleteConflictList deleteConflicts, RequestDetails theRequest) {

		Set<ResourcePersistentId> resourceIds = myMatchResourceUrlService.processMatchUrl(theUrl, getResourceType(), theRequest);
		return deletePidList(theUrl, resourceIds, deleteConflicts, theRequest);
	}

	@Nonnull
	public DeleteMethodOutcome deletePidList(String theUrl, Collection<ResourcePersistentId> theResourceIds, DeleteConflictList theDeleteConflicts, RequestDetails theRequest) {
		return doDeletePidList(theUrl, false, theResourceIds, theDeleteConflicts, theRequest);
	}

	@Nonnull
	public DeleteMethodOutcome deleteAndExpungePidList(String theUrl, Collection<ResourcePersistentId> theResourceIds, DeleteConflictList theDeleteConflicts, RequestDetails theRequest) {
		return doDeletePidList(theUrl, true, theResourceIds, theDeleteConflicts, theRequest);
	}

	@Nonnull
	private DeleteMethodOutcome doDeletePidList(String theUrl, boolean theExpunge, Collection<ResourcePersistentId> theResourceIds, DeleteConflictList theDeleteConflicts, RequestDetails theTheRequest) {
		StopWatch w = new StopWatch();
		if (theResourceIds.size() > 1) {
			if (myDaoConfig.isAllowMultipleDelete() == false) {
				throw new PreconditionFailedException(myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "DELETE", theUrl, theResourceIds.size()));
			}
		}

		TransactionDetails transactionDetails = new TransactionDetails();
		List<ResourceTable> deletedResources = new ArrayList<>();

		List<Long> pids = theResourceIds.stream().map(ResourcePersistentId::getIdAsLong).collect(Collectors.toList());
		// FIXME KHS first check for conflicts in HFJ_RES_LINK
		List<ResourceTable> entities = myResourceTableDao.findAllById(pids);
		for (ResourceTable entity : entities) {
			deletedResources.add(entity);

			T resourceToDelete = myDao.toResource(getResourceType(), entity, null, false);

			// Notify IServerOperationInterceptors about pre-action call
			HookParams hooks = new HookParams()
				.add(IBaseResource.class, resourceToDelete)
				.add(RequestDetails.class, theTheRequest)
				.addIfMatchesType(ServletRequestDetails.class, theTheRequest)
				.add(TransactionDetails.class, transactionDetails);
			doCallHooks(theTheRequest, Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED, hooks);

			myDeleteConflictService.validateOkToDelete(theDeleteConflicts, entity, false, theTheRequest, transactionDetails);

			// Notify interceptors
			IdDt idToDelete = entity.getIdDt();
			if (theTheRequest != null) {
				IServerInterceptor.ActionRequestDetails requestDetails = new IServerInterceptor.ActionRequestDetails(theTheRequest, idToDelete.getResourceType(), idToDelete);
				myDao.notifyInterceptors(RestOperationTypeEnum.DELETE, requestDetails);
			}

			// Perform delete

			myDao.updateEntityForDelete(theTheRequest, transactionDetails, entity);
			resourceToDelete.setId(entity.getIdDt());

			// Notify JPA interceptors
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void beforeCommit(boolean readOnly) {
					HookParams hookParams = new HookParams()
						.add(IBaseResource.class, resourceToDelete)
						.add(RequestDetails.class, theTheRequest)
						.addIfMatchesType(ServletRequestDetails.class, theTheRequest)
						.add(TransactionDetails.class, transactionDetails);
					doCallHooks(theTheRequest, Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED, hookParams);
				}
			});
		}

		IBaseOperationOutcome oo;
		if (deletedResources.isEmpty()) {
			oo = OperationOutcomeUtil.newInstance(myFhirContext);
			String message = myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "unableToDeleteNotFound", theUrl);
			String severity = "warning";
			String code = "not-found";
			OperationOutcomeUtil.addIssue(myFhirContext, oo, severity, message, null, code);
		} else {
			oo = OperationOutcomeUtil.newInstance(myFhirContext);
			String message = myFhirContext.getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulDeletes", deletedResources.size(), w.getMillis());
			String severity = "information";
			String code = "informational";
			OperationOutcomeUtil.addIssue(myFhirContext, oo, severity, message, null, code);
		}

		ourLog.debug("Processed delete on {} (matched {} resource(s)) in {}ms", theUrl, deletedResources.size(), w.getMillis());

		DeleteMethodOutcome retVal = new DeleteMethodOutcome();
		retVal.setDeletedEntities(deletedResources);
		retVal.setOperationOutcome(oo);
		return retVal;
	}

	private void validateDeleteEnabled() {
		if (!myDaoConfig.isDeleteEnabled()) {
			String msg = myFhirContext.getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "deleteBlockedBecauseDisabled");
			throw new PreconditionFailedException(msg);
		}
	}

	private void validateIdPresentForDelete(IIdType theId) {
		if (theId == null || !theId.hasIdPart()) {
			throw new InvalidRequestException("Can not perform delete, no ID provided");
		}
	}

	protected void doCallHooks(RequestDetails theRequestDetails, Pointcut thePointcut, HookParams theParams) {
		JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, thePointcut, theParams);
	}
}
