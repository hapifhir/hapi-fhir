package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourcePersistentIdMap {
	private final Map<IIdType, ResourcePersistentId> myMap = new HashMap<>();

	public static ResourcePersistentIdMap fromResourcePersistentIds(List<ResourcePersistentId> theResourcePersistentIds) {
		ResourcePersistentIdMap retval = new ResourcePersistentIdMap();
		theResourcePersistentIds.forEach(retval::add);
		return retval;
	}

	private void add(ResourcePersistentId theResourcePersistentId) {
		IIdType id = theResourcePersistentId.getAssociatedResourceId();
		myMap.put(id.toUnqualifiedVersionless(), theResourcePersistentId);
	}

	public boolean containsKey(IIdType theId) {
		return myMap.containsKey(theId.toUnqualifiedVersionless());
	}

	public ResourcePersistentId getResourcePersistentId(IIdType theId) {
		return myMap.get(theId.toUnqualifiedVersionless());
	}

	public boolean isEmpty() {
		return myMap.isEmpty();
	}

	public int size() {
		return myMap.size();
	}
}
