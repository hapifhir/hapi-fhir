package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;

import java.util.stream.Stream;

public interface IResourcePidStream {
	Stream<TypedResourcePid> getTypedResourcePidStream();

	RequestPartitionId getRequestPartitionId();
}
