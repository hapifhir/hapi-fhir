package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.Map;

public class ResourceVersionCache {
	private final Map<IdDt, String> myVersionMap = new HashMap<>();

	public void clear() {
		myVersionMap.clear();
	}

	/**
	 *
	 * @param theResourceId
	 * @param theVersion
	 * @return previous value
	 */
	public String addOrUpdate(IIdType theResourceId, String theVersion) {
		return myVersionMap.put(new IdDt(theResourceId), theVersion);
	}

	public String get(IIdType theResourceId) {
		return myVersionMap.get(new IdDt(theResourceId));
	}

	public String remove(IIdType theResourceId) {
		return myVersionMap.remove(new IdDt(theResourceId));
	}

	public void clearForUnitTest() {
		myVersionMap.clear();
	}
}
