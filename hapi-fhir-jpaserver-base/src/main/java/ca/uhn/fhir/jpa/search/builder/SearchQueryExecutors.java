/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.search.builder.models.ResolvedSearchQueryExecutor;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;

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

	public static ISearchQueryExecutor from(Iterator<JpaPid> theIterator) {
		return new JpaPidQueryAdaptor(theIterator);
	}

	public static ISearchQueryExecutor from(Iterable<JpaPid> theIterable) {
		return new JpaPidQueryAdaptor(theIterable.iterator());
	}

	static class JpaPidQueryAdaptor implements ISearchQueryExecutor {
		final Iterator<JpaPid> myIterator;

		JpaPidQueryAdaptor(Iterator<JpaPid> theIterator) {
			myIterator = theIterator;
		}

		@Override
		public void close() {}

		@Override
		public boolean hasNext() {
			return myIterator.hasNext();
		}

		@Override
		public Long next() {
			JpaPid next = myIterator.next();
			return next == null ? null : next.getId();
		}
	}
}
