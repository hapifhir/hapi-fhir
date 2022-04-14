package ca.uhn.fhir.jpa.search.builder;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

public class SearchQueryExecutors {

	public static ISearchQueryExecutor limited(ISearchQueryExecutor theExecutor, long theLimit) {
		Validate.isTrue(theLimit >= 0, "limit must be non-negative");

		return new ISearchQueryExecutor() {
			long myCount = 0;

			@Override
			public void close() {
				theExecutor.close();
			}

			@Override
			public boolean hasNext() {
				return theExecutor.hasNext() && myCount < theLimit;
			}

			@Override
			public Long next() {
				myCount += 1;
				return theExecutor.next();
			}
		};
	}

	@Nonnull
	public static ISearchQueryExecutor from(List<Long> rawPids) {
		return new ResolvedSearchQueryExecutor(rawPids);
	}

	/**
	 * Adapt bare Iterator to our internal query interface.
	 */
	static class ResolvedSearchQueryExecutor implements ISearchQueryExecutor {
		private final Iterator<Long> myIterator;

		ResolvedSearchQueryExecutor(Iterable<Long> theIterable) {
			this(theIterable.iterator());
		}

		ResolvedSearchQueryExecutor(Iterator<Long> theIterator) {
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

	static public ISearchQueryExecutor from(Iterator<ResourcePersistentId> theIterator) {
		return new ResourcePersistentIdQueryAdaptor(theIterator);
	}

	static public ISearchQueryExecutor from(Iterable<ResourcePersistentId> theIterable) {
		return new ResourcePersistentIdQueryAdaptor(theIterable.iterator());
	}

	static class ResourcePersistentIdQueryAdaptor implements ISearchQueryExecutor {
		final Iterator<ResourcePersistentId> myIterator;

		ResourcePersistentIdQueryAdaptor(Iterator<ResourcePersistentId> theIterator) {
			myIterator = theIterator;
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
			ResourcePersistentId next = myIterator.next();
			return next==null?null:next.getIdAsLong();
		}

	}
}
