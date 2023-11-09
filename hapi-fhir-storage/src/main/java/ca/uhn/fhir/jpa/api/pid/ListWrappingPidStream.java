package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;

import java.util.stream.Stream;

public class ListWrappingPidStream implements IResourcePidStream {
	private final IResourcePidList myList;
	public ListWrappingPidStream(IResourcePidList theList) {
		myList = theList;
	}

	@Override
	public Stream<TypedResourcePid> getTypedResourcePidStream() {
		return myList.getTypedResourcePids().stream();
	}

	@Override
	public RequestPartitionId getRequestPartitionId() {
		return myList.getRequestPartitionId();
	}
}
