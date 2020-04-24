package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchViewDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilder;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderFactory;
import ca.uhn.fhir.jpa.dao.predicate.QueryRoot;
import ca.uhn.fhir.jpa.dao.predicate.SearchBuilderJoinEnum;
import ca.uhn.fhir.jpa.dao.predicate.SearchBuilderJoinKey;
import ca.uhn.fhir.jpa.entity.ResourceSearchView;
import ca.uhn.fhir.jpa.interceptor.JpaPreResourceAccessDetails;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedCompositeStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.util.Dstu3DistanceHelper;
import ca.uhn.fhir.jpa.util.BaseIterator;
import ca.uhn.fhir.jpa.util.CurrentThreadCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.jpa.util.ScrollableResultsIterator;
import ca.uhn.fhir.jpa.util.SqlQueryList;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.query.Query;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * The SearchBuilder is responsible for actually forming the SQL query that handles
 * searches for resources
 */
@Component
@Scope("prototype")
public class SearchBuilder implements ISearchBuilder {

	/**
	 * See loadResourcesByPid
	 * for an explanation of why we use the constant 800
	 */
	// NB: keep public
	public static final int MAXIMUM_PAGE_SIZE = 800;

	private static final List<ResourcePersistentId> EMPTY_LONG_LIST = Collections.unmodifiableList(new ArrayList<>());
	private static final Logger ourLog = LoggerFactory.getLogger(SearchBuilder.class);
	private static ResourcePersistentId NO_MORE = new ResourcePersistentId(-1L);
	private final QueryRoot myQueryRoot = new QueryRoot();
	private final String myResourceName;
	private final Class<? extends IBaseResource> myResourceType;
	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	protected IResourceTagDao myResourceTagDao;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private IResourceSearchViewDao myResourceSearchViewDao;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired(required = false)
	private IFulltextSearchSvc myFulltextSearchSvc;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private PredicateBuilderFactory myPredicateBuilderFactory;
	private List<ResourcePersistentId> myAlsoIncludePids;
	private CriteriaBuilder myCriteriaBuilder;
	private IDao myCallingDao;
	private SearchParameterMap myParams;
	private String mySearchUuid;
	private int myFetchSize;
	private Integer myMaxResultsToFetch;
	private Set<ResourcePersistentId> myPidSet;
	private PredicateBuilder myPredicateBuilder;

	/**
	 * Constructor
	 */
	SearchBuilder(IDao theDao, String theResourceName, Class<? extends IBaseResource> theResourceType) {
		myCallingDao = theDao;
		myResourceName = theResourceName;
		myResourceType = theResourceType;
	}

	@Override
	public void setMaxResultsToFetch(Integer theMaxResultsToFetch) {
		myMaxResultsToFetch = theMaxResultsToFetch;
	}

	private void searchForIdsWithAndOr(String theResourceName, String theNextParamName, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest) {
		myPredicateBuilder.searchForIdsWithAndOr(theResourceName, theNextParamName, theAndOrParams, theRequest);
	}

	private void searchForIdsWithAndOr(@Nonnull SearchParameterMap theParams, RequestDetails theRequest) {
		myParams = theParams;

		// Remove any empty parameters
		theParams.clean();

		// For DSTU3, pull out near-distance first so when it comes time to evaluate near, we already know the distance
		if (myContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			Dstu3DistanceHelper.setNearDistance(myResourceType, theParams);
		}

		// Attempt to lookup via composite unique key.
		if (isCompositeUniqueSpCandidate()) {
			attemptCompositeUniqueSpProcessing(theParams, theRequest);
		}

		// Handle each parameter
		for (Map.Entry<String, List<List<IQueryParameterType>>> nextParamEntry : myParams.entrySet()) {
			String nextParamName = nextParamEntry.getKey();
			List<List<IQueryParameterType>> andOrParams = nextParamEntry.getValue();
			searchForIdsWithAndOr(myResourceName, nextParamName, andOrParams, theRequest);
		}
	}

	/**
	 * A search is a candidate for Composite Unique SP if unique indexes are enabled, there is no EverythingMode, and the
	 * parameters all have no modifiers.
	 */
	private boolean isCompositeUniqueSpCandidate() {
		return myDaoConfig.isUniqueIndexesEnabled() &&
			myParams.getEverythingMode() == null &&
			myParams.isAllParametersHaveNoModifier();
	}

	@Override
	public Iterator<Long> createCountQuery(SearchParameterMap theParams, String theSearchUuid, RequestDetails theRequest) {
		init(theParams, theSearchUuid);

		TypedQuery<Long> query = createQuery(null, null, true, theRequest);
		return new CountQueryIterator(query);
	}

	/**
	 * @param thePidSet May be null
	 */
	@Override
	public void setPreviouslyAddedResourcePids(@Nullable List<ResourcePersistentId> thePidSet) {
		myPidSet = new HashSet<>(thePidSet);
	}

	@Override
	public IResultIterator createQuery(SearchParameterMap theParams, SearchRuntimeDetails theSearchRuntimeDetails, RequestDetails theRequest) {
		init(theParams, theSearchRuntimeDetails.getSearchUuid());

		if (myPidSet == null) {
			myPidSet = new HashSet<>();
		}

		return new QueryIterator(theSearchRuntimeDetails, theRequest);
	}

	private void init(SearchParameterMap theParams, String theSearchUuid) {
		myParams = theParams;
		myCriteriaBuilder = myEntityManager.getCriteriaBuilder();
		mySearchUuid = theSearchUuid;
		myPredicateBuilder = new PredicateBuilder(this, myPredicateBuilderFactory);
	}

	private TypedQuery<Long> createQuery(SortSpec sort, Integer theMaximumResults, boolean theCount, RequestDetails theRequest) {
		CriteriaQuery<Long> outerQuery;
		/*
		 * Sort
		 *
		 * If we have a sort, we wrap the criteria search (the search that actually
		 * finds the appropriate resources) in an outer search which is then sorted
		 */
		if (sort != null) {
			assert !theCount;

			outerQuery = myCriteriaBuilder.createQuery(Long.class);
			myQueryRoot.push(outerQuery);
			if (theCount) {
				outerQuery.multiselect(myCriteriaBuilder.countDistinct(myQueryRoot.getRoot()));
			} else {
				outerQuery.multiselect(myQueryRoot.get("myId").as(Long.class));
			}

			List<Order> orders = Lists.newArrayList();

			createSort(myCriteriaBuilder, myQueryRoot, sort, orders);
			if (orders.size() > 0) {
				outerQuery.orderBy(orders);
			}

		} else {

			outerQuery = myCriteriaBuilder.createQuery(Long.class);
			myQueryRoot.push(outerQuery);
			if (theCount) {
				outerQuery.multiselect(myCriteriaBuilder.countDistinct(myQueryRoot.getRoot()));
			} else {
				outerQuery.multiselect(myQueryRoot.get("myId").as(Long.class));
				// KHS This distinct call is causing performance issues in large installations
//				outerQuery.distinct(true);
			}
		}

		if (myParams.getEverythingMode() != null) {
			Join<ResourceTable, ResourceLink> join = myQueryRoot.join("myResourceLinks", JoinType.LEFT);

			if (myParams.get(IAnyResource.SP_RES_ID) != null) {
				StringParam idParam = (StringParam) myParams.get(IAnyResource.SP_RES_ID).get(0).get(0);
				ResourcePersistentId pid = myIdHelperService.resolveResourcePersistentIds(myResourceName, idParam.getValue());
				if (myAlsoIncludePids == null) {
					myAlsoIncludePids = new ArrayList<>(1);
				}
				myAlsoIncludePids.add(pid);
				myQueryRoot.addPredicate(myCriteriaBuilder.equal(join.get("myTargetResourcePid").as(Long.class), pid.getIdAsLong()));
			} else {
				Predicate targetTypePredicate = myCriteriaBuilder.equal(join.get("myTargetResourceType").as(String.class), myResourceName);
				Predicate sourceTypePredicate = myCriteriaBuilder.equal(myQueryRoot.get("myResourceType").as(String.class), myResourceName);
				myQueryRoot.addPredicate(myCriteriaBuilder.or(sourceTypePredicate, targetTypePredicate));
			}

		} else {
			// Normal search
			searchForIdsWithAndOr(myParams, theRequest);
		}

		/*
		 * Fulltext search
		 */
		if (myParams.containsKey(Constants.PARAM_CONTENT) || myParams.containsKey(Constants.PARAM_TEXT)) {
			if (myFulltextSearchSvc == null) {
				if (myParams.containsKey(Constants.PARAM_TEXT)) {
					throw new InvalidRequestException("Fulltext search is not enabled on this service, can not process parameter: " + Constants.PARAM_TEXT);
				} else if (myParams.containsKey(Constants.PARAM_CONTENT)) {
					throw new InvalidRequestException("Fulltext search is not enabled on this service, can not process parameter: " + Constants.PARAM_CONTENT);
				}
			}

			List<ResourcePersistentId> pids;
			if (myParams.getEverythingMode() != null) {
				pids = myFulltextSearchSvc.everything(myResourceName, myParams, theRequest);
			} else {
				pids = myFulltextSearchSvc.search(myResourceName, myParams);
			}
			if (pids.isEmpty()) {
				// Will never match
				pids = Collections.singletonList(new ResourcePersistentId(-1L));
			}

			myQueryRoot.addPredicate(myQueryRoot.get("myId").as(Long.class).in(ResourcePersistentId.toLongList(pids)));
		}

		/*
		 * Add a predicate to make sure we only include non-deleted resources, and only include
		 * resources of the right type.
		 *
		 * If we have any joins to index tables, we get this behaviour already guaranteed so we don't
		 * need an explicit predicate for it.
		 */
		boolean haveNoIndexSearchParams = myParams.size() == 0 || myParams.keySet().stream().allMatch(t -> t.startsWith("_"));
		if (haveNoIndexSearchParams) {
			if (myParams.getEverythingMode() == null) {
				myQueryRoot.addPredicate(myCriteriaBuilder.equal(myQueryRoot.get("myResourceType"), myResourceName));
			}
			myQueryRoot.addPredicate(myCriteriaBuilder.isNull(myQueryRoot.get("myDeleted")));
		}

		// Last updated
		DateRangeParam lu = myParams.getLastUpdated();
		List<Predicate> lastUpdatedPredicates = createLastUpdatedPredicates(lu, myCriteriaBuilder, myQueryRoot.getRoot());
		myQueryRoot.addPredicates(lastUpdatedPredicates);

		myQueryRoot.where(myCriteriaBuilder.and(myQueryRoot.getPredicateArray()));

		/*
		 * Now perform the search
		 */
		final TypedQuery<Long> query = myEntityManager.createQuery(outerQuery);

		if (theMaximumResults != null) {
			query.setMaxResults(theMaximumResults);
		}

		return query;
	}

	/**
	 * @return Returns {@literal true} if any search parameter sorts were found, or false if
	 * no sorts were found, or only non-search parameters ones (e.g. _id, _lastUpdated)
	 */
	private boolean createSort(CriteriaBuilder theBuilder, QueryRoot theQueryRoot, SortSpec theSort, List<Order> theOrders) {
		if (theSort == null || isBlank(theSort.getParamName())) {
			return false;
		}

		if (IAnyResource.SP_RES_ID.equals(theSort.getParamName())) {
			From<?, ?> forcedIdJoin = theQueryRoot.join("myForcedId", JoinType.LEFT);
			if (theSort.getOrder() == null || theSort.getOrder() == SortOrderEnum.ASC) {
				theOrders.add(theBuilder.asc(forcedIdJoin.get("myForcedId")));
				theOrders.add(theBuilder.asc(theQueryRoot.get("myId")));
			} else {
				theOrders.add(theBuilder.desc(forcedIdJoin.get("myForcedId")));
				theOrders.add(theBuilder.desc(theQueryRoot.get("myId")));
			}

			return createSort(theBuilder, theQueryRoot, theSort.getChain(), theOrders);
		}

		if (Constants.PARAM_LASTUPDATED.equals(theSort.getParamName())) {
			if (theSort.getOrder() == null || theSort.getOrder() == SortOrderEnum.ASC) {
				theOrders.add(theBuilder.asc(theQueryRoot.get("myUpdated")));
			} else {
				theOrders.add(theBuilder.desc(theQueryRoot.get("myUpdated")));
			}

			return createSort(theBuilder, theQueryRoot, theSort.getChain(), theOrders);
		}

		RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(myResourceName);
		RuntimeSearchParam param = mySearchParamRegistry.getSearchParamByName(resourceDef, theSort.getParamName());
		if (param == null) {
			throw new InvalidRequestException("Unknown sort parameter '" + theSort.getParamName() + "'");
		}

		String joinAttrName;
		String[] sortAttrName;
		SearchBuilderJoinEnum joinType;

		switch (param.getParamType()) {
			case STRING:
				joinAttrName = "myParamsString";
				sortAttrName = new String[]{"myValueExact"};
				joinType = SearchBuilderJoinEnum.STRING;
				break;
			case DATE:
				joinAttrName = "myParamsDate";
				sortAttrName = new String[]{"myValueLow"};
				joinType = SearchBuilderJoinEnum.DATE;
				break;
			case REFERENCE:
				joinAttrName = "myResourceLinks";
				sortAttrName = new String[]{"myTargetResourcePid"};
				joinType = SearchBuilderJoinEnum.REFERENCE;
				break;
			case TOKEN:
				joinAttrName = "myParamsToken";
				sortAttrName = new String[]{"mySystem", "myValue"};
				joinType = SearchBuilderJoinEnum.TOKEN;
				break;
			case NUMBER:
				joinAttrName = "myParamsNumber";
				sortAttrName = new String[]{"myValue"};
				joinType = SearchBuilderJoinEnum.NUMBER;
				break;
			case URI:
				joinAttrName = "myParamsUri";
				sortAttrName = new String[]{"myUri"};
				joinType = SearchBuilderJoinEnum.URI;
				break;
			case QUANTITY:
				joinAttrName = "myParamsQuantity";
				sortAttrName = new String[]{"myValue"};
				joinType = SearchBuilderJoinEnum.QUANTITY;
				break;
			case SPECIAL:
			case COMPOSITE:
			case HAS:
			default:
				throw new InvalidRequestException("This server does not support _sort specifications of type " + param.getParamType() + " - Can't serve _sort=" + theSort.getParamName());
		}

		/*
		 * If we've already got a join for the specific parameter we're
		 * sorting on, we'll also sort with it. Otherwise we need a new join.
		 */
		SearchBuilderJoinKey key = new SearchBuilderJoinKey(theSort.getParamName(), joinType);
		Join<?, ?> join = theQueryRoot.getIndexJoin(key);
		if (join == null) {
			join = theQueryRoot.join(joinAttrName, JoinType.LEFT);

			if (param.getParamType() == RestSearchParameterTypeEnum.REFERENCE) {
				theQueryRoot.addPredicate(join.get("mySourcePath").as(String.class).in(param.getPathsSplit()));
			} else {
				if (myDaoConfig.getDisableHashBasedSearches()) {
					Predicate joinParam1 = theBuilder.equal(join.get("myParamName"), theSort.getParamName());
					theQueryRoot.addPredicate(joinParam1);
				} else {
					Long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(myResourceName, theSort.getParamName());
					Predicate joinParam1 = theBuilder.equal(join.get("myHashIdentity"), hashIdentity);
					theQueryRoot.addPredicate(joinParam1);
				}
			}
		} else {
			ourLog.debug("Reusing join for {}", theSort.getParamName());
		}

		for (String next : sortAttrName) {
			if (theSort.getOrder() == null || theSort.getOrder() == SortOrderEnum.ASC) {
				theOrders.add(theBuilder.asc(join.get(next)));
			} else {
				theOrders.add(theBuilder.desc(join.get(next)));
			}
		}

		createSort(theBuilder, theQueryRoot, theSort.getChain(), theOrders);

		return true;
	}


	private void doLoadPids(Collection<ResourcePersistentId> thePids, Collection<ResourcePersistentId> theIncludedPids, List<IBaseResource> theResourceListToPopulate, boolean theForHistoryOperation,
									Map<ResourcePersistentId, Integer> thePosition, RequestDetails theRequest) {

		// -- get the resource from the searchView
		Collection<ResourceSearchView> resourceSearchViewList = myResourceSearchViewDao.findByResourceIds(ResourcePersistentId.toLongList(thePids));

		//-- preload all tags with tag definition if any
		Map<ResourcePersistentId, Collection<ResourceTag>> tagMap = getResourceTagMap(resourceSearchViewList);

		ResourcePersistentId resourceId;
		for (ResourceSearchView next : resourceSearchViewList) {
			if (next.getDeleted() != null) {
				continue;
			}

			Class<? extends IBaseResource> resourceType = myContext.getResourceDefinition(next.getResourceType()).getImplementingClass();

			resourceId = new ResourcePersistentId(next.getId());

			IBaseResource resource = myCallingDao.toResource(resourceType, next, tagMap.get(resourceId), theForHistoryOperation);
			if (resource == null) {
				ourLog.warn("Unable to find resource {}/{}/_history/{} in database", next.getResourceType(), next.getIdDt().getIdPart(), next.getVersion());
				continue;
			}
			Integer index = thePosition.get(resourceId);
			if (index == null) {
				ourLog.warn("Got back unexpected resource PID {}", resourceId);
				continue;
			}

			if (resource instanceof IResource) {
				if (theIncludedPids.contains(resourceId)) {
					ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put((IResource) resource, BundleEntrySearchModeEnum.INCLUDE);
				} else {
					ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put((IResource) resource, BundleEntrySearchModeEnum.MATCH);
				}
			} else {
				if (theIncludedPids.contains(resourceId)) {
					ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put((IAnyResource) resource, BundleEntrySearchModeEnum.INCLUDE.getCode());
				} else {
					ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put((IAnyResource) resource, BundleEntrySearchModeEnum.MATCH.getCode());
				}
			}

			theResourceListToPopulate.set(index, resource);
		}
	}

	private Map<ResourcePersistentId, Collection<ResourceTag>> getResourceTagMap(Collection<ResourceSearchView> theResourceSearchViewList) {

		List<Long> idList = new ArrayList<>(theResourceSearchViewList.size());

		//-- find all resource has tags
		for (ResourceSearchView resource : theResourceSearchViewList) {
			if (resource.isHasTags())
				idList.add(resource.getId());
		}

		Map<ResourcePersistentId, Collection<ResourceTag>> tagMap = new HashMap<>();

		//-- no tags
		if (idList.size() == 0)
			return tagMap;

		//-- get all tags for the idList
		Collection<ResourceTag> tagList = myResourceTagDao.findByResourceIds(idList);

		//-- build the map, key = resourceId, value = list of ResourceTag
		ResourcePersistentId resourceId;
		Collection<ResourceTag> tagCol;
		for (ResourceTag tag : tagList) {

			resourceId = new ResourcePersistentId(tag.getResourceId());
			tagCol = tagMap.get(resourceId);
			if (tagCol == null) {
				tagCol = new ArrayList<>();
				tagCol.add(tag);
				tagMap.put(resourceId, tagCol);
			} else {
				tagCol.add(tag);
			}
		}

		return tagMap;
	}

	@Override
	public void loadResourcesByPid(Collection<ResourcePersistentId> thePids, Collection<ResourcePersistentId> theIncludedPids, List<IBaseResource> theResourceListToPopulate, boolean theForHistoryOperation, RequestDetails theDetails) {
		if (thePids.isEmpty()) {
			ourLog.debug("The include pids are empty");
			// return;
		}

		// Dupes will cause a crash later anyhow, but this is expensive so only do it
		// when running asserts
		assert new HashSet<>(thePids).size() == thePids.size() : "PID list contains duplicates: " + thePids;

		Map<ResourcePersistentId, Integer> position = new HashMap<>();
		for (ResourcePersistentId next : thePids) {
			position.put(next, theResourceListToPopulate.size());
			theResourceListToPopulate.add(null);
		}

		/*
		 * As always, Oracle can't handle things that other databases don't mind.. In this
		 * case it doesn't like more than ~1000 IDs in a single load, so we break this up
		 * if it's lots of IDs. I suppose maybe we should be doing this as a join anyhow
		 * but this should work too. Sigh.
		 */
		List<ResourcePersistentId> pids = new ArrayList<>(thePids);
		for (int i = 0; i < pids.size(); i += MAXIMUM_PAGE_SIZE) {
			int to = i + MAXIMUM_PAGE_SIZE;
			to = Math.min(to, pids.size());
			List<ResourcePersistentId> pidsSubList = pids.subList(i, to);
			doLoadPids(pidsSubList, theIncludedPids, theResourceListToPopulate, theForHistoryOperation, position, theDetails);
		}

	}

	/**
	 * THIS SHOULD RETURN HASHSET and not just Set because we add to it later
	 * so it can't be Collections.emptySet() or some such thing
	 */
	@Override
	public HashSet<ResourcePersistentId> loadIncludes(FhirContext theContext, EntityManager theEntityManager, Collection<ResourcePersistentId> theMatches, Set<Include> theRevIncludes,
																	  boolean theReverseMode, DateRangeParam theLastUpdated, String theSearchIdOrDescription, RequestDetails theRequest) {
		if (theMatches.size() == 0) {
			return new HashSet<>();
		}
		if (theRevIncludes == null || theRevIncludes.isEmpty()) {
			return new HashSet<>();
		}
		String searchFieldName = theReverseMode ? "myTargetResourcePid" : "mySourceResourcePid";
		String findFieldName = theReverseMode ? "mySourceResourcePid" : "myTargetResourcePid";

		Collection<ResourcePersistentId> nextRoundMatches = theMatches;
		HashSet<ResourcePersistentId> allAdded = new HashSet<>();
		HashSet<ResourcePersistentId> original = new HashSet<>(theMatches);
		ArrayList<Include> includes = new ArrayList<>(theRevIncludes);

		int roundCounts = 0;
		StopWatch w = new StopWatch();

		boolean addedSomeThisRound;
		do {
			roundCounts++;

			HashSet<ResourcePersistentId> pidsToInclude = new HashSet<>();

			for (Iterator<Include> iter = includes.iterator(); iter.hasNext(); ) {
				Include nextInclude = iter.next();
				if (nextInclude.isRecurse() == false) {
					iter.remove();
				}

				boolean matchAll = "*".equals(nextInclude.getValue());
				if (matchAll) {
					String sql;
					sql = "SELECT r." + findFieldName + " FROM ResourceLink r WHERE r." + searchFieldName + " IN (:target_pids) ";
					List<Collection<ResourcePersistentId>> partitions = partition(nextRoundMatches, MAXIMUM_PAGE_SIZE);
					for (Collection<ResourcePersistentId> nextPartition : partitions) {
						TypedQuery<Long> q = theEntityManager.createQuery(sql, Long.class);
						q.setParameter("target_pids", ResourcePersistentId.toLongList(nextPartition));
						List<Long> results = q.getResultList();
						for (Long resourceLink : results) {
							if (theReverseMode) {
								pidsToInclude.add(new ResourcePersistentId(resourceLink));
							} else {
								pidsToInclude.add(new ResourcePersistentId(resourceLink));
							}
						}
					}
				} else {

					List<String> paths;
					RuntimeSearchParam param;
					String resType = nextInclude.getParamType();
					if (isBlank(resType)) {
						continue;
					}
					RuntimeResourceDefinition def = theContext.getResourceDefinition(resType);
					if (def == null) {
						ourLog.warn("Unknown resource type in include/revinclude=" + nextInclude.getValue());
						continue;
					}

					String paramName = nextInclude.getParamName();
					if (isNotBlank(paramName)) {
						param = mySearchParamRegistry.getSearchParamByName(def, paramName);
					} else {
						param = null;
					}
					if (param == null) {
						ourLog.warn("Unknown param name in include/revinclude=" + nextInclude.getValue());
						continue;
					}

					paths = param.getPathsSplit();

					String targetResourceType = defaultString(nextInclude.getParamTargetType(), null);
					for (String nextPath : paths) {
						String sql;

						boolean haveTargetTypesDefinedByParam = param.hasTargets();
						if (targetResourceType != null) {
							sql = "SELECT r." + findFieldName + " FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids) AND r.myTargetResourceType = :target_resource_type";
						} else if (haveTargetTypesDefinedByParam) {
							sql = "SELECT r." + findFieldName + " FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids) AND r.myTargetResourceType in (:target_resource_types)";
						} else {
							sql = "SELECT r." + findFieldName + " FROM ResourceLink r WHERE r.mySourcePath = :src_path AND r." + searchFieldName + " IN (:target_pids)";
						}

						List<Collection<ResourcePersistentId>> partitions = partition(nextRoundMatches, MAXIMUM_PAGE_SIZE);
						for (Collection<ResourcePersistentId> nextPartition : partitions) {
							TypedQuery<Long> q = theEntityManager.createQuery(sql, Long.class);
							q.setParameter("src_path", nextPath);
							q.setParameter("target_pids", ResourcePersistentId.toLongList(nextPartition));
							if (targetResourceType != null) {
								q.setParameter("target_resource_type", targetResourceType);
							} else if (haveTargetTypesDefinedByParam) {
								q.setParameter("target_resource_types", param.getTargets());
							}
							List<Long> results = q.getResultList();
							for (Long resourceLink : results) {
								if (resourceLink != null) {
									pidsToInclude.add(new ResourcePersistentId(resourceLink));
								}
							}
						}
					}
				}
			}

			if (theReverseMode) {
				if (theLastUpdated != null && (theLastUpdated.getLowerBoundAsInstant() != null || theLastUpdated.getUpperBoundAsInstant() != null)) {
					pidsToInclude = new HashSet<>(filterResourceIdsByLastUpdated(theEntityManager, theLastUpdated, pidsToInclude));
				}
			}
			for (ResourcePersistentId next : pidsToInclude) {
				if (original.contains(next) == false && allAdded.contains(next) == false) {
					theMatches.add(next);
				}
			}

			addedSomeThisRound = allAdded.addAll(pidsToInclude);
			nextRoundMatches = pidsToInclude;
		} while (includes.size() > 0 && nextRoundMatches.size() > 0 && addedSomeThisRound);

		allAdded.removeAll(original);

		ourLog.info("Loaded {} {} in {} rounds and {} ms for search {}", allAdded.size(), theReverseMode ? "_revincludes" : "_includes", roundCounts, w.getMillisAndRestart(), theSearchIdOrDescription);

		// Interceptor call: STORAGE_PREACCESS_RESOURCES
		// This can be used to remove results from the search result details before
		// the user has a chance to know that they were in the results
		if (allAdded.size() > 0) {
			List<ResourcePersistentId> includedPidList = new ArrayList<>(allAdded);
			JpaPreResourceAccessDetails accessDetails = new JpaPreResourceAccessDetails(includedPidList, () -> this);
			HookParams params = new HookParams()
				.add(IPreResourceAccessDetails.class, accessDetails)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);
			JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);

			for (int i = includedPidList.size() - 1; i >= 0; i--) {
				if (accessDetails.isDontReturnResourceAtIndex(i)) {
					ResourcePersistentId value = includedPidList.remove(i);
					if (value != null) {
						theMatches.remove(value);
					}
				}
			}

			allAdded = new HashSet<>(includedPidList);
		}

		return allAdded;
	}

	private List<Collection<ResourcePersistentId>> partition(Collection<ResourcePersistentId> theNextRoundMatches, int theMaxLoad) {
		if (theNextRoundMatches.size() <= theMaxLoad) {
			return Collections.singletonList(theNextRoundMatches);
		} else {

			List<Collection<ResourcePersistentId>> retVal = new ArrayList<>();
			Collection<ResourcePersistentId> current = null;
			for (ResourcePersistentId next : theNextRoundMatches) {
				if (current == null) {
					current = new ArrayList<>(theMaxLoad);
					retVal.add(current);
				}

				current.add(next);

				if (current.size() >= theMaxLoad) {
					current = null;
				}
			}

			return retVal;
		}
	}

	private void attemptCompositeUniqueSpProcessing(@Nonnull SearchParameterMap theParams, RequestDetails theRequest) {
		// Since we're going to remove elements below
		theParams.values().forEach(nextAndList -> ensureSubListsAreWritable(nextAndList));

		List<JpaRuntimeSearchParam> activeUniqueSearchParams = mySearchParamRegistry.getActiveUniqueSearchParams(myResourceName, theParams.keySet());
		if (activeUniqueSearchParams.size() > 0) {

			StringBuilder sb = new StringBuilder();
			sb.append(myResourceName);
			sb.append("?");

			boolean first = true;

			ArrayList<String> keys = new ArrayList<>(theParams.keySet());
			Collections.sort(keys);
			for (String nextParamName : keys) {
				List<List<IQueryParameterType>> nextValues = theParams.get(nextParamName);

				nextParamName = UrlUtil.escapeUrlParam(nextParamName);
				if (nextValues.get(0).size() != 1) {
					sb = null;
					break;
				}

				// Reference params are only eligible for using a composite index if they
				// are qualified
				RuntimeSearchParam nextParamDef = mySearchParamRegistry.getActiveSearchParam(myResourceName, nextParamName);
				if (nextParamDef.getParamType() == RestSearchParameterTypeEnum.REFERENCE) {
					ReferenceParam param = (ReferenceParam) nextValues.get(0).get(0);
					if (isBlank(param.getResourceType())) {
						sb = null;
						break;
					}
				}

				List<? extends IQueryParameterType> nextAnd = nextValues.remove(0);
				IQueryParameterType nextOr = nextAnd.remove(0);
				String nextOrValue = nextOr.getValueAsQueryToken(myContext);
				nextOrValue = UrlUtil.escapeUrlParam(nextOrValue);

				if (first) {
					first = false;
				} else {
					sb.append('&');
				}

				sb.append(nextParamName).append('=').append(nextOrValue);

			}

			if (sb != null) {
				String indexString = sb.toString();
				ourLog.debug("Checking for unique index for query: {}", indexString);

				// Interceptor broadcast: JPA_PERFTRACE_INFO
				StorageProcessingMessage msg = new StorageProcessingMessage()
					.setMessage("Using unique index for query for search: " + indexString);
				HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(StorageProcessingMessage.class, msg);
				JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_INFO, params);

				addPredicateCompositeStringUnique(theParams, indexString);
			}
		}
	}

	private <T> void ensureSubListsAreWritable(List<List<T>> theListOfLists) {
		for (int i = 0; i < theListOfLists.size(); i++) {
			List<T> oldSubList = theListOfLists.get(i);
			if (!(oldSubList instanceof ArrayList)) {
				List<T> newSubList = new ArrayList<>(oldSubList);
				theListOfLists.set(i, newSubList);
			}
		}
	}

	private void addPredicateCompositeStringUnique(@Nonnull SearchParameterMap theParams, String theIndexedString) {
		myQueryRoot.setHasIndexJoins(true);
		Join<ResourceTable, ResourceIndexedCompositeStringUnique> join = myQueryRoot.join("myParamsCompositeStringUnique", JoinType.LEFT);
		Predicate predicate = myCriteriaBuilder.equal(join.get("myIndexString"), theIndexedString);
		myQueryRoot.addPredicate(predicate);

		// Remove any empty parameters remaining after this
		theParams.clean();
	}

	@Override
	public void setFetchSize(int theFetchSize) {
		myFetchSize = theFetchSize;
	}

	@VisibleForTesting
	void setParamsForUnitTest(SearchParameterMap theParams) {
		myParams = theParams;
	}

	public SearchParameterMap getParams() {
		return myParams;
	}

	@VisibleForTesting
	void setEntityManagerForUnitTest(EntityManager theEntityManager) {
		myEntityManager = theEntityManager;
	}

	public CriteriaBuilder getBuilder() {
		return myCriteriaBuilder;
	}

	public QueryRoot getQueryRoot() {
		return myQueryRoot;
	}

	public Class<? extends IBaseResource> getResourceType() {
		return myResourceType;
	}

	public String getResourceName() {
		return myResourceName;
	}

	public IDao getCallingDao() {
		return myCallingDao;
	}

	@VisibleForTesting
	public void setDaoConfigForUnitTest(DaoConfig theDaoConfig) {
		myDaoConfig = theDaoConfig;
	}

	public class IncludesIterator extends BaseIterator<ResourcePersistentId> implements Iterator<ResourcePersistentId> {

		private final RequestDetails myRequest;
		private Iterator<ResourcePersistentId> myCurrentIterator;
		private Set<ResourcePersistentId> myCurrentPids;
		private ResourcePersistentId myNext;
		private int myPageSize = myDaoConfig.getEverythingIncludesFetchPageSize();

		IncludesIterator(Set<ResourcePersistentId> thePidSet, RequestDetails theRequest) {
			myCurrentPids = new HashSet<>(thePidSet);
			myCurrentIterator = EMPTY_LONG_LIST.iterator();
			myRequest = theRequest;
		}

		private void fetchNext() {
			while (myNext == null) {

				if (myCurrentIterator.hasNext()) {
					myNext = myCurrentIterator.next();
					break;
				}

				Set<Include> includes = Collections.singleton(new Include("*", true));
				Set<ResourcePersistentId> newPids = loadIncludes(myContext, myEntityManager, myCurrentPids, includes, false, getParams().getLastUpdated(), mySearchUuid, myRequest);
				if (newPids.isEmpty()) {
					myNext = NO_MORE;
					break;
				}
				myCurrentIterator = newPids.iterator();
			}
		}

		@Override
		public boolean hasNext() {
			fetchNext();
			return !NO_MORE.equals(myNext);
		}

		@Override
		public ResourcePersistentId next() {
			fetchNext();
			ResourcePersistentId retVal = myNext;
			myNext = null;
			return retVal;
		}

	}

	private final class QueryIterator extends BaseIterator<ResourcePersistentId> implements IResultIterator {

		private final SearchRuntimeDetails mySearchRuntimeDetails;
		private final RequestDetails myRequest;
		private final boolean myHaveRawSqlHooks;
		private final boolean myHavePerfTraceFoundIdHook;
		private boolean myFirst = true;
		private IncludesIterator myIncludesIterator;
		private ResourcePersistentId myNext;
		private Iterator<ResourcePersistentId> myPreResultsIterator;
		private ScrollableResultsIterator<Long> myResultsIterator;
		private SortSpec mySort;
		private boolean myStillNeedToFetchIncludes;
		private int mySkipCount = 0;
		private int myNonSkipCount = 0;

		private QueryIterator(SearchRuntimeDetails theSearchRuntimeDetails, RequestDetails theRequest) {
			mySearchRuntimeDetails = theSearchRuntimeDetails;
			mySort = myParams.getSort();
			myRequest = theRequest;

			// Includes are processed inline for $everything query
			if (myParams.getEverythingMode() != null) {
				myStillNeedToFetchIncludes = true;
			}

			myHavePerfTraceFoundIdHook = JpaInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_SEARCH_FOUND_ID, myInterceptorBroadcaster, myRequest);
			myHaveRawSqlHooks = JpaInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_RAW_SQL, myInterceptorBroadcaster, myRequest);

		}

		private void fetchNext() {

			try {
				if (myHaveRawSqlHooks) {
					CurrentThreadCaptureQueriesListener.startCapturing();
				}

				// If we don't have a query yet, create one
				if (myResultsIterator == null) {
					if (myMaxResultsToFetch == null) {
						myMaxResultsToFetch = myDaoConfig.getFetchSizeDefaultMaximum();
					}

					initializeIteratorQuery(myMaxResultsToFetch);

					// If the query resulted in extra results being requested
					if (myAlsoIncludePids != null) {
						myPreResultsIterator = myAlsoIncludePids.iterator();
					}
				}

				if (myNext == null) {

					if (myPreResultsIterator != null && myPreResultsIterator.hasNext()) {
						while (myPreResultsIterator.hasNext()) {
							ResourcePersistentId next = myPreResultsIterator.next();
							if (next != null)
								if (myPidSet.add(next)) {
									myNext = next;
									break;
								}
						}
					}

					if (myNext == null) {
						while (myResultsIterator.hasNext()) {
							Long nextLong = myResultsIterator.next();
							if (myHavePerfTraceFoundIdHook) {
								HookParams params = new HookParams()
									.add(Integer.class, System.identityHashCode(this))
									.add(Object.class, nextLong);
								JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_FOUND_ID, params);
							}

							if (nextLong != null) {
								ResourcePersistentId next = new ResourcePersistentId(nextLong);
								if (myPidSet.add(next)) {
									myNext = next;
									myNonSkipCount++;
									break;
								} else {
									mySkipCount++;
								}
							}

							if (!myResultsIterator.hasNext()) {
								if (myMaxResultsToFetch != null && (mySkipCount + myNonSkipCount == myMaxResultsToFetch)) {
									if (mySkipCount > 0 && myNonSkipCount == 0) {
										myMaxResultsToFetch += 1000;

										StorageProcessingMessage message = new StorageProcessingMessage();
										String msg = "Pass completed with no matching results. This indicates an inefficient query! Retrying with new max count of " + myMaxResultsToFetch;
										ourLog.warn(msg);
										message.setMessage(msg);
										HookParams params = new HookParams()
											.add(RequestDetails.class, myRequest)
											.addIfMatchesType(ServletRequestDetails.class, myRequest)
											.add(StorageProcessingMessage.class, message);
										JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_WARNING, params);

										initializeIteratorQuery(myMaxResultsToFetch);
									}
								}
							}
						}
					}

					if (myNext == null) {
						if (myStillNeedToFetchIncludes) {
							myIncludesIterator = new IncludesIterator(myPidSet, myRequest);
							myStillNeedToFetchIncludes = false;
						}
						if (myIncludesIterator != null) {
							while (myIncludesIterator.hasNext()) {
								ResourcePersistentId next = myIncludesIterator.next();
								if (next != null)
									if (myPidSet.add(next)) {
										myNext = next;
										break;
									}
							}
							if (myNext == null) {
								myNext = NO_MORE;
							}
						} else {
							myNext = NO_MORE;
						}
					}

				} // if we need to fetch the next result

				mySearchRuntimeDetails.setFoundMatchesCount(myPidSet.size());

			} finally {
				if (myHaveRawSqlHooks) {
					SqlQueryList capturedQueries = CurrentThreadCaptureQueriesListener.getCurrentQueueAndStopCapturing();
					HookParams params = new HookParams()
						.add(RequestDetails.class, myRequest)
						.addIfMatchesType(ServletRequestDetails.class, myRequest)
						.add(SqlQueryList.class, capturedQueries);
					JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_RAW_SQL, params);
				}
			}

			if (myFirst) {
				HookParams params = new HookParams()
					.add(RequestDetails.class, myRequest)
					.addIfMatchesType(ServletRequestDetails.class, myRequest)
					.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED, params);
				myFirst = false;
			}

			if (NO_MORE.equals(myNext)) {
				HookParams params = new HookParams()
					.add(RequestDetails.class, myRequest)
					.addIfMatchesType(ServletRequestDetails.class, myRequest)
					.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE, params);
			}

		}

		private void initializeIteratorQuery(Integer theMaxResultsToFetch) {
			final TypedQuery<Long> query = createQuery(mySort, theMaxResultsToFetch, false, myRequest);

			mySearchRuntimeDetails.setQueryStopwatch(new StopWatch());

			Query<Long> hibernateQuery = (Query<Long>) query;
			hibernateQuery.setFetchSize(myFetchSize);
			ScrollableResults scroll = hibernateQuery.scroll(ScrollMode.FORWARD_ONLY);
			myResultsIterator = new ScrollableResultsIterator<>(scroll);

			mySkipCount = 0;
			myNonSkipCount = 0;
		}

		@Override
		public boolean hasNext() {
			if (myNext == null) {
				fetchNext();
			}
			return !NO_MORE.equals(myNext);
		}

		@Override
		public ResourcePersistentId next() {
			fetchNext();
			ResourcePersistentId retVal = myNext;
			myNext = null;
			Validate.isTrue(!NO_MORE.equals(retVal), "No more elements");
			return retVal;
		}

		@Override
		public int getSkippedCount() {
			return mySkipCount;
		}

		@Override
		public int getNonSkippedCount() {
			return myNonSkipCount;
		}

		@Override
		public void close() {
			if (myResultsIterator != null) {
				myResultsIterator.close();
			}
		}
	}

	private static class CountQueryIterator implements Iterator<Long> {
		private final TypedQuery<Long> myQuery;
		private boolean myCountLoaded;
		private Long myCount;

		CountQueryIterator(TypedQuery<Long> theQuery) {
			myQuery = theQuery;
		}

		@Override
		public boolean hasNext() {
			boolean retVal = myCount != null;
			if (!retVal) {
				if (myCountLoaded == false) {
					myCount = myQuery.getSingleResult();
					retVal = true;
					myCountLoaded = true;
				}
			}
			return retVal;
		}

		@Override
		public Long next() {
			Validate.isTrue(hasNext());
			Validate.isTrue(myCount != null);
			Long retVal = myCount;
			myCount = null;
			return retVal;
		}
	}

	private static List<Predicate> createLastUpdatedPredicates(final DateRangeParam theLastUpdated, CriteriaBuilder builder, From<?, ResourceTable> from) {
		List<Predicate> lastUpdatedPredicates = new ArrayList<>();
		if (theLastUpdated != null) {
			if (theLastUpdated.getLowerBoundAsInstant() != null) {
				ourLog.debug("LastUpdated lower bound: {}", new InstantDt(theLastUpdated.getLowerBoundAsInstant()));
				Predicate predicateLower = builder.greaterThanOrEqualTo(from.get("myUpdated"), theLastUpdated.getLowerBoundAsInstant());
				lastUpdatedPredicates.add(predicateLower);
			}
			if (theLastUpdated.getUpperBoundAsInstant() != null) {
				Predicate predicateUpper = builder.lessThanOrEqualTo(from.get("myUpdated"), theLastUpdated.getUpperBoundAsInstant());
				lastUpdatedPredicates.add(predicateUpper);
			}
		}
		return lastUpdatedPredicates;
	}

	private static List<ResourcePersistentId> filterResourceIdsByLastUpdated(EntityManager theEntityManager, final DateRangeParam theLastUpdated, Collection<ResourcePersistentId> thePids) {
		if (thePids.isEmpty()) {
			return Collections.emptyList();
		}
		CriteriaBuilder builder = theEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		Root<ResourceTable> from = cq.from(ResourceTable.class);
		cq.select(from.get("myId").as(Long.class));

		List<Predicate> lastUpdatedPredicates = createLastUpdatedPredicates(theLastUpdated, builder, from);
		lastUpdatedPredicates.add(from.get("myId").as(Long.class).in(ResourcePersistentId.toLongList(thePids)));

		cq.where(SearchBuilder.toPredicateArray(lastUpdatedPredicates));
		TypedQuery<Long> query = theEntityManager.createQuery(cq);

		return ResourcePersistentId.fromLongList(query.getResultList());
	}

	private static Predicate[] toPredicateArray(List<Predicate> thePredicates) {
		return thePredicates.toArray(new Predicate[0]);
	}
}
