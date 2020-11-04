package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ResourceVersionMap {
	private final Map<IdDt, String> myMap = new HashMap<>();

	public String getVersion(IIdType theResourceId) {
		return myMap.get(new IdDt(theResourceId.toUnqualifiedVersionless()));
	}

	public int size() {
		return myMap.size();
	}

	public void add(IIdType theId) {
		IdDt id = new IdDt(theId);
		myMap.put(id.toUnqualifiedVersionless(), id.getVersionIdPart());
	}

	public Set<IdDt> keySet() {
		return myMap.keySet();
	}

	public String get(IdDt theId) {
		return myMap.get(theId);
	}

	public boolean containsKey(IdDt theId) {
		return myMap.containsKey(theId);
	}
}
