package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CdsServiceRequestContextJson extends BaseCdsServiceJson implements IModelJson {
	private Map<String, Object> myMap;

	public String getString(String theKey) {
		if (myMap == null) {
			return null;
		}
		return (String) myMap.get(theKey);
	}

	public List<String> getArray(String theKey) {
		if (myMap == null) {
			return null;
		}
		return (List<String>) myMap.get(theKey);
	}

	public IBaseResource getResource(String theKey) {
		if (myMap == null) {
			return null;
		}
		return (IBaseResource) myMap.get(theKey);
	}

	public void put(String theKey, Object theValue) {
		if (myMap == null) {
			myMap = new LinkedHashMap<>();
		}
		myMap.put(theKey, theValue);
	}

	public Set<String> getKeys() {
		return myMap.keySet();
	}

	public Object get(String theKey) {
		return myMap.get(theKey);
	}

	public boolean containsKey(String theKey) {
		return myMap.containsKey(theKey);
	}
}
