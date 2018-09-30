package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.util.DeleteConflict;
import ca.uhn.fhir.jpa.util.ExpungeOptions;
import ca.uhn.fhir.jpa.util.ExpungeOutcome;
import ca.uhn.fhir.jpa.util.IReindexController;
import ca.uhn.fhir.jpa.util.jsonpatch.JsonPatchUtils;
import ca.uhn.fhir.jpa.util.xmlpatch.XmlPatchUtils;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.param.QualifierDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.*;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.IServerOperationInterceptor;
import ca.uhn.fhir.rest.server.method.SearchMethodBinding;
import ca.uhn.fhir.util.*;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.r4.model.InstantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Transactional(propagation = Propagation.REQUIRED)
public abstract class BaseHapiFhirResourceDao<T extends IBaseResource> extends BaseHapiFhirDao<T> implements IFhirResourceDao<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiFhirResourceDao.class);
	@Autowired
	protected PlatformTransactionManager myPlatformTransactionManager;
	@Autowired(required = false)
	protected IFulltextSearchSvc mySearchDao;
	@Autowired()
	protected ISearchResultDao mySearchResultDao;
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	private IResourceLinkDao myResourceLinkDao;
	private String myResourceName;
	private Class<T> myResourceType;
	private String mySecondaryPrimaryKeyParamName;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private IReindexController myReindexController;

	@Override
	public void addTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm, String theLabel) {
		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId);
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
		return create(theResource, null, true, null);
	}

	@Override
	public DaoMethodOutcome create(final T theResource, RequestDetails theRequestDetails) {
		return create(theResource, null, true, theRequestDetails);
	}

	@Override
	public DaoMethodOutcome create(final T theResource, String theIfNoneExist) {
		return create(theResource, theIfNoneExist, null);
	}

	@Override
	public DaoMethodOutcome create(T theResource, String theIfNoneExist, boolean thePerformIndexing, RequestDetails theRequestDetails) {
		if (isNotBlank(theResource.getIdElement().getIdPart())) {
			if (getContext().getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
				String message = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedId", theResource.getIdElement().getIdPart());
				throw new InvalidRequestException(message, createErrorOperationOutcome(message, "processing"));
			} else {
				// As of DSTU3, ID and version in the body should be ignored for a create/update
				theResource.setId("");
			}
		}

		if (myDaoConfig.getResourceServerIdStrategy() == DaoConfig.IdStrategyEnum.UUID) {
			theResource.setId(UUID.randomUUID().toString());
		}

		return doCreate(theResource, theIfNoneExist, thePerformIndexing, new Date(), theRequestDetails);
	}

	@Override
	public DaoMethodOutcome create(final T theResource, String theIfNoneExist, RequestDetails theRequestDetails) {
		return create(theResource, theIfNoneExist, true, theRequestDetails);
	}

	public IBaseOperationOutcome createErrorOperationOutcome(String theMessage, String theCode) {
		return createOperationOutcome(OO_SEVERITY_ERROR, theMessage, theCode);
	}

	public IBaseOperationOutcome createInfoOperationOutcome(String theMessage) {
		return createOperationOutcome(OO_SEVERITY_INFO, theMessage, "informational");
	}

	protected abstract IBaseOperationOutcome createOperationOutcome(String theSeverity, String theMessage, String theCode);

	@Override
	public DaoMethodOutcome delete(IIdType theId) {
		return delete(theId, null);
	}

	@Override
	public DaoMethodOutcome delete(IIdType theId, List<DeleteConflict> theDeleteConflicts, RequestDetails theReques) {
		if (theId == null || !theId.hasIdPart()) {
			throw new InvalidRequestException("Can not perform delete, no ID provided");
		}
		final ResourceTable entity = readEntityLatestVersion(theId);
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

		// Notify IServerOperationInterceptors about pre-action call
		if (theReques != null) {
			theReques.getRequestOperationCallback().resourcePreDelete(resourceToDelete);
		}
		for (IServerInterceptor next : getConfig().getInterceptors()) {
			if (next instanceof IServerOperationInterceptor) {
				((IServerOperationInterceptor) next).resourcePreDelete(theReques, resourceToDelete);
			}
		}

		validateOkToDelete(theDeleteConflicts, entity, false);

		preDelete(resourceToDelete, entity);

		// Notify interceptors
		if (theReques != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theReques, getContext(), theId.getResourceType(), theId);
			notifyInterceptors(RestOperationTypeEnum.DELETE, requestDetails);
		}

		Date updateTime = new Date();
		ResourceTable savedEntity = updateEntity(theReques, null, entity, updateTime, updateTime);
		resourceToDelete.setId(entity.getIdDt());

		// Notify JPA interceptors
		if (theReques != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theReques, getContext(), theId.getResourceType(), theId);
			theReques.getRequestOperationCallback().resourceDeleted(resourceToDelete);
		}
		for (IServerInterceptor next : getConfig().getInterceptors()) {
			if (next instanceof IServerOperationInterceptor) {
				((IServerOperationInterceptor) next).resourceDeleted(theReques, resourceToDelete);
			}
		}

		DaoMethodOutcome outcome = toMethodOutcome(savedEntity, resourceToDelete).setCreated(true);

		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(getContext());
		String message = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulDeletes", 1, w.getMillis());
		String severity = "information";
		String code = "informational";
		OperationOutcomeUtil.addIssue(getContext(), oo, severity, message, null, code);
		outcome.setOperationOutcome(oo);

		return outcome;
	}

	@Override
	public DaoMethodOutcome delete(IIdType theId, RequestDetails theRequestDetails) {
		List<DeleteConflict> deleteConflicts = new ArrayList<DeleteConflict>();
		StopWatch w = new StopWatch();

		DaoMethodOutcome retVal = delete(theId, deleteConflicts, theRequestDetails);

		validateDeleteConflictsEmptyOrThrowException(deleteConflicts);

		ourLog.debug("Processed delete on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
		return retVal;
	}

	/**
	 * This method gets called by {@link #deleteByUrl(String, List, RequestDetails)} as well as by
	 * transaction processors
	 */
	@Override
	public DeleteMethodOutcome deleteByUrl(String theUrl, List<DeleteConflict> deleteConflicts, RequestDetails theRequest) {
		StopWatch w = new StopWatch();

		Set<Long> resource = processMatchUrl(theUrl, myResourceType);
		if (resource.size() > 1) {
			if (myDaoConfig.isAllowMultipleDelete() == false) {
				throw new PreconditionFailedException(getContext().getLocalizer().getMessage(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "DELETE", theUrl, resource.size()));
			}
		}

		List<ResourceTable> deletedResources = new ArrayList<ResourceTable>();
		for (Long pid : resource) {
			ResourceTable entity = myEntityManager.find(ResourceTable.class, pid);
			deletedResources.add(entity);

			T resourceToDelete = toResource(myResourceType, entity, null, false);

			// Notify IServerOperationInterceptors about pre-action call
			if (theRequest != null) {
				theRequest.getRequestOperationCallback().resourcePreDelete(resourceToDelete);
			}
			for (IServerInterceptor next : getConfig().getInterceptors()) {
				if (next instanceof IServerOperationInterceptor) {
					((IServerOperationInterceptor) next).resourcePreDelete(theRequest, resourceToDelete);
				}
			}

			validateOkToDelete(deleteConflicts, entity, false);

			// Notify interceptors
			IdDt idToDelete = entity.getIdDt();
			if (theRequest != null) {
				ActionRequestDetails requestDetails = new ActionRequestDetails(theRequest, idToDelete.getResourceType(), idToDelete);
				notifyInterceptors(RestOperationTypeEnum.DELETE, requestDetails);
			}

			// Perform delete
			Date updateTime = new Date();
			updateEntity(theRequest, null, entity, updateTime, updateTime);
			resourceToDelete.setId(entity.getIdDt());

			// Notify JPA interceptors
			if (theRequest != null) {
				theRequest.getRequestOperationCallback().resourceDeleted(resourceToDelete);
				ActionRequestDetails requestDetails = new ActionRequestDetails(theRequest, idToDelete.getResourceType(), idToDelete);
			}
			for (IServerInterceptor next : getConfig().getInterceptors()) {
				if (next instanceof IServerOperationInterceptor) {
					((IServerOperationInterceptor) next).resourceDeleted(theRequest, resourceToDelete);
				}
			}
		}

		IBaseOperationOutcome oo;
		if (deletedResources.isEmpty()) {
			oo = OperationOutcomeUtil.newInstance(getContext());
			String message = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "unableToDeleteNotFound", theUrl);
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

	@Override
	public DeleteMethodOutcome deleteByUrl(String theUrl, RequestDetails theRequestDetails) {
		List<DeleteConflict> deleteConflicts = new ArrayList<>();

		DeleteMethodOutcome outcome = deleteByUrl(theUrl, deleteConflicts, theRequestDetails);

		validateDeleteConflictsEmptyOrThrowException(deleteConflicts);

		return outcome;
	}

	@PostConstruct
	public void detectSearchDaoDisabled() {
		if (mySearchDao != null && mySearchDao.isDisabled()) {
			mySearchDao = null;
		}
	}

	private DaoMethodOutcome doCreate(T theResource, String theIfNoneExist, boolean thePerformIndexing, Date theUpdateTime, RequestDetails theRequest) {
		StopWatch w = new StopWatch();

		preProcessResourceForStorage(theResource);

		ResourceTable entity = new ResourceTable();
		entity.setResourceType(toResourceName(theResource));

		if (isNotBlank(theIfNoneExist)) {
			Set<Long> match = processMatchUrl(theIfNoneExist, myResourceType);
			if (match.size() > 1) {
				String msg = getContext().getLocalizer().getMessage(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "CREATE", theIfNoneExist, match.size());
				throw new PreconditionFailedException(msg);
			} else if (match.size() == 1) {
				Long pid = match.iterator().next();
				entity = myEntityManager.find(ResourceTable.class, pid);
				return toMethodOutcome(entity, theResource).setCreated(false);
			}
		}

		if (isNotBlank(theResource.getIdElement().getIdPart())) {
			if (isValidPid(theResource.getIdElement())) {
				throw new UnprocessableEntityException(
					"This server cannot create an entity with a user-specified numeric ID - Client should not specify an ID when creating a new resource, or should include at least one letter in the ID to force a client-defined ID");
			}
			createForcedIdIfNeeded(entity, theResource.getIdElement());
		}

		// Notify interceptors
		if (theRequest != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequest, getContext(), theResource);
			notifyInterceptors(RestOperationTypeEnum.CREATE, requestDetails);
		}

		// Notify JPA interceptors
		if (theRequest != null) {
			theRequest.getRequestOperationCallback().resourcePreCreate(theResource);
		}
		for (IServerInterceptor next : getConfig().getInterceptors()) {
			if (next instanceof IServerOperationInterceptor) {
				((IServerOperationInterceptor) next).resourcePreCreate(theRequest, theResource);
			}
		}

		// Perform actual DB update
		ResourceTable updatedEntity = updateEntity(theRequest, theResource, entity, null, thePerformIndexing, thePerformIndexing, theUpdateTime, false, thePerformIndexing);
		theResource.setId(entity.getIdDt());


		/*
		 * If we aren't indexing (meaning we're probably executing a sub-operation within a transaction),
		 * we'll manually increase the version. This is important because we want the updated version number
		 * to be reflected in the resource shared with interceptors
		 */
		if (!thePerformIndexing) {
			incrementId(theResource, entity, theResource.getIdElement());
		}

		// Notify JPA interceptors
		if (!updatedEntity.isUnchangedInCurrentOperation()) {
			if (theRequest != null) {
				theRequest.getRequestOperationCallback().resourceCreated(theResource);
			}
			for (IServerInterceptor next : getConfig().getInterceptors()) {
				if (next instanceof IServerOperationInterceptor) {
					((IServerOperationInterceptor) next).resourceCreated(theRequest, theResource);
				}
			}
		}

		DaoMethodOutcome outcome = toMethodOutcome(entity, theResource).setCreated(true);
		if (!thePerformIndexing) {
			outcome.setId(theResource.getIdElement());
		}

		String msg = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulCreate", outcome.getId(), w.getMillisAndRestart());
		outcome.setOperationOutcome(createInfoOperationOutcome(msg));

		ourLog.debug(msg);
		return outcome;
	}

	private <MT extends IBaseMetaType> void doMetaAdd(MT theMetaAdd, BaseHasResource entity) {
		List<TagDefinition> tags = toTagList(theMetaAdd);

		for (TagDefinition nextDef : tags) {

			boolean hasTag = false;
			for (BaseTag next : new ArrayList<>(entity.getTags())) {
				if (ObjectUtil.equals(next.getTag().getTagType(), nextDef.getTagType()) &&
					ObjectUtil.equals(next.getTag().getSystem(), nextDef.getSystem()) &&
					ObjectUtil.equals(next.getTag().getCode(), nextDef.getCode())) {
					hasTag = true;
					break;
				}
			}

			if (!hasTag) {
				entity.setHasTags(true);

				TagDefinition def = getTagOrNull(nextDef.getTagType(), nextDef.getSystem(), nextDef.getCode(), nextDef.getDisplay());
				if (def != null) {
					BaseTag newEntity = entity.addTag(def);
					if (newEntity.getTagId() == null) {
						myEntityManager.persist(newEntity);
					}
				}
			}
		}

		validateMetaCount(entity.getTags().size());

		myEntityManager.merge(entity);
	}

	private <MT extends IBaseMetaType> void doMetaDelete(MT theMetaDel, BaseHasResource entity) {
		List<TagDefinition> tags = toTagList(theMetaDel);

		//@formatter:off
		for (TagDefinition nextDef : tags) {
			for (BaseTag next : new ArrayList<BaseTag>(entity.getTags())) {
				if (ObjectUtil.equals(next.getTag().getTagType(), nextDef.getTagType()) &&
					ObjectUtil.equals(next.getTag().getSystem(), nextDef.getSystem()) &&
					ObjectUtil.equals(next.getTag().getCode(), nextDef.getCode())) {
					myEntityManager.remove(next);
					entity.getTags().remove(next);
				}
			}
		}
		//@formatter:on

		if (entity.getTags().isEmpty()) {
			entity.setHasTags(false);
		}

		myEntityManager.merge(entity);
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public ExpungeOutcome expunge(IIdType theId, ExpungeOptions theExpungeOptions) {
		BaseHasResource entity = readEntity(theId);
		if (theId.hasVersionIdPart()) {
			BaseHasResource currentVersion = readEntity(theId.toVersionless());
			if (entity.getVersion() == currentVersion.getVersion()) {
				throw new PreconditionFailedException("Can not perform version-specific expunge of resource " + theId.toUnqualified().getValue() + " as this is the current version");
			}

			return doExpunge(getResourceName(), entity.getResourceId(), entity.getVersion(), theExpungeOptions);
		}

		return doExpunge(getResourceName(), entity.getResourceId(), null, theExpungeOptions);
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public ExpungeOutcome expunge(ExpungeOptions theExpungeOptions) {
		ourLog.info("Beginning TYPE[{}] expunge operation", getResourceName());

		return doExpunge(getResourceName(), null, null, theExpungeOptions);
	}

	@Override
	public TagList getAllResourceTags(RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails);
		notifyInterceptors(RestOperationTypeEnum.GET_TAGS, requestDetails);

		StopWatch w = new StopWatch();
		TagList tags = super.getTags(myResourceType, null);
		ourLog.debug("Processed getTags on {} in {}ms", myResourceName, w.getMillisAndRestart());
		return tags;
	}

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
	public TagList getTags(IIdType theResourceId, RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, null, theResourceId);
		notifyInterceptors(RestOperationTypeEnum.GET_TAGS, requestDetails);

		StopWatch w = new StopWatch();
		TagList retVal = super.getTags(myResourceType, theResourceId);
		ourLog.debug("Processed getTags on {} in {}ms", theResourceId, w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public IBundleProvider history(Date theSince, Date theUntil, RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails);
		notifyInterceptors(RestOperationTypeEnum.HISTORY_TYPE, requestDetails);

		StopWatch w = new StopWatch();
		IBundleProvider retVal = super.history(myResourceName, null, theSince, theUntil);
		ourLog.debug("Processed history on {} in {}ms", myResourceName, w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public IBundleProvider history(final IIdType theId, final Date theSince, Date theUntil, RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), theId);
		notifyInterceptors(RestOperationTypeEnum.HISTORY_INSTANCE, requestDetails);

		StopWatch w = new StopWatch();

		IIdType id = theId.withResourceType(myResourceName).toUnqualifiedVersionless();
		BaseHasResource entity = readEntity(id);

		IBundleProvider retVal = super.history(myResourceName, entity.getId(), theSince, theUntil);

		ourLog.debug("Processed history on {} in {}ms", id, w.getMillisAndRestart());
		return retVal;
	}

	protected boolean isPagingProviderDatabaseBacked(RequestDetails theRequestDetails) {
		if (theRequestDetails == null || theRequestDetails.getServer() == null) {
			return false;
		}
		return theRequestDetails.getServer().getPagingProvider() instanceof DatabaseBackedPagingProvider;
	}

	protected void markResourcesMatchingExpressionAsNeedingReindexing(Boolean theCurrentlyReindexing, String theExpression) {
		// Avoid endless loops
		if (Boolean.TRUE.equals(theCurrentlyReindexing)) {
			return;
		}

		if (myDaoConfig.isMarkResourcesForReindexingUponSearchParameterChange()) {
			if (isNotBlank(theExpression)) {
				final String resourceType = theExpression.substring(0, theExpression.indexOf('.'));
				ourLog.debug("Marking all resources of type {} for reindexing due to updated search parameter with path: {}", resourceType, theExpression);

				TransactionTemplate txTemplate = new TransactionTemplate(myPlatformTransactionManager);
				txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
				Integer updatedCount = txTemplate.execute(new TransactionCallback<Integer>() {
					@Override
					public @NonNull
					Integer doInTransaction(@Nonnull TransactionStatus theStatus) {
						return myResourceTableDao.markResourcesOfTypeAsRequiringReindexing(resourceType);
					}
				});

				ourLog.debug("Marked {} resources for reindexing", updatedCount);
			}
		}

		mySearchParamRegistry.requestRefresh();
		myReindexController.requestReindex();
	}

	@Override
	public <MT extends IBaseMetaType> MT metaAddOperation(IIdType theResourceId, MT theMetaAdd, RequestDetails theRequestDetails) {
		// Notify interceptors
		if (theRequestDetails != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), theResourceId);
			notifyInterceptors(RestOperationTypeEnum.META_ADD, requestDetails);
		}

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theResourceId);
		if (entity == null) {
			throw new ResourceNotFoundException(theResourceId);
		}

		ResourceTable latestVersion = readEntityLatestVersion(theResourceId);
		if (latestVersion.getVersion() != entity.getVersion()) {
			doMetaAdd(theMetaAdd, entity);
		} else {
			doMetaAdd(theMetaAdd, latestVersion);

			// Also update history entry
			ResourceHistoryTable history = myResourceHistoryTableDao.findForIdAndVersion(entity.getId(), entity.getVersion());
			doMetaAdd(theMetaAdd, history);
		}

		ourLog.debug("Processed metaAddOperation on {} in {}ms", new Object[] {theResourceId, w.getMillisAndRestart()});

		@SuppressWarnings("unchecked")
		MT retVal = (MT) metaGetOperation(theMetaAdd.getClass(), theResourceId, theRequestDetails);
		return retVal;
	}

	@Override
	public <MT extends IBaseMetaType> MT metaDeleteOperation(IIdType theResourceId, MT theMetaDel, RequestDetails theRequestDetails) {
		// Notify interceptors
		if (theRequestDetails != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), theResourceId);
			notifyInterceptors(RestOperationTypeEnum.META_DELETE, requestDetails);
		}

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theResourceId);
		if (entity == null) {
			throw new ResourceNotFoundException(theResourceId);
		}

		ResourceTable latestVersion = readEntityLatestVersion(theResourceId);
		if (latestVersion.getVersion() != entity.getVersion()) {
			doMetaDelete(theMetaDel, entity);
		} else {
			doMetaDelete(theMetaDel, latestVersion);

			// Also update history entry
			ResourceHistoryTable history = myResourceHistoryTableDao.findForIdAndVersion(entity.getId(), entity.getVersion());
			doMetaDelete(theMetaDel, history);
		}

		myEntityManager.flush();

		ourLog.debug("Processed metaDeleteOperation on {} in {}ms", new Object[] {theResourceId.getValue(), w.getMillisAndRestart()});

		@SuppressWarnings("unchecked")
		MT retVal = (MT) metaGetOperation(theMetaDel.getClass(), theResourceId, theRequestDetails);
		return retVal;
	}

	@Override
	public <MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, IIdType theId, RequestDetails theRequestDetails) {
		// Notify interceptors
		if (theRequestDetails != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), theId);
			notifyInterceptors(RestOperationTypeEnum.META, requestDetails);
		}

		Set<TagDefinition> tagDefs = new HashSet<>();
		BaseHasResource entity = readEntity(theId);
		for (BaseTag next : entity.getTags()) {
			tagDefs.add(next.getTag());
		}
		MT retVal = toMetaDt(theType, tagDefs);

		retVal.setLastUpdated(entity.getUpdatedDate());
		retVal.setVersionId(Long.toString(entity.getVersion()));

		return retVal;
	}

	@Override
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
	public DaoMethodOutcome patch(IIdType theId, PatchTypeEnum thePatchType, String thePatchBody, RequestDetails theRequestDetails) {
		ResourceTable entityToUpdate = readEntityLatestVersion(theId);
		if (theId.hasVersionIdPart()) {
			if (theId.getVersionIdPartAsLong() != entityToUpdate.getVersion()) {
				throw new ResourceVersionConflictException("Version " + theId.getVersionIdPart() + " is not the most recent version of this resource, unable to apply patch");
			}
		}

		validateResourceType(entityToUpdate);

		IBaseResource resourceToUpdate = toResource(entityToUpdate, false);
		IBaseResource destination;
		if (thePatchType == PatchTypeEnum.JSON_PATCH) {
			destination = JsonPatchUtils.apply(getContext(), resourceToUpdate, thePatchBody);
		} else {
			destination = XmlPatchUtils.apply(getContext(), resourceToUpdate, thePatchBody);
		}

		@SuppressWarnings("unchecked")
		T destinationCasted = (T) destination;
		return update(destinationCasted, null, true, theRequestDetails);
	}

	@PostConstruct
	public void postConstruct() {
		RuntimeResourceDefinition def = getContext().getResourceDefinition(myResourceType);
		myResourceName = def.getName();

		if (mySecondaryPrimaryKeyParamName != null) {
			RuntimeSearchParam sp = getSearchParamByName(def, mySecondaryPrimaryKeyParamName);
			if (sp == null) {
				throw new ConfigurationException("Unknown search param on resource[" + myResourceName + "] for secondary key[" + mySecondaryPrimaryKeyParamName + "]");
			}
			if (sp.getParamType() != RestSearchParameterTypeEnum.TOKEN) {
				throw new ConfigurationException("Search param on resource[" + myResourceName + "] for secondary key[" + mySecondaryPrimaryKeyParamName + "] is not a token type, only token is supported");
			}
		}

	}

	/**
	 * Subclasses may override to provide behaviour. Invoked within a delete
	 * transaction with the resource that is about to be deleted.
	 */
	protected void preDelete(T theResourceToDelete, ResourceTable theEntityToDelete) {
		// nothing by default
	}

	/**
	 * May be overridden by subclasses to validate resources prior to storage
	 *
	 * @param theResource The resource that is about to be stored
	 */
	protected void preProcessResourceForStorage(T theResource) {
		String type = getContext().getResourceDefinition(theResource).getName();
		if (!getResourceName().equals(type)) {
			throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "incorrectResourceType", type, getResourceName()));
		}

		if (theResource.getIdElement().hasIdPart()) {
			if (!theResource.getIdElement().isIdPartValid()) {
				throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "failedToCreateWithInvalidId", theResource.getIdElement().getIdPart()));
			}
		}

		/*
		 * Replace absolute references with relative ones if configured to do so
		 */
		if (getConfig().getTreatBaseUrlsAsLocal().isEmpty() == false) {
			FhirTerser t = getContext().newTerser();
			List<ResourceReferenceInfo> refs = t.getAllResourceReferences(theResource);
			for (ResourceReferenceInfo nextRef : refs) {
				IIdType refId = nextRef.getResourceReference().getReferenceElement();
				if (refId != null && refId.hasBaseUrl()) {
					if (getConfig().getTreatBaseUrlsAsLocal().contains(refId.getBaseUrl())) {
						IIdType newRefId = refId.toUnqualified();
						nextRef.getResourceReference().setReference(newRefId.getValue());
					}
				}
			}
		}
	}

	@Override
	public Set<Long> processMatchUrl(String theMatchUrl) {
		return processMatchUrl(theMatchUrl, getResourceType());
	}

	@Override
	public T read(IIdType theId) {
		return read(theId, null);
	}

	@Override
	public T read(IIdType theId, RequestDetails theRequestDetails) {
		validateResourceTypeAndThrowIllegalArgumentException(theId);

		// Notify interceptors
		if (theRequestDetails != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), theId);
			RestOperationTypeEnum operationType = theId.hasVersionIdPart() ? RestOperationTypeEnum.VREAD : RestOperationTypeEnum.READ;
			notifyInterceptors(operationType, requestDetails);
		}

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId);
		validateResourceType(entity);

		T retVal = toResource(myResourceType, entity, null, false);

		if (entity.getDeleted() != null) {
			throw new ResourceGoneException("Resource was deleted at " + new InstantType(entity.getDeleted()).getValueAsString());
		}

		ourLog.debug("Processed read on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public BaseHasResource readEntity(IIdType theId) {

		return readEntity(theId, true);
	}

	@Override
	public BaseHasResource readEntity(IIdType theId, boolean theCheckForForcedId) {
		validateResourceTypeAndThrowIllegalArgumentException(theId);

		Long pid = translateForcedIdToPid(getResourceName(), theId.getIdPart());
		BaseHasResource entity = myEntityManager.find(ResourceTable.class, pid);

		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}

		if (theId.hasVersionIdPart()) {
			if (theId.isVersionIdPartValidLong() == false) {
				throw new ResourceNotFoundException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "invalidVersion", theId.getVersionIdPart(), theId.toUnqualifiedVersionless()));
			}
			if (entity.getVersion() != theId.getVersionIdPartAsLong()) {
				entity = null;
			}
		}

		if (entity == null) {
			if (theId.hasVersionIdPart()) {
				TypedQuery<ResourceHistoryTable> q = myEntityManager
					.createQuery("SELECT t from ResourceHistoryTable t WHERE t.myResourceId = :RID AND t.myResourceType = :RTYP AND t.myResourceVersion = :RVER", ResourceHistoryTable.class);
				q.setParameter("RID", pid);
				q.setParameter("RTYP", myResourceName);
				q.setParameter("RVER", theId.getVersionIdPartAsLong());
				try {
					entity = q.getSingleResult();
				} catch (NoResultException e) {
					throw new ResourceNotFoundException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "invalidVersion", theId.getVersionIdPart(), theId.toUnqualifiedVersionless()));
				}
			}
		}

		validateResourceType(entity);

		if (theCheckForForcedId) {
			validateGivenIdIsAppropriateToRetrieveResource(theId, entity);
		}
		return entity;
	}

	protected ResourceTable readEntityLatestVersion(IIdType theId) {
		ResourceTable entity = myEntityManager.find(ResourceTable.class, translateForcedIdToPid(getResourceName(), theId.getIdPart()));
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}
		validateGivenIdIsAppropriateToRetrieveResource(theId, entity);
		return entity;
	}

	@Override
	public void reindex(T theResource, ResourceTable theEntity) {
		ourLog.debug("Indexing resource {} - PID {}", theEntity.getIdDt().getValue(), theEntity.getId());
		if (theResource != null) {
			CURRENTLY_REINDEXING.put(theResource, Boolean.TRUE);
		}
		updateEntity(null, theResource, theEntity, theEntity.getDeleted(), true, false, theEntity.getUpdatedDate(), true, false);
		if (theResource != null) {
			CURRENTLY_REINDEXING.put(theResource, null);
		}
	}

	@Override
	public void removeTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm) {
		removeTag(theId, theTagType, theScheme, theTerm, null);
	}

	@Override
	public void removeTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm, RequestDetails theRequestDetails) {
		// Notify interceptors
		if (theRequestDetails != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), theId);
			notifyInterceptors(RestOperationTypeEnum.DELETE_TAGS, requestDetails);
		}

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId);
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
	public IBundleProvider search(final SearchParameterMap theParams, RequestDetails theRequestDetails) {
		return search(theParams, theRequestDetails, null);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public IBundleProvider search(final SearchParameterMap theParams, RequestDetails theRequestDetails, HttpServletResponse theServletResponse) {

		if (myDaoConfig.getIndexMissingFields() == DaoConfig.IndexEnabledEnum.DISABLED) {
			for (List<List<? extends IQueryParameterType>> nextAnds : theParams.values()) {
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
		if (theRequestDetails != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getContext(), getResourceName(), null);
			notifyInterceptors(RestOperationTypeEnum.SEARCH_TYPE, requestDetails);

			if (theRequestDetails.isSubRequest()) {
				Integer max = myDaoConfig.getMaximumSearchResultCountInTransaction();
				if (max != null) {
					Validate.inclusiveBetween(1, Integer.MAX_VALUE, max.intValue(), "Maximum search result count in transaction ust be a positive integer");
					theParams.setLoadSynchronousUpTo(myDaoConfig.getMaximumSearchResultCountInTransaction());
				}
			}

			if (!isPagingProviderDatabaseBacked(theRequestDetails)) {
				theParams.setLoadSynchronous(true);
			}
		}

		CacheControlDirective cacheControlDirective = new CacheControlDirective();
		if (theRequestDetails != null) {
			cacheControlDirective.parse(theRequestDetails.getHeaders(Constants.HEADER_CACHE_CONTROL));
		}

		IBundleProvider retVal = mySearchCoordinatorSvc.registerSearch(this, theParams, getResourceName(), cacheControlDirective);

		if (retVal instanceof PersistedJpaBundleProvider) {
			PersistedJpaBundleProvider provider = (PersistedJpaBundleProvider) retVal;
			if (provider.isCacheHit()) {
				if (theServletResponse != null && theRequestDetails != null) {
					String value = "HIT from " + theRequestDetails.getFhirServerBase();
					theServletResponse.addHeader(Constants.HEADER_X_CACHE, value);
				}
			}
		}

		return retVal;
	}

	@Override
	public Set<Long> searchForIds(SearchParameterMap theParams) {

		SearchBuilder builder = newSearchBuilder();
		builder.setType(getResourceType(), getResourceName());

		// FIXME: fail if too many results

		HashSet<Long> retVal = new HashSet<Long>();

		String uuid = UUID.randomUUID().toString();
		Iterator<Long> iter = builder.createQuery(theParams, uuid);
		while (iter.hasNext()) {
			retVal.add(iter.next());
		}

		return retVal;
	}

	/**
	 * If set, the given param will be treated as a secondary primary key, and multiple resources will not be able to share the same value.
	 */
	public void setSecondaryPrimaryKeyParamName(String theSecondaryPrimaryKeyParamName) {
		mySecondaryPrimaryKeyParamName = theSecondaryPrimaryKeyParamName;
	}

	@PostConstruct
	public void start() {
		ourLog.debug("Starting resource DAO for type: {}", getResourceName());
	}

	protected <MT extends IBaseMetaType> MT toMetaDt(Class<MT> theType, Collection<TagDefinition> tagDefinitions) {
		MT retVal;
		try {
			retVal = theType.newInstance();
		} catch (Exception e) {
			throw new InternalErrorException("Failed to instantiate " + theType.getName(), e);
		}
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

	private DaoMethodOutcome toMethodOutcome(final BaseHasResource theEntity, IBaseResource theResource) {
		DaoMethodOutcome outcome = new DaoMethodOutcome();

		IIdType id = theEntity.getIdDt();
		if (getContext().getVersion().getVersion().isRi()) {
			id = getContext().getVersion().newIdType().setValue(id.getValue());
		}

		outcome.setId(id);
		outcome.setResource(theResource);
		if (theResource != null) {
			theResource.setId(id);
			if (theResource instanceof IResource) {
				ResourceMetadataKeyEnum.UPDATED.put((IResource) theResource, theEntity.getUpdated());
			} else {
				IBaseMetaType meta = theResource.getMeta();
				meta.setLastUpdated(theEntity.getUpdatedDate());
			}
		}
		return outcome;
	}

	private DaoMethodOutcome toMethodOutcome(final ResourceTable theEntity, IBaseResource theResource) {
		DaoMethodOutcome retVal = toMethodOutcome((BaseHasResource) theEntity, theResource);
		retVal.setEntity(theEntity);
		return retVal;
	}

	private ArrayList<TagDefinition> toTagList(IBaseMetaType theMeta) {
		ArrayList<TagDefinition> retVal = new ArrayList<TagDefinition>();

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

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public void translateRawParameters(Map<String, List<String>> theSource, SearchParameterMap theTarget) {
		if (theSource == null || theSource.isEmpty()) {
			return;
		}

		Map<String, RuntimeSearchParam> searchParams = mySerarchParamRegistry.getActiveSearchParams(getResourceName());

		Set<String> paramNames = theSource.keySet();
		for (String nextParamName : paramNames) {
			QualifierDetails qualifiedParamName = SearchMethodBinding.extractQualifiersFromParameterName(nextParamName);
			RuntimeSearchParam param = searchParams.get(qualifiedParamName.getParamName());
			if (param == null) {
				String msg = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "invalidSearchParameter", qualifiedParamName.getParamName(), new TreeSet<String>(searchParams.keySet()));
				throw new InvalidRequestException(msg);
			}

			// Should not be null since the check above would have caught it
			RuntimeResourceDefinition resourceDef = getContext().getResourceDefinition(myResourceName);
			RuntimeSearchParam paramDef = getSearchParamByName(resourceDef, qualifiedParamName.getParamName());

			for (String nextValue : theSource.get(nextParamName)) {
				if (isNotBlank(nextValue)) {
					QualifiedParamList qualifiedParam = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(qualifiedParamName.getWholeQualifier(), nextValue);
					List<QualifiedParamList> paramList = Collections.singletonList(qualifiedParam);
					IQueryParameterAnd<?> parsedParam = ParameterUtil.parseQueryParams(getContext(), paramDef, nextParamName, paramList);
					theTarget.add(qualifiedParamName.getParamName(), parsedParam);
				}
			}

		}
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
	public DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing, boolean theForceUpdateVersion, RequestDetails theRequestDetails) {
		StopWatch w = new StopWatch();

		preProcessResourceForStorage(theResource);

		final ResourceTable entity;

		IIdType resourceId;
		if (isNotBlank(theMatchUrl)) {
			StopWatch sw = new StopWatch();
			Set<Long> match = processMatchUrl(theMatchUrl, myResourceType);
			if (match.size() > 1) {
				String msg = getContext().getLocalizer().getMessage(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "UPDATE", theMatchUrl, match.size());
				throw new PreconditionFailedException(msg);
			} else if (match.size() == 1) {
				Long pid = match.iterator().next();
				entity = myEntityManager.find(ResourceTable.class, pid);
				resourceId = entity.getIdDt();
			} else {
				return create(theResource, null, thePerformIndexing, theRequestDetails);
			}
		} else {
			/*
			 * Note: resourceId will not be null or empty here, because we
			 * check it and reject requests in
			 * BaseOutcomeReturningMethodBindingWithResourceParam
			 */
			resourceId = theResource.getIdElement();

			try {
				entity = readEntityLatestVersion(resourceId);
			} catch (ResourceNotFoundException e) {
				if (resourceId.isIdPartValidLong()) {
					throw new InvalidRequestException(
						getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedNumericId", theResource.getIdElement().getIdPart()));
				}
				return doCreate(theResource, null, thePerformIndexing, new Date(), theRequestDetails);
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
		 * If we aren't indexing, that means we're doing this inside a transaction.
		 * The transaction will do the actual storage to the database a bit later on,
		 * after placeholder IDs have been replaced, by calling {@link #updateInternal}
		 * directly. So we just bail now.
		 */
		if (!thePerformIndexing) {
			DaoMethodOutcome outcome = toMethodOutcome(entity, theResource).setCreated(false);
			outcome.setPreviousResource(oldResource);
			return outcome;
		}

		/*
		 * Otherwise, we're not in a transaction
		 */
		ResourceTable savedEntity = updateInternal(theRequestDetails, theResource, thePerformIndexing, theForceUpdateVersion, theRequestDetails, entity, resourceId, oldResource);
		DaoMethodOutcome outcome = toMethodOutcome(savedEntity, theResource).setCreated(false);

		if (!thePerformIndexing) {
			outcome.setId(theResource.getIdElement());
		}

		String msg = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulCreate", outcome.getId(), w.getMillisAndRestart());
		outcome.setOperationOutcome(createInfoOperationOutcome(msg));

		ourLog.debug(msg);
		return outcome;
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing, RequestDetails theRequestDetails) {
		return update(theResource, theMatchUrl, thePerformIndexing, false, theRequestDetails);
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl, RequestDetails theRequestDetails) {
		return update(theResource, theMatchUrl, true, theRequestDetails);
	}

	/**
	 * Get the resource definition from the criteria which specifies the resource type
	 *
	 * @param criteria
	 * @return
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
			if (theId.isIdPartValidLong()) {
				// This means that the resource with the given numeric ID exists, but it has a "forced ID", meaning that
				// as far as the outside world is concerned, the given ID doesn't exist (it's just an internal pointer
				// to the
				// forced ID)
				throw new ResourceNotFoundException(theId);
			}
		}
	}

	protected void validateOkToDelete(List<DeleteConflict> theDeleteConflicts, ResourceTable theEntity, boolean theForValidate) {
		TypedQuery<ResourceLink> query = myEntityManager.createQuery("SELECT l FROM ResourceLink l WHERE l.myTargetResourcePid = :target_pid", ResourceLink.class);
		query.setParameter("target_pid", theEntity.getId());
		query.setMaxResults(1);
		List<ResourceLink> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return;
		}

		if (myDaoConfig.isEnforceReferentialIntegrityOnDelete() == false && !theForValidate) {
			ourLog.debug("Deleting {} resource dependencies which can no longer be satisfied", resultList.size());
			myResourceLinkDao.deleteAll(resultList);
			return;
		}

		ResourceLink link = resultList.get(0);
		IdDt targetId = theEntity.getIdDt();
		IdDt sourceId = link.getSourceResource().getIdDt();
		String sourcePath = link.getSourcePath();

		theDeleteConflicts.add(new DeleteConflict(sourceId, sourcePath, targetId));
	}

	private void validateResourceType(BaseHasResource entity) {
		validateResourceType(entity, myResourceName);
	}

	private void validateResourceTypeAndThrowIllegalArgumentException(IIdType theId) {
		if (theId.hasResourceType() && !theId.getResourceType().equals(myResourceName)) {
			throw new IllegalArgumentException("Incorrect resource type (" + theId.getResourceType() + ") for this DAO, wanted: " + myResourceName);
		}
	}

}
