package ca.uhn.fhir.jpa.search.cache;

import ca.uhn.fhir.jpa.entity.Search;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseSearchResultCacheSvcImpl implements ISearchResultCacheSvc {

	@Autowired
	private PlatformTransactionManager myTxManager;

	private ConcurrentHashMap<Long, Date> myUnsyncedLastUpdated = new ConcurrentHashMap<>();

	@Override
	public void updateSearchLastReturned(Search theSearch, Date theDate) {
		myUnsyncedLastUpdated.put(theSearch.getId(), theDate);
	}


	@Override
	@Scheduled(fixedDelay = 10 * DateUtils.MILLIS_PER_SECOND)
	public void flushLastUpdated() {
		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.execute(t->{
			for (Iterator<Map.Entry<Long, Date>> iter = myUnsyncedLastUpdated.entrySet().iterator(); iter.hasNext(); ) {
				Map.Entry<Long, Date> next = iter.next();
				flushLastUpdated(next.getKey(), next.getValue());
				iter.remove();
			}
			return null;
		});
	}

	protected abstract void flushLastUpdated(Long theSearchId, Date theLastUpdated);


}
