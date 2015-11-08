package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
import static org.apache.commons.lang3.StringUtils.isBlank;
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
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.BaseTag;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceLink;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.jpa.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.interceptor.IJpaServerInterceptor;
import ca.uhn.fhir.jpa.util.DeleteConflict;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ObjectUtil;

@Transactional(propagation = Propagation.REQUIRED)
public abstract class BaseHapiFhirResourceDao<T extends IResource> extends BaseHapiFhirDao<T> implements IFhirResourceDao<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiFhirResourceDao.class);

	@Autowired
	private DaoConfig myDaoConfig;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	@Autowired
	protected PlatformTransactionManager myPlatformTransactionManager;

	private String myResourceName;

	private Class<T> myResourceType;
	@Autowired(required = false)
	protected ISearchDao mySearchDao;
	@Autowired()
	protected ISearchResultDao mySearchResultDao;

	private String mySecondaryPrimaryKeyParamName;

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
		return create(theResource, null, true);
	}

	@Override
	public DaoMethodOutcome create(final T theResource, String theIfNoneExist) {
		return create(theResource, theIfNoneExist, true);
	}

	@Override
	public DaoMethodOutcome create(T theResource, String theIfNoneExist, boolean thePerformIndexing) {
		if (isNotBlank(theResource.getId().getIdPart())) {
			if (getContext().getVersion().getVersion().equals(FhirVersionEnum.DSTU1)) {
				if (theResource.getId().isIdPartValidLong()) {
					String message = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedNumericId", theResource.getId().getIdPart());
					throw new InvalidRequestException(message, createErrorOperationOutcome(message));
				}
			} else {
				String message = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedId", theResource.getId().getIdPart());
				throw new InvalidRequestException(message, createErrorOperationOutcome(message));
			}
		}

		return doCreate(theResource, theIfNoneExist, thePerformIndexing, new Date());
	}

	protected IBaseOperationOutcome createErrorOperationOutcome(String theMessage) {
		return createOperationOutcome(OO_SEVERITY_ERROR, theMessage);
	}

	protected IBaseOperationOutcome createInfoOperationOutcome(String theMessage) {
		return createOperationOutcome(OO_SEVERITY_INFO, theMessage);
	}

	protected abstract IBaseOperationOutcome createOperationOutcome(String theSeverity, String theMessage);

	@Override
	public DaoMethodOutcome delete(IIdType theId) {
		List<DeleteConflict> deleteConflicts = new ArrayList<DeleteConflict>();
		StopWatch w = new StopWatch();
		
		ResourceTable savedEntity = delete(theId, deleteConflicts);

		validateDeleteConflictsEmptyOrThrowException(deleteConflicts);
		
		ourLog.info("Processed delete on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
		return toMethodOutcome(savedEntity, null);
	}

	@Override
	public ResourceTable delete(IIdType theId, List<DeleteConflict> deleteConflicts) {
		if (theId == null || !theId.hasIdPart()) {
			throw new InvalidRequestException("Can not perform delete, no ID provided");
		}
		final ResourceTable entity = readEntityLatestVersion(theId);
		if (theId.hasVersionIdPart() && Long.parseLong(theId.getVersionIdPart()) != entity.getVersion()) {
			throw new InvalidRequestException("Trying to delete " + theId + " but this is not the current version");
		}

		validateOkToDelete(deleteConflicts, entity);

		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theId, theId.getResourceType());
		notifyInterceptors(RestOperationTypeEnum.DELETE, requestDetails);

		Date updateTime = new Date();
		ResourceTable savedEntity = updateEntity(null, entity, true, updateTime, updateTime);

		// Notify JPA interceptors
		for (IServerInterceptor next : getConfig().getInterceptors()) {
			if (next instanceof IJpaServerInterceptor) {
				((IJpaServerInterceptor) next).resourceDeleted(requestDetails, entity);
			}
		}
		return savedEntity;
	}

	@Override
	public DaoMethodOutcome deleteByUrl(String theUrl) {
		StopWatch w = new StopWatch();
		List<DeleteConflict> deleteConflicts = new ArrayList<DeleteConflict>();

		List<ResourceTable> deletedResources = deleteByUrl(theUrl, deleteConflicts);
		
		validateDeleteConflictsEmptyOrThrowException(deleteConflicts);
		
		if (deletedResources.isEmpty()) {
			throw new ResourceNotFoundException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "unableToDeleteNotFound", theUrl));
		}

		ourLog.info("Processed delete on {} (matched {} resource(s)) in {}ms", new Object[] { theUrl, deletedResources.size(), w.getMillisAndRestart() });
		return new DaoMethodOutcome();
	}

	@Override
	public List<ResourceTable> deleteByUrl(String theUrl, List<DeleteConflict> deleteConflicts) {
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
			ActionRequestDetails requestDetails = new ActionRequestDetails(idToDelete, idToDelete.getResourceType());
			notifyInterceptors(RestOperationTypeEnum.DELETE, requestDetails);

			// Perform delete
			Date updateTime = new Date();
			updateEntity(null, entity, true, updateTime, updateTime);

			// Notify JPA interceptors
			for (IServerInterceptor next : getConfig().getInterceptors()) {
				if (next instanceof IJpaServerInterceptor) {
					((IJpaServerInterceptor) next).resourceDeleted(requestDetails, entity);
				}
			}

		}
		
		return retVal;
	}

	private DaoMethodOutcome doCreate(T theResource, String theIfNoneExist, boolean thePerformIndexing, Date theUpdateTime) {
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

		if (isNotBlank(theResource.getId().getIdPart())) {
			if (isValidPid(theResource.getId())) {
				throw new UnprocessableEntityException("This server cannot create an entity with a user-specified numeric ID - Client should not specify an ID when creating a new resource, or should include at least one letter in the ID to force a client-defined ID");
			}
			createForcedIdIfNeeded(entity, theResource.getId());

			if (entity.getForcedId() != null) {
				try {
					translateForcedIdToPid(theResource.getId());
					throw new UnprocessableEntityException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "duplicateCreateForcedId", theResource.getId().getIdPart()));
				} catch (ResourceNotFoundException e) {
					// good, this ID doesn't exist so we can create it
				}
			}

		}

		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theResource.getId(), toResourceName(theResource), theResource);
		notifyInterceptors(RestOperationTypeEnum.CREATE, requestDetails);

		// Perform actual DB update
		updateEntity(theResource, entity, false, null, thePerformIndexing, true, theUpdateTime);

		// Notify JPA interceptors
		for (IServerInterceptor next : getConfig().getInterceptors()) {
			if (next instanceof IJpaServerInterceptor) {
				((IJpaServerInterceptor) next).resourceCreated(requestDetails, entity);
			}
		}

		DaoMethodOutcome outcome = toMethodOutcome(entity, theResource).setCreated(true);

		String msg = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulCreate", outcome.getId(), w.getMillisAndRestart());
		outcome.setOperationOutcome(createInfoOperationOutcome(msg));

		ourLog.info(msg);
		return outcome;
	}

	@Override
	public TagList getAllResourceTags() {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, null);
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
	public TagList getTags(IIdType theResourceId) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theResourceId, null);
		notifyInterceptors(RestOperationTypeEnum.GET_TAGS, requestDetails);

		StopWatch w = new StopWatch();
		TagList retVal = super.getTags(myResourceType, theResourceId);
		ourLog.info("Processed getTags on {} in {}ms", theResourceId, w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public IBundleProvider history(Date theSince) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, null);
		notifyInterceptors(RestOperationTypeEnum.HISTORY_SYSTEM, requestDetails);

		StopWatch w = new StopWatch();
		IBundleProvider retVal = super.history(myResourceName, null, theSince);
		ourLog.info("Processed history on {} in {}ms", myResourceName, w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public IBundleProvider history(final IIdType theId, final Date theSince) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theId, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.HISTORY_INSTANCE, requestDetails);

		final InstantDt end = createHistoryToTimestamp();
		final String resourceType = getContext().getResourceDefinition(myResourceType).getName();

		T currentTmp;
		try {
			BaseHasResource entity = readEntity(theId.toVersionless(), false);
			validateResourceType(entity);
			currentTmp = toResource(myResourceType, entity, true);
			if (ResourceMetadataKeyEnum.UPDATED.get(currentTmp).after(end.getValue())) {
				currentTmp = null;
			}
		} catch (ResourceNotFoundException e) {
			currentTmp = null;
		}

		final T current = currentTmp;

		StringBuilder B = new StringBuilder();
		B.append("SELECT count(h) FROM ResourceHistoryTable h ");
		B.append("WHERE h.myResourceId = :PID AND h.myResourceType = :RESTYPE");
		B.append(" AND h.myUpdated < :END");
		B.append((theSince != null ? " AND h.myUpdated >= :SINCE" : ""));
		String querySring = B.toString();

		TypedQuery<Long> countQuery = myEntityManager.createQuery(querySring, Long.class);
		countQuery.setParameter("PID", translateForcedIdToPid(theId));
		countQuery.setParameter("RESTYPE", resourceType);
		countQuery.setParameter("END", end.getValue(), TemporalType.TIMESTAMP);
		if (theSince != null) {
			countQuery.setParameter("SINCE", theSince, TemporalType.TIMESTAMP);
		}
		int historyCount = countQuery.getSingleResult().intValue();

		final int offset;
		final int count;
		if (current != null) {
			count = historyCount + 1;
			offset = 1;
		} else {
			offset = 0;
			count = historyCount;
		}

		if (count == 0) {
			throw new ResourceNotFoundException(theId);
		}

		return new IBundleProvider() {

			@Override
			public InstantDt getPublished() {
				return end;
			}

			@Override
			public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
				List<IBaseResource> retVal = new ArrayList<IBaseResource>();
				if (theFromIndex == 0 && current != null) {
					retVal.add(current);
				}

				StringBuilder b = new StringBuilder();
				b.append("SELECT h FROM ResourceHistoryTable h WHERE h.myResourceId = :PID AND h.myResourceType = :RESTYPE AND h.myUpdated < :END ");
				b.append((theSince != null ? " AND h.myUpdated >= :SINCE" : ""));
				b.append(" ORDER BY h.myUpdated DESC");
				TypedQuery<ResourceHistoryTable> q = myEntityManager.createQuery(b.toString(), ResourceHistoryTable.class);
				q.setParameter("PID", translateForcedIdToPid(theId));
				q.setParameter("RESTYPE", resourceType);
				q.setParameter("END", end.getValue(), TemporalType.TIMESTAMP);
				if (theSince != null) {
					q.setParameter("SINCE", theSince, TemporalType.TIMESTAMP);
				}

				int firstResult = Math.max(0, theFromIndex - offset);
				q.setFirstResult(firstResult);

				int maxResults = (theToIndex - theFromIndex) + 1;
				q.setMaxResults(maxResults);

				List<ResourceHistoryTable> results = q.getResultList();
				for (ResourceHistoryTable next : results) {
					if (retVal.size() == maxResults) {
						break;
					}
					retVal.add(toResource(myResourceType, next, true));
				}

				return retVal;
			}

			@Override
			public Integer preferredPageSize() {
				return null;
			}

			@Override
			public int size() {
				return count;
			}
		};

	}

	@Override
	public IBundleProvider history(Long theId, Date theSince) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.HISTORY_TYPE, requestDetails);

		StopWatch w = new StopWatch();
		IBundleProvider retVal = super.history(myResourceName, theId, theSince);
		ourLog.info("Processed history on {} in {}ms", theId, w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public MetaDt metaAddOperation(IIdType theResourceId, MetaDt theMetaAdd) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theResourceId, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.META_ADD, requestDetails);

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theResourceId);
		if (entity == null) {
			throw new ResourceNotFoundException(theResourceId);
		}

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
		ourLog.info("Processed metaAddOperation on {} in {}ms", new Object[] { theResourceId, w.getMillisAndRestart() });

		return metaGetOperation(theResourceId);
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
	public MetaDt metaDeleteOperation(IIdType theResourceId, MetaDt theMetaDel) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theResourceId, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.META_DELETE, requestDetails);

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theResourceId);
		if (entity == null) {
			throw new ResourceNotFoundException(theResourceId);
		}

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
		myEntityManager.flush();

		ourLog.info("Processed metaDeleteOperation on {} in {}ms", new Object[] { theResourceId.getValue(), w.getMillisAndRestart() });

		return metaGetOperation(theResourceId);
	}

	@Override
	public MetaDt metaGetOperation() {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.META, requestDetails);

		String sql = "SELECT d FROM TagDefinition d WHERE d.myId IN (SELECT DISTINCT t.myTagId FROM ResourceTag t WHERE t.myResourceType = :res_type)";
		TypedQuery<TagDefinition> q = myEntityManager.createQuery(sql, TagDefinition.class);
		q.setParameter("res_type", myResourceName);
		List<TagDefinition> tagDefinitions = q.getResultList();

		MetaDt retVal = super.toMetaDt(tagDefinitions);

		return retVal;
	}

	@Override
	public MetaDt metaGetOperation(IIdType theId) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theId, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.META, requestDetails);

		Set<TagDefinition> tagDefs = new HashSet<TagDefinition>();
		BaseHasResource entity = readEntity(theId);
		for (BaseTag next : entity.getTags()) {
			tagDefs.add(next.getTag());
		}
		MetaDt retVal = super.toMetaDt(tagDefs);

		retVal.setLastUpdated(entity.getUpdated());
		retVal.setVersionId(Long.toString(entity.getVersion()));

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
		if (theResource.getId().hasIdPart()) {
			if (!theResource.getId().isIdPartValid()) {
				throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "failedToCreateWithInvalidId", theResource.getId().getIdPart()));
			}
		}
	}

	@Override
	public Set<Long> processMatchUrl(String theMatchUrl) {
		return processMatchUrl(theMatchUrl, getResourceType());
	}

	@Override
	public T read(IIdType theId) {
		validateResourceTypeAndThrowIllegalArgumentException(theId);

		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theId, getResourceName());
		RestOperationTypeEnum operationType = theId.hasVersionIdPart() ? RestOperationTypeEnum.VREAD : RestOperationTypeEnum.READ;
		notifyInterceptors(operationType, requestDetails);

		StopWatch w = new StopWatch();
		BaseHasResource entity = readEntity(theId);
		validateResourceType(entity);

		T retVal = toResource(myResourceType, entity, false);

		InstantDt deleted = ResourceMetadataKeyEnum.DELETED_AT.get(retVal);
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

		Long pid = translateForcedIdToPid(theId);
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
				TypedQuery<ResourceHistoryTable> q = myEntityManager.createQuery("SELECT t from ResourceHistoryTable t WHERE t.myResourceId = :RID AND t.myResourceType = :RTYP AND t.myResourceVersion = :RVER", ResourceHistoryTable.class);
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
		ResourceTable entity = myEntityManager.find(ResourceTable.class, translateForcedIdToPid(theId));
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}
		validateGivenIdIsAppropriateToRetrieveResource(theId, entity);
		return entity;
	}

	@Override
	public void reindex(T theResource, ResourceTable theEntity) {
		updateEntity(theResource, theEntity, false, null, true, false, theEntity.getUpdatedDate());
	}

	@Override
	public void removeTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theId, getResourceName());
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
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, getResourceName());
		notifyInterceptors(RestOperationTypeEnum.SEARCH_TYPE, requestDetails);

		SearchBuilder builder = new SearchBuilder(getContext(), myEntityManager, myPlatformTransactionManager, mySearchDao, mySearchResultDao, this);
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
		return searchForIdsWithAndOr(map, null, null);
	}

	@Override
	public Set<Long> searchForIds(String theParameterName, IQueryParameterType theValue) {
		return searchForIds(Collections.singletonMap(theParameterName, theValue));
	}

	@Override
	public Set<Long> searchForIdsWithAndOr(SearchParameterMap theParams, Collection<Long> theInitialPids, DateRangeParam theLastUpdated) {
		SearchBuilder builder = new SearchBuilder(getContext(), myEntityManager, myPlatformTransactionManager, mySearchDao, mySearchResultDao, this);
		builder.setType(getResourceType(), getResourceName());
		return builder.searchForIdsWithAndOr(theParams, theInitialPids, theLastUpdated);
	}

	@SuppressWarnings("unchecked")
	@Required
	public void setResourceType(Class<? extends IResource> theTableType) {
		myResourceType = (Class<T>) theTableType;
	}

	/**
	 * If set, the given param will be treated as a secondary primary key, and multiple resources will not be able to
	 * share the same value.
	 */
	public void setSecondaryPrimaryKeyParamName(String theSecondaryPrimaryKeyParamName) {
		mySecondaryPrimaryKeyParamName = theSecondaryPrimaryKeyParamName;
	}

	private DaoMethodOutcome toMethodOutcome(final BaseHasResource theEntity, IResource theResource) {
		DaoMethodOutcome outcome = new DaoMethodOutcome();
		outcome.setId(theEntity.getIdDt());
		outcome.setResource(theResource);
		if (theResource != null) {
			theResource.setId(theEntity.getIdDt());
			ResourceMetadataKeyEnum.UPDATED.put(theResource, theEntity.getUpdated());
		}
		return outcome;
	}

	private DaoMethodOutcome toMethodOutcome(final ResourceTable theEntity, IResource theResource) {
		DaoMethodOutcome retVal = toMethodOutcome((BaseHasResource) theEntity, theResource);
		retVal.setEntity(theEntity);
		return retVal;
	}

	private ArrayList<TagDefinition> toTagList(MetaDt theMeta) {
		ArrayList<TagDefinition> retVal = new ArrayList<TagDefinition>();

		for (CodingDt next : theMeta.getTag()) {
			retVal.add(new TagDefinition(TagTypeEnum.TAG, next.getSystem(), next.getCode(), next.getDisplay()));
		}
		for (CodingDt next : theMeta.getSecurity()) {
			retVal.add(new TagDefinition(TagTypeEnum.SECURITY_LABEL, next.getSystem(), next.getCode(), next.getDisplay()));
		}
		for (UriDt next : theMeta.getProfile()) {
			retVal.add(new TagDefinition(TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, next.getValue(), null));
		}

		return retVal;
	}




	@Override
	public DaoMethodOutcome update(T theResource) {
		return update(theResource, null);
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl) {
		return update(theResource, theMatchUrl, true);
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing) {
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
				return create(theResource, null, thePerformIndexing);
			}
		} else {
			resourceId = theResource.getId();
			if (resourceId == null || isBlank(resourceId.getIdPart())) {
				throw new InvalidRequestException("Can not update a resource with no ID");
			}
			try {
				entity = readEntityLatestVersion(resourceId);
			} catch (ResourceNotFoundException e) {
				if (resourceId.isIdPartValidLong()) {
					throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "failedToCreateWithClientAssignedNumericId", theResource.getId().getIdPart()));
				}
				return doCreate(theResource, null, thePerformIndexing, new Date());
			}
		}

		if (resourceId.hasVersionIdPart() && Long.parseLong(resourceId.getVersionIdPart()) != entity.getVersion()) {
			throw new InvalidRequestException("Trying to update " + resourceId + " but this is not the current version");
		}

		if (resourceId.hasResourceType() && !resourceId.getResourceType().equals(getResourceName())) {
			throw new UnprocessableEntityException("Invalid resource ID[" + entity.getIdDt().toUnqualifiedVersionless() + "] of type[" + entity.getResourceType() + "] - Does not match expected [" + getResourceName() + "]");
		}

		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(resourceId, getResourceName(), theResource);
		notifyInterceptors(RestOperationTypeEnum.UPDATE, requestDetails);

		// Perform update
		ResourceTable savedEntity = updateEntity(theResource, entity, true, null, thePerformIndexing, true, new Date());

		// Notify JPA interceptors
		for (IServerInterceptor next : getConfig().getInterceptors()) {
			if (next instanceof IJpaServerInterceptor) {
				((IJpaServerInterceptor) next).resourceUpdated(requestDetails, entity);
			}
		}

		DaoMethodOutcome outcome = toMethodOutcome(savedEntity, theResource).setCreated(false);

		String msg = getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "successfulCreate", outcome.getId(), w.getMillisAndRestart());
		outcome.setOperationOutcome(createInfoOperationOutcome(msg));

		ourLog.info(msg);
		return outcome;
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
