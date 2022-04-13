package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.jpa.search.builder.ISearchQueryExecutor;
import org.hibernate.search.engine.search.query.SearchScroll;
import org.hibernate.search.engine.search.query.SearchScrollResult;

import java.util.Iterator;

public class SearchScrollQueryExecutorAdaptor implements ISearchQueryExecutor {
	private final SearchScroll<Long> myScroll;
	private Iterator<Long> myCurrentIterator;
	private SearchScrollResult<Long> myScrollResults;

	public SearchScrollQueryExecutorAdaptor(SearchScroll<Long> theScroll) {
		myScroll = theScroll;
		advanceNextScrollPage();
	}

	private void advanceNextScrollPage() {
		myScrollResults = myScroll.next();
		myCurrentIterator = myScrollResults.hits().iterator();
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
		// was this the last in the page?
		if (!myCurrentIterator.hasNext()) {
			advanceNextScrollPage();
		}
		return result;
	}
}
