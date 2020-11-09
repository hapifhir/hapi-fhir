package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Immutable copy of current resource versions read from the repository
 */
public class ResourceVersionMap {
	private final Map<IdDt, String> myMap = new HashMap<>();
	private ResourceVersionMap() {}

	public static ResourceVersionMap fromResourceIds(List<ResourceTable> theEntities) {
		ResourceVersionMap retval = new ResourceVersionMap();
		theEntities.forEach(entity -> retval.add(entity.getIdDt()));
		return retval;
	}

	private void add(IIdType theId) {
		IdDt id = new IdDt(theId);
		myMap.put(id.toUnqualifiedVersionless(), id.getVersionIdPart());
	}

	public String getVersion(IIdType theResourceId) {
		return myMap.get(new IdDt(theResourceId.toUnqualifiedVersionless()));
	}

	public int size() {
		return myMap.size();
	}

	public Set<IdDt> keySet() {
		return Collections.unmodifiableSet(myMap.keySet());
	}

	public String get(IdDt theId) {
		return myMap.get(theId);
	}

	public boolean containsKey(IdDt theId) {
		return myMap.containsKey(theId);
	}
}
