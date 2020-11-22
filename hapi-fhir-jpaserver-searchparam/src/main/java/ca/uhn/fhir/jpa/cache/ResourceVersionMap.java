package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This immutable map holds a copy of current resource versions read from the repository.
 */
public class ResourceVersionMap {
	private final Set<IIdType> mySourceIds = new HashSet<>();
	private final Map<IIdType, String> myMap = new HashMap<>();
	private ResourceVersionMap() {}

	public static ResourceVersionMap fromResourceTableEntities(List<ResourceTable> theEntities) {
		ResourceVersionMap retval = new ResourceVersionMap();
		theEntities.forEach(entity -> retval.add(entity.getIdDt()));
		return retval;
	}

	private void add(IIdType theId) {
		IdDt id = new IdDt(theId);
		mySourceIds.add(id);
		myMap.put(id.toUnqualifiedVersionless(), id.getVersionIdPart());
	}

	public String getVersion(IIdType theResourceId) {
		return myMap.get(new IdDt(theResourceId.toUnqualifiedVersionless()));
	}

	public int size() {
		return myMap.size();
	}

	public Set<IIdType> keySet() {
		return Collections.unmodifiableSet(myMap.keySet());
	}

	public Set<IIdType> getSourceIds() {
		return Collections.unmodifiableSet(mySourceIds);
	}

	public String get(IIdType theId) {
		return myMap.get(theId);
	}

	public boolean containsKey(IIdType theId) {
		return myMap.containsKey(theId);
	}
}
