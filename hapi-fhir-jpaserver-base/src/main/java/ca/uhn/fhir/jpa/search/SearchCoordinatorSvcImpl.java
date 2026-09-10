/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
 * #L%family
 */
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.NonPersistedSearch;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.search.ResourceNotFoundInIndexException;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.builder.StorageInterceptorHooksFacade;
import ca.uhn.fhir.jpa.search.exec.ICacheAwareJpaSearchSvc;
import ca.uhn.fhir.jpa.search.exec.IStatelessJpaSearchSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.util.QueryParameterUtils.DEFAULT_SYNC_SIZE;
import static org.apache.commons.lang3.ObjectUtils.getIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component("mySearchCoordinatorSvc")
public class SearchCoordinatorSvcImpl implements ISearchCoordinatorSvc<JpaPid> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchCoordinatorSvcImpl.class);

	@Autowired
	private FhirContext myContext;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private ICacheAwareJpaSearchSvc myCacheAwareSearchSvc;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private SearchBuilderFactory<JpaPid> mySearchBuilderFactory;

	@Autowired
	private IStatelessJpaSearchSvc myStatelessSearchSvc;

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	@Autowired
	private SearchStrategyFactory mySearchStrategyFactory;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	private StorageInterceptorHooksFacade myStorageInterceptorHooks;
	private int mySyncSize = DEFAULT_SYNC_SIZE;
	private IPagingProvider myPagingProvider;

	/**
	 * Constructor
	 */
	public SearchCoordinatorSvcImpl() {
		super();
	}

	/**
	 * Unit test constructor
	 */
	public SearchCoordinatorSvcImpl(
			FhirContext theContext,
			JpaStorageSettings theStorageSettings,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			DaoRegistry theDaoRegistry,
			SearchBuilderFactory<JpaPid> theSearchBuilderFactory,
			IStatelessJpaSearchSvc theStatelessSearchSvc,
			ICacheAwareJpaSearchSvc theCacheAwareSearchSvc,
			SearchStrategyFactory theSearchStrategyFactory,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc,
			IPagingProvider thePagingProvider) {
		super();
		myContext = theContext;
		myStorageSettings = theStorageSettings;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		mySearchBuilderFactory = theSearchBuilderFactory;
		myStatelessSearchSvc = theStatelessSearchSvc;
		mySearchStrategyFactory = theSearchStrategyFactory;
		myDaoRegistry = theDaoRegistry;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
		myCacheAwareSearchSvc = theCacheAwareSearchSvc;
		myPagingProvider = thePagingProvider;
		start();
	}

	@PostConstruct
	public void start() {
		myStorageInterceptorHooks = new StorageInterceptorHooksFacade(myInterceptorBroadcaster);
	}

	@VisibleForTesting
	public void setSyncSizeForUnitTests(int theSyncSize) {
		mySyncSize = theSyncSize;
	}

	@Override
	public IBundleProvider createNewSearch(
			final IFhirResourceDao<?> theCallingDao,
			final SearchParameterMap theParams,
			String theResourceType,
			CacheControlDirective theCacheControlDirective,
			@Nullable RequestDetails theRequestDetails) {
		final String searchUuid = UUID.randomUUID().toString();

		final String queryString = theParams.toNormalizedQueryString();
		ourLog.debug("Registering new search {}", searchUuid);

		// Invoke any STORAGE_PRESEARCH_PARTITION_SELECTED interceptor hooks
		NonPersistedSearch nonPersistedSearch = new NonPersistedSearch(theResourceType);
		nonPersistedSearch.setUuid(searchUuid);
		myStorageInterceptorHooks.callStoragePresearchPartitionSelected(
				theRequestDetails, theParams, nonPersistedSearch);

		RequestPartitionId requestPartitionId = null;
		if (theRequestDetails instanceof SystemRequestDetails srd) {
			requestPartitionId = srd.getRequestPartitionId();
		}

		// If an explicit request partition wasn't provided, calculate the request
		// partition after invoking STORAGE_PRESEARCH_REGISTERED just in case any interceptors
		// made changes which could affect the calculated partition
		if (requestPartitionId == null) {
			requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(
					theRequestDetails, theResourceType, theParams);
		}

		Search search = new Search();
		QueryParameterUtils.populateSearchEntity(
				theParams, theResourceType, searchUuid, queryString, search, requestPartitionId, myPagingProvider);

		// Invoke any STORAGE_PRESEARCH_REGISTERED interceptor hooks
		myStorageInterceptorHooks.callStoragePresearchRegistered(
				theRequestDetails, theParams, search, requestPartitionId);

		validateSearch(theParams);

		Class<? extends IBaseResource> resourceTypeClass =
				myContext.getResourceDefinition(theResourceType).getImplementingClass();
		final ISearchBuilder<JpaPid> sb = mySearchBuilderFactory.newSearchBuilder(theResourceType, resourceTypeClass);
		sb.setFetchSize(mySyncSize);
		sb.setRequireTotal(theParams.getCount() != null);

		final Integer loadSynchronousUpTo = getLoadSynchronousUpToOrNull(theCacheControlDirective);
		boolean isOffsetQuery = theParams.isOffsetQuery();

		// todo someday - not today.
		//		SearchStrategyFactory.ISearchStrategy searchStrategy = mySearchStrategyFactory.pickStrategy(theResourceType,
		// theParams, theRequestDetails);
		//		return searchStrategy.get();

		if (theParams.isLoadSynchronous() || loadSynchronousUpTo != null || isOffsetQuery) {
			if (mySearchStrategyFactory.isSupportsHSearchDirect(theResourceType, theParams, theRequestDetails)) {
				ourLog.info("Search {} is using direct load strategy", searchUuid);
				SearchStrategyFactory.ISearchStrategy direct = mySearchStrategyFactory.makeDirectStrategy(
						searchUuid, theResourceType, theParams, theRequestDetails);

				try {
					return direct.get();
				} catch (ResourceNotFoundInIndexException theE) {
					// some resources were not found in index, so we will inform this and resort to JPA search
					ourLog.warn(
							"Some resources were not found in index. Make sure all resources were indexed. Resorting to database search.");
				}
			}

			// we need a max to fetch for synchronous searches;
			// otherwise we'll explode memory.
			Integer maxToLoad = getSynchronousMaxResultsToFetch(theParams, loadSynchronousUpTo);
			ourLog.debug("Setting a max fetch value of {} for synchronous search", maxToLoad);
			sb.setMaxResultsToFetch(maxToLoad);

			ourLog.debug("Search {} is loading in synchronous mode", searchUuid);
			return myStatelessSearchSvc.createNewSearch(
					theParams, theRequestDetails, searchUuid, sb, loadSynchronousUpTo, requestPartitionId);
		}

		return myCacheAwareSearchSvc.createNewSearch(
				theParams, theRequestDetails, theCacheControlDirective, search, sb, requestPartitionId);
	}

	@Override
	public IBundleProvider continueExistingSearch(String theSearchUuid, @Nullable RequestDetails theRequestDetails) {
		return myCacheAwareSearchSvc.continueExistingSearch(theSearchUuid, theRequestDetails);
	}

	/**
	 * The max results to return if this is a synchronous search.
	 * <p>
	 * We'll look in this order:
	 * * load synchronous up to (on params)
	 * * param count (+ offset)
	 * * StorageSettings fetch size default max
	 * *
	 */
	private Integer getSynchronousMaxResultsToFetch(SearchParameterMap theParams, Integer theLoadSynchronousUpTo) {
		if (theLoadSynchronousUpTo != null) {
			return theLoadSynchronousUpTo;
		}

		if (theParams.getCount() != null) {
			int valToReturn = theParams.getCount() + 1;
			if (theParams.getOffset() != null) {
				valToReturn += theParams.getOffset();
			}
			return valToReturn;
		}

		if (myStorageSettings.getFetchSizeDefaultMaximum() != null) {
			return myStorageSettings.getFetchSizeDefaultMaximum();
		}

		return myStorageSettings.getInternalSynchronousSearchSize();
	}

	private void validateSearch(SearchParameterMap theParams) {
		/*
		 * Having duplicate identical params in the search (e.g. Patient?gender=male&gender=male) is not
		 * technically wrong, but it's inefficient and can slow query execution down. Checking for it also
		 * adds CPU load itself though, so we only check this in an assert to hopefully catch errors in tests.
		 */
		assert checkNoDuplicateParameters(theParams)
				: "Duplicate parameters found in query: " + theParams.toNormalizedQueryString();

		validateIncludes(theParams.getIncludes(), Constants.PARAM_INCLUDE);
		validateIncludes(theParams.getRevIncludes(), Constants.PARAM_REVINCLUDE);
	}

	/**
	 * This method detects whether we have any duplicate lists of parameters and returns
	 * {@literal true} if none are found. For example, the following query would result
	 * in this method returning {@literal false}:
	 * <code>Patient?name=bart,homer&name=bart,homer</code>
	 * <p>
	 * This is not an optimized test, and it's not technically even prohibited to have
	 * duplicates like these in queries so this method should only be called as a
	 * part of an {@literal assert} statement to catch errors in tests.
	 */
	private boolean checkNoDuplicateParameters(SearchParameterMap theParams) {
		HashSet<List<IQueryParameterType>> lists = new HashSet<>();
		for (List<List<IQueryParameterType>> andList : theParams.values()) {

			lists.clear();
			for (List<IQueryParameterType> orListI : andList) {
				if (!orListI.isEmpty() && !lists.add(orListI)) {
					return false;
				}
			}
		}
		return true;
	}

	private void validateIncludes(Set<Include> includes, String name) {
		for (Include next : includes) {
			String value = next.getValue();
			if (value.equals(Constants.INCLUDE_STAR) || isBlank(value)) {
				continue;
			}

			String paramType = next.getParamType();
			String paramName = next.getParamName();
			String paramTargetType = next.getParamTargetType();

			if (isBlank(paramType) || isBlank(paramName)) {
				String msg = myContext
						.getLocalizer()
						.getMessageSanitized(SearchCoordinatorSvcImpl.class, "invalidInclude", name, value, "");
				throw new InvalidRequestException(Msg.code(2018) + msg);
			}

			if (!myDaoRegistry.isResourceTypeSupported(paramType)) {
				String resourceTypeMsg = myContext
						.getLocalizer()
						.getMessageSanitized(SearchCoordinatorSvcImpl.class, "invalidResourceType", paramType);
				String msg = myContext
						.getLocalizer()
						.getMessage(
								SearchCoordinatorSvcImpl.class,
								"invalidInclude",
								UrlUtil.sanitizeUrlPart(name),
								UrlUtil.sanitizeUrlPart(value),
								resourceTypeMsg); // last param is pre-sanitized
				throw new InvalidRequestException(Msg.code(2017) + msg);
			}

			if (isNotBlank(paramTargetType) && !myDaoRegistry.isResourceTypeSupported(paramTargetType)) {
				String resourceTypeMsg = myContext
						.getLocalizer()
						.getMessageSanitized(SearchCoordinatorSvcImpl.class, "invalidResourceType", paramTargetType);
				String msg = myContext
						.getLocalizer()
						.getMessage(
								SearchCoordinatorSvcImpl.class,
								"invalidInclude",
								UrlUtil.sanitizeUrlPart(name),
								UrlUtil.sanitizeUrlPart(value),
								resourceTypeMsg); // last param is pre-sanitized
				throw new InvalidRequestException(Msg.code(2016) + msg);
			}

			if (!Constants.INCLUDE_STAR.equals(paramName)
					&& mySearchParamRegistry.getActiveSearchParam(
									paramType, paramName, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH)
							== null) {
				List<String> validNames = mySearchParamRegistry
						.getActiveSearchParams(paramType, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH)
						.values()
						.stream()
						.filter(t -> t.getParamType() == RestSearchParameterTypeEnum.REFERENCE)
						.map(t -> UrlUtil.sanitizeUrlPart(t.getName()))
						.sorted()
						.collect(Collectors.toList());
				String searchParamMessage = myContext
						.getLocalizer()
						.getMessage(
								BaseStorageDao.class,
								"invalidSearchParameter",
								UrlUtil.sanitizeUrlPart(paramName),
								UrlUtil.sanitizeUrlPart(paramType),
								validNames);
				String msg = myContext
						.getLocalizer()
						.getMessage(
								SearchCoordinatorSvcImpl.class,
								"invalidInclude",
								UrlUtil.sanitizeUrlPart(name),
								UrlUtil.sanitizeUrlPart(value),
								searchParamMessage); // last param is pre-sanitized
				throw new InvalidRequestException(Msg.code(2015) + msg);
			}
		}
	}

	@Nullable
	private Integer getLoadSynchronousUpToOrNull(CacheControlDirective theCacheControlDirective) {
		final Integer loadSynchronousUpTo;
		if (theCacheControlDirective != null && theCacheControlDirective.isNoStore()) {
			if (theCacheControlDirective.getMaxResults() != null) {
				loadSynchronousUpTo = theCacheControlDirective.getMaxResults();
				if (loadSynchronousUpTo > myStorageSettings.getCacheControlNoStoreMaxResultsUpperLimit()) {
					throw new InvalidRequestException(Msg.code(1165) + Constants.HEADER_CACHE_CONTROL + " header "
							+ Constants.CACHE_CONTROL_MAX_RESULTS + " value must not exceed "
							+ myStorageSettings.getCacheControlNoStoreMaxResultsUpperLimit());
				}
			} else {
				loadSynchronousUpTo = 100;
			}
		} else {
			loadSynchronousUpTo = null;
		}
		return loadSynchronousUpTo;
	}

	/**
	 * Creates a {@link Pageable} using a start and end index
	 */
	@SuppressWarnings("WeakerAccess")
	@Nullable
	public static Pageable toPage(final int theFromIndex, int theToIndex) {
		int pageSize = theToIndex - theFromIndex;
		if (pageSize < 1) {
			return null;
		}

		int pageIndex = theFromIndex / pageSize;

		return new PageRequest(pageIndex, pageSize, Sort.unsorted()) {
			@Serial
			private static final long serialVersionUID = 1L;

			@Override
			public long getOffset() {
				return theFromIndex;
			}
		};
	}

	/**
	 * Updates the search entity with failure information based on the exception.
	 * Determines the appropriate HTTP status code based on exception type:
	 * - DataFormatException -> 400 Bad Request (client error)
	 * - BaseServerResponseException -> Use exception's status code
	 * - Other exceptions -> 500 Internal Server Error
	 * <p>
	 * Optionally appends stack trace if unit test capture is enabled.
	 * </p>
	 * @param theThrowable The exception that caused the search to fail
	 */
	public static void markSearchAsFailedWithExceptionDetails(Search theSearch, Throwable theThrowable) {
		Throwable rootCause = ExceptionUtils.getRootCause(theThrowable);
		rootCause = getIfNull(rootCause, theThrowable);

		String failureMessage = rootCause.getMessage();

		int failureCode;
		if (rootCause instanceof DataFormatException || theThrowable instanceof DataFormatException) {
			// DataFormatException indicates invalid client input
			// and should return HTTP 400 Bad Request, not 500 Internal Server Error.
			failureCode = InvalidRequestException.STATUS_CODE;
		} else if (theThrowable instanceof BaseServerResponseException baseServerResponseException) {
			failureCode = baseServerResponseException.getStatusCode();
		} else {
			failureCode = InternalErrorException.STATUS_CODE;
		}

		if (HapiSystemProperties.isUnitTestCaptureStackEnabled()) {
			failureMessage += "\nStack\n" + ExceptionUtils.getStackTrace(rootCause);
		}

		theSearch.setFailureMessage(failureMessage);
		theSearch.setFailureCode(failureCode);
		theSearch.setStatus(SearchStatusEnum.FAILED);
	}
}
