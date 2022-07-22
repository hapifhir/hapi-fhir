package ca.uhn.fhir.jpa.api.model;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PersistentIdToForcedIdMap {
	private final Map<ResourcePersistentId, Optional<String>> myResourcePersistentIdOptionalMap;

	public PersistentIdToForcedIdMap(Map<ResourcePersistentId, Optional<String>> theResourcePersistentIdOptionalMap){
		myResourcePersistentIdOptionalMap = theResourcePersistentIdOptionalMap;
	}

	public Set<String> getResolvedResourceIds() {

		return myResourcePersistentIdOptionalMap.entrySet().stream()
			.map(this::getResolvedPid)
			.collect(Collectors.toSet());
	}

	private String getResolvedPid(Map.Entry<ResourcePersistentId, Optional<String>> entry) {
		//If the result of the translation is an empty optional, it means there is no forced id, and we can use the PID as the resource ID.
		return entry.getValue().isPresent() ? entry.getValue().get() : entry.getKey().toString();
	}

	public Optional<String> get(ResourcePersistentId theResourcePersistentId) {
		return myResourcePersistentIdOptionalMap.get(theResourcePersistentId);
	}

	public Map<ResourcePersistentId, Optional<String>> getResourcePersistentIdOptionalMap(){
		return myResourcePersistentIdOptionalMap;
	}
}
