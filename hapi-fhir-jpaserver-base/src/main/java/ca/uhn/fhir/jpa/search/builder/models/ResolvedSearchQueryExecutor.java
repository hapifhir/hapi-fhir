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
package ca.uhn.fhir.jpa.search.builder.models;

import ca.uhn.fhir.jpa.search.builder.ISearchQueryExecutor;
import jakarta.annotation.Nonnull;

import java.util.Iterator;
import java.util.List;

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
