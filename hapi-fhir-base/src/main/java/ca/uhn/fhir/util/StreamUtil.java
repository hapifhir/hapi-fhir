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
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(partition, 0), false);
	}
}
