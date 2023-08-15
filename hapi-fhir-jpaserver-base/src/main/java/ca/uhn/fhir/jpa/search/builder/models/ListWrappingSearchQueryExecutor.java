package ca.uhn.fhir.jpa.search.builder.models;

import ca.uhn.fhir.jpa.search.builder.ISearchQueryExecutor;

import java.util.Iterator;
import java.util.List;

/**
 * An implementation of ISearchQueryExecutor that wraps a simple list.
 */
public class ListWrappingSearchQueryExecutor implements ISearchQueryExecutor {

	private final Iterator<Long> myIterator;

	public ListWrappingSearchQueryExecutor(List<Long> theList) {
		myIterator = theList.iterator();
	}

	@Override
	public void close() {

	}

	@Override
	public boolean hasNext() {
		return myIterator.hasNext();
	}

	@Override
	public Long next() {
		return myIterator.next();
	}
}
