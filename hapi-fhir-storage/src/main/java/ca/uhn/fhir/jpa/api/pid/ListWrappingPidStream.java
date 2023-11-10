package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class ListWrappingPidStream implements IResourcePidStream {
	private final IResourcePidList myList;

	public ListWrappingPidStream(IResourcePidList theList) {
		myList = theList;
	}

	public Stream<TypedResourcePid> getTypedResourcePidStream() {
		return myList.getTypedResourcePids().stream();
	}

	@Override
	public void visitStream(Consumer<Stream<TypedResourcePid>> theCallback) {
		theCallback.accept(getTypedResourcePidStream());
	}

	@Override
	public RequestPartitionId getRequestPartitionId() {
		return myList.getRequestPartitionId();
	}
}
