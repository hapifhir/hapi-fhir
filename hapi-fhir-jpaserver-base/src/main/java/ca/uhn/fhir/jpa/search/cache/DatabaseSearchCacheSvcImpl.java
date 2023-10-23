/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService.IExecutionBuilder;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.system.HapiSystemProperties;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.dstu3.model.InstantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.sql.DataSource;

public class DatabaseSearchCacheSvcImpl implements ISearchCacheSvc {
	/*
	 * Be careful increasing this number! We use the number of params here in a
	 * DELETE FROM foo WHERE params IN (term,term,term...)
	 * type query and this can fail if we have 1000s of params
	 */
	public static final int DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_STMT = 500;
	public static final int DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_PAS = 20000;
	public static final long SEARCH_CLEANUP_JOB_INTERVAL_MILLIS = DateUtils.MILLIS_PER_MINUTE;
	public static final int DEFAULT_MAX_DELETE_CANDIDATES_TO_FIND = 2000;
	private static final Logger ourLog = LoggerFactory.getLogger(DatabaseSearchCacheSvcImpl.class);
	private static int ourMaximumResultsToDeleteInOneStatement = DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_STMT;
	private static int ourMaximumResultsToDeleteInOnePass = DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_PAS;
	private static int ourMaximumSearchesToCheckForDeletionCandidacy = 2;
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
	private ISearchResultDao mySearchResultDao;

	@Autowired
	private ISearchIncludeDao mySearchIncludeDao;

	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;

	@Autowired
	private DataSource myDataSource;

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

	class DeleteRun {
		final RequestPartitionId myRequestPartitionId;
		final Instant myDeadline;
		boolean myDeadlineLogged = false;
		final Date myCutoffForDeletion;
		final Set<Long> mySearchesToMarkForDeletion = new HashSet<>();
		final Set<Long> mySearchPidsToDelete = new HashSet<>();
		final Set<Long> mySearchPidsToDeleteResults = new HashSet<>();
		/**
		 * Number of results we have queued up in mySearchPidsToDeleteResults to delete.
		 * We try to keep this to a reasonable size to avoid long transactions that may escalate to a table lock.
		 */
		private int myPendingSearchResultCount = 0;

		DeleteRun(Instant theDeadline, Date theCutoffForDeletion, RequestPartitionId theRequestPartitionId) {
			myDeadline = theDeadline;
			myCutoffForDeletion = theCutoffForDeletion;
			myRequestPartitionId = theRequestPartitionId;
		}

		/**
		 * Iterate over all theMarkDeletedTargets ids and mark them as deleted in batches of size ourMaximumSearchesToCheckForDeletionCandidacy
		 */
		public void markDeletedInBatches(@Nonnull Iterable<Long> theMarkDeletedTargets) {
			assert theMarkDeletedTargets != null;

			for (final Long nextSearchToDelete : theMarkDeletedTargets) {
				if (isPastDeadline()) {
					break;
				}
				if (mySearchesToMarkForDeletion.size() >= ourMaximumSearchesToCheckForDeletionCandidacy) {
					flushDeleteMarks();
				}
				ourLog.debug("Marking search with PID {} as ready for deletion", nextSearchToDelete);
				mySearchesToMarkForDeletion.add(nextSearchToDelete);
			}
			flushDeleteMarks();
		}

		/**
		 * Mark all ids in the mySearchesToMarkForDeletion buffer as deleted, and clear the buffer.
		 */
		public void flushDeleteMarks() {
			if (mySearchesToMarkForDeletion.isEmpty()) {
				return;
			}
			ourLog.debug("Marking {} searches as deleted", mySearchesToMarkForDeletion.size());
			mySearchDao.updateDeleted(mySearchesToMarkForDeletion, true);
			commitOpenChanges();
			mySearchesToMarkForDeletion.clear();
		}

		/**
		 * Dig into the guts of our Hibernate session, flush any changes in the session, and commit the underlying connection.
		 */
		private void commitOpenChanges() {
			// flush to force Hibernate to actually get a connection from the pool
			mySearchDao.flush();
			// goofy hack to get the underlying connection and commit
			new JdbcTemplate(myDataSource).execute((ConnectionCallback<?>) con -> {
				con.commit();
				return null;
			});
		}

		boolean isPastDeadline() {
			boolean result = Instant.ofEpochMilli(now()).isAfter(myDeadline);
			if (result && !myDeadlineLogged) {
				ourLog.warn("Search cache cleaner did not finish all work before deadline.");
				myDeadlineLogged = true;
			}
			return result;
		}

		private int deleteMarkedSearches() {
			final Iterable<Object[]> toDelete = mySearchDao.findDeleted();
			assert toDelete != null;

			int count = 0;
			for (final Object[] nextSearchToDelete : toDelete) {
				long searchPid = (long) nextSearchToDelete[0];
				Integer numberOfResults = (Integer) nextSearchToDelete[1];
				numberOfResults = numberOfResults == null ? 0 : numberOfResults;

				ourLog.trace("Buffering deletion Search with pid {} and {} results", searchPid, numberOfResults);

				deleteSearchAndResults(searchPid, numberOfResults);
				count += 1;
				if (isPastDeadline()) {
					break;
				}
			}
			flushSearchResultDeletes();
			flushSearchDeletes();
			return count;
		}

		private void deleteSearchAndResults(long theSearchPid, int theNumberOfResults) {
			// wipmb check for oversize.

			mySearchPidsToDeleteResults.add(theSearchPid);
			mySearchPidsToDelete.add(theSearchPid);
			myPendingSearchResultCount += theNumberOfResults;

			// wipmb constant?
			if (myPendingSearchResultCount > 50000) {
				flushSearchResultDeletes();
			}

			if (mySearchPidsToDelete.size() > 1000) {
				flushSearchResultDeletes();
				flushSearchDeletes();
			}
		}

		private void flushSearchDeletes() {
			if (mySearchPidsToDelete.isEmpty()) {
				return;
			}
			ourLog.debug("Deleting {} Search records", mySearchPidsToDelete.size());
			mySearchIncludeDao.deleteForSearch(mySearchPidsToDelete);
			mySearchDao.deleteByPids(mySearchPidsToDelete);
			commitOpenChanges();
			mySearchPidsToDelete.clear();
		}

		private void flushSearchResultDeletes() {
			if (mySearchPidsToDeleteResults.isEmpty()) {
				return;
			}
			ourLog.debug("Deleting {} Search Results", mySearchPidsToDeleteResults.size());
			mySearchResultDao.deleteBySearchIds(mySearchPidsToDeleteResults);
			commitOpenChanges();
			mySearchPidsToDeleteResults.clear();
			myPendingSearchResultCount = 0;
		}

		IExecutionBuilder getTxBuilder() {
			return myTransactionService.withSystemRequest().withRequestPartitionId(myRequestPartitionId);
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

		ourLog.debug("Searching for searches which are before {}", cutoff);

		// Mark searches as deleted if they should be
		run.getTxBuilder().execute(theStatus -> {
			final Iterable<Long> toMarkDeleted = mySearchDao.findWhereCreatedBefore(cutoff, new Date(now()));

			run.markDeletedInBatches(toMarkDeleted);

			if (run.isPastDeadline()) {
				return null;
			}

			// Delete searches that are marked as deleted
			int deletedCount = run.deleteMarkedSearches();

			if ((ourLog.isDebugEnabled() || HapiSystemProperties.isTestModeEnabled()) && (deletedCount > 0)) {
				Long total = mySearchDao.count();
				ourLog.debug("Deleted {} searches, {} remaining", deletedCount, total);
			}
			return null;
		});
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
	public static void setMaximumSearchesToCheckForDeletionCandidacyForUnitTest(
			int theMaximumSearchesToCheckForDeletionCandidacy) {
		ourMaximumSearchesToCheckForDeletionCandidacy = theMaximumSearchesToCheckForDeletionCandidacy;
	}

	@VisibleForTesting
	public static void setMaximumResultsToDeleteInOnePassForUnitTest(int theMaximumResultsToDeleteInOnePass) {
		ourMaximumResultsToDeleteInOnePass = theMaximumResultsToDeleteInOnePass;
	}

	@VisibleForTesting
	public static void setMaximumResultsToDeleteForUnitTest(int theMaximumResultsToDelete) {
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
