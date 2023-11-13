package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;

import java.util.function.Function;
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
	public <T> T visitStream(Function<Stream<TypedResourcePid>, T> theCallback) {
		return theCallback.apply(getTypedResourcePidStream());
	}

	@Override
	public RequestPartitionId getRequestPartitionId() {
		return myList.getRequestPartitionId();
	}
}
