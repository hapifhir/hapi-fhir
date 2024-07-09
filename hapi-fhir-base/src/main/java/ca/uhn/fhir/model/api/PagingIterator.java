/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.model.api;

import ca.uhn.fhir.i18n.Msg;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * This paging iterator only works with already ordered queries
 */
public class PagingIterator<T> implements Iterator<T> {

	public interface PageFetcher<T> {
		void fetchNextPage(int thePageIndex, int theBatchSize, Consumer<T> theConsumer);
	}

	static final int DEFAULT_PAGE_SIZE = 100;

	private int myPage;

	private boolean myIsFinished;

	private final LinkedList<T> myCurrentBatch = new LinkedList<>();

	private final PageFetcher<T> myFetcher;

	private final int myPageSize;

	public PagingIterator(PageFetcher<T> theFetcher) {
		this(DEFAULT_PAGE_SIZE, theFetcher);
	}

	public PagingIterator(int thePageSize, PageFetcher<T> theFetcher) {
		assert thePageSize > 0 : "Page size must be a positive value";
		myFetcher = theFetcher;
		myPageSize = thePageSize;
	}

	@Override
	public boolean hasNext() {
		fetchNextBatch();

		return !myCurrentBatch.isEmpty();
	}

	@Override
	public T next() {
		fetchNextBatch();

		if (myCurrentBatch.isEmpty()) {
			throw new NoSuchElementException(Msg.code(2098) + " Nothing to fetch");
		}

		return myCurrentBatch.remove(0);
	}

	private void fetchNextBatch() {
		if (!myIsFinished && myCurrentBatch.isEmpty()) {
			myFetcher.fetchNextPage(myPage, myPageSize, myCurrentBatch::add);
			myPage++;
			myIsFinished = myCurrentBatch.size() < myPageSize;
		}
	}
}
