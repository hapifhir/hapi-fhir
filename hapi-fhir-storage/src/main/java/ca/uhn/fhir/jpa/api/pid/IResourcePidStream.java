package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface IResourcePidStream {
	void visitStream(Consumer<Stream<TypedResourcePid>> theCallback);

	RequestPartitionId getRequestPartitionId();
}
