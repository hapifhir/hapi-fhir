package ca.uhn.fhir.jpa.search.builder;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
