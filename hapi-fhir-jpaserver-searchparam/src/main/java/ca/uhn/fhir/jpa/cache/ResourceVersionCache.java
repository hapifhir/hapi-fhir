package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This maintains a mapping of resource id to resource version.  We cache these in order to
 * detect resources that were modified on remote servers in our cluster.
 * Note that even though it stores an internal Map of Maps, you can access an internal Map directly
 * by providing an IdDt, since the String key in the top-level Map is defined by IdDt.getResourceType().
 */
public class ResourceVersionCache {
	private final Map<String, Map<IIdType, String>> myVersionMap = new HashMap<>();

	public void clear() {
		myVersionMap.forEach((k, v) -> v.clear());
		myVersionMap.clear();
	}

	/**
	 * @param theResourceId
	 * @param theVersion
	 * @return previous value
	 */
	public String addOrUpdate(IIdType theResourceId, String theVersion) {
		Map<IIdType, String> entryByTypeMap = myVersionMap.computeIfAbsent(theResourceId.getResourceType(), key -> new HashMap<>());
		String previousMapEntry = entryByTypeMap.put(new IdDt(theResourceId).toVersionless(), theVersion);
		return previousMapEntry;
	}

	public String getVersionForResourceId(IIdType theResourceId) {
		Map<IIdType, String> entryByTypeMap = myVersionMap.computeIfAbsent(theResourceId.getResourceType(), key -> new HashMap<>());
		return entryByTypeMap.get(theResourceId);
	}

	@Nonnull
	public Map<IIdType, String> getMapForResourceName(String theResourceName) {
		Map<IIdType, String> entryByTypeMap = myVersionMap.computeIfAbsent(theResourceName, key -> new HashMap<>());
		return entryByTypeMap;
	}

	public String removeResourceId(IIdType theResourceId) {
		Map<IIdType, String> entryByTypeMap = myVersionMap.get(theResourceId.getResourceType());
		if (entryByTypeMap == null) {
			return null;
		} else {
			return entryByTypeMap.remove(new IdDt(theResourceId));
		}
	}

	public Set<String> keySet() {
		return myVersionMap.keySet();
	}

	public void initialize(ResourceVersionMap theResourceVersionMap) {
		for (IIdType key : theResourceVersionMap.keySet()) {
			Map<IIdType, String> entryByTypeMap = myVersionMap.computeIfAbsent(key.getResourceType(), k -> new HashMap<>());
			entryByTypeMap.put(key, theResourceVersionMap.get(key));
		}
	}

	public void listenerRemoved(IResourceChangeListener theResourceChangeListener) {
		// FIXME KHS How do we clear the Cache for a specific ResourceType if we
		//           don't know what ResourceType this particular Listener was for ?
	}

	public boolean hasEntriesForResourceName(String theResourceName) {
		return myVersionMap.containsKey(theResourceName);
	}

	public void removeCacheForResourceName(String theResourceName) {
		myVersionMap.remove(theResourceName);
	}
}
