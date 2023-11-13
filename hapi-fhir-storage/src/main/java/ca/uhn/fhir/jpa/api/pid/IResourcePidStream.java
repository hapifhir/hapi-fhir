package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public interface IResourcePidStream {
	<T> T visitStream(Function<Stream<TypedResourcePid>, T> theCallback);

	default void visitStream(Consumer<Stream<TypedResourcePid>> theCallback) {
		visitStream((s) -> {
			theCallback.accept(s);
			return null;
		});
	}

	RequestPartitionId getRequestPartitionId();
}
