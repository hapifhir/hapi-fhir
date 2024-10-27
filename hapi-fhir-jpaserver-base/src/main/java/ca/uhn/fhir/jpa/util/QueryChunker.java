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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.util.TaskChunker;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * As always, Oracle can't handle things that other databases don't mind.. In this
 * case it doesn't like more than ~1000 IDs in a single load, so we break this up
 * if it's lots of IDs. I suppose maybe we should be doing this as a join anyhow
 * but this should work too. Sigh.
 */
public class QueryChunker<T> extends TaskChunker<T> {

	public void chunk(Collection<T> theInput, Consumer<List<T>> theBatchConsumer) {
		chunk(theInput, SearchBuilder.getMaximumPageSize(), theBatchConsumer);
	}

	public Stream<List<T>> chunk(Stream<T> theStream) {
		return chunk(theStream, SearchBuilder.getMaximumPageSize());
	}
}
