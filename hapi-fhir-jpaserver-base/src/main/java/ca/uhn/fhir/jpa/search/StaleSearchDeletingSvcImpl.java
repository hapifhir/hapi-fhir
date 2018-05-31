package ca.uhn.fhir.jpa.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.jpa.entity.Search;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.dstu3.model.InstantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

/**
 * Deletes old searches
 */
public class StaleSearchDeletingSvcImpl implements IStaleSearchDeletingSvc {
	public static final long DEFAULT_CUTOFF_SLACK = 10 * DateUtils.MILLIS_PER_SECOND;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StaleSearchDeletingSvcImpl.class);
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
			ourLog.info("Deleting search {}/{} - Created[{}] -- Last returned[{}]", searchToDelete.getId(), searchToDelete.getUuid(), new InstantType(searchToDelete.getCreated()), new InstantType(searchToDelete.getSearchLastReturned()));
			mySearchIncludeDao.deleteForSearch(searchToDelete.getId());
			mySearchResultDao.deleteForSearch(searchToDelete.getId());
			mySearchDao.delete(searchToDelete);
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
		final Slice<Long> toDelete = tt.execute(new TransactionCallback<Slice<Long>>() {
			@Override
			public Slice<Long> doInTransaction(TransactionStatus theStatus) {
				return mySearchDao.findWhereLastReturnedBefore(cutoff, new PageRequest(0, 1000));
			}
		});

		for (final Long nextSearchToDelete : toDelete) {
			ourLog.debug("Deleting search with PID {}", nextSearchToDelete);
			tt.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					deleteSearch(nextSearchToDelete);
				}
			});
		}

		int count = toDelete.getContent().size();
		if (count > 0) {
			long total = tt.execute(new TransactionCallback<Long>() {
				@Override
				public Long doInTransaction(TransactionStatus theStatus) {
					return mySearchDao.count();
				}
			});
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
