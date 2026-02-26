/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder;

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
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.search.ResourceNotFoundInIndexException;
import ca.uhn.fhir.jpa.interceptor.JpaPreResourceAccessDetails;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.search.SearchBuilderLoadIncludesParameters;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.BatchResourceLoader;
import ca.uhn.fhir.jpa.search.BatchResourceLoader.ResourceLoadResult;
import ca.uhn.fhir.jpa.search.SearchConstants;
import ca.uhn.fhir.jpa.search.builder.models.ResolvedSearchQueryExecutor;
import ca.uhn.fhir.jpa.search.builder.models.SearchQueryProperties;
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
import ca.uhn.fhir.jpa.util.CartesianProductUtil;
import ca.uhn.fhir.jpa.util.CurrentThreadCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.jpa.util.ScrollableResultsIterator;
import ca.uhn.fhir.jpa.util.SqlQueryList;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.BaseParamWithPrefix;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.StringUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MultimapBuilder;
import com.healthmarketscience.sqlbuilder.Condition;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.NO_MORE;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.UNDESIRED_RESOURCE_LINKAGES_FOR_EVERYTHING_ON_PATIENT_INSTANCE;
import static ca.uhn.fhir.jpa.search.builder.QueryStack.LOCATION_POSITION;
import static ca.uhn.fhir.jpa.search.builder.QueryStack.SearchForIdsParams.with;
import static ca.uhn.fhir.jpa.util.InClauseNormalizer.normalizeIdListForInClause;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.EQUAL;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.stripStart;

/**
 * The SearchBuilder is responsible for actually forming the SQL query that handles
 * searches for resources
 */
public class SearchBuilder implements ISearchBuilder<JpaPid> {

	/**
	 * See loadResourcesByPid
	 * for an explanation of why we use the constant 800
	 */
	// NB: keep public
	@Deprecated
	public static final int MAXIMUM_PAGE_SIZE = SearchConstants.MAX_PAGE_SIZE;

	public static final String RESOURCE_ID_ALIAS = "resource_id";
	public static final String PARTITION_ID_ALIAS = "partition_id";
	public static final String RESOURCE_VERSION_ALIAS = "resource_version";
	private static final Logger ourLog = LoggerFactory.getLogger(SearchBuilder.class);

	private static final String MY_SOURCE_RESOURCE_PID = "mySourceResourcePid";
	private static final String MY_SOURCE_RESOURCE_PARTITION_ID = "myPartitionIdValue";
	private static final String MY_SOURCE_RESOURCE_TYPE = "mySourceResourceType";
	private static final String MY_TARGET_RESOURCE_PID = "myTargetResourcePid";
	private static final String MY_TARGET_RESOURCE_PARTITION_ID = "myTargetResourcePartitionId";
	private static final String MY_TARGET_RESOURCE_TYPE = "myTargetResourceType";
	private static final String MY_TARGET_RESOURCE_VERSION = "myTargetResourceVersion";
	public static final JpaPid[] EMPTY_JPA_PID_ARRAY = new JpaPid[0];
	public static Integer myMaxPageSizeForTests = null;
	protected final IInterceptorBroadcaster myInterceptorBroadcaster;
	protected final IResourceTagDao myResourceTagDao;
	private String myResourceName;
	private final Class<? extends IBaseResource> myResourceType;
	private final HapiFhirLocalContainerEntityManagerFactoryBean myEntityManagerFactory;
	private final SqlObjectFactory mySqlBuilderFactory;
	private final HibernatePropertiesProvider myDialectProvider;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final PartitionSettings myPartitionSettings;
	private final DaoRegistry myDaoRegistry;
	private final FhirContext myContext;
	private final IIdHelperService<JpaPid> myIdHelperService;
	private final JpaStorageSettings myStorageSettings;
	private final SearchQueryProperties mySearchProperties;
	private final IResourceHistoryTableDao myResourceHistoryTableDao;
	private final BatchResourceLoader myBatchResourceLoader;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	private CriteriaBuilder myCriteriaBuilder;
	private SearchParameterMap myParams;
	private String mySearchUuid;
	private int myFetchSize;

	private boolean myRequiresTotal;

	/**
	 * @see SearchBuilder#setDeduplicateInDatabase(boolean)
	 */
	private Set<JpaPid> myPidSet;

	private boolean myHasNextIteratorQuery = false;
	private RequestPartitionId myRequestPartitionId;

	private IFulltextSearchSvc myFulltextSearchSvc;

	@Autowired(required = false)
	public void setFullTextSearch(IFulltextSearchSvc theFulltextSearchSvc) {
		myFulltextSearchSvc = theFulltextSearchSvc;
	}

	@Autowired(required = false)
	private IElasticsearchSvc myIElasticsearchSvc;

	@Autowired
	private IRequestPartitionHelperSvc myPartitionHelperSvc;

	/**
	 * Constructor
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public SearchBuilder(
			String theResourceName,
			JpaStorageSettings theStorageSettings,
			HapiFhirLocalContainerEntityManagerFactoryBean theEntityManagerFactory,
			SqlObjectFactory theSqlBuilderFactory,
			HibernatePropertiesProvider theDialectProvider,
			ISearchParamRegistry theSearchParamRegistry,
			PartitionSettings thePartitionSettings,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			IResourceTagDao theResourceTagDao,
			DaoRegistry theDaoRegistry,
			FhirContext theContext,
			IIdHelperService theIdHelperService,
			IResourceHistoryTableDao theResourceHistoryTagDao,
			BatchResourceLoader theBatchResourceLoader,
			Class<? extends IBaseResource> theResourceType) {
		myResourceName = theResourceName;
		myResourceType = theResourceType;
		myStorageSettings = theStorageSettings;

		myEntityManagerFactory = theEntityManagerFactory;
		mySqlBuilderFactory = theSqlBuilderFactory;
		myDialectProvider = theDialectProvider;
		mySearchParamRegistry = theSearchParamRegistry;
		myPartitionSettings = thePartitionSettings;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		myResourceTagDao = theResourceTagDao;
		myDaoRegistry = theDaoRegistry;
		myContext = theContext;
		myIdHelperService = theIdHelperService;
		myResourceHistoryTableDao = theResourceHistoryTagDao;
		myBatchResourceLoader = theBatchResourceLoader;

		mySearchProperties = new SearchQueryProperties();
	}

	@VisibleForTesting
	void setResourceName(String theName) {
		myResourceName = theName;
	}

	@Override
	public void setMaxResultsToFetch(Integer theMaxResultsToFetch) {
		mySearchProperties.setMaxResultsRequested(theMaxResultsToFetch);
	}

	@Override
	public void setDeduplicateInDatabase(boolean theShouldDeduplicateInDB) {
		mySearchProperties.setDeduplicateInDatabase(theShouldDeduplicateInDB);
	}

	@Override
	public void setRequireTotal(boolean theRequireTotal) {
		myRequiresTotal = theRequireTotal;
	}

	@Override
	public boolean requiresTotal() {
		return myRequiresTotal;
	}

	private void searchForIdsWithAndOr(
			SearchQueryBuilder theSearchSqlBuilder,
			QueryStack theQueryStack,
			@Nonnull SearchParameterMap theParams,
			RequestDetails theRequest) {
		myParams = theParams;
		mySearchProperties.setSortSpec(myParams.getSort());

		// Remove any empty parameters
		theParams.clean();

		// For DSTU3, pull out near-distance first so when it comes time to evaluate near, we already know the distance
		if (myContext.getVersion().getVersion() == FhirVersionEnum.DSTU3) {
			Dstu3DistanceHelper.setNearDistance(myResourceType, theParams);
		}

		// Attempt to lookup via composite unique key.
		if (isComboSearchCandidate()) {
			attemptComboSearchParameterProcessing(theQueryStack, theParams, theRequest);
		}

		// Handle _id and _tag last, since they can typically be tacked onto a different parameter
		List<String> paramNames = myParams.keySet().stream()
				.filter(t -> !t.equals(IAnyResource.SP_RES_ID))
				.filter(t -> !t.equals(Constants.PARAM_TAG))
				.collect(Collectors.toList());
		if (myParams.containsKey(IAnyResource.SP_RES_ID)) {
			paramNames.add(IAnyResource.SP_RES_ID);
		}
		if (myParams.containsKey(Constants.PARAM_TAG)) {
			paramNames.add(Constants.PARAM_TAG);
		}

		// Handle each parameter
		for (String nextParamName : paramNames) {
			if (myParams.isLastN() && LastNParameterHelper.isLastNParameter(nextParamName, myContext)) {
				// Skip parameters for Subject, Patient, Code and Category for LastN as these will be filtered by
				// Elasticsearch
				continue;
			}
			List<List<IQueryParameterType>> andOrParams = myParams.get(nextParamName);
			Condition predicate = theQueryStack.searchForIdsWithAndOr(with().setResourceName(myResourceName)
					.setParamName(nextParamName)
					.setAndOrParams(andOrParams)
					.setRequest(theRequest)
					.setRequestPartitionId(myRequestPartitionId)
					.setIncludeDeleted(myParams.getSearchIncludeDeletedMode()));
			if (predicate != null) {
				theSearchSqlBuilder.addPredicate(predicate);
			}
		}
	}

	/**
	 * This method returns <code>true</code> if the search is potentially a candidate for
	 * processing using a Combo SearchParameter. This means that:
	 * <ul>
	 *     <li>Combo SearchParamdeters are enabled</li>
	 *     <li>It's not an $everything search</li>
	 *     <li>We're searching on a specific resource type</li>
	 * </ul>
	 */
	private boolean isComboSearchCandidate() {
		return myStorageSettings.isUniqueIndexesEnabled()
				&& myParams.getEverythingMode() == null
				&& myResourceName != null;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public Long createCountQuery(
			SearchParameterMap theParams,
			String theSearchUuid,
			RequestDetails theRequest,
			@Nonnull RequestPartitionId theRequestPartitionId) {

		assert theRequestPartitionId != null;
		assert TransactionSynchronizationManager.isActualTransactionActive();

		init(theParams, theSearchUuid, theRequestPartitionId);

		if (checkUseHibernateSearch()) {
			return myFulltextSearchSvc.count(myResourceName, theParams.clone());
		}

		SearchQueryProperties properties = mySearchProperties.clone();
		properties.setDoCountOnlyFlag(true);
		properties.setSortSpec(null); // counts don't require sorts
		properties.setMaxResultsRequested(null);
		properties.setOffset(null);
		List<ISearchQueryExecutor> queries = createQuery(theParams.clone(), properties, theRequest, null);
		if (queries.isEmpty()) {
			return 0L;
		} else {
			JpaPid jpaPid = queries.get(0).next();
			return jpaPid.getId();
		}
	}

	/**
	 * @param thePidSet May be null
	 */
	@Override
	public void setPreviouslyAddedResourcePids(@Nonnull List<JpaPid> thePidSet) {
		myPidSet = new HashSet<>(thePidSet);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public IResultIterator<JpaPid> createQuery(
			SearchParameterMap theParams,
			SearchRuntimeDetails theSearchRuntimeDetails,
			RequestDetails theRequest,
			@Nonnull RequestPartitionId theRequestPartitionId) {
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
		// we mutate the params.  Make a private copy.
		myParams = theParams.clone();
		mySearchProperties.setSortSpec(myParams.getSort());
		mySearchUuid = theSearchUuid;
		myRequestPartitionId = theRequestPartitionId;
	}

	/**
	 * The query created can be either a count query or the
	 * actual query.
	 * This is why it takes a SearchQueryProperties object
	 * (and doesn't use the local version of it).
	 * The properties may differ slightly for whichever
	 * query this is.
	 */
	private List<ISearchQueryExecutor> createQuery(
			SearchParameterMap theParams,
			SearchQueryProperties theSearchProperties,
			RequestDetails theRequest,
			SearchRuntimeDetails theSearchRuntimeDetails) {
		ArrayList<ISearchQueryExecutor> queries = new ArrayList<>();

		if (checkUseHibernateSearch()) {
			// we're going to run at least part of the search against the Fulltext service.

			// Ugh - we have two different return types for now
			ISearchQueryExecutor fulltextExecutor = null;
			List<JpaPid> fulltextMatchIds = null;
			int resultCount = 0;
			if (myParams.isLastN()) {
				fulltextMatchIds = executeLastNAgainstIndex(theRequest, theSearchProperties.getMaxResultsRequested());
				resultCount = fulltextMatchIds.size();
			} else if (myParams.getEverythingMode() != null) {
				fulltextMatchIds = queryHibernateSearchForEverythingPids(theRequest);
				resultCount = fulltextMatchIds.size();
			} else {
				// todo performance MB - some queries must intersect with JPA (e.g. they have a chain, or we haven't
				// enabled SP indexing).
				// and some queries don't need JPA.  We only need the scroll when we need to intersect with JPA.
				// It would be faster to have a non-scrolled search in this case, since creating the scroll requires
				// extra work in Elastic.
				// if (eligibleToSkipJPAQuery) fulltextExecutor = myFulltextSearchSvc.searchNotScrolled( ...

				// we might need to intersect with JPA.  So we might need to traverse ALL results from lucene, not just
				// a page.
				fulltextExecutor = myFulltextSearchSvc.searchScrolled(myResourceName, myParams, theRequest);
			}

			if (fulltextExecutor == null) {
				fulltextExecutor =
						SearchQueryExecutors.from(fulltextMatchIds != null ? fulltextMatchIds : new ArrayList<>());
			}

			if (theSearchRuntimeDetails != null) {
				theSearchRuntimeDetails.setFoundIndexMatchesCount(resultCount);
				IInterceptorBroadcaster compositeBroadcaster =
						CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequest);
				if (compositeBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_INDEXSEARCH_QUERY_COMPLETE)) {
					HookParams params = new HookParams()
							.add(RequestDetails.class, theRequest)
							.addIfMatchesType(ServletRequestDetails.class, theRequest)
							.add(SearchRuntimeDetails.class, theSearchRuntimeDetails);
					compositeBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_INDEXSEARCH_QUERY_COMPLETE, params);
				}
			}

			// can we skip the database entirely and return the pid list from here?
			boolean canSkipDatabase =
					// if we processed an AND clause, and it returned nothing, then nothing can match.
					!fulltextExecutor.hasNext()
							||
							// Our hibernate search query doesn't respect partitions yet
							(!myPartitionSettings.isPartitioningEnabled()
									&&
									// were there AND terms left?  Then we still need the db.
									theParams.isEmpty()
									&&
									// not every param is a param. :-(
									theParams.getNearDistanceParam() == null
									&&
									// todo MB don't we support _lastUpdated and _offset now?
									theParams.getLastUpdated() == null
									&& theParams.getEverythingMode() == null
									&& theParams.getOffset() == null);

			if (canSkipDatabase) {
				ourLog.trace("Query finished after HSearch.  Skip db query phase");
				if (theSearchProperties.hasMaxResultsRequested()) {
					fulltextExecutor = SearchQueryExecutors.limited(
							fulltextExecutor, theSearchProperties.getMaxResultsRequested());
				}
				queries.add(fulltextExecutor);
			} else {
				ourLog.trace("Query needs db after HSearch.  Chunking.");
				// Finish the query in the database for the rest of the search parameters, sorting, partitioning, etc.
				// We break the pids into chunks that fit in the 1k limit for jdbc bind params.
				QueryChunker.chunk(
						fulltextExecutor,
						SearchBuilder.getMaximumPageSize(),
						// for each list of (SearchBuilder.getMaximumPageSize())
						// we create a chunked query and add it to 'queries'
						t -> doCreateChunkedQueries(theParams, t, theSearchProperties, theRequest, queries));
			}
		} else {
			// do everything in the database.
			createChunkedQuery(theParams, theSearchProperties, theRequest, null, queries);
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
		} else {
			for (SortSpec sortSpec : myParams.getAllChainsInOrder()) {
				final String paramName = sortSpec.getParamName();
				if (paramName.contains(".")) {
					failIfUsedWithChainedSort(Constants.PARAM_TEXT);
					failIfUsedWithChainedSort(Constants.PARAM_CONTENT);
				}
			}
		}

		// someday we'll want a query planner to figure out if we _should_ or _must_ use the ft index, not just if we
		// can.
		return fulltextEnabled
				&& myParams != null
				&& myParams.getSearchContainedMode() == SearchContainedModeEnum.FALSE
				&& myFulltextSearchSvc.canUseHibernateSearch(myResourceName, myParams)
				&& myFulltextSearchSvc.supportsAllSortTerms(myResourceName, myParams);
	}

	private void failIfUsed(String theParamName) {
		if (myParams.containsKey(theParamName)) {
			throw new InvalidRequestException(Msg.code(1192)
					+ "Fulltext search is not enabled on this service, can not process parameter: " + theParamName);
		}
	}

	private void failIfUsedWithChainedSort(String theParamName) {
		if (myParams.containsKey(theParamName)) {
			throw new InvalidRequestException(Msg.code(2524)
					+ "Fulltext search combined with chained sorts are not supported, can not process parameter: "
					+ theParamName);
		}
	}

	private List<JpaPid> executeLastNAgainstIndex(RequestDetails theRequestDetails, Integer theMaximumResults) {
		// Can we use our hibernate search generated index on resource to support lastN?:
		if (myStorageSettings.isHibernateSearchIndexSearchParams()) {
			if (myFulltextSearchSvc == null) {
				throw new InvalidRequestException(Msg.code(2027)
						+ "LastN operation is not enabled on this service, can not process this request");
			}
			return myFulltextSearchSvc.lastN(myParams, theMaximumResults).stream()
					.map(t -> (JpaPid) t)
					.collect(Collectors.toList());
		} else {
			throw new InvalidRequestException(
					Msg.code(2033) + "LastN operation is not enabled on this service, can not process this request");
		}
	}

	private List<JpaPid> queryHibernateSearchForEverythingPids(RequestDetails theRequestDetails) {
		JpaPid pid = null;
		if (myParams.get(IAnyResource.SP_RES_ID) != null) {
			String idParamValue;
			IQueryParameterType idParam =
					myParams.get(IAnyResource.SP_RES_ID).get(0).get(0);
			if (idParam instanceof TokenParam idParm) {
				idParamValue = idParm.getValue();
			} else {
				StringParam idParm = (StringParam) idParam;
				idParamValue = idParm.getValue();
			}

			pid = myIdHelperService
					.resolveResourceIdentity(
							myRequestPartitionId,
							myResourceName,
							idParamValue,
							ResolveIdentityMode.includeDeleted().cacheOk())
					.getPersistentId();
		}
		return myFulltextSearchSvc.everything(myResourceName, myParams, pid, theRequestDetails);
	}

	private void doCreateChunkedQueries(
			SearchParameterMap theParams,
			List<JpaPid> thePids,
			SearchQueryProperties theSearchQueryProperties,
			RequestDetails theRequest,
			ArrayList<ISearchQueryExecutor> theQueries) {

		if (thePids.size() < getMaximumPageSize()) {
			thePids = normalizeIdListForInClause(thePids);
		}
		theSearchQueryProperties.setMaxResultsRequested(thePids.size());
		createChunkedQuery(theParams, theSearchQueryProperties, theRequest, thePids, theQueries);
	}

	/**
	 * Combs through the params for any _id parameters and extracts the PIDs for them
	 */
	private void extractTargetPidsFromIdParams(Set<JpaPid> theTargetPids) {
		// get all the IQueryParameterType objects
		// for _id -> these should all be StringParam values
		HashSet<IIdType> ids = new HashSet<>();
		List<List<IQueryParameterType>> params = myParams.get(IAnyResource.SP_RES_ID);
		for (List<IQueryParameterType> paramList : params) {
			for (IQueryParameterType param : paramList) {
				String id;
				if (param instanceof StringParam) {
					// we expect all _id values to be StringParams
					id = ((StringParam) param).getValue();
				} else if (param instanceof TokenParam) {
					id = ((TokenParam) param).getValue();
				} else {
					// we do not expect the _id parameter to be a non-string value
					throw new IllegalArgumentException(
							Msg.code(1193) + "_id parameter must be a StringParam or TokenParam");
				}

				IIdType idType = myContext.getVersion().newIdType();
				if (id.contains("/")) {
					idType.setValue(id);
				} else {
					idType.setValue(myResourceName + "/" + id);
				}
				ids.add(idType);
			}
		}

		// fetch our target Pids
		// this will throw if an id is not found
		Map<IIdType, IResourceLookup<JpaPid>> idToIdentity = myIdHelperService.resolveResourceIdentities(
				myRequestPartitionId,
				new ArrayList<>(ids),
				ResolveIdentityMode.failOnDeleted().noCacheUnlessDeletesDisabled());

		// add the pids to targetPids
		for (IResourceLookup<JpaPid> pid : idToIdentity.values()) {
			theTargetPids.add(pid.getPersistentId());
		}
	}

	private void createChunkedQuery(
			SearchParameterMap theParams,
			SearchQueryProperties theSearchProperties,
			RequestDetails theRequest,
			List<JpaPid> thePidList,
			List<ISearchQueryExecutor> theSearchQueryExecutors) {
		if (myParams.getEverythingMode() != null) {
			createChunkedQueryForEverythingSearch(
					theRequest, theParams, theSearchProperties, thePidList, theSearchQueryExecutors);
		} else {
			createChunkedQueryNormalSearch(
					theParams, theSearchProperties, theRequest, thePidList, theSearchQueryExecutors);
		}
	}

	private void createChunkedQueryNormalSearch(
			SearchParameterMap theParams,
			SearchQueryProperties theSearchProperties,
			RequestDetails theRequest,
			List<JpaPid> thePidList,
			List<ISearchQueryExecutor> theSearchQueryExecutors) {
		SearchQueryBuilder sqlBuilder = new SearchQueryBuilder(
				myContext,
				myStorageSettings,
				myPartitionSettings,
				myRequestPartitionId,
				myResourceName,
				mySqlBuilderFactory,
				myDialectProvider,
				theSearchProperties.isDoCountOnlyFlag(),
				myResourceName == null || myResourceName.isBlank());
		QueryStack queryStack3 = new QueryStack(
				theRequest,
				theParams,
				myStorageSettings,
				myContext,
				sqlBuilder,
				mySearchParamRegistry,
				myPartitionSettings);

		if (theParams.keySet().size() > 1
				|| theParams.getSort() != null
				|| theParams.keySet().contains(Constants.PARAM_HAS)
				|| isPotentiallyContainedReferenceParameterExistsAtRoot(theParams)) {
			List<RuntimeSearchParam> activeComboParams = List.of();
			if (myResourceName != null) {
				activeComboParams = mySearchParamRegistry.getActiveComboSearchParams(
						myResourceName, theParams.keySet(), ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);
			}
			if (activeComboParams.isEmpty()) {
				sqlBuilder.setNeedResourceTableRoot(true);
			}
		}

		/*
		 * If we're doing a filter, always use the resource table as the root - This avoids the possibility of
		 * specific filters with ORs as their root from working around the natural resource type / deletion
		 * status / partition IDs built into queries.
		 */
		if (theParams.containsKey(Constants.PARAM_FILTER)) {
			Condition partitionIdPredicate = sqlBuilder
					.getOrCreateResourceTablePredicateBuilder()
					.createPartitionIdPredicate(myRequestPartitionId);
			if (partitionIdPredicate != null) {
				sqlBuilder.addPredicate(partitionIdPredicate);
			}
		}

		// Normal search
		// we will create a resourceTablePredicate if and only if we have an _id SP.
		searchForIdsWithAndOr(sqlBuilder, queryStack3, myParams, theRequest);

		// If we haven't added any predicates yet, we're doing a search for all resources. Make sure we add the
		// partition ID predicate in that case.
		if (!sqlBuilder.haveAtLeastOnePredicate()) {
			Condition partitionIdPredicate;

			if (theParams.getSearchIncludeDeletedMode() != null) {
				partitionIdPredicate = sqlBuilder
						.getOrCreateResourceTablePredicateBuilder(true, theParams.getSearchIncludeDeletedMode())
						.createPartitionIdPredicate(myRequestPartitionId);
			} else {
				partitionIdPredicate = sqlBuilder
						.getOrCreateResourceTablePredicateBuilder()
						.createPartitionIdPredicate(myRequestPartitionId);
			}

			if (partitionIdPredicate != null) {
				sqlBuilder.addPredicate(partitionIdPredicate);
			}
		}

		// Add PID list predicate for full text search and/or lastn operation
		addPidListPredicate(thePidList, sqlBuilder);

		// Last updated
		addLastUpdatePredicate(sqlBuilder);

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
		 * If offset is present, we want to deduplicate the results by using GROUP BY;
		 * OR
		 * if the MaxResultsToFetch is null, we are requesting "everything",
		 * so we'll let the db do the deduplication (instead of in-memory)
		 */
		if (theSearchProperties.isDeduplicateInDatabase()) {
			queryStack3.addGrouping();
			queryStack3.setUseAggregate(true);
		}

		/*
		 * Sort
		 *
		 * If we have a sort, we wrap the criteria search (the search that actually
		 * finds the appropriate resources) in an outer search which is then sorted
		 */
		if (theSearchProperties.hasSort()) {
			assert !theSearchProperties.isDoCountOnlyFlag();

			createSort(queryStack3, theSearchProperties.getSortSpec(), theParams);
		}

		/*
		 * Now perform the search
		 */
		executeSearch(theSearchProperties, theSearchQueryExecutors, sqlBuilder);
	}

	private void executeSearch(
			SearchQueryProperties theProperties,
			List<ISearchQueryExecutor> theSearchQueryExecutors,
			SearchQueryBuilder sqlBuilder) {
		GeneratedSql generatedSql =
				sqlBuilder.generate(theProperties.getOffset(), theProperties.getMaxResultsRequested());
		if (!generatedSql.isMatchNothing()) {
			SearchQueryExecutor executor =
					mySqlBuilderFactory.newSearchQueryExecutor(generatedSql, theProperties.getMaxResultsRequested());
			theSearchQueryExecutors.add(executor);
		}
	}

	private void createChunkedQueryForEverythingSearch(
			RequestDetails theRequest,
			SearchParameterMap theParams,
			SearchQueryProperties theSearchQueryProperties,
			List<JpaPid> thePidList,
			List<ISearchQueryExecutor> theSearchQueryExecutors) {

		SearchQueryBuilder sqlBuilder = new SearchQueryBuilder(
				myContext,
				myStorageSettings,
				myPartitionSettings,
				myRequestPartitionId,
				null,
				mySqlBuilderFactory,
				myDialectProvider,
				theSearchQueryProperties.isDoCountOnlyFlag(),
				false);

		QueryStack queryStack3 = new QueryStack(
				theRequest,
				theParams,
				myStorageSettings,
				myContext,
				sqlBuilder,
				mySearchParamRegistry,
				myPartitionSettings);

		JdbcTemplate jdbcTemplate = initializeJdbcTemplate(theSearchQueryProperties.getMaxResultsRequested());

		Set<JpaPid> targetPids = new HashSet<>();
		if (myParams.get(IAnyResource.SP_RES_ID) != null) {

			extractTargetPidsFromIdParams(targetPids);

			if (targetPids.isEmpty()) {
				// An _id parameter was provided to perform an instance $everything operation,
				// but the ID could not be resoled to any PIDs --> throw ResourceNotFoundException as per FHIR spec
				throw new ResourceNotFoundException(
						Msg.code(2841) + "Resource " + myParams.get(IAnyResource.SP_RES_ID) + " is not known.");
			}

			// add the target pids to our executors as the first
			// results iterator to go through
			theSearchQueryExecutors.add(new ResolvedSearchQueryExecutor(new ArrayList<>(targetPids)));
		} else {
			// For Everything queries, we make the query root by the ResourceLink table, since this query
			// is basically a reverse-include search. For type/Everything (as opposed to instance/Everything)
			// the one problem with this approach is that it doesn't catch Patients that have absolutely
			// nothing linked to them. So we do one additional query to make sure we catch those too.
			SearchQueryBuilder fetchPidsSqlBuilder = new SearchQueryBuilder(
					myContext,
					myStorageSettings,
					myPartitionSettings,
					myRequestPartitionId,
					myResourceName,
					mySqlBuilderFactory,
					myDialectProvider,
					theSearchQueryProperties.isDoCountOnlyFlag(),
					false);
			GeneratedSql allTargetsSql = fetchPidsSqlBuilder.generate(
					theSearchQueryProperties.getOffset(), mySearchProperties.getMaxResultsRequested());
			String sql = allTargetsSql.getSql();
			Object[] args = allTargetsSql.getBindVariables().toArray(new Object[0]);

			List<JpaPid> output =
					jdbcTemplate.query(sql, new JpaPidRowMapper(myPartitionSettings.isPartitioningEnabled()), args);

			// we add a search executor to fetch unlinked patients first
			theSearchQueryExecutors.add(new ResolvedSearchQueryExecutor(output));
		}

		List<String> typeSourceResources = new ArrayList<>();
		if (myParams.get(Constants.PARAM_TYPE) != null) {
			typeSourceResources.addAll(extractTypeSourceResourcesFromParams());
		}

		queryStack3.addPredicateEverythingOperation(
				myResourceName, typeSourceResources, targetPids.toArray(EMPTY_JPA_PID_ARRAY));

		// Add PID list predicate for full text search and/or lastn operation
		addPidListPredicate(thePidList, sqlBuilder);

		/*
		 * If offset is present, we want deduplicate the results by using GROUP BY
		 * ORDER BY is required to make sure we return unique results for each page
		 */
		if (theSearchQueryProperties.hasOffset()) {
			queryStack3.addGrouping();
			queryStack3.addOrdering();
			queryStack3.setUseAggregate(true);
		}

		if (myParams.getEverythingMode().isPatient()) {
			/*
			 * NB: patient-compartment limitation
			 *
			 * We are manually excluding Group and List resources
			 * from the patient-compartment for $everything operations on Patient type/instance.
			 *
			 * See issue: https://github.com/hapifhir/hapi-fhir/issues/7118
			 */
			sqlBuilder.excludeResourceTypesPredicate(
					SearchParameterUtil.RESOURCE_TYPES_TO_SP_TO_OMIT_FROM_PATIENT_COMPARTMENT.keySet());
		}

		/*
		 * Now perform the search
		 */
		executeSearch(theSearchQueryProperties, theSearchQueryExecutors, sqlBuilder);
	}

	private void addPidListPredicate(List<JpaPid> thePidList, SearchQueryBuilder theSqlBuilder) {
		if (thePidList != null && !thePidList.isEmpty()) {
			theSqlBuilder.addResourceIdsPredicate(thePidList);
		}
	}

	private void addLastUpdatePredicate(SearchQueryBuilder theSqlBuilder) {
		DateRangeParam lu = myParams.getLastUpdated();
		if (lu != null && !lu.isEmpty()) {
			Condition lastUpdatedPredicates = theSqlBuilder.addPredicateLastUpdated(lu);
			theSqlBuilder.addPredicate(lastUpdatedPredicates);
		}
	}

	private JdbcTemplate initializeJdbcTemplate(Integer theMaximumResults) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(myEntityManagerFactory.getDataSource());
		jdbcTemplate.setFetchSize(myFetchSize);
		if (theMaximumResults != null) {
			jdbcTemplate.setMaxRows(theMaximumResults);
		}
		return jdbcTemplate;
	}

	private Collection<String> extractTypeSourceResourcesFromParams() {

		List<List<IQueryParameterType>> listOfList = myParams.get(Constants.PARAM_TYPE);

		// first off, let's flatten the list of list
		List<IQueryParameterType> iQueryParameterTypesList =
				listOfList.stream().flatMap(List::stream).toList();

		// then, extract all elements of each CSV into one big list
		List<String> resourceTypes = iQueryParameterTypesList.stream()
				.map(param -> ((StringParam) param).getValue())
				.map(csvString -> List.of(csvString.split(",")))
				.flatMap(List::stream)
				.toList();

		Set<String> knownResourceTypes = myContext.getResourceTypes();

		// remove leading/trailing whitespaces if any and remove duplicates
		Set<String> retVal = new HashSet<>();

		for (String type : resourceTypes) {
			String trimmed = type.trim();
			if (!knownResourceTypes.contains(trimmed)) {
				throw new ResourceNotFoundException(
						Msg.code(2197) + "Unknown resource type '" + trimmed + "' in _type parameter.");
			}
			retVal.add(trimmed);
		}

		return retVal;
	}

	private boolean isPotentiallyContainedReferenceParameterExistsAtRoot(SearchParameterMap theParams) {
		return myStorageSettings.isIndexOnContainedResources()
				&& theParams.values().stream()
						.flatMap(Collection::stream)
						.flatMap(Collection::stream)
						.anyMatch(ReferenceParam.class::isInstance);
	}

	private void createSort(QueryStack theQueryStack, SortSpec theSort, SearchParameterMap theParams) {
		if (theSort == null || isBlank(theSort.getParamName())) {
			return;
		}

		boolean ascending = (theSort.getOrder() == null) || (theSort.getOrder() == SortOrderEnum.ASC);

		if (IAnyResource.SP_RES_ID.equals(theSort.getParamName())) {

			theQueryStack.addSortOnResourceId(ascending);

		} else if (Constants.PARAM_PID.equals(theSort.getParamName())) {

			theQueryStack.addSortOnResourcePID(ascending);

		} else if (Constants.PARAM_LASTUPDATED.equals(theSort.getParamName())) {

			theQueryStack.addSortOnLastUpdated(ascending);

		} else {
			RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam(
					myResourceName, theSort.getParamName(), ISearchParamRegistry.SearchParamLookupContextEnum.SORT);

			/*
			 * If we have a sort like _sort=subject.name and we  have an
			 * uplifted refchain for that combination we can do it more efficiently
			 * by using the index associated with the uplifted refchain. In this case,
			 * we need to find the actual target search parameter (corresponding
			 * to "name" in this example) so that we know what datatype it is.
			 */
			String paramName = theSort.getParamName();
			if (param == null && myStorageSettings.isIndexOnUpliftedRefchains()) {
				String[] chains = StringUtils.split(paramName, '.');
				if (chains.length == 2) {

					// Given: Encounter?_sort=Patient:subject.name
					String referenceParam = chains[0]; // subject
					String referenceParamTargetType = null; // Patient
					String targetParam = chains[1]; // name

					int colonIdx = referenceParam.indexOf(':');
					if (colonIdx > -1) {
						referenceParamTargetType = referenceParam.substring(0, colonIdx);
						referenceParam = referenceParam.substring(colonIdx + 1);
					}
					RuntimeSearchParam outerParam = mySearchParamRegistry.getActiveSearchParam(
							myResourceName, referenceParam, ISearchParamRegistry.SearchParamLookupContextEnum.SORT);
					if (outerParam == null) {
						throwInvalidRequestExceptionForUnknownSortParameter(myResourceName, referenceParam);
					} else if (outerParam.hasUpliftRefchain(targetParam)) {
						for (String nextTargetType : outerParam.getTargets()) {
							if (referenceParamTargetType != null && !referenceParamTargetType.equals(nextTargetType)) {
								continue;
							}
							RuntimeSearchParam innerParam = mySearchParamRegistry.getActiveSearchParam(
									nextTargetType,
									targetParam,
									ISearchParamRegistry.SearchParamLookupContextEnum.SORT);
							if (innerParam != null) {
								param = innerParam;
								break;
							}
						}
					}
				}
			}

			int colonIdx = paramName.indexOf(':');
			String referenceTargetType = null;
			if (colonIdx > -1) {
				referenceTargetType = paramName.substring(0, colonIdx);
				paramName = paramName.substring(colonIdx + 1);
			}

			int dotIdx = paramName.indexOf('.');
			String chainName = null;
			if (param == null && dotIdx > -1) {
				chainName = paramName.substring(dotIdx + 1);
				paramName = paramName.substring(0, dotIdx);
				if (chainName.contains(".")) {
					String msg = myContext
							.getLocalizer()
							.getMessageSanitized(
									BaseStorageDao.class,
									"invalidSortParameterTooManyChains",
									paramName + "." + chainName);
					throw new InvalidRequestException(Msg.code(2286) + msg);
				}
			}

			if (param == null) {
				param = mySearchParamRegistry.getActiveSearchParam(
						myResourceName, paramName, ISearchParamRegistry.SearchParamLookupContextEnum.SORT);
			}

			if (param == null) {
				throwInvalidRequestExceptionForUnknownSortParameter(getResourceName(), paramName);
			}

			// param will never be null here (the above line throws if it does)
			// this is just to prevent the warning
			assert param != null;
			if (isNotBlank(chainName) && param.getParamType() != RestSearchParameterTypeEnum.REFERENCE) {
				throw new InvalidRequestException(
						Msg.code(2285) + "Invalid chain, " + paramName + " is not a reference SearchParameter");
			}

			switch (param.getParamType()) {
				case STRING:
					theQueryStack.addSortOnString(myResourceName, paramName, ascending);
					break;
				case DATE:
					theQueryStack.addSortOnDate(myResourceName, paramName, ascending);
					break;
				case REFERENCE:
					theQueryStack.addSortOnResourceLink(
							myResourceName, referenceTargetType, paramName, chainName, ascending, theParams);
					break;
				case TOKEN:
					theQueryStack.addSortOnToken(myResourceName, paramName, ascending);
					break;
				case NUMBER:
					theQueryStack.addSortOnNumber(myResourceName, paramName, ascending);
					break;
				case URI:
					theQueryStack.addSortOnUri(myResourceName, paramName, ascending);
					break;
				case QUANTITY:
					theQueryStack.addSortOnQuantity(myResourceName, paramName, ascending);
					break;
				case COMPOSITE:
					List<JpaParamUtil.ComponentAndCorrespondingParam> compositeList =
							JpaParamUtil.resolveCompositeComponents(mySearchParamRegistry, param);
					if (compositeList == null) {
						throw new InvalidRequestException(Msg.code(1195) + "The composite _sort parameter " + paramName
								+ " is not defined by the resource " + myResourceName);
					}
					if (compositeList.size() != 2) {
						throw new InvalidRequestException(Msg.code(1196) + "The composite _sort parameter " + paramName
								+ " must have 2 composite types declared in parameter annotation, found "
								+ compositeList.size());
					}
					RuntimeSearchParam left = compositeList.get(0).getComponentParameter();
					RuntimeSearchParam right = compositeList.get(1).getComponentParameter();

					createCompositeSort(theQueryStack, left.getParamType(), left.getName(), ascending);
					createCompositeSort(theQueryStack, right.getParamType(), right.getName(), ascending);

					break;
				case SPECIAL:
					if (LOCATION_POSITION.equals(param.getPath())) {
						theQueryStack.addSortOnCoordsNear(paramName, ascending, theParams);
						break;
					}
					throw new InvalidRequestException(
							Msg.code(2306) + "This server does not support _sort specifications of type "
									+ param.getParamType() + " - Can't serve _sort=" + paramName);

				case HAS:
				default:
					throw new InvalidRequestException(
							Msg.code(1197) + "This server does not support _sort specifications of type "
									+ param.getParamType() + " - Can't serve _sort=" + paramName);
			}
		}

		// Recurse
		createSort(theQueryStack, theSort.getChain(), theParams);
	}

	private void throwInvalidRequestExceptionForUnknownSortParameter(String theResourceName, String theParamName) {
		Collection<String> validSearchParameterNames = mySearchParamRegistry.getValidSearchParameterNamesIncludingMeta(
				theResourceName, ISearchParamRegistry.SearchParamLookupContextEnum.SORT);
		String msg = myContext
				.getLocalizer()
				.getMessageSanitized(
						BaseStorageDao.class,
						"invalidSortParameter",
						theParamName,
						theResourceName,
						validSearchParameterNames);
		throw new InvalidRequestException(Msg.code(1194) + msg);
	}

	private void createCompositeSort(
			QueryStack theQueryStack,
			RestSearchParameterTypeEnum theParamType,
			String theParamName,
			boolean theAscending) {

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
				throw new InvalidRequestException(
						Msg.code(1198) + "Don't know how to handle composite parameter with type of " + theParamType
								+ " on _sort=" + theParamName);
		}
	}

	private void doLoadPids(
			RequestDetails theRequest,
			Collection<JpaPid> thePids,
			Collection<JpaPid> theIncludedPids,
			List<IBaseResource> theResourceListToPopulate,
			boolean theForHistoryOperation,
			Map<Long, Integer> thePosition) {

		Map<JpaPid, Long> resourcePidToVersion = null;
		for (JpaPid next : thePids) {
			if (next.getVersion() != null && myStorageSettings.isRespectVersionsForSearchIncludes()) {
				if (resourcePidToVersion == null) {
					resourcePidToVersion = new HashMap<>();
				}
				resourcePidToVersion.put(next, next.getVersion());
			}
		}

		List<JpaPid> versionlessPids = new ArrayList<>(thePids);
		int expectedCount = versionlessPids.size();
		if (versionlessPids.size() < getMaximumPageSize()) {
			/*
			 * This method adds a bunch of extra params to the end of the parameter list
			 * which are for a resource PID that will never exist (-1 / NO_MORE). We do this
			 * so that the database can rely on a cached execution plan since we're not
			 * generating a new SQL query for every possible number of resources.
			 */
			versionlessPids = normalizeIdListForInClause(versionlessPids);
		}

		// Load the resource bodies
		List<ResourceHistoryTable> resourceSearchViewList =
				myResourceHistoryTableDao.findCurrentVersionsByResourcePidsAndFetchResourceTable(versionlessPids);

		/*
		 * If we have specific versions to load, replace the history entries with the
		 * correct ones
		 *
		 * TODO: this could definitely be made more efficient, probably by not loading the wrong
		 * version entity first, and by batching the fetches. But this is a fairly infrequently
		 * used feature, and loading history entities by PK is a very efficient query so it's
		 * not the end of the world
		 */
		if (resourcePidToVersion != null) {
			for (int i = 0; i < resourceSearchViewList.size(); i++) {
				ResourceHistoryTable next = resourceSearchViewList.get(i);
				JpaPid resourceId = next.getPersistentId();
				Long version = resourcePidToVersion.get(resourceId);
				resourceId.setVersion(version);
				if (version != null && !version.equals(next.getVersion())) {
					ResourceHistoryTable replacement = myResourceHistoryTableDao.findForIdAndVersion(
							next.getResourceId().toFk(), version);
					resourceSearchViewList.set(i, replacement);
				}
			}
		}

		/*
		 * If we got fewer rows back than we expected, that means that one or more ResourceTable
		 * entities (HFJ_RESOURCE) have a RES_VER version which doesn't exist in the
		 * ResourceHistoryTable (HFJ_RES_VER) table. This should never happen under normal
		 * operation, but if someone manually deletes a row or otherwise ends up in a weird
		 * state it can happen. In that case, we do a manual process of figuring out what
		 * is the right version.
		 */
		if (resourceSearchViewList.size() != expectedCount) {

			Set<JpaPid> loadedPks = resourceSearchViewList.stream()
					.map(ResourceHistoryTable::getResourceId)
					.collect(Collectors.toSet());
			for (JpaPid nextWantedPid : versionlessPids) {
				if (!nextWantedPid.equals(NO_MORE) && !loadedPks.contains(nextWantedPid)) {
					Optional<ResourceHistoryTable> latestVersion = findLatestVersion(
							theRequest, nextWantedPid, myResourceHistoryTableDao, myInterceptorBroadcaster);
					latestVersion.ifPresent(resourceSearchViewList::add);
				}
			}
		}

		List<ResourceLoadResult> resourceLoadResults =
				myBatchResourceLoader.loadResources(resourceSearchViewList, theForHistoryOperation);

		for (ResourceLoadResult next : resourceLoadResults) {
			if (next.isDeleted()) {
				continue;
			}
			JpaPid resourceId = next.pid();

			if (resourcePidToVersion != null) {
				Long version = resourcePidToVersion.get(resourceId);
				resourceId.setVersion(version);
			}

			Integer index = thePosition.get(resourceId.getId());
			if (index == null) {
				ourLog.warn("Got back unexpected resource PID {}", resourceId);
				continue;
			}

			if (theIncludedPids.contains(resourceId)) {
				ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(next.resource(), BundleEntrySearchModeEnum.INCLUDE);
			} else {
				ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(next.resource(), BundleEntrySearchModeEnum.MATCH);
			}

			// ensure there's enough space; "<=" because of 0-indexing
			while (theResourceListToPopulate.size() <= index) {
				theResourceListToPopulate.add(null);
			}
			theResourceListToPopulate.set(index, next.resource());
		}
	}

	@SuppressWarnings("OptionalIsPresent")
	@Nonnull
	public static Optional<ResourceHistoryTable> findLatestVersion(
			RequestDetails theRequest,
			JpaPid nextWantedPid,
			IResourceHistoryTableDao resourceHistoryTableDao,
			IInterceptorBroadcaster interceptorBroadcaster1) {
		assert nextWantedPid != null && !nextWantedPid.equals(NO_MORE);

		Optional<ResourceHistoryTable> latestVersion = resourceHistoryTableDao
				.findVersionsForResource(JpaConstants.SINGLE_RESULT, nextWantedPid.toFk())
				.findFirst();
		String warning;
		if (latestVersion.isPresent()) {
			warning = "Database resource entry (HFJ_RESOURCE) with PID " + nextWantedPid
					+ " specifies an unknown current version, returning version "
					+ latestVersion.get().getVersion()
					+ " instead. This invalid entry has a negative impact on performance; consider performing an appropriate $reindex to correct your data.";
		} else {
			warning = "Database resource entry (HFJ_RESOURCE) with PID " + nextWantedPid
					+ " specifies an unknown current version, and no versions of this resource exist. This invalid entry has a negative impact on performance; consider performing an appropriate $reindex to correct your data.";
		}

		IInterceptorBroadcaster interceptorBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(interceptorBroadcaster1, theRequest);
		logAndBroadcastWarning(theRequest, warning, interceptorBroadcaster);
		return latestVersion;
	}

	private static void logAndBroadcastWarning(
			RequestDetails theRequest, String warning, IInterceptorBroadcaster interceptorBroadcaster) {
		ourLog.warn(warning);

		if (interceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_WARNING)) {
			HookParams params = new HookParams();
			params.add(RequestDetails.class, theRequest);
			params.addIfMatchesType(ServletRequestDetails.class, theRequest);
			params.add(StorageProcessingMessage.class, new StorageProcessingMessage().setMessage(warning));
			interceptorBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_WARNING, params);
		}
	}

	@Override
	public void loadResourcesByPid(
			Collection<JpaPid> thePids,
			Collection<JpaPid> theIncludedPids,
			List<IBaseResource> theResourceListToPopulate,
			boolean theForHistoryOperation,
			RequestDetails theRequestDetails) {
		if (thePids.isEmpty()) {
			ourLog.debug("The include pids are empty");
		}

		// Dupes will cause a crash later anyhow, but this is expensive so only do it
		// when running asserts
		assert new HashSet<>(thePids).size() == thePids.size() : "PID list contains duplicates: " + thePids;

		Map<Long, Integer> position = new HashMap<>();
		int index = 0;
		for (JpaPid next : thePids) {
			position.put(next.getId(), index++);
		}

		// Can we fast track this loading by checking elastic search?
		boolean isUsingElasticSearch = isLoadingFromElasticSearchSupported(thePids);
		if (isUsingElasticSearch) {
			try {
				theResourceListToPopulate.addAll(loadResourcesFromElasticSearch(thePids));
				return;

			} catch (ResourceNotFoundInIndexException theE) {
				// some resources were not found in index, so we will inform this and resort to JPA search
				ourLog.warn(
						"Some resources were not found in index. Make sure all resources were indexed. Resorting to database search.");
			}
		}

		// We only chunk because some jdbc drivers can't handle long param lists.
		QueryChunker.chunk(
				thePids,
				t -> doLoadPids(
						theRequestDetails,
						t,
						theIncludedPids,
						theResourceListToPopulate,
						theForHistoryOperation,
						position));
	}

	/**
	 * Check if we can load the resources from Hibernate Search instead of the database.
	 * We assume this is faster.
	 * <p>
	 * Hibernate Search only stores the current version, and only if enabled.
	 *
	 * @param thePids the pids to check for versioned references
	 * @return can we fetch from Hibernate Search?
	 */
	private boolean isLoadingFromElasticSearchSupported(Collection<JpaPid> thePids) {
		// is storage enabled?
		return myStorageSettings.isStoreResourceInHSearchIndex()
				&& myStorageSettings.isHibernateSearchIndexSearchParams()
				&&
				// we don't support history
				thePids.stream().noneMatch(p -> p.getVersion() != null)
				&&
				// skip the complexity for metadata in dstu2
				myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3);
	}

	private List<IBaseResource> loadResourcesFromElasticSearch(Collection<JpaPid> thePids) {
		// Do we use the fulltextsvc via hibernate-search to load resources or be backwards compatible with older ES
		// only impl
		// to handle lastN?
		if (myStorageSettings.isHibernateSearchIndexSearchParams()
				&& myStorageSettings.isStoreResourceInHSearchIndex()) {
			List<Long> pidList = thePids.stream().map(JpaPid::getId).collect(Collectors.toList());

			return myFulltextSearchSvc.getResources(pidList);
		} else if (!Objects.isNull(myParams) && myParams.isLastN()) {
			// legacy LastN implementation
			return myIElasticsearchSvc.getObservationResources(thePids);
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * THIS SHOULD RETURN HASHSET and not just Set because we add to it later
	 * so it can't be Collections.emptySet() or some such thing.
	 * The JpaPid returned will have resource type populated.
	 */
	@Override
	public Set<JpaPid> loadIncludes(
			FhirContext theContext,
			EntityManager theEntityManager,
			Collection<JpaPid> theMatches,
			Collection<Include> theIncludes,
			boolean theReverseMode,
			DateRangeParam theLastUpdated,
			String theSearchIdOrDescription,
			RequestDetails theRequest,
			Integer theMaxCount) {
		SearchBuilderLoadIncludesParameters<JpaPid> parameters = new SearchBuilderLoadIncludesParameters<>();
		parameters.setFhirContext(theContext);
		parameters.setEntityManager(theEntityManager);
		parameters.setMatches(theMatches);
		parameters.setIncludeFilters(theIncludes);
		parameters.setReverseMode(theReverseMode);
		parameters.setLastUpdated(theLastUpdated);
		parameters.setSearchIdOrDescription(theSearchIdOrDescription);
		parameters.setRequestDetails(theRequest);
		parameters.setMaxCount(theMaxCount);
		return loadIncludes(parameters);
	}

	@Override
	public Set<JpaPid> loadIncludes(SearchBuilderLoadIncludesParameters<JpaPid> theParameters) {
		Collection<JpaPid> matches = theParameters.getMatches();
		Collection<Include> currentIncludes = theParameters.getIncludeFilters();
		boolean reverseMode = theParameters.isReverseMode();
		EntityManager entityManager = theParameters.getEntityManager();
		Integer maxCount = theParameters.getMaxCount();
		FhirContext fhirContext = theParameters.getFhirContext();
		RequestDetails request = theParameters.getRequestDetails();
		String searchIdOrDescription = theParameters.getSearchIdOrDescription();
		List<String> desiredResourceTypes = theParameters.getDesiredResourceTypes();
		boolean hasDesiredResourceTypes = desiredResourceTypes != null && !desiredResourceTypes.isEmpty();
		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, request);

		if (compositeBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_RAW_SQL)) {
			CurrentThreadCaptureQueriesListener.startCapturing();
		}
		if (matches.isEmpty()) {
			return new HashSet<>();
		}
		if (currentIncludes == null || currentIncludes.isEmpty()) {
			return new HashSet<>();
		}
		String searchPidFieldName = reverseMode ? MY_TARGET_RESOURCE_PID : MY_SOURCE_RESOURCE_PID;
		String searchPartitionIdFieldName =
				reverseMode ? MY_TARGET_RESOURCE_PARTITION_ID : MY_SOURCE_RESOURCE_PARTITION_ID;
		String findPidFieldName = reverseMode ? MY_SOURCE_RESOURCE_PID : MY_TARGET_RESOURCE_PID;
		String findPartitionIdFieldName =
				reverseMode ? MY_SOURCE_RESOURCE_PARTITION_ID : MY_TARGET_RESOURCE_PARTITION_ID;
		String findResourceTypeFieldName = reverseMode ? MY_SOURCE_RESOURCE_TYPE : MY_TARGET_RESOURCE_TYPE;
		String findVersionFieldName = null;
		if (!reverseMode && myStorageSettings.isRespectVersionsForSearchIncludes()) {
			findVersionFieldName = MY_TARGET_RESOURCE_VERSION;
		}

		List<JpaPid> nextRoundMatches = new ArrayList<>(matches);
		HashSet<JpaPid> allAdded = new HashSet<>();
		HashSet<JpaPid> original = new HashSet<>(matches);
		ArrayList<Include> includes = new ArrayList<>(currentIncludes);

		int roundCounts = 0;
		StopWatch w = new StopWatch();

		boolean addedSomeThisRound;
		do {
			roundCounts++;

			HashSet<JpaPid> pidsToInclude = new HashSet<>();

			for (Iterator<Include> iter = includes.iterator(); iter.hasNext(); ) {
				Include nextInclude = iter.next();
				if (!nextInclude.isRecurse()) {
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
					loadIncludesMatchAll(
							findPidFieldName,
							findPartitionIdFieldName,
							findResourceTypeFieldName,
							findVersionFieldName,
							searchPidFieldName,
							searchPartitionIdFieldName,
							wantResourceType,
							reverseMode,
							hasDesiredResourceTypes,
							nextRoundMatches,
							entityManager,
							maxCount,
							desiredResourceTypes,
							pidsToInclude,
							request);
				} else {
					loadIncludesMatchSpecific(
							nextInclude,
							fhirContext,
							findPidFieldName,
							findPartitionIdFieldName,
							findVersionFieldName,
							searchPidFieldName,
							reverseMode,
							nextRoundMatches,
							entityManager,
							maxCount,
							pidsToInclude,
							request);
				}
			}

			nextRoundMatches.clear();
			for (JpaPid next : pidsToInclude) {
				if (!original.contains(next) && !allAdded.contains(next)) {
					nextRoundMatches.add(next);
				} else {
					ourLog.trace("Skipping include since it has already been seen. [jpaPid={}]", next);
				}
			}

			addedSomeThisRound = allAdded.addAll(pidsToInclude);

			if (maxCount != null && allAdded.size() >= maxCount) {
				break;
			}

		} while (!includes.isEmpty() && !nextRoundMatches.isEmpty() && addedSomeThisRound);

		allAdded.removeAll(original);

		ourLog.info(
				"Loaded {} {} in {} rounds and {} ms for search {}",
				allAdded.size(),
				reverseMode ? "_revincludes" : "_includes",
				roundCounts,
				w.getMillisAndRestart(),
				searchIdOrDescription);

		if (compositeBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_RAW_SQL)) {
			callRawSqlHookWithCurrentThreadQueries(request, compositeBroadcaster);
		}

		// Interceptor call: STORAGE_PREACCESS_RESOURCES
		// This can be used to remove results from the search result details before
		// the user has a chance to know that they were in the results
		if (!allAdded.isEmpty()) {

			if (compositeBroadcaster.hasHooks(Pointcut.STORAGE_PREACCESS_RESOURCES)) {
				List<JpaPid> includedPidList = new ArrayList<>(allAdded);
				JpaPreResourceAccessDetails accessDetails =
						new JpaPreResourceAccessDetails(includedPidList, () -> this);
				HookParams params = new HookParams()
						.add(IPreResourceAccessDetails.class, accessDetails)
						.add(RequestDetails.class, request)
						.addIfMatchesType(ServletRequestDetails.class, request);
				compositeBroadcaster.callHooks(Pointcut.STORAGE_PREACCESS_RESOURCES, params);

				for (int i = includedPidList.size() - 1; i >= 0; i--) {
					if (accessDetails.isDontReturnResourceAtIndex(i)) {
						JpaPid value = includedPidList.remove(i);
						if (value != null) {
							allAdded.remove(value);
						}
					}
				}
			}
		}

		return allAdded;
	}

	private void loadIncludesMatchSpecific(
			Include nextInclude,
			FhirContext fhirContext,
			String findPidFieldName,
			String findPartitionFieldName,
			String findVersionFieldName,
			String searchPidFieldName,
			boolean reverseMode,
			List<JpaPid> nextRoundMatches,
			EntityManager entityManager,
			Integer maxCount,
			HashSet<JpaPid> pidsToInclude,
			RequestDetails theRequest) {
		List<String> paths;

		// Start replace
		RuntimeSearchParam param;
		String resType = nextInclude.getParamType();
		if (isBlank(resType)) {
			return;
		}
		RuntimeResourceDefinition def = fhirContext.getResourceDefinition(resType);
		if (def == null) {
			ourLog.warn("Unknown resource type in include/revinclude=" + nextInclude.getValue());
			return;
		}

		String paramName = nextInclude.getParamName();
		if (isNotBlank(paramName)) {
			param = mySearchParamRegistry.getActiveSearchParam(
					resType, paramName, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);
		} else {
			param = null;
		}
		if (param == null) {
			ourLog.warn("Unknown param name in include/revinclude=" + nextInclude.getValue());
			return;
		}

		paths = param.getPathsSplitForResourceType(resType);
		// end replace

		Set<String> targetResourceTypes = computeTargetResourceTypes(nextInclude, param);

		for (String nextPath : paths) {
			String findPidFieldSqlColumn =
					findPidFieldName.equals(MY_SOURCE_RESOURCE_PID) ? "src_resource_id" : "target_resource_id";
			String fieldsToLoad = "r." + findPidFieldSqlColumn + " AS " + RESOURCE_ID_ALIAS;
			if (findVersionFieldName != null) {
				fieldsToLoad += ", r.target_resource_version AS " + RESOURCE_VERSION_ALIAS;
			}
			if (myPartitionSettings.isDatabasePartitionMode()) {
				fieldsToLoad += ", r.";
				fieldsToLoad += findPartitionFieldName.equals(MY_SOURCE_RESOURCE_PARTITION_ID)
						? "partition_id"
						: "target_res_partition_id";
				fieldsToLoad += " as " + PARTITION_ID_ALIAS;
			}

			// Query for includes lookup has 2 cases
			// Case 1: Where target_resource_id is available in hfj_res_link table for local references
			// Case 2: Where target_resource_id is null in hfj_res_link table and referred by a canonical
			// url in target_resource_url

			// Case 1:
			Map<String, Object> localReferenceQueryParams = new HashMap<>();

			String searchPidFieldSqlColumn =
					searchPidFieldName.equals(MY_TARGET_RESOURCE_PID) ? "target_resource_id" : "src_resource_id";
			StringBuilder localReferenceQuery = new StringBuilder();
			localReferenceQuery.append("SELECT ").append(fieldsToLoad);
			localReferenceQuery.append(" FROM hfj_res_link r ");
			localReferenceQuery.append("WHERE r.src_path = :src_path");
			if (!"target_resource_id".equals(searchPidFieldSqlColumn)) {
				localReferenceQuery.append(" AND r.target_resource_id IS NOT NULL");
			}
			localReferenceQuery
					.append(" AND r.")
					.append(searchPidFieldSqlColumn)
					.append(" IN (:target_pids) ");
			if (myPartitionSettings.isDatabasePartitionMode()) {
				String partitionFieldToSearch = findPartitionFieldName.equals(MY_SOURCE_RESOURCE_PARTITION_ID)
						? "target_res_partition_id"
						: "partition_id";
				localReferenceQuery
						.append("AND r.")
						.append(partitionFieldToSearch)
						.append(" = :search_partition_id ");
			}
			localReferenceQueryParams.put("src_path", nextPath);
			// we loop over target_pids later.
			if (targetResourceTypes != null) {
				if (targetResourceTypes.size() == 1) {
					localReferenceQuery.append("AND r.target_resource_type = :target_resource_type ");
					localReferenceQueryParams.put(
							"target_resource_type",
							targetResourceTypes.iterator().next());
				} else {
					localReferenceQuery.append("AND r.target_resource_type in (:target_resource_types) ");
					localReferenceQueryParams.put("target_resource_types", targetResourceTypes);
				}
			}

			// Case 2:
			Pair<String, Map<String, Object>> canonicalQuery =
					buildCanonicalUrlQuery(findVersionFieldName, targetResourceTypes, reverseMode, theRequest, param);

			String sql = localReferenceQuery.toString();
			if (canonicalQuery != null) {
				sql = localReferenceQuery + "UNION " + canonicalQuery.getLeft();
			}

			Map<String, Object> limitParams = new HashMap<>();
			if (maxCount != null) {
				LinkedList<Object> bindVariables = new LinkedList<>();
				sql = SearchQueryBuilder.applyLimitToSql(
						myDialectProvider.getDialect(), null, maxCount, sql, null, bindVariables);

				// The dialect SQL limiter uses positional params, but we're using
				// named params here, so we need to replace the positional params
				// with equivalent named ones
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < sql.length(); i++) {
					char nextChar = sql.charAt(i);
					if (nextChar == '?') {
						String nextName = "limit" + i;
						sb.append(':').append(nextName);
						limitParams.put(nextName, bindVariables.removeFirst());
					} else {
						sb.append(nextChar);
					}
				}
				sql = sb.toString();
			}

			List<Collection<JpaPid>> partitions = partitionBySizeAndPartitionId(nextRoundMatches, getMaximumPageSize());
			for (Collection<JpaPid> nextPartition : partitions) {
				Query q = entityManager.createNativeQuery(sql, Tuple.class);
				q.setParameter("target_pids", JpaPid.toLongList(nextPartition));
				if (myPartitionSettings.isDatabasePartitionMode()) {
					q.setParameter(
							"search_partition_id",
							nextPartition.iterator().next().getPartitionId());
				}
				localReferenceQueryParams.forEach(q::setParameter);
				if (canonicalQuery != null) {
					canonicalQuery.getRight().forEach(q::setParameter);
				}
				limitParams.forEach(q::setParameter);

				try (ScrollableResultsIterator<Tuple> iter = new ScrollableResultsIterator<>(toScrollableResults(q))) {
					Tuple result;
					while (iter.hasNext()) {
						result = iter.next();
						Long resourceId = NumberUtils.createLong(String.valueOf(result.get(RESOURCE_ID_ALIAS)));
						Long resourceVersion = null;
						if (findVersionFieldName != null && result.get(RESOURCE_VERSION_ALIAS) != null) {
							resourceVersion =
									NumberUtils.createLong(String.valueOf(result.get(RESOURCE_VERSION_ALIAS)));
						}
						Integer partitionId = null;
						if (myPartitionSettings.isDatabasePartitionMode()) {
							partitionId = result.get(PARTITION_ID_ALIAS, Integer.class);
						}

						JpaPid pid = JpaPid.fromIdAndVersion(resourceId, resourceVersion);
						pid.setPartitionId(partitionId);
						pidsToInclude.add(pid);
					}
				}
				//				myEntityManager.clear();
			}
		}
	}

	private void loadIncludesMatchAll(
			String findPidFieldName,
			String findPartitionFieldName,
			String findResourceTypeFieldName,
			String findVersionFieldName,
			String searchPidFieldName,
			String searchPartitionFieldName,
			String wantResourceType,
			boolean reverseMode,
			boolean hasDesiredResourceTypes,
			List<JpaPid> nextRoundMatches,
			EntityManager entityManager,
			Integer maxCount,
			List<String> desiredResourceTypes,
			HashSet<JpaPid> pidsToInclude,
			RequestDetails request) {

		record IncludesRecord(
				Long resourceId, String resourceType, String resourceCanonicalUrl, Long version, Integer partitionId) {}

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<IncludesRecord> query = cb.createQuery(IncludesRecord.class);
		Root<ResourceLink> root = query.from(ResourceLink.class);

		List<Selection<?>> selectionList = new ArrayList<>();
		selectionList.add(root.get(findPidFieldName));
		selectionList.add(root.get(findResourceTypeFieldName));
		selectionList.add(root.get("myTargetResourceUrl"));
		if (findVersionFieldName != null) {
			selectionList.add(root.get(findVersionFieldName));
		} else {
			selectionList.add(cb.nullLiteral(Long.class));
		}
		if (myPartitionSettings.isDatabasePartitionMode()) {
			selectionList.add(root.get(findPartitionFieldName));
		} else {
			selectionList.add(cb.nullLiteral(Integer.class));
		}
		query.multiselect(selectionList);

		List<Predicate> predicates = new ArrayList<>();

		if (myPartitionSettings.isDatabasePartitionMode()) {
			predicates.add(
					cb.equal(root.get(searchPartitionFieldName), cb.parameter(Integer.class, "target_partition_id")));
		}

		predicates.add(root.get(searchPidFieldName).in(cb.parameter(List.class, "target_pids")));

		/*
		 * We need to set the resource type in 2 cases only:
		 * 1) we are in $everything mode
		 * 		(where we only want to fetch specific resource types, regardless of what is
		 * 		available to fetch)
		 * 2) we are doing revincludes
		 *
		 *	Technically if the request is a qualified star (e.g. _include=Observation:*) we
		 * should always be checking the source resource type on the resource link. We don't
		 * actually index that column though by default, so in order to try and be efficient
		 * we don't actually include it for includes (but we do for revincludes). This is
		 * because for an include, it doesn't really make sense to include a different
		 * resource type than the one you are searching on.
		 */
		if (wantResourceType != null && (reverseMode || (myParams != null && myParams.getEverythingMode() != null))) {
			// because mySourceResourceType is not part of the HFJ_RES_LINK
			// index, this might not be the most optimal performance.
			// but it is for an $everything operation (and maybe we should update the index)
			predicates.add(
					cb.equal(root.get("mySourceResourceType"), cb.parameter(String.class, "want_resource_type")));
		} else {
			wantResourceType = null;
		}

		// When calling $everything on a Patient instance, we don't want to recurse into new Patient
		// resources
		// (e.g. via Provenance, List, or Group) when in an $everything operation
		if (myParams != null
				&& myParams.getEverythingMode() == SearchParameterMap.EverythingModeEnum.PATIENT_INSTANCE) {
			predicates.add(cb.notEqual(root.get("myTargetResourceType"), "Patient"));
			predicates.add(cb.not(root.get("mySourceResourceType")
					.in(UNDESIRED_RESOURCE_LINKAGES_FOR_EVERYTHING_ON_PATIENT_INSTANCE)));
		}

		if (hasDesiredResourceTypes) {
			predicates.add(
					root.get("myTargetResourceType").in(cb.parameter(List.class, "desired_target_resource_types")));
		}

		query.where(cb.and(predicates.toArray(new Predicate[0])));

		List<Collection<JpaPid>> partitions = partitionBySizeAndPartitionId(nextRoundMatches, getMaximumPageSize());
		for (Collection<JpaPid> nextPartition : partitions) {

			TypedQuery<IncludesRecord> q = myEntityManager.createQuery(query);
			q.setParameter("target_pids", JpaPid.toLongList(nextPartition));
			if (myPartitionSettings.isDatabasePartitionMode()) {
				q.setParameter(
						"target_partition_id", nextPartition.iterator().next().getPartitionId());
			}
			if (wantResourceType != null) {
				q.setParameter("want_resource_type", wantResourceType);
			}
			if (maxCount != null) {
				q.setMaxResults(maxCount);
			}
			if (hasDesiredResourceTypes) {
				q.setParameter("desired_target_resource_types", desiredResourceTypes);
			}

			Set<String> canonicalUrls = null;

			try (ScrollableResultsIterator<IncludesRecord> iter =
					new ScrollableResultsIterator<>(toScrollableResults(q))) {
				IncludesRecord nextRow;
				while (iter.hasNext()) {
					nextRow = iter.next();
					if (nextRow == null) {
						// This can happen if there are outgoing references which are canonical or point to
						// other servers
						continue;
					}

					Long version = nextRow.version;
					Long resourceId = nextRow.resourceId;
					String resourceType = nextRow.resourceType;
					String resourceCanonicalUrl = nextRow.resourceCanonicalUrl;
					Integer partitionId = nextRow.partitionId;

					if (resourceId != null) {
						JpaPid pid = JpaPid.fromIdAndVersionAndResourceType(resourceId, version, resourceType);
						pid.setPartitionId(partitionId);
						pidsToInclude.add(pid);
					} else if (resourceCanonicalUrl != null) {
						if (canonicalUrls == null) {
							canonicalUrls = new HashSet<>();
						}
						canonicalUrls.add(resourceCanonicalUrl);
					}
				}
			}

			if (canonicalUrls != null) {
				loadCanonicalUrls(request, canonicalUrls, entityManager, pidsToInclude, reverseMode);
			}
		}
	}

	private void loadCanonicalUrls(
			RequestDetails theRequestDetails,
			Set<String> theCanonicalUrls,
			EntityManager theEntityManager,
			HashSet<JpaPid> thePidsToInclude,
			boolean theReverse) {
		StringBuilder sqlBuilder;
		CanonicalUrlTargets canonicalUrlTargets =
				calculateIndexUriIdentityHashesForResourceTypes(theRequestDetails, null, theReverse);
		if (canonicalUrlTargets.isEmpty()) {
			return;
		}

		String message =
				"Search with _include=* can be inefficient when references using canonical URLs are detected. Use more specific _include values instead.";
		firePerformanceWarning(theRequestDetails, message);

		List<List<String>> canonicalUrlPartitions = ListUtils.partition(
				List.copyOf(theCanonicalUrls), getMaximumPageSize() - canonicalUrlTargets.hashIdentityValues.size());

		sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT ");
		if (myPartitionSettings.isPartitioningEnabled()) {
			sqlBuilder.append("i.myPartitionIdValue, ");
		}
		sqlBuilder.append("i.myResourcePid ");

		sqlBuilder.append("FROM ResourceIndexedSearchParamUri i ");
		sqlBuilder.append("WHERE i.myHashIdentity IN (:hash_identity) ");
		sqlBuilder.append("AND i.myUri IN (:uris)");

		String canonicalResSql = sqlBuilder.toString();

		for (Collection<String> nextCanonicalUrlList : canonicalUrlPartitions) {
			TypedQuery<Object[]> canonicalResIdQuery = theEntityManager.createQuery(canonicalResSql, Object[].class);
			canonicalResIdQuery.setParameter("hash_identity", canonicalUrlTargets.hashIdentityValues);
			canonicalResIdQuery.setParameter("uris", nextCanonicalUrlList);
			List<Object[]> results = canonicalResIdQuery.getResultList();
			for (var next : results) {
				if (next != null) {
					Integer partitionId = null;
					Long pid;
					if (next.length == 1) {
						pid = (Long) next[0];
					} else {
						partitionId = (Integer) ((Object[]) next)[0];
						pid = (Long) ((Object[]) next)[1];
					}
					if (pid != null) {
						thePidsToInclude.add(JpaPid.fromId(pid, partitionId));
					}
				}
			}
		}
	}

	/**
	 * Calls Performance Trace Hook
	 *
	 * @param request                 the request deatils
	 *                                Sends a raw SQL query to the Pointcut for raw SQL queries.
	 */
	private void callRawSqlHookWithCurrentThreadQueries(
			RequestDetails request, IInterceptorBroadcaster theCompositeBroadcaster) {
		SqlQueryList capturedQueries = CurrentThreadCaptureQueriesListener.getCurrentQueueAndStopCapturing();
		HookParams params = new HookParams()
				.add(RequestDetails.class, request)
				.addIfMatchesType(ServletRequestDetails.class, request)
				.add(SqlQueryList.class, capturedQueries);
		theCompositeBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_RAW_SQL, params);
	}

	@Nullable
	private static Set<String> computeTargetResourceTypes(Include nextInclude, RuntimeSearchParam param) {
		String targetResourceType = nextInclude.getParamTargetType();
		boolean haveTargetTypesDefinedByParam = param.hasTargets();
		Set<String> targetResourceTypes;
		if (targetResourceType != null) {
			targetResourceTypes = Set.of(targetResourceType);
		} else if (haveTargetTypesDefinedByParam) {
			targetResourceTypes = param.getTargets();
		} else {
			// all types!
			targetResourceTypes = null;
		}
		return targetResourceTypes;
	}

	@Nullable
	private Pair<String, Map<String, Object>> buildCanonicalUrlQuery(
			String theVersionFieldName,
			Set<String> theTargetResourceTypes,
			boolean theReverse,
			RequestDetails theRequest,
			RuntimeSearchParam theParam) {

		String[] searchParameterPaths = SearchParameterUtil.splitSearchParameterExpressions(theParam.getPath());

		// If we know for sure that none of the paths involved in this SearchParameter could
		// be indexing a canonical
		if (Arrays.stream(searchParameterPaths)
				.noneMatch(t -> SearchParameterUtil.referencePathCouldPotentiallyReferenceCanonicalElement(
						myContext, myResourceName, t, theReverse))) {
			return null;
		}

		String fieldsToLoadFromSpidxUriTable = theReverse ? "r.src_resource_id" : "rUri.res_id";
		if (theVersionFieldName != null) {
			// canonical-uri references aren't versioned, but we need to match the column count for the UNION
			fieldsToLoadFromSpidxUriTable += ", NULL";
		}

		if (myPartitionSettings.isDatabasePartitionMode()) {
			if (theReverse) {
				fieldsToLoadFromSpidxUriTable += ", r.partition_id as " + PARTITION_ID_ALIAS;
			} else {
				fieldsToLoadFromSpidxUriTable += ", rUri.partition_id as " + PARTITION_ID_ALIAS;
			}
		}

		// The logical join will be by hfj_spidx_uri on sp_name='uri' and sp_uri=target_resource_url.
		// But sp_name isn't indexed, so we use hash_identity instead.
		CanonicalUrlTargets canonicalUrlTargets =
				calculateIndexUriIdentityHashesForResourceTypes(theRequest, theTargetResourceTypes, theReverse);
		if (canonicalUrlTargets.isEmpty()) {
			return null;
		}

		Map<String, Object> canonicalUriQueryParams = new HashMap<>();
		StringBuilder canonicalUrlQuery = new StringBuilder();
		canonicalUrlQuery
				.append("SELECT ")
				.append(fieldsToLoadFromSpidxUriTable)
				.append(' ');
		canonicalUrlQuery.append("FROM hfj_res_link r ");

		// join on hash_identity and sp_uri - indexed in IDX_SP_URI_HASH_IDENTITY_V2
		canonicalUrlQuery.append("JOIN hfj_spidx_uri rUri ON (");
		if (myPartitionSettings.isDatabasePartitionMode()) {
			canonicalUrlQuery.append("rUri.partition_id IN (:uri_partition_id) AND ");
			canonicalUriQueryParams.put("uri_partition_id", canonicalUrlTargets.partitionIds);
		}
		if (canonicalUrlTargets.hashIdentityValues.size() == 1) {
			canonicalUrlQuery.append("rUri.hash_identity = :uri_identity_hash");
			canonicalUriQueryParams.put(
					"uri_identity_hash",
					canonicalUrlTargets.hashIdentityValues.iterator().next());
		} else {
			canonicalUrlQuery.append("rUri.hash_identity in (:uri_identity_hashes)");
			canonicalUriQueryParams.put("uri_identity_hashes", canonicalUrlTargets.hashIdentityValues);
		}
		canonicalUrlQuery.append(" AND r.target_resource_url = rUri.sp_uri");
		canonicalUrlQuery.append(")");

		canonicalUrlQuery.append(" WHERE r.src_path = :src_path AND");
		canonicalUrlQuery.append(" r.target_resource_id IS NULL");
		canonicalUrlQuery.append(" AND");
		if (myPartitionSettings.isDatabasePartitionMode()) {
			if (theReverse) {
				canonicalUrlQuery.append(" rUri.partition_id");
			} else {
				canonicalUrlQuery.append(" r.partition_id");
			}
			canonicalUrlQuery.append(" = :search_partition_id");
			canonicalUrlQuery.append(" AND");
		}
		if (theReverse) {
			canonicalUrlQuery.append(" rUri.res_id");
		} else {
			canonicalUrlQuery.append(" r.src_resource_id");
		}
		canonicalUrlQuery.append(" IN (:target_pids)");

		return Pair.of(canonicalUrlQuery.toString(), canonicalUriQueryParams);
	}

	@Nonnull
	CanonicalUrlTargets calculateIndexUriIdentityHashesForResourceTypes(
			RequestDetails theRequestDetails, Set<String> theTargetResourceTypes, boolean theReverse) {
		Set<String> targetResourceTypes = theTargetResourceTypes;
		if (targetResourceTypes == null) {
			/*
			 * If we don't have a list of valid target types, we need to figure out a list of all
			 * possible target types in order to perform the search of the URI index table. This is
			 * because the hash_identity column encodes the resource type, so we'll need a hash
			 * value for each possible target type.
			 */
			targetResourceTypes = new HashSet<>();
			Set<String> possibleTypes = myDaoRegistry.getRegisteredDaoTypes();
			if (theReverse) {
				// For reverse includes, it is really hard to figure out what types
				// are actually potentially pointing to the type we're searching for
				// in this context, so let's just assume it could be anything.
				targetResourceTypes = possibleTypes;
			} else {
				List<RuntimeSearchParam> params = mySearchParamRegistry
						.getActiveSearchParams(myResourceName, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH)
						.values()
						.stream()
						.filter(t -> t.getParamType().equals(RestSearchParameterTypeEnum.REFERENCE))
						.toList();
				for (var next : params) {

					String paths = next.getPath();
					for (String path : SearchParameterUtil.splitSearchParameterExpressions(paths)) {

						if (!SearchParameterUtil.referencePathCouldPotentiallyReferenceCanonicalElement(
								myContext, myResourceName, path, theReverse)) {
							continue;
						}

						if (!next.getTargets().isEmpty()) {
							// For each reference parameter on the resource type we're searching for,
							// add all the potential target types to the list of possible target
							// resource types we can look up.
							for (var nextTarget : next.getTargets()) {
								if (possibleTypes.contains(nextTarget)) {
									targetResourceTypes.add(nextTarget);
								}
							}
						} else {
							// If we have any references that don't define any target types, then
							// we need to assume that all enabled resource types are possible target
							// types
							targetResourceTypes.addAll(possibleTypes);
							break;
						}
					}
				}
			}
		}

		if (targetResourceTypes.isEmpty()) {
			return new CanonicalUrlTargets(Set.of(), Set.of());
		}

		Set<Long> hashIdentityValues = new HashSet<>();
		Set<Integer> partitionIds = new HashSet<>();
		for (String type : targetResourceTypes) {

			RequestPartitionId readPartition;
			if (myPartitionSettings.isPartitioningEnabled()) {
				readPartition =
						myPartitionHelperSvc.determineReadPartitionForRequestForSearchType(theRequestDetails, type);
			} else {
				readPartition = RequestPartitionId.defaultPartition();
			}
			if (readPartition.hasPartitionIds()) {
				partitionIds.addAll(readPartition.getPartitionIds());
			}

			Long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(
					myPartitionSettings, readPartition, type, "url");
			hashIdentityValues.add(hashIdentity);
		}

		return new CanonicalUrlTargets(hashIdentityValues, partitionIds);
	}

	record CanonicalUrlTargets(@Nonnull Set<Long> hashIdentityValues, @Nonnull Set<Integer> partitionIds) {
		public boolean isEmpty() {
			return hashIdentityValues.isEmpty();
		}
	}

	/**
	 * This method takes in a list of {@link JpaPid}'s and returns a series of sublists containing
	 * those pids where:
	 * <ul>
	 *     <li>No single list is more than {@literal theMaxLoad} entries</li>
	 *     <li>Each list only contains JpaPids with the same partition ID</li>
	 * </ul>
	 */
	static List<Collection<JpaPid>> partitionBySizeAndPartitionId(List<JpaPid> theNextRoundMatches, int theMaxLoad) {

		if (theNextRoundMatches.size() <= theMaxLoad) {
			boolean allSamePartition = true;
			for (int i = 1; i < theNextRoundMatches.size(); i++) {
				if (!Objects.equals(
						theNextRoundMatches.get(i - 1).getPartitionId(),
						theNextRoundMatches.get(i).getPartitionId())) {
					allSamePartition = false;
					break;
				}
			}
			if (allSamePartition) {
				return Collections.singletonList(theNextRoundMatches);
			}
		}

		// Break into partitioned sublists
		ListMultimap<String, JpaPid> lists =
				MultimapBuilder.hashKeys().arrayListValues().build();
		for (JpaPid nextRoundMatch : theNextRoundMatches) {
			String partitionId = nextRoundMatch.getPartitionId() != null
					? nextRoundMatch.getPartitionId().toString()
					: "";
			lists.put(partitionId, nextRoundMatch);
		}

		List<Collection<JpaPid>> retVal = new ArrayList<>();
		for (String key : lists.keySet()) {
			List<List<JpaPid>> nextPartition = Lists.partition(lists.get(key), theMaxLoad);
			retVal.addAll(nextPartition);
		}

		// In unit test mode, we sort the results just for unit test predictability
		if (HapiSystemProperties.isUnitTestModeEnabled()) {
			retVal = retVal.stream()
					.map(t -> t.stream().sorted().collect(Collectors.toList()))
					.collect(Collectors.toList());
		}

		return retVal;
	}

	/**
	 * If any Combo SearchParameters match the given query parameters, add a predicate
	 * to {@literal theQueryStack} and remove the parameters from {@literal theParams}.
	 * This method handles both UNIQUE and NON_UNIQUE combo parameters.
	 */
	private void attemptComboSearchParameterProcessing(
			QueryStack theQueryStack, @Nonnull SearchParameterMap theParams, RequestDetails theRequest) {

		List<RuntimeSearchParam> candidateComboParams = mySearchParamRegistry.getActiveComboSearchParams(
				myResourceName, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);
		for (RuntimeSearchParam nextCandidate : candidateComboParams) {

			List<JpaParamUtil.ComponentAndCorrespondingParam> nextCandidateComponents =
					JpaParamUtil.resolveCompositeComponents(mySearchParamRegistry, nextCandidate);

			/*
			 * First, a quick and dirty check to see if we have a parameter in the current search
			 * that contains all the parameters for the candidate combo search parameter. We do
			 * a more nuanced check later to make sure that the parameters have appropriate values,
			 * modifiers, etc. so this doesn't need to be perfect in terms of rejecting bad matches.
			 * It just needs to fail fast if the search couldn't possibly be a match for the
			 * candidate so we can move on quickly.
			 */
			boolean noMatch = false;
			for (JpaParamUtil.ComponentAndCorrespondingParam nextComponent : nextCandidateComponents) {
				if (!theParams.containsKey(nextComponent.getParamName())
						&& !theParams.containsKey(nextComponent.getCombinedParamName())) {
					noMatch = true;
					break;
				}
			}
			if (noMatch) {
				continue;
			}

			for (JpaParamUtil.ComponentAndCorrespondingParam nextComponent : nextCandidateComponents) {
				ensureSubListsAreWritable(theParams.get(nextComponent.getParamName()));
				ensureSubListsAreWritable(theParams.get(nextComponent.getCombinedParamName()));
			}

			/*
			 * Apply search against the combo param index in a loop:
			 *
			 * 1. First we check whether the actual parameter values in the
			 * parameter map are actually usable for searching against the combo
			 * param index. E.g. no search modifiers, date comparators, etc.,
			 * since these mean you can't use the combo index.
			 *
			 * 2. Apply and create the join SQl. We remove parameter values from
			 * the map as we apply them, so any parameter values remaining in the
			 * map after each loop haven't yet been factored into the SQL.
			 *
			 * The loop allows us to create multiple combo index joins if there
			 * are multiple AND expressions for the related parameters.
			 */
			boolean matched;
			do {
				matched = applyComboSearchParamIfAppropriate(
						theRequest, theQueryStack, theParams, nextCandidate, nextCandidateComponents);
			} while (matched);
		}
	}

	/**
	 * Attempts to apply a Combo SearchParameter to the current search. Assuming some or all parameters of
	 * the search are appropriate for the given Combo SearchParameter, a predicate is created and added to
	 * the QueryStack, and the parameters are removed from the search parameters map.
	 *
	 * @param theRequest              The RequestDetails for the current search.
	 * @param theQueryStack           The current SQL builder QueryStack to add a predicate to.
	 * @param theParams               The search parameters for the current search.
	 * @param theComboParam           The Combo SearchParameter to apply.
	 * @param theComboParamComponents The components of the Combo SearchParameter.
	 * @return Returns <code>true</code> if the Combo SearchParameter was applied successfully.
	 */
	private boolean applyComboSearchParamIfAppropriate(
			RequestDetails theRequest,
			QueryStack theQueryStack,
			@Nonnull SearchParameterMap theParams,
			RuntimeSearchParam theComboParam,
			List<JpaParamUtil.ComponentAndCorrespondingParam> theComboParamComponents) {

		List<List<IQueryParameterType>> inputs = new ArrayList<>(theComboParamComponents.size());
		List<Runnable> searchParameterConsumerTasks = new ArrayList<>(theComboParamComponents.size());
		for (JpaParamUtil.ComponentAndCorrespondingParam nextComponent : theComboParamComponents) {
			boolean foundMatch = false;

			/*
			 * The following List<List<IQueryParameterType>> is a list of query parameters where the
			 * outer list contains AND combinations, and the inner lists contain OR combinations.
			 * For each component in the Combo SearchParameter, we need to find a list of OR parameters
			 * (i.e. the inner List) which is appropriate for the given component.
			 *
			 * We can only use a combo param when the query parameter is fairly basic
			 * (no modifiers such as :missing or :below, references are qualified with
			 * a resource type, etc.) Once we've confirmed that we have a parameter for
			 * each component, we remove the components from the source SearchParameterMap
			 * since we're going to consume them and add a predicate to the SQL builder.
			 */
			List<List<IQueryParameterType>> sameNameParametersAndList = theParams.get(nextComponent.getParamName());
			if (sameNameParametersAndList != null) {
				boolean parameterIsChained = false;
				for (int andIndex = 0; andIndex < sameNameParametersAndList.size(); andIndex++) {
					List<IQueryParameterType> sameNameParametersOrList = sameNameParametersAndList.get(andIndex);
					IQueryParameterType firstValue = sameNameParametersOrList.get(0);

					if (firstValue instanceof ReferenceParam refParam) {
						if (!Objects.equals(nextComponent.getChain(), refParam.getChain())) {
							continue;
						}
					}

					if (!validateParamValuesAreValidForComboParam(
							theRequest, theParams, theComboParam, nextComponent, sameNameParametersOrList)) {
						continue;
					}

					inputs.add(sameNameParametersOrList);
					searchParameterConsumerTasks.add(() -> sameNameParametersAndList.remove(sameNameParametersOrList));
					foundMatch = true;
					break;
				}
			} else if (!nextComponent.getParamName().equals(nextComponent.getCombinedParamName())) {

				/*
				 * If we didn't find any parameters for the parameter name (e.g. "patient") and
				 * we're looking for a chained parameter (e.g. "patient.identifier"), check if
				 * there are any matches for the full combined parameter name
				 * (e.g. "patient.identifier").
				 */
				List<List<IQueryParameterType>> combinedNameParametersAndList =
						theParams.get(nextComponent.getCombinedParamName());
				if (combinedNameParametersAndList != null) {
					for (int andIndex = 0; andIndex < combinedNameParametersAndList.size(); andIndex++) {
						List<IQueryParameterType> combinedNameParametersOrList =
								combinedNameParametersAndList.get(andIndex);
						if (!combinedNameParametersOrList.isEmpty()) {

							if (!validateParamValuesAreValidForComboParam(
									theRequest,
									theParams,
									theComboParam,
									nextComponent,
									combinedNameParametersOrList)) {
								continue;
							}

							inputs.add(combinedNameParametersOrList);
							searchParameterConsumerTasks.add(
									() -> combinedNameParametersAndList.remove(combinedNameParametersOrList));
							foundMatch = true;
							break;
						}
					}
				}
			}

			if (!foundMatch) {
				return false;
			}
		}

		if (CartesianProductUtil.calculateCartesianProductSize(inputs) > 500) {
			ourLog.debug(
					"Search is not a candidate for unique combo searching - Too many OR values would result in too many permutations");
			return false;
		}

		searchParameterConsumerTasks.forEach(Runnable::run);

		List<List<IQueryParameterType>> inputPermutations = Lists.cartesianProduct(inputs);
		List<String> indexStrings = new ArrayList<>(CartesianProductUtil.calculateCartesianProductSize(inputs));
		for (List<IQueryParameterType> nextPermutation : inputPermutations) {

			List<String> parameters = new ArrayList<>();
			for (int paramIndex = 0; paramIndex < theComboParamComponents.size(); paramIndex++) {

				JpaParamUtil.ComponentAndCorrespondingParam componentAndCorrespondingParam =
						theComboParamComponents.get(paramIndex);
				String nextParamName = componentAndCorrespondingParam.getCombinedParamName();
				IQueryParameterType nextOr = nextPermutation.get(paramIndex);

				// The only prefix accepted when combo searching is 'eq' (see validateParamValuesAreValidForComboParam).
				// As a result, we strip the prefix if present.
				String nextOrValue = stripStart(nextOr.getValueAsQueryToken(), EQUAL.getValue());

				RestSearchParameterTypeEnum paramType = JpaParamUtil.getParameterTypeForComposite(
						mySearchParamRegistry, componentAndCorrespondingParam);
				if (theComboParam.getComboSearchParamType() == ComboSearchParamType.NON_UNIQUE) {
					if (paramType == RestSearchParameterTypeEnum.STRING) {
						nextOrValue = StringUtil.normalizeStringForSearchIndexing(nextOrValue);
					}
				}

				if (paramType == RestSearchParameterTypeEnum.TOKEN) {

					/*
					 * The gender SP indexes a fixed binding ValueSet with a single CodeSystem, so we
					 * infer the codesystem just to be friendly to clients who don't provide it
					 * in the search.
					 */
					if ("gender".equals(componentAndCorrespondingParam.getParamName())
							|| "gender".equals(componentAndCorrespondingParam.getChain())) {
						if (!nextOrValue.contains("|")) {
							nextOrValue = "http://hl7.org/fhir/administrative-gender|" + nextOrValue;
						}
					}
				}

				nextParamName = UrlUtil.escapeUrlParam(nextParamName);
				nextOrValue = UrlUtil.escapeUrlParam(nextOrValue);

				parameters.add(nextParamName + "=" + nextOrValue);
			}

			// Make sure the parameters end up in the search URL in the same order
			// we would index them in (we also alphabetically sort when we create
			// the index rows)
			Collections.sort(parameters);

			StringBuilder searchStringBuilder = new StringBuilder();
			searchStringBuilder.append(myResourceName);
			for (int i = 0; i < parameters.size(); i++) {
				if (i == 0) {
					searchStringBuilder.append("?");
				} else {
					searchStringBuilder.append("&");
				}
				searchStringBuilder.append(parameters.get(i));
			}

			String indexString = searchStringBuilder.toString();
			ourLog.debug(
					"Checking for {} combo index for query: {}", theComboParam.getComboSearchParamType(), indexString);

			indexStrings.add(indexString);
		}

		// Just to make sure we're stable for tests
		indexStrings.sort(Comparator.naturalOrder());

		// Interceptor broadcast: JPA_PERFTRACE_INFO
		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequest);
		if (compositeBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_INFO)) {
			String indexStringForLog = indexStrings.size() > 1 ? indexStrings.toString() : indexStrings.get(0);
			StorageProcessingMessage msg = new StorageProcessingMessage()
					.setMessage("Using " + theComboParam.getComboSearchParamType() + " index(es) for query for search: "
							+ indexStringForLog);
			HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(StorageProcessingMessage.class, msg);
			compositeBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_INFO, params);
		}

		switch (requireNonNull(theComboParam.getComboSearchParamType())) {
			case UNIQUE:
				theQueryStack.addPredicateCompositeUnique(indexStrings, myRequestPartitionId);
				break;
			case NON_UNIQUE:
				theQueryStack.addPredicateCompositeNonUnique(indexStrings, myRequestPartitionId);
				break;
		}

		// Remove any empty parameters remaining after this
		theParams.clean();

		return true;
	}

	/**
	 * Returns {@literal true} if the actual parameter instances in a given query are actually usable for
	 * searching against a combo param with the given parameter names. This might be {@literal false} if
	 * parameters have modifiers (e.g. <code>?name:exact=SIMPSON</code>), prefixes
	 * (e.g. <code>?date=gt2024-02-01</code>), etc.
	 */
	private boolean validateParamValuesAreValidForComboParam(
			RequestDetails theRequest,
			@Nonnull SearchParameterMap theParams,
			RuntimeSearchParam theComboParam,
			JpaParamUtil.ComponentAndCorrespondingParam theComboComponent,
			List<IQueryParameterType> theValues) {

		for (IQueryParameterType nextOrValue : theValues) {
			if (nextOrValue instanceof DateParam dateParam) {
				if (dateParam.getPrecision() != TemporalPrecisionEnum.DAY) {
					String message = "Search with params " + describeParams(theParams)
							+ " is not a candidate for combo searching - Date search with non-DAY precision for parameter '"
							+ theComboComponent.getCombinedParamName() + "'";
					firePerformanceInfo(theRequest, message);
					return false;
				}
			}

			if (nextOrValue instanceof BaseParamWithPrefix<?> paramWithPrefix) {
				ParamPrefixEnum prefix = paramWithPrefix.getPrefix();
				// A parameter with the 'eq' prefix is the only accepted prefix when combo searching since
				// birthdate=2025-01-01 and birthdate=eq2025-01-01 are equivalent searches.
				if (prefix != null && prefix != EQUAL) {
					String message = "Search with params " + describeParams(theParams)
							+ " is not a candidate for combo searching - Parameter '"
							+ theComboComponent.getCombinedParamName()
							+ "' has prefix: '"
							+ paramWithPrefix.getPrefix().getValue() + "'";
					firePerformanceInfo(theRequest, message);
					return false;
				}
			}

			// Reference params are only eligible for using a composite index if they
			// are qualified
			boolean haveChain = false;
			if (nextOrValue instanceof ReferenceParam refParam) {
				haveChain = refParam.hasChain();
				if (theComboComponent.getChain() == null && isBlank(refParam.getResourceType())) {
					String message =
							"Search is not a candidate for unique combo searching - Reference with no type specified for parameter '"
									+ theComboComponent.getCombinedParamName() + "'";
					firePerformanceInfo(theRequest, message);
					return false;
				}
			}

			// Qualifiers such as :missing can't be resolved by a combo param
			if (!haveChain && isNotBlank(nextOrValue.getQueryParameterQualifier())) {
				String message = "Search with params " + describeParams(theParams)
						+ " is not a candidate for combo searching - Parameter '"
						+ theComboComponent.getCombinedParamName()
						+ "' has modifier: '" + nextOrValue.getQueryParameterQualifier() + "'";
				firePerformanceInfo(theRequest, message);
				return false;
			}

			// Date params are not eligible for using composite unique index
			// as index could contain date with different precision (e.g. DAY, SECOND)
			if (theComboParam.getComboSearchParamType() == ComboSearchParamType.UNIQUE) {
				if (nextOrValue instanceof DateParam) {
					ourLog.debug(
							"Search with params {} is not a candidate for combo searching - "
									+ "Unique combo search parameter '{}' has DATE type",
							describeParams(theParams),
							theComboComponent);
					return false;
				}
			}
		}

		return true;
	}

	@Nonnull
	private static String describeParams(@Nonnull SearchParameterMap theParams) {
		return '[' + theParams.keySet().stream().sorted().collect(Collectors.joining(", ")) + ']';
	}

	private <T> void ensureSubListsAreWritable(@Nullable List<List<T>> theListOfLists) {
		if (theListOfLists != null) {
			for (int i = 0; i < theListOfLists.size(); i++) {
				List<T> oldSubList = theListOfLists.get(i);
				if (!(oldSubList instanceof ArrayList)) {
					List<T> newSubList = new ArrayList<>(oldSubList);
					theListOfLists.set(i, newSubList);
				}
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

	/**
	 * IncludesIterator, used to recursively fetch resources from the provided list of PIDs
	 */
	private class IncludesIterator extends BaseIterator<JpaPid> implements Iterator<JpaPid> {

		private final RequestDetails myRequest;
		private final Set<JpaPid> myCurrentPids;
		private Iterator<JpaPid> myCurrentIterator;
		private JpaPid myNext;

		IncludesIterator(Set<JpaPid> thePidSet, RequestDetails theRequest) {
			myCurrentPids = new HashSet<>(thePidSet);
			myCurrentIterator = null;
			myRequest = theRequest;
		}

		private void fetchNext() {
			while (myNext == null) {

				if (myCurrentIterator == null) {
					Set<Include> includes = new HashSet<>();
					if (myParams.containsKey(Constants.PARAM_TYPE)) {
						for (List<IQueryParameterType> typeList : myParams.get(Constants.PARAM_TYPE)) {
							for (IQueryParameterType type : typeList) {
								String queryString = ParameterUtil.unescape(type.getValueAsQueryToken());
								for (String resourceType : queryString.split(",")) {
									String rt = resourceType.trim();
									if (isNotBlank(rt)) {
										includes.add(new Include(rt + ":*", true));
									}
								}
							}
						}
					}
					if (includes.isEmpty()) {
						includes.add(new Include("*", true));
					}
					Set<JpaPid> newPids = loadIncludes(
							myContext,
							myEntityManager,
							myCurrentPids,
							includes,
							false,
							getParams().getLastUpdated(),
							mySearchUuid,
							myRequest,
							null);
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
		public JpaPid next() {
			fetchNext();
			JpaPid retVal = myNext;
			myNext = null;
			return retVal;
		}
	}
	/**
	 * Basic Query iterator, used to fetch the results of a query.
	 */
	private final class QueryIterator extends BaseIterator<JpaPid> implements IResultIterator<JpaPid> {

		private final SearchRuntimeDetails mySearchRuntimeDetails;

		private final RequestDetails myRequest;
		private final boolean myHaveRawSqlHooks;
		private final boolean myHavePerfTraceFoundIdHook;
		private final Integer myOffset;
		private final IInterceptorBroadcaster myCompositeBroadcaster;
		private boolean myFirst = true;
		private IncludesIterator myIncludesIterator;
		/**
		 * The next JpaPid value of the next result in this query.
		 * Will not be null if fetched using getNext()
		 */
		private JpaPid myNext;
		/**
		 * The current query result iterator running sql and supplying PIDs
		 * @see #myQueryList
		 */
		private ISearchQueryExecutor myResultsIterator;

		private boolean myFetchIncludesForEverythingOperation;

		/**
		 * The count of resources skipped because they were seen in earlier results
		 */
		private int mySkipCount = 0;
		/**
		 * The count of resources that are new in this search
		 * (ie, not cached in previous searches)
		 */
		private int myNonSkipCount = 0;
		/**
		 * The list of queries to use to find all results.
		 * Normal JPA queries will normally have a single entry.
		 * Queries that involve Hibernate Search/Elasticsearch may have
		 * multiple queries because of chunking.
		 * The $everything operation also jams some extra results in.
		 */
		private List<ISearchQueryExecutor> myQueryList = new ArrayList<>();

		private QueryIterator(SearchRuntimeDetails theSearchRuntimeDetails, RequestDetails theRequest) {
			mySearchRuntimeDetails = theSearchRuntimeDetails;
			myOffset = myParams.getOffset();
			myRequest = theRequest;
			myCompositeBroadcaster =
					CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequest);

			// everything requires fetching recursively all related resources
			if (myParams.getEverythingMode() != null) {
				myFetchIncludesForEverythingOperation = true;
			}

			myHavePerfTraceFoundIdHook = myCompositeBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_SEARCH_FOUND_ID);
			myHaveRawSqlHooks = myCompositeBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_RAW_SQL);
		}

		private void fetchNext() {
			try {
				if (myHaveRawSqlHooks) {
					CurrentThreadCaptureQueriesListener.startCapturing();
				}

				// If we don't have a query yet, create one
				if (myResultsIterator == null) {
					if (!mySearchProperties.hasMaxResultsRequested()) {
						mySearchProperties.setMaxResultsRequested(calculateMaxResultsToFetch());
					}

					/*
					 * assigns the results iterator
					 * and populates the myQueryList.
					 */
					initializeIteratorQuery(myOffset, mySearchProperties.getMaxResultsRequested());
				}

				if (myNext == null) {
					// no next means we need a new query (if one is available)
					while (myResultsIterator.hasNext() || !myQueryList.isEmpty()) {
						/*
						 * Because we combine our DB searches with Lucene
						 * sometimes we can have multiple results iterators
						 * (with only some having data in them to extract).
						 *
						 * We'll iterate our results iterators until we
						 * either run out of results iterators, or we
						 * have one that actually has data in it.
						 */
						while (!myResultsIterator.hasNext() && !myQueryList.isEmpty()) {
							retrieveNextIteratorQuery();
						}

						if (!myResultsIterator.hasNext()) {
							// we couldn't find a results iterator;
							// we're done here
							break;
						}

						JpaPid nextPid = myResultsIterator.next();
						if (myHavePerfTraceFoundIdHook) {
							callPerformanceTracingHook(nextPid);
						}

						if (nextPid != null) {
							if (!myPidSet.contains(nextPid)) {
								if (!mySearchProperties.isDeduplicateInDatabase()) {
									/*
									 * We only add to the map if we aren't fetching "everything";
									 * otherwise, we let the de-duplication happen in the database
									 * (see createChunkedQueryNormalSearch above), because it
									 * saves memory that way.
									 */
									myPidSet.add(nextPid);
								}
								if (doNotSkipNextPidForEverything()) {
									myNext = nextPid;
									myNonSkipCount++;
									break;
								}
							} else {
								mySkipCount++;
							}
						}

						if (!myResultsIterator.hasNext()) {
							if (mySearchProperties.hasMaxResultsRequested()
									&& (mySkipCount + myNonSkipCount == mySearchProperties.getMaxResultsRequested())) {
								if (mySkipCount > 0 && myNonSkipCount == 0) {
									sendProcessingMsgAndFirePerformanceHook();
									// need the next iterator; increase the maxsize
									// (we should always do this)
									int maxResults = mySearchProperties.getMaxResultsRequested() + 1000;
									mySearchProperties.setMaxResultsRequested(maxResults);

									if (!mySearchProperties.isDeduplicateInDatabase()) {
										// if we're not using the database to deduplicate
										// we should recheck our memory usage
										// the prefetch size check is future proofing
										int prefetchSize = myStorageSettings
												.getSearchPreFetchThresholds()
												.size();
										if (prefetchSize > 0) {
											if (myStorageSettings
															.getSearchPreFetchThresholds()
															.get(prefetchSize - 1)
													< mySearchProperties.getMaxResultsRequested()) {
												mySearchProperties.setDeduplicateInDatabase(true);
											}
										}
									}

									initializeIteratorQuery(myOffset, mySearchProperties.getMaxResultsRequested());
								}
							}
						}
					}
				}

				if (myNext == null) {
					// if we got here, it means the current JpaPid has already been processed,
					// and we will decide (here) if we need to fetch related resources recursively
					if (myFetchIncludesForEverythingOperation) {
						myIncludesIterator = new IncludesIterator(myPidSet, myRequest);
						myFetchIncludesForEverythingOperation = false;
					}
					if (myIncludesIterator != null) {
						while (myIncludesIterator.hasNext()) {
							JpaPid next = myIncludesIterator.next();
							if (next != null && myPidSet.add(next) && doNotSkipNextPidForEverything()) {
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

				if (!mySearchProperties.hasMaxResultsRequested()) {
					mySearchRuntimeDetails.setFoundIndexMatchesCount(myNonSkipCount);
				} else {
					mySearchRuntimeDetails.setFoundMatchesCount(myPidSet.size());
				}

			} finally {
				// search finished - fire hooks
				if (myHaveRawSqlHooks) {
					callRawSqlHookWithCurrentThreadQueries(myRequest, myCompositeBroadcaster);
				}
			}

			if (myFirst) {
				HookParams params = new HookParams()
						.add(RequestDetails.class, myRequest)
						.addIfMatchesType(ServletRequestDetails.class, myRequest)
						.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				myCompositeBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED, params);
				myFirst = false;
			}

			if (NO_MORE.equals(myNext)) {
				HookParams params = new HookParams()
						.add(RequestDetails.class, myRequest)
						.addIfMatchesType(ServletRequestDetails.class, myRequest)
						.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				myCompositeBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE, params);
			}
		}

		private Integer calculateMaxResultsToFetch() {
			if (myParams.isLoadSynchronous()) {
				// this might be null - we support this for streaming.
				return myParams.getLoadSynchronousUpTo();
			} else if (myParams.getOffset() != null && myParams.getCount() != null) {
				return myParams.getEverythingMode() != null
						? myParams.getOffset() + myParams.getCount()
						: myParams.getCount();
			} else {
				return myStorageSettings.getFetchSizeDefaultMaximum();
			}
		}

		private boolean doNotSkipNextPidForEverything() {
			return !(myParams.getEverythingMode() != null && (myOffset != null && myOffset >= myPidSet.size()));
		}

		private void callPerformanceTracingHook(JpaPid theNextPid) {
			HookParams params = new HookParams()
					.add(Integer.class, System.identityHashCode(this))
					.add(Object.class, theNextPid);
			myCompositeBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_SEARCH_FOUND_ID, params);
		}

		private void sendProcessingMsgAndFirePerformanceHook() {
			String msg = "Pass completed with no matching results seeking rows "
					+ myPidSet.size() + "-" + mySkipCount
					+ ". This indicates an inefficient query! Retrying with new max count of "
					+ mySearchProperties.getMaxResultsRequested();
			firePerformanceWarning(myRequest, msg);
		}

		private void initializeIteratorQuery(Integer theOffset, Integer theMaxResultsToFetch) {
			Integer offset = theOffset;
			if (myQueryList.isEmpty()) {
				// Capture times for Lucene/Elasticsearch queries as well
				mySearchRuntimeDetails.setQueryStopwatch(new StopWatch());

				// setting offset to 0 to fetch all resource ids to guarantee
				// correct output result for everything operation during paging
				if (myParams.getEverythingMode() != null) {
					offset = 0;
				}

				SearchQueryProperties properties = mySearchProperties.clone();
				properties
						.setOffset(offset)
						.setMaxResultsRequested(theMaxResultsToFetch)
						.setDoCountOnlyFlag(false)
						.setDeduplicateInDatabase(properties.isDeduplicateInDatabase() || offset != null);
				myQueryList = createQuery(myParams, properties, myRequest, mySearchRuntimeDetails);
			}

			mySearchRuntimeDetails.setQueryStopwatch(new StopWatch());

			retrieveNextIteratorQuery();

			mySkipCount = 0;
			myNonSkipCount = 0;
		}

		private void retrieveNextIteratorQuery() {
			close();
			if (isNotEmpty(myQueryList)) {
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
		public JpaPid next() {
			fetchNext();
			JpaPid retVal = myNext;
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
		public Collection<JpaPid> getNextResultBatch(long theBatchSize) {
			Collection<JpaPid> batch = new ArrayList<>();
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

	private void firePerformanceInfo(RequestDetails theRequest, String theMessage) {
		// Only log at debug level since these messages aren't considered important enough
		// that we should be cluttering the system log, but they are important to the
		// specific query being executed to we'll INFO level them there
		ourLog.debug(theMessage);
		firePerformanceMessage(theRequest, theMessage, Pointcut.JPA_PERFTRACE_INFO);
	}

	private void firePerformanceWarning(RequestDetails theRequest, String theMessage) {
		ourLog.warn(theMessage);
		firePerformanceMessage(theRequest, theMessage, Pointcut.JPA_PERFTRACE_WARNING);
	}

	private void firePerformanceMessage(RequestDetails theRequest, String theMessage, Pointcut thePointcut) {
		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequest);
		if (compositeBroadcaster.hasHooks(thePointcut)) {
			StorageProcessingMessage message = new StorageProcessingMessage();
			message.setMessage(theMessage);
			HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(StorageProcessingMessage.class, message);
			compositeBroadcaster.callHooks(thePointcut, params);
		}
	}

	public static int getMaximumPageSize() {
		if (myMaxPageSizeForTests != null) {
			return myMaxPageSizeForTests;
		}
		return MAXIMUM_PAGE_SIZE;
	}

	public static void setMaxPageSizeForTest(Integer theTestSize) {
		myMaxPageSizeForTests = theTestSize;
	}

	private static ScrollableResults<?> toScrollableResults(Query theQuery) {
		org.hibernate.query.Query<?> hibernateQuery = (org.hibernate.query.Query<?>) theQuery;
		return hibernateQuery.scroll(ScrollMode.FORWARD_ONLY);
	}
}
