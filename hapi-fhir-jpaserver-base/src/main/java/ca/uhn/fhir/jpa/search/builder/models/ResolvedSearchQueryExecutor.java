package ca.uhn.fhir.jpa.search.builder.models;

import ca.uhn.fhir.jpa.search.builder.ISearchQueryExecutor;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;

public class ResolvedSearchQueryExecutor implements ISearchQueryExecutor {
	private final Iterator<Long> myIterator;

	public ResolvedSearchQueryExecutor(Iterable<Long> theIterable) {
		this(theIterable.iterator());
	}

	public ResolvedSearchQueryExecutor(Iterator<Long> theIterator) {
		myIterator = theIterator;
	}

	@Nonnull
	public static ResolvedSearchQueryExecutor from(List<Long> rawPids) {
		return new ResolvedSearchQueryExecutor(rawPids);
	}

	@Override
	public boolean hasNext() {
		return myIterator.hasNext();
	}

	@Override
	public Long next() {
		return myIterator.next();
	}

	@Override
	public void close() {
		// empty
	}
}
