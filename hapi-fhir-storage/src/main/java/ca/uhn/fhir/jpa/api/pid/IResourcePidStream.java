package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Wrapper for a query result stream.
 */
public interface IResourcePidStream {
	<T> T visitStream(Function<Stream<TypedResourcePid>, T> theCallback);

	default void visitStreamNoResult(Consumer<Stream<TypedResourcePid>> theCallback) {
		visitStream(theStream -> {
			theCallback.accept(theStream);
			return null;
		});
	}

	/**
	 * The partition info for the query.
	 */
	RequestPartitionId getRequestPartitionId();
}
