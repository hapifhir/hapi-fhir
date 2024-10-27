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
package ca.uhn.fhir.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtil {
	/** Static util class */
	private StreamUtil() {}

	/**
	 * Chunk the stream into Lists of size theChunkSize.
	 * The last chunk will be smaller unless the stream size is evenly divisible.
	 * Closes the underlying stream when done.
	 *
	 * @param theStream the input stream
	 * @param theChunkSize the chunk size.
	 * @return a stream of chunks
	 */
	public static <T> Stream<List<T>> partition(Stream<T> theStream, int theChunkSize) {
		Spliterator<T> spliterator = theStream.spliterator();
		Iterator<T> iterator = Spliterators.iterator(spliterator);
		UnmodifiableIterator<List<T>> partition = Iterators.partition(iterator, theChunkSize);

		// we could be fancier here and support parallel, and sizes; but serial-only is fine for now.
		Spliterator<List<T>> partitionedSpliterator = Spliterators.spliteratorUnknownSize(partition, 0);
		Stream<List<T>> result = StreamSupport.stream(partitionedSpliterator, false);

		// we lose close() via the Iterator.  Add it back.
		return result.onClose(theStream::close);
	}
}
