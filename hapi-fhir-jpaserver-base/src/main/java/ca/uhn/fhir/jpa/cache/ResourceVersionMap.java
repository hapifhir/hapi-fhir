package ca.uhn.fhir.jpa.cache;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.Map;

public class ResourceVersionMap implements IResourceVersionMap {
	private final Map<String, Long> myMap = new HashMap<>();

	@Override
	public Long getVersion(IIdType theResourceId) {
		return myMap.get(theResourceId.toUnqualifiedVersionless().toString());
	}

	@Override
	public int size() {
		return myMap.size();
	}

	public void add(IIdType theId) {
		myMap.put(theId.toUnqualifiedVersionless().toString(), theId.getVersionIdPartAsLong());
	}
}
