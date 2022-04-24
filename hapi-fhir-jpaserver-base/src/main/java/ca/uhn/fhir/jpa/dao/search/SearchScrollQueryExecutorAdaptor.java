package ca.uhn.fhir.jpa.dao.search;

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

import ca.uhn.fhir.jpa.search.builder.ISearchQueryExecutor;
import org.hibernate.search.engine.backend.common.DocumentReference;
import org.hibernate.search.engine.search.query.SearchScroll;
import org.hibernate.search.engine.search.query.SearchScrollResult;

import java.util.Iterator;

/**
 * Adapt Hibernate Search SearchScroll paging result to our ISearchQueryExecutor
 */
public class SearchScrollQueryExecutorAdaptor implements ISearchQueryExecutor {
	private final SearchScroll<Long> myScroll;
	private Iterator<Long> myCurrentIterator;

	public SearchScrollQueryExecutorAdaptor(SearchScroll<Long> theScroll) {
		myScroll = theScroll;
		advanceNextScrollPage();
	}

	/**
	 * Advance one page (i.e. SearchScrollResult).
	 * Note: the last page will have 0 hits.
	 */
	private void advanceNextScrollPage() {
		SearchScrollResult<Long> scrollResults = myScroll.next();
		myCurrentIterator = scrollResults.hits().iterator();
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
		// was this the last in the current scroll page?
		if (!myCurrentIterator.hasNext()) {
			advanceNextScrollPage();
		}
		return result;
	}
}
