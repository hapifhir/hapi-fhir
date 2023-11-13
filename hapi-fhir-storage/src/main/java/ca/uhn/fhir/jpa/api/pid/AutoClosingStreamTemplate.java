package ca.uhn.fhir.jpa.api.pid;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

class AutoClosingStreamTemplate<T> implements StreamTemplate<T> {
	private final Supplier<Stream<T>> theStreamQuery;

	AutoClosingStreamTemplate(Supplier<Stream<T>> theTheStreamQuery) {
		theStreamQuery = theTheStreamQuery;
	}

	@Nonnull
	@Override
	public <R> R call(Function<Stream<T>, R> theCallback) {
		try (Stream<T> s = theStreamQuery.get()) {
			return theCallback.apply(s);
		}
	}
}
