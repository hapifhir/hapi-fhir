package ca.uhn.fhir.jpa.dao.method;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.MatchResourceUrlService;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Scope("prototype")
public class ResourceUpdater<T extends IBaseResource> extends BaseMethodService<T> {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceUpdater.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;

	@Autowired
	private MatchResourceUrlService myMatchResourceUrlService;
	@Autowired
	private IForcedIdDao myForcedIdDao;
	
	public ResourceUpdater(BaseHapiFhirResourceDao<T> theDao) {
		super(theDao);
	}

	public DaoMethodOutcome create(T theResource, RequestDetails theRequestDetails, String theIfNoneExist, boolean thePerformIndexing, TransactionDetails theTransactionDetails, RequestDetails theRequestDetails1) {
		return myTransactionService.execute(theRequestDetails, tx -> doCreateForPost(theResource, theIfNoneExist, thePerformIndexing, theTransactionDetails, theRequestDetails));
	}

	/**
	 * Called for FHIR create (POST) operations
	 */
	private DaoMethodOutcome doCreateForPost(T theResource, String theIfNoneExist, boolean thePerformIndexing, TransactionDetails theTransactionDetails, RequestDetails theRequestDetails) {
		if (theResource == null) {
			String msg = myFhirContext.getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "missingBody");
			throw new InvalidRequestException(msg);
		}

		if (isNotBlank(theResource.getIdElement().getIdPart())) {
			if (myFhirContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
				String message = myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedId", theResource.getIdElement().getIdPart());
				throw new InvalidRequestException(message, createErrorOperationOutcome(message, "processing"));
			} else {
				// As of DSTU3, ID and version in the body should be ignored for a create/update
				theResource.setId("");
			}
		}

		if (myDaoConfig.getResourceServerIdStrategy() == DaoConfig.IdStrategyEnum.UUID) {
			theResource.setId(UUID.randomUUID().toString());
			theResource.setUserData(JpaConstants.RESOURCE_ID_SERVER_ASSIGNED, Boolean.TRUE);
		}

		RequestPartitionId requestPartitionId = myRequestPartitionHelperService.determineCreatePartitionForRequest(theRequestDetails, theResource, getResourceName());
		return doCreateForPostOrPut(theResource, theIfNoneExist, thePerformIndexing, theTransactionDetails, theRequestDetails, requestPartitionId);
	}

	/**
	 * Called both for FHIR create (POST) operations (via {@link #doCreateForPost(IBaseResource, String, boolean, TransactionDetails, RequestDetails)}
	 * as well as for FHIR update (PUT) where we're doing a create-with-client-assigned-ID (via {@link #doUpdate(IBaseResource, String, boolean, boolean, RequestDetails, TransactionDetails)}.
	 */
	private DaoMethodOutcome doCreateForPostOrPut(T theResource, String theIfNoneExist, boolean thePerformIndexing, TransactionDetails theTransactionDetails, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {
		StopWatch w = new StopWatch();

		myDao.preProcessResourceForStorage(theResource);

		ResourceTable entity = new ResourceTable();
		entity.setResourceType(toResourceName(theResource));
		entity.setPartitionId(theRequestPartitionId);

		if (isNotBlank(theIfNoneExist)) {
			Set<ResourcePersistentId> match = myMatchResourceUrlService.processMatchUrl(theIfNoneExist, getResourceType(), theRequest);
			if (match.size() > 1) {
				String msg = myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "CREATE", theIfNoneExist, match.size());
				throw new PreconditionFailedException(msg);
			} else if (match.size() == 1) {
				ResourcePersistentId pid = match.iterator().next();
				entity = myEntityManager.find(ResourceTable.class, pid.getId());
				IBaseResource resource = myDao.toResource(entity, false);
				theResource.setId(resource.getIdElement().getValue());
				return toMethodOutcome(theRequest, entity, resource).setCreated(false);
			}
		}

		boolean serverAssignedId;
		if (isNotBlank(theResource.getIdElement().getIdPart())) {
			if (theResource.getUserData(JpaConstants.RESOURCE_ID_SERVER_ASSIGNED) == Boolean.TRUE) {
				createForcedIdIfNeeded(entity, theResource.getIdElement(), true);
				serverAssignedId = true;
			} else {
				switch (myDaoConfig.getResourceClientIdStrategy()) {
					case NOT_ALLOWED:
						throw new ResourceNotFoundException(
							myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedIdNotAllowed", theResource.getIdElement().getIdPart()));
					case ALPHANUMERIC:
						if (theResource.getIdElement().isIdPartValidLong()) {
							throw new InvalidRequestException(
								myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedNumericId", theResource.getIdElement().getIdPart()));
						}
						createForcedIdIfNeeded(entity, theResource.getIdElement(), false);
						break;
					case ANY:
						createForcedIdIfNeeded(entity, theResource.getIdElement(), true);
						break;
				}
				serverAssignedId = false;
			}
		} else {
			serverAssignedId = true;
		}

		// Notify interceptors
		if (theRequest != null) {
			IServerInterceptor.ActionRequestDetails requestDetails = new IServerInterceptor.ActionRequestDetails(theRequest, myFhirContext, theResource);
			myDao.notifyInterceptors(RestOperationTypeEnum.CREATE, requestDetails);
		}

		// Interceptor call: STORAGE_PRESTORAGE_RESOURCE_CREATED
		HookParams hookParams = new HookParams()
			.add(IBaseResource.class, theResource)
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest)
			.add(TransactionDetails.class, theTransactionDetails);
		myDao.doCallHooks(theTransactionDetails, theRequest, Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, hookParams);

		// Perform actual DB update
		ResourceTable updatedEntity = myDao.updateEntity(theRequest, theResource, entity, null, thePerformIndexing, thePerformIndexing, theTransactionDetails, false, thePerformIndexing);

		theResource.setId(entity.getIdDt());
		if (serverAssignedId) {
			switch (myDaoConfig.getResourceClientIdStrategy()) {
				case NOT_ALLOWED:
				case ALPHANUMERIC:
					break;
				case ANY:
					ForcedId forcedId = createForcedIdIfNeeded(updatedEntity, theResource.getIdElement(), true);
					if (forcedId != null) {
						myForcedIdDao.save(forcedId);
					}
					break;
			}
		}

		/*
		 * If we aren't indexing (meaning we're probably executing a sub-operation within a transaction),
		 * we'll manually increase the version. This is important because we want the updated version number
		 * to be reflected in the resource shared with interceptors
		 */
		if (!thePerformIndexing) {
			myDao.incrementId(theResource, entity, theResource.getIdElement());
		}

		// Update the version/last updated in the resource so that interceptors get
		// the correct version
		// FIXME KHS why is this in basehapifhirdao and not basehapifhirresourcedao?
		myDao.updateResourceMetadata(entity, theResource);

		// Populate the PID in the resource so it is available to hooks
		myDao.addPidToResource(entity, theResource);

		// Notify JPA interceptors
		if (!updatedEntity.isUnchangedInCurrentOperation()) {
			hookParams = new HookParams()
				.add(IBaseResource.class, theResource)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest)
				.add(TransactionDetails.class, theTransactionDetails);
			myDao.doCallHooks(theTransactionDetails, theRequest, Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED, hookParams);
		}

		DaoMethodOutcome outcome = toMethodOutcome(theRequest, entity, theResource).setCreated(true);
		if (!thePerformIndexing) {
			outcome.setId(theResource.getIdElement());
		}

		String msg = myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "successfulCreate", outcome.getId(), w.getMillisAndRestart());
		outcome.setOperationOutcome(createInfoOperationOutcome(msg));

		ourLog.debug(msg);
		return outcome;
	}


	/**
	 * Returns the newly created forced ID. If the entity already had a forced ID, or if
	 * none was created, returns null.
	 */
	protected ForcedId createForcedIdIfNeeded(ResourceTable theEntity, IIdType theId, boolean theCreateForPureNumericIds) {
		ForcedId retVal = null;
		if (theId.isEmpty() == false && theId.hasIdPart() && theEntity.getForcedId() == null) {
			if (theCreateForPureNumericIds || !IdHelperService.isValidPid(theId)) {
				retVal = new ForcedId();
				retVal.setResourceType(theEntity.getResourceType());
				retVal.setForcedId(theId.getIdPart());
				retVal.setResource(theEntity);
				retVal.setPartitionId(theEntity.getPartitionId());
				theEntity.setForcedId(retVal);
			}
		}

		return retVal;
	}

	public DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing, boolean theForceUpdateVersion, RequestDetails theRequest, TransactionDetails theTransactionDetails) {
		if (theResource == null) {
			String msg = myFhirContext.getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "missingBody");
			throw new InvalidRequestException(msg);
		}
		assert theResource.getIdElement().hasIdPart() || isNotBlank(theMatchUrl);

		/*
		 * Resource updates will modify/update the version of the resource with the new version. This is generally helpful,
		 * but leads to issues if the transaction is rolled back and retried. So if we do a rollback, we reset the resource
		 * version to what it was.
		 */
		String id = theResource.getIdElement().getValue();
		Runnable onRollback = () -> theResource.getIdElement().setValue(id);

		// Execute the update in a retriable transasction
		return myTransactionService.execute(theRequest, tx -> doUpdate(theResource, theMatchUrl, thePerformIndexing, theForceUpdateVersion, theRequest, theTransactionDetails), onRollback);
	}

	private DaoMethodOutcome doUpdate(T theResource, String theMatchUrl, boolean thePerformIndexing, boolean theForceUpdateVersion, RequestDetails theRequest, TransactionDetails theTransactionDetails) {
		StopWatch w = new StopWatch();

		T resource = theResource;

		myDao.preProcessResourceForStorage(resource);

		final ResourceTable entity;

		IIdType resourceId;
		if (isNotBlank(theMatchUrl)) {
			Set<ResourcePersistentId> match = myMatchResourceUrlService.processMatchUrl(theMatchUrl, getResourceType(), theRequest);
			if (match.size() > 1) {
				String msg = myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "UPDATE", theMatchUrl, match.size());
				throw new PreconditionFailedException(msg);
			} else if (match.size() == 1) {
				ResourcePersistentId pid = match.iterator().next();
				entity = myEntityManager.find(ResourceTable.class, pid.getId());
				resourceId = entity.getIdDt();
			} else {
				return create(resource, null, thePerformIndexing, theTransactionDetails, theRequest);
			}
		} else {
			/*
			 * Note: resourceId will not be null or empty here, because we
			 * check it and reject requests in
			 * BaseOutcomeReturningMethodBindingWithResourceParam
			 */
			resourceId = theResource.getIdElement();
			assert resourceId != null;
			assert resourceId.hasIdPart();

			RequestPartitionId requestPartitionId = myRequestPartitionHelperService.determineReadPartitionForRequest(theRequest, getResourceName());
			try {
				entity = myDao.readEntityLatestVersion(resourceId, requestPartitionId);
			} catch (ResourceNotFoundException e) {
				requestPartitionId = myRequestPartitionHelperService.determineCreatePartitionForRequest(theRequest, theResource, getResourceName());
				return doCreateForPostOrPut(resource, null, thePerformIndexing, theTransactionDetails, theRequest, requestPartitionId);
			}
		}

		if (resourceId.hasVersionIdPart() && Long.parseLong(resourceId.getVersionIdPart()) != entity.getVersion()) {
			throw new ResourceVersionConflictException("Trying to update " + resourceId + " but this is not the current version");
		}

		if (resourceId.hasResourceType() && !resourceId.getResourceType().equals(getResourceName())) {
			throw new UnprocessableEntityException(
				"Invalid resource ID[" + entity.getIdDt().toUnqualifiedVersionless() + "] of type[" + entity.getResourceType() + "] - Does not match expected [" + getResourceName() + "]");
		}

		IBaseResource oldResource = myDao.toResource(entity, false);

		/*
		 * Mark the entity as not deleted - This is also done in the actual updateInternal()
		 * method later on so it usually doesn't matter whether we do it here, but in the
		 * case of a transaction with multiple PUTs we don't get there until later so
		 * having this here means that a transaction can have a reference in one
		 * resource to another resource in the same transaction that is being
		 * un-deleted by the transaction. Wacky use case, sure. But it's real.
		 *
		 * See SystemProviderR4Test#testTransactionReSavesPreviouslyDeletedResources
		 * for a test that needs this.
		 */
		boolean wasDeleted = entity.getDeleted() != null;
		entity.setDeleted(null);

		/*
		 * If we aren't indexing, that means we're doing this inside a transaction.
		 * The transaction will do the actual storage to the database a bit later on,
		 * after placeholder IDs have been replaced, by calling {@link #updateInternal}
		 * directly. So we just bail now.
		 */
		if (!thePerformIndexing) {
			resource.setId(entity.getIdDt().getValue());
			DaoMethodOutcome outcome = toMethodOutcome(theRequest, entity, resource).setCreated(wasDeleted);
			outcome.setPreviousResource(oldResource);
			return outcome;
		}

		/*
		 * Otherwise, we're not in a transaction
		 */
		ResourceTable savedEntity = myDao.updateInternal(theRequest, resource, thePerformIndexing, theForceUpdateVersion, entity, resourceId, oldResource, theTransactionDetails);
		DaoMethodOutcome outcome = toMethodOutcome(theRequest, savedEntity, resource).setCreated(wasDeleted);

		if (!thePerformIndexing) {
			IIdType id = myFhirContext.getVersion().newIdType();
			id.setValue(entity.getIdDt().getValue());
			outcome.setId(id);
		}

		String msg = myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "successfulUpdate", outcome.getId(), w.getMillisAndRestart());
		outcome.setOperationOutcome(createInfoOperationOutcome(msg));

		ourLog.debug(msg);
		return outcome;
	}

	public DaoMethodOutcome create(T theResource, String theIfNoneExist, boolean thePerformIndexing, TransactionDetails theTransactionDetails, RequestDetails theRequestDetails) {
		return create(theResource, theRequestDetails, theIfNoneExist, thePerformIndexing, theTransactionDetails, theRequestDetails);
	}
}
