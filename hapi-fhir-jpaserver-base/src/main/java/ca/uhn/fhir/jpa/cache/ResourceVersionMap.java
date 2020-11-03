package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.Map;

public class ResourceVersionMap implements IResourceVersionMap {
	private final Map<String, Long> myMap = new HashMap<>();

	@Override
	public Long getVersion(IIdType theResourceId) {
		return myMap.get(theResourceId.toUnqualifiedVersionless().toString());
	}

	@Override
	public int size() {
		return myMap.size();
	}

	public void add(IIdType theId) {
		myMap.put(theId.toUnqualifiedVersionless().toString(), theId.getVersionIdPartAsLong());
	}

	@Override
	public long populateInto(ResourceVersionCache theResourceVersionCache, IVersionChangeConsumer theConsumer) {
		long count = 0;
		for (String id : myMap.keySet()) {
			Long previousValue = theResourceVersionCache.addOrUpdate(id, myMap.get(id));
			// FIXME KHS this is clunky.  Maybe we should switch our map keys back to IIdTypes...
			String newId = id + "/_history/" + myMap.get(id);
			if (previousValue == null) {
				theConsumer.accept(new IdDt(newId), BaseResourceMessage.OperationTypeEnum.CREATE);
				++count;
			} else if (!myMap.get(id).equals(previousValue)) {
				theConsumer.accept(new IdDt(newId), BaseResourceMessage.OperationTypeEnum.UPDATE);
				++count;
			}
		}
		// FIXME KBD compare other direction for DELETE
		return count;
	}
}
