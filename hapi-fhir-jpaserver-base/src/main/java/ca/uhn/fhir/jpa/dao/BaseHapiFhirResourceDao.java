package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.jpa.dao.expunge.DeleteExpungeService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.delete.DeleteConflictService;
import ca.uhn.fhir.jpa.model.entity.BaseHasResource;
import ca.uhn.fhir.jpa.model.entity.BaseTag;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.patch.FhirPatch;
import ca.uhn.fhir.jpa.patch.JsonPatchUtils;
import ca.uhn.fhir.jpa.patch.XmlPatchUtils;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.search.cache.SearchCacheStatusEnum;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ObjectUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IInstanceValidatorModule;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ValidationOptions;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.WordUtils;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseHapiFhirResourceDao<T extends IBaseResource> extends BaseHapiFhirDao<T> implements IFhirResourceDao<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiFhirResourceDao.class);

	@Autowired
	protected PlatformTransactionManager myPlatformTransactionManager;
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired(required = false)
	protected IFulltextSearchSvc mySearchDao;
	@Autowired
	private MatchResourceUrlService myMatchResourceUrlService;
	@Autowired
	private IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperService;
	@Autowired
	private HapiTransactionService myTransactionService;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private DeleteExpungeService myDeleteExpungeService;

	private IInstanceValidatorModule myInstanceValidator;
	private String myResourceName;
	private Class<T> myResourceType;
	@Autowired
	private IRequestPartitionHelperSvc myPartitionHelperSvc;

	@Override
	@Transactional
	public void addTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm, String theLabel, RequestDetails theRequest) {
		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId, theRequest);
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}

		for (BaseTag next : new ArrayList<>(entity.getTags())) {
			if (ObjectUtil.equals(next.getTag().getTagType(), theTagType) &&
				ObjectUtil.equals(next.getTag().getSystem(), theScheme) &&
				ObjectUtil.equals(next.getTag().getCode(), theTerm)) {
				return;
			}
		}

		entity.setHasTags(true);

		TagDefinition def = getTagOrNull(TagTypeEnum.TAG, theScheme, theTerm, theLabel);
		if (def != null) {
			BaseTag newEntity = entity.addTag(def);
			if (newEntity.getTagId() == null) {
				myEntityManager.persist(newEntity);
				myEntityManager.merge(entity);
			}
		}

		ourLog.debug("Processed addTag {}/{} on {} in {}ms", theScheme, theTerm, theId, w.getMillisAndRestart());
	}

	@Override
	public DaoMethodOutcome create(final T theResource) {
		return create(theResource, null, true, new TransactionDetails(), null);
	}

	@Override
	public DaoMethodOutcome create(final T theResource, RequestDetails theRequestDetails) {
		return create(theResource, null, true, new TransactionDetails(), theRequestDetails);
	}

	@Override
	public DaoMethodOutcome create(final T theResource, String theIfNoneExist) {
		return create(theResource, theIfNoneExist, null);
	}

	@Override
	public DaoMethodOutcome create(final T theResource, String theIfNoneExist, RequestDetails theRequestDetails) {
		return create(theResource, theIfNoneExist, true, new TransactionDetails(), theRequestDetails);
	}

	@Override
	public DaoMethodOutcome create(T theResource, String theIfNoneExist, boolean thePerformIndexing, @Nonnull TransactionDetails theTransactionDetails, RequestDetails theRequestDetails) {
		return myTransactionService.execute(theRequestDetails, tx -> doCreateForPost(theResource, theIfNoneExist, thePerformIndexing, theTransactionDetails, theRequestDetails));
	}

	/**
	 * Called for FHIR create (POST) operations
	 */
	private DaoMethodOutcome doCreateForPost(T theResource, String theIfNoneExist, boolean thePerformIndexing, TransactionDetails theTransactionDetails, RequestDetails theRequestDetails) {
		if (theResource == null) {
			String msg = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "missingBody");
			throw new InvalidRequestException(msg);
		}

		if (isNotBlank(theResource.getIdElement().getIdPart())) {
			if (getContext().getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
				String message = getContext().getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedId", theResource.getIdElement().getIdPart());
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

		preProcessResourceForStorage(theResource);

		ResourceTable entity = new ResourceTable();
		entity.setResourceType(toResourceName(theResource));
		entity.setPartitionId(theRequestPartitionId);

		if (isNotBlank(theIfNoneExist)) {
			Set<ResourcePersistentId> match = myMatchResourceUrlService.processMatchUrl(theIfNoneExist, myResourceType, theRequest);
			if (match.size() > 1) {
				String msg = getContext().getLocalizer().getMessageSanitized(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "CREATE", theIfNoneExist, match.size());
				throw new PreconditionFailedException(msg);
			} else if (match.size() == 1) {
				ResourcePersistentId pid = match.iterator().next();
				entity = myEntityManager.find(ResourceTable.class, pid.getId());
				IBaseResource resource = toResource(entity, false);
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
							getContext().getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedIdNotAllowed", theResource.getIdElement().getIdPart()));
					case ALPHANUMERIC:
						if (theResource.getIdElement().isIdPartValidLong()) {
							throw new InvalidRequestException(
								getContext().getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedNumericId", theResource.getIdElement().getIdPart()));
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
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequest, getContext(), theResource);
			notifyInterceptors(RestOperationTypeEnum.CREATE, requestDetails);
		}

		// Interceptor call: STORAGE_PRESTORAGE_RESOURCE_CREATED
		HookParams hookParams = new HookParams()
			.add(IBaseResource.class, theResource)
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest)
			.add(TransactionDetails.class, theTransactionDetails);
		doCallHooks(theTransactionDetails, theRequest, Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, hookParams);

		// Perform actual DB update
		ResourceTable updatedEntity = updateEntity(theRequest, theResource, entity, null, thePerformIndexing, thePerformIndexing, theTransactionDetails, false, thePerformIndexing);

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
			incrementId(theResource, entity, theResource.getIdElement());
		}

		// Update the version/last updated in the resource so that interceptors get
		// the correct version
		updateResourceMetadata(entity, theResource);

		// Populate the PID in the resource so it is available to hooks
		addPidToResource(entity, theResource);

		// Notify JPA interceptors
		if (!updatedEntity.isUnchangedInCurrentOperation()) {
			hookParams = new HookParams()
				.add(IBaseResource.class, theResource)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest)
				.add(TransactionDetails.class, theTransactionDetails);
			doCallHooks(theTransactionDetails, theRequest, Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED, hookParams);
		}

		DaoMethodOutcome outcome = toMethodOutcome(theRequest, entity, theResource).setCreated(true);
		if (!thePerformIndexing) {
			outcome.setId(theResource.getIdElement());
		}

		String msg = getContext().getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "successfulCreate", outcome.getId(), w.getMillisAndRestart());
		outcome.setOperationOutcome(createInfoOperationOutcome(msg));

		ourLog.debug(msg);
		return outcome;
	}

	private IInstanceValidatorModule getInstanceValidator() {
		return myInstanceValidator;
	}

	@Override
	public DaoMethodOutcome delete(IIdType theId) {
		return delete(theId, null);
	}

	@Override
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

			DeleteConflictService.validateDeleteConflictsEmptyOrThrowException(getContext(), deleteConflicts);

			ourLog.debug("Processed delete on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
			return retVal;
		});
	}

	@Override
	public DaoMethodOutcome delete(IIdType theId, DeleteConflictList theDeleteConflicts, RequestDetails theRequestDetails, @Nonnull TransactionDetails theTransactionDetails) {
		validateIdPresentForDelete(theId);
		validateDeleteEnabled();

		final ResourceTable entity = readEntityLatestVersion(theId, theRequestDetails);
		if (theId.hasVersionIdPart() && Long.parseLong(theId.getVersionIdPart()) != entity.getVersion()) {
			throw new ResourceVersionConflictException("Trying to delete " + theId + " but this is not the current version");
		}

		// Don't delete again if it's already deleted
		if (entity.getDeleted() != null) {
			DaoMethodOutcome outcome = new DaoMethodOutcome();
			outcome.setEntity(entity);

			IIdType id = getContext().getVersion().newIdType();
			id.setValue(entity.getIdDt().getValue());
			outcome.setId(id);

			IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(getContext());
			String message = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulDeletes", 1, 0);
			String severity = "information";
			String code = "informational";
			OperationOutcomeUtil.addIssue(getContext(), oo, severity, message, null, code);
			outcome.setOperationOutcome(oo);

			return outcome;
		}

		StopWatch w = new StopWatch();

		T resourceToDelete = toResource(myResourceType, entity, null, false);
		theDeleteConflicts.setResourceIdMarkedForDeletion(theId);

		// Notify IServerOperationInterceptors about pre-action call
		HookParams hook = new HookParams()
			.add(IBaseResource.class, resourceToDelete)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
			.add(TransactionDetails.class, theTransactionDetails);
		doCallHooks(theTransactionDetails, theRequestDetails, Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED, hook);

		myDeleteConflictService.validateOkToDelete(theDeleteConflicts, entity, false, theRequestDetails, theTransactionDetails);

		preDelete(resourceToDelete, entity);

		// Notify interceptors
		if (theRequestDetails != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getContext(), theId.getResourceType(), theId);
			notifyInterceptors(RestOperationTypeEnum.DELETE, requestDetails);
		}

		ResourceTable savedEntity = updateEntityForDelete(theRequestDetails, theTransactionDetails, entity);
		resourceToDelete.setId(entity.getIdDt());

		// Notify JPA interceptors
		HookParams hookParams = new HookParams()
			.add(IBaseResource.class, resourceToDelete)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
			.add(TransactionDetails.class, theTransactionDetails);

		if (theTransactionDetails.isAcceptingDeferredInterceptorBroadcasts()) {
			theTransactionDetails.addDeferredInterceptorBroadcast(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED, hookParams);
		} else {
			doCallHooks(theTransactionDetails, theRequestDetails, Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED, hookParams);
		}

		DaoMethodOutcome outcome = toMethodOutcome(theRequestDetails, savedEntity, resourceToDelete).setCreated(true);

		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(getContext());
		String message = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulDeletes", 1, w.getMillis());
		String severity = "information";
		String code = "informational";
		OperationOutcomeUtil.addIssue(getContext(), oo, severity, message, null, code);
		outcome.setOperationOutcome(oo);

		return outcome;
	}

	@Override
	public DeleteMethodOutcome deleteByUrl(String theUrl, RequestDetails theRequestDetails) {
		validateDeleteEnabled();

		return myTransactionService.execute(theRequestDetails, tx -> {
			DeleteConflictList deleteConflicts = new DeleteConflictList();
			DeleteMethodOutcome outcome = deleteByUrl(theUrl, deleteConflicts, theRequestDetails);
			DeleteConflictService.validateDeleteConflictsEmptyOrThrowException(getContext(), deleteConflicts);
			return outcome;
		});
	}

	/**
	 * This method gets called by {@link #deleteByUrl(String, RequestDetails)} as well as by
	 * transaction processors
	 */
	@Override
	public DeleteMethodOutcome deleteByUrl(String theUrl, DeleteConflictList deleteConflicts, RequestDetails theRequestDetails) {
		validateDeleteEnabled();

		return myTransactionService.execute(theRequestDetails, tx -> doDeleteByUrl(theUrl, deleteConflicts, theRequestDetails));
	}

	@Nonnull
	private DeleteMethodOutcome doDeleteByUrl(String theUrl, DeleteConflictList deleteConflicts, RequestDetails theRequest) {
		RuntimeResourceDefinition resourceDef = getContext().getResourceDefinition(myResourceType);
		SearchParameterMap paramMap = myMatchUrlService.translateMatchUrl(theUrl, resourceDef);
		paramMap.setLoadSynchronous(true);

		Set<ResourcePersistentId> resourceIds = myMatchResourceUrlService.search(paramMap, myResourceType, theRequest);

		if (resourceIds.size() > 1) {
			if (!myDaoConfig.isAllowMultipleDelete()) {
				throw new PreconditionFailedException(getContext().getLocalizer().getMessageSanitized(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "DELETE", theUrl, resourceIds.size()));
			}
		}

		if (paramMap.isDeleteExpunge()) {
			return deleteExpunge(theUrl, theRequest, resourceIds);
		} else {
			return deletePidList(theUrl, resourceIds, deleteConflicts, theRequest);
		}
	}

	private DeleteMethodOutcome deleteExpunge(String theUrl, RequestDetails theTheRequest, Set<ResourcePersistentId> theResourceIds) {
		if (!myDaoConfig.isExpungeEnabled() || !myDaoConfig.isDeleteExpungeEnabled()) {
			throw new MethodNotAllowedException("_expunge is not enabled on this server");
		}

		return myDeleteExpungeService.expungeByResourcePids(theUrl, myResourceName, new SliceImpl<>(ResourcePersistentId.toLongList(theResourceIds)), theTheRequest);
	}

	@NotNull
	@Override
	public DeleteMethodOutcome deletePidList(String theUrl, Collection<ResourcePersistentId> theResourceIds, DeleteConflictList theDeleteConflicts, RequestDetails theRequest) {
		StopWatch w = new StopWatch();
		TransactionDetails transactionDetails = new TransactionDetails();
		List<ResourceTable> deletedResources = new ArrayList<>();
		for (ResourcePersistentId pid : theResourceIds) {
			ResourceTable entity = myEntityManager.find(ResourceTable.class, pid.getId());
			deletedResources.add(entity);

			T resourceToDelete = toResource(myResourceType, entity, null, false);

			// Notify IServerOperationInterceptors about pre-action call
			HookParams hooks = new HookParams()
				.add(IBaseResource.class, resourceToDelete)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest)
				.add(TransactionDetails.class, transactionDetails);
			doCallHooks(transactionDetails, theRequest, Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED, hooks);

			myDeleteConflictService.validateOkToDelete(theDeleteConflicts, entity, false, theRequest, transactionDetails);

			// Notify interceptors
			IdDt idToDelete = entity.getIdDt();
			if (theRequest != null) {
				ActionRequestDetails requestDetails = new ActionRequestDetails(theRequest, idToDelete.getResourceType(), idToDelete);
				notifyInterceptors(RestOperationTypeEnum.DELETE, requestDetails);
			}

			// Perform delete

			updateEntityForDelete(theRequest, transactionDetails, entity);
			resourceToDelete.setId(entity.getIdDt());

			// Notify JPA interceptors
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void beforeCommit(boolean readOnly) {
					HookParams hookParams = new HookParams()
						.add(IBaseResource.class, resourceToDelete)
						.add(RequestDetails.class, theRequest)
						.addIfMatchesType(ServletRequestDetails.class, theRequest)
						.add(TransactionDetails.class, transactionDetails);
					doCallHooks(transactionDetails, theRequest, Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED, hookParams);
				}
			});
		}

		IBaseOperationOutcome oo;
		if (deletedResources.isEmpty()) {
			oo = OperationOutcomeUtil.newInstance(getContext());
			String message = getContext().getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "unableToDeleteNotFound", theUrl);
			String severity = "warning";
			String code = "not-found";
			OperationOutcomeUtil.addIssue(getContext(), oo, severity, message, null, code);
		} else {
			oo = OperationOutcomeUtil.newInstance(getContext());
			String message = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulDeletes", deletedResources.size(), w.getMillis());
			String severity = "information";
			String code = "informational";
			OperationOutcomeUtil.addIssue(getContext(), oo, severity, message, null, code);
		}

		ourLog.debug("Processed delete on {} (matched {} resource(s)) in {}ms", theUrl, deletedResources.size(), w.getMillis());

		DeleteMethodOutcome retVal = new DeleteMethodOutcome();
		retVal.setDeletedEntities(deletedResources);
		retVal.setOperationOutcome(oo);
		return retVal;
	}

	private void validateDeleteEnabled() {
		if (!myDaoConfig.isDeleteEnabled()) {
			String msg = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "deleteBlockedBecauseDisabled");
			throw new PreconditionFailedException(msg);
		}
	}

	private void validateIdPresentForDelete(IIdType theId) {
		if (theId == null || !theId.hasIdPart()) {
			throw new InvalidRequestException("Can not perform delete, no ID provided");
		}
	}

	@PostConstruct
	public void detectSearchDaoDisabled() {
		if (mySearchDao != null && mySearchDao.isDisabled()) {
			mySearchDao = null;
		}
	}


	private <MT extends IBaseMetaType> void doMetaAdd(MT theMetaAdd, BaseHasResource theEntity, RequestDetails theRequestDetails, TransactionDetails theTransactionDetails) {
		IBaseResource oldVersion = toResource(theEntity, false);

		List<TagDefinition> tags = toTagList(theMetaAdd);
		for (TagDefinition nextDef : tags) {

			boolean hasTag = false;
			for (BaseTag next : new ArrayList<>(theEntity.getTags())) {
				if (ObjectUtil.equals(next.getTag().getTagType(), nextDef.getTagType()) &&
					ObjectUtil.equals(next.getTag().getSystem(), nextDef.getSystem()) &&
					ObjectUtil.equals(next.getTag().getCode(), nextDef.getCode())) {
					hasTag = true;
					break;
				}
			}

			if (!hasTag) {
				theEntity.setHasTags(true);

				TagDefinition def = getTagOrNull(nextDef.getTagType(), nextDef.getSystem(), nextDef.getCode(), nextDef.getDisplay());
				if (def != null) {
					BaseTag newEntity = theEntity.addTag(def);
					if (newEntity.getTagId() == null) {
						myEntityManager.persist(newEntity);
					}
				}
			}
		}

		validateMetaCount(theEntity.getTags().size());

		myEntityManager.merge(theEntity);

		// Interceptor call: STORAGE_PRECOMMIT_RESOURCE_UPDATED
		// Interceptor call: STORAGE_PRESTORAGE_RESOURCE_UPDATED
		IBaseResource newVersion = toResource(theEntity, false);
		HookParams params = new HookParams()
			.add(IBaseResource.class, oldVersion)
			.add(IBaseResource.class, newVersion)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
			.add(TransactionDetails.class, theTransactionDetails);
		myInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, params);
		myInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, params);

	}

	private <MT extends IBaseMetaType> void doMetaDelete(MT theMetaDel, BaseHasResource theEntity, RequestDetails theRequestDetails, TransactionDetails theTransactionDetails) {

		IBaseResource oldVersion = toResource(theEntity, false);


		List<TagDefinition> tags = toTagList(theMetaDel);

		for (TagDefinition nextDef : tags) {
			for (BaseTag next : new ArrayList<BaseTag>(theEntity.getTags())) {
				if (ObjectUtil.equals(next.getTag().getTagType(), nextDef.getTagType()) &&
					ObjectUtil.equals(next.getTag().getSystem(), nextDef.getSystem()) &&
					ObjectUtil.equals(next.getTag().getCode(), nextDef.getCode())) {
					myEntityManager.remove(next);
					theEntity.getTags().remove(next);
				}
			}
		}

		if (theEntity.getTags().isEmpty()) {
			theEntity.setHasTags(false);
		}

		theEntity = myEntityManager.merge(theEntity);

		// Interceptor call: STORAGE_PRECOMMIT_RESOURCE_UPDATED
		IBaseResource newVersion = toResource(theEntity, false);
		HookParams params = new HookParams()
			.add(IBaseResource.class, oldVersion)
			.add(IBaseResource.class, newVersion)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
			.add(TransactionDetails.class, theTransactionDetails);
		myInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, params);
		myInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, params);

	}

	private void validateExpungeEnabled() {
		if (!myDaoConfig.isExpungeEnabled()) {
			throw new MethodNotAllowedException("$expunge is not enabled on this server");
		}
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public ExpungeOutcome expunge(IIdType theId, ExpungeOptions theExpungeOptions, RequestDetails theRequest) {
		validateExpungeEnabled();
		return forceExpungeInExistingTransaction(theId, theExpungeOptions, theRequest);
	}

	@Override
	public ExpungeOutcome forceExpungeInExistingTransaction(IIdType theId, ExpungeOptions theExpungeOptions, RequestDetails theRequest) {
		TransactionTemplate txTemplate = new TransactionTemplate(myPlatformTransactionManager);

		BaseHasResource entity = txTemplate.execute(t -> readEntity(theId, theRequest));
		Validate.notNull(entity, "Resource with ID %s not found in database", theId);

		if (theId.hasVersionIdPart()) {
			BaseHasResource currentVersion;
			currentVersion = txTemplate.execute(t -> readEntity(theId.toVersionless(), theRequest));
			Validate.notNull(currentVersion, "Current version of resource with ID %s not found in database", theId.toVersionless());

			if (entity.getVersion() == currentVersion.getVersion()) {
				throw new PreconditionFailedException("Can not perform version-specific expunge of resource " + theId.toUnqualified().getValue() + " as this is the current version");
			}

			return myExpungeService.expunge(getResourceName(), entity.getResourceId(), entity.getVersion(), theExpungeOptions, theRequest);
		}

		return myExpungeService.expunge(getResourceName(), entity.getResourceId(), null, theExpungeOptions, theRequest);
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public ExpungeOutcome expunge(ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails) {
		ourLog.info("Beginning TYPE[{}] expunge operation", getResourceName());

		return myExpungeService.expunge(getResourceName(), null, null, theExpungeOptions, theRequestDetails);
	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}


	@Override
	public Class<T> getResourceType() {
		return myResourceType;
	}

	@SuppressWarnings("unchecked")
	@Required
	public void setResourceType(Class<? extends IBaseResource> theTableType) {
		myResourceType = (Class<T>) theTableType;
	}

	@Override
	@Transactional
	public IBundleProvider history(Date theSince, Date theUntil, RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails);
		notifyInterceptors(RestOperationTypeEnum.HISTORY_TYPE, requestDetails);

		StopWatch w = new StopWatch();
		IBundleProvider retVal = super.history(theRequestDetails, myResourceName, null, theSince, theUntil);
		ourLog.debug("Processed history on {} in {}ms", myResourceName, w.getMillisAndRestart());
		return retVal;
	}

	@Override
	@Transactional
	public IBundleProvider history(final IIdType theId, final Date theSince, Date theUntil, RequestDetails theRequest) {
		if (theRequest != null) {
			// Notify interceptors
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequest, getResourceName(), theId);
			notifyInterceptors(RestOperationTypeEnum.HISTORY_INSTANCE, requestDetails);
		}

		StopWatch w = new StopWatch();

		IIdType id = theId.withResourceType(myResourceName).toUnqualifiedVersionless();
		BaseHasResource entity = readEntity(id, theRequest);

		IBundleProvider retVal = super.history(theRequest, myResourceName, entity.getId(), theSince, theUntil);

		ourLog.debug("Processed history on {} in {}ms", id, w.getMillisAndRestart());
		return retVal;
	}

	protected boolean isPagingProviderDatabaseBacked(RequestDetails theRequestDetails) {
		if (theRequestDetails == null || theRequestDetails.getServer() == null) {
			return false;
		}
		IRestfulServerDefaults server = theRequestDetails.getServer();
		IPagingProvider pagingProvider = server.getPagingProvider();
		return pagingProvider instanceof DatabaseBackedPagingProvider;
	}

	protected void markResourcesMatchingExpressionAsNeedingReindexing(Boolean theCurrentlyReindexing, String theExpression) {
		// Avoid endless loops
		if (Boolean.TRUE.equals(theCurrentlyReindexing)) {
			return;
		}

		if (myDaoConfig.isMarkResourcesForReindexingUponSearchParameterChange()) {

			String expression = defaultString(theExpression);

			Set<String> typesToMark = myDaoRegistry
				.getRegisteredDaoTypes()
				.stream()
				.filter(t -> WordUtils.containsAllWords(expression, t))
				.collect(Collectors.toSet());

			for (String resourceType : typesToMark) {
				ourLog.debug("Marking all resources of type {} for reindexing due to updated search parameter with path: {}", resourceType, theExpression);

				TransactionTemplate txTemplate = new TransactionTemplate(myPlatformTransactionManager);
				txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
				txTemplate.execute(t -> {
					myResourceReindexingSvc.markAllResourcesForReindexing(resourceType);
					return null;
				});

				ourLog.debug("Marked resources of type {} for reindexing", resourceType);
			}

		}

		mySearchParamRegistry.requestRefresh();
	}

	@Override
	@Transactional
	public <MT extends IBaseMetaType> MT metaAddOperation(IIdType theResourceId, MT theMetaAdd, RequestDetails theRequest) {
		TransactionDetails transactionDetails = new TransactionDetails();

		// Notify interceptors
		if (theRequest != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequest, getResourceName(), theResourceId);
			notifyInterceptors(RestOperationTypeEnum.META_ADD, requestDetails);
		}

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theResourceId, theRequest);
		if (entity == null) {
			throw new ResourceNotFoundException(theResourceId);
		}

		ResourceTable latestVersion = readEntityLatestVersion(theResourceId, theRequest);
		if (latestVersion.getVersion() != entity.getVersion()) {
			doMetaAdd(theMetaAdd, entity, theRequest, transactionDetails);
		} else {
			doMetaAdd(theMetaAdd, latestVersion, theRequest, transactionDetails);

			// Also update history entry
			ResourceHistoryTable history = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(entity.getId(), entity.getVersion());
			doMetaAdd(theMetaAdd, history, theRequest, transactionDetails);
		}

		ourLog.debug("Processed metaAddOperation on {} in {}ms", theResourceId, w.getMillisAndRestart());

		@SuppressWarnings("unchecked")
		MT retVal = (MT) metaGetOperation(theMetaAdd.getClass(), theResourceId, theRequest);
		return retVal;
	}

	@Override
	@Transactional
	public <MT extends IBaseMetaType> MT metaDeleteOperation(IIdType theResourceId, MT theMetaDel, RequestDetails theRequest) {
		TransactionDetails transactionDetails = new TransactionDetails();

		// Notify interceptors
		if (theRequest != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequest, getResourceName(), theResourceId);
			notifyInterceptors(RestOperationTypeEnum.META_DELETE, requestDetails);
		}

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theResourceId, theRequest);
		if (entity == null) {
			throw new ResourceNotFoundException(theResourceId);
		}

		ResourceTable latestVersion = readEntityLatestVersion(theResourceId, theRequest);
		if (latestVersion.getVersion() != entity.getVersion()) {
			doMetaDelete(theMetaDel, entity, theRequest, transactionDetails);
		} else {
			doMetaDelete(theMetaDel, latestVersion, theRequest, transactionDetails);

			// Also update history entry
			ResourceHistoryTable history = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(entity.getId(), entity.getVersion());
			doMetaDelete(theMetaDel, history, theRequest, transactionDetails);
		}

		ourLog.debug("Processed metaDeleteOperation on {} in {}ms", theResourceId.getValue(), w.getMillisAndRestart());

		@SuppressWarnings("unchecked")
		MT retVal = (MT) metaGetOperation(theMetaDel.getClass(), theResourceId, theRequest);
		return retVal;
	}

	@Override
	@Transactional
	public <MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, IIdType theId, RequestDetails theRequest) {
		// Notify interceptors
		if (theRequest != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequest, getResourceName(), theId);
			notifyInterceptors(RestOperationTypeEnum.META, requestDetails);
		}

		Set<TagDefinition> tagDefs = new HashSet<>();
		BaseHasResource entity = readEntity(theId, theRequest);
		for (BaseTag next : entity.getTags()) {
			tagDefs.add(next.getTag());
		}
		MT retVal = toMetaDt(theType, tagDefs);

		retVal.setLastUpdated(entity.getUpdatedDate());
		retVal.setVersionId(Long.toString(entity.getVersion()));

		return retVal;
	}

	@Override
	@Transactional
	public <MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, RequestDetails theRequestDetails) {
		// Notify interceptors
		if (theRequestDetails != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), null);
			notifyInterceptors(RestOperationTypeEnum.META, requestDetails);
		}

		String sql = "SELECT d FROM TagDefinition d WHERE d.myId IN (SELECT DISTINCT t.myTagId FROM ResourceTag t WHERE t.myResourceType = :res_type)";
		TypedQuery<TagDefinition> q = myEntityManager.createQuery(sql, TagDefinition.class);
		q.setParameter("res_type", myResourceName);
		List<TagDefinition> tagDefinitions = q.getResultList();

		return toMetaDt(theType, tagDefinitions);
	}

	@Override
	public DaoMethodOutcome patch(IIdType theId, String theConditionalUrl, PatchTypeEnum thePatchType, String thePatchBody, IBaseParameters theFhirPatchBody, RequestDetails theRequest) {
		return myTransactionService.execute(theRequest, tx -> doPatch(theId, theConditionalUrl, thePatchType, thePatchBody, theFhirPatchBody, theRequest));
	}

	private DaoMethodOutcome doPatch(IIdType theId, String theConditionalUrl, PatchTypeEnum thePatchType, String thePatchBody, IBaseParameters theFhirPatchBody, RequestDetails theRequest) {
		ResourceTable entityToUpdate;
		if (isNotBlank(theConditionalUrl)) {

			Set<ResourcePersistentId> match = myMatchResourceUrlService.processMatchUrl(theConditionalUrl, myResourceType, theRequest);
			if (match.size() > 1) {
				String msg = getContext().getLocalizer().getMessageSanitized(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "PATCH", theConditionalUrl, match.size());
				throw new PreconditionFailedException(msg);
			} else if (match.size() == 1) {
				ResourcePersistentId pid = match.iterator().next();
				entityToUpdate = myEntityManager.find(ResourceTable.class, pid.getId());
			} else {
				String msg = getContext().getLocalizer().getMessageSanitized(BaseHapiFhirDao.class, "invalidMatchUrlNoMatches", theConditionalUrl);
				throw new ResourceNotFoundException(msg);
			}

		} else {
			entityToUpdate = readEntityLatestVersion(theId, theRequest);
			if (theId.hasVersionIdPart()) {
				if (theId.getVersionIdPartAsLong() != entityToUpdate.getVersion()) {
					throw new ResourceVersionConflictException("Version " + theId.getVersionIdPart() + " is not the most recent version of this resource, unable to apply patch");
				}
			}
		}

		validateResourceType(entityToUpdate);

		IBaseResource resourceToUpdate = toResource(entityToUpdate, false);
		IBaseResource destination;
		switch (thePatchType) {
			case JSON_PATCH:
				destination = JsonPatchUtils.apply(getContext(), resourceToUpdate, thePatchBody);
				break;
			case XML_PATCH:
				destination = XmlPatchUtils.apply(getContext(), resourceToUpdate, thePatchBody);
				break;
			case FHIR_PATCH_XML:
			case FHIR_PATCH_JSON:
			default:
				IBaseParameters fhirPatchJson = theFhirPatchBody;
				new FhirPatch(getContext()).apply(resourceToUpdate, fhirPatchJson);
				destination = resourceToUpdate;
				break;
		}

		@SuppressWarnings("unchecked")
		T destinationCasted = (T) destination;
		return update(destinationCasted, null, true, theRequest);
	}

	@PostConstruct
	@Override
	public void start() {
		ourLog.debug("Starting resource DAO for type: {}", getResourceName());
		myInstanceValidator = getApplicationContext().getBean(IInstanceValidatorModule.class);
		super.start();
	}

	@PostConstruct
	public void postConstruct() {
		RuntimeResourceDefinition def = getContext().getResourceDefinition(myResourceType);
		myResourceName = def.getName();
	}

	/**
	 * Subclasses may override to provide behaviour. Invoked within a delete
	 * transaction with the resource that is about to be deleted.
	 */
	protected void preDelete(T theResourceToDelete, ResourceTable theEntityToDelete) {
		// nothing by default
	}

	@Override
	@Transactional
	public T readByPid(ResourcePersistentId thePid) {
		StopWatch w = new StopWatch();

		Optional<ResourceTable> entity = myResourceTableDao.findById(thePid.getIdAsLong());
		if (!entity.isPresent()) {
			throw new ResourceNotFoundException("No resource found with PID " + thePid);
		}
		if (entity.get().getDeleted() != null) {
			throw createResourceGoneException(entity.get());
		}

		T retVal = toResource(myResourceType, entity.get(), null, false);

		ourLog.debug("Processed read on {} in {}ms", thePid, w.getMillis());
		return retVal;
	}

	@Override
	public T read(IIdType theId) {
		return read(theId, null);
	}

	@Override
	public T read(IIdType theId, RequestDetails theRequestDetails) {
		return read(theId, theRequestDetails, false);
	}

	@Override
	public T read(IIdType theId, RequestDetails theRequest, boolean theDeletedOk) {
		validateResourceTypeAndThrowInvalidRequestException(theId);

		return myTransactionService.execute(theRequest, tx -> doRead(theId, theRequest, theDeletedOk));
	}

	public T doRead(IIdType theId, RequestDetails theRequest, boolean theDeletedOk) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		// Notify interceptors
		if (theRequest != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequest, getResourceName(), theId);
			RestOperationTypeEnum operationType = theId.hasVersionIdPart() ? RestOperationTypeEnum.VREAD : RestOperationTypeEnum.READ;
			notifyInterceptors(operationType, requestDetails);
		}

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId, theRequest);
		validateResourceType(entity);

		T retVal = toResource(myResourceType, entity, null, false);

		if (theDeletedOk == false) {
			if (entity.getDeleted() != null) {
				throw createResourceGoneException(entity);
			}
		}

		// Interceptor broadcast: STORAGE_PREACCESS_RESOURCES
		{
			SimplePreResourceAccessDetails accessDetails = new SimplePreResourceAccessDetails(retVal);
			HookParams params = new HookParams()
				.add(IPreResourceAccessDetails.class, accessDetails)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);
			JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);
			if (accessDetails.isDontReturnResourceAtIndex(0)) {
				throw new ResourceNotFoundException(theId);
			}
		}

		// Interceptor broadcast: STORAGE_PRESHOW_RESOURCES
		{
			SimplePreResourceShowDetails showDetails = new SimplePreResourceShowDetails(retVal);
			HookParams params = new HookParams()
				.add(IPreResourceShowDetails.class, showDetails)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);
			JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PRESHOW_RESOURCES, params);
			//noinspection unchecked
			retVal = (T) showDetails.getResource(0);
		}

		ourLog.debug("Processed read on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
		return retVal;
	}

	@Override
	@Transactional
	public BaseHasResource readEntity(IIdType theId, RequestDetails theRequest) {
		return readEntity(theId, true, theRequest);
	}

	@Override
	@Transactional
	public BaseHasResource readEntity(IIdType theId, boolean theCheckForForcedId, RequestDetails theRequest) {
		validateResourceTypeAndThrowInvalidRequestException(theId);

		RequestPartitionId requestPartitionId = myRequestPartitionHelperService.determineReadPartitionForRequest(theRequest, getResourceName());
		ResourcePersistentId pid = myIdHelperService.resolveResourcePersistentIds(requestPartitionId, getResourceName(), theId.getIdPart());
		BaseHasResource entity = myEntityManager.find(ResourceTable.class, pid.getIdAsLong());

		// Verify that the resource is for the correct partition
		if (!requestPartitionId.isAllPartitions()) {
			if (entity.getPartitionId() != null && entity.getPartitionId().getPartitionId() != null) {
				if (!requestPartitionId.hasPartitionId(entity.getPartitionId().getPartitionId())) {
					ourLog.debug("Performing a read for PartitionId={} but entity has partition: {}", requestPartitionId, entity.getPartitionId());
					entity = null;
				}
			} else {
				// Entity Partition ID is null
				if (!requestPartitionId.hasPartitionId(null)) {
					ourLog.debug("Performing a read for PartitionId=null but entity has partition: {}", entity.getPartitionId());
					entity = null;
				}
			}
		}

		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}

		if (theId.hasVersionIdPart()) {
			if (theId.isVersionIdPartValidLong() == false) {
				throw new ResourceNotFoundException(getContext().getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "invalidVersion", theId.getVersionIdPart(), theId.toUnqualifiedVersionless()));
			}
			if (entity.getVersion() != theId.getVersionIdPartAsLong()) {
				entity = null;
			}
		}

		if (entity == null) {
			if (theId.hasVersionIdPart()) {
				TypedQuery<ResourceHistoryTable> q = myEntityManager.createQuery("SELECT t from ResourceHistoryTable t WHERE t.myResourceId = :RID AND t.myResourceType = :RTYP AND t.myResourceVersion = :RVER", ResourceHistoryTable.class);
				q.setParameter("RID", pid.getId());
				q.setParameter("RTYP", myResourceName);
				q.setParameter("RVER", theId.getVersionIdPartAsLong());
				try {
					entity = q.getSingleResult();
				} catch (NoResultException e) {
					throw new ResourceNotFoundException(getContext().getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "invalidVersion", theId.getVersionIdPart(), theId.toUnqualifiedVersionless()));
				}
			}
		}

		Validate.notNull(entity);
		validateResourceType(entity);

		if (theCheckForForcedId) {
			validateGivenIdIsAppropriateToRetrieveResource(theId, entity);
		}
		return entity;
	}

	@NotNull
	protected ResourceTable readEntityLatestVersion(IIdType theId, RequestDetails theRequestDetails) {
		RequestPartitionId requestPartitionId = myRequestPartitionHelperService.determineReadPartitionForRequest(theRequestDetails, getResourceName());
		return readEntityLatestVersion(theId, requestPartitionId);
	}

	@NotNull
	private ResourceTable readEntityLatestVersion(IIdType theId, @Nullable RequestPartitionId theRequestPartitionId) {
		validateResourceTypeAndThrowInvalidRequestException(theId);

		ResourcePersistentId persistentId = myIdHelperService.resolveResourcePersistentIds(theRequestPartitionId, getResourceName(), theId.getIdPart());
		ResourceTable entity = myEntityManager.find(ResourceTable.class, persistentId.getId());
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}
		validateGivenIdIsAppropriateToRetrieveResource(theId, entity);
		entity.setTransientForcedId(theId.getIdPart());
		return entity;
	}

	@Override
	public void reindex(T theResource, ResourceTable theEntity) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		ourLog.debug("Indexing resource {} - PID {}", theEntity.getIdDt().getValue(), theEntity.getId());
		if (theResource != null) {
			CURRENTLY_REINDEXING.put(theResource, Boolean.TRUE);
		}

		TransactionDetails transactionDetails = new TransactionDetails(theEntity.getUpdatedDate());
		updateEntity(null, theResource, theEntity, theEntity.getDeleted(), true, false, transactionDetails, true, false);
		if (theResource != null) {
			CURRENTLY_REINDEXING.put(theResource, null);
		}
	}

	@Transactional
	@Override
	public void removeTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm) {
		removeTag(theId, theTagType, theScheme, theTerm, null);
	}

	@Transactional
	@Override
	public void removeTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm, RequestDetails theRequest) {
		// Notify interceptors
		if (theRequest != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequest, getResourceName(), theId);
			notifyInterceptors(RestOperationTypeEnum.DELETE_TAGS, requestDetails);
		}

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId, theRequest);
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}

		for (BaseTag next : new ArrayList<>(entity.getTags())) {
			if (ObjectUtil.equals(next.getTag().getTagType(), theTagType) &&
				ObjectUtil.equals(next.getTag().getSystem(), theScheme) &&
				ObjectUtil.equals(next.getTag().getCode(), theTerm)) {
				myEntityManager.remove(next);
				entity.getTags().remove(next);
			}
		}

		if (entity.getTags().isEmpty()) {
			entity.setHasTags(false);
		}

		myEntityManager.merge(entity);

		ourLog.debug("Processed remove tag {}/{} on {} in {}ms", theScheme, theTerm, theId.getValue(), w.getMillisAndRestart());
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public IBundleProvider search(final SearchParameterMap theParams) {
		return search(theParams, null);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public IBundleProvider search(final SearchParameterMap theParams, RequestDetails theRequest) {
		return search(theParams, theRequest, null);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public IBundleProvider search(final SearchParameterMap theParams, RequestDetails theRequest, HttpServletResponse theServletResponse) {

		if (myDaoConfig.getIndexMissingFields() == DaoConfig.IndexEnabledEnum.DISABLED) {
			for (List<List<IQueryParameterType>> nextAnds : theParams.values()) {
				for (List<? extends IQueryParameterType> nextOrs : nextAnds) {
					for (IQueryParameterType next : nextOrs) {
						if (next.getMissing() != null) {
							throw new MethodNotAllowedException(":missing modifier is disabled on this server");
						}
					}
				}
			}
		}

		// Notify interceptors
		if (theRequest != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequest, getContext(), getResourceName(), null);
			notifyInterceptors(RestOperationTypeEnum.SEARCH_TYPE, requestDetails);

			if (theRequest.isSubRequest()) {
				Integer max = myDaoConfig.getMaximumSearchResultCountInTransaction();
				if (max != null) {
					Validate.inclusiveBetween(1, Integer.MAX_VALUE, max.intValue(), "Maximum search result count in transaction ust be a positive integer");
					theParams.setLoadSynchronousUpTo(myDaoConfig.getMaximumSearchResultCountInTransaction());
				}
			}

			final Integer offset = RestfulServerUtils.extractOffsetParameter(theRequest);
			if (offset != null || !isPagingProviderDatabaseBacked(theRequest)) {
				theParams.setLoadSynchronous(true);
				if (offset != null) {
					Validate.inclusiveBetween(0, Integer.MAX_VALUE, offset.intValue(), "Offset must be a positive integer");
				}
				theParams.setOffset(offset);
			}

			final Integer count = RestfulServerUtils.extractCountParameter(theRequest);
			if (count != null) {
				Integer maxPageSize = theRequest.getServer().getMaximumPageSize();
				if (maxPageSize != null) {
					Validate.inclusiveBetween(1, theRequest.getServer().getMaximumPageSize(), count.intValue(), "Count must be positive integer and less than " + maxPageSize);
				}
				theParams.setCount(count);
			} else if (theRequest.getServer().getDefaultPageSize() != null) {
				theParams.setCount(theRequest.getServer().getDefaultPageSize());
			}
		}

		CacheControlDirective cacheControlDirective = new CacheControlDirective();
		if (theRequest != null) {
			cacheControlDirective.parse(theRequest.getHeaders(Constants.HEADER_CACHE_CONTROL));
		}

		RequestPartitionId requestPartitionId = myPartitionHelperSvc.determineReadPartitionForRequest(theRequest, getResourceName());
		IBundleProvider retVal = mySearchCoordinatorSvc.registerSearch(this, theParams, getResourceName(), cacheControlDirective, theRequest, requestPartitionId);

		if (retVal instanceof PersistedJpaBundleProvider) {
			PersistedJpaBundleProvider provider = (PersistedJpaBundleProvider) retVal;
			if (provider.getCacheStatus() == SearchCacheStatusEnum.HIT) {
				if (theServletResponse != null && theRequest != null) {
					String value = "HIT from " + theRequest.getFhirServerBase();
					theServletResponse.addHeader(Constants.HEADER_X_CACHE, value);
				}
			}
		}

		return retVal;
	}

	@Override
	public Set<ResourcePersistentId> searchForIds(SearchParameterMap theParams, RequestDetails theRequest) {
		return myTransactionService.execute(theRequest, tx -> {
			theParams.setLoadSynchronousUpTo(10000);

			ISearchBuilder builder = mySearchBuilderFactory.newSearchBuilder(this, getResourceName(), getResourceType());

			HashSet<ResourcePersistentId> retVal = new HashSet<>();

			String uuid = UUID.randomUUID().toString();
			SearchRuntimeDetails searchRuntimeDetails = new SearchRuntimeDetails(theRequest, uuid);

			RequestPartitionId requestPartitionId = myRequestPartitionHelperService.determineReadPartitionForRequest(theRequest, getResourceName());
			try (IResultIterator iter = builder.createQuery(theParams, searchRuntimeDetails, theRequest, requestPartitionId)) {
				while (iter.hasNext()) {
					retVal.add(iter.next());
				}
			} catch (IOException e) {
				ourLog.error("IO failure during database access", e);
			}

			return retVal;
		});
	}

	protected <MT extends IBaseMetaType> MT toMetaDt(Class<MT> theType, Collection<TagDefinition> tagDefinitions) {
		MT retVal = ReflectionUtil.newInstance(theType);
		for (TagDefinition next : tagDefinitions) {
			switch (next.getTagType()) {
				case PROFILE:
					retVal.addProfile(next.getCode());
					break;
				case SECURITY_LABEL:
					retVal.addSecurity().setSystem(next.getSystem()).setCode(next.getCode()).setDisplay(next.getDisplay());
					break;
				case TAG:
					retVal.addTag().setSystem(next.getSystem()).setCode(next.getCode()).setDisplay(next.getDisplay());
					break;
			}
		}
		return retVal;
	}

	private ArrayList<TagDefinition> toTagList(IBaseMetaType theMeta) {
		ArrayList<TagDefinition> retVal = new ArrayList<>();

		for (IBaseCoding next : theMeta.getTag()) {
			retVal.add(new TagDefinition(TagTypeEnum.TAG, next.getSystem(), next.getCode(), next.getDisplay()));
		}
		for (IBaseCoding next : theMeta.getSecurity()) {
			retVal.add(new TagDefinition(TagTypeEnum.SECURITY_LABEL, next.getSystem(), next.getCode(), next.getDisplay()));
		}
		for (IPrimitiveType<String> next : theMeta.getProfile()) {
			retVal.add(new TagDefinition(TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, next.getValue(), null));
		}

		return retVal;
	}

	@Override
	public DaoMethodOutcome update(T theResource) {
		return update(theResource, null, null);
	}

	@Override
	public DaoMethodOutcome update(T theResource, RequestDetails theRequestDetails) {
		return update(theResource, null, theRequestDetails);
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl) {
		return update(theResource, theMatchUrl, null);
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl, RequestDetails theRequestDetails) {
		return update(theResource, theMatchUrl, true, theRequestDetails);
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing, RequestDetails theRequestDetails) {
		return update(theResource, theMatchUrl, thePerformIndexing, false, theRequestDetails, new TransactionDetails());
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing, boolean theForceUpdateVersion, RequestDetails theRequest, @Nonnull TransactionDetails theTransactionDetails) {
		if (theResource == null) {
			String msg = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "missingBody");
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

		preProcessResourceForStorage(resource);

		final ResourceTable entity;

		IIdType resourceId;
		if (isNotBlank(theMatchUrl)) {
			Set<ResourcePersistentId> match = myMatchResourceUrlService.processMatchUrl(theMatchUrl, myResourceType, theRequest);
			if (match.size() > 1) {
				String msg = getContext().getLocalizer().getMessageSanitized(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "UPDATE", theMatchUrl, match.size());
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
				entity = readEntityLatestVersion(resourceId, requestPartitionId);
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

		IBaseResource oldResource = toResource(entity, false);

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
		ResourceTable savedEntity = updateInternal(theRequest, resource, thePerformIndexing, theForceUpdateVersion, entity, resourceId, oldResource, theTransactionDetails);
		DaoMethodOutcome outcome = toMethodOutcome(theRequest, savedEntity, resource).setCreated(wasDeleted);

		if (!thePerformIndexing) {
			IIdType id = getContext().getVersion().newIdType();
			id.setValue(entity.getIdDt().getValue());
			outcome.setId(id);
		}

		String msg = getContext().getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "successfulUpdate", outcome.getId(), w.getMillisAndRestart());
		outcome.setOperationOutcome(createInfoOperationOutcome(msg));

		ourLog.debug(msg);
		return outcome;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public MethodOutcome validate(T theResource, IIdType theId, String theRawResource, EncodingEnum theEncoding, ValidationModeEnum theMode, String theProfile, RequestDetails theRequest) {
		if (theRequest != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequest, theResource, null, theId);
			notifyInterceptors(RestOperationTypeEnum.VALIDATE, requestDetails);
		}

		if (theMode == ValidationModeEnum.DELETE) {
			if (theId == null || theId.hasIdPart() == false) {
				throw new InvalidRequestException("No ID supplied. ID is required when validating with mode=DELETE");
			}
			final ResourceTable entity = readEntityLatestVersion(theId, theRequest);

			// Validate that there are no resources pointing to the candidate that
			// would prevent deletion
			DeleteConflictList deleteConflicts = new DeleteConflictList();
			if (myDaoConfig.isEnforceReferentialIntegrityOnDelete()) {
				myDeleteConflictService.validateOkToDelete(deleteConflicts, entity, true, theRequest, new TransactionDetails());
			}
			DeleteConflictService.validateDeleteConflictsEmptyOrThrowException(getContext(), deleteConflicts);

			IBaseOperationOutcome oo = createInfoOperationOutcome("Ok to delete");
			return new MethodOutcome(new IdDt(theId.getValue()), oo);
		}

		FhirValidator validator = getContext().newValidator();

		validator.registerValidatorModule(getInstanceValidator());
		validator.registerValidatorModule(new IdChecker(theMode));

		IBaseResource resourceToValidateById = null;
		if (theId != null && theId.hasResourceType() && theId.hasIdPart()) {
			Class<? extends IBaseResource> type = getContext().getResourceDefinition(theId.getResourceType()).getImplementingClass();
			IFhirResourceDao<? extends IBaseResource> dao = myDaoRegistry.getResourceDaoOrNull(type);
			resourceToValidateById = dao.read(theId, theRequest);
		}


		ValidationResult result;
		ValidationOptions options = new ValidationOptions()
			.addProfileIfNotBlank(theProfile);

		if (theResource == null) {
			if (resourceToValidateById != null) {
				result = validator.validateWithResult(resourceToValidateById, options);
			} else {
				String msg = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "cantValidateWithNoResource");
				throw new InvalidRequestException(msg);
			}
		} else if (isNotBlank(theRawResource)) {
			result = validator.validateWithResult(theRawResource, options);
		} else {
			result = validator.validateWithResult(theResource, options);
		}

		if (result.isSuccessful()) {
			MethodOutcome retVal = new MethodOutcome();
			retVal.setOperationOutcome(result.toOperationOutcome());
			return retVal;
		} else {
			throw new PreconditionFailedException("Validation failed", result.toOperationOutcome());
		}

	}

	/**
	 * Get the resource definition from the criteria which specifies the resource type
	 */
	@Override
	public RuntimeResourceDefinition validateCriteriaAndReturnResourceDefinition(String criteria) {
		String resourceName;
		if (criteria == null || criteria.trim().isEmpty()) {
			throw new IllegalArgumentException("Criteria cannot be empty");
		}
		if (criteria.contains("?")) {
			resourceName = criteria.substring(0, criteria.indexOf("?"));
		} else {
			resourceName = criteria;
		}

		return getContext().getResourceDefinition(resourceName);
	}

	private void validateGivenIdIsAppropriateToRetrieveResource(IIdType theId, BaseHasResource entity) {
		if (entity.getForcedId() != null) {
			if (myDaoConfig.getResourceClientIdStrategy() != DaoConfig.ClientIdStrategyEnum.ANY) {
				if (theId.isIdPartValidLong()) {
					// This means that the resource with the given numeric ID exists, but it has a "forced ID", meaning that
					// as far as the outside world is concerned, the given ID doesn't exist (it's just an internal pointer
					// to the
					// forced ID)
					throw new ResourceNotFoundException(theId);
				}
			}
		}
	}

	private void validateResourceType(BaseHasResource entity) {
		validateResourceType(entity, myResourceName);
	}

	private void validateResourceTypeAndThrowInvalidRequestException(IIdType theId) {
		if (theId.hasResourceType() && !theId.getResourceType().equals(myResourceName)) {
			// Note- Throw a HAPI FHIR exception here so that hibernate doesn't try to translate it into a database exception
			throw new InvalidRequestException("Incorrect resource type (" + theId.getResourceType() + ") for this DAO, wanted: " + myResourceName);
		}
	}

	private static class IdChecker implements IValidatorModule {

		private final ValidationModeEnum myMode;

		IdChecker(ValidationModeEnum theMode) {
			myMode = theMode;
		}

		@Override
		public void validateResource(IValidationContext<IBaseResource> theCtx) {
			boolean hasId = theCtx.getResource().getIdElement().hasIdPart();
			if (myMode == ValidationModeEnum.CREATE) {
				if (hasId) {
					throw new UnprocessableEntityException("Resource has an ID - ID must not be populated for a FHIR create");
				}
			} else if (myMode == ValidationModeEnum.UPDATE) {
				if (hasId == false) {
					throw new UnprocessableEntityException("Resource has no ID - ID must be populated for a FHIR update");
				}
			}

		}

	}

}
