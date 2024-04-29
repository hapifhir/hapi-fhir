package ca.uhn.fhir.util;

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

import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * This utility takes an input collection, breaks it up into chunks of a
 * given maximum chunk size, and then passes those chunks to a consumer for
 * processing. Use this to break up large tasks into smaller tasks.
 *
 * @since 6.6.0
 * @param <T> The type for the chunks
 */
public class TaskChunker<T> {

	public void chunk(Collection<T> theInput, int theChunkSize, Consumer<List<T>> theBatchConsumer) {
		List<T> input;
		if (theInput instanceof List) {
			input = (List<T>) theInput;
		} else {
			input = new ArrayList<>(theInput);
		}
		for (int i = 0; i < input.size(); i += theChunkSize) {
			int to = i + theChunkSize;
			to = Math.min(to, input.size());
			List<T> batch = input.subList(i, to);
			theBatchConsumer.accept(batch);
		}
	}

	@Nonnull
	public <T> Stream<List<T>> chunk(Stream<T> theStream, int theChunkSize) {
		return StreamUtil.partition(theStream, theChunkSize);
	}
}
