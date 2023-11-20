package ca.uhn.fhir.jpa.api.pid;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Template for wrapping access to stream supplier in a try-with-resources block.
 */
class AutoClosingStreamTemplate<T> implements StreamTemplate<T> {
	private final Supplier<Stream<T>> myStreamQuery;

	AutoClosingStreamTemplate(Supplier<Stream<T>> theStreamQuery) {
		myStreamQuery = theStreamQuery;
	}

	@Nullable
	@Override
	public <R> R call(@Nonnull Function<Stream<T>, R> theCallback) {
		try (Stream<T> stream = myStreamQuery.get()) {
			return theCallback.apply(stream);
		}
	}
}
