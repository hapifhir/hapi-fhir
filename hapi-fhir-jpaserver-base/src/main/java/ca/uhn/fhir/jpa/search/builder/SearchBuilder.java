package ca.uhn.fhir.jpa.search.builder;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.ComboSearchParamType;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchViewDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.entity.ResourceSearchView;
import ca.uhn.fhir.jpa.interceptor.JpaPreResourceAccessDetails;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.IBaseResourceEntity;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.search.SearchConstants;
import ca.uhn.fhir.jpa.search.builder.sql.GeneratedSql;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryExecutor;
import ca.uhn.fhir.jpa.search.builder.sql.SqlObjectFactory;
import ca.uhn.fhir.jpa.search.lastn.IElasticsearchSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.util.Dstu3DistanceHelper;
import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.jpa.searchparam.util.LastNParameterHelper;
import ca.uhn.fhir.jpa.util.BaseIterator;
import ca.uhn.fhir.jpa.util.CurrentThreadCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.jpa.util.SqlQueryList;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.StringUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.healthmarketscience.sqlbuilder.Condition;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.jena.sparql.engine.QueryIterator;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * The SearchBuilder is responsible for actually forming the SQL query that handles
 * searches for resources
 */
public class SearchBuilder implements ISearchBuilder {

	/**
	 * See loadResourcesByPid
	 * for an explanation of why we use the constant 800
	 */
	// NB: keep public
	@Deprecated
	public static final int MAXIMUM_PAGE_SIZE = SearchConstants.MAX_PAGE_SIZE;
	public static final int MAXIMUM_PAGE_SIZE_FOR_TESTING = 50;
	private static final Logger ourLog = LoggerFactory.getLogger(SearchBuilder.class);
	private static final ResourcePersistentId NO_MORE = new ResourcePersistentId(-1L);
	private static final String MY_TARGET_RESOURCE_PID = "myTargetResourcePid";
	private static final String MY_SOURCE_RESOURCE_PID = "mySourceResourcePid";
	private static final String MY_TARGET_RESOURCE_VERSION = "myTargetResourceVersion";
	public static final String RESOURCE_ID_ALIAS = "resource_id";
	public static final String RESOURCE_VERSION_ALIAS = "resource_version";
	public static boolean myUseMaxPageSize50ForTest = false;
	private final String myResourceName;
	private final Class<? extends IBaseResource> myResourceType;
	private final IDao myCallingDao;
	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	protected IResourceTagDao myResourceTagDao;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IResourceSearchViewDao myResourceSearchViewDao;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private IIdHelperService myIdHelperService;
	@Autowired(required = false)
	private IFulltextSearchSvc myFulltextSearchSvc;
	@Autowired(required = false)
	private IElasticsearchSvc myIElasticsearchSvc;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	private List<ResourcePersistentId> myAlsoIncludePids;
	private CriteriaBuilder myCriteriaBuilder;
	private SearchParameterMap myParams;
	private String mySearchUuid;
	private int myFetchSize;
	private Integer myMaxResultsToFetch;
	private Set<ResourcePersistentId> myPidSet;
	private boolean myHasNextIteratorQuery = false;
	private RequestPartitionId myRequestPartitionId;
	@Autowired
	private PartitionSettings myPartitionSettings;
	@Autowired
	private HapiFhirLocalContainerEntityManagerFactoryBean myEntityManagerFactory;
	@Autowired
	private SqlObjectFactory mySqlBuilderFactory;
	@Autowired
	private HibernatePropertiesProvider myDialectProvider;
	@Autowired
	private ModelConfig myModelConfig;


	/**
	 * Constructor
	 */
	public SearchBuilder(IDao theDao, String theResourceName, Class<? extends IBaseResource> theResourceType) {
		myCallingDao = theDao;
		myResourceName = theResourceName;
		myResourceType = theResourceType;
	}

	@Override
	public void setMaxResultsToFetch(Integer theMaxResultsToFetch) {
		myMaxResultsToFetch = theMaxResultsToFetch;
	}

	private void searchForIdsWithAndOr(SearchQueryBuilder theSearchSqlBuilder, QueryStack theQueryStack, @Nonnull SearchParameterMap theParams, RequestDetails theRequest) {
		myParams = theParams;

		// Remove any empty parameters
		theParams.clean();

		// For DSTU3, pull out near-distance first so when it comes time to evaluate near, we already know the distance
		if (myContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			Dstu3DistanceHelper.setNearDistance(myResourceType, theParams);
		}

		// Attempt to lookup via composite unique key.
		if (isCompositeUniqueSpCandidate()) {
			attemptComboUniqueSpProcessing(theQueryStack, theParams, theRequest);
		}

		SearchContainedModeEnum searchContainedMode = theParams.getSearchContainedMode();

		// Handle _id and _tag last, since they can typically be tacked onto a different parameter
		List<String> paramNames = myParams.keySet().stream().filter(t -> !t.equals(IAnyResource.SP_RES_ID))
			.filter(t -> !t.equals(Constants.PARAM_TAG)).collect(Collectors.toList());
		if (myParams.containsKey(IAnyResource.SP_RES_ID)) {
			paramNames.add(IAnyResource.SP_RES_ID);
		}
		if (myParams.containsKey(Constants.PARAM_TAG)) {
			paramNames.add(Constants.PARAM_TAG);
		}

		// Handle each parameter
		for (String nextParamName : paramNames) {
			if (myParams.isLastN() && LastNParameterHelper.isLastNParameter(nextParamName, myContext)) {
				// Skip parameters for Subject, Patient, Code and Category for LastN as these will be filtered by Elasticsearch
				continue;
			}
			List<List<IQueryParameterType>> andOrParams = myParams.get(nextParamName);
			Condition predicate = theQueryStack.searchForIdsWithAndOr(null, myResourceName, nextParamName, andOrParams, theRequest, myRequestPartitionId, searchContainedMode);
			if (predicate != null) {
				theSearchSqlBuilder.addPredicate(predicate);
			}
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

	@SuppressWarnings("ConstantConditions")
	@Override
	public Long createCountQuery(SearchParameterMap theParams, String theSearchUuid,
				RequestDetails theRequest, @Nonnull RequestPartitionId theRequestPartitionId) {

		assert theRequestPartitionId != null;
		assert TransactionSynchronizationManager.isActualTransactionActive();

		init(theParams, theSearchUuid, theRequestPartitionId);

		if (checkUseHibernateSearch()) {
			long count = myFulltextSearchSvc.count(myResourceName, theParams.clone());
			return count;
		}

		List<ISearchQueryExecutor> queries = createQuery(theParams.clone(), null, null, null, true, theRequest, null);
		if (queries.isEmpty()) {
			return 0L;
		} else {
			return queries.get(0).next();
		}
	}

	/**
	 * @param thePidSet May be null
	 */
	@Override
	public void setPreviouslyAddedResourcePids(@Nonnull List<ResourcePersistentId> thePidSet) {
		myPidSet = new HashSet<>(thePidSet);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public IResultIterator createQuery(SearchParameterMap theParams, SearchRuntimeDetails theSearchRuntimeDetails, RequestDetails theRequest, @Nonnull RequestPartitionId theRequestPartitionId) {
		assert theRequestPartitionId != null;
		assert TransactionSynchronizationManager.isActualTransactionActive();

		init(theParams, theSearchRuntimeDetails.getSearchUuid(), theRequestPartitionId);

		if (myPidSet == null) {
			myPidSet = new HashSet<>();
		}

		return new QueryIterator(theSearchRuntimeDetails, theRequest);
	}


	private void init(SearchParameterMap theParams, String theSearchUuid, RequestPartitionId theRequestPartitionId) {
		myCriteriaBuilder = myEntityManager.getCriteriaBuilder();
		myParams = theParams;
		mySearchUuid = theSearchUuid;
		myRequestPartitionId = theRequestPartitionId;
	}

	private List<ISearchQueryExecutor> createQuery(SearchParameterMap theParams, SortSpec sort, Integer theOffset, Integer theMaximumResults, boolean theCountOnlyFlag, RequestDetails theRequest,
																		 SearchRuntimeDetails theSearchRuntimeDetails) {

		ArrayList<ISearchQueryExecutor> queries = new ArrayList<>();

		if (checkUseHibernateSearch()) {
			// we're going to run at least part of the search against the Fulltext service.

			// Ugh - we have two different return types for now
			ISearchQueryExecutor fulltextExecutor = null;
			List<ResourcePersistentId> fulltextMatchIds = null;
			int resultCount = 0;
			if (myParams.isLastN()) {
				fulltextMatchIds = executeLastNAgainstIndex(theMaximumResults);
				resultCount = fulltextMatchIds.size();
			} else if (myParams.getEverythingMode() != null) {
				fulltextMatchIds = queryHibernateSearchForEverythingPids();
				resultCount = fulltextMatchIds.size();
			} else {
				fulltextExecutor = myFulltextSearchSvc.searchAsync(myResourceName, myParams);
			}

			if (fulltextExecutor == null) {
				fulltextExecutor = SearchQueryExecutors.from(fulltextMatchIds);
			}

			if (theSearchRuntimeDetails != null) {
				// wipmb we no longer have full size.
				theSearchRuntimeDetails.setFoundIndexMatchesCount(resultCount);
				HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(SearchRuntimeDetails.class, theSearchRuntimeDetails);
				CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_INDEXSEARCH_QUERY_COMPLETE, params);
			}

			// wipmb extract
			// can we skip the database entirely and return the pid list from here?
			boolean canSkipDatabase =
				// if we processed an AND clause, and it returned nothing, then nothing can match.
				!fulltextExecutor.hasNext() ||
					// Our hibernate search query doesn't respect partitions yet
					(!myPartitionSettings.isPartitioningEnabled() &&
					// were there AND terms left?  Then we still need the db.
						theParams.isEmpty() &&
						// not every param is a param. :-(
						theParams.getNearDistanceParam() == null &&
						theParams.getLastUpdated() == null &&
						theParams.getEverythingMode() == null &&
						theParams.getOffset() == null &&
						// or sorting?
						theParams.getSort() == null
					);

			if (canSkipDatabase) {
				if (theMaximumResults != null) {
					fulltextExecutor = SearchQueryExecutors.limited(fulltextExecutor, theMaximumResults);
				}
				queries.add(fulltextExecutor);
			} else {
				// Finish the query in the database for the rest of the search parameters, sorting, partitioning, etc.
				// We break the pids into chunks that fit in the 1k limit for jdbc bind params.
				// wipmb change chunk to take iterator
				new QueryChunker<Long>()
					.chunk(Streams.stream(fulltextExecutor).collect(Collectors.toList()), t -> doCreateChunkedQueries(theParams, t, theOffset, sort, theCountOnlyFlag, theRequest, queries));
			}
		} else {
			// do everything in the database.
			Optional<SearchQueryExecutor> query = createChunkedQuery(theParams, sort, theOffset, theMaximumResults, theCountOnlyFlag, theRequest, null);
			query.ifPresent(queries::add);
		}

		return queries;
	}

	/**
	 * Check to see if query should use Hibernate Search, and error if the query can't continue.
	 *
	 * @return true if the query should first be processed by Hibernate Search
	 * @throws InvalidRequestException if fulltext search is not enabled but the query requires it - _content or _text
	 */
	private boolean checkUseHibernateSearch() {
		boolean fulltextEnabled = (myFulltextSearchSvc != null) && !myFulltextSearchSvc.isDisabled();

		if (!fulltextEnabled) {
			failIfUsed(Constants.PARAM_TEXT);
			failIfUsed(Constants.PARAM_CONTENT);
		}

		// TODO MB someday we'll want a query planner to figure out if we _should_ or _must_ use the ft index, not just if we can.
		return fulltextEnabled && myParams != null &&
			myParams.getSearchContainedMode() == SearchContainedModeEnum.FALSE &&
			myFulltextSearchSvc.supportsSomeOf(myParams);
	}

	private void failIfUsed(String theParamName) {
		if (myParams.containsKey(theParamName)) {
			throw new InvalidRequestException(Msg.code(1192) + "Fulltext search is not enabled on this service, can not process parameter: " + theParamName);
		}
	}

	private List<ResourcePersistentId> executeLastNAgainstIndex(Integer theMaximumResults) {
		// Can we use our hibernate search generated index on resource to support lastN?:
		if (myDaoConfig.isAdvancedLuceneIndexing()) {
			if (myFulltextSearchSvc == null) {
				throw new InvalidRequestException(Msg.code(2027) + "LastN operation is not enabled on this service, can not process this request");
			}
			return myFulltextSearchSvc.lastN(myParams, theMaximumResults)
				.stream().map(lastNResourceId -> myIdHelperService.resolveResourcePersistentIds(myRequestPartitionId, myResourceName, String.valueOf(lastNResourceId)))
				.collect(Collectors.toList());
		} else {
			if (myIElasticsearchSvc == null) {
				throw new InvalidRequestException(Msg.code(2033) + "LastN operation is not enabled on this service, can not process this request");
			}
			// use the dedicated observation ES/Lucene index to support lastN query
			return myIElasticsearchSvc.executeLastN(myParams, myContext, theMaximumResults).stream()
				.map(lastnResourceId -> myIdHelperService.resolveResourcePersistentIds(myRequestPartitionId, myResourceName, lastnResourceId))
				.collect(Collectors.toList());
		}
	}

	private List<ResourcePersistentId> queryHibernateSearchForEverythingPids() {
		ResourcePersistentId pid = null;
		if (myParams.get(IAnyResource.SP_RES_ID) != null) {
			String idParamValue;
			IQueryParameterType idParam = myParams.get(IAnyResource.SP_RES_ID).get(0).get(0);
			if (idParam instanceof TokenParam) {
				TokenParam idParm = (TokenParam) idParam;
				idParamValue = idParm.getValue();
			} else {
				StringParam idParm = (StringParam) idParam;
				idParamValue = idParm.getValue();
			}

			pid = myIdHelperService.resolveResourcePersistentIds(myRequestPartitionId, myResourceName, idParamValue);
		}
		List<ResourcePersistentId> pids = myFulltextSearchSvc.everything(myResourceName, myParams, pid);
		return pids;
	}

	private void doCreateChunkedQueries(SearchParameterMap theParams, List<Long> thePids, Integer theOffset, SortSpec sort, boolean theCount, RequestDetails theRequest, ArrayList<ISearchQueryExecutor> theQueries) {
		if (thePids.size() < getMaximumPageSize()) {
			normalizeIdListForLastNInClause(thePids);
		}
		Optional<SearchQueryExecutor> query = createChunkedQuery(theParams, sort, theOffset, thePids.size(), theCount, theRequest, thePids);
		query.ifPresent(t -> theQueries.add(t));
	}

	/**
	 * Combs through the params for any _id parameters and extracts the PIDs for them
	 *
	 * @param theTargetPids
	 */
	private void extractTargetPidsFromIdParams(HashSet<Long> theTargetPids) {
		// get all the IQueryParameterType objects
		// for _id -> these should all be StringParam values
		HashSet<String> ids = new HashSet<>();
		List<List<IQueryParameterType>> params = myParams.get(IAnyResource.SP_RES_ID);
		for (List<IQueryParameterType> paramList : params) {
			for (IQueryParameterType param : paramList) {
				if (param instanceof StringParam) {
					// we expect all _id values to be StringParams
					ids.add(((StringParam) param).getValue());
				} else if (param instanceof TokenParam) {
					ids.add(((TokenParam) param).getValue());
				} else {
					// we do not expect the _id parameter to be a non-string value
					throw new IllegalArgumentException(Msg.code(1193) + "_id parameter must be a StringParam or TokenParam");
				}
			}
		}

		// fetch our target Pids
		// this will throw if an id is not found
		Map<String, ResourcePersistentId> idToPid = myIdHelperService.resolveResourcePersistentIds(myRequestPartitionId,
			myResourceName,
			new ArrayList<>(ids));
		if (myAlsoIncludePids == null) {
			myAlsoIncludePids = new ArrayList<>();
		}

		// add the pids to targetPids
		for (ResourcePersistentId pid : idToPid.values()) {
			myAlsoIncludePids.add(pid);
			theTargetPids.add(pid.getIdAsLong());
		}
	}

	private Optional<SearchQueryExecutor> createChunkedQuery(SearchParameterMap theParams, SortSpec sort, Integer theOffset, Integer theMaximumResults, boolean theCountOnlyFlag, RequestDetails theRequest, List<Long> thePidList) {
		String sqlBuilderResourceName = myParams.getEverythingMode() == null ? myResourceName : null;
		SearchQueryBuilder sqlBuilder = new SearchQueryBuilder(myContext, myDaoConfig.getModelConfig(), myPartitionSettings, myRequestPartitionId, sqlBuilderResourceName, mySqlBuilderFactory, myDialectProvider, theCountOnlyFlag);
		QueryStack queryStack3 = new QueryStack(theParams, myDaoConfig, myDaoConfig.getModelConfig(), myContext, sqlBuilder, mySearchParamRegistry, myPartitionSettings);

		if (theParams.keySet().size() > 1 || theParams.getSort() != null || theParams.keySet().contains(Constants.PARAM_HAS) || isPotentiallyContainedReferenceParameterExistsAtRoot(theParams)) {
			List<RuntimeSearchParam> activeComboParams = mySearchParamRegistry.getActiveComboSearchParams(myResourceName, theParams.keySet());
			if (activeComboParams.isEmpty()) {
				sqlBuilder.setNeedResourceTableRoot(true);
			}
		}

		JdbcTemplate jdbcTemplate = new JdbcTemplate(myEntityManagerFactory.getDataSource());
		jdbcTemplate.setFetchSize(myFetchSize);
		if (theMaximumResults != null) {
			jdbcTemplate.setMaxRows(theMaximumResults);
		}

		if (myParams.getEverythingMode() != null) {
			HashSet<Long> targetPids = new HashSet<>();
			if (myParams.get(IAnyResource.SP_RES_ID) != null) {
				extractTargetPidsFromIdParams(targetPids);
			} else {
				// For Everything queries, we make the query root by the ResourceLink table, since this query
				// is basically a reverse-include search. For type/Everything (as opposed to instance/Everything)
				// the one problem with this approach is that it doesn't catch Patients that have absolutely
				// nothing linked to them. So we do one additional query to make sure we catch those too.
				SearchQueryBuilder fetchPidsSqlBuilder = new SearchQueryBuilder(myContext, myDaoConfig.getModelConfig(), myPartitionSettings, myRequestPartitionId, myResourceName, mySqlBuilderFactory, myDialectProvider, theCountOnlyFlag);
				GeneratedSql allTargetsSql = fetchPidsSqlBuilder.generate(theOffset, myMaxResultsToFetch);
				String sql = allTargetsSql.getSql();
				Object[] args = allTargetsSql.getBindVariables().toArray(new Object[0]);
				List<Long> output = jdbcTemplate.query(sql, args, new SingleColumnRowMapper<>(Long.class));
				if (myAlsoIncludePids == null) {
					myAlsoIncludePids = new ArrayList<>(output.size());
				}
				myAlsoIncludePids.addAll(ResourcePersistentId.fromLongList(output));

			}

			queryStack3.addPredicateEverythingOperation(myResourceName, targetPids.toArray(new Long[0]));
		} else {
			/*
			 * If we're doing a filter, always use the resource table as the root - This avoids the possibility of
			 * specific filters with ORs as their root from working around the natural resource type / deletion
			 * status / partition IDs built into queries.
			 */
			if (theParams.containsKey(Constants.PARAM_FILTER)) {
				Condition partitionIdPredicate = sqlBuilder.getOrCreateResourceTablePredicateBuilder().createPartitionIdPredicate(myRequestPartitionId);
				if (partitionIdPredicate != null) {
					sqlBuilder.addPredicate(partitionIdPredicate);
				}
			}

			// Normal search
			searchForIdsWithAndOr(sqlBuilder, queryStack3, myParams, theRequest);
		}

		// If we haven't added any predicates yet, we're doing a search for all resources. Make sure we add the
		// partition ID predicate in that case.
		if (!sqlBuilder.haveAtLeastOnePredicate()) {
			Condition partitionIdPredicate = sqlBuilder.getOrCreateResourceTablePredicateBuilder().createPartitionIdPredicate(myRequestPartitionId);
			if (partitionIdPredicate != null) {
				sqlBuilder.addPredicate(partitionIdPredicate);
			}
		}

		// Add PID list predicate for full text search and/or lastn operation
		if (thePidList != null && thePidList.size() > 0) {
			sqlBuilder.addResourceIdsPredicate(thePidList);
		}

		// Last updated
		DateRangeParam lu = myParams.getLastUpdated();
		if (lu != null && !lu.isEmpty()) {
			Condition lastUpdatedPredicates = sqlBuilder.addPredicateLastUpdated(lu);
			sqlBuilder.addPredicate(lastUpdatedPredicates);
		}

		/*
		 * Exclude the pids already in the previous iterator. This is an optimization, as opposed
		 * to something needed to guarantee correct results.
		 *
		 * Why do we need it? Suppose for example, a query like:
		 *    Observation?category=foo,bar,baz
		 * And suppose you have many resources that have all 3 of these category codes. In this case
		 * the SQL query will probably return the same PIDs multiple times, and if this happens enough
		 * we may exhaust the query results without getting enough distinct results back. When that
		 * happens we re-run the query with a larger limit. Excluding results we already know about
		 * tries to ensure that we get new unique results.
		 *
		 * The challenge with that though is that lots of DBs have an issue with too many
		 * parameters in one query. So we only do this optimization if there aren't too
		 * many results.
		 */
		if (myHasNextIteratorQuery) {
			if (myPidSet.size() + sqlBuilder.countBindVariables() < 900) {
				sqlBuilder.excludeResourceIdsPredicate(myPidSet);
			}
		}

		/*
		 * Sort
		 *
		 * If we have a sort, we wrap the criteria search (the search that actually
		 * finds the appropriate resources) in an outer search which is then sorted
		 */
		if (sort != null) {
			assert !theCountOnlyFlag;

			createSort(queryStack3, sort);
		}

		/*
		 * Now perform the search
		 */
		GeneratedSql generatedSql = sqlBuilder.generate(theOffset, myMaxResultsToFetch);
		if (generatedSql.isMatchNothing()) {
			return Optional.empty();
		}

		SearchQueryExecutor executor = mySqlBuilderFactory.newSearchQueryExecutor(generatedSql, myMaxResultsToFetch);
		return Optional.of(executor);
	}

	private boolean isPotentiallyContainedReferenceParameterExistsAtRoot(SearchParameterMap theParams) {
		return myModelConfig.isIndexOnContainedResources() && theParams.values().stream()
			.flatMap(Collection::stream)
			.flatMap(Collection::stream)
			.anyMatch(t -> t instanceof ReferenceParam);
	}

	private List<Long> normalizeIdListForLastNInClause(List<Long> lastnResourceIds) {
		/*
			The following is a workaround to a known issue involving Hibernate. If queries are used with "in" clauses with large and varying
			numbers of parameters, this can overwhelm Hibernate's QueryPlanCache and deplete heap space. See the following link for more info:
			https://stackoverflow.com/questions/31557076/spring-hibernate-query-plan-cache-memory-usage.

			Normalizing the number of parameters in the "in" clause stabilizes the size of the QueryPlanCache, so long as the number of
			arguments never exceeds the maximum specified below.
		 */
		int listSize = lastnResourceIds.size();

		if (listSize > 1 && listSize < 10) {
			padIdListWithPlaceholders(lastnResourceIds, 10);
		} else if (listSize > 10 && listSize < 50) {
			padIdListWithPlaceholders(lastnResourceIds, 50);
		} else if (listSize > 50 && listSize < 100) {
			padIdListWithPlaceholders(lastnResourceIds, 100);
		} else if (listSize > 100 && listSize < 200) {
			padIdListWithPlaceholders(lastnResourceIds, 200);
		} else if (listSize > 200 && listSize < 500) {
			padIdListWithPlaceholders(lastnResourceIds, 500);
		} else if (listSize > 500 && listSize < 800) {
			padIdListWithPlaceholders(lastnResourceIds, 800);
		}

		return lastnResourceIds;
	}

	private void padIdListWithPlaceholders(List<Long> theIdList, int preferredListSize) {
		while (theIdList.size() < preferredListSize) {
			theIdList.add(-1L);
		}
	}

	private void createSort(QueryStack theQueryStack, SortSpec theSort) {
		if (theSort == null || isBlank(theSort.getParamName())) {
			return;
		}

		boolean ascending = (theSort.getOrder() == null) || (theSort.getOrder() == SortOrderEnum.ASC);

		if (IAnyResource.SP_RES_ID.equals(theSort.getParamName())) {

			theQueryStack.addSortOnResourceId(ascending);

		} else if (Constants.PARAM_LASTUPDATED.equals(theSort.getParamName())) {

			theQueryStack.addSortOnLastUpdated(ascending);

		} else {

			RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam(myResourceName, theSort.getParamName());
			if (param == null) {
				String msg = myContext.getLocalizer().getMessageSanitized(BaseStorageDao.class, "invalidSortParameter", theSort.getParamName(), getResourceName(), mySearchParamRegistry.getValidSearchParameterNamesIncludingMeta(getResourceName()));
				throw new InvalidRequestException(Msg.code(1194) + msg);
			}

			switch (param.getParamType()) {
				case STRING:
					theQueryStack.addSortOnString(myResourceName, theSort.getParamName(), ascending);
					break;
				case DATE:
					theQueryStack.addSortOnDate(myResourceName, theSort.getParamName(), ascending);
					break;
				case REFERENCE:
					theQueryStack.addSortOnResourceLink(myResourceName, theSort.getParamName(), ascending);
					break;
				case TOKEN:
					theQueryStack.addSortOnToken(myResourceName, theSort.getParamName(), ascending);
					break;
				case NUMBER:
					theQueryStack.addSortOnNumber(myResourceName, theSort.getParamName(), ascending);
					break;
				case URI:
					theQueryStack.addSortOnUri(myResourceName, theSort.getParamName(), ascending);
					break;
				case QUANTITY:
					theQueryStack.addSortOnQuantity(myResourceName, theSort.getParamName(), ascending);
					break;
				case COMPOSITE:
					List<RuntimeSearchParam> compositeList = JpaParamUtil.resolveComponentParameters(mySearchParamRegistry, param);
					if (compositeList == null) {
						throw new InvalidRequestException(Msg.code(1195) + "The composite _sort parameter " + theSort.getParamName() + " is not defined by the resource " + myResourceName);
					}
					if (compositeList.size() != 2) {
						throw new InvalidRequestException(Msg.code(1196) + "The composite _sort parameter " + theSort.getParamName()
							+ " must have 2 composite types declared in parameter annotation, found "
							+ compositeList.size());
					}
					RuntimeSearchParam left = compositeList.get(0);
					RuntimeSearchParam right = compositeList.get(1);

					createCompositeSort(theQueryStack, myResourceName, left.getParamType(), left.getName(), ascending);
					createCompositeSort(theQueryStack, myResourceName, right.getParamType(), right.getName(), ascending);

					break;
				case SPECIAL:
				case HAS:
				default:
					throw new InvalidRequestException(Msg.code(1197) + "This server does not support _sort specifications of type " + param.getParamType() + " - Can't serve _sort=" + theSort.getParamName());
			}

		}

		// Recurse
		createSort(theQueryStack, theSort.getChain());

	}

	private void createCompositeSort(QueryStack theQueryStack, String theResourceName, RestSearchParameterTypeEnum theParamType, String theParamName, boolean theAscending) {

		switch (theParamType) {
			case STRING:
				theQueryStack.addSortOnString(myResourceName, theParamName, theAscending);
				break;
			case DATE:
				theQueryStack.addSortOnDate(myResourceName, theParamName, theAscending);
				break;
			case TOKEN:
				theQueryStack.addSortOnToken(myResourceName, theParamName, theAscending);
				break;
			case QUANTITY:
				theQueryStack.addSortOnQuantity(myResourceName, theParamName, theAscending);
				break;
			case NUMBER:
			case REFERENCE:
			case COMPOSITE:
			case URI:
			case HAS:
			case SPECIAL:
			default:
				throw new InvalidRequestException(Msg.code(1198) + "Don't know how to handle composite parameter with type of " + theParamType + " on _sort=" + theParamName);
		}

	}

	private void doLoadPids(Collection<ResourcePersistentId> thePids, Collection<ResourcePersistentId> theIncludedPids, List<IBaseResource> theResourceListToPopulate, boolean theForHistoryOperation,
									Map<ResourcePersistentId, Integer> thePosition) {

		Map<Long, Long> resourcePidToVersion = null;
		for (ResourcePersistentId next : thePids) {
			if (next.getVersion() != null && myModelConfig.isRespectVersionsForSearchIncludes()) {
				if (resourcePidToVersion == null) {
					resourcePidToVersion = new HashMap<>();
				}
				resourcePidToVersion.put(next.getIdAsLong(), next.getVersion());
			}
		}

		List<Long> versionlessPids = ResourcePersistentId.toLongList(thePids);
		if (versionlessPids.size() < getMaximumPageSize()) {
			versionlessPids = normalizeIdListForLastNInClause(versionlessPids);
		}

		// -- get the resource from the searchView
		Collection<ResourceSearchView> resourceSearchViewList = myResourceSearchViewDao.findByResourceIds(versionlessPids);

		//-- preload all tags with tag definition if any
		Map<Long, Collection<ResourceTag>> tagMap = getResourceTagMap(resourceSearchViewList);

		for (IBaseResourceEntity next : resourceSearchViewList) {
			if (next.getDeleted() != null) {
				continue;
			}

			Class<? extends IBaseResource> resourceType = myContext.getResourceDefinition(next.getResourceType()).getImplementingClass();

			ResourcePersistentId resourceId = new ResourcePersistentId(next.getResourceId());

			/*
			 * If a specific version is requested via an include, we'll replace the current version
			 * with the specific desired version. This is not the most efficient thing, given that
			 * we're loading the current version and then turning around and throwing it away again.
			 * This could be optimized and probably should be, but it's not critical given that
			 * this only applies to includes, which don't tend to be massive in numbers.
			 */
			if (resourcePidToVersion != null) {
				Long version = resourcePidToVersion.get(next.getResourceId());
				resourceId.setVersion(version);
				if (version != null && !version.equals(next.getVersion())) {
					IFhirResourceDao<? extends IBaseResource> dao = myDaoRegistry.getResourceDao(resourceType);
					next = dao.readEntity(next.getIdDt().withVersion(Long.toString(version)), null);
				}
			}

			IBaseResource resource = null;
			if (next != null) {
				resource = myCallingDao.toResource(resourceType, next, tagMap.get(next.getId()), theForHistoryOperation);
			}
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

	private Map<Long, Collection<ResourceTag>> getResourceTagMap(Collection<? extends IBaseResourceEntity> theResourceSearchViewList) {

		List<Long> idList = new ArrayList<>(theResourceSearchViewList.size());

		//-- find all resource has tags
		for (IBaseResourceEntity resource : theResourceSearchViewList) {
			if (resource.isHasTags())
				idList.add(resource.getId());
		}

		return getPidToTagMap(idList);
	}

	@Nonnull
	private Map<Long, Collection<ResourceTag>> getPidToTagMap(List<Long> thePidList) {
		Map<Long, Collection<ResourceTag>> tagMap = new HashMap<>();

		//-- no tags
		if (thePidList.size() == 0)
			return tagMap;

		//-- get all tags for the idList
		Collection<ResourceTag> tagList = myResourceTagDao.findByResourceIds(thePidList);

		//-- build the map, key = resourceId, value = list of ResourceTag
		ResourcePersistentId resourceId;
		Collection<ResourceTag> tagCol;
		for (ResourceTag tag : tagList) {

			resourceId = new ResourcePersistentId(tag.getResourceId());
			tagCol = tagMap.get(resourceId.getIdAsLong());
			if (tagCol == null) {
				tagCol = new ArrayList<>();
				tagCol.add(tag);
				tagMap.put(resourceId.getIdAsLong(), tagCol);
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

		// Can we fast track this loading by checking elastic search?
		if (isLoadingFromElasticSearchSupported(thePids)) {
			theResourceListToPopulate.addAll(loadResourcesFromElasticSearch(thePids));
		} else {
			// We only chunk because some jdbc drivers can't handle long param lists.
			new QueryChunker<ResourcePersistentId>().chunk(thePids, t -> doLoadPids(t, theIncludedPids, theResourceListToPopulate, theForHistoryOperation, position));
		}
	}

	/**
	 * Check if we can load the resources from Hibernate Search instead of the database.
	 * We assume this is faster.
	 *
	 * Hibernate Search only stores the current version, and only if enabled.
	 * @param thePids the pids to check for versioned references
	 * @return can we fetch from Hibernate Search?
	 */
	private boolean isLoadingFromElasticSearchSupported(Collection<ResourcePersistentId> thePids) {

		// is storage enabled?
		return myDaoConfig.isStoreResourceInLuceneIndex() &&
			// we don't support history
			thePids.stream().noneMatch(p->p.getVersion()!=null) &&
			// skip the complexity for metadata in dstu2
			myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3);
	}

	private List<IBaseResource> loadResourcesFromElasticSearch(Collection<ResourcePersistentId> thePids) {
		// Do we use the fulltextsvc via hibernate-search to load resources or be backwards compatible with older ES only impl
		// to handle lastN?
		if (myDaoConfig.isAdvancedLuceneIndexing() && myDaoConfig.isStoreResourceInLuceneIndex()) {
			List<Long> pidList = thePids.stream().map(ResourcePersistentId::getIdAsLong).collect(Collectors.toList());

			// wipmb standardize on ResourcePersistentId
			List<IBaseResource> resources = myFulltextSearchSvc.getResources(pidList);
			return resources;
		} else if (!Objects.isNull(myParams) && myParams.isLastN()) {
			// legacy LastN implementation
			return myIElasticsearchSvc.getObservationResources(thePids);
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * THIS SHOULD RETURN HASHSET and not just Set because we add to it later
	 * so it can't be Collections.emptySet() or some such thing
	 */
	@Override
	public Set<ResourcePersistentId> loadIncludes(FhirContext theContext, EntityManager theEntityManager, Collection<ResourcePersistentId> theMatches, Set<Include> theIncludes,
																 boolean theReverseMode, DateRangeParam theLastUpdated, String theSearchIdOrDescription, RequestDetails theRequest, Integer theMaxCount) {
		if (theMatches.size() == 0) {
			return new HashSet<>();
		}
		if (theIncludes == null || theIncludes.isEmpty()) {
			return new HashSet<>();
		}
		String searchPidFieldName = theReverseMode ? MY_TARGET_RESOURCE_PID : MY_SOURCE_RESOURCE_PID;
		String findPidFieldName = theReverseMode ? MY_SOURCE_RESOURCE_PID : MY_TARGET_RESOURCE_PID;
		String findVersionFieldName = null;
		if (!theReverseMode && myModelConfig.isRespectVersionsForSearchIncludes()) {
			findVersionFieldName = MY_TARGET_RESOURCE_VERSION;
		}

		List<ResourcePersistentId> nextRoundMatches = new ArrayList<>(theMatches);
		HashSet<ResourcePersistentId> allAdded = new HashSet<>();
		HashSet<ResourcePersistentId> original = new HashSet<>(theMatches);
		ArrayList<Include> includes = new ArrayList<>(theIncludes);

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

				// Account for _include=*
				boolean matchAll = "*".equals(nextInclude.getValue());

				// Account for _include=[resourceType]:*
				String wantResourceType = null;
				if (!matchAll) {
					if ("*".equals(nextInclude.getParamName())) {
						wantResourceType = nextInclude.getParamType();
						matchAll = true;
					}
				}

				if (matchAll) {
					StringBuilder sqlBuilder = new StringBuilder();
					sqlBuilder.append("SELECT r.").append(findPidFieldName);
					if (findVersionFieldName != null) {
						sqlBuilder.append(", r." + findVersionFieldName);
					}
					sqlBuilder.append(" FROM ResourceLink r WHERE ");

					sqlBuilder.append("r.");
					sqlBuilder.append(searchPidFieldName);
					sqlBuilder.append(" IN (:target_pids)");

					// Technically if the request is a qualified star (e.g. _include=Observation:*) we
					// should always be checking the source resource type on the resource link. We don't
					// actually index that column though by default, so in order to try and be efficient
					// we don't actually include it for includes (but we do for revincludes). This is
					// because for an include it doesn't really make sense to include a different
					// resource type than the one you are searching on.
					if (wantResourceType != null && theReverseMode) {
						sqlBuilder.append(" AND r.mySourceResourceType = :want_resource_type");
					} else {
						wantResourceType = null;
					}

					String sql = sqlBuilder.toString();
					List<Collection<ResourcePersistentId>> partitions = partition(nextRoundMatches, getMaximumPageSize());
					for (Collection<ResourcePersistentId> nextPartition : partitions) {
						TypedQuery<?> q = theEntityManager.createQuery(sql, Object[].class);
						q.setParameter("target_pids", ResourcePersistentId.toLongList(nextPartition));
						if (wantResourceType != null) {
							q.setParameter("want_resource_type", wantResourceType);
						}
						if (theMaxCount != null) {
							q.setMaxResults(theMaxCount);
						}
						List<?> results = q.getResultList();
						for (Object nextRow : results) {
							if (nextRow == null) {
								// This can happen if there are outgoing references which are canonical or point to
								// other servers
								continue;
							}

							Long resourceLink;
							Long version = null;
							if (findVersionFieldName != null) {
								resourceLink = (Long) ((Object[]) nextRow)[0];
								version = (Long) ((Object[]) nextRow)[1];
							} else {
								resourceLink = (Long) nextRow;
							}

							pidsToInclude.add(new ResourcePersistentId(resourceLink, version));
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
						param = mySearchParamRegistry.getActiveSearchParam(resType, paramName);
					} else {
						param = null;
					}
					if (param == null) {
						ourLog.warn("Unknown param name in include/revinclude=" + nextInclude.getValue());
						continue;
					}

					paths = param.getPathsSplitForResourceType(resType);

					String targetResourceType = defaultString(nextInclude.getParamTargetType(), null);
					for (String nextPath : paths) {
						boolean haveTargetTypesDefinedByParam = param.hasTargets();
						String findPidFieldSqlColumn = findPidFieldName.equals(MY_SOURCE_RESOURCE_PID) ? "src_resource_id" : "target_resource_id";
						String fieldsToLoad = "r." + findPidFieldSqlColumn + " AS " + RESOURCE_ID_ALIAS;
						if (findVersionFieldName != null) {
							fieldsToLoad += ", r.target_resource_version AS " + RESOURCE_VERSION_ALIAS;
						}

						// Query for includes lookup has consider 2 cases
						// Case 1: Where target_resource_id is available in hfj_res_link table for local references
						// Case 2: Where target_resource_id is null in hfj_res_link table and referred by a canonical url in target_resource_url

						// Case 1:
						String searchPidFieldSqlColumn = searchPidFieldName.equals(MY_TARGET_RESOURCE_PID) ? "target_resource_id" : "src_resource_id";
						StringBuilder resourceIdBasedQuery = new StringBuilder("SELECT " + fieldsToLoad +
							" FROM hfj_res_link r " +
							" WHERE r.src_path = :src_path AND " +
							" r.target_resource_id IS NOT NULL AND " +
							" r." + searchPidFieldSqlColumn + " IN (:target_pids) ");
						if(targetResourceType != null) {
							resourceIdBasedQuery.append(" AND r.target_resource_type = :target_resource_type ");
						} else if(haveTargetTypesDefinedByParam) {
							resourceIdBasedQuery.append(" AND r.target_resource_type in (:target_resource_types) ");
						}

						// Case 2:
						String fieldsToLoadFromSpidxUriTable = "rUri.res_id";
						// to match the fields loaded in union
						if(fieldsToLoad.split(",").length > 1) {
							for (int i = 0; i < fieldsToLoad.split(",").length - 1; i++) {
								fieldsToLoadFromSpidxUriTable += ", NULL";
							}
						}
						//@formatter:off
						String resourceUrlBasedQuery = "SELECT " + fieldsToLoadFromSpidxUriTable +
							" FROM hfj_res_link r " +
							" JOIN hfj_spidx_uri rUri ON ( " +
							"   r.target_resource_url = rUri.sp_uri AND " +
							"   rUri.sp_name = 'url' " +
							    (targetResourceType != null ? " AND rUri.res_type = :target_resource_type " : "") +
							    (haveTargetTypesDefinedByParam ? " AND rUri.res_type IN (:target_resource_types) " : "") +
							" ) " +
							" WHERE r.src_path = :src_path AND " +
							" r.target_resource_id IS NULL AND " +
							" r." + searchPidFieldSqlColumn + " IN (:target_pids) ";
						//@formatter:on

						String sql = resourceIdBasedQuery + " UNION " + resourceUrlBasedQuery;

						List<Collection<ResourcePersistentId>> partitions = partition(nextRoundMatches, getMaximumPageSize());
						for (Collection<ResourcePersistentId> nextPartition : partitions) {
							Query q = theEntityManager.createNativeQuery(sql, Tuple.class);
							q.setParameter("src_path", nextPath);
							q.setParameter("target_pids", ResourcePersistentId.toLongList(nextPartition));
							if (targetResourceType != null) {
								q.setParameter("target_resource_type", targetResourceType);
							} else if (haveTargetTypesDefinedByParam) {
								q.setParameter("target_resource_types", param.getTargets());
							}
							List<Tuple> results = q.getResultList();
							if (theMaxCount != null) {
								q.setMaxResults(theMaxCount);
							}
							for (Tuple result : results) {
								if (result != null) {
									Long resourceId = NumberUtils.createLong(String.valueOf(result.get(RESOURCE_ID_ALIAS)));
									Long resourceVersion = null;
									if (findVersionFieldName != null && result.get(RESOURCE_VERSION_ALIAS) != null) {
										resourceVersion = NumberUtils.createLong(String.valueOf(result.get(RESOURCE_VERSION_ALIAS)));
									}
									pidsToInclude.add(new ResourcePersistentId(resourceId, resourceVersion));
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

			nextRoundMatches.clear();
			for (ResourcePersistentId next : pidsToInclude) {
				if (original.contains(next) == false && allAdded.contains(next) == false) {
					nextRoundMatches.add(next);
				}
			}

			addedSomeThisRound = allAdded.addAll(pidsToInclude);

			if (theMaxCount != null && allAdded.size() >= theMaxCount) {
				break;
			}

		} while (includes.size() > 0 && nextRoundMatches.size() > 0 && addedSomeThisRound);

		allAdded.removeAll(original);

		ourLog.info("Loaded {} {} in {} rounds and {} ms for search {}", allAdded.size(), theReverseMode ? "_revincludes" : "_includes", roundCounts, w.getMillisAndRestart(), theSearchIdOrDescription);

		// Interceptor call: STORAGE_PREACCESS_RESOURCES
		// This can be used to remove results from the search result details before
		// the user has a chance to know that they were in the results
		if (allAdded.size() > 0) {

			if (CompositeInterceptorBroadcaster.hasHooks(Pointcut.STORAGE_PREACCESS_RESOURCES, myInterceptorBroadcaster, theRequest)) {
				List<ResourcePersistentId> includedPidList = new ArrayList<>(allAdded);
				JpaPreResourceAccessDetails accessDetails = new JpaPreResourceAccessDetails(includedPidList, () -> this);
				HookParams params = new HookParams()
					.add(IPreResourceAccessDetails.class, accessDetails)
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest);
				CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);

				for (int i = includedPidList.size() - 1; i >= 0; i--) {
					if (accessDetails.isDontReturnResourceAtIndex(i)) {
						ResourcePersistentId value = includedPidList.remove(i);
						if (value != null) {
							allAdded.remove(value);
						}
					}
				}
			}
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

	private void attemptComboUniqueSpProcessing(QueryStack theQueryStack3, @Nonnull SearchParameterMap theParams, RequestDetails theRequest) {
		RuntimeSearchParam comboParam = null;
		List<String> comboParamNames = null;
		List<RuntimeSearchParam> exactMatchParams = mySearchParamRegistry.getActiveComboSearchParams(myResourceName, theParams.keySet());
		if (exactMatchParams.size() > 0) {
			comboParam = exactMatchParams.get(0);
			comboParamNames = new ArrayList<>(theParams.keySet());
		}

		if (comboParam == null) {
			List<RuntimeSearchParam> candidateComboParams = mySearchParamRegistry.getActiveComboSearchParams(myResourceName);
			for (RuntimeSearchParam nextCandidate : candidateComboParams) {
				List<String> nextCandidateParamNames = JpaParamUtil
					.resolveComponentParameters(mySearchParamRegistry, nextCandidate)
					.stream()
					.map(t -> t.getName())
					.collect(Collectors.toList());
				if (theParams.keySet().containsAll(nextCandidateParamNames)) {
					comboParam = nextCandidate;
					comboParamNames = nextCandidateParamNames;
					break;
				}
			}
		}

		if (comboParam != null) {
			// Since we're going to remove elements below
			theParams.values().forEach(nextAndList -> ensureSubListsAreWritable(nextAndList));

			StringBuilder sb = new StringBuilder();
			sb.append(myResourceName);
			sb.append("?");

			boolean first = true;

			Collections.sort(comboParamNames);
			for (String nextParamName : comboParamNames) {
				List<List<IQueryParameterType>> nextValues = theParams.get(nextParamName);

				// TODO Hack to fix weird IOOB on the next stanza until James comes back and makes sense of this.
				if (nextValues.isEmpty()) {
					ourLog.error("query parameter {} is unexpectedly empty. Encountered while considering {} index for {}", nextParamName, comboParam.getName(), theRequest.getCompleteUrl());
					sb = null;
					break;
				}

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

				if (comboParam.getComboSearchParamType() == ComboSearchParamType.NON_UNIQUE) {
					if (nextParamDef.getParamType() == RestSearchParameterTypeEnum.STRING) {
						nextOrValue = StringUtil.normalizeStringForSearchIndexing(nextOrValue);
					}
				}

				if (first) {
					first = false;
				} else {
					sb.append('&');
				}

				nextParamName = UrlUtil.escapeUrlParam(nextParamName);
				nextOrValue = UrlUtil.escapeUrlParam(nextOrValue);

				sb.append(nextParamName).append('=').append(nextOrValue);

			}

			if (sb != null) {
				String indexString = sb.toString();
				ourLog.debug("Checking for {} combo index for query: {}", comboParam.getComboSearchParamType(), indexString);

				// Interceptor broadcast: JPA_PERFTRACE_INFO
				StorageProcessingMessage msg = new StorageProcessingMessage()
					.setMessage("Using " + comboParam.getComboSearchParamType() + " index for query for search: " + indexString);
				HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(StorageProcessingMessage.class, msg);
				CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_INFO, params);

				switch (comboParam.getComboSearchParamType()) {
					case UNIQUE:
						theQueryStack3.addPredicateCompositeUnique(indexString, myRequestPartitionId);
						break;
					case NON_UNIQUE:
						theQueryStack3.addPredicateCompositeNonUnique(indexString, myRequestPartitionId);
						break;
				}

				// Remove any empty parameters remaining after this
				theParams.clean();
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

	@Override
	public void setFetchSize(int theFetchSize) {
		myFetchSize = theFetchSize;
	}

	public SearchParameterMap getParams() {
		return myParams;
	}

	public CriteriaBuilder getBuilder() {
		return myCriteriaBuilder;
	}

	public Class<? extends IBaseResource> getResourceType() {
		return myResourceType;
	}

	public String getResourceName() {
		return myResourceName;
	}

	@VisibleForTesting
	public void setDaoConfigForUnitTest(DaoConfig theDaoConfig) {
		myDaoConfig = theDaoConfig;
	}

	public class IncludesIterator extends BaseIterator<ResourcePersistentId> implements Iterator<ResourcePersistentId> {

		private final RequestDetails myRequest;
		private final Set<ResourcePersistentId> myCurrentPids;
		private Iterator<ResourcePersistentId> myCurrentIterator;
		private ResourcePersistentId myNext;

		IncludesIterator(Set<ResourcePersistentId> thePidSet, RequestDetails theRequest) {
			myCurrentPids = new HashSet<>(thePidSet);
			myCurrentIterator = null;
			myRequest = theRequest;
		}

		private void fetchNext() {
			while (myNext == null) {

				if (myCurrentIterator == null) {
					Set<Include> includes = Collections.singleton(new Include("*", true));
					Set<ResourcePersistentId> newPids = loadIncludes(myContext, myEntityManager, myCurrentPids, includes, false, getParams().getLastUpdated(), mySearchUuid, myRequest, null);
					myCurrentIterator = newPids.iterator();
				}

				if (myCurrentIterator.hasNext()) {
					myNext = myCurrentIterator.next();
				} else {
					myNext = NO_MORE;
				}

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
		private final SortSpec mySort;
		private final Integer myOffset;
		private boolean myFirst = true;
		private IncludesIterator myIncludesIterator;
		private ResourcePersistentId myNext;
		private ISearchQueryExecutor myResultsIterator;
		private boolean myStillNeedToFetchIncludes;
		private int mySkipCount = 0;
		private int myNonSkipCount = 0;
		private List<ISearchQueryExecutor> myQueryList = new ArrayList<>();

		private QueryIterator(SearchRuntimeDetails theSearchRuntimeDetails, RequestDetails theRequest) {
			mySearchRuntimeDetails = theSearchRuntimeDetails;
			mySort = myParams.getSort();
			myOffset = myParams.getOffset();
			myRequest = theRequest;

			// Includes are processed inline for $everything query
			if (myParams.getEverythingMode() != null) {
				myStillNeedToFetchIncludes = true;
			}

			myHavePerfTraceFoundIdHook = CompositeInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_SEARCH_FOUND_ID, myInterceptorBroadcaster, myRequest);
			myHaveRawSqlHooks = CompositeInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_RAW_SQL, myInterceptorBroadcaster, myRequest);

		}

		private void fetchNext() {

			try {
				if (myHaveRawSqlHooks) {
					CurrentThreadCaptureQueriesListener.startCapturing();
				}

				// If we don't have a query yet, create one
				if (myResultsIterator == null) {
					if (myMaxResultsToFetch == null) {
						if (myParams.getLoadSynchronousUpTo() != null) {
							myMaxResultsToFetch = myParams.getLoadSynchronousUpTo();
						} else if (myParams.getOffset() != null && myParams.getCount() != null) {
							myMaxResultsToFetch = myParams.getCount();
						} else {
							myMaxResultsToFetch = myDaoConfig.getFetchSizeDefaultMaximum();
						}
					}

					initializeIteratorQuery(myOffset, myMaxResultsToFetch);

					if (myAlsoIncludePids == null) {
						myAlsoIncludePids = new ArrayList<>();
					}
				}

				if (myNext == null) {


					for (Iterator<ResourcePersistentId> myPreResultsIterator = myAlsoIncludePids.iterator(); myPreResultsIterator.hasNext(); ) {
						ResourcePersistentId next = myPreResultsIterator.next();
						if (next != null)
							if (myPidSet.add(next)) {
								myNext = next;
								break;
							}
					}

					if (myNext == null) {
						while (myResultsIterator.hasNext() || !myQueryList.isEmpty()) {
							// Update iterator with next chunk if necessary.
							if (!myResultsIterator.hasNext()) {
								retrieveNextIteratorQuery();
							}

							Long nextLong = myResultsIterator.next();
							if (myHavePerfTraceFoundIdHook) {
								HookParams params = new HookParams()
									.add(Integer.class, System.identityHashCode(this))
									.add(Object.class, nextLong);
								CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_FOUND_ID, params);
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

										StorageProcessingMessage message = new StorageProcessingMessage();
										String msg = "Pass completed with no matching results seeking rows " + myPidSet.size() + "-" + mySkipCount + ". This indicates an inefficient query! Retrying with new max count of " + myMaxResultsToFetch;
										ourLog.warn(msg);
										message.setMessage(msg);
										HookParams params = new HookParams()
											.add(RequestDetails.class, myRequest)
											.addIfMatchesType(ServletRequestDetails.class, myRequest)
											.add(StorageProcessingMessage.class, message);
										CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_WARNING, params);

										myMaxResultsToFetch += 1000;
										initializeIteratorQuery(myOffset, myMaxResultsToFetch);
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
					CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_RAW_SQL, params);
				}
			}

			if (myFirst) {
				HookParams params = new HookParams()
					.add(RequestDetails.class, myRequest)
					.addIfMatchesType(ServletRequestDetails.class, myRequest)
					.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED, params);
				myFirst = false;
			}

			if (NO_MORE.equals(myNext)) {
				HookParams params = new HookParams()
					.add(RequestDetails.class, myRequest)
					.addIfMatchesType(ServletRequestDetails.class, myRequest)
					.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE, params);
			}

		}

		private void initializeIteratorQuery(Integer theOffset, Integer theMaxResultsToFetch) {
			if (myQueryList.isEmpty()) {
				// wipmb what is this?
				// Capture times for Lucene/Elasticsearch queries as well
				mySearchRuntimeDetails.setQueryStopwatch(new StopWatch());
				myQueryList = createQuery(myParams, mySort, theOffset, theMaxResultsToFetch, false, myRequest, mySearchRuntimeDetails);
			}

			mySearchRuntimeDetails.setQueryStopwatch(new StopWatch());

			retrieveNextIteratorQuery();

			mySkipCount = 0;
			myNonSkipCount = 0;
		}

		private void retrieveNextIteratorQuery() {
			close();
			if (myQueryList != null && myQueryList.size() > 0) {
				myResultsIterator = myQueryList.remove(0);
				myHasNextIteratorQuery = true;
			} else {
				myResultsIterator = SearchQueryExecutor.emptyExecutor();
				myHasNextIteratorQuery = false;
			}

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
		public Collection<ResourcePersistentId> getNextResultBatch(long theBatchSize) {
			Collection<ResourcePersistentId> batch = new ArrayList<>();
			while (this.hasNext() && batch.size() < theBatchSize) {
				batch.add(this.next());
			}
			return batch;
		}

		@Override
		public void close() {
			if (myResultsIterator != null) {
				myResultsIterator.close();
			}
			myResultsIterator = null;
		}

	}

	public static int getMaximumPageSize() {
		if (myUseMaxPageSize50ForTest) {
			return MAXIMUM_PAGE_SIZE_FOR_TESTING;
		} else {
			return MAXIMUM_PAGE_SIZE;
		}
	}

	public static void setMaxPageSize50ForTest(boolean theIsTest) {
		myUseMaxPageSize50ForTest = theIsTest;
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

	public static Predicate[] toPredicateArray(List<Predicate> thePredicates) {
		return thePredicates.toArray(new Predicate[0]);
	}

}
