package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;

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
	private final Map<IIdType, String> myVersionMap = new HashMap<>();

	public void clear() {
		myVersionMap.clear();
	}

	/**
	 * @param theResourceId
	 * @param theVersion
	 * @return previous value
	 */
	public String put(IIdType theResourceId, String theVersion) {
		return myVersionMap.put(new IdDt(theResourceId).toVersionless(), theVersion);
	}

	public String getVersionForResourceId(IIdType theResourceId) {
		return myVersionMap.get(new IdDt(theResourceId));
	}

	public String removeResourceId(IIdType theResourceId) {
		return myVersionMap.remove(new IdDt(theResourceId));
	}

	public void initialize(ResourceVersionMap theResourceVersionMap) {
		for (IIdType resourceId : theResourceVersionMap.keySet()) {
			myVersionMap.put(resourceId, theResourceVersionMap.get(resourceId));
		}
	}

	public int size() {
		return myVersionMap.size();
	}

	public Set<IIdType> keySet() {
		return myVersionMap.keySet();
	}
}
