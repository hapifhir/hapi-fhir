package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;

import java.util.List;
import java.util.function.Consumer;

/**
 * As always, Oracle can't handle things that other databases don't mind.. In this
 * case it doesn't like more than ~1000 IDs in a single load, so we break this up
 * if it's lots of IDs. I suppose maybe we should be doing this as a join anyhow
 * but this should work too. Sigh.
 */
public class QueryChunker<T> {

	public void chunk(List<T> theInput, Consumer<List<T>> theBatchConsumer) {
		for (int i = 0; i < theInput.size(); i += SearchBuilder.getMaximumPageSize()) {
			int to = i + SearchBuilder.getMaximumPageSize();
			to = Math.min(to, theInput.size());
			List<T> batch = theInput.subList(i, to);
			theBatchConsumer.accept(batch);
		}
	}

}
