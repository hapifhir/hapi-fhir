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
package ca.uhn.fhir.jpa.dao.data;

import java.util.Objects;

/**
 * Record for search result returning the PK of a Search, and the number of associated SearchResults
 */
public class SearchIdAndResultSize {
	/** Search PK */
	public final long searchId;
	/** Number of SearchResults attached */
	public final int size;

	public SearchIdAndResultSize(long theSearchId, Integer theSize) {
		searchId = theSearchId;
		size = Objects.requireNonNullElse(theSize, 0);
	}
}
