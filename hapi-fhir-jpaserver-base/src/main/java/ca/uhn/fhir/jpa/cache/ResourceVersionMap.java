package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.Map;

public class ResourceVersionMap implements IResourceVersionMap {
	private final Map<IdDt, String> myMap = new HashMap<>();

	@Override
	public String getVersion(IIdType theResourceId) {
		return myMap.get(new IdDt(theResourceId));
	}

	@Override
	public int size() {
		return myMap.size();
	}

	public void add(IIdType theId) {
		IdDt id = new IdDt(theId);
		myMap.put(id.toUnqualifiedVersionless(), id.getVersionIdPart());
	}

	@Override
	public long populateInto(ResourceVersionCache theResourceVersionCache, IVersionChangeListener theListener) {
		long count = 0;
		for (IdDt id : myMap.keySet()) {
			String previousValue = theResourceVersionCache.addOrUpdate(id, myMap.get(id));
			IdDt newId = id.withVersion(myMap.get(id));
			if (previousValue == null) {
				theListener.handleCreate(newId);
				++count;
			} else if (!myMap.get(id).equals(previousValue)) {
				theListener.handleUpdate(newId);
				++count;
			}
		}
		// FIXME KBD compare other direction for DELETE
		return count;
	}
}
