package ca.uhn.fhir.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Streams;
import com.google.common.collect.UnmodifiableIterator;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;

public class StreamUtil {
	/** Static util class */
	private StreamUtil() {}

	public static <T> Stream<List<T>> partition(Stream<T> theStream, int theChunkSize) {
		Spliterator<T> spliterator = theStream.spliterator();

		Iterator<T> iterator = Spliterators.iterator(spliterator);
		UnmodifiableIterator<List<T>> partition = Iterators.partition(iterator, theChunkSize);
		return Streams.stream(partition);
	}
}
