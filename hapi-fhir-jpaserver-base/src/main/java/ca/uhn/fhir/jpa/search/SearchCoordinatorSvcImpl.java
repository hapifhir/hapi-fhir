package ca.uhn.fhir.jpa.search;

import java.util.*;
import java.util.concurrent.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IDao;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchIncludeDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.method.PageMethodBinding;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;

public class SearchCoordinatorSvcImpl implements ISearchCoordinatorSvc {
	static final int DEFAULT_SYNC_SIZE = 250;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchCoordinatorSvcImpl.class);

	@Autowired
	private FhirContext myContext;
	@Autowired
	private EntityManager myEntityManager;
	private ExecutorService myExecutor;
	private final ConcurrentHashMap<String, SearchTask> myIdToSearchTask = new ConcurrentHashMap<String, SearchTask>();
	private long myMaxMillisToWaitForRemoteResults = DateUtils.MILLIS_PER_MINUTE;

	private final List<CountDownLatch> myResultSizeLatch = new ArrayList<CountDownLatch>();
	@Autowired
	private ISearchDao mySearchDao;
	@Autowired
	private ISearchIncludeDao mySearchIncludeDao;
	@Autowired
	private ISearchResultDao mySearchResultDao;
	@Autowired
	private PlatformTransactionManager myTxManager;

	/**
	 * Constructor
	 */
	public SearchCoordinatorSvcImpl() {
		CustomizableThreadFactory threadFactory = new CustomizableThreadFactory("search_coord_");
		myExecutor = Executors.newCachedThreadPool(threadFactory);
	}

	@Override
	@Transactional(value=TxType.NOT_SUPPORTED)
	public List<Long> getResources(final String theUuid, int theFrom, int theTo) {
		if (myNeverUseLocalSearchForUnitTests == false) {
			SearchTask task = myIdToSearchTask.get(theUuid);
			if (task != null) {
				return task.getResourcePids(theFrom, theTo);
			}
		}

		Search search;
		StopWatch sw = new StopWatch();
		while (true) {
			
			TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
			txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
			search = txTemplate.execute(new TransactionCallback<Search>() {
				@Override
				public Search doInTransaction(TransactionStatus theStatus) {
					return mySearchDao.findByUuid(theUuid);
				}
			});

			
			if (search == null) {
				ourLog.info("Client requested unknown paging ID[{}]", theUuid);
				String msg = myContext.getLocalizer().getMessage(PageMethodBinding.class, "unknownSearchId", theUuid);
				throw new ResourceGoneException(msg);
			}

			verifySearchHasntFailedOrThrowInternalErrorException(search);
			if (search.getStatus() == SearchStatusEnum.FINISHED) {
				ourLog.info("Search entity marked as finished");
				break;
			}
			if (search.getNumFound() >= theTo) {
				ourLog.info("Search entity has {} results so far", search.getNumFound());
				break;
			}

			if (sw.getMillis() > myMaxMillisToWaitForRemoteResults) {
				throw new InternalErrorException("Request timed out after " + sw.getMillis() + "ms");
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// ignore
			}
		}

		Pageable page = toPage(theFrom, theTo);
		if (page == null) {
			return Collections.emptyList();
		}

		Page<SearchResult> searchResults = mySearchResultDao.findWithSearchUuid(search, page);
		List<Long> retVal = new ArrayList<Long>();
		for (SearchResult next : searchResults) {
			retVal.add(next.getResourcePid());
		}

		return retVal;
	}

	@Override
	public IBundleProvider registerSearch(IDao theCallingDao, SearchParameterMap theParams, String theResourceType) {
		StopWatch w = new StopWatch();

		Class<? extends IBaseResource> resourceTypeClass = myContext.getResourceDefinition(theResourceType).getImplementingClass();
		ISearchBuilder sb = theCallingDao.newSearchBuilder();
		sb.setType(resourceTypeClass, theResourceType);
		Iterator<Long> resultIter = sb.createQuery(theParams);

		if (theParams.isLoadSynchronous()) {

			// Load the results synchronously
			List<Long> pids = new ArrayList<Long>();
			while (resultIter.hasNext()) {
				pids.add(resultIter.next());
				if (theParams.getLoadSynchronousUpTo() != null && pids.size() >= theParams.getLoadSynchronousUpTo()) {
					break;
				}
			}

			/*
			 * For synchronous queries, we load all the includes right away
			 * since we're returning a static bundle with all the results
			 * pre-loaded. This is ok because syncronous requests are not
			 * expected to be paged
			 * 
			 * On the other hand for async queries we load includes/revincludes
			 * individually for pages as we return them to clients
			 */
			Set<Long> includedPids = new HashSet<Long>();
			includedPids.addAll(sb.loadReverseIncludes(theCallingDao, myContext, myEntityManager, pids, theParams.getRevIncludes(), true, theParams.getLastUpdated()));
			includedPids.addAll(sb.loadReverseIncludes(theCallingDao, myContext, myEntityManager, pids, theParams.getIncludes(), false, theParams.getLastUpdated()));

			// Execute the query and make sure we return distinct results
			List<IBaseResource> resources = new ArrayList<IBaseResource>();
			sb.loadResourcesByPid(pids, resources, includedPids, false, myEntityManager, myContext, theCallingDao);
			return new SimpleBundleProvider(resources);
		}

		Search search = new Search();
		search.setUuid(UUID.randomUUID().toString());
		search.setCreated(new Date());
		search.setTotalCount(null);
		search.setNumFound(0);
		search.setPreferredPageSize(theParams.getCount());
		search.setSearchType(theParams.getEverythingMode() != null ? SearchTypeEnum.EVERYTHING : SearchTypeEnum.SEARCH);
		search.setLastUpdated(theParams.getLastUpdated());
		search.setResourceType(theResourceType);
		search.setStatus(SearchStatusEnum.LOADING);

		for (Include next : theParams.getIncludes()) {
			search.getIncludes().add(new SearchInclude(search, next.getValue(), false, next.isRecurse()));
		}
		for (Include next : theParams.getRevIncludes()) {
			search.getIncludes().add(new SearchInclude(search, next.getValue(), true, next.isRecurse()));
		}

		SearchTask task = new SearchTask(search, theCallingDao, theParams, theResourceType);
		myIdToSearchTask.put(search.getUuid(), task);
		myExecutor.submit(task);

		PersistedJpaSearchFirstPageBundleProvider retVal = new PersistedJpaSearchFirstPageBundleProvider(search, theCallingDao, task, sb, myTxManager);
		retVal.setContext(myContext);
		retVal.setEntityManager(myEntityManager);
		retVal.setPlatformTransactionManager(myTxManager);
		retVal.setSearchDao(mySearchDao);
		retVal.setSearchCoordinatorSvc(this);

		ourLog.info("Search initial phase completed in {}ms", w);
		return retVal;

	}

	@VisibleForTesting
	void setContextForUnitTest(FhirContext theCtx) {
		myContext = theCtx;
	}

	@VisibleForTesting
	void setEntityManagerForUnitTest(EntityManager theEntityManager) {
		myEntityManager = theEntityManager;
	}

	@VisibleForTesting
	void setMaxMillisToWaitForRemoteResultsForUnitTest(long theMaxMillisToWaitForRemoteResults) {
		myMaxMillisToWaitForRemoteResults = theMaxMillisToWaitForRemoteResults;
	}

	@VisibleForTesting
	void setSearchDaoForUnitTest(ISearchDao theSearchDao) {
		mySearchDao = theSearchDao;
	}

	@VisibleForTesting
	void setSearchDaoIncludeForUnitTest(ISearchIncludeDao theSearchIncludeDao) {
		mySearchIncludeDao = theSearchIncludeDao;
	}

	@VisibleForTesting
	void setSearchDaoResultForUnitTest(ISearchResultDao theSearchResultDao) {
		mySearchResultDao = theSearchResultDao;
	}

	@VisibleForTesting
	void setTransactionManagerForUnitTest(PlatformTransactionManager theTxManager) {
		myTxManager = theTxManager;
	}

	static void verifySearchHasntFailedOrThrowInternalErrorException(Search theSearch) {
		if (theSearch.getStatus() == SearchStatusEnum.FAILED) {
			throw new InternalErrorException(theSearch.getFailureMessage());
		}
	}

	private int mySyncSize = DEFAULT_SYNC_SIZE;

	@VisibleForTesting
	void setSyncSizeForUnitTests(int theSyncSize) {
		mySyncSize = theSyncSize;
	}

	@VisibleForTesting
	void setLoadingThrottleForUnitTests(Integer theLoadingThrottleForUnitTests) {
		myLoadingThrottleForUnitTests = theLoadingThrottleForUnitTests;
	}

	private Integer myLoadingThrottleForUnitTests = null;

	private boolean myNeverUseLocalSearchForUnitTests;

	public class SearchTask implements Callable<Void> {

		private int myCountSaved = 0;
		private final CountDownLatch myInitialCollectionLatch = new CountDownLatch(1);
		private final Search mySearch;
		private final ArrayList<Long> mySyncedPids = new ArrayList<Long>();
		private final ArrayList<Long> myUnsyncedPids = new ArrayList<Long>();
		private final IDao myCallingDao;
		private SearchParameterMap myParams;
		private String myResourceType;

		public SearchTask(Search theSearch, IDao theCallingDao, SearchParameterMap theParams, String theResourceType) {
			mySearch = theSearch;
			myCallingDao = theCallingDao;
			myParams = theParams;
			myResourceType = theResourceType;
		}

		public void awaitInitialSync() {
			ourLog.info("Awaiting initial sync");
			do {
				try {
					if (myInitialCollectionLatch.await(250, TimeUnit.MILLISECONDS)) {
						break;
					}
				} catch (InterruptedException e) {
					throw new InternalErrorException(e);
				}
			} while (mySearch.getStatus() == SearchStatusEnum.LOADING);
			ourLog.info("Initial sync completed");
		}

		@Override
		public Void call() throws Exception {
			StopWatch sw = new StopWatch();

			try {
				TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
				txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
				txTemplate.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
						doSearch();
					}
				});

				ourLog.info("Completed search for {} resources in {}ms", mySyncedPids.size(), sw.getMillis());

			} catch (Throwable t) {
				ourLog.error("Failed during search loading after {}ms", t, sw.getMillis());
				myUnsyncedPids.clear();

				mySearch.setStatus(SearchStatusEnum.FAILED);
				String failureMessage = ExceptionUtils.getRootCauseMessage(t);
				mySearch.setFailureMessage(failureMessage);
				saveSearch();

			}

			myIdToSearchTask.remove(mySearch.getUuid());
			return null;
		}

		private void doSearch() {
			Class<? extends IBaseResource> resourceTypeClass = myContext.getResourceDefinition(myResourceType).getImplementingClass();
			ISearchBuilder sb = myCallingDao.newSearchBuilder();
			sb.setType(resourceTypeClass, myResourceType);
			Iterator<Long> theResultIter = sb.createQuery(myParams);

			while (theResultIter.hasNext()) {
				myUnsyncedPids.add(theResultIter.next());
				if (myUnsyncedPids.size() >= mySyncSize) {
					saveUnsynced(theResultIter);
				}
				if (myLoadingThrottleForUnitTests != null) {
					try {
						Thread.sleep(myLoadingThrottleForUnitTests);
					} catch (InterruptedException e) {
						// ignore
					}
				}
			}
			saveUnsynced(theResultIter);
		}

		private void doSaveSearch() {
			if (mySearch.getId() == null) {
				mySearchDao.save(mySearch);
				for (SearchInclude next : mySearch.getIncludes()) {
					mySearchIncludeDao.save(next);
				}
			} else {
				mySearchDao.save(mySearch);
			}
		}

		public List<Long> getResourcePids(int theFromIndex, int theToIndex) {
			ourLog.info("Requesting search PIDs from {}-{}", theFromIndex, theToIndex);

			CountDownLatch latch = null;
			synchronized (mySyncedPids) {
				if (mySyncedPids.size() < theToIndex && mySearch.getStatus() == SearchStatusEnum.LOADING) {
					int latchSize = theToIndex - mySyncedPids.size();
					ourLog.info("Registering latch to await {} results (want {} total)", latchSize, theToIndex);
					latch = new CountDownLatch(latchSize);
					myResultSizeLatch.add(latch);
				}
			}

			if (latch != null) {
				while (latch.getCount() > 0 && mySearch.getStatus() == SearchStatusEnum.LOADING) {
					try {
						ourLog.trace("Awaiting latch with {}", latch.getCount());
						latch.await(500, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						// ok
					}
				}
			}

			ArrayList<Long> retVal = new ArrayList<Long>();
			synchronized (mySyncedPids) {
				verifySearchHasntFailedOrThrowInternalErrorException(mySearch);

				int toIndex = theToIndex;
				if (mySyncedPids.size() < toIndex) {
					toIndex = mySyncedPids.size();
				}
				for (int i = theFromIndex; i < toIndex; i++) {
					retVal.add(mySyncedPids.get(i));
				}
			}

			return retVal;
		}

		private void saveSearch() {
			TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
			txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
			txTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
					doSaveSearch();
				}

			});
		}

		private void saveUnsynced(final Iterator<Long> theResultIter) {
			TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
			txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
			txTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
					if (mySearch.getId() == null) {
						doSaveSearch();
					}

					List<SearchResult> resultsToSave = Lists.newArrayList();
					for (Long nextPid : myUnsyncedPids) {
						SearchResult nextResult = new SearchResult(mySearch);
						nextResult.setResourcePid(nextPid);
						nextResult.setOrder(myCountSaved++);
						resultsToSave.add(nextResult);
					}
					mySearchResultDao.save(resultsToSave);

					synchronized (mySyncedPids) {
						int numSyncedThisPass = myUnsyncedPids.size();
						ourLog.info("Syncing {} search results", numSyncedThisPass);
						mySyncedPids.addAll(myUnsyncedPids);
						myUnsyncedPids.clear();

						if (theResultIter.hasNext() == false) {
							mySearch.setStatus(SearchStatusEnum.FINISHED);
							mySearch.setTotalCount(myCountSaved);
							for (CountDownLatch next : myResultSizeLatch) {
								while (next.getCount() > 0) {
									next.countDown();
								}
							}
						} else {
							if (myResultSizeLatch.isEmpty() == false) {
								for (CountDownLatch next : myResultSizeLatch) {
									for (int i = 0; i < numSyncedThisPass; i++) {
										next.countDown();
									}
								}
							}
						}
					}
					mySearch.setNumFound(myCountSaved);
					doSaveSearch();

				}
			});

			myInitialCollectionLatch.countDown();
		}

	}

	static Pageable toPage(final int theFromIndex, int theToIndex) {
		int pageSize = theToIndex - theFromIndex;
		if (pageSize < 1) {
			return null;
		}

		int pageIndex = theFromIndex / pageSize;

		Pageable page = new PageRequest(pageIndex, pageSize) {
			private static final long serialVersionUID = 1L;

			@Override
			public int getOffset() {
				return theFromIndex;
			}
		};

		return page;
	}

	void setNeverUseLocalSearchForUnitTests(boolean theNeverUseLocalSearchForUnitTests) {
		myNeverUseLocalSearchForUnitTests = theNeverUseLocalSearchForUnitTests;
	}

}
