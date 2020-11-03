package ca.uhn.fhir.jpa.cache;

import java.util.HashMap;
import java.util.Map;

public class ResourceVersionCache {
	private final Map<String, Long> myVersionMap = new HashMap<>();

	public void clear() {
		myVersionMap.clear();
	}

	/**
	 *
	 * @param theResourceId
	 * @param theVersion
	 * @return previous value
	 */
	public Long addOrUpdate(String theResourceId, Long theVersion) {
		return myVersionMap.put(theResourceId, theVersion);
	}

	public Long get(String theResourceId) {
		return myVersionMap.get(theResourceId);
	}

	public Long remove(String theResourceId) {
		return myVersionMap.remove(theResourceId);
	}
}
