package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;

import java.util.function.Function;
import java.util.stream.Stream;

public class TypedResourceStream implements IResourcePidStream {

	private final RequestPartitionId myRequestPartitionId;
	private final StreamTemplate<TypedResourcePid> myStreamSupplier;

	public TypedResourceStream(
			RequestPartitionId theRequestPartitionId, StreamTemplate<TypedResourcePid> theStreamSupplier) {
		myRequestPartitionId = theRequestPartitionId;
		myStreamSupplier = theStreamSupplier;
	}

	@Override
	public <T> T visitStream(Function<Stream<TypedResourcePid>, T> theCallback) {
		return myStreamSupplier.call(theCallback);
	}

	@Override
	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}
}
