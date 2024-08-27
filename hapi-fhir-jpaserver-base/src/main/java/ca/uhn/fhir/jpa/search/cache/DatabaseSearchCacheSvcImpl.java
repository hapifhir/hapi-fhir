/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.cache;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchIncludeDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.dao.data.SearchIdAndResultSize;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService.IExecutionBuilder;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.system.HapiSystemProperties;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Session;
import org.hl7.fhir.dstu3.model.InstantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class DatabaseSearchCacheSvcImpl implements ISearchCacheSvc {
	/*
	 * Be careful increasing this number! We use the number of params here in a
	 * DELETE FROM foo WHERE params IN (term,term,term...)
	 * type query and this can fail if we have 1000s of params
	 */
	public static final int DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_STMT = 500;
	public static final int DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_PAS = 50000;
	public static final long SEARCH_CLEANUP_JOB_INTERVAL_MILLIS = DateUtils.MILLIS_PER_MINUTE;
	public static final int DEFAULT_MAX_DELETE_CANDIDATES_TO_FIND = 2000;
	private static final Logger ourLog = LoggerFactory.getLogger(DatabaseSearchCacheSvcImpl.class);
	private static int ourMaximumResultsToDeleteInOneStatement = DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_STMT;
	private static int ourMaximumResultsToDeleteInOneCommit = DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_PAS;
	private static Long ourNowForUnitTests;
	/*
	 * We give a bit of extra leeway just to avoid race conditions where a query result
	 * is being reused (because a new client request came in with the same params) right before
	 * the result is to be deleted
	 */
	private long myCutoffSlack = SEARCH_CLEANUP_JOB_INTERVAL_MILLIS;

	@Autowired
	private ISearchDao mySearchDao;

	@Autowired
	private EntityManager myEntityManager;

	@Autowired
	private ISearchResultDao mySearchResultDao;

	@Autowired
	private ISearchIncludeDao mySearchIncludeDao;

	@Autowired
	private IHapiTransactionService myTransactionService;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@VisibleForTesting
	public void setCutoffSlackForUnitTest(long theCutoffSlack) {
		myCutoffSlack = theCutoffSlack;
	}

	@Override
	public Search save(Search theSearch, RequestPartitionId theRequestPartitionId) {
		return myTransactionService
				.withSystemRequestOnPartition(theRequestPartitionId)
				.execute(() -> mySearchDao.save(theSearch));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<Search> fetchByUuid(String theUuid, RequestPartitionId theRequestPartitionId) {
		Validate.notBlank(theUuid);
		return myTransactionService
				.withSystemRequestOnPartition(theRequestPartitionId)
				.execute(() -> mySearchDao.findByUuidAndFetchIncludes(theUuid));
	}

	void setSearchDaoForUnitTest(ISearchDao theSearchDao) {
		mySearchDao = theSearchDao;
	}

	void setTransactionServiceForUnitTest(IHapiTransactionService theTransactionService) {
		myTransactionService = theTransactionService;
	}

	@Override
	public Optional<Search> tryToMarkSearchAsInProgress(Search theSearch, RequestPartitionId theRequestPartitionId) {
		ourLog.trace(
				"Going to try to change search status from {} to {}", theSearch.getStatus(), SearchStatusEnum.LOADING);
		try {

			return myTransactionService
					.withSystemRequest()
					.withRequestPartitionId(theRequestPartitionId)
					.withPropagation(Propagation.REQUIRES_NEW)
					.execute(t -> {
						Search search = mySearchDao.findById(theSearch.getId()).orElse(theSearch);

						if (search.getStatus() != SearchStatusEnum.PASSCMPLET) {
							throw new IllegalStateException(
									Msg.code(1167) + "Can't change to LOADING because state is " + search.getStatus());
						}
						search.setStatus(SearchStatusEnum.LOADING);
						Search newSearch = mySearchDao.save(search);
						return Optional.of(newSearch);
					});
		} catch (Exception e) {
			ourLog.warn("Failed to activate search: {}", e.toString());
			ourLog.trace("Failed to activate search", e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<Search> findCandidatesForReuse(
			String theResourceType,
			String theQueryString,
			Instant theCreatedAfter,
			RequestPartitionId theRequestPartitionId) {
		HapiTransactionService.requireTransaction();

		String queryString = Search.createSearchQueryStringForStorage(theQueryString, theRequestPartitionId);

		int hashCode = queryString.hashCode();
		Collection<Search> candidates =
				mySearchDao.findWithCutoffOrExpiry(theResourceType, hashCode, Date.from(theCreatedAfter));

		for (Search nextCandidateSearch : candidates) {
			// We should only reuse our search if it was created within the permitted window
			// Date.after() is unreliable.  Instant.isAfter() always works.
			if (queryString.equals(nextCandidateSearch.getSearchQueryString())
					&& nextCandidateSearch.getCreated().toInstant().isAfter(theCreatedAfter)) {
				return Optional.of(nextCandidateSearch);
			}
		}

		return Optional.empty();
	}

	/**
	 * A transient worker for a single pass through stale-search deletion.
	 */
	class DeleteRun {
		final RequestPartitionId myRequestPartitionId;
		final Instant myDeadline;
		final Date myCutoffForDeletion;
		final Set<Long> myUpdateDeletedFlagBatch = new HashSet<>();
		final Set<Long> myDeleteSearchBatch = new HashSet<>();
		/** the Search pids of the SearchResults we plan to delete in a chunk */
		final Set<Long> myDeleteSearchResultsBatch = new HashSet<>();
		/**
		 * Number of results we have queued up in mySearchPidsToDeleteResults to delete.
		 * We try to keep this to a reasonable size to avoid long transactions that may escalate to a table lock.
		 */
		private int myDeleteSearchResultsBatchCount = 0;

		DeleteRun(Instant theDeadline, Date theCutoffForDeletion, RequestPartitionId theRequestPartitionId) {
			myDeadline = theDeadline;
			myCutoffForDeletion = theCutoffForDeletion;
			myRequestPartitionId = theRequestPartitionId;
		}

		/**
		 * Mark all ids in the mySearchesToMarkForDeletion buffer as deleted, and clear the buffer.
		 */
		public void flushDeleteMarks() {
			if (myUpdateDeletedFlagBatch.isEmpty()) {
				return;
			}
			ourLog.debug("Marking {} searches as deleted", myUpdateDeletedFlagBatch.size());
			mySearchDao.updateDeleted(myUpdateDeletedFlagBatch, true);
			myUpdateDeletedFlagBatch.clear();
			commitOpenChanges();
		}

		/**
		 * Dig into the guts of our Hibernate session, flush any changes in the session, and commit the underlying connection.
		 */
		private void commitOpenChanges() {
			// flush to force Hibernate to actually get a connection from the pool
			myEntityManager.flush();
			// get our connection from the underlying Hibernate session, and commit
			myEntityManager.unwrap(Session.class).doWork(Connection::commit);
		}

		void throwIfDeadlineExpired() {
			boolean result = Instant.ofEpochMilli(now()).isAfter(myDeadline);
			if (result) {
				throw new DeadlineException(
						Msg.code(2443) + "Deadline expired while cleaning Search cache - " + myDeadline);
			}
		}

		private int deleteMarkedSearchesInBatches() {
			AtomicInteger deletedCounter = new AtomicInteger(0);

			try (final Stream<SearchIdAndResultSize> toDelete = mySearchDao.findDeleted()) {
				assert toDelete != null;

				toDelete.forEach(nextSearchToDelete -> {
					throwIfDeadlineExpired();

					deleteSearchAndResults(nextSearchToDelete.searchId, nextSearchToDelete.size);

					deletedCounter.incrementAndGet();
				});
			}

			// flush anything left in the buffers
			flushSearchResultDeletes();
			flushSearchAndIncludeDeletes();

			int deletedCount = deletedCounter.get();
			if (deletedCount > 0) {
				ourLog.debug("Deleted {} expired searches", deletedCount);
			}

			return deletedCount;
		}

		/**
		 * Schedule theSearchPid for deletion assuming it has theNumberOfResults SearchResults attached.
		 * <p>
		 * We accumulate a batch of search pids for deletion, and then do a bulk DML as we reach a threshold number
		 * of SearchResults.
		 * </p>
		 *
		 * @param theSearchPid pk of the Search
		 * @param theNumberOfResults the number of SearchResults attached
		 */
		private void deleteSearchAndResults(long theSearchPid, int theNumberOfResults) {
			ourLog.trace("Buffering deletion of search pid {} and {} results", theSearchPid, theNumberOfResults);

			myDeleteSearchBatch.add(theSearchPid);

			if (theNumberOfResults > ourMaximumResultsToDeleteInOneCommit) {
				// don't buffer this one - do it inline
				deleteSearchResultsByChunk(theSearchPid, theNumberOfResults);
				return;
			}
			myDeleteSearchResultsBatch.add(theSearchPid);
			myDeleteSearchResultsBatchCount += theNumberOfResults;

			if (myDeleteSearchResultsBatchCount > ourMaximumResultsToDeleteInOneCommit) {
				flushSearchResultDeletes();
			}

			if (myDeleteSearchBatch.size() > ourMaximumResultsToDeleteInOneStatement) {
				// flush the results to make sure we don't have any references.
				flushSearchResultDeletes();

				flushSearchAndIncludeDeletes();
			}
		}

		/**
		 * If this Search has more results than our max delete size,
		 * delete in by itself in range chunks.
		 * @param theSearchPid the target Search pid
		 * @param theNumberOfResults the number of search results present
		 */
		private void deleteSearchResultsByChunk(long theSearchPid, int theNumberOfResults) {
			ourLog.debug(
					"Search {} is large: has {} results.  Deleting results in chunks.",
					theSearchPid,
					theNumberOfResults);
			for (int rangeEnd = theNumberOfResults; rangeEnd >= 0; rangeEnd -= ourMaximumResultsToDeleteInOneCommit) {
				int rangeStart = rangeEnd - ourMaximumResultsToDeleteInOneCommit;
				ourLog.trace("Deleting results for search {}: {} - {}", theSearchPid, rangeStart, rangeEnd);
				mySearchResultDao.deleteBySearchIdInRange(theSearchPid, rangeStart, rangeEnd);
				commitOpenChanges();
			}
		}

		private void flushSearchAndIncludeDeletes() {
			if (myDeleteSearchBatch.isEmpty()) {
				return;
			}
			ourLog.debug("Deleting {} Search records", myDeleteSearchBatch.size());
			// referential integrity requires we delete includes before the search
			mySearchIncludeDao.deleteForSearch(myDeleteSearchBatch);
			mySearchDao.deleteByPids(myDeleteSearchBatch);
			myDeleteSearchBatch.clear();
			commitOpenChanges();
		}

		private void flushSearchResultDeletes() {
			if (myDeleteSearchResultsBatch.isEmpty()) {
				return;
			}
			ourLog.debug(
					"Deleting {} Search Results from {} searches",
					myDeleteSearchResultsBatchCount,
					myDeleteSearchResultsBatch.size());
			mySearchResultDao.deleteBySearchIds(myDeleteSearchResultsBatch);
			myDeleteSearchResultsBatch.clear();
			myDeleteSearchResultsBatchCount = 0;
			commitOpenChanges();
		}

		IExecutionBuilder getTxBuilder() {
			return myTransactionService.withSystemRequest().withRequestPartitionId(myRequestPartitionId);
		}

		private void run() {
			ourLog.debug("Searching for searches which are before {}", myCutoffForDeletion);

			// this tx builder is not really for tx management.
			// Instead, it is used bind a Hibernate session + connection to this thread.
			// We will run a streaming query to look for work, and then commit changes in batches during the loops.
			getTxBuilder().execute(theStatus -> {
				try {
					markDeletedInBatches();

					throwIfDeadlineExpired();

					// Delete searches that are marked as deleted
					int deletedCount = deleteMarkedSearchesInBatches();

					throwIfDeadlineExpired();

					if ((ourLog.isDebugEnabled() || HapiSystemProperties.isTestModeEnabled()) && (deletedCount > 0)) {
						Long total = mySearchDao.count();
						ourLog.debug("Deleted {} searches, {} remaining", deletedCount, total);
					}
				} catch (DeadlineException theTimeoutException) {
					ourLog.warn(theTimeoutException.getMessage());
				}

				return null;
			});
		}

		/**
		 * Stream through a list of pids before our cutoff, and set myDeleted=true in batches in a DML statement.
		 */
		private void markDeletedInBatches() {

			try (Stream<Long> toMarkDeleted =
					mySearchDao.findWhereCreatedBefore(myCutoffForDeletion, new Date(now()))) {
				assert toMarkDeleted != null;

				toMarkDeleted.forEach(nextSearchToDelete -> {
					throwIfDeadlineExpired();

					if (myUpdateDeletedFlagBatch.size() >= ourMaximumResultsToDeleteInOneStatement) {
						flushDeleteMarks();
					}
					ourLog.trace("Marking search with PID {} as ready for deletion", nextSearchToDelete);
					myUpdateDeletedFlagBatch.add(nextSearchToDelete);
				});

				flushDeleteMarks();
			}
		}
	}

	/**
	 * Marker to abandon our delete run when we are over time.
	 */
	private static class DeadlineException extends RuntimeException {
		public DeadlineException(String message) {
			super(message);
		}
	}

	@Override
	public void pollForStaleSearchesAndDeleteThem(RequestPartitionId theRequestPartitionId, Instant theDeadline) {
		HapiTransactionService.noTransactionAllowed();

		if (!myStorageSettings.isExpireSearchResults()) {
			return;
		}

		final Date cutoff = getCutoff();

		final DeleteRun run = new DeleteRun(theDeadline, cutoff, theRequestPartitionId);

		run.run();
	}

	@Nonnull
	private Date getCutoff() {
		long cutoffMillis = myStorageSettings.getExpireSearchResultsAfterMillis();
		if (myStorageSettings.getReuseCachedSearchResultsForMillis() != null) {
			cutoffMillis = cutoffMillis + myStorageSettings.getReuseCachedSearchResultsForMillis();
		}
		final Date cutoff = new Date((now() - cutoffMillis) - myCutoffSlack);

		if (ourNowForUnitTests != null) {
			ourLog.info(
					"Searching for searches which are before {} - now is {}",
					new InstantType(cutoff),
					new InstantType(new Date(now())));
		}
		return cutoff;
	}

	@VisibleForTesting
	public static void setMaximumResultsToDeleteInOnePassForUnitTest(int theMaximumResultsToDeleteInOnePass) {
		ourMaximumResultsToDeleteInOneCommit = theMaximumResultsToDeleteInOnePass;
	}

	@VisibleForTesting
	public static void setMaximumResultsToDeleteInOneStatement(int theMaximumResultsToDelete) {
		ourMaximumResultsToDeleteInOneStatement = theMaximumResultsToDelete;
	}

	/**
	 * This is for unit tests only, do not call otherwise
	 */
	@VisibleForTesting
	public static void setNowForUnitTests(Long theNowForUnitTests) {
		ourNowForUnitTests = theNowForUnitTests;
	}

	public static long now() {
		if (ourNowForUnitTests != null) {
			return ourNowForUnitTests;
		}
		return System.currentTimeMillis();
	}
}
