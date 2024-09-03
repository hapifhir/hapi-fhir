/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexAppCtx;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.dao.ReindexOutcome;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.jpa.api.model.LazyDaoMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.delete.DeleteConflictUtil;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseHasResource;
import ca.uhn.fhir.jpa.model.entity.BaseTag;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.jpa.search.ResourceSearchUrlSvc;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.search.cache.SearchCacheStatusEnum;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.model.dstu2.resource.ListResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.InterceptorInvocationTimingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.HistorySearchDateRangeParam;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IInstanceValidatorModule;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ValidationOptions;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseHapiFhirResourceDao<T extends IBaseResource> extends BaseHapiFhirDao<T>
		implements IFhirResourceDao<T> {

	public static final String BASE_RESOURCE_NAME = "resource";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiFhirResourceDao.class);

	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	protected PlatformTransactionManager myPlatformTransactionManager;

	@Autowired(required = false)
	protected IFulltextSearchSvc mySearchDao;

	@Autowired
	protected HapiTransactionService myTransactionService;

	@Autowired
	private MatchResourceUrlService<JpaPid> myMatchResourceUrlService;

	@Autowired
	private SearchBuilderFactory<JpaPid> mySearchBuilderFactory;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperService;

	@Autowired
	private IJobPartitionProvider myJobPartitionProvider;

	@Autowired
	private MatchUrlService myMatchUrlService;

	@Autowired
	private IDeleteExpungeJobSubmitter myDeleteExpungeJobSubmitter;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	private IInstanceValidatorModule myInstanceValidator;
	private String myResourceName;
	private Class<T> myResourceType;

	@Autowired
	private PersistedJpaBundleProviderFactory myPersistedJpaBundleProviderFactory;

	@Autowired
	private MemoryCacheService myMemoryCacheService;

	private TransactionTemplate myTxTemplate;

	@Autowired
	private ResourceSearchUrlSvc myResourceSearchUrlSvc;

	@Autowired
	private IFhirSystemDao<?, ?> mySystemDao;

	@Nullable
	public static <T extends IBaseResource> T invokeStoragePreShowResources(
			IInterceptorBroadcaster theInterceptorBroadcaster, RequestDetails theRequest, T retVal) {
		if (CompositeInterceptorBroadcaster.hasHooks(
				Pointcut.STORAGE_PRESHOW_RESOURCES, theInterceptorBroadcaster, theRequest)) {
			SimplePreResourceShowDetails showDetails = new SimplePreResourceShowDetails(retVal);
			HookParams params = new HookParams()
					.add(IPreResourceShowDetails.class, showDetails)
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest);
			CompositeInterceptorBroadcaster.doCallHooks(
					theInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PRESHOW_RESOURCES, params);
			//noinspection unchecked
			retVal = (T) showDetails.getResource(
					0); // TODO GGG/JA : getting resource 0 is interesting. We apparently allow null values in the list.
			// Should we?
			return retVal;
		} else {
			return retVal;
		}
	}

	public static void invokeStoragePreAccessResources(
			IInterceptorBroadcaster theInterceptorBroadcaster,
			RequestDetails theRequest,
			IIdType theId,
			IBaseResource theResource) {
		if (CompositeInterceptorBroadcaster.hasHooks(
				Pointcut.STORAGE_PREACCESS_RESOURCES, theInterceptorBroadcaster, theRequest)) {
			SimplePreResourceAccessDetails accessDetails = new SimplePreResourceAccessDetails(theResource);
			HookParams params = new HookParams()
					.add(IPreResourceAccessDetails.class, accessDetails)
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest);
			CompositeInterceptorBroadcaster.doCallHooks(
					theInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);
			if (accessDetails.isDontReturnResourceAtIndex(0)) {
				throw new ResourceNotFoundException(Msg.code(1995) + "Resource " + theId + " is not known");
			}
		}
	}

	@Override
	protected HapiTransactionService getTransactionService() {
		return myTransactionService;
	}

	@VisibleForTesting
	public void setTransactionService(HapiTransactionService theTransactionService) {
		myTransactionService = theTransactionService;
	}

	@Override
	protected MatchResourceUrlService getMatchResourceUrlService() {
		return myMatchResourceUrlService;
	}

	@Override
	protected IStorageResourceParser getStorageResourceParser() {
		return myJpaStorageResourceParser;
	}

	@Override
	protected IDeleteExpungeJobSubmitter getDeleteExpungeJobSubmitter() {
		return myDeleteExpungeJobSubmitter;
	}

	/**
	 * @deprecated Use {@link #create(T, RequestDetails)} instead
	 */
	@Override
	public DaoMethodOutcome create(final T theResource) {
		return create(theResource, null, true, null, new TransactionDetails());
	}

	@Override
	public DaoMethodOutcome create(final T theResource, RequestDetails theRequestDetails) {
		return create(theResource, null, true, theRequestDetails, new TransactionDetails());
	}

	/**
	 * @deprecated Use {@link #create(T, String, RequestDetails)} instead
	 */
	@Override
	public DaoMethodOutcome create(final T theResource, String theIfNoneExist) {
		return create(theResource, theIfNoneExist, null);
	}

	@Override
	public DaoMethodOutcome create(final T theResource, String theIfNoneExist, RequestDetails theRequestDetails) {
		return create(theResource, theIfNoneExist, true, theRequestDetails, new TransactionDetails());
	}

	@Override
	public DaoMethodOutcome create(
			T theResource,
			String theIfNoneExist,
			boolean thePerformIndexing,
			RequestDetails theRequestDetails,
			@Nonnull TransactionDetails theTransactionDetails) {
		RequestPartitionId requestPartitionId = myRequestPartitionHelperService.determineCreatePartitionForRequest(
				theRequestDetails, theResource, getResourceName());
		return myTransactionService
				.withRequest(theRequestDetails)
				.withTransactionDetails(theTransactionDetails)
				.withRequestPartitionId(requestPartitionId)
				.execute(tx -> doCreateForPost(
						theResource,
						theIfNoneExist,
						thePerformIndexing,
						theTransactionDetails,
						theRequestDetails,
						requestPartitionId));
	}

	@VisibleForTesting
	public void setRequestPartitionHelperService(IRequestPartitionHelperSvc theRequestPartitionHelperService) {
		myRequestPartitionHelperService = theRequestPartitionHelperService;
	}

	/**
	 * Called for FHIR create (POST) operations
	 */
	protected DaoMethodOutcome doCreateForPost(
			T theResource,
			String theIfNoneExist,
			boolean thePerformIndexing,
			TransactionDetails theTransactionDetails,
			RequestDetails theRequestDetails,
			RequestPartitionId theRequestPartitionId) {
		if (theResource == null) {
			String msg = getContext().getLocalizer().getMessage(BaseStorageDao.class, "missingBody");
			throw new InvalidRequestException(Msg.code(956) + msg);
		}

		if (isNotBlank(theResource.getIdElement().getIdPart())) {
			if (getContext().getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
				String message = getMessageSanitized(
						"failedToCreateWithClientAssignedId",
						theResource.getIdElement().getIdPart());
				throw new InvalidRequestException(
						Msg.code(957) + message, createErrorOperationOutcome(message, "processing"));
			} else {
				// As of DSTU3, ID and version in the body should be ignored for a create/update
				theResource.setId("");
			}
		}

		if (getStorageSettings().getResourceServerIdStrategy() == JpaStorageSettings.IdStrategyEnum.UUID) {
			theResource.setId(UUID.randomUUID().toString());
			theResource.setUserData(JpaConstants.RESOURCE_ID_SERVER_ASSIGNED, Boolean.TRUE);
		}

		return doCreateForPostOrPut(
				theRequestDetails,
				theResource,
				theIfNoneExist,
				true,
				thePerformIndexing,
				theRequestPartitionId,
				RestOperationTypeEnum.CREATE,
				theTransactionDetails);
	}

	/**
	 * Called both for FHIR create (POST) operations (via {@link #doCreateForPost(IBaseResource, String, boolean, TransactionDetails, RequestDetails, RequestPartitionId)}
	 * as well as for FHIR update (PUT) where we're doing a create-with-client-assigned-ID (via {@link #doUpdate(IBaseResource, String, boolean, boolean, RequestDetails, TransactionDetails, RequestPartitionId)}.
	 */
	private DaoMethodOutcome doCreateForPostOrPut(
			RequestDetails theRequest,
			T theResource,
			String theMatchUrl,
			boolean theProcessMatchUrl,
			boolean thePerformIndexing,
			RequestPartitionId theRequestPartitionId,
			RestOperationTypeEnum theOperationType,
			TransactionDetails theTransactionDetails) {
		StopWatch w = new StopWatch();

		preProcessResourceForStorage(theResource);
		preProcessResourceForStorage(theResource, theRequest, theTransactionDetails, thePerformIndexing);

		ResourceTable entity = new ResourceTable();
		entity.setResourceType(toResourceName(theResource));
		entity.setPartitionId(PartitionablePartitionId.toStoragePartition(theRequestPartitionId, myPartitionSettings));
		entity.setCreatedByMatchUrl(theMatchUrl);
		entity.initializeVersion();

		if (isNotBlank(theMatchUrl) && theProcessMatchUrl) {
			Set<JpaPid> match = myMatchResourceUrlService.processMatchUrl(
					theMatchUrl, myResourceType, theTransactionDetails, theRequest);
			if (match.size() > 1) {
				String msg = getContext()
						.getLocalizer()
						.getMessageSanitized(
								BaseStorageDao.class,
								"transactionOperationWithMultipleMatchFailure",
								"CREATE",
								myResourceName,
								theMatchUrl,
								match.size());
				throw new PreconditionFailedException(Msg.code(958) + msg);
			} else if (match.size() == 1) {

				/*
				 * Ok, so we've found a single PID that matches the conditional URL.
				 * That's good, there are two possibilities below.
				 */

				JpaPid pid = match.iterator().next();
				if (theTransactionDetails.getDeletedResourceIds().contains(pid)) {

					/*
					 * If the resource matching the given match URL has already been
					 * deleted within this transaction. This is a really rare case, since
					 * it means the client has performed a FHIR transaction with both
					 * a delete and a create on the same conditional URL. This is rare
					 * but allowed, and means that it's now ok to create a new one resource
					 * matching the conditional URL since we'll be deleting any existing
					 * index rows on the existing resource as a part of this transaction.
					 * We can also un-resolve the previous match URL in the TransactionDetails
					 * since we'll resolve it to the new resource ID below
					 */

					myMatchResourceUrlService.unresolveMatchUrl(theTransactionDetails, getResourceName(), theMatchUrl);

				} else {

					/*
					 * This is the normal path where the conditional URL matched exactly
					 * one resource, so we won't be creating anything but instead
					 * just returning the existing ID. We now have a PID for the matching
					 * resource, but we haven't loaded anything else (e.g. the forced ID
					 * or the resource body aren't yet loaded from the DB). We're going to
					 * return a LazyDaoOutcome with two lazy loaded providers for loading the
					 * entity and the forced ID since we can avoid these extra SQL loads
					 * unless we know we're actually going to use them. For example, if
					 * the client has specified "Prefer: return=minimal" then we won't be
					 * needing the load the body.
					 */

					Supplier<LazyDaoMethodOutcome.EntityAndResource> entitySupplier = () -> myTxTemplate.execute(tx -> {
						ResourceTable foundEntity = myEntityManager.find(ResourceTable.class, pid.getId());
						IBaseResource resource = myJpaStorageResourceParser.toResource(foundEntity, false);
						theResource.setId(resource.getIdElement().getValue());
						return new LazyDaoMethodOutcome.EntityAndResource(foundEntity, resource);
					});
					Supplier<IIdType> idSupplier = () -> myTxTemplate.execute(tx -> {
						IIdType retVal = myIdHelperService.translatePidIdToForcedId(myFhirContext, myResourceName, pid);
						if (!retVal.hasVersionIdPart()) {
							Long version = myMemoryCacheService.getIfPresent(
									MemoryCacheService.CacheEnum.RESOURCE_CONDITIONAL_CREATE_VERSION, pid.getId());
							if (version == null) {
								version = myResourceTableDao.findCurrentVersionByPid(pid.getId());
								if (version != null) {
									myMemoryCacheService.putAfterCommit(
											MemoryCacheService.CacheEnum.RESOURCE_CONDITIONAL_CREATE_VERSION,
											pid.getId(),
											version);
								}
							}
							if (version != null) {
								retVal = myFhirContext
										.getVersion()
										.newIdType()
										.setParts(
												retVal.getBaseUrl(),
												retVal.getResourceType(),
												retVal.getIdPart(),
												Long.toString(version));
							}
						}
						return retVal;
					});

					DaoMethodOutcome outcome = toMethodOutcomeLazy(theRequest, pid, entitySupplier, idSupplier)
							.setCreated(false)
							.setNop(true);
					StorageResponseCodeEnum responseCode =
							StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH;
					String msg = getContext()
							.getLocalizer()
							.getMessageSanitized(
									BaseStorageDao.class,
									"successfulCreateConditionalWithMatch",
									w.getMillisAndRestart(),
									UrlUtil.sanitizeUrlPart(theMatchUrl));
					outcome.setOperationOutcome(createInfoOperationOutcome(msg, responseCode));
					return outcome;
				}
			}
		}

		boolean isClientAssignedId = storeNonPidResourceId(theResource, entity);

		HookParams hookParams;

		// Notify interceptor for accepting/rejecting client assigned ids
		if (isClientAssignedId) {
			hookParams = new HookParams().add(IBaseResource.class, theResource).add(RequestDetails.class, theRequest);
			doCallHooks(theTransactionDetails, theRequest, Pointcut.STORAGE_PRESTORAGE_CLIENT_ASSIGNED_ID, hookParams);
		}

		// Interceptor call: STORAGE_PRESTORAGE_RESOURCE_CREATED
		hookParams = new HookParams()
				.add(IBaseResource.class, theResource)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest)
				.add(RequestPartitionId.class, theRequestPartitionId)
				.add(TransactionDetails.class, theTransactionDetails);
		doCallHooks(theTransactionDetails, theRequest, Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, hookParams);

		if (isClientAssignedId) {
			validateResourceIdCreation(theResource, theRequest);
		}

		if (theMatchUrl != null) {
			// Note: We actually create the search URL below by calling enforceMatchUrlResourceUniqueness
			// since we can't do that until we know the assigned PID, but we set this flag up here
			// because we need to set it before we persist the ResourceTable entity in order to
			// avoid triggering an extra DB update
			entity.setSearchUrlPresent(true);
		}

		// Perform actual DB update
		// this call will also update the metadata
		ResourceTable updatedEntity = updateEntity(
				theRequest,
				theResource,
				entity,
				null,
				thePerformIndexing,
				false,
				theTransactionDetails,
				false,
				thePerformIndexing);

		// Store the resource forced ID if necessary
		JpaPid jpaPid = JpaPid.fromId(updatedEntity.getResourceId());

		// Populate the resource with its actual final stored ID from the entity
		theResource.setId(entity.getIdDt());

		// Pre-cache the resource ID
		jpaPid.setAssociatedResourceId(entity.getIdType(myFhirContext));
		myIdHelperService.addResolvedPidToForcedId(
				jpaPid, theRequestPartitionId, getResourceName(), entity.getFhirId(), null);
		theTransactionDetails.addResolvedResourceId(jpaPid.getAssociatedResourceId(), jpaPid);
		theTransactionDetails.addResolvedResource(jpaPid.getAssociatedResourceId(), theResource);

		// Pre-cache the match URL, and create an entry in the HFJ_RES_SEARCH_URL table to
		// protect against concurrent writes to the same conditional URL
		if (theMatchUrl != null) {
			myResourceSearchUrlSvc.enforceMatchUrlResourceUniqueness(getResourceName(), theMatchUrl, updatedEntity);
			myMatchResourceUrlService.matchUrlResolved(theTransactionDetails, getResourceName(), theMatchUrl, jpaPid);
		}

		// Update the version/last updated in the resource so that interceptors get
		// the correct version
		// TODO - the above updateEntity calls updateResourceMetadata
		// 		Maybe we don't need this call here?
		myJpaStorageResourceParser.updateResourceMetadata(entity, theResource);

		// Populate the PID in the resource so it is available to hooks
		addPidToResource(entity, theResource);

		// Notify JPA interceptors
		if (!updatedEntity.isUnchangedInCurrentOperation()) {
			hookParams = new HookParams()
					.add(IBaseResource.class, theResource)
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(TransactionDetails.class, theTransactionDetails)
					.add(
							InterceptorInvocationTimingEnum.class,
							theTransactionDetails.getInvocationTiming(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED));
			doCallHooks(theTransactionDetails, theRequest, Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED, hookParams);
		}

		DaoMethodOutcome outcome = toMethodOutcome(theRequest, entity, theResource, theMatchUrl, theOperationType)
				.setCreated(true);

		if (!thePerformIndexing) {
			outcome.setId(theResource.getIdElement());
		}

		populateOperationOutcomeForUpdate(w, outcome, theMatchUrl, theOperationType);

		return outcome;
	}

	/**
	 * Check for an id on the resource and if so,
	 * store it in ResourceTable.
	 *
	 * The fhirId property is either set here with the resource id
	 * OR by hibernate once the PK is generated for a server-assigned id.
	 *
	 * Used for both client-assigned id and for server-assigned UUIDs.
	 *
	 * @return true if this is a client-assigned id
	 *
	 * @see ca.uhn.fhir.jpa.model.entity.ResourceTable.FhirIdGenerator
	 */
	private boolean storeNonPidResourceId(T theResource, ResourceTable entity) {
		String resourceIdBeforeStorage = theResource.getIdElement().getIdPart();
		boolean resourceHadIdBeforeStorage = isNotBlank(resourceIdBeforeStorage);
		boolean resourceIdWasServerAssigned =
				theResource.getUserData(JpaConstants.RESOURCE_ID_SERVER_ASSIGNED) == Boolean.TRUE;

		// We distinguish actual client-assigned ids from UUIDs which the server assigned.
		boolean isClientAssigned = resourceHadIdBeforeStorage && !resourceIdWasServerAssigned;

		// But both need to be set on the entity fhirId field.
		if (resourceHadIdBeforeStorage) {
			entity.setFhirId(resourceIdBeforeStorage);
		}

		return isClientAssigned;
	}

	void validateResourceIdCreation(T theResource, RequestDetails theRequest) {
		JpaStorageSettings.ClientIdStrategyEnum strategy = getStorageSettings().getResourceClientIdStrategy();

		if (strategy == JpaStorageSettings.ClientIdStrategyEnum.NOT_ALLOWED) {
			if (!isSystemRequest(theRequest)) {
				throw new ResourceNotFoundException(Msg.code(959)
						+ getMessageSanitized(
								"failedToCreateWithClientAssignedIdNotAllowed",
								theResource.getIdElement().getIdPart()));
			}
		}

		if (strategy == JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC) {
			if (theResource.getIdElement().isIdPartValidLong()) {
				throw new InvalidRequestException(Msg.code(960)
						+ getMessageSanitized(
								"failedToCreateWithClientAssignedNumericId",
								theResource.getIdElement().getIdPart()));
			}
		}
	}

	protected String getMessageSanitized(String theKey, String theIdPart) {
		return getContext().getLocalizer().getMessageSanitized(BaseStorageDao.class, theKey, theIdPart);
	}

	private boolean isSystemRequest(RequestDetails theRequest) {
		return theRequest instanceof SystemRequestDetails;
	}

	private IInstanceValidatorModule getInstanceValidator() {
		return myInstanceValidator;
	}

	/**
	 * @deprecated Use {@link #delete(IIdType, RequestDetails)} instead
	 */
	@Override
	public DaoMethodOutcome delete(IIdType theId) {
		return delete(theId, null);
	}

	@Override
	public DaoMethodOutcome delete(IIdType theId, RequestDetails theRequestDetails) {
		TransactionDetails transactionDetails = new TransactionDetails();

		validateIdPresentForDelete(theId);
		validateDeleteEnabled();

		return myTransactionService.execute(theRequestDetails, transactionDetails, tx -> {
			DeleteConflictList deleteConflicts = new DeleteConflictList();
			if (isNotBlank(theId.getValue())) {
				deleteConflicts.setResourceIdMarkedForDeletion(theId);
			}

			StopWatch w = new StopWatch();

			DaoMethodOutcome retVal = delete(theId, deleteConflicts, theRequestDetails, transactionDetails);

			DeleteConflictUtil.validateDeleteConflictsEmptyOrThrowException(getContext(), deleteConflicts);

			ourLog.debug("Processed delete on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
			return retVal;
		});
	}

	@Override
	public DaoMethodOutcome delete(
			IIdType theId,
			DeleteConflictList theDeleteConflicts,
			RequestDetails theRequestDetails,
			@Nonnull TransactionDetails theTransactionDetails) {
		validateIdPresentForDelete(theId);
		validateDeleteEnabled();

		final ResourceTable entity;
		try {
			entity = readEntityLatestVersion(theId, theRequestDetails, theTransactionDetails);
		} catch (ResourceNotFoundException ex) {
			// we don't want to throw 404s.
			// if not found, return an outcome anyways.
			// Because no object actually existed, we'll
			// just set the id and nothing else
			return createMethodOutcomeForResourceId(
					theId.getValue(),
					MESSAGE_KEY_DELETE_RESOURCE_NOT_EXISTING,
					StorageResponseCodeEnum.SUCCESSFUL_DELETE_NOT_FOUND);
		}

		if (theId.hasVersionIdPart() && Long.parseLong(theId.getVersionIdPart()) != entity.getVersion()) {
			throw new ResourceVersionConflictException(
					Msg.code(961) + "Trying to delete " + theId + " but this is not the current version");
		}

		JpaPid persistentId = JpaPid.fromId(entity.getResourceId());
		theTransactionDetails.addDeletedResourceId(persistentId);

		// Don't delete again if it's already deleted
		if (isDeleted(entity)) {
			DaoMethodOutcome outcome = createMethodOutcomeForResourceId(
					entity.getIdDt().getValue(),
					MESSAGE_KEY_DELETE_RESOURCE_ALREADY_DELETED,
					StorageResponseCodeEnum.SUCCESSFUL_DELETE_ALREADY_DELETED);

			// used to exist, so we'll set the persistent id
			outcome.setPersistentId(persistentId);
			outcome.setEntity(entity);

			return outcome;
		}

		StopWatch w = new StopWatch();

		T resourceToDelete = myJpaStorageResourceParser.toResource(myResourceType, entity, null, false);
		theDeleteConflicts.setResourceIdMarkedForDeletion(theId);

		// Notify IServerOperationInterceptors about pre-action call
		HookParams hook = new HookParams()
				.add(IBaseResource.class, resourceToDelete)
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
				.add(TransactionDetails.class, theTransactionDetails);
		doCallHooks(theTransactionDetails, theRequestDetails, Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED, hook);

		myDeleteConflictService.validateOkToDelete(
				theDeleteConflicts, entity, false, theRequestDetails, theTransactionDetails);

		preDelete(resourceToDelete, entity, theRequestDetails);

		ResourceTable savedEntity = updateEntityForDelete(theRequestDetails, theTransactionDetails, entity);
		resourceToDelete.setId(entity.getIdDt());

		// Notify JPA interceptors
		HookParams hookParams = new HookParams()
				.add(IBaseResource.class, resourceToDelete)
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
				.add(TransactionDetails.class, theTransactionDetails)
				.add(
						InterceptorInvocationTimingEnum.class,
						theTransactionDetails.getInvocationTiming(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED));

		doCallHooks(theTransactionDetails, theRequestDetails, Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED, hookParams);

		DaoMethodOutcome outcome = toMethodOutcome(
						theRequestDetails, savedEntity, resourceToDelete, null, RestOperationTypeEnum.DELETE)
				.setCreated(true);

		String msg = getContext().getLocalizer().getMessageSanitized(BaseStorageDao.class, "successfulDeletes", 1);
		msg += " "
				+ getContext()
						.getLocalizer()
						.getMessageSanitized(BaseStorageDao.class, "successfulTimingSuffix", w.getMillis());
		outcome.setOperationOutcome(createInfoOperationOutcome(msg, StorageResponseCodeEnum.SUCCESSFUL_DELETE));

		return outcome;
	}

	@Override
	public DeleteMethodOutcome deleteByUrl(String theUrl, RequestDetails theRequest) {
		validateDeleteEnabled();

		TransactionDetails transactionDetails = new TransactionDetails();
		ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(theUrl);

		if (resourceSearch.isDeleteExpunge()) {
			return deleteExpunge(theUrl, theRequest);
		}

		return myTransactionService
				.withRequest(theRequest)
				.withTransactionDetails(transactionDetails)
				.execute(tx -> {
					DeleteConflictList deleteConflicts = new DeleteConflictList();
					DeleteMethodOutcome outcome = deleteByUrl(theUrl, deleteConflicts, theRequest, transactionDetails);
					DeleteConflictUtil.validateDeleteConflictsEmptyOrThrowException(getContext(), deleteConflicts);
					return outcome;
				});
	}

	/**
	 * This method gets called by {@link #deleteByUrl(String, RequestDetails)} as well as by
	 * transaction processors
	 */
	@Override
	public DeleteMethodOutcome deleteByUrl(
			String theUrl,
			DeleteConflictList deleteConflicts,
			RequestDetails theRequestDetails,
			@Nonnull TransactionDetails theTransactionDetails) {
		validateDeleteEnabled();

		return myTransactionService
				.withRequest(theRequestDetails)
				.withTransactionDetails(theTransactionDetails)
				.execute(tx -> doDeleteByUrl(theUrl, deleteConflicts, theTransactionDetails, theRequestDetails));
	}

	@Nonnull
	private DeleteMethodOutcome doDeleteByUrl(
			String theUrl,
			DeleteConflictList deleteConflicts,
			TransactionDetails theTransactionDetails,
			RequestDetails theRequestDetails) {
		ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(theUrl);
		SearchParameterMap paramMap = resourceSearch.getSearchParameterMap();
		paramMap.setLoadSynchronous(true);

		Set<JpaPid> resourceIds = myMatchResourceUrlService.search(paramMap, myResourceType, theRequestDetails, null);

		if (resourceIds.size() > 1) {
			if (!getStorageSettings().isAllowMultipleDelete()) {
				throw new PreconditionFailedException(Msg.code(962)
						+ getContext()
								.getLocalizer()
								.getMessageSanitized(
										BaseStorageDao.class,
										"transactionOperationWithMultipleMatchFailure",
										"DELETE",
										myResourceName,
										theUrl,
										resourceIds.size()));
			}
			// TODO: LD: There is a still a bug on slow deletes:  https://github.com/hapifhir/hapi-fhir/issues/5675
			final long threshold = getStorageSettings().getRestDeleteByUrlResourceIdThreshold();
			if (resourceIds.size() > threshold) {
				throw new PreconditionFailedException(Msg.code(2496)
						+ getContext()
								.getLocalizer()
								.getMessageSanitized(
										BaseStorageDao.class,
										"deleteByUrlThresholdExceeded",
										theUrl,
										resourceIds.size(),
										threshold));
			}
		}

		return deletePidList(theUrl, resourceIds, deleteConflicts, theRequestDetails, theTransactionDetails);
	}

	@Override
	public <P extends IResourcePersistentId> void expunge(Collection<P> theResourceIds, RequestDetails theRequest) {
		ExpungeOptions options = new ExpungeOptions();
		options.setExpungeDeletedResources(true);
		for (P pid : theResourceIds) {
			if (pid instanceof JpaPid) {
				ResourceTable entity = myEntityManager.find(ResourceTable.class, pid.getId());

				forceExpungeInExistingTransaction(entity.getIdDt().toVersionless(), options, theRequest);
			} else {
				ourLog.warn("Unable to process expunge on resource {}", pid);
				return;
			}
		}
	}

	@Nonnull
	@Override
	public <P extends IResourcePersistentId> DeleteMethodOutcome deletePidList(
			String theUrl,
			Collection<P> theResourceIds,
			DeleteConflictList theDeleteConflicts,
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails) {
		StopWatch w = new StopWatch();
		TransactionDetails transactionDetails =
				theTransactionDetails != null ? theTransactionDetails : new TransactionDetails();
		List<ResourceTable> deletedResources = new ArrayList<>();

		List<IResourcePersistentId<?>> resolvedIds =
				theResourceIds.stream().map(t -> (IResourcePersistentId<?>) t).collect(Collectors.toList());
		mySystemDao.preFetchResources(resolvedIds, false);

		for (P pid : theResourceIds) {
			JpaPid jpaPid = (JpaPid) pid;

			// This shouldn't actually need to hit the DB because we pre-fetch above
			ResourceTable entity = myEntityManager.find(ResourceTable.class, jpaPid.getId());
			deletedResources.add(entity);

			T resourceToDelete = myJpaStorageResourceParser.toResource(myResourceType, entity, null, false);

			transactionDetails.addDeletedResourceId(pid);

			// Notify IServerOperationInterceptors about pre-action call
			HookParams hooks = new HookParams()
					.add(IBaseResource.class, resourceToDelete)
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
					.add(TransactionDetails.class, transactionDetails);
			doCallHooks(transactionDetails, theRequestDetails, Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED, hooks);

			myDeleteConflictService.validateOkToDelete(
					theDeleteConflicts, entity, false, theRequestDetails, transactionDetails);

			// Perform delete

			preDelete(resourceToDelete, entity, theRequestDetails);

			updateEntityForDelete(theRequestDetails, transactionDetails, entity);
			resourceToDelete.setId(entity.getIdDt());

			// Notify JPA interceptors
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void beforeCommit(boolean readOnly) {
					HookParams hookParams = new HookParams()
							.add(IBaseResource.class, resourceToDelete)
							.add(RequestDetails.class, theRequestDetails)
							.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
							.add(TransactionDetails.class, transactionDetails)
							.add(
									InterceptorInvocationTimingEnum.class,
									transactionDetails.getInvocationTiming(
											Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED));
					doCallHooks(
							transactionDetails,
							theRequestDetails,
							Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED,
							hookParams);
				}
			});
		}

		IBaseOperationOutcome oo;
		if (deletedResources.isEmpty()) {
			String msg = getContext()
					.getLocalizer()
					.getMessageSanitized(BaseStorageDao.class, "unableToDeleteNotFound", theUrl);
			oo = createOperationOutcome(
					OO_SEVERITY_WARN, msg, "not-found", StorageResponseCodeEnum.SUCCESSFUL_DELETE_NOT_FOUND);
		} else {
			String msg = getContext()
					.getLocalizer()
					.getMessageSanitized(BaseStorageDao.class, "successfulDeletes", deletedResources.size());
			msg += " "
					+ getContext()
							.getLocalizer()
							.getMessageSanitized(BaseStorageDao.class, "successfulTimingSuffix", w.getMillis());
			oo = createInfoOperationOutcome(msg, StorageResponseCodeEnum.SUCCESSFUL_DELETE);
		}

		ourLog.debug(
				"Processed delete on {} (matched {} resource(s)) in {}ms",
				theUrl,
				deletedResources.size(),
				w.getMillis());

		DeleteMethodOutcome retVal = new DeleteMethodOutcome();
		retVal.setDeletedEntities(deletedResources);
		retVal.setOperationOutcome(oo);
		return retVal;
	}

	protected ResourceTable updateEntityForDelete(
			RequestDetails theRequest, TransactionDetails theTransactionDetails, ResourceTable theEntity) {
		myResourceSearchUrlSvc.deleteByResId(theEntity.getId());
		Date updateTime = new Date();
		return updateEntity(theRequest, null, theEntity, updateTime, true, true, theTransactionDetails, false, true);
	}

	private void validateDeleteEnabled() {
		if (!getStorageSettings().isDeleteEnabled()) {
			String msg = getContext().getLocalizer().getMessage(BaseStorageDao.class, "deleteBlockedBecauseDisabled");
			throw new PreconditionFailedException(Msg.code(966) + msg);
		}
	}

	private void validateIdPresentForDelete(IIdType theId) {
		if (theId == null || !theId.hasIdPart()) {
			throw new InvalidRequestException(Msg.code(967) + "Can not perform delete, no ID provided");
		}
	}

	private <MT extends IBaseMetaType> void doMetaAdd(
			MT theMetaAdd,
			BaseHasResource theEntity,
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails) {
		IBaseResource oldVersion = myJpaStorageResourceParser.toResource(theEntity, false);

		List<TagDefinition> tags = toTagList(theMetaAdd);
		for (TagDefinition nextDef : tags) {

			boolean entityHasTag = false;
			for (BaseTag next : new ArrayList<>(theEntity.getTags())) {
				if (Objects.equals(next.getTag().getTagType(), nextDef.getTagType())
						&& Objects.equals(next.getTag().getSystem(), nextDef.getSystem())
						&& Objects.equals(next.getTag().getCode(), nextDef.getCode())
						&& Objects.equals(next.getTag().getVersion(), nextDef.getVersion())
						&& Objects.equals(next.getTag().getUserSelected(), nextDef.getUserSelected())) {
					entityHasTag = true;
					break;
				}
			}

			if (!entityHasTag) {
				theEntity.setHasTags(true);

				TagDefinition def = getTagOrNull(
						theTransactionDetails,
						nextDef.getTagType(),
						nextDef.getSystem(),
						nextDef.getCode(),
						nextDef.getDisplay(),
						nextDef.getVersion(),
						nextDef.getUserSelected());
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
		IBaseResource newVersion = myJpaStorageResourceParser.toResource(theEntity, false);
		HookParams preStorageParams = new HookParams()
				.add(IBaseResource.class, oldVersion)
				.add(IBaseResource.class, newVersion)
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
				.add(TransactionDetails.class, theTransactionDetails);
		myInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, preStorageParams);

		// Interceptor call: STORAGE_PRECOMMIT_RESOURCE_UPDATED
		HookParams preCommitParams = new HookParams()
				.add(IBaseResource.class, oldVersion)
				.add(IBaseResource.class, newVersion)
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
				.add(TransactionDetails.class, theTransactionDetails)
				.add(
						InterceptorInvocationTimingEnum.class,
						theTransactionDetails.getInvocationTiming(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED));
		myInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, preCommitParams);
	}

	private <MT extends IBaseMetaType> void doMetaDelete(
			MT theMetaDel,
			BaseHasResource theEntity,
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails) {

		// todo mb update hibernate search index if we are storing resources - it assumes inline tags.
		IBaseResource oldVersion = myJpaStorageResourceParser.toResource(theEntity, false);

		List<TagDefinition> tags = toTagList(theMetaDel);

		for (TagDefinition nextDef : tags) {
			for (BaseTag next : new ArrayList<BaseTag>(theEntity.getTags())) {
				if (Objects.equals(next.getTag().getTagType(), nextDef.getTagType())
						&& Objects.equals(next.getTag().getSystem(), nextDef.getSystem())
						&& Objects.equals(next.getTag().getCode(), nextDef.getCode())) {
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
		IBaseResource newVersion = myJpaStorageResourceParser.toResource(theEntity, false);
		HookParams preStorageParams = new HookParams()
				.add(IBaseResource.class, oldVersion)
				.add(IBaseResource.class, newVersion)
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
				.add(TransactionDetails.class, theTransactionDetails);
		myInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, preStorageParams);

		HookParams preCommitParams = new HookParams()
				.add(IBaseResource.class, oldVersion)
				.add(IBaseResource.class, newVersion)
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
				.add(TransactionDetails.class, theTransactionDetails)
				.add(
						InterceptorInvocationTimingEnum.class,
						theTransactionDetails.getInvocationTiming(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED));

		myInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, preCommitParams);
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public ExpungeOutcome expunge(IIdType theId, ExpungeOptions theExpungeOptions, RequestDetails theRequest) {
		validateExpungeEnabled();
		return forceExpungeInExistingTransaction(theId, theExpungeOptions, theRequest);
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public ExpungeOutcome expunge(ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails) {
		ourLog.info("Beginning TYPE[{}] expunge operation", getResourceName());
		validateExpungeEnabled();
		return myExpungeService.expunge(getResourceName(), null, theExpungeOptions, theRequestDetails);
	}

	private void validateExpungeEnabled() {
		if (!getStorageSettings().isExpungeEnabled()) {
			throw new MethodNotAllowedException(Msg.code(968) + "$expunge is not enabled on this server");
		}
	}

	@Override
	public ExpungeOutcome forceExpungeInExistingTransaction(
			IIdType theId, ExpungeOptions theExpungeOptions, RequestDetails theRequest) {
		TransactionTemplate txTemplate = new TransactionTemplate(myPlatformTransactionManager);

		BaseHasResource entity = txTemplate.execute(t -> readEntity(theId, theRequest));
		Validate.notNull(entity, "Resource with ID %s not found in database", theId);

		if (theId.hasVersionIdPart()) {
			BaseHasResource currentVersion;
			currentVersion = txTemplate.execute(t -> readEntity(theId.toVersionless(), theRequest));
			Validate.notNull(
					currentVersion,
					"Current version of resource with ID %s not found in database",
					theId.toVersionless());

			if (entity.getVersion() == currentVersion.getVersion()) {
				throw new PreconditionFailedException(
						Msg.code(969) + "Can not perform version-specific expunge of resource "
								+ theId.toUnqualified().getValue() + " as this is the current version");
			}

			return myExpungeService.expunge(
					getResourceName(),
					JpaPid.fromIdAndVersion(entity.getResourceId(), entity.getVersion()),
					theExpungeOptions,
					theRequest);
		}

		return myExpungeService.expunge(
				getResourceName(), JpaPid.fromId(entity.getResourceId()), theExpungeOptions, theRequest);
	}

	@Override
	@Nonnull
	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public Class<T> getResourceType() {
		return myResourceType;
	}

	@SuppressWarnings("unchecked")
	public void setResourceType(Class<? extends IBaseResource> theTableType) {
		myResourceType = (Class<T>) theTableType;
	}

	@Override
	public IBundleProvider history(Date theSince, Date theUntil, Integer theOffset, RequestDetails theRequestDetails) {
		StopWatch w = new StopWatch();
		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequestForHistory(
						theRequestDetails, myResourceName, null);
		IBundleProvider retVal = myTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(requestPartitionId)
				.execute(() -> myPersistedJpaBundleProviderFactory.history(
						theRequestDetails, myResourceName, null, theSince, theUntil, theOffset, requestPartitionId));

		ourLog.debug("Processed history on {} in {}ms", myResourceName, w.getMillisAndRestart());
		return retVal;
	}

	/**
	 * @deprecated Use {@link #history(IIdType, HistorySearchDateRangeParam, RequestDetails)} instead
	 */
	@Override
	public IBundleProvider history(
			final IIdType theId, final Date theSince, Date theUntil, Integer theOffset, RequestDetails theRequest) {
		StopWatch w = new StopWatch();

		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequestForHistory(
						theRequest, myResourceName, theId);
		IBundleProvider retVal = myTransactionService
				.withRequest(theRequest)
				.withRequestPartitionId(requestPartitionId)
				.execute(() -> {
					IIdType id = theId.withResourceType(myResourceName).toUnqualifiedVersionless();
					BaseHasResource entity = readEntity(id, true, theRequest, requestPartitionId);

					return myPersistedJpaBundleProviderFactory.history(
							theRequest,
							myResourceName,
							entity.getId(),
							theSince,
							theUntil,
							theOffset,
							requestPartitionId);
				});

		ourLog.debug("Processed history on {} in {}ms", theId, w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public IBundleProvider history(
			final IIdType theId,
			final HistorySearchDateRangeParam theHistorySearchDateRangeParam,
			RequestDetails theRequest) {
		StopWatch w = new StopWatch();
		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequestForHistory(
						theRequest, myResourceName, theId);
		IBundleProvider retVal = myTransactionService
				.withRequest(theRequest)
				.withRequestPartitionId(requestPartitionId)
				.execute(() -> {
					IIdType id = theId.withResourceType(myResourceName).toUnqualifiedVersionless();
					BaseHasResource entity = readEntity(id, true, theRequest, requestPartitionId);

					return myPersistedJpaBundleProviderFactory.history(
							theRequest,
							myResourceName,
							entity.getId(),
							theHistorySearchDateRangeParam.getLowerBoundAsInstant(),
							theHistorySearchDateRangeParam.getUpperBoundAsInstant(),
							theHistorySearchDateRangeParam.getOffset(),
							theHistorySearchDateRangeParam.getHistorySearchType(),
							requestPartitionId);
				});

		ourLog.debug("Processed history on {} in {}ms", theId, w.getMillisAndRestart());
		return retVal;
	}

	protected boolean isPagingProviderDatabaseBacked(RequestDetails theRequestDetails) {
		if (theRequestDetails == null || theRequestDetails.getServer() == null) {
			return false;
		}
		IRestfulServerDefaults server = theRequestDetails.getServer();
		IPagingProvider pagingProvider = server.getPagingProvider();
		return pagingProvider != null;
	}

	protected void requestReindexForRelatedResources(
			Boolean theCurrentlyReindexing, List<String> theBase, RequestDetails theRequestDetails) {
		// Avoid endless loops
		if (Boolean.TRUE.equals(theCurrentlyReindexing) || shouldSkipReindex(theRequestDetails)) {
			return;
		}

		if (getStorageSettings().isMarkResourcesForReindexingUponSearchParameterChange()) {

			ReindexJobParameters params = new ReindexJobParameters();

			List<String> urls = List.of();
			if (!isCommonSearchParam(theBase)) {
				urls = theBase.stream().map(t -> t + "?").collect(Collectors.toList());
			}

			myJobPartitionProvider.getPartitionedUrls(theRequestDetails, urls).forEach(params::addPartitionedUrl);

			JobInstanceStartRequest request = new JobInstanceStartRequest();
			request.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
			request.setParameters(params);
			myJobCoordinator.startInstance(theRequestDetails, request);

			ourLog.debug("Started reindex job with parameters {}", params);
		}

		mySearchParamRegistry.requestRefresh();
	}

	protected final boolean shouldSkipReindex(RequestDetails theRequestDetails) {
		if (theRequestDetails == null) {
			return false;
		}
		Object shouldSkip = theRequestDetails.getUserData().getOrDefault(JpaConstants.SKIP_REINDEX_ON_UPDATE, false);
		return Boolean.parseBoolean(shouldSkip.toString());
	}

	private boolean isCommonSearchParam(List<String> theBase) {
		// If the base contains the special resource "Resource", this is a common SP that applies to all resources
		return theBase.stream().map(String::toLowerCase).anyMatch(BASE_RESOURCE_NAME::equals);
	}

	@Override
	@Transactional
	public <MT extends IBaseMetaType> MT metaAddOperation(
			IIdType theResourceId, MT theMetaAdd, RequestDetails theRequest) {

		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequestForServerOperation(
						theRequest, JpaConstants.OPERATION_META_ADD);

		myTransactionService
				.withRequest(theRequest)
				.withRequestPartitionId(requestPartitionId)
				.execute(() -> doMetaAddOperation(theResourceId, theMetaAdd, theRequest, requestPartitionId));

		@SuppressWarnings("unchecked")
		MT retVal = (MT) metaGetOperation(theMetaAdd.getClass(), theResourceId, theRequest);
		return retVal;
	}

	protected <MT extends IBaseMetaType> void doMetaAddOperation(
			IIdType theResourceId, MT theMetaAdd, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {
		TransactionDetails transactionDetails = new TransactionDetails();

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theResourceId, true, theRequest, theRequestPartitionId);

		if (isNull(entity)) {
			throw new ResourceNotFoundException(Msg.code(1993) + theResourceId);
		}

		ResourceTable latestVersion = readEntityLatestVersion(theResourceId, theRequestPartitionId, transactionDetails);
		if (latestVersion.getVersion() != entity.getVersion()) {
			doMetaAdd(theMetaAdd, entity, theRequest, transactionDetails);
		} else {
			doMetaAdd(theMetaAdd, latestVersion, theRequest, transactionDetails);

			// Also update history entry
			ResourceHistoryTable history = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(
					entity.getId(), entity.getVersion());
			doMetaAdd(theMetaAdd, history, theRequest, transactionDetails);
		}

		ourLog.debug("Processed metaAddOperation on {} in {}ms", theResourceId, w.getMillisAndRestart());
	}

	@Override
	@Transactional
	public <MT extends IBaseMetaType> MT metaDeleteOperation(
			IIdType theResourceId, MT theMetaDel, RequestDetails theRequest) {

		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequestForServerOperation(
						theRequest, JpaConstants.OPERATION_META_DELETE);

		myTransactionService
				.withRequest(theRequest)
				.withRequestPartitionId(requestPartitionId)
				.execute(() -> doMetaDeleteOperation(theResourceId, theMetaDel, theRequest, requestPartitionId));

		@SuppressWarnings("unchecked")
		MT retVal = (MT) metaGetOperation(theMetaDel.getClass(), theResourceId, theRequest);
		return retVal;
	}

	@Transactional
	public <MT extends IBaseMetaType> void doMetaDeleteOperation(
			IIdType theResourceId, MT theMetaDel, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {
		TransactionDetails transactionDetails = new TransactionDetails();
		StopWatch w = new StopWatch();

		BaseHasResource entity = readEntity(theResourceId, true, theRequest, theRequestPartitionId);

		if (isNull(entity)) {
			throw new ResourceNotFoundException(Msg.code(1994) + theResourceId);
		}

		ResourceTable latestVersion = readEntityLatestVersion(theResourceId, theRequestPartitionId, transactionDetails);
		boolean nonVersionedTags =
				myStorageSettings.getTagStorageMode() != JpaStorageSettings.TagStorageModeEnum.VERSIONED;

		if (latestVersion.getVersion() != entity.getVersion() || nonVersionedTags) {
			doMetaDelete(theMetaDel, entity, theRequest, transactionDetails);
		} else {
			doMetaDelete(theMetaDel, latestVersion, theRequest, transactionDetails);
			// Also update history entry
			ResourceHistoryTable history = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(
					entity.getId(), entity.getVersion());
			doMetaDelete(theMetaDel, history, theRequest, transactionDetails);
		}

		ourLog.debug("Processed metaDeleteOperation on {} in {}ms", theResourceId.getValue(), w.getMillisAndRestart());
	}

	@Override
	public <MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, IIdType theId, RequestDetails theRequest) {
		return myTransactionService.withRequest(theRequest).execute(() -> {
			Set<TagDefinition> tagDefs = new HashSet<>();
			BaseHasResource entity = readEntity(theId, theRequest);
			for (BaseTag next : entity.getTags()) {
				tagDefs.add(next.getTag());
			}
			MT retVal = toMetaDt(theType, tagDefs);

			retVal.setLastUpdated(entity.getUpdatedDate());
			retVal.setVersionId(Long.toString(entity.getVersion()));

			return retVal;
		});
	}

	@Override
	@Transactional
	public <MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, RequestDetails theRequestDetails) {
		String sql =
				"SELECT d FROM TagDefinition d WHERE d.myId IN (SELECT DISTINCT t.myTagId FROM ResourceTag t WHERE t.myResourceType = :res_type)";
		TypedQuery<TagDefinition> q = myEntityManager.createQuery(sql, TagDefinition.class);
		q.setParameter("res_type", myResourceName);
		List<TagDefinition> tagDefinitions = q.getResultList();

		return toMetaDt(theType, tagDefinitions);
	}

	private boolean isDeleted(BaseHasResource entityToUpdate) {
		return entityToUpdate.getDeleted() != null;
	}

	@PostConstruct
	@Override
	public void start() {
		assert getStorageSettings() != null;

		RuntimeResourceDefinition def = getContext().getResourceDefinition(myResourceType);
		myResourceName = def.getName();

		if (mySearchDao != null && mySearchDao.isDisabled()) {
			mySearchDao = null;
		}

		ourLog.debug("Starting resource DAO for type: {}", getResourceName());
		myInstanceValidator = getApplicationContext().getBean(IInstanceValidatorModule.class);
		myTxTemplate = new TransactionTemplate(myPlatformTransactionManager);
		super.start();
	}

	/**
	 * Subclasses may override to provide behaviour. Invoked within a delete
	 * transaction with the resource that is about to be deleted.
	 */
	protected void preDelete(T theResourceToDelete, ResourceTable theEntityToDelete, RequestDetails theRequestDetails) {
		// nothing by default
	}

	@Override
	@Transactional
	public T readByPid(IResourcePersistentId thePid) {
		return readByPid(thePid, false);
	}

	@Override
	@Transactional
	public T readByPid(IResourcePersistentId thePid, boolean theDeletedOk) {
		StopWatch w = new StopWatch();
		JpaPid jpaPid = (JpaPid) thePid;

		Optional<ResourceTable> entity = myResourceTableDao.findById(jpaPid.getId());
		if (entity.isEmpty()) {
			throw new ResourceNotFoundException(Msg.code(975) + "No resource found with PID " + jpaPid);
		}
		if (isDeleted(entity.get()) && !theDeletedOk) {
			throw createResourceGoneException(entity.get());
		}

		T retVal = myJpaStorageResourceParser.toResource(myResourceType, entity.get(), null, false);

		ourLog.debug("Processed read on {} in {}ms", jpaPid, w.getMillis());
		return retVal;
	}

	/**
	 * @deprecated Use {@link #read(IIdType, RequestDetails)} instead
	 */
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
		TransactionDetails transactionDetails = new TransactionDetails();

		RequestPartitionId requestPartitionId = myRequestPartitionHelperService.determineReadPartitionForRequestForRead(
				theRequest, myResourceName, theId);

		return myTransactionService
				.withRequest(theRequest)
				.withTransactionDetails(transactionDetails)
				.withRequestPartitionId(requestPartitionId)
				.read(() -> doReadInTransaction(theId, theRequest, theDeletedOk, requestPartitionId));
	}

	private T doReadInTransaction(
			IIdType theId, RequestDetails theRequest, boolean theDeletedOk, RequestPartitionId theRequestPartitionId) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId, true, theRequest, theRequestPartitionId);
		validateResourceType(entity);

		T retVal = myJpaStorageResourceParser.toResource(myResourceType, entity, null, false);

		if (!theDeletedOk) {
			if (isDeleted(entity)) {
				throw createResourceGoneException(entity);
			}
		}
		// If the resolved fhir model is null, we don't need to run pre-access over or pre-show over it.
		if (retVal != null) {
			invokeStoragePreAccessResources(theId, theRequest, retVal);
			retVal = invokeStoragePreShowResources(theRequest, retVal);
		}

		ourLog.debug("Processed read on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
		return retVal;
	}

	@Nullable
	private T invokeStoragePreShowResources(RequestDetails theRequest, T retVal) {
		retVal = invokeStoragePreShowResources(myInterceptorBroadcaster, theRequest, retVal);
		return retVal;
	}

	private void invokeStoragePreAccessResources(IIdType theId, RequestDetails theRequest, T theResource) {
		invokeStoragePreAccessResources(myInterceptorBroadcaster, theRequest, theId, theResource);
	}

	private Optional<T> invokeStoragePreAccessResources(RequestDetails theRequest, T theResource) {
		if (CompositeInterceptorBroadcaster.hasHooks(
				Pointcut.STORAGE_PREACCESS_RESOURCES, myInterceptorBroadcaster, theRequest)) {
			SimplePreResourceAccessDetails accessDetails = new SimplePreResourceAccessDetails(theResource);
			HookParams params = new HookParams()
					.add(IPreResourceAccessDetails.class, accessDetails)
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest);
			CompositeInterceptorBroadcaster.doCallHooks(
					myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);
			if (accessDetails.isDontReturnResourceAtIndex(0)) {
				return Optional.empty();
			}
		}
		return Optional.of(theResource);
	}

	@Override
	public BaseHasResource readEntity(IIdType theId, RequestDetails theRequest) {
		RequestPartitionId requestPartitionId = myRequestPartitionHelperService.determineReadPartitionForRequestForRead(
				theRequest, myResourceName, theId);
		return myTransactionService
				.withRequest(theRequest)
				.withRequestPartitionId(requestPartitionId)
				.execute(() -> readEntity(theId, true, theRequest, requestPartitionId));
	}

	@Override
	public ReindexOutcome reindex(
			IResourcePersistentId thePid,
			ReindexParameters theReindexParameters,
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails) {
		ReindexOutcome retVal = new ReindexOutcome();

		JpaPid jpaPid = (JpaPid) thePid;

		// Careful!  Reindex only reads ResourceTable, but we tell Hibernate to check version
		// to ensure Hibernate will catch concurrent updates (PUT/DELETE) elsewhere.
		// Otherwise, we may index stale data.  See #4584
		// We use the main entity as the lock object since all the index rows hang off it.
		ResourceTable entity;
		if (theReindexParameters.isOptimisticLock()) {
			entity = myEntityManager.find(ResourceTable.class, jpaPid.getId(), LockModeType.OPTIMISTIC);
		} else {
			entity = myEntityManager.find(ResourceTable.class, jpaPid.getId());
		}

		if (entity == null) {
			retVal.addWarning("Unable to find entity with PID: " + jpaPid.getId());
			return retVal;
		}

		if (theReindexParameters.getReindexSearchParameters() == ReindexParameters.ReindexSearchParametersEnum.ALL) {
			reindexSearchParameters(entity, retVal, theTransactionDetails);
		}
		if (theReindexParameters.getOptimizeStorage() != ReindexParameters.OptimizeStorageModeEnum.NONE) {
			reindexOptimizeStorage(entity, theReindexParameters.getOptimizeStorage());
		}

		return retVal;
	}

	@SuppressWarnings("unchecked")
	private void reindexSearchParameters(
			ResourceTable entity, ReindexOutcome theReindexOutcome, TransactionDetails theTransactionDetails) {
		try {
			T resource = (T) myJpaStorageResourceParser.toResource(entity, false);
			reindexSearchParameters(resource, entity, theTransactionDetails);
		} catch (Exception e) {
			ourLog.warn("Failure during reindex: {}", e.toString());
			theReindexOutcome.addWarning("Failed to reindex resource " + entity.getIdDt() + ": " + e);
			myResourceTableDao.updateIndexStatus(entity.getId(), INDEX_STATUS_INDEXING_FAILED);
		}
	}

	/**
	 * @deprecated Use {@link #reindex(IResourcePersistentId, ReindexParameters, RequestDetails, TransactionDetails)}
	 */
	@Deprecated
	@Override
	public void reindex(T theResource, IBasePersistedResource theEntity) {
		assert TransactionSynchronizationManager.isActualTransactionActive();
		ResourceTable entity = (ResourceTable) theEntity;
		TransactionDetails transactionDetails = new TransactionDetails(entity.getUpdatedDate());

		reindexSearchParameters(theResource, theEntity, transactionDetails);
	}

	private void reindexSearchParameters(
			T theResource, IBasePersistedResource theEntity, TransactionDetails transactionDetails) {
		ourLog.debug("Indexing resource {} - PID {}", theEntity.getIdDt().getValue(), theEntity.getPersistentId());
		if (theResource != null) {
			CURRENTLY_REINDEXING.put(theResource, Boolean.TRUE);
		}

		SystemRequestDetails request = new SystemRequestDetails();
		request.getUserData().put(JpaConstants.SKIP_REINDEX_ON_UPDATE, Boolean.TRUE);

		updateEntity(
				request, theResource, theEntity, theEntity.getDeleted(), true, false, transactionDetails, true, false);
		if (theResource != null) {
			CURRENTLY_REINDEXING.put(theResource, null);
		}
	}

	private void reindexOptimizeStorage(
			ResourceTable entity, ReindexParameters.OptimizeStorageModeEnum theOptimizeStorageMode) {
		ResourceHistoryTable historyEntity = entity.getCurrentVersionEntity();
		if (historyEntity != null) {
			reindexOptimizeStorageHistoryEntity(entity, historyEntity);
			if (theOptimizeStorageMode == ReindexParameters.OptimizeStorageModeEnum.ALL_VERSIONS) {
				int pageSize = 100;
				for (int page = 0; ((long) page * pageSize) < entity.getVersion(); page++) {
					Slice<ResourceHistoryTable> historyEntities =
							myResourceHistoryTableDao.findForResourceIdAndReturnEntitiesAndFetchProvenance(
									PageRequest.of(page, pageSize), entity.getId(), historyEntity.getVersion());
					for (ResourceHistoryTable next : historyEntities) {
						reindexOptimizeStorageHistoryEntity(entity, next);
					}
				}
			}
		}
	}

	private void reindexOptimizeStorageHistoryEntity(ResourceTable entity, ResourceHistoryTable historyEntity) {
		boolean changed = false;
		if (historyEntity.getEncoding() == ResourceEncodingEnum.JSONC
				|| historyEntity.getEncoding() == ResourceEncodingEnum.JSON) {
			byte[] resourceBytes = historyEntity.getResource();
			if (resourceBytes != null) {
				String resourceText = decodeResource(resourceBytes, historyEntity.getEncoding());
				if (myResourceHistoryCalculator.conditionallyAlterHistoryEntity(entity, historyEntity, resourceText)) {
					changed = true;
				}
			}
		}
		if (isBlank(historyEntity.getSourceUri()) && isBlank(historyEntity.getRequestId())) {
			if (historyEntity.getProvenance() != null) {
				historyEntity.setSourceUri(historyEntity.getProvenance().getSourceUri());
				historyEntity.setRequestId(historyEntity.getProvenance().getRequestId());
				changed = true;
			}
		}
		if (changed) {
			myResourceHistoryTableDao.save(historyEntity);
		}
	}

	private BaseHasResource readEntity(
			IIdType theId,
			boolean theCheckForForcedId,
			RequestDetails theRequest,
			RequestPartitionId requestPartitionId) {
		validateResourceTypeAndThrowInvalidRequestException(theId);

		BaseHasResource entity;
		JpaPid pid = myIdHelperService.resolveResourcePersistentIds(
				requestPartitionId, getResourceName(), theId.getIdPart());
		Set<Integer> readPartitions = null;
		if (requestPartitionId.isAllPartitions()) {
			entity = myEntityManager.find(ResourceTable.class, pid.getId());
		} else {
			readPartitions = myRequestPartitionHelperService.toReadPartitions(requestPartitionId);
			if (readPartitions.size() == 1) {
				if (readPartitions.contains(null)) {
					entity = myResourceTableDao
							.readByPartitionIdNull(pid.getId())
							.orElse(null);
				} else {
					entity = myResourceTableDao
							.readByPartitionId(readPartitions.iterator().next(), pid.getId())
							.orElse(null);
				}
			} else {
				if (readPartitions.contains(null)) {
					List<Integer> readPartitionsWithoutNull =
							readPartitions.stream().filter(Objects::nonNull).collect(Collectors.toList());
					entity = myResourceTableDao
							.readByPartitionIdsOrNull(readPartitionsWithoutNull, pid.getId())
							.orElse(null);
				} else {
					entity = myResourceTableDao
							.readByPartitionIds(readPartitions, pid.getId())
							.orElse(null);
				}
			}
		}

		// Verify that the resource is for the correct partition
		if (entity != null && readPartitions != null && entity.getPartitionId() != null) {
			if (!readPartitions.contains(entity.getPartitionId().getPartitionId())) {
				ourLog.debug(
						"Performing a read for PartitionId={} but entity has partition: {}",
						requestPartitionId,
						entity.getPartitionId());
				entity = null;
			}
		}

		if (entity == null) {
			throw new ResourceNotFoundException(Msg.code(1996) + "Resource " + theId + " is not known");
		}

		if (theId.hasVersionIdPart()) {
			if (!theId.isVersionIdPartValidLong()) {
				throw new ResourceNotFoundException(Msg.code(978)
						+ getContext()
								.getLocalizer()
								.getMessageSanitized(
										BaseStorageDao.class,
										"invalidVersion",
										theId.getVersionIdPart(),
										theId.toUnqualifiedVersionless()));
			}
			if (entity.getVersion() != theId.getVersionIdPartAsLong()) {
				entity = null;
			}
		}

		if (entity == null) {
			if (theId.hasVersionIdPart()) {
				TypedQuery<ResourceHistoryTable> q = myEntityManager.createQuery(
						"SELECT t from ResourceHistoryTable t WHERE t.myResourceId = :RID AND t.myResourceType = :RTYP AND t.myResourceVersion = :RVER",
						ResourceHistoryTable.class);
				q.setParameter("RID", pid.getId());
				q.setParameter("RTYP", myResourceName);
				q.setParameter("RVER", theId.getVersionIdPartAsLong());
				try {
					entity = q.getSingleResult();
				} catch (NoResultException e) {
					throw new ResourceNotFoundException(Msg.code(979)
							+ getContext()
									.getLocalizer()
									.getMessageSanitized(
											BaseStorageDao.class,
											"invalidVersion",
											theId.getVersionIdPart(),
											theId.toUnqualifiedVersionless()));
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

	@Override
	protected IBasePersistedResource readEntityLatestVersion(
			IResourcePersistentId thePersistentId,
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails) {
		JpaPid jpaPid = (JpaPid) thePersistentId;
		return myEntityManager.find(ResourceTable.class, jpaPid.getId());
	}

	@Override
	@Nonnull
	protected ResourceTable readEntityLatestVersion(
			IIdType theId, RequestDetails theRequestDetails, TransactionDetails theTransactionDetails) {
		RequestPartitionId requestPartitionId = myRequestPartitionHelperService.determineReadPartitionForRequestForRead(
				theRequestDetails, getResourceName(), theId);

		return myTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(requestPartitionId)
				.execute(() -> readEntityLatestVersion(theId, requestPartitionId, theTransactionDetails));
	}

	@Nonnull
	private ResourceTable readEntityLatestVersion(
			IIdType theId,
			@Nonnull RequestPartitionId theRequestPartitionId,
			TransactionDetails theTransactionDetails) {
		validateResourceTypeAndThrowInvalidRequestException(theId);

		JpaPid persistentId = null;
		if (theTransactionDetails != null) {
			if (theTransactionDetails.isResolvedResourceIdEmpty(theId.toUnqualifiedVersionless())) {
				throw new ResourceNotFoundException(Msg.code(1997) + theId);
			}
			if (theTransactionDetails.hasResolvedResourceIds()) {
				persistentId = (JpaPid) theTransactionDetails.getResolvedResourceId(theId);
			}
		}

		if (persistentId == null) {
			persistentId = myIdHelperService.resolveResourcePersistentIds(
					theRequestPartitionId, getResourceName(), theId.getIdPart());
		}

		ResourceTable entity = myEntityManager.find(ResourceTable.class, persistentId.getId());
		if (entity == null) {
			throw new ResourceNotFoundException(Msg.code(1998) + theId);
		}
		validateGivenIdIsAppropriateToRetrieveResource(theId, entity);
		return entity;
	}

	@Transactional
	@Override
	public void removeTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm) {
		removeTag(theId, theTagType, theScheme, theTerm, null);
	}

	@Transactional
	@Override
	public void removeTag(
			IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm, RequestDetails theRequest) {
		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId, theRequest);
		if (entity == null) {
			throw new ResourceNotFoundException(Msg.code(1999) + theId);
		}

		for (BaseTag next : new ArrayList<>(entity.getTags())) {
			if (Objects.equals(next.getTag().getTagType(), theTagType)
					&& Objects.equals(next.getTag().getSystem(), theScheme)
					&& Objects.equals(next.getTag().getCode(), theTerm)) {
				myEntityManager.remove(next);
				entity.getTags().remove(next);
			}
		}

		if (entity.getTags().isEmpty()) {
			entity.setHasTags(false);
		}

		myEntityManager.merge(entity);

		ourLog.debug(
				"Processed remove tag {}/{} on {} in {}ms",
				theScheme,
				theTerm,
				theId.getValue(),
				w.getMillisAndRestart());
	}

	/**
	 * @deprecated Use {@link #search(SearchParameterMap, RequestDetails)} instead
	 */
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
	public IBundleProvider search(
			final SearchParameterMap theParams, RequestDetails theRequest, HttpServletResponse theServletResponse) {

		if (theParams.getSearchContainedMode() == SearchContainedModeEnum.BOTH) {
			throw new MethodNotAllowedException(Msg.code(983) + "Contained mode 'both' is not currently supported");
		}
		if (theParams.getSearchContainedMode() != SearchContainedModeEnum.FALSE
				&& !myStorageSettings.isIndexOnContainedResources()) {
			throw new MethodNotAllowedException(
					Msg.code(984) + "Searching with _contained mode enabled is not enabled on this server");
		}

		translateListSearchParams(theParams);

		setOffsetAndCount(theParams, theRequest);

		CacheControlDirective cacheControlDirective = new CacheControlDirective();
		if (theRequest != null) {
			cacheControlDirective.parse(theRequest.getHeaders(Constants.HEADER_CACHE_CONTROL));
		}

		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequestForSearchType(
						theRequest, getResourceName(), theParams);
		IBundleProvider retVal = mySearchCoordinatorSvc.registerSearch(
				this, theParams, getResourceName(), cacheControlDirective, theRequest, requestPartitionId);

		if (retVal instanceof PersistedJpaBundleProvider) {
			PersistedJpaBundleProvider provider = (PersistedJpaBundleProvider) retVal;
			provider.setRequestPartitionId(requestPartitionId);
			if (provider.getCacheStatus() == SearchCacheStatusEnum.HIT) {
				if (theServletResponse != null && theRequest != null) {
					String value = "HIT from " + theRequest.getFhirServerBase();
					theServletResponse.addHeader(Constants.HEADER_X_CACHE, value);
				}
			}
		}

		return retVal;
	}

	private void translateListSearchParams(SearchParameterMap theParams) {

		Set<Map.Entry<String, List<List<IQueryParameterType>>>> entryHashSet = new HashSet<>(theParams.entrySet());

		// Translate _list=42 to _has=List:item:_id=42
		for (Map.Entry<String, List<List<IQueryParameterType>>> stringListEntry : entryHashSet) {
			String key = stringListEntry.getKey();
			if (Constants.PARAM_LIST.equals((key))) {
				List<List<IQueryParameterType>> andOrValues = theParams.get(key);
				theParams.remove(key);
				List<List<IQueryParameterType>> hasParamValues = new ArrayList<>();
				for (List<IQueryParameterType> orValues : andOrValues) {
					List<IQueryParameterType> orList = new ArrayList<>();
					for (IQueryParameterType value : orValues) {
						orList.add(new HasParam(
								"List",
								ListResource.SP_ITEM,
								BaseResource.SP_RES_ID,
								value.getValueAsQueryToken(null)));
					}
					hasParamValues.add(orList);
				}
				theParams.put(Constants.PARAM_HAS, hasParamValues);
			}
		}
	}

	protected void setOffsetAndCount(SearchParameterMap theParams, RequestDetails theRequest) {
		if (theRequest != null) {

			if (theRequest.isSubRequest()) {
				Integer max = getStorageSettings().getMaximumSearchResultCountInTransaction();
				if (max != null) {
					Validate.inclusiveBetween(
							1,
							Integer.MAX_VALUE,
							max,
							"Maximum search result count in transaction must be a positive integer");
					theParams.setLoadSynchronousUpTo(getStorageSettings().getMaximumSearchResultCountInTransaction());
				}
			}

			final Integer offset = RestfulServerUtils.extractOffsetParameter(theRequest);
			if (offset != null || !isPagingProviderDatabaseBacked(theRequest)) {
				theParams.setLoadSynchronous(true);
				if (offset != null) {
					Validate.inclusiveBetween(0, Integer.MAX_VALUE, offset, "Offset must be a positive integer");
				}
				theParams.setOffset(offset);
			}

			Integer count = RestfulServerUtils.extractCountParameter(theRequest);
			if (count != null) {
				Integer maxPageSize = theRequest.getServer().getMaximumPageSize();
				if (maxPageSize != null && count > maxPageSize) {
					ourLog.info(
							"Reducing {} from {} to {} which is the maximum allowable page size.",
							Constants.PARAM_COUNT,
							count,
							maxPageSize);
					count = maxPageSize;
				}
				theParams.setCount(count);
			} else if (theRequest.getServer().getDefaultPageSize() != null) {
				theParams.setCount(theRequest.getServer().getDefaultPageSize());
			}
		}
	}

	@Override
	public List<JpaPid> searchForIds(
			SearchParameterMap theParams,
			RequestDetails theRequest,
			@Nullable IBaseResource theConditionalOperationTargetOrNull) {
		TransactionDetails transactionDetails = new TransactionDetails();
		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequestForSearchType(
						theRequest, myResourceName, theParams, theConditionalOperationTargetOrNull);

		return myTransactionService
				.withRequest(theRequest)
				.withTransactionDetails(transactionDetails)
				.withRequestPartitionId(requestPartitionId)
				.searchList(() -> {
					if (isNull(theParams.getLoadSynchronousUpTo())) {
						theParams.setLoadSynchronousUpTo(myStorageSettings.getInternalSynchronousSearchSize());
					}

					ISearchBuilder<JpaPid> builder =
							mySearchBuilderFactory.newSearchBuilder(this, getResourceName(), getResourceType());

					List<JpaPid> ids = new ArrayList<>();

					String uuid = UUID.randomUUID().toString();

					SearchRuntimeDetails searchRuntimeDetails = new SearchRuntimeDetails(theRequest, uuid);
					try (IResultIterator<JpaPid> iter =
							builder.createQuery(theParams, searchRuntimeDetails, theRequest, requestPartitionId)) {
						while (iter.hasNext()) {
							ids.add(iter.next());
						}
					} catch (IOException e) {
						ourLog.error("IO failure during database access", e);
					}

					return ids;
				});
	}

	@Override
	public <PID extends IResourcePersistentId<?>> Stream<PID> searchForIdStream(
			SearchParameterMap theParams,
			RequestDetails theRequest,
			@Nullable IBaseResource theConditionalOperationTargetOrNull) {

		// the Stream is useless outside the bound connection time, so require our caller to have a session.
		HapiTransactionService.requireTransaction();

		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequestForSearchType(
						theRequest, myResourceName, theParams, theConditionalOperationTargetOrNull);

		ISearchBuilder<JpaPid> builder =
				mySearchBuilderFactory.newSearchBuilder(this, getResourceName(), getResourceType());

		String uuid = UUID.randomUUID().toString();

		SearchRuntimeDetails searchRuntimeDetails = new SearchRuntimeDetails(theRequest, uuid);
		//noinspection unchecked
		return (Stream<PID>) myTransactionService
				.withRequest(theRequest)
				.search(() ->
						builder.createQueryStream(theParams, searchRuntimeDetails, theRequest, requestPartitionId));
	}

	@Override
	public List<T> searchForResources(SearchParameterMap theParams, RequestDetails theRequest) {
		return searchForTransformedIds(theParams, theRequest, this::pidsToResource);
	}

	@Override
	public List<IIdType> searchForResourceIds(SearchParameterMap theParams, RequestDetails theRequest) {
		return searchForTransformedIds(theParams, theRequest, this::pidsToIds);
	}

	private <V> List<V> searchForTransformedIds(
			SearchParameterMap theParams,
			RequestDetails theRequest,
			BiFunction<RequestDetails, Stream<JpaPid>, Stream<V>> transform) {
		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequestForSearchType(
						theRequest, myResourceName, theParams);

		String uuid = UUID.randomUUID().toString();

		SearchRuntimeDetails searchRuntimeDetails = new SearchRuntimeDetails(theRequest, uuid);
		return myTransactionService
				.withRequest(theRequest)
				.withPropagation(Propagation.REQUIRED)
				.searchList(() -> {
					ISearchBuilder<JpaPid> builder =
							mySearchBuilderFactory.newSearchBuilder(this, getResourceName(), getResourceType());
					Stream<JpaPid> pidStream =
							builder.createQueryStream(theParams, searchRuntimeDetails, theRequest, requestPartitionId);

					Stream<V> transformedStream = transform.apply(theRequest, pidStream);

					return transformedStream.collect(Collectors.toList());
				});
	}

	/**
	 * Fetch the resources in chunks and apply PreAccess/PreShow interceptors.
	 */
	@Nonnull
	private Stream<T> pidsToResource(RequestDetails theRequest, Stream<JpaPid> pidStream) {
		ISearchBuilder<JpaPid> searchBuilder =
				mySearchBuilderFactory.newSearchBuilder(this, getResourceName(), getResourceType());
		@SuppressWarnings("unchecked")
		Stream<T> resourceStream = (Stream<T>) new QueryChunker<>()
				.chunk(pidStream, SearchBuilder.getMaximumPageSize())
				.flatMap(pidChunk -> searchBuilder.loadResourcesByPid(pidChunk, theRequest).stream());
		// apply interceptors
		return resourceStream
				.flatMap(resource -> invokeStoragePreAccessResources(theRequest, resource).stream())
				.flatMap(resource -> Optional.ofNullable(invokeStoragePreShowResources(theRequest, resource)).stream());
	}

	/**
	 * get the Ids from the ResourceTable entities in chunks.
	 */
	@Nonnull
	private Stream<IIdType> pidsToIds(RequestDetails theRequestDetails, Stream<JpaPid> thePidStream) {
		Stream<Long> longStream = thePidStream.map(JpaPid::getId);

		return new QueryChunker<>()
				.chunk(longStream, SearchBuilder.getMaximumPageSize())
				.flatMap(ids -> myResourceTableDao.findAllById(ids).stream())
				.map(ResourceTable::getIdDt);
	}

	protected <MT extends IBaseMetaType> MT toMetaDt(Class<MT> theType, Collection<TagDefinition> tagDefinitions) {
		MT retVal = ReflectionUtil.newInstance(theType);
		for (TagDefinition next : tagDefinitions) {
			switch (next.getTagType()) {
				case PROFILE:
					retVal.addProfile(next.getCode());
					break;
				case SECURITY_LABEL:
					retVal.addSecurity()
							.setSystem(next.getSystem())
							.setCode(next.getCode())
							.setDisplay(next.getDisplay());
					break;
				case TAG:
					retVal.addTag()
							.setSystem(next.getSystem())
							.setCode(next.getCode())
							.setDisplay(next.getDisplay());
					break;
			}
		}
		myMetaTagSorter.sort(retVal);
		return retVal;
	}

	private ArrayList<TagDefinition> toTagList(IBaseMetaType theMeta) {
		ArrayList<TagDefinition> retVal = new ArrayList<>();

		for (IBaseCoding next : theMeta.getTag()) {
			retVal.add(new TagDefinition(TagTypeEnum.TAG, next.getSystem(), next.getCode(), next.getDisplay()));
		}
		for (IBaseCoding next : theMeta.getSecurity()) {
			retVal.add(
					new TagDefinition(TagTypeEnum.SECURITY_LABEL, next.getSystem(), next.getCode(), next.getDisplay()));
		}
		for (IPrimitiveType<String> next : theMeta.getProfile()) {
			retVal.add(new TagDefinition(TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, next.getValue(), null));
		}

		return retVal;
	}

	/**
	 * @deprecated Use {@link #update(T, RequestDetails)} instead
	 */
	@Override
	public DaoMethodOutcome update(T theResource) {
		return update(theResource, null, null);
	}

	@Override
	public DaoMethodOutcome update(T theResource, RequestDetails theRequestDetails) {
		return update(theResource, null, theRequestDetails);
	}

	/**
	 * @deprecated Use {@link #update(T, String, RequestDetails)} instead
	 */
	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl) {
		return update(theResource, theMatchUrl, null);
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl, RequestDetails theRequestDetails) {
		return update(theResource, theMatchUrl, true, theRequestDetails);
	}

	@Override
	public DaoMethodOutcome update(
			T theResource, String theMatchUrl, boolean thePerformIndexing, RequestDetails theRequestDetails) {
		return update(theResource, theMatchUrl, thePerformIndexing, false, theRequestDetails, new TransactionDetails());
	}

	@Override
	public DaoMethodOutcome update(
			T theResource,
			String theMatchUrl,
			boolean thePerformIndexing,
			boolean theForceUpdateVersion,
			RequestDetails theRequest,
			@Nonnull TransactionDetails theTransactionDetails) {
		if (theResource == null) {
			String msg = getContext().getLocalizer().getMessage(BaseStorageDao.class, "missingBody");
			throw new InvalidRequestException(Msg.code(986) + msg);
		}
		if (!theResource.getIdElement().hasIdPart() && isBlank(theMatchUrl)) {
			String type = myFhirContext.getResourceType(theResource);
			String msg = myFhirContext.getLocalizer().getMessage(BaseStorageDao.class, "updateWithNoId", type);
			throw new InvalidRequestException(Msg.code(987) + msg);
		}

		/*
		 * Resource updates will modify/update the version of the resource with the new version. This is generally helpful,
		 * but leads to issues if the transaction is rolled back and retried. So if we do a rollback, we reset the resource
		 * version to what it was.
		 */
		String id = theResource.getIdElement().getValue();
		Runnable onRollback = () -> theResource.getIdElement().setValue(id);

		RequestPartitionId requestPartitionId = myRequestPartitionHelperService.determineCreatePartitionForRequest(
				theRequest, theResource, getResourceName());

		Callable<DaoMethodOutcome> updateCallback;
		if (myStorageSettings.isUpdateWithHistoryRewriteEnabled()
				&& theRequest != null
				&& theRequest.isRewriteHistory()) {
			updateCallback = () ->
					doUpdateWithHistoryRewrite(theResource, theRequest, theTransactionDetails, requestPartitionId);
		} else {
			updateCallback = () -> doUpdate(
					theResource,
					theMatchUrl,
					thePerformIndexing,
					theForceUpdateVersion,
					theRequest,
					theTransactionDetails,
					requestPartitionId);
		}

		// Execute the update in a retryable transaction
		return myTransactionService
				.withRequest(theRequest)
				.withTransactionDetails(theTransactionDetails)
				.withRequestPartitionId(requestPartitionId)
				.onRollback(onRollback)
				.execute(updateCallback);
	}

	private DaoMethodOutcome doUpdate(
			T theResource,
			String theMatchUrl,
			boolean thePerformIndexing,
			boolean theForceUpdateVersion,
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails,
			RequestPartitionId theRequestPartitionId) {

		preProcessResourceForStorage(theResource);
		preProcessResourceForStorage(theResource, theRequest, theTransactionDetails, thePerformIndexing);

		ResourceTable entity = null;

		IIdType resourceId;
		RestOperationTypeEnum update = RestOperationTypeEnum.UPDATE;
		if (isNotBlank(theMatchUrl)) {
			// Validate that the supplied resource matches the conditional.
			Set<JpaPid> match = myMatchResourceUrlService.processMatchUrl(
					theMatchUrl, myResourceType, theTransactionDetails, theRequest, theResource);
			if (match.size() > 1) {
				String msg = getContext()
						.getLocalizer()
						.getMessageSanitized(
								BaseStorageDao.class,
								"transactionOperationWithMultipleMatchFailure",
								"UPDATE",
								myResourceName,
								theMatchUrl,
								match.size());
				throw new PreconditionFailedException(Msg.code(988) + msg);
			} else if (match.size() == 1) {
				JpaPid pid = match.iterator().next();
				entity = myEntityManager.find(ResourceTable.class, pid.getId());
				resourceId = entity.getIdDt();
				if (myFhirContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)
						&& theResource.getIdElement().getIdPart() != null) {
					if (!Objects.equals(theResource.getIdElement().getIdPart(), resourceId.getIdPart())) {
						String msg = getContext()
								.getLocalizer()
								.getMessageSanitized(
										BaseStorageDao.class,
										"transactionOperationWithIdNotMatchFailure",
										"UPDATE",
										theMatchUrl);
						throw new InvalidRequestException(Msg.code(2279) + msg);
					}
				}
			} else {
				// assign UUID if no id provided in the request (numeric id mode is handled in doCreateForPostOrPut)
				if (!theResource.getIdElement().hasIdPart()
						&& getStorageSettings().getResourceServerIdStrategy()
								== JpaStorageSettings.IdStrategyEnum.UUID) {
					theResource.setId(UUID.randomUUID().toString());
					theResource.setUserData(JpaConstants.RESOURCE_ID_SERVER_ASSIGNED, Boolean.TRUE);
				}
				DaoMethodOutcome outcome = doCreateForPostOrPut(
						theRequest,
						theResource,
						theMatchUrl,
						false,
						thePerformIndexing,
						theRequestPartitionId,
						update,
						theTransactionDetails);

				// Pre-cache the match URL
				if (outcome.getPersistentId() != null) {
					myMatchResourceUrlService.matchUrlResolved(
							theTransactionDetails, getResourceName(), theMatchUrl, (JpaPid) outcome.getPersistentId());
				}

				return outcome;
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

			boolean create = false;

			if (theRequest != null) {
				String existenceCheck = theRequest.getHeader(JpaConstants.HEADER_UPSERT_EXISTENCE_CHECK);
				if (JpaConstants.HEADER_UPSERT_EXISTENCE_CHECK_DISABLED.equals(existenceCheck)) {
					create = true;
				}
			}

			if (!create) {
				try {
					entity = readEntityLatestVersion(resourceId, theRequestPartitionId, theTransactionDetails);
				} catch (ResourceNotFoundException e) {
					create = true;
				}
			}

			if (create) {
				return doCreateForPostOrPut(
						theRequest,
						theResource,
						null,
						false,
						thePerformIndexing,
						theRequestPartitionId,
						update,
						theTransactionDetails);
			}
		}

		// Start
		return doUpdateForUpdateOrPatch(
				theRequest,
				resourceId,
				theMatchUrl,
				thePerformIndexing,
				theForceUpdateVersion,
				theResource,
				entity,
				update,
				theTransactionDetails);
	}

	@Override
	protected DaoMethodOutcome doUpdateForUpdateOrPatch(
			RequestDetails theRequest,
			IIdType theResourceId,
			String theMatchUrl,
			boolean thePerformIndexing,
			boolean theForceUpdateVersion,
			T theResource,
			IBasePersistedResource theEntity,
			RestOperationTypeEnum theOperationType,
			TransactionDetails theTransactionDetails) {

		/*
		 * We stored a resource searchUrl at creation time to prevent resource duplication.
		 * We'll clear any currently existing urls from the db, otherwise we could hit
		 * duplicate index violations if we try to add another (after this create/update)
		 */
		ResourceTable entity = (ResourceTable) theEntity;
		if (entity.isSearchUrlPresent()) {
			myResourceSearchUrlSvc.deleteByResId(
					(Long) theEntity.getPersistentId().getId());
			entity.setSearchUrlPresent(false);
		}

		return super.doUpdateForUpdateOrPatch(
				theRequest,
				theResourceId,
				theMatchUrl,
				thePerformIndexing,
				theForceUpdateVersion,
				theResource,
				theEntity,
				theOperationType,
				theTransactionDetails);
	}

	/**
	 * Method for updating the historical version of the resource when a history version id is included in the request.
	 *
	 * @param theResource           to be saved
	 * @param theRequest            details of the request
	 * @param theTransactionDetails details of the transaction
	 * @return the outcome of the operation
	 */
	private DaoMethodOutcome doUpdateWithHistoryRewrite(
			T theResource,
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails,
			RequestPartitionId theRequestPartitionId) {
		StopWatch w = new StopWatch();

		// No need for indexing as this will update a non-current version of the resource which will not be searchable
		preProcessResourceForStorage(theResource, theRequest, theTransactionDetails, false);

		BaseHasResource entity;
		BaseHasResource currentEntity;

		IIdType resourceId;

		resourceId = theResource.getIdElement();
		assert resourceId != null;
		assert resourceId.hasIdPart();

		try {
			currentEntity =
					readEntityLatestVersion(resourceId.toVersionless(), theRequestPartitionId, theTransactionDetails);

			if (!resourceId.hasVersionIdPart()) {
				throw new InvalidRequestException(
						Msg.code(2093) + "Invalid resource ID, ID must contain a history version");
			}
			entity = readEntity(resourceId, theRequest);
			validateResourceType(entity);
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException(
					Msg.code(2087) + "Resource not found [" + resourceId + "] - Doesn't exist");
		}

		if (resourceId.hasResourceType() && !resourceId.getResourceType().equals(getResourceName())) {
			throw new UnprocessableEntityException(
					Msg.code(2088) + "Invalid resource ID[" + entity.getIdDt().toUnqualifiedVersionless() + "] of type["
							+ entity.getResourceType() + "] - Does not match expected [" + getResourceName() + "]");
		}
		assert resourceId.hasVersionIdPart();

		boolean wasDeleted = isDeleted(entity);
		entity.setDeleted(null);
		boolean isUpdatingCurrent = resourceId.hasVersionIdPart()
				&& Long.parseLong(resourceId.getVersionIdPart()) == currentEntity.getVersion();
		IBasePersistedResource<?> savedEntity = updateHistoryEntity(
				theRequest, theResource, currentEntity, entity, resourceId, theTransactionDetails, isUpdatingCurrent);
		DaoMethodOutcome outcome = toMethodOutcome(
						theRequest, savedEntity, theResource, null, RestOperationTypeEnum.UPDATE)
				.setCreated(wasDeleted);

		populateOperationOutcomeForUpdate(w, outcome, null, RestOperationTypeEnum.UPDATE);

		return outcome;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public MethodOutcome validate(
			T theResource,
			IIdType theId,
			String theRawResource,
			EncodingEnum theEncoding,
			ValidationModeEnum theMode,
			String theProfile,
			RequestDetails theRequest) {
		TransactionDetails transactionDetails = new TransactionDetails();

		if (theMode == ValidationModeEnum.DELETE) {
			if (theId == null || !theId.hasIdPart()) {
				throw new InvalidRequestException(
						Msg.code(991) + "No ID supplied. ID is required when validating with mode=DELETE");
			}
			final ResourceTable entity = readEntityLatestVersion(theId, theRequest, transactionDetails);

			// Validate that there are no resources pointing to the candidate that
			// would prevent deletion
			DeleteConflictList deleteConflicts = new DeleteConflictList();
			if (getStorageSettings().isEnforceReferentialIntegrityOnDelete()) {
				myDeleteConflictService.validateOkToDelete(
						deleteConflicts, entity, true, theRequest, new TransactionDetails());
			}
			DeleteConflictUtil.validateDeleteConflictsEmptyOrThrowException(getContext(), deleteConflicts);

			IBaseOperationOutcome oo = createInfoOperationOutcome("Ok to delete");
			return new MethodOutcome(new IdDt(theId.getValue()), oo);
		}

		FhirValidator validator = getContext().newValidator();
		validator.setInterceptorBroadcaster(
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequest));
		validator.registerValidatorModule(getInstanceValidator());
		validator.registerValidatorModule(new IdChecker(theMode));

		IBaseResource resourceToValidateById = null;
		if (theId != null && theId.hasResourceType() && theId.hasIdPart()) {
			Class<? extends IBaseResource> type =
					getContext().getResourceDefinition(theId.getResourceType()).getImplementingClass();
			IFhirResourceDao<? extends IBaseResource> dao = myDaoRegistry.getResourceDaoOrNull(type);
			resourceToValidateById = dao.read(theId, theRequest);
		}

		ValidationResult result;
		ValidationOptions options = new ValidationOptions().addProfileIfNotBlank(theProfile);

		if (theResource == null) {
			if (resourceToValidateById != null) {
				result = validator.validateWithResult(resourceToValidateById, options);
			} else {
				String msg = getContext().getLocalizer().getMessage(BaseStorageDao.class, "cantValidateWithNoResource");
				throw new InvalidRequestException(Msg.code(992) + msg);
			}
		} else if (isNotBlank(theRawResource)) {
			result = validator.validateWithResult(theRawResource, options);
		} else {
			result = validator.validateWithResult(theResource, options);
		}

		MethodOutcome retVal = new MethodOutcome();
		retVal.setOperationOutcome(result.toOperationOutcome());
		// Note an earlier version of this code returned PreconditionFailedException when the validation
		// failed, but we since realized the spec requires we return 200 regardless of the validation result.
		return retVal;
	}

	/**
	 * Get the resource definition from the criteria which specifies the resource type
	 */
	@Override
	public RuntimeResourceDefinition validateCriteriaAndReturnResourceDefinition(String criteria) {
		String resourceName;
		if (criteria == null || criteria.trim().isEmpty()) {
			throw new IllegalArgumentException(Msg.code(994) + "Criteria cannot be empty");
		}
		if (criteria.contains("?")) {
			resourceName = criteria.substring(0, criteria.indexOf("?"));
		} else {
			resourceName = criteria;
		}

		return getContext().getResourceDefinition(resourceName);
	}

	private void validateGivenIdIsAppropriateToRetrieveResource(IIdType theId, BaseHasResource entity) {
		if (!entity.getIdDt().getIdPart().equals(theId.getIdPart())) {
			// This means that the resource with the given numeric ID exists, but it has a "forced ID", meaning
			// that
			// as far as the outside world is concerned, the given ID doesn't exist (it's just an internal
			// pointer
			// to the
			// forced ID)
			throw new ResourceNotFoundException(Msg.code(2000) + theId);
		}
	}

	private void validateResourceType(BaseHasResource entity) {
		validateResourceType(entity, myResourceName);
	}

	private void validateResourceTypeAndThrowInvalidRequestException(IIdType theId) {
		if (theId.hasResourceType() && !theId.getResourceType().equals(myResourceName)) {
			// Note- Throw a HAPI FHIR exception here so that hibernate doesn't try to translate it into a database
			// exception
			throw new InvalidRequestException(Msg.code(996) + "Incorrect resource type (" + theId.getResourceType()
					+ ") for this DAO, wanted: " + myResourceName);
		}
	}

	@VisibleForTesting
	public void setIdHelperSvcForUnitTest(IIdHelperService theIdHelperService) {
		myIdHelperService = theIdHelperService;
	}

	private static class IdChecker implements IValidatorModule {

		private final ValidationModeEnum myMode;

		IdChecker(ValidationModeEnum theMode) {
			myMode = theMode;
		}

		@Override
		public void validateResource(IValidationContext<IBaseResource> theCtx) {
			IBaseResource resource = theCtx.getResource();
			if (resource instanceof Parameters) {
				List<ParametersParameterComponent> params = ((Parameters) resource).getParameter();
				params = params.stream()
						.filter(param -> param.getName().contains("resource"))
						.collect(Collectors.toList());
				resource = params.get(0).getResource();
			}
			boolean hasId = resource.getIdElement().hasIdPart();
			if (myMode == ValidationModeEnum.CREATE) {
				if (hasId) {
					throw new UnprocessableEntityException(
							Msg.code(997) + "Resource has an ID - ID must not be populated for a FHIR create");
				}
			} else if (myMode == ValidationModeEnum.UPDATE) {
				if (!hasId) {
					throw new UnprocessableEntityException(
							Msg.code(998) + "Resource has no ID - ID must be populated for a FHIR update");
				}
			}
		}
	}
}
