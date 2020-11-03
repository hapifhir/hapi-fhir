package ca.uhn.fhir.jpa.searchparam.cache;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.Map;

public class VersionChangeCache {
	private final Map<IIdType, Long> myVersionMap = new HashMap<>();

	public void clear() {
		myVersionMap.clear();
	}

	public Long addOrUpdate(IIdType theIIdType, Long theVersion) {
		return myVersionMap.put(theIIdType, theVersion);
	}

	public Long get(IIdType theIIdType) {
		return myVersionMap.get(theIIdType);
	}

	public Long remove(IIdType theIIdType) {
		return myVersionMap.remove(theIIdType);
	}
}
