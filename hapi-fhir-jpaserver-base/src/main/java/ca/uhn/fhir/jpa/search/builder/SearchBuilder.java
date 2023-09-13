/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.IJpaStorageResourceParser;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchViewDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.search.ResourceNotFoundInIndexException;
import ca.uhn.fhir.jpa.entity.ResourceSearchView;
import ca.uhn.fhir.jpa.interceptor.JpaPreResourceAccessDetails;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.IBaseResourceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.search.SearchBuilderLoadIncludesParameters;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.search.SearchConstants;
import ca.uhn.fhir.jpa.search.builder.models.ResolvedSearchQueryExecutor;
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
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.StringUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.Streams;
import com.healthmarketscience.sqlbuilder.Condition;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.UNDESIRED_RESOURCE_LINKAGES_FOR_EVERYTHING_ON_PATIENT_INSTANCE;
import static ca.uhn.fhir.jpa.search.builder.QueryStack.LOCATION_POSITION;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

	public static final int MAXIMUM_PAGE_SIZE_FOR_TESTING = 50;
	public static final String RESOURCE_ID_ALIAS = "resource_id";
	public static final String RESOURCE_VERSION_ALIAS = "resource_version";
	private static final Logger ourLog = LoggerFactory.getLogger(SearchBuilder.class);
	private static final JpaPid NO_MORE = JpaPid.fromId(-1L);
	private static final String MY_TARGET_RESOURCE_PID = "myTargetResourcePid";
	private static final String MY_SOURCE_RESOURCE_PID = "mySourceResourcePid";
	private static final String MY_TARGET_RESOURCE_TYPE = "myTargetResourceType";
	private static final String MY_SOURCE_RESOURCE_TYPE = "mySourceResourceType";
	private static final String MY_TARGET_RESOURCE_VERSION = "myTargetResourceVersion";
	public static boolean myUseMaxPageSize50ForTest = false;
	protected final IInterceptorBroadcaster myInterceptorBroadcaster;
	protected final IResourceTagDao myResourceTagDao;
	private final String myResourceName;
	private final Class<? extends IBaseResource> myResourceType;
	private final HapiFhirLocalContainerEntityManagerFactoryBean myEntityManagerFactory;
	private final SqlObjectFactory mySqlBuilderFactory;
	private final HibernatePropertiesProvider myDialectProvider;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final PartitionSettings myPartitionSettings;
	private final DaoRegistry myDaoRegistry;
	private final IResourceSearchViewDao myResourceSearchViewDao;
	private final FhirContext myContext;
	private final IIdHelperService<JpaPid> myIdHelperService;
	private final JpaStorageSettings myStorageSettings;
	private final IDao myCallingDao;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	private CriteriaBuilder myCriteriaBuilder;
	private SearchParameterMap myParams;
	private String mySearchUuid;
	private int myFetchSize;
	private Integer myMaxResultsToFetch;
	private Set<JpaPid> myPidSet;
	private boolean myHasNextIteratorQuery = false;
	private RequestPartitionId myRequestPartitionId;

	@Autowired(required = false)
	private IFulltextSearchSvc myFulltextSearchSvc;

	@Autowired(required = false)
	private IElasticsearchSvc myIElasticsearchSvc;

	@Autowired
	private IJpaStorageResourceParser myJpaStorageResourceParser;

	/**
	 * Constructor
	 */
	public SearchBuilder(
			IDao theDao,
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
			IResourceSearchViewDao theResourceSearchViewDao,
			FhirContext theContext,
			IIdHelperService theIdHelperService,
			Class<? extends IBaseResource> theResourceType) {
		myCallingDao = theDao;
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
		myResourceSearchViewDao = theResourceSearchViewDao;
		myContext = theContext;
		myIdHelperService = theIdHelperService;
	}

	@Override
	public void setMaxResultsToFetch(Integer theMaxResultsToFetch) {
		myMaxResultsToFetch = theMaxResultsToFetch;
	}

	private void searchForIdsWithAndOr(
			SearchQueryBuilder theSearchSqlBuilder,
			QueryStack theQueryStack,
			@Nonnull SearchParameterMap theParams,
			RequestDetails theRequest) {
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
			Condition predicate = theQueryStack.searchForIdsWithAndOr(
					null,
					myResourceName,
					nextParamName,
					andOrParams,
					theRequest,
					myRequestPartitionId,
					searchContainedMode);
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
		return myStorageSettings.isUniqueIndexesEnabled()
				&& myParams.getEverythingMode() == null
				&& myParams.isAllParametersHaveNoModifier();
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
	public void setPreviouslyAddedResourcePids(@Nonnull List<JpaPid> thePidSet) {
		myPidSet = new HashSet<>(thePidSet);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public IResultIterator createQuery(
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
		mySearchUuid = theSearchUuid;
		myRequestPartitionId = theRequestPartitionId;
	}

	private List<ISearchQueryExecutor> createQuery(
			SearchParameterMap theParams,
			SortSpec sort,
			Integer theOffset,
			Integer theMaximumResults,
			boolean theCountOnlyFlag,
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
				fulltextMatchIds = executeLastNAgainstIndex(theMaximumResults);
				resultCount = fulltextMatchIds.size();
			} else if (myParams.getEverythingMode() != null) {
				fulltextMatchIds = queryHibernateSearchForEverythingPids(theRequest);
				resultCount = fulltextMatchIds.size();
			} else {
				fulltextExecutor = myFulltextSearchSvc.searchNotScrolled(
						myResourceName, myParams, myMaxResultsToFetch, theRequest);
			}

			if (fulltextExecutor == null) {
				fulltextExecutor = SearchQueryExecutors.from(fulltextMatchIds);
			}

			if (theSearchRuntimeDetails != null) {
				theSearchRuntimeDetails.setFoundIndexMatchesCount(resultCount);
				HookParams params = new HookParams()
						.add(RequestDetails.class, theRequest)
						.addIfMatchesType(ServletRequestDetails.class, theRequest)
						.add(SearchRuntimeDetails.class, theSearchRuntimeDetails);
				CompositeInterceptorBroadcaster.doCallHooks(
						myInterceptorBroadcaster,
						theRequest,
						Pointcut.JPA_PERFTRACE_INDEXSEARCH_QUERY_COMPLETE,
						params);
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
				if (theMaximumResults != null) {
					fulltextExecutor = SearchQueryExecutors.limited(fulltextExecutor, theMaximumResults);
				}
				queries.add(fulltextExecutor);
			} else {
				ourLog.trace("Query needs db after HSearch.  Chunking.");
				// Finish the query in the database for the rest of the search parameters, sorting, partitioning, etc.
				// We break the pids into chunks that fit in the 1k limit for jdbc bind params.
				new QueryChunker<Long>()
						.chunk(
								Streams.stream(fulltextExecutor).collect(Collectors.toList()),
								t -> doCreateChunkedQueries(
										theParams, t, theOffset, sort, theCountOnlyFlag, theRequest, queries));
			}
		} else {
			// do everything in the database.
			createChunkedQuery(
					theParams, sort, theOffset, theMaximumResults, theCountOnlyFlag, theRequest, null, queries);
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

		// someday we'll want a query planner to figure out if we _should_ or _must_ use the ft index, not just if we
		// can.
		return fulltextEnabled
				&& myParams != null
				&& myParams.getSearchContainedMode() == SearchContainedModeEnum.FALSE
				&& myFulltextSearchSvc.supportsSomeOf(myParams);
	}

	private void failIfUsed(String theParamName) {
		if (myParams.containsKey(theParamName)) {
			throw new InvalidRequestException(Msg.code(1192)
					+ "Fulltext search is not enabled on this service, can not process parameter: " + theParamName);
		}
	}

	private List<JpaPid> executeLastNAgainstIndex(Integer theMaximumResults) {
		// Can we use our hibernate search generated index on resource to support lastN?:
		if (myStorageSettings.isAdvancedHSearchIndexing()) {
			if (myFulltextSearchSvc == null) {
				throw new InvalidRequestException(Msg.code(2027)
						+ "LastN operation is not enabled on this service, can not process this request");
			}
			return myFulltextSearchSvc.lastN(myParams, theMaximumResults).stream()
					.map(lastNResourceId -> myIdHelperService.resolveResourcePersistentIds(
							myRequestPartitionId, myResourceName, String.valueOf(lastNResourceId)))
					.collect(Collectors.toList());
		} else {
			if (myIElasticsearchSvc == null) {
				throw new InvalidRequestException(Msg.code(2033)
						+ "LastN operation is not enabled on this service, can not process this request");
			}
			// use the dedicated observation ES/Lucene index to support lastN query
			return myIElasticsearchSvc.executeLastN(myParams, myContext, theMaximumResults).stream()
					.map(lastnResourceId -> myIdHelperService.resolveResourcePersistentIds(
							myRequestPartitionId, myResourceName, lastnResourceId))
					.collect(Collectors.toList());
		}
	}

	private List<JpaPid> queryHibernateSearchForEverythingPids(RequestDetails theRequestDetails) {
		JpaPid pid = null;
		if (myParams.get(IAnyResource.SP_RES_ID) != null) {
			String idParamValue;
			IQueryParameterType idParam =
					myParams.get(IAnyResource.SP_RES_ID).get(0).get(0);
			if (idParam instanceof TokenParam) {
				TokenParam idParm = (TokenParam) idParam;
				idParamValue = idParm.getValue();
			} else {
				StringParam idParm = (StringParam) idParam;
				idParamValue = idParm.getValue();
			}

			pid = myIdHelperService.resolveResourcePersistentIds(myRequestPartitionId, myResourceName, idParamValue);
		}
		List<JpaPid> pids = myFulltextSearchSvc.everything(myResourceName, myParams, pid, theRequestDetails);
		return pids;
	}

	private void doCreateChunkedQueries(
			SearchParameterMap theParams,
			List<Long> thePids,
			Integer theOffset,
			SortSpec sort,
			boolean theCount,
			RequestDetails theRequest,
			ArrayList<ISearchQueryExecutor> theQueries) {
		if (thePids.size() < getMaximumPageSize()) {
			normalizeIdListForLastNInClause(thePids);
		}
		createChunkedQuery(theParams, sort, theOffset, thePids.size(), theCount, theRequest, thePids, theQueries);
	}

	/**
	 * Combs through the params for any _id parameters and extracts the PIDs for them
	 *
	 * @param theTargetPids
	 */
	private void extractTargetPidsFromIdParams(
			HashSet<Long> theTargetPids, List<ISearchQueryExecutor> theSearchQueryExecutors) {
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
					throw new IllegalArgumentException(
							Msg.code(1193) + "_id parameter must be a StringParam or TokenParam");
				}
			}
		}

		// fetch our target Pids
		// this will throw if an id is not found
		Map<String, JpaPid> idToPid = myIdHelperService.resolveResourcePersistentIds(
				myRequestPartitionId, myResourceName, new ArrayList<>(ids));

		// add the pids to targetPids
		for (JpaPid pid : idToPid.values()) {
			theTargetPids.add(pid.getId());
		}

		// add the target pids to our executors as the first
		// results iterator to go through
		theSearchQueryExecutors.add(new ResolvedSearchQueryExecutor(new ArrayList<>(theTargetPids)));
	}

	private void createChunkedQuery(
			SearchParameterMap theParams,
			SortSpec sort,
			Integer theOffset,
			Integer theMaximumResults,
			boolean theCountOnlyFlag,
			RequestDetails theRequest,
			List<Long> thePidList,
			List<ISearchQueryExecutor> theSearchQueryExecutors) {
		String sqlBuilderResourceName = myParams.getEverythingMode() == null ? myResourceName : null;
		SearchQueryBuilder sqlBuilder = new SearchQueryBuilder(
				myContext,
				myStorageSettings,
				myPartitionSettings,
				myRequestPartitionId,
				sqlBuilderResourceName,
				mySqlBuilderFactory,
				myDialectProvider,
				theCountOnlyFlag);
		QueryStack queryStack3 = new QueryStack(
				theParams, myStorageSettings, myContext, sqlBuilder, mySearchParamRegistry, myPartitionSettings);

		if (theParams.keySet().size() > 1
				|| theParams.getSort() != null
				|| theParams.keySet().contains(Constants.PARAM_HAS)
				|| isPotentiallyContainedReferenceParameterExistsAtRoot(theParams)) {
			List<RuntimeSearchParam> activeComboParams =
					mySearchParamRegistry.getActiveComboSearchParams(myResourceName, theParams.keySet());
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
				// will add an initial search executor for
				// _id params
				extractTargetPidsFromIdParams(targetPids, theSearchQueryExecutors);
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
						theCountOnlyFlag);
				GeneratedSql allTargetsSql = fetchPidsSqlBuilder.generate(theOffset, myMaxResultsToFetch);
				String sql = allTargetsSql.getSql();
				Object[] args = allTargetsSql.getBindVariables().toArray(new Object[0]);

				List<Long> output = jdbcTemplate.query(sql, args, new SingleColumnRowMapper<>(Long.class));

				// we add a search executor to fetch unlinked patients first
				theSearchQueryExecutors.add(new ResolvedSearchQueryExecutor(output));
			}

			List<String> typeSourceResources = new ArrayList<>();
			if (myParams.get(Constants.PARAM_TYPE) != null) {
				typeSourceResources.addAll(extractTypeSourceResourcesFromParams());
			}

			queryStack3.addPredicateEverythingOperation(
					myResourceName, typeSourceResources, targetPids.toArray(new Long[0]));
		} else {
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
			searchForIdsWithAndOr(sqlBuilder, queryStack3, myParams, theRequest);
		}

		// If we haven't added any predicates yet, we're doing a search for all resources. Make sure we add the
		// partition ID predicate in that case.
		if (!sqlBuilder.haveAtLeastOnePredicate()) {
			Condition partitionIdPredicate = sqlBuilder
					.getOrCreateResourceTablePredicateBuilder()
					.createPartitionIdPredicate(myRequestPartitionId);
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
		 * If offset is present, we want deduplicate the results by using GROUP BY
		 */
		if (theOffset != null) {
			queryStack3.addGrouping();
			queryStack3.setUseAggregate(true);
		}

		/*
		 * Sort
		 *
		 * If we have a sort, we wrap the criteria search (the search that actually
		 * finds the appropriate resources) in an outer search which is then sorted
		 */
		if (sort != null) {
			assert !theCountOnlyFlag;

			createSort(queryStack3, sort, theParams);
		}

		/*
		 * Now perform the search
		 */
		GeneratedSql generatedSql = sqlBuilder.generate(theOffset, myMaxResultsToFetch);
		if (!generatedSql.isMatchNothing()) {
			SearchQueryExecutor executor =
					mySqlBuilderFactory.newSearchQueryExecutor(generatedSql, myMaxResultsToFetch);
			theSearchQueryExecutors.add(executor);
		}
	}

	private Collection<String> extractTypeSourceResourcesFromParams() {

		List<List<IQueryParameterType>> listOfList = myParams.get(Constants.PARAM_TYPE);

		// first off, let's flatten the list of list
		List<IQueryParameterType> iQueryParameterTypesList =
				listOfList.stream().flatMap(List::stream).collect(Collectors.toList());

		// then, extract all elements of each CSV into one big list
		List<String> resourceTypes = iQueryParameterTypesList.stream()
				.map(param -> ((StringParam) param).getValue())
				.map(csvString -> List.of(csvString.split(",")))
				.flatMap(List::stream)
				.collect(Collectors.toList());

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

	private void createSort(QueryStack theQueryStack, SortSpec theSort, SearchParameterMap theParams) {
		if (theSort == null || isBlank(theSort.getParamName())) {
			return;
		}

		boolean ascending = (theSort.getOrder() == null) || (theSort.getOrder() == SortOrderEnum.ASC);

		if (IAnyResource.SP_RES_ID.equals(theSort.getParamName())) {

			theQueryStack.addSortOnResourceId(ascending);

		} else if (Constants.PARAM_LASTUPDATED.equals(theSort.getParamName())) {

			theQueryStack.addSortOnLastUpdated(ascending);

		} else {

			RuntimeSearchParam param = null;

			/*
			 * If we have a sort like _sort=subject.name and we  have an
			 * uplifted refchain for that combination we can do it more efficiently
			 * by using the index associated with the uplifted refchain. In this case,
			 * we need to find the actual target search parameter (corresponding
			 * to "name" in this example) so that we know what datatype it is.
			 */
			String paramName = theSort.getParamName();
			if (myStorageSettings.isIndexOnUpliftedRefchains()) {
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
					RuntimeSearchParam outerParam =
							mySearchParamRegistry.getActiveSearchParam(myResourceName, referenceParam);
					if (outerParam == null) {
						throwInvalidRequestExceptionForUnknownSortParameter(myResourceName, referenceParam);
					}

					if (outerParam.hasUpliftRefchain(targetParam)) {
						for (String nextTargetType : outerParam.getTargets()) {
							if (referenceParamTargetType != null && !referenceParamTargetType.equals(nextTargetType)) {
								continue;
							}
							RuntimeSearchParam innerParam =
									mySearchParamRegistry.getActiveSearchParam(nextTargetType, targetParam);
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
				param = mySearchParamRegistry.getActiveSearchParam(myResourceName, paramName);
			}

			if (param == null) {
				throwInvalidRequestExceptionForUnknownSortParameter(getResourceName(), paramName);
			}

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
							myResourceName, referenceTargetType, paramName, chainName, ascending);
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
					List<RuntimeSearchParam> compositeList =
							JpaParamUtil.resolveComponentParameters(mySearchParamRegistry, param);
					if (compositeList == null) {
						throw new InvalidRequestException(Msg.code(1195) + "The composite _sort parameter " + paramName
								+ " is not defined by the resource " + myResourceName);
					}
					if (compositeList.size() != 2) {
						throw new InvalidRequestException(Msg.code(1196) + "The composite _sort parameter " + paramName
								+ " must have 2 composite types declared in parameter annotation, found "
								+ compositeList.size());
					}
					RuntimeSearchParam left = compositeList.get(0);
					RuntimeSearchParam right = compositeList.get(1);

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
		Collection<String> validSearchParameterNames =
				mySearchParamRegistry.getValidSearchParameterNamesIncludingMeta(theResourceName);
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
			Collection<JpaPid> thePids,
			Collection<JpaPid> theIncludedPids,
			List<IBaseResource> theResourceListToPopulate,
			boolean theForHistoryOperation,
			Map<JpaPid, Integer> thePosition) {

		Map<Long, Long> resourcePidToVersion = null;
		for (JpaPid next : thePids) {
			if (next.getVersion() != null && myStorageSettings.isRespectVersionsForSearchIncludes()) {
				if (resourcePidToVersion == null) {
					resourcePidToVersion = new HashMap<>();
				}
				resourcePidToVersion.put((next).getId(), next.getVersion());
			}
		}

		List<Long> versionlessPids = JpaPid.toLongList(thePids);
		if (versionlessPids.size() < getMaximumPageSize()) {
			versionlessPids = normalizeIdListForLastNInClause(versionlessPids);
		}

		// -- get the resource from the searchView
		Collection<ResourceSearchView> resourceSearchViewList =
				myResourceSearchViewDao.findByResourceIds(versionlessPids);

		// -- preload all tags with tag definition if any
		Map<Long, Collection<ResourceTag>> tagMap = getResourceTagMap(resourceSearchViewList);

		for (IBaseResourceEntity next : resourceSearchViewList) {
			if (next.getDeleted() != null) {
				continue;
			}

			Class<? extends IBaseResource> resourceType =
					myContext.getResourceDefinition(next.getResourceType()).getImplementingClass();

			JpaPid resourceId = JpaPid.fromId(next.getResourceId());

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
					next = (IBaseResourceEntity)
							dao.readEntity(next.getIdDt().withVersion(Long.toString(version)), null);
				}
			}

			IBaseResource resource = null;
			if (next != null) {
				resource = myJpaStorageResourceParser.toResource(
						resourceType, next, tagMap.get(next.getId()), theForHistoryOperation);
			}
			if (resource == null) {
				ourLog.warn(
						"Unable to find resource {}/{}/_history/{} in database",
						next.getResourceType(),
						next.getIdDt().getIdPart(),
						next.getVersion());
				continue;
			}

			Integer index = thePosition.get(resourceId);
			if (index == null) {
				ourLog.warn("Got back unexpected resource PID {}", resourceId);
				continue;
			}

			if (theIncludedPids.contains(resourceId)) {
				ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(resource, BundleEntrySearchModeEnum.INCLUDE);
			} else {
				ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(resource, BundleEntrySearchModeEnum.MATCH);
			}

			theResourceListToPopulate.set(index, resource);
		}
	}

	private Map<Long, Collection<ResourceTag>> getResourceTagMap(
			Collection<? extends IBaseResourceEntity> theResourceSearchViewList) {

		List<Long> idList = new ArrayList<>(theResourceSearchViewList.size());

		// -- find all resource has tags
		for (IBaseResourceEntity resource : theResourceSearchViewList) {
			if (resource.isHasTags()) idList.add(resource.getId());
		}

		return getPidToTagMap(idList);
	}

	@Nonnull
	private Map<Long, Collection<ResourceTag>> getPidToTagMap(List<Long> thePidList) {
		Map<Long, Collection<ResourceTag>> tagMap = new HashMap<>();

		// -- no tags
		if (thePidList.size() == 0) return tagMap;

		// -- get all tags for the idList
		Collection<ResourceTag> tagList = myResourceTagDao.findByResourceIds(thePidList);

		// -- build the map, key = resourceId, value = list of ResourceTag
		JpaPid resourceId;
		Collection<ResourceTag> tagCol;
		for (ResourceTag tag : tagList) {

			resourceId = JpaPid.fromId(tag.getResourceId());
			tagCol = tagMap.get(resourceId.getId());
			if (tagCol == null) {
				tagCol = new ArrayList<>();
				tagCol.add(tag);
				tagMap.put(resourceId.getId(), tagCol);
			} else {
				tagCol.add(tag);
			}
		}

		return tagMap;
	}

	@Override
	public void loadResourcesByPid(
			Collection<JpaPid> thePids,
			Collection<JpaPid> theIncludedPids,
			List<IBaseResource> theResourceListToPopulate,
			boolean theForHistoryOperation,
			RequestDetails theDetails) {
		if (thePids.isEmpty()) {
			ourLog.debug("The include pids are empty");
			// return;
		}

		// Dupes will cause a crash later anyhow, but this is expensive so only do it
		// when running asserts
		assert new HashSet<>(thePids).size() == thePids.size() : "PID list contains duplicates: " + thePids;

		Map<JpaPid, Integer> position = new HashMap<>();
		for (JpaPid next : thePids) {
			position.put(next, theResourceListToPopulate.size());
			theResourceListToPopulate.add(null);
		}

		// Can we fast track this loading by checking elastic search?
		if (isLoadingFromElasticSearchSupported(thePids)) {
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
		new QueryChunker<JpaPid>()
				.chunk(
						thePids,
						t -> doLoadPids(
								t, theIncludedPids, theResourceListToPopulate, theForHistoryOperation, position));
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
				&& myStorageSettings.isAdvancedHSearchIndexing()
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
		if (myStorageSettings.isAdvancedHSearchIndexing() && myStorageSettings.isStoreResourceInHSearchIndex()) {
			List<Long> pidList = thePids.stream().map(pid -> (pid).getId()).collect(Collectors.toList());

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
		DateRangeParam lastUpdated = theParameters.getLastUpdated();
		RequestDetails request = theParameters.getRequestDetails();
		String searchIdOrDescription = theParameters.getSearchIdOrDescription();
		List<String> desiredResourceTypes = theParameters.getDesiredResourceTypes();
		boolean hasDesiredResourceTypes = desiredResourceTypes != null && !desiredResourceTypes.isEmpty();
		if (CompositeInterceptorBroadcaster.hasHooks(
				Pointcut.JPA_PERFTRACE_RAW_SQL, myInterceptorBroadcaster, theParameters.getRequestDetails())) {
			CurrentThreadCaptureQueriesListener.startCapturing();
		}
		if (matches.size() == 0) {
			return new HashSet<>();
		}
		if (currentIncludes == null || currentIncludes.isEmpty()) {
			return new HashSet<>();
		}
		String searchPidFieldName = reverseMode ? MY_TARGET_RESOURCE_PID : MY_SOURCE_RESOURCE_PID;
		String findPidFieldName = reverseMode ? MY_SOURCE_RESOURCE_PID : MY_TARGET_RESOURCE_PID;
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
					sqlBuilder.append(", r.").append(findResourceTypeFieldName);
					if (findVersionFieldName != null) {
						sqlBuilder.append(", r.").append(findVersionFieldName);
					}
					sqlBuilder.append(" FROM ResourceLink r WHERE ");

					sqlBuilder.append("r.");
					sqlBuilder.append(searchPidFieldName); // (rev mode) target_resource_id | source_resource_id
					sqlBuilder.append(" IN (:target_pids)");

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
					if (wantResourceType != null
							&& (reverseMode || (myParams != null && myParams.getEverythingMode() != null))) {
						// because mySourceResourceType is not part of the HFJ_RES_LINK
						// index, this might not be the most optimal performance.
						// but it is for an $everything operation (and maybe we should update the index)
						sqlBuilder.append(" AND r.mySourceResourceType = :want_resource_type");
					} else {
						wantResourceType = null;
					}

					// When calling $everything on a Patient instance, we don't want to recurse into new Patient
					// resources
					// (e.g. via Provenance, List, or Group) when in an $everything operation
					if (myParams != null
							&& myParams.getEverythingMode() == SearchParameterMap.EverythingModeEnum.PATIENT_INSTANCE) {
						sqlBuilder.append(" AND r.myTargetResourceType != 'Patient'");
						sqlBuilder.append(UNDESIRED_RESOURCE_LINKAGES_FOR_EVERYTHING_ON_PATIENT_INSTANCE.stream()
								.collect(Collectors.joining("', '", " AND r.mySourceResourceType NOT IN ('", "')")));
					}
					if (hasDesiredResourceTypes) {
						sqlBuilder.append(" AND r.myTargetResourceType IN (:desired_target_resource_types)");
					}

					String sql = sqlBuilder.toString();
					List<Collection<JpaPid>> partitions = partition(nextRoundMatches, getMaximumPageSize());
					for (Collection<JpaPid> nextPartition : partitions) {
						TypedQuery<?> q = entityManager.createQuery(sql, Object[].class);
						q.setParameter("target_pids", JpaPid.toLongList(nextPartition));
						if (wantResourceType != null) {
							q.setParameter("want_resource_type", wantResourceType);
						}
						if (maxCount != null) {
							q.setMaxResults(maxCount);
						}
						if (hasDesiredResourceTypes) {
							q.setParameter("desired_target_resource_types", String.join(", ", desiredResourceTypes));
						}
						List<?> results = q.getResultList();
						for (Object nextRow : results) {
							if (nextRow == null) {
								// This can happen if there are outgoing references which are canonical or point to
								// other servers
								continue;
							}

							Long version = null;
							Long resourceLink = (Long) ((Object[]) nextRow)[0];
							String resourceType = (String) ((Object[]) nextRow)[1];
							if (findVersionFieldName != null) {
								version = (Long) ((Object[]) nextRow)[2];
							}

							if (resourceLink != null) {
								JpaPid pid =
										JpaPid.fromIdAndVersionAndResourceType(resourceLink, version, resourceType);
								pidsToInclude.add(pid);
							}
						}
					}
				} else {
					List<String> paths;

					// Start replace
					RuntimeSearchParam param;
					String resType = nextInclude.getParamType();
					if (isBlank(resType)) {
						continue;
					}
					RuntimeResourceDefinition def = fhirContext.getResourceDefinition(resType);
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
					// end replace

					Set<String> targetResourceTypes = computeTargetResourceTypes(nextInclude, param);

					for (String nextPath : paths) {
						String findPidFieldSqlColumn = findPidFieldName.equals(MY_SOURCE_RESOURCE_PID)
								? "src_resource_id"
								: "target_resource_id";
						String fieldsToLoad = "r." + findPidFieldSqlColumn + " AS " + RESOURCE_ID_ALIAS;
						if (findVersionFieldName != null) {
							fieldsToLoad += ", r.target_resource_version AS " + RESOURCE_VERSION_ALIAS;
						}

						// Query for includes lookup has 2 cases
						// Case 1: Where target_resource_id is available in hfj_res_link table for local references
						// Case 2: Where target_resource_id is null in hfj_res_link table and referred by a canonical
						// url in target_resource_url

						// Case 1:
						Map<String, Object> localReferenceQueryParams = new HashMap<>();

						String searchPidFieldSqlColumn = searchPidFieldName.equals(MY_TARGET_RESOURCE_PID)
								? "target_resource_id"
								: "src_resource_id";
						StringBuilder localReferenceQuery =
								new StringBuilder("SELECT " + fieldsToLoad + " FROM hfj_res_link r "
										+ " WHERE r.src_path = :src_path AND "
										+ " r.target_resource_id IS NOT NULL AND "
										+ " r."
										+ searchPidFieldSqlColumn + " IN (:target_pids) ");
						localReferenceQueryParams.put("src_path", nextPath);
						// we loop over target_pids later.
						if (targetResourceTypes != null) {
							if (targetResourceTypes.size() == 1) {
								localReferenceQuery.append(" AND r.target_resource_type = :target_resource_type ");
								localReferenceQueryParams.put(
										"target_resource_type",
										targetResourceTypes.iterator().next());
							} else {
								localReferenceQuery.append(" AND r.target_resource_type in (:target_resource_types) ");
								localReferenceQueryParams.put("target_resource_types", targetResourceTypes);
							}
						}

						// Case 2:
						Pair<String, Map<String, Object>> canonicalQuery = buildCanonicalUrlQuery(
								findVersionFieldName, searchPidFieldSqlColumn, targetResourceTypes);

						// @formatter:on

						String sql = localReferenceQuery + " UNION " + canonicalQuery.getLeft();

						List<Collection<JpaPid>> partitions = partition(nextRoundMatches, getMaximumPageSize());
						for (Collection<JpaPid> nextPartition : partitions) {
							Query q = entityManager.createNativeQuery(sql, Tuple.class);
							q.setParameter("target_pids", JpaPid.toLongList(nextPartition));
							localReferenceQueryParams.forEach(q::setParameter);
							canonicalQuery.getRight().forEach(q::setParameter);

							if (maxCount != null) {
								q.setMaxResults(maxCount);
							}
							@SuppressWarnings("unchecked")
							List<Tuple> results = q.getResultList();
							for (Tuple result : results) {
								if (result != null) {
									Long resourceId =
											NumberUtils.createLong(String.valueOf(result.get(RESOURCE_ID_ALIAS)));
									Long resourceVersion = null;
									if (findVersionFieldName != null && result.get(RESOURCE_VERSION_ALIAS) != null) {
										resourceVersion = NumberUtils.createLong(
												String.valueOf(result.get(RESOURCE_VERSION_ALIAS)));
									}
									pidsToInclude.add(JpaPid.fromIdAndVersion(resourceId, resourceVersion));
								}
							}
						}
					}
				}
			}

			nextRoundMatches.clear();
			for (JpaPid next : pidsToInclude) {
				if (!original.contains(next) && !allAdded.contains(next)) {
					nextRoundMatches.add(next);
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

		if (CompositeInterceptorBroadcaster.hasHooks(
				Pointcut.JPA_PERFTRACE_RAW_SQL, myInterceptorBroadcaster, request)) {
			callRawSqlHookWithCurrentThreadQueries(request);
		}
		// Interceptor call: STORAGE_PREACCESS_RESOURCES
		// This can be used to remove results from the search result details before
		// the user has a chance to know that they were in the results
		if (!allAdded.isEmpty()) {

			if (CompositeInterceptorBroadcaster.hasHooks(
					Pointcut.STORAGE_PREACCESS_RESOURCES, myInterceptorBroadcaster, request)) {
				List<JpaPid> includedPidList = new ArrayList<>(allAdded);
				JpaPreResourceAccessDetails accessDetails =
						new JpaPreResourceAccessDetails(includedPidList, () -> this);
				HookParams params = new HookParams()
						.add(IPreResourceAccessDetails.class, accessDetails)
						.add(RequestDetails.class, request)
						.addIfMatchesType(ServletRequestDetails.class, request);
				CompositeInterceptorBroadcaster.doCallHooks(
						myInterceptorBroadcaster, request, Pointcut.STORAGE_PREACCESS_RESOURCES, params);

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

	/**
	 * Given a
	 * @param request
	 */
	private void callRawSqlHookWithCurrentThreadQueries(RequestDetails request) {
		SqlQueryList capturedQueries = CurrentThreadCaptureQueriesListener.getCurrentQueueAndStopCapturing();
		HookParams params = new HookParams()
				.add(RequestDetails.class, request)
				.addIfMatchesType(ServletRequestDetails.class, request)
				.add(SqlQueryList.class, capturedQueries);
		CompositeInterceptorBroadcaster.doCallHooks(
				myInterceptorBroadcaster, request, Pointcut.JPA_PERFTRACE_RAW_SQL, params);
	}

	@Nullable
	private static Set<String> computeTargetResourceTypes(Include nextInclude, RuntimeSearchParam param) {
		String targetResourceType = defaultString(nextInclude.getParamTargetType(), null);
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

	@Nonnull
	private Pair<String, Map<String, Object>> buildCanonicalUrlQuery(
			String theVersionFieldName, String thePidFieldSqlColumn, Set<String> theTargetResourceTypes) {
		String fieldsToLoadFromSpidxUriTable = "rUri.res_id";
		if (theVersionFieldName != null) {
			// canonical-uri references aren't versioned, but we need to match the column count for the UNION
			fieldsToLoadFromSpidxUriTable += ", NULL";
		}
		// The logical join will be by hfj_spidx_uri on sp_name='uri' and sp_uri=target_resource_url.
		// But sp_name isn't indexed, so we use hash_identity instead.
		if (theTargetResourceTypes == null) {
			// hash_identity includes the resource type.  So a null wildcard must be replaced with a list of all types.
			theTargetResourceTypes = myDaoRegistry.getRegisteredDaoTypes();
		}
		assert !theTargetResourceTypes.isEmpty();

		Set<Long> identityHashesForTypes = theTargetResourceTypes.stream()
				.map(type -> BaseResourceIndexedSearchParam.calculateHashIdentity(
						myPartitionSettings, myRequestPartitionId, type, "url"))
				.collect(Collectors.toSet());

		Map<String, Object> canonicalUriQueryParams = new HashMap<>();
		StringBuilder canonicalUrlQuery = new StringBuilder(
				"SELECT " + fieldsToLoadFromSpidxUriTable + " FROM hfj_res_link r " + " JOIN hfj_spidx_uri rUri ON ( ");
		// join on hash_identity and sp_uri - indexed in IDX_SP_URI_HASH_IDENTITY_V2
		if (theTargetResourceTypes.size() == 1) {
			canonicalUrlQuery.append("   rUri.hash_identity = :uri_identity_hash ");
			canonicalUriQueryParams.put(
					"uri_identity_hash", identityHashesForTypes.iterator().next());
		} else {
			canonicalUrlQuery.append("   rUri.hash_identity in (:uri_identity_hashes) ");
			canonicalUriQueryParams.put("uri_identity_hashes", identityHashesForTypes);
		}

		canonicalUrlQuery.append("  AND r.target_resource_url = rUri.sp_uri  )" + " WHERE r.src_path = :src_path AND "
				+ " r.target_resource_id IS NULL AND "
				+ " r."
				+ thePidFieldSqlColumn + " IN (:target_pids) ");
		return Pair.of(canonicalUrlQuery.toString(), canonicalUriQueryParams);
	}

	private List<Collection<JpaPid>> partition(Collection<JpaPid> theNextRoundMatches, int theMaxLoad) {
		if (theNextRoundMatches.size() <= theMaxLoad) {
			return Collections.singletonList(theNextRoundMatches);
		} else {

			List<Collection<JpaPid>> retVal = new ArrayList<>();
			Collection<JpaPid> current = null;
			for (JpaPid next : theNextRoundMatches) {
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

	private void attemptComboUniqueSpProcessing(
			QueryStack theQueryStack3, @Nonnull SearchParameterMap theParams, RequestDetails theRequest) {
		RuntimeSearchParam comboParam = null;
		List<String> comboParamNames = null;
		List<RuntimeSearchParam> exactMatchParams =
				mySearchParamRegistry.getActiveComboSearchParams(myResourceName, theParams.keySet());
		if (exactMatchParams.size() > 0) {
			comboParam = exactMatchParams.get(0);
			comboParamNames = new ArrayList<>(theParams.keySet());
		}

		if (comboParam == null) {
			List<RuntimeSearchParam> candidateComboParams =
					mySearchParamRegistry.getActiveComboSearchParams(myResourceName);
			for (RuntimeSearchParam nextCandidate : candidateComboParams) {
				List<String> nextCandidateParamNames =
						JpaParamUtil.resolveComponentParameters(mySearchParamRegistry, nextCandidate).stream()
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
					ourLog.error(
							"query parameter {} is unexpectedly empty. Encountered while considering {} index for {}",
							nextParamName,
							comboParam.getName(),
							theRequest.getCompleteUrl());
					sb = null;
					break;
				}

				if (nextValues.get(0).size() != 1) {
					sb = null;
					break;
				}

				// Reference params are only eligible for using a composite index if they
				// are qualified
				RuntimeSearchParam nextParamDef =
						mySearchParamRegistry.getActiveSearchParam(myResourceName, nextParamName);
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
				ourLog.debug(
						"Checking for {} combo index for query: {}", comboParam.getComboSearchParamType(), indexString);

				// Interceptor broadcast: JPA_PERFTRACE_INFO
				StorageProcessingMessage msg = new StorageProcessingMessage()
						.setMessage("Using " + comboParam.getComboSearchParamType() + " index for query for search: "
								+ indexString);
				HookParams params = new HookParams()
						.add(RequestDetails.class, theRequest)
						.addIfMatchesType(ServletRequestDetails.class, theRequest)
						.add(StorageProcessingMessage.class, msg);
				CompositeInterceptorBroadcaster.doCallHooks(
						myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_INFO, params);

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

	/**
	 * IncludesIterator, used to recursively fetch resources from the provided list of PIDs
	 */
	public class IncludesIterator extends BaseIterator<JpaPid> implements Iterator<JpaPid> {

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
								String queryString = ParameterUtil.unescape(type.getValueAsQueryToken(myContext));
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
		private final SortSpec mySort;
		private final Integer myOffset;
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
		 * Queries that involve Hibernate Search/Elastisearch may have
		 * multiple queries because of chunking.
		 * The $everything operation also jams some extra results in.
		 */
		private List<ISearchQueryExecutor> myQueryList = new ArrayList<>();

		private QueryIterator(SearchRuntimeDetails theSearchRuntimeDetails, RequestDetails theRequest) {
			mySearchRuntimeDetails = theSearchRuntimeDetails;
			mySort = myParams.getSort();
			myOffset = myParams.getOffset();
			myRequest = theRequest;

			// everything requires fetching recursively all related resources
			if (myParams.getEverythingMode() != null) {
				myFetchIncludesForEverythingOperation = true;
			}

			myHavePerfTraceFoundIdHook = CompositeInterceptorBroadcaster.hasHooks(
					Pointcut.JPA_PERFTRACE_SEARCH_FOUND_ID, myInterceptorBroadcaster, myRequest);
			myHaveRawSqlHooks = CompositeInterceptorBroadcaster.hasHooks(
					Pointcut.JPA_PERFTRACE_RAW_SQL, myInterceptorBroadcaster, myRequest);
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
							myMaxResultsToFetch = myStorageSettings.getFetchSizeDefaultMaximum();
						}
					}

					/*
					 * assigns the results iterator
					 * and populates the myQueryList.
					 */
					initializeIteratorQuery(myOffset, myMaxResultsToFetch);
				}

				if (myNext == null) {
					// no next means we need a new query (if one is available)
					while (myResultsIterator.hasNext() || !myQueryList.isEmpty()) {
						// Update iterator with next chunk if necessary.
						if (!myResultsIterator.hasNext()) {
							retrieveNextIteratorQuery();

							// if our new results iterator is also empty
							// we're done here
							if (!myResultsIterator.hasNext()) {
								break;
							}
						}

						Long nextLong = myResultsIterator.next();
						if (myHavePerfTraceFoundIdHook) {
							HookParams params = new HookParams()
									.add(Integer.class, System.identityHashCode(this))
									.add(Object.class, nextLong);
							CompositeInterceptorBroadcaster.doCallHooks(
									myInterceptorBroadcaster,
									myRequest,
									Pointcut.JPA_PERFTRACE_SEARCH_FOUND_ID,
									params);
						}

						if (nextLong != null) {
							JpaPid next = JpaPid.fromId(nextLong);
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

									sendProcessingMsgAndFirePerformanceHook();

									myMaxResultsToFetch += 1000;
									initializeIteratorQuery(myOffset, myMaxResultsToFetch);
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

				mySearchRuntimeDetails.setFoundMatchesCount(myPidSet.size());

			} finally {
				// search finished - fire hooks
				if (myHaveRawSqlHooks) {
					callRawSqlHookWithCurrentThreadQueries(myRequest);
				}
			}

			if (myFirst) {
				HookParams params = new HookParams()
						.add(RequestDetails.class, myRequest)
						.addIfMatchesType(ServletRequestDetails.class, myRequest)
						.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				CompositeInterceptorBroadcaster.doCallHooks(
						myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED, params);
				myFirst = false;
			}

			if (NO_MORE.equals(myNext)) {
				HookParams params = new HookParams()
						.add(RequestDetails.class, myRequest)
						.addIfMatchesType(ServletRequestDetails.class, myRequest)
						.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				CompositeInterceptorBroadcaster.doCallHooks(
						myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE, params);
			}
		}

		private void sendProcessingMsgAndFirePerformanceHook() {
			StorageProcessingMessage message = new StorageProcessingMessage();
			String msg = "Pass completed with no matching results seeking rows "
					+ myPidSet.size() + "-" + mySkipCount
					+ ". This indicates an inefficient query! Retrying with new max count of "
					+ myMaxResultsToFetch;
			ourLog.warn(msg);
			message.setMessage(msg);
			HookParams params = new HookParams()
					.add(RequestDetails.class, myRequest)
					.addIfMatchesType(ServletRequestDetails.class, myRequest)
					.add(StorageProcessingMessage.class, message);
			CompositeInterceptorBroadcaster.doCallHooks(
					myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_WARNING, params);
		}

		private void initializeIteratorQuery(Integer theOffset, Integer theMaxResultsToFetch) {
			if (myQueryList.isEmpty()) {
				// Capture times for Lucene/Elasticsearch queries as well
				mySearchRuntimeDetails.setQueryStopwatch(new StopWatch());
				myQueryList = createQuery(
						myParams, mySort, theOffset, theMaxResultsToFetch, false, myRequest, mySearchRuntimeDetails);
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
}
