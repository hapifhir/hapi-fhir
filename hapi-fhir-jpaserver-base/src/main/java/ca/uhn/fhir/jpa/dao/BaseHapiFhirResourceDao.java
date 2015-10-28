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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TemporalType;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.SearchParameterMap.EverythingModeEnum;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.entity.BaseTag;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.entity.ResourceLink;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.ResourceTag;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchResult;
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.jpa.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.model.dstu.resource.BaseResource;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.method.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ObjectUtil;

@Transactional(propagation = Propagation.REQUIRED)
public abstract class BaseHapiFhirResourceDao<T extends IResource> extends BaseHapiFhirDao<T>implements IFhirResourceDao<T> {

	static final String OO_SEVERITY_ERROR = "error";
	static final String OO_SEVERITY_INFO = "information";
	static final String OO_SEVERITY_WARN = "warning";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiFhirResourceDao.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;

	@Autowired
	private DaoConfig myDaoConfig;

	@Autowired(required=false)
	private ISearchDao mySearchDao;
	
	private String myResourceName;
	private Class<T> myResourceType;
	private String mySecondaryPrimaryKeyParamName;

	@Autowired()
	private ISearchResultDao mySearchResultDao;


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
		notifyWriteCompleted();
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
		if (theId == null || !theId.hasIdPart()) {
			throw new InvalidRequestException("Can not perform delete, no ID provided");
		}
		StopWatch w = new StopWatch();
		final ResourceTable entity = readEntityLatestVersion(theId);
		if (theId.hasVersionIdPart() && Long.parseLong(theId.getVersionIdPart()) != entity.getVersion()) {
			throw new InvalidRequestException("Trying to delete " + theId + " but this is not the current version");
		}

		validateOkToDeleteOrThrowResourceVersionConflictException(entity);

		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theId, theId.getResourceType());
		notifyInterceptors(RestOperationTypeEnum.DELETE, requestDetails);

		Date updateTime = new Date();
		ResourceTable savedEntity = updateEntity(null, entity, true, updateTime, updateTime);

		notifyWriteCompleted();

		ourLog.info("Processed delete on {} in {}ms", theId.getValue(), w.getMillisAndRestart());
		return toMethodOutcome(savedEntity, null);
	}

	@Override
	public DaoMethodOutcome deleteByUrl(String theUrl) {
		return deleteByUrl(theUrl, false);
	}

	@Override
	public DaoMethodOutcome deleteByUrl(String theUrl, boolean theInTransaction) {
		StopWatch w = new StopWatch();

		Set<Long> resource = processMatchUrl(theUrl, myResourceType);
		if (resource.isEmpty()) {
			if (!theInTransaction) {
				throw new ResourceNotFoundException(getContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "unableToDeleteNotFound", theUrl));
			} else {
				return new DaoMethodOutcome();
			}
		} else if (resource.size() > 1) {
			if (myDaoConfig.isAllowMultipleDelete() == false) {
				throw new PreconditionFailedException(getContext().getLocalizer().getMessage(BaseHapiFhirDao.class, "transactionOperationWithMultipleMatchFailure", "DELETE", theUrl, resource.size()));
			}
		}

		for (Long pid : resource) {
			ResourceTable entity = myEntityManager.find(ResourceTable.class, pid);

			validateOkToDeleteOrThrowResourceVersionConflictException(entity);

			// Notify interceptors
			IdDt idToDelete = entity.getIdDt();
			ActionRequestDetails requestDetails = new ActionRequestDetails(idToDelete, idToDelete.getResourceType());
			notifyInterceptors(RestOperationTypeEnum.DELETE, requestDetails);

			// Perform delete
			Date updateTime = new Date();
			updateEntity(null, entity, true, updateTime, updateTime);
			notifyWriteCompleted();

		}

		ourLog.info("Processed delete on {} (matched {} resource(s)) in {}ms", new Object[] { theUrl, resource.size(), w.getMillisAndRestart() });

		return new DaoMethodOutcome();
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
				throw new UnprocessableEntityException(
						"This server cannot create an entity with a user-specified numeric ID - Client should not specify an ID when creating a new resource, or should include at least one letter in the ID to force a client-defined ID");
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

		updateEntity(theResource, entity, false, null, thePerformIndexing, true, theUpdateTime);

		DaoMethodOutcome outcome = toMethodOutcome(entity, theResource).setCreated(true);

		notifyWriteCompleted();

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

	private void loadResourcesByPid(Collection<Long> theIncludePids, List<IBaseResource> theResourceListToPopulate, Set<Long> theRevIncludedPids, boolean theForHistoryOperation) {
		if (theIncludePids.isEmpty()) {
			return;
		}

		Map<Long, Integer> position = new HashMap<Long, Integer>();
		for (Long next : theIncludePids) {
			position.put(next, theResourceListToPopulate.size());
			theResourceListToPopulate.add(null);
		}

		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceTable> cq = builder.createQuery(ResourceTable.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.where(from.get("myId").in(theIncludePids));
		TypedQuery<ResourceTable> q = myEntityManager.createQuery(cq);

		for (ResourceTable next : q.getResultList()) {
			Class<? extends IBaseResource> resourceType = getContext().getResourceDefinition(next.getResourceType()).getImplementingClass();
			IResource resource = (IResource) toResource(resourceType, next, theForHistoryOperation);
			Integer index = position.get(next.getId());
			if (index == null) {
				ourLog.warn("Got back unexpected resource PID {}", next.getId());
				continue;
			}

			if (theRevIncludedPids.contains(next.getId())) {
				ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(resource, BundleEntrySearchModeEnum.INCLUDE);
			} else {
				ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(resource, BundleEntrySearchModeEnum.MATCH);
			}

			theResourceListToPopulate.set(index, resource);
		}
	}

//	@Override
//	public IBundleProvider everything(IIdType theId) {
//		Search search = new Search();
//		search.setUuid(UUID.randomUUID().toString());
//		search.setCreated(new Date());
//		myEntityManager.persist(search);
//
//		List<SearchResult> results = new ArrayList<SearchResult>();
//		if (theId != null) {
//			Long pid = translateForcedIdToPid(theId);
//			ResourceTable entity = myEntityManager.find(ResourceTable.class, pid);
//			validateGivenIdIsAppropriateToRetrieveResource(theId, entity);
//			SearchResult res = new SearchResult(search);
//			res.setResourcePid(pid);
//			results.add(res);
//		} else {
//			TypedQuery<Tuple> query = createSearchAllByTypeQuery();
//			for (Tuple next : query.getResultList()) {
//				SearchResult res = new SearchResult(search);
//				res.setResourcePid(next.get(0, Long.class));
//				results.add(res);
//			}
//		}
//
//		int totalCount = results.size();
//		mySearchResultDao.save(results);
//		mySearchResultDao.flush();
//
//		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
//
//		// Load _revincludes
//		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
//		Root<ResourceLink> from = cq.from(ResourceLink.class);
//		cq.select(from.get("mySourceResourcePid").as(Long.class));
//
//		Subquery<Long> pidsSubquery = cq.subquery(Long.class);
//		Root<SearchResult> pidsSubqueryFrom = pidsSubquery.from(SearchResult.class);
//		pidsSubquery.select(pidsSubqueryFrom.get("myResourcePid").as(Long.class));
//		pidsSubquery.where(pidsSubqueryFrom.get("mySearch").in(search));
//
//		cq.where(from.get("myTargetResourceId").in(pidsSubquery));
//		TypedQuery<Long> query = myEntityManager.createQuery(cq);
//
//		results = new ArrayList<SearchResult>();
//		for (Long next : query.getResultList()) {
//			SearchResult res = new SearchResult(search);
//			res.setResourcePid(next);
//			results.add(res);
//		}
//
//		// Save _revincludes
//		totalCount += results.size();
//		mySearchResultDao.save(results);
//		mySearchResultDao.flush();
//
//		final int finalTotalCount = totalCount;
//		return new IBundleProvider() {
//
//			@Override
//			public int size() {
//				return finalTotalCount;
//			}
//
//			@Override
//			public Integer preferredPageSize() {
//				return null;
//			}
//
//			@Override
//			public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public InstantDt getPublished() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		};
//	}

	/**
	 * THIS SHOULD RETURN HASHSET and not jsut Set because we add to it later (so it can't be Collections.emptySet())
	 * @param theLastUpdated 
	 */
	private HashSet<Long> loadReverseIncludes(Collection<Long> theMatches, Set<Include> theRevIncludes, boolean theReverseMode, EverythingModeEnum theEverythingModeEnum, DateRangeParam theLastUpdated) {
		if (theMatches.size() == 0) {
			return new HashSet<Long>();
		}
		if (theRevIncludes == null || theRevIncludes.isEmpty()) {
			return new HashSet<Long>();
		}
		String searchFieldName = theReverseMode ? "myTargetResourcePid" : "mySourceResourcePid";

		Collection<Long> nextRoundMatches = theMatches;
		HashSet<Long> allAdded = new HashSet<Long>();
		HashSet<Long> original = new HashSet<Long>(theMatches);
		ArrayList<Include> includes = new ArrayList<Include>(theRevIncludes);

		int roundCounts = 0;
		StopWatch w = new StopWatch();

		boolean addedSomeThisRound;
		do {
			roundCounts++;

			HashSet<Long> pidsToInclude = new HashSet<Long>();
			Set<Long> nextRoundOmit = new HashSet<Long>();

			for (Iterator<Include> iter = includes.iterator(); iter.hasNext();) {
				Include nextInclude = iter.next();
				if (nextInclude.isRecurse() == false) {
					iter.remove();
				}

				boolean matchAll = "*".equals(nextInclude.getValue());
				if (matchAll) {
					String sql;
					sql = "SELECT r FROM ResourceLink r WHERE r." + searchFieldName + " IN (:target_pids)";
					TypedQuery<ResourceLink> q = myEntityManager.createQuery(sql, ResourceLink.class);
					q.setParameter("target_pids", nextRoundMatches);
					List<ResourceLink> results = q.getResultList();
					for (ResourceLink resourceLink : results) {
						if (theReverseMode) {
//							if (theEverythingModeEnum.isEncounter()) {
//								if (resourceLink.getSourcePath().equals("Encounter.subject") || resourceLink.getSourcePath().equals("Encounter.patient")) {
//									nextRoundOmit.add(resourceLink.getSourceResourcePid());
//								}
//							}
							pidsToInclude.add(resourceLink.getSourceResourcePid());
						} else {
							pidsToInclude.add(resourceLink.getTargetResourcePid());
						}
					}
				} else {

					List<String> paths;
					if (getContext().getVersion().getVersion() == FhirVersionEnum.DSTU1) {
						paths = Collections.singletonList(nextInclude.getValue());
					} else {
						int colonIdx = nextInclude.getValue().indexOf(':');
						if (colonIdx < 2) {
							continue;
						}
						String resType = nextInclude.getValue().substring(0, colonIdx);
						RuntimeResourceDefinition def = getContext().getResourceDefinition(resType);
						if (def == null) {
							ourLog.warn("Unknown resource type in include/revinclude=" + nextInclude.getValue());
							continue;
						}

						String paramName = nextInclude.getValue().substring(colonIdx + 1);
						RuntimeSearchParam param = def.getSearchParam(paramName);
						if (param == null) {
							ourLog.warn("Unknown param name in include/revinclude=" + nextInclude.getValue());
							continue;
						}

						paths = param.getPathsSplit();
					}

					for (String nextPath : paths) {
						String sql = "SELECT r FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids)";
						TypedQuery<ResourceLink> q = myEntityManager.createQuery(sql, ResourceLink.class);
						q.setParameter("src_path", nextPath);
						q.setParameter("target_pids", nextRoundMatches);
						List<ResourceLink> results = q.getResultList();
						for (ResourceLink resourceLink : results) {
							if (theReverseMode) {
								pidsToInclude.add(resourceLink.getSourceResourcePid());
							} else {
								pidsToInclude.add(resourceLink.getTargetResourcePid());
							}
						}
					}
				}
			}

			if (theLastUpdated != null && (theLastUpdated.getLowerBoundAsInstant() != null || theLastUpdated.getUpperBoundAsInstant() != null)) {
				pidsToInclude = new HashSet<Long>(filterResourceIdsByLastUpdated(pidsToInclude, theLastUpdated));
			}
			for (Long next : pidsToInclude) {
				if (original.contains(next) == false && allAdded.contains(next) == false) {
					theMatches.add(next);
				}
			}

			pidsToInclude.removeAll(nextRoundOmit);

			addedSomeThisRound = allAdded.addAll(pidsToInclude);
			nextRoundMatches = pidsToInclude;
		} while (includes.size() > 0 && nextRoundMatches.size() > 0 && addedSomeThisRound);

		ourLog.info("Loaded {} {} in {} rounds and {} ms", new Object[] { allAdded.size(), theReverseMode ? "_revincludes" : "_includes", roundCounts, w.getMillisAndRestart() });

		return allAdded;
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
		notifyWriteCompleted();
		ourLog.info("Processed metaAddOperation on {} in {}ms", new Object[] { theResourceId, w.getMillisAndRestart() });

		return metaGetOperation(theResourceId);
	}

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
		ResourceTable entity = myEntityManager.find(ResourceTable.class, translateForcedIdToPid(theId));
		if (entity == null) {
			throw new ResourceNotFoundException(theId);
		}
		validateGivenIdIsAppropriateToRetrieveResource(theId, entity);
		return entity;
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

		StopWatch w = new StopWatch();
		final InstantDt now = InstantDt.withCurrentTime();

		DateRangeParam lu = theParams.getLastUpdated();
		if (lu != null && lu.isEmpty()) {
			lu = null;
		}

		Collection<Long> loadPids;
		if (theParams.getEverythingMode() != null) {

			CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<Tuple> cq = builder.createTupleQuery();
			Root<ResourceTable> from = cq.from(ResourceTable.class);
			List<Predicate> predicates = new ArrayList<Predicate>();
			if (theParams.get(BaseResource.SP_RES_ID) != null) {
				StringParam idParm = (StringParam) theParams.get(BaseResource.SP_RES_ID).get(0).get(0);
				predicates.add(builder.equal(from.get("myId"), idParm.getValue()));
			}
			predicates.add(builder.equal(from.get("myResourceType"), myResourceName));
			predicates.add(builder.isNull(from.get("myDeleted")));
			cq.where(builder.and(SearchBuilder.toArray(predicates)));

			Join<Object, Object> join = from.join("myIncomingResourceLinks", JoinType.LEFT);
			cq.multiselect(from.get("myId").as(Long.class), join.get("mySourceResourcePid").as(Long.class));
			
			TypedQuery<Tuple> query = myEntityManager.createQuery(cq);
			loadPids = new HashSet<Long>();
			for (Tuple next : query.getResultList()) {
				loadPids.add(next.get(0, Long.class));
				Long nextLong = next.get(1, Long.class);
				if(nextLong != null) {
					loadPids.add(nextLong);
				}
			}

		} else if (theParams.isEmpty()) {

			loadPids = new HashSet<Long>();
			TypedQuery<Tuple> query = createSearchAllByTypeQuery(lu);
			lu = null;
			for (Tuple next : query.getResultList()) {
				loadPids.add(next.get(0, Long.class));
			}
			if (loadPids.isEmpty()) {
				return new SimpleBundleProvider();
			}

		} else {
			
			List<Long> searchResultPids;
			if (mySearchDao == null) {
				if (theParams.containsKey(Constants.PARAM_TEXT)) {
					throw new InvalidRequestException("Fulltext search is not enabled on this service, can not process parameter: " + Constants.PARAM_TEXT);
				} else if (theParams.containsKey(Constants.PARAM_CONTENT)) {
					throw new InvalidRequestException("Fulltext search is not enabled on this service, can not process parameter: " + Constants.PARAM_CONTENT);
				}
				searchResultPids = null;
			} else {
				searchResultPids = mySearchDao.search(getResourceName(), theParams);
			}
			if (theParams.isEmpty()) {
				loadPids = searchResultPids;
			} else {
				loadPids = searchForIdsWithAndOr(theParams, searchResultPids, lu);
			}
			if (loadPids.isEmpty()) {
				return new SimpleBundleProvider();
			}

		}

		// // Load _include and _revinclude before filter and sort in everything mode
		// if (theParams.getEverythingMode() != null) {
		// if (theParams.getRevIncludes() != null && theParams.getRevIncludes().isEmpty() == false) {
		// loadPids.addAll(loadReverseIncludes(loadPids, theParams.getRevIncludes(), true, theParams.getEverythingMode()));
		// loadPids.addAll(loadReverseIncludes(loadPids, theParams.getIncludes(), false, theParams.getEverythingMode()));
		// }
		// }

		// Handle _lastUpdated
		if (lu != null) {
			List<Long> resultList = filterResourceIdsByLastUpdated(loadPids, lu);
			loadPids.clear();
			for (Long next : resultList) {
				loadPids.add(next);
			}

			if (loadPids.isEmpty()) {
				return new SimpleBundleProvider();
			}
		}

		// Handle sorting if any was provided
		final List<Long> pids = processSort(theParams, loadPids);

		// Load _revinclude resources
		final Set<Long> revIncludedPids;
		if (theParams.getEverythingMode() == null) {
			if (theParams.getRevIncludes() != null && theParams.getRevIncludes().isEmpty() == false) {
				revIncludedPids = loadReverseIncludes(pids, theParams.getRevIncludes(), true, null, lu);
			} else {
				revIncludedPids = new HashSet<Long>();
			}
		} else {
			revIncludedPids = new HashSet<Long>();
		}

		ourLog.debug("Search returned PIDs: {}", pids);

		final int totalCount = pids.size();

		IBundleProvider retVal = new IBundleProvider() {
			@Override
			public InstantDt getPublished() {
				return now;
			}

			@Override
			public List<IBaseResource> getResources(final int theFromIndex, final int theToIndex) {
				TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);
				return template.execute(new TransactionCallback<List<IBaseResource>>() {
					@Override
					public List<IBaseResource> doInTransaction(TransactionStatus theStatus) {
						List<Long> pidsSubList = pids.subList(theFromIndex, theToIndex);

						// Load includes
						pidsSubList = new ArrayList<Long>(pidsSubList);
						revIncludedPids.addAll(loadReverseIncludes(pidsSubList, theParams.getIncludes(), false, null, theParams.getLastUpdated()));

						// Execute the query and make sure we return distinct results
						List<IBaseResource> resources = new ArrayList<IBaseResource>();
						loadResourcesByPid(pidsSubList, resources, revIncludedPids, false);

						return resources;
					}

				});
			}

			@Override
			public Integer preferredPageSize() {
				return theParams.getCount();
			}

			@Override
			public int size() {
				return totalCount;
			}
		};

		ourLog.info(" {} on {} in {}ms", new Object[] { myResourceName, theParams, w.getMillisAndRestart() });

		return retVal;
	}

	private List<Long> filterResourceIdsByLastUpdated(Collection<Long> thePids, final DateRangeParam theLastUpdated) {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		List<Predicate> lastUpdatedPredicates = createLastUpdatedPredicates(theLastUpdated, builder, from);		
		lastUpdatedPredicates.add(0, from.get("myId").in(thePids));
		
		cq.where(toArray(lastUpdatedPredicates));
		TypedQuery<Long> query = myEntityManager.createQuery(cq);
		List<Long> resultList = query.getResultList();
		return resultList;
	}

	private List<Predicate> createLastUpdatedPredicates(final DateRangeParam theLastUpdated, CriteriaBuilder builder, From<?, ResourceTable> from) {
		List<Predicate> lastUpdatedPredicates = new ArrayList<Predicate>();
		if (theLastUpdated != null) {
			if (theLastUpdated.getLowerBoundAsInstant() != null) {
				Predicate predicateLower = builder.greaterThanOrEqualTo(from.<Date> get("myUpdated"), theLastUpdated.getLowerBoundAsInstant());
				lastUpdatedPredicates.add(predicateLower);
			}
			if (theLastUpdated.getUpperBoundAsInstant() != null) {
				Predicate predicateUpper = builder.lessThanOrEqualTo(from.<Date> get("myUpdated"), theLastUpdated.getUpperBoundAsInstant());
				lastUpdatedPredicates.add(predicateUpper);
			}
		}
		return lastUpdatedPredicates;
	}

	

	private List<Long> toList(Collection<Long> theLoadPids) {
		final List<Long> pids;
		if (theLoadPids instanceof List) {
			pids = (List<Long>) theLoadPids;
		} else {
			pids = new ArrayList<Long>(theLoadPids);
		}
		return pids;
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
		SearchParameterMap params = theParams;
		if (params == null) {
			params = new SearchParameterMap();
		}

		RuntimeResourceDefinition resourceDef = getContext().getResourceDefinition(myResourceType);

		Set<Long> pids = new HashSet<Long>();
		if (theInitialPids != null) {
			pids.addAll(theInitialPids);
		}

		for (Entry<String, List<List<? extends IQueryParameterType>>> nextParamEntry : params.entrySet()) {
			String nextParamName = nextParamEntry.getKey();
			if (nextParamName.equals(BaseResource.SP_RES_ID)) {

				if (nextParamEntry.getValue().isEmpty()) {
					continue;
				} else {
					for (List<? extends IQueryParameterType> nextValue : nextParamEntry.getValue()) {
						Set<Long> joinPids = new HashSet<Long>();
						if (nextValue == null || nextValue.size() == 0) {
							continue;
						} else {
							for (IQueryParameterType next : nextValue) {
								String value = next.getValueAsQueryToken();
								IIdType valueId = new IdDt(value);

								try {
									BaseHasResource entity = readEntity(valueId);
									if (entity.getDeleted() != null) {
										continue;
									}
									joinPids.add(entity.getId());
								} catch (ResourceNotFoundException e) {
									// This isn't an error, just means no result found
								}
							}
							if (joinPids.isEmpty()) {
								return new HashSet<Long>();
							}
						}

						pids = addPredicateId(pids, joinPids, theLastUpdated);
						if (pids.isEmpty()) {
							return new HashSet<Long>();
						}

						if (pids.isEmpty()) {
							pids.addAll(joinPids);
						} else {
							pids.retainAll(joinPids);
						}
					}
				}

			} else if (nextParamName.equals(BaseResource.SP_RES_LANGUAGE)) {

				pids = addPredicateLanguage(pids, nextParamEntry.getValue(), theLastUpdated);

			} else if (nextParamName.equals(Constants.PARAM_TAG) || nextParamName.equals(Constants.PARAM_PROFILE) || nextParamName.equals(Constants.PARAM_SECURITY)) {

				pids = addPredicateTag(pids, nextParamEntry.getValue(), nextParamName, theLastUpdated);

			} else {

				RuntimeSearchParam nextParamDef = resourceDef.getSearchParam(nextParamName);
				if (nextParamDef != null) {
					switch (nextParamDef.getParamType()) {
					case DATE:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateDate(nextParamName, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					case QUANTITY:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateQuantity(nextParamName, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					case REFERENCE:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateReference(nextParamName, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					case STRING:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateString(nextParamName, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					case TOKEN:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateToken(nextParamName, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					case NUMBER:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateNumber(nextParamName, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					case COMPOSITE:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateComposite(nextParamDef, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					case URI:
						for (List<? extends IQueryParameterType> nextAnd : nextParamEntry.getValue()) {
							pids = addPredicateUri(nextParamName, pids, nextAnd);
							if (pids.isEmpty()) {
								return new HashSet<Long>();
							}
						}
						break;
					}
				}
			}
		}

		return pids;
	}

	@SuppressWarnings("unchecked")
	@Required
	public void setResourceType(Class<? extends IResource> theTableType) {
		myResourceType = (Class<T>) theTableType;
	}

	/**
	 * If set, the given param will be treated as a secondary primary key, and multiple resources will not be able to share the same value.
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

	private IQueryParameterType toParameterType(RuntimeSearchParam theParam) {
		IQueryParameterType qp;
		switch (theParam.getParamType()) {
		case DATE:
			qp = new DateParam();
			break;
		case NUMBER:
			qp = new NumberParam();
			break;
		case QUANTITY:
			qp = new QuantityParam();
			break;
		case STRING:
			qp = new StringParam();
			break;
		case TOKEN:
			qp = new TokenParam();
			break;
		case COMPOSITE:
			List<RuntimeSearchParam> compositeOf = theParam.getCompositeOf();
			if (compositeOf.size() != 2) {
				throw new InternalErrorException("Parameter " + theParam.getName() + " has " + compositeOf.size() + " composite parts. Don't know how handlt this.");
			}
			IQueryParameterType leftParam = toParameterType(compositeOf.get(0));
			IQueryParameterType rightParam = toParameterType(compositeOf.get(1));
			qp = new CompositeParam<IQueryParameterType, IQueryParameterType>(leftParam, rightParam);
			break;
		case REFERENCE:
			qp = new ReferenceParam();
			break;
		default:
			throw new InternalErrorException("Don't know how to convert param type: " + theParam.getParamType());
		}
		return qp;
	}

	private IQueryParameterType toParameterType(RuntimeSearchParam theParam, String theQualifier, String theValueAsQueryToken) {
		IQueryParameterType qp = toParameterType(theParam);

		qp.setValueAsQueryToken(theQualifier, theValueAsQueryToken); // aaaa
		return qp;
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
			throw new UnprocessableEntityException(
					"Invalid resource ID[" + entity.getIdDt().toUnqualifiedVersionless() + "] of type[" + entity.getResourceType() + "] - Does not match expected [" + getResourceName() + "]");
		}

		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(resourceId, getResourceName(), theResource);
		notifyInterceptors(RestOperationTypeEnum.UPDATE, requestDetails);

		// Perform update
		ResourceTable savedEntity = updateEntity(theResource, entity, true, null, thePerformIndexing, true, new Date());

		notifyWriteCompleted();

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

	protected void validateOkToDeleteOrThrowResourceVersionConflictException(ResourceTable theEntity) {
		TypedQuery<ResourceLink> query = myEntityManager.createQuery("SELECT l FROM ResourceLink l WHERE l.myTargetResourcePid = :target_pid", ResourceLink.class);
		query.setParameter("target_pid", theEntity.getId());
		query.setMaxResults(1);
		List<ResourceLink> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return;
		}

		ResourceLink link = resultList.get(0);
		String targetId = theEntity.getIdDt().toUnqualifiedVersionless().getValue();
		String sourceId = link.getSourceResource().getIdDt().toUnqualifiedVersionless().getValue();
		String sourcePath = link.getSourcePath();

		throw new ResourceVersionConflictException(
				"Unable to delete " + targetId + " because at least one resource has a reference to this resource. First reference found was resource " + sourceId + " in path " + sourcePath);
	}

	private void validateResourceType(BaseHasResource entity) {
		if (!myResourceName.equals(entity.getResourceType())) {
			throw new ResourceNotFoundException(
					"Resource with ID " + entity.getIdDt().getIdPart() + " exists but it is not of type " + myResourceName + ", found resource of type " + entity.getResourceType());
		}
	}

	private void validateResourceTypeAndThrowIllegalArgumentException(IIdType theId) {
		if (theId.hasResourceType() && !theId.getResourceType().equals(myResourceName)) {
			throw new IllegalArgumentException("Incorrect resource type (" + theId.getResourceType() + ") for this DAO, wanted: " + myResourceName);
		}
	}

}
