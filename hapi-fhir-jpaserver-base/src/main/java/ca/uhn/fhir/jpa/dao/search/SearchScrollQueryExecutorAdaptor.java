package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.jpa.search.builder.ISearchQueryExecutor;
import org.hibernate.search.engine.backend.common.DocumentReference;
import org.hibernate.search.engine.search.query.SearchScroll;
import org.hibernate.search.engine.search.query.SearchScrollResult;

import java.util.Iterator;

/**
 * Adapt Hibernate Search SearchScroll paging result to our ISearchQueryExecutor
 */
public class SearchScrollQueryExecutorAdaptor implements ISearchQueryExecutor {
	private final SearchScroll<Long> myScroll;
	private Iterator<Long> myCurrentIterator;

	public SearchScrollQueryExecutorAdaptor(SearchScroll<Long> theScroll) {
		myScroll = theScroll;
		advanceNextScrollPage();
	}

	/**
	 * Advance one page (i.e. SearchScrollResult).
	 * Note: the last page will have 0 hits.
	 */
	private void advanceNextScrollPage() {
		SearchScrollResult<Long> scrollResults = myScroll.next();
		myCurrentIterator = scrollResults.hits().iterator();
	}

	@Override
	public void close() {
		myScroll.close();
	}

	@Override
	public boolean hasNext() {
		return myCurrentIterator.hasNext();
	}

	@Override
	public Long next() {
		Long result = myCurrentIterator.next();
		// was this the last in the current scroll page?
		if (!myCurrentIterator.hasNext()) {
			advanceNextScrollPage();
		}
		return result;
	}
}
