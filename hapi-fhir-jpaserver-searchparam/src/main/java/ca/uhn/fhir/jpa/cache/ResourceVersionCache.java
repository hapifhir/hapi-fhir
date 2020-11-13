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
	private final Map<String, Map<IdDt, String>> myVersionMap = new HashMap<>();

	public void clear() {
		myVersionMap.forEach((k, v) -> v.clear());
		myVersionMap.clear();
	}

	/**
	 *
	 * @param theResourceId
	 * @param theVersion
	 * @return previous value
	 */
	public String addOrUpdate(IIdType theResourceId, String theVersion) {
		Map<IdDt, String> entryByTypeMap = myVersionMap.computeIfAbsent(theResourceId.getResourceType(), key -> new HashMap<IdDt, String>());
		String previousMapEntry = entryByTypeMap.put(new IdDt(theResourceId).toVersionless(), theVersion);
		return previousMapEntry;
	}

	public String get(IdDt theResourceId) {
		Map<IdDt, String> entryByTypeMap = myVersionMap.computeIfAbsent(theResourceId.getResourceType(), key -> new HashMap<IdDt, String>());
		return entryByTypeMap.get(theResourceId);
	}

	public String get(IIdType theResourceId) {
		Map<IdDt, String> entryByTypeMap = myVersionMap.computeIfAbsent(theResourceId.getResourceType(), key -> new HashMap<IdDt, String>());
		return entryByTypeMap.get(new IdDt(theResourceId));
	}

	@Nonnull
	public Map<IdDt, String> getMap(String theResourceType) {
		Map<IdDt, String> entryByTypeMap = myVersionMap.computeIfAbsent(theResourceType, key -> new HashMap<IdDt, String>());
		return entryByTypeMap;
	}

	public String remove(IIdType theResourceId) {
		Map<IdDt, String> entryByTypeMap = myVersionMap.computeIfAbsent(theResourceId.getResourceType(), key -> new HashMap<IdDt, String>());
		String previousValue = entryByTypeMap.remove(new IdDt(theResourceId));
		return previousValue;
	}

	public Set<String> keySet() {
		return myVersionMap.keySet();
	}

	public void initialize(ResourceVersionMap theResourceVersionMap) {
		for (IdDt key : theResourceVersionMap.keySet()) {
			Map<IdDt, String> entryByTypeMap = myVersionMap.computeIfAbsent(key.getResourceType(), k -> new HashMap<IdDt, String>());
			entryByTypeMap.put(key, theResourceVersionMap.get(key));
		}
	}

	public void listenerRemoved(IResourceChangeListener theResourceChangeListener) {
		// FIXME KBD How do we clear the Cache for a specific ResourceType if we
		//           don't know what ResourceType this particular Listener was for ?
	}
}
