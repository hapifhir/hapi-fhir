package ca.uhn.fhir.jpa.search;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.annotations.VisibleForTesting;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchIncludeDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.Search;

/**
 * Deletes old searches
 */
public class StaleSearchDeletingSvcImpl implements IStaleSearchDeletingSvc {
	public static final long DEFAULT_CUTOFF_SLACK = 10 * DateUtils.MILLIS_PER_SECOND;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StaleSearchDeletingSvcImpl.class);
	

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

	protected void deleteSearch(final Search next) {
		TransactionTemplate tt = new TransactionTemplate(myTransactionManager);
		tt.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				Search searchToDelete = mySearchDao.findOne(next.getId());
				ourLog.info("Expiring stale search {} / {}", searchToDelete.getId(), searchToDelete.getUuid());
				mySearchIncludeDao.deleteForSearch(searchToDelete.getId());
				mySearchResultDao.deleteForSearch(searchToDelete.getId());
				mySearchDao.delete(searchToDelete);
			}
		});
	}
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void pollForStaleSearchesAndDeleteThem() {
		
		long cutoffMillis = myDaoConfig.getExpireSearchResultsAfterMillis();
		if (myDaoConfig.getReuseCachedSearchResultsForMillis() != null) {
			cutoffMillis = Math.max(cutoffMillis, myDaoConfig.getReuseCachedSearchResultsForMillis());
		}
		Date cutoff = new Date((System.currentTimeMillis() - cutoffMillis) - myCutoffSlack);
		
		ourLog.debug("Searching for searches which are before {}", cutoff);

		Collection<Search> toDelete = mySearchDao.findWhereLastReturnedBefore(cutoff);
		if (!toDelete.isEmpty()) {

			for (final Search next : toDelete) {
				
				ourLog.info("Deleting search {} - Created[{}] -- Last returned[{}]", next.getUuid(), next.getCreated(), next.getSearchLastReturned());
				
				deleteSearch(next);
			}

			ourLog.info("Deleted {} searches, {} remaining", toDelete.size(), mySearchDao.count());
		}
	}

	@Scheduled(fixedDelay = DEFAULT_CUTOFF_SLACK)
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Override
	public synchronized void schedulePollForStaleSearches() {
		if (!myDaoConfig.isSchedulingDisabled()) {
			if (myDaoConfig.isExpireSearchResults()) {
				pollForStaleSearchesAndDeleteThem();
			}
		}
	}

	@VisibleForTesting
	public void setCutoffSlackForUnitTest(long theCutoffSlack) {
		myCutoffSlack = theCutoffSlack;
	}

}
