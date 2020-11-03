package ca.uhn.fhir.jpa.searchparam.cache;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.Map;

public class ResourceVersionMap implements IResourceVersionMap {
	private final Map<IIdType, Long> myMap = new HashMap<>();

	@Override
	public Long getVersion(IIdType theResourceId) {
		return myMap.get(theResourceId);
	}

	@Override
	public int size() {
		return myMap.size();
	}
}
