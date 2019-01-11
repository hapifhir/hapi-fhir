package ca.uhn.fhir.jpa.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchIncludeDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.dstu3.model.InstantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;

/**
 * Deletes old searches
 */
//
// NOTE: This is not a @Service because we manually instantiate
// it in BaseConfig. This is so that we can override the definition
// in Smile.
//
public class StaleSearchDeletingSvcImpl implements IStaleSearchDeletingSvc {
	public static final long DEFAULT_CUTOFF_SLACK = 10 * DateUtils.MILLIS_PER_SECOND;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StaleSearchDeletingSvcImpl.class);
	/*
	 * Be careful increasing this number! We use the number of params here in a
	 * DELETE FROM foo WHERE params IN (aaaa)
	 * type query and this can fail if we have 1000s of params
	 */
	public static final int DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_STMT = 500;
	public static final int DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_PAS = 20000;
	private static int ourMaximumResultsToDeleteInOneStatement = DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_STMT;
	private static int ourMaximumResultsToDeleteInOnePass = DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_PAS;
	private static Long ourNowForUnitTests;
	/*
	 * We give a bit of extra leeway just to avoid race conditions where a query result
	 * is being reused (because a new client request came in with the same params) right before
	 * the result is to be deleted
	 */
	private long myCutoffSlack = DEFAULT_CUTOFF_SLACK;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private ISearchDao mySearchDao;
	@Autowired
	private ISearchIncludeDao mySearchIncludeDao;
	@Autowired
	private ISearchResultDao mySearchResultDao;
	@Autowired
	private PlatformTransactionManager myTransactionManager;

	private void deleteSearch(final Long theSearchPid) {
		mySearchDao.findById(theSearchPid).ifPresent(searchToDelete -> {
			mySearchIncludeDao.deleteForSearch(searchToDelete.getId());

			/*
			 * Note, we're only deleting up to 500 results in an individual search here. This
			 * is to prevent really long running transactions in cases where there are
			 * huge searches with tons of results in them. By the time we've gotten here
			 * we have marked the parent Search entity as deleted, so it's not such a
			 * huge deal to be only partially deleting search results. They'll get deleted
			 * eventually
			 */
			int max = ourMaximumResultsToDeleteInOnePass;
			Slice<Long> resultPids = mySearchResultDao.findForSearch(PageRequest.of(0, max), searchToDelete.getId());
			if (resultPids.hasContent()) {
				List<List<Long>> partitions = Lists.partition(resultPids.getContent(), ourMaximumResultsToDeleteInOneStatement);
				for (List<Long> nextPartition : partitions) {
					mySearchResultDao.deleteByIds(nextPartition);
				}

			}

			// Only delete if we don't have results left in this search
			if (resultPids.getNumberOfElements() < max) {
				ourLog.info("Deleting search {}/{} - Created[{}] -- Last returned[{}]", searchToDelete.getId(), searchToDelete.getUuid(), new InstantType(searchToDelete.getCreated()), new InstantType(searchToDelete.getSearchLastReturned()));
				mySearchDao.deleteByPid(searchToDelete.getId());
			} else {
				ourLog.info("Purged {} search results for deleted search {}/{}", resultPids.getSize(), searchToDelete.getId(), searchToDelete.getUuid());
			}
		});
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public void pollForStaleSearchesAndDeleteThem() {
		if (!myDaoConfig.isExpireSearchResults()) {
			return;
		}

		long cutoffMillis = myDaoConfig.getExpireSearchResultsAfterMillis();
		if (myDaoConfig.getReuseCachedSearchResultsForMillis() != null) {
			cutoffMillis = Math.max(cutoffMillis, myDaoConfig.getReuseCachedSearchResultsForMillis());
		}
		final Date cutoff = new Date((now() - cutoffMillis) - myCutoffSlack);

		if (ourNowForUnitTests != null) {
			ourLog.info("Searching for searches which are before {} - now is {}", new InstantType(cutoff), new InstantType(new Date(now())));
		}

		ourLog.debug("Searching for searches which are before {}", cutoff);

		TransactionTemplate tt = new TransactionTemplate(myTransactionManager);
		final Slice<Long> toDelete = tt.execute(theStatus ->
			mySearchDao.findWhereLastReturnedBefore(cutoff, PageRequest.of(0, 1000))
		);
		for (final Long nextSearchToDelete : toDelete) {
			ourLog.debug("Deleting search with PID {}", nextSearchToDelete);
			tt.execute(t -> {
				mySearchDao.updateDeleted(nextSearchToDelete, true);
				return null;
			});

			tt.execute(t -> {
				deleteSearch(nextSearchToDelete);
				return null;
			});
		}

		int count = toDelete.getContent().size();
		if (count > 0) {
			long total = tt.execute(t -> mySearchDao.count());
			ourLog.info("Deleted {} searches, {} remaining", count, total);
		}

	}

	@Scheduled(fixedDelay = DEFAULT_CUTOFF_SLACK)
	@Transactional(propagation = Propagation.NEVER)
	@Override
	public synchronized void schedulePollForStaleSearches() {
		if (!myDaoConfig.isSchedulingDisabled()) {
			pollForStaleSearchesAndDeleteThem();
		}
	}

	@VisibleForTesting
	public void setCutoffSlackForUnitTest(long theCutoffSlack) {
		myCutoffSlack = theCutoffSlack;
	}

	@VisibleForTesting
	public static void setMaximumResultsToDeleteInOnePassForUnitTest(int theMaximumResultsToDeleteInOnePass) {
		ourMaximumResultsToDeleteInOnePass = theMaximumResultsToDeleteInOnePass;
	}

	@VisibleForTesting
	public static void setMaximumResultsToDeleteForUnitTest(int theMaximumResultsToDelete) {
		ourMaximumResultsToDeleteInOneStatement = theMaximumResultsToDelete;
	}

	private static long now() {
		if (ourNowForUnitTests != null) {
			return ourNowForUnitTests;
		}
		return System.currentTimeMillis();
	}

	/**
	 * This is for unit tests only, do not call otherwise
	 */
	@VisibleForTesting
	public static void setNowForUnitTests(Long theNowForUnitTests) {
		ourNowForUnitTests = theNowForUnitTests;
	}

}
