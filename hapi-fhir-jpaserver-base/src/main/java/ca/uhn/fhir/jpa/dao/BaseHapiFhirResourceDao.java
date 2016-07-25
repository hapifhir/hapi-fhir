package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamUriDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.BaseTag;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceLink;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.jpa.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.interceptor.IJpaServerInterceptor;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.jpa.util.DeleteConflict;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.method.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ObjectUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ResourceReferenceInfo;

@Transactional(propagation = Propagation.REQUIRED)
public abstract class BaseHapiFhirResourceDao<T extends IBaseResource> extends BaseHapiFhirDao<T> implements IFhirResourceDao<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiFhirResourceDao.class);

	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	protected PlatformTransactionManager myPlatformTransactionManager;
	@Autowired
	private IResourceHistoryTableDao myResourceHistoryTableDao;
	@Autowired()
	protected IResourceIndexedSearchParamUriDao myResourceIndexedSearchParamUriDao;
	private String myResourceName;
	private Class<T> myResourceType;
	@Autowired(required = false)
	protected IFulltextSearchSvc mySearchDao;
	@Autowired()
	protected ISearchResultDao mySearchResultDao;
	private String mySecondaryPrimaryKeyParamName;
	@Autowired()
	protected IHapiTerminologySvc myTerminologySvc;

	@Override
	public void addTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm, String theLabel) {
		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId);
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}

		//@formatter:off
		for (BaseTag next : new ArrayList<BaseTag>(entity.getTags())) {
			if (ObjectUtil.equals(next.getTag().getTagType(), theTagType) && 
					ObjectUtil.equals(next.getTag().getSystem(), theScheme) && 
					ObjectUtil.equals(next.getTag().getCode(), theTerm)) {
				return;
			}
		}
		//@formatter:on

		entity.setHasTags(true);

		TagDefinition def = getTag(TagTypeEnum.TAG, theScheme, theTerm, theLabel);
		BaseTag newEntity = entity.addTag(def);

		myEntityManager.persist(newEntity);
		myEntityManager.merge(entity);

		ourLog.info("Processed addTag {}/{} on {} in {}ms", new Object[] { theScheme, theTerm, theId, w.getMillisAndRestart() });
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
			if (getContext().getVersion().getVersion().equals(FhirVersionEnum.DSTU1)) {
				if (theResource.getIdElement().isIdPartValidLong()) {
					String message = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedNumericId", theResource.getIdElement().getIdPart());
					throw new InvalidRequestException(message, createErrorOperationOutcome(message, "processing"));
				}
			} else {
				String message = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedId", theResource.getIdElement().getIdPart());
				throw new InvalidRequestException(message, createErrorOperationOutcome(message, "processing"));
			}
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
	public ResourceTable delete(IIdType theId, List<DeleteConflict> deleteConflicts, RequestDetails theRequestDetails) {
		if (theId == null || !theId.hasIdPart()) {
			throw new InvalidRequestException("Can not perform delete, no ID provided");
		}
		final ResourceTable entity = readEntityLatestVersion(theId);
		if (theId.hasVersionIdPart() && Long.parseLong(theId.getVersionIdPart()) != entity.getVersion()) {
			throw new InvalidRequestException("Trying to delete " + theId + " but this is not the current version");
		}

		validateOkToDelete(deleteConflicts, entity);

		// Notify interceptors
		if (theRequestDetails != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getContext(), theId.getResourceType(), theId);
			notifyInterceptors(RestOperationTypeEnum.DELETE, requestDetails);
		}

		Date updateTime = new Date();
		ResourceTable savedEntity = updateEntity(null, entity, updateTime, updateTime);

		// Notify JPA interceptors
		if (theRequestDetails != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getContext(), theId.getResourceType(), theId);
			T resourceToDelete = toResource(myResourceType, entity, false);
			theRequestDetails.getRequestOperationCallback().resourceDeleted(resourceToDelete);
			for (IServerInterceptor next : getConfig().getInterceptors()) {
				if (next instanceof IJpaServerInterceptor) {
					((IJpaServerInterceptor) next).resourceDeleted(requestDetails, entity);
				}
			}
		}

		return savedEntity;
	}

	@Override
	public DaoMethodOutcome delete(IIdType theId, RequestDetails theRequestDetails) {
		List<DeleteConflict> deleteConflicts = new ArrayList<DeleteConflict>();
		StopWatch w = new StopWatch();

		ResourceTable savedEntity = delete(theId, deleteConflicts, theRequestDetails);

		validateDeleteConflictsEmptyOrThrowException(deleteConflicts);

		ourLog.info("Processed delete on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
		return toMethodOutcome(savedEntity, null);
	}
	
	@Override
	public List<ResourceTable> deleteByUrl(String theUrl, List<DeleteConflict> deleteConflicts, RequestDetails theRequestDetails) {
		Set<Long> resource = processMatchUrl(theUrl, myResourceType);
		if (resource.size() > 1) {
			if (myDaoConfig.isAllowMultipleDelete() == false) {
				throw new PreconditionFailedException(getContext().getLocalizer().getMessage(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "DELETE", theUrl, resource.size()));
			}
		}

		List<ResourceTable> retVal = new ArrayList<ResourceTable>();
		for (Long pid : resource) {
			ResourceTable entity = myEntityManager.find(ResourceTable.class, pid);
			retVal.add(entity);

			validateOkToDelete(deleteConflicts, entity);

			// Notify interceptors
			IdDt idToDelete = entity.getIdDt();
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, idToDelete.getResourceType(), idToDelete);
			notifyInterceptors(RestOperationTypeEnum.DELETE, requestDetails);

			// Perform delete
			Date updateTime = new Date();
			updateEntity(null, entity, updateTime, updateTime);

			// Notify JPA interceptors
			T resourceToDelete = toResource(myResourceType, entity, false);
			theRequestDetails.getRequestOperationCallback().resourceDeleted(resourceToDelete);
			for (IServerInterceptor next : getConfig().getInterceptors()) {
				if (next instanceof IJpaServerInterceptor) {
					((IJpaServerInterceptor) next).resourceDeleted(requestDetails, entity);
				}
			}

		}

		return retVal;
	}

	@Override
	public DaoMethodOutcome deleteByUrl(String theUrl, RequestDetails theRequestDetails) {
		StopWatch w = new StopWatch();
		List<DeleteConflict> deleteConflicts = new ArrayList<DeleteConflict>();

		List<ResourceTable> deletedResources = deleteByUrl(theUrl, deleteConflicts, theRequestDetails);

		validateDeleteConflictsEmptyOrThrowException(deleteConflicts);

		
		IBaseOperationOutcome oo;
		if (deletedResources.isEmpty()) {
			oo = OperationOutcomeUtil.newInstance(getContext());
			String message = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "unableToDeleteNotFound", theUrl);
			String severity = "warning";
			String code = "not-found";
			OperationOutcomeUtil.addIssue(getContext(), oo, severity, message, null, code);
		} else {
			oo = OperationOutcomeUtil.newInstance(getContext());
			String message = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulDeletes", theUrl, deletedResources.size(), w.getMillis());
			String severity = "information";
			String code = "informational";
			OperationOutcomeUtil.addIssue(getContext(), oo, severity, message, null, code);
		}

		ourLog.info("Processed delete on {} (matched {} resource(s)) in {}ms", new Object[] { theUrl, deletedResources.size(), w.getMillis() });
		DaoMethodOutcome retVal = new DaoMethodOutcome();
		retVal.setOperationOutcome(oo);
		return retVal;
	}

	@PostConstruct
	public void detectSearchDaoDisabled() {
		if (mySearchDao != null && mySearchDao.isDisabled()) {
			mySearchDao = null;
		}
	}

	private DaoMethodOutcome doCreate(T theResource, String theIfNoneExist, boolean thePerformIndexing, Date theUpdateTime, RequestDetails theRequestDetails) {
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

			if (entity.getForcedId() != null) {
				try {
					translateForcedIdToPid(getResourceName(), theResource.getIdElement().getIdPart());
					throw new UnprocessableEntityException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "duplicateCreateForcedId", theResource.getIdElement().getIdPart()));
				} catch (ResourceNotFoundException e) {
					// good, this ID doesn't exist so we can create it
				}
			}

		}

		// Notify interceptors
		if (theRequestDetails != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getContext(), theResource);
			notifyInterceptors(RestOperationTypeEnum.CREATE, requestDetails);
		}

		// Perform actual DB update
		updateEntity(theResource, entity, null, thePerformIndexing, true, theUpdateTime);
		theResource.setId(entity.getIdDt());

		// Notify JPA interceptors
		if (theRequestDetails != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getContext(), theResource);
			theRequestDetails.getRequestOperationCallback().resourceCreated(theResource);
			for (IServerInterceptor next : getConfig().getInterceptors()) {
				if (next instanceof IJpaServerInterceptor) {
					((IJpaServerInterceptor) next).resourceCreated(requestDetails, entity);
				}
			}
		}

		DaoMethodOutcome outcome = toMethodOutcome(entity, theResource).setCreated(true);

		String msg = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulCreate", outcome.getId(), w.getMillisAndRestart());
		outcome.setOperationOutcome(createInfoOperationOutcome(msg));

		ourLog.info(msg);
		return outcome;
	}

	private <MT extends IBaseMetaType> void doMetaAdd(MT theMetaAdd, BaseHasResource entity) {
		List<TagDefinition> tags = toTagList(theMetaAdd);

		//@formatter:off
		for (TagDefinition nextDef : tags) {
			
			boolean hasTag = false;
			for (BaseTag next : new ArrayList<BaseTag>(entity.getTags())) {
				if (ObjectUtil.equals(next.getTag().getTagType(), nextDef.getTagType()) && 
						ObjectUtil.equals(next.getTag().getSystem(), nextDef.getSystem()) && 
						ObjectUtil.equals(next.getTag().getCode(), nextDef.getCode())) {
					hasTag = true;
					break;
				}
			}

			if (!hasTag) {
				entity.setHasTags(true);
				
				TagDefinition def = getTag(nextDef.getTagType(), nextDef.getSystem(), nextDef.getCode(), nextDef.getDisplay());
				BaseTag newEntity = entity.addTag(def);
				myEntityManager.persist(newEntity);
			}
		}
		//@formatter:on

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
	public TagList getAllResourceTags(RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails);
		notifyInterceptors(RestOperationTypeEnum.GET_TAGS, requestDetails);

		StopWatch w = new StopWatch();
		TagList tags = super.getTags(myResourceType, null);
		ourLog.info("Processed getTags on {} in {}ms", myResourceName, w.getMillisAndRestart());
		return tags;
	}

	protected abstract List<Object> getIncludeValues(FhirTerser theTerser, Include theInclude, IBaseResource theResource, RuntimeResourceDefinition theResourceDef);

	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public Class<T> getResourceType() {
		return myResourceType;
	}

	@Override
	public TagList getTags(IIdType theResourceId, RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, null, theResourceId);
		notifyInterceptors(RestOperationTypeEnum.GET_TAGS, requestDetails);

		StopWatch w = new StopWatch();
		TagList retVal = super.getTags(myResourceType, theResourceId);
		ourLog.info("Processed getTags on {} in {}ms", theResourceId, w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public IBundleProvider history(Date theSince, Date theUntil, RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails);
		notifyInterceptors(RestOperationTypeEnum.HISTORY_TYPE, requestDetails);

		StopWatch w = new StopWatch();
		IBundleProvider retVal = super.history(myResourceName, null, theSince, theUntil);
		ourLog.info("Processed history on {} in {}ms", myResourceName, w.getMillisAndRestart());
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

		ourLog.info("Processed history on {} in {}ms", id, w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public <MT extends IBaseMetaType> MT metaAddOperation(IIdType theResourceId, MT theMetaAdd, RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), theResourceId);
		notifyInterceptors(RestOperationTypeEnum.META_ADD, requestDetails);

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

		ourLog.info("Processed metaAddOperation on {} in {}ms", new Object[] { theResourceId, w.getMillisAndRestart() });

		@SuppressWarnings("unchecked")
		MT retVal = (MT) metaGetOperation(theMetaAdd.getClass(), theResourceId, theRequestDetails);
		return retVal;
	}

	// @Override
	// public IBundleProvider everything(IIdType theId) {
	// Search search = new Search();
	// search.setUuid(UUID.randomUUID().toString());
	// search.setCreated(new Date());
	// myEntityManager.persist(search);
	//
	// List<SearchResult> results = new ArrayList<SearchResult>();
	// if (theId != null) {
	// Long pid = translateForcedIdToPid(theId);
	// ResourceTable entity = myEntityManager.find(ResourceTable.class, pid);
	// validateGivenIdIsAppropriateToRetrieveResource(theId, entity);
	// SearchResult res = new SearchResult(search);
	// res.setResourcePid(pid);
	// results.add(res);
	// } else {
	// TypedQuery<Tuple> query = createSearchAllByTypeQuery();
	// for (Tuple next : query.getResultList()) {
	// SearchResult res = new SearchResult(search);
	// res.setResourcePid(next.get(0, Long.class));
	// results.add(res);
	// }
	// }
	//
	// int totalCount = results.size();
	// mySearchResultDao.save(results);
	// mySearchResultDao.flush();
	//
	// CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
	//
	// // Load _revincludes
	// CriteriaQuery<Long> cq = builder.createQuery(Long.class);
	// Root<ResourceLink> from = cq.from(ResourceLink.class);
	// cq.select(from.get("mySourceResourcePid").as(Long.class));
	//
	// Subquery<Long> pidsSubquery = cq.subquery(Long.class);
	// Root<SearchResult> pidsSubqueryFrom = pidsSubquery.from(SearchResult.class);
	// pidsSubquery.select(pidsSubqueryFrom.get("myResourcePid").as(Long.class));
	// pidsSubquery.where(pidsSubqueryFrom.get("mySearch").in(search));
	//
	// cq.where(from.get("myTargetResourceId").in(pidsSubquery));
	// TypedQuery<Long> query = myEntityManager.createQuery(cq);
	//
	// results = new ArrayList<SearchResult>();
	// for (Long next : query.getResultList()) {
	// SearchResult res = new SearchResult(search);
	// res.setResourcePid(next);
	// results.add(res);
	// }
	//
	// // Save _revincludes
	// totalCount += results.size();
	// mySearchResultDao.save(results);
	// mySearchResultDao.flush();
	//
	// final int finalTotalCount = totalCount;
	// return new IBundleProvider() {
	//
	// @Override
	// public int size() {
	// return finalTotalCount;
	// }
	//
	// @Override
	// public Integer preferredPageSize() {
	// return null;
	// }
	//
	// @Override
	// public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public InstantDt getPublished() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	// };
	// }

	@Override
	public <MT extends IBaseMetaType> MT metaDeleteOperation(IIdType theResourceId, MT theMetaDel, RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), theResourceId);
		notifyInterceptors(RestOperationTypeEnum.META_DELETE, requestDetails);

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

		ourLog.info("Processed metaDeleteOperation on {} in {}ms", new Object[] { theResourceId.getValue(), w.getMillisAndRestart() });

		@SuppressWarnings("unchecked")
		MT retVal = (MT) metaGetOperation(theMetaDel.getClass(), theResourceId, theRequestDetails);
		return retVal;
	}

	@Override
	public <MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, IIdType theId, RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), theId);
		notifyInterceptors(RestOperationTypeEnum.META, requestDetails);

		Set<TagDefinition> tagDefs = new HashSet<TagDefinition>();
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
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), null);
		notifyInterceptors(RestOperationTypeEnum.META, requestDetails);

		String sql = "SELECT d FROM TagDefinition d WHERE d.myId IN (SELECT DISTINCT t.myTagId FROM ResourceTag t WHERE t.myResourceType = :res_type)";
		TypedQuery<TagDefinition> q = myEntityManager.createQuery(sql, TagDefinition.class);
		q.setParameter("res_type", myResourceName);
		List<TagDefinition> tagDefinitions = q.getResultList();

		MT retVal = toMetaDt(theType, tagDefinitions);

		return retVal;
	}

	@PostConstruct
	public void postConstruct() {
		RuntimeResourceDefinition def = getContext().getResourceDefinition(myResourceType);
		myResourceName = def.getName();

		if (mySecondaryPrimaryKeyParamName != null) {
			RuntimeSearchParam sp = def.getSearchParam(mySecondaryPrimaryKeyParamName);
			if (sp == null) {
				throw new ConfigurationException("Unknown search param on resource[" + myResourceName + "] for secondary key[" + mySecondaryPrimaryKeyParamName + "]");
			}
			if (sp.getParamType() != RestSearchParameterTypeEnum.TOKEN) {
				throw new ConfigurationException("Search param on resource[" + myResourceName + "] for secondary key[" + mySecondaryPrimaryKeyParamName + "] is not a token type, only token is supported");
			}
		}

	}

	/**
	 * May be overridden by subclasses to validate resources prior to storage
	 * 
	 * @param theResource
	 *           The resource that is about to be stored
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

		T retVal = toResource(myResourceType, entity, false);

		IPrimitiveType<Date> deleted;
		if (retVal instanceof IResource) {
			deleted = ResourceMetadataKeyEnum.DELETED_AT.get((IResource) retVal);
		} else {
			deleted = ResourceMetadataKeyEnum.DELETED_AT.get((IAnyResource) retVal);
		}
		if (deleted != null && !deleted.isEmpty()) {
			throw new ResourceGoneException("Resource was deleted at " + deleted.getValueAsString());
		}

		ourLog.info("Processed read on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public BaseHasResource readEntity(IIdType theId) {
		boolean checkForForcedId = true;

		BaseHasResource entity = readEntity(theId, checkForForcedId);

		return entity;
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
			if (entity.getVersion() != theId.getVersionIdPartAsLong().longValue()) {
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
		updateEntity(theResource, theEntity, null, true, false, theEntity.getUpdatedDate());
	}

	@Override
	public void removeTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm, RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, getResourceName(), theId);
		notifyInterceptors(RestOperationTypeEnum.DELETE_TAGS, requestDetails);

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId);
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}

		//@formatter:off
		for (BaseTag next : new ArrayList<BaseTag>(entity.getTags())) {
			if (ObjectUtil.equals(next.getTag().getTagType(), theTagType) && 
					ObjectUtil.equals(next.getTag().getSystem(), theScheme) && 
					ObjectUtil.equals(next.getTag().getCode(), theTerm)) {
				myEntityManager.remove(next);
				entity.getTags().remove(next);
			}
		}
		//@formatter:on

		if (entity.getTags().isEmpty()) {
			entity.setHasTags(false);
		}

		myEntityManager.merge(entity);

		ourLog.info("Processed remove tag {}/{} on {} in {}ms", new Object[] { theScheme, theTerm, theId.getValue(), w.getMillisAndRestart() });
	}

	@Override
	public IBundleProvider search(Map<String, IQueryParameterType> theParams) {
		SearchParameterMap map = new SearchParameterMap();
		for (Entry<String, IQueryParameterType> nextEntry : theParams.entrySet()) {
			map.add(nextEntry.getKey(), (nextEntry.getValue()));
		}
		return search(map);
	}

	@Override
	public IBundleProvider search(final SearchParameterMap theParams) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theParams.getRequestDetails(), getContext(), getResourceName(), null);
		notifyInterceptors(RestOperationTypeEnum.SEARCH_TYPE, requestDetails);

		SearchBuilder builder = new SearchBuilder(getContext(), myEntityManager, myPlatformTransactionManager, mySearchDao, mySearchResultDao, this, myResourceIndexedSearchParamUriDao, myForcedIdDao,
				myTerminologySvc);
		builder.setType(getResourceType(), getResourceName());
		return builder.search(theParams);
	}

	@Override
	public IBundleProvider search(String theParameterName, IQueryParameterType theValue) {
		return search(Collections.singletonMap(theParameterName, theValue));
	}

	@Override
	public Set<Long> searchForIds(Map<String, IQueryParameterType> theParams) {
		SearchParameterMap map = new SearchParameterMap();
		for (Entry<String, IQueryParameterType> nextEntry : theParams.entrySet()) {
			map.add(nextEntry.getKey(), (nextEntry.getValue()));
		}
		return searchForIdsWithAndOr(map);
	}

	@Override
	public Set<Long> searchForIds(String theParameterName, IQueryParameterType theValue) {
		return searchForIds(Collections.singletonMap(theParameterName, theValue));
	}

	@Override
	public Set<Long> searchForIdsWithAndOr(SearchParameterMap theParams) {
		theParams.setPersistResults(false);

		SearchBuilder builder = new SearchBuilder(getContext(), myEntityManager, myPlatformTransactionManager, mySearchDao, mySearchResultDao, this, myResourceIndexedSearchParamUriDao, myForcedIdDao,
				myTerminologySvc);
		builder.setType(getResourceType(), getResourceName());
		builder.search(theParams);
		return builder.doGetPids();
	}

	@SuppressWarnings("unchecked")
	@Required
	public void setResourceType(Class<? extends IBaseResource> theTableType) {
		myResourceType = (Class<T>) theTableType;
	}

	/**
	 * If set, the given param will be treated as a secondary primary key, and multiple resources will not be able to share the same value.
	 */
	public void setSecondaryPrimaryKeyParamName(String theSecondaryPrimaryKeyParamName) {
		mySecondaryPrimaryKeyParamName = theSecondaryPrimaryKeyParamName;
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
			id = new IdType(id.getValue());
		}

		outcome.setId(id);
		outcome.setResource(theResource);
		if (theResource != null) {
			theResource.setId(id);
			if (theResource instanceof IResource) {
				ResourceMetadataKeyEnum.UPDATED.put((IResource) theResource, theEntity.getUpdated());
			} else {
				IBaseMetaType meta = ((IAnyResource) theResource).getMeta();
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
	public DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing, RequestDetails theRequestDetails) {
		StopWatch w = new StopWatch();

		preProcessResourceForStorage(theResource);

		final ResourceTable entity;

		IIdType resourceId;
		if (isNotBlank(theMatchUrl)) {
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
			 * Note: resourcdeId will not be null or empty here, because we check it and reject requests in BaseOutcomeReturningMethodBindingWithResourceParam
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
			throw new InvalidRequestException("Trying to update " + resourceId + " but this is not the current version");
		}

		if (resourceId.hasResourceType() && !resourceId.getResourceType().equals(getResourceName())) {
			throw new UnprocessableEntityException(
					"Invalid resource ID[" + entity.getIdDt().toUnqualifiedVersionless() + "] of type[" + entity.getResourceType() + "] - Does not match expected [" + getResourceName() + "]");
		}

		// Notify interceptors
		ActionRequestDetails requestDetails = null;
		if (theRequestDetails != null) {
			requestDetails = new ActionRequestDetails(theRequestDetails, theResource, getResourceName(), resourceId);
			notifyInterceptors(RestOperationTypeEnum.UPDATE, requestDetails);
		}

		// Perform update
		ResourceTable savedEntity = updateEntity(theResource, entity, null, thePerformIndexing, true, new Date());

		// Notify interceptors
		if (theRequestDetails != null) {
			theRequestDetails.getRequestOperationCallback().resourceUpdated(theResource);
			for (IServerInterceptor next : getConfig().getInterceptors()) {
				if (next instanceof IJpaServerInterceptor) {
					((IJpaServerInterceptor) next).resourceUpdated(requestDetails, entity);
				}
			}
		}

		DaoMethodOutcome outcome = toMethodOutcome(savedEntity, theResource).setCreated(false);

		String msg = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulCreate", outcome.getId(), w.getMillisAndRestart());
		outcome.setOperationOutcome(createInfoOperationOutcome(msg));

		ourLog.info(msg);
		return outcome;
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl, RequestDetails theRequestDetails) {
		return update(theResource, theMatchUrl, true, theRequestDetails);
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

	protected void validateOkToDelete(List<DeleteConflict> theDeleteConflicts, ResourceTable theEntity) {
		TypedQuery<ResourceLink> query = myEntityManager.createQuery("SELECT l FROM ResourceLink l WHERE l.myTargetResourcePid = :target_pid", ResourceLink.class);
		query.setParameter("target_pid", theEntity.getId());
		query.setMaxResults(1);
		List<ResourceLink> resultList = query.getResultList();
		if (resultList.isEmpty()) {
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
