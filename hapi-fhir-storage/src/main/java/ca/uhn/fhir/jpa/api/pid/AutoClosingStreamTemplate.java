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
	private final Supplier<Stream<T>> theStreamQuery;

	AutoClosingStreamTemplate(Supplier<Stream<T>> theTheStreamQuery) {
		theStreamQuery = theTheStreamQuery;
	}

	@Nullable
	@Override
	public <R> R call(@Nonnull Function<Stream<T>, R> theCallback) {
		try (Stream<T> stream = theStreamQuery.get()) {
			return theCallback.apply(stream);
		}
	}
}
