/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.util;

import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * This class collects items from a stream to a given limit and know whether there are
 * still more items beyond that limit.
 *
 * @param <T> the type of object being streamed
 */
public class StopLimitAccumulator<T> {
	private final boolean isTruncated;
	private final List<T> myList;

	private StopLimitAccumulator(List<T> theList, boolean theIsTruncated) {
		myList = Collections.unmodifiableList(theList);
		isTruncated = theIsTruncated;
	}

	public static <T> StopLimitAccumulator<T> fromStreamAndLimit(@Nonnull Stream<T> theItemStream, long theLimit) {
		assert theLimit > 0;
		AtomicBoolean isBeyondLimit = new AtomicBoolean(false);
		List<T> accumulator = new ArrayList<>();

		theItemStream
				.limit(theLimit + 1) // Fetch one extra item to see if there are any more items past our limit
				.forEach(item -> {
					if (accumulator.size() < theLimit) {
						accumulator.add(item);
					} else {
						isBeyondLimit.set(true);
					}
				});
		return new StopLimitAccumulator<>(accumulator, isBeyondLimit.get());
	}

	public boolean isTruncated() {
		return isTruncated;
	}

	public List<T> getItemList() {
		return myList;
	}
}
