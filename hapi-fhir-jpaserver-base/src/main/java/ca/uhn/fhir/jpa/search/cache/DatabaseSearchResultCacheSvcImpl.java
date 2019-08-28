package ca.uhn.fhir.jpa.search.cache;

import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchResult;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.*;

import static ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl.toPage;

public class DatabaseSearchResultCacheSvcImpl implements ISearchResultCacheSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(DatabaseSearchResultCacheSvcImpl.class);

	@Autowired
	private ISearchResultDao mySearchResultDao;

	@Override
	@Transactional(Transactional.TxType.REQUIRED)
	public List<Long> fetchResultPids(Search theSearch, int theFrom, int theTo) {
		final Pageable page = toPage(theFrom, theTo);
		if (page == null) {
			return Collections.emptyList();
		}

		List<Long> retVal = mySearchResultDao
			.findWithSearchPid(theSearch.getId(), page)
			.getContent();

		ourLog.trace("fetchResultPids for range {}-{} returned {} pids", theFrom, theTo, retVal.size());

		return new ArrayList<>(retVal);
	}

	@Override
	@Transactional(Transactional.TxType.REQUIRED)
	public List<Long> fetchAllResultPids(Search theSearch) {
		List<Long> retVal = mySearchResultDao.findWithSearchPidOrderIndependent(theSearch.getId());
		ourLog.trace("fetchAllResultPids returned {} pids", retVal.size());
		return retVal;
	}

	@Override
	@Transactional(Transactional.TxType.REQUIRED)
	public void storeResults(Search theSearch, List<Long> thePreviouslyStoredResourcePids, List<Long> theNewResourcePids) {
		List<SearchResult> resultsToSave = Lists.newArrayList();

		ourLog.trace("Storing {} results with {} previous for search", theNewResourcePids.size(), thePreviouslyStoredResourcePids.size());

		int order = thePreviouslyStoredResourcePids.size();
		for (Long nextPid : theNewResourcePids) {
			SearchResult nextResult = new SearchResult(theSearch);
			nextResult.setResourcePid(nextPid);
			nextResult.setOrder(order);
			resultsToSave.add(nextResult);
			ourLog.trace("Saving ORDER[{}] Resource {}", order, nextResult.getResourcePid());

			order++;
		}

		mySearchResultDao.saveAll(resultsToSave);
	}

	@VisibleForTesting
	void setSearchDaoResultForUnitTest(ISearchResultDao theSearchResultDao) {
		mySearchResultDao = theSearchResultDao;
	}


}
